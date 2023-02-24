package org.xyg.eshop.main.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.base.DBEntity;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.*;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.springrabbit.flow.core.dto.TaskCompleteDto;
import org.springrabbit.flow.core.entity.Activity;
import org.springrabbit.flow.core.entity.RabbitFlow;
import org.springrabbit.flow.core.feign.IFlowOpenClient;
import org.springrabbit.flow.core.utils.CommentTypeEnum;
import org.springrabbit.system.entity.ApproverRoles;
import org.springrabbit.system.entity.Dict;
import org.springrabbit.system.feign.IDictClient;
import org.springrabbit.system.feign.ISysClient;
import org.springrabbit.system.user.entity.User;
import org.springrabbit.system.user.feign.IUserClient;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.ehop.common.constants.EshopConstants;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.*;
import org.xyg.eshop.main.excel.ProductAdjustmentRecordExcel;
import org.xyg.eshop.main.mapper.ProductAdjustmentRecordMapper;
import org.xyg.eshop.main.service.*;
import org.xyg.eshop.main.util.ProcessUtils;
import org.xyg.eshop.main.vo.ProductAdjustmentRecordVO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProductAdjustmentRecordServiceImpl extends BaseServiceImpl<ProductAdjustmentRecordMapper, ProductAdjustmentRecord> implements IProductAdjustmentRecordService {

	private final IProductAdjustmentRecordLinesService linesService;
	private final AutoIncrementIDGenerator valueService;
	private final IProductPriceListService productPriceListService;
	private final IProductPriceListLinesService priceListLinesService;
	private final IDictClient dictClient;
	private final IUserClient userClient;
	private final IProductModelLinesService modelLinesService;
	private final IFlowOpenClient flowOpenClient;
	private final ExecutorService executorService;

	private final ISysClient sysClient;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<ProductAdjustmentRecordVO> saveProductAdjustmentRecord(ProductAdjustmentRecordVO productAdjustmentRecordVO){

		// 新增需要设置项目编码
		if (Func.isEmpty(productAdjustmentRecordVO.getDocnum())) {
			String docnum = getDocnum();
			productAdjustmentRecordVO.setDocnum(docnum);
		}

		if (productAdjustmentRecordVO.getId() == null){
			productAdjustmentRecordVO.setStatus(EshopConstants.STATUS_SAVE);
			save(productAdjustmentRecordVO);
		} else {
			updateById(productAdjustmentRecordVO);
		}

		// 获取价目表行表数据更新到调整单行表
		if (StringUtil.isNotBlank(productAdjustmentRecordVO.getPriceListLineIds())){
			productAdjustmentRecordVO.setProductAdjustmentRecordLinesList(getAdjustmentRecordLinesByPriceListLineIds(productAdjustmentRecordVO.getPriceListLineIds()));
		}

		// 保存调整单行表数据
		List<ProductAdjustmentRecordLines> productAdjustmentRecordLinesList = productAdjustmentRecordVO.getProductAdjustmentRecordLinesList();
		if (CollectionUtil.isNotEmpty(productAdjustmentRecordLinesList)){
			for (ProductAdjustmentRecordLines productAdjustmentRecordLines : productAdjustmentRecordLinesList) {
				productAdjustmentRecordLines.setHeadId(productAdjustmentRecordVO.getId());
				linesService.saveOrUpdate(productAdjustmentRecordLines);
			}
		}

		return R.data(productAdjustmentRecordVO);
	}

	/**
	 * 调整单更新价目表
	 * @param id 调整单头表id
	 */
	/*private void updateAdjustmentRecordToPriceList (Long id){

		ProductAdjustmentRecord productAdjustmentRecord = getById(id);

		if (productAdjustmentRecord == null){
			throw new ServiceException("当前调整单数据为空: id " + id);
		}

		log.info("内销更新价目表开始: -> {} " , id);

		// 价目表id
		Long priceListId = productAdjustmentRecord.getPriceListId();
		if (priceListId == null){
			throw new ServiceException("当前价目表为空");
		}

		// 查询调整单的行表数据
		List<ProductAdjustmentRecordLines> productAdjustmentRecordLinesList = linesService.lambdaQuery().eq(ProductAdjustmentRecordLines::getHeadId, id).list();
		if (CollectionUtil.isEmpty(productAdjustmentRecordLinesList)){
			return;
		}

		// 查询价目表行表数据
		List<ProductPriceListLines> priceListLinesList = priceListLinesService.lambdaQuery().eq(ProductPriceListLines::getHeadId, priceListId).list();

		Set<String> adjustmentRecordMaterialCodeSet = new HashSet<>();

		// 调整单行表
		for (ProductAdjustmentRecordLines productAdjustmentRecordLines : productAdjustmentRecordLinesList) {
			// 是否新增标识
			Boolean addFlag = true;
			// 调整单的物料编码
			String adjustmentRecordMaterialCode = productAdjustmentRecordLines.getMaterialCode();
			// 如果当前调整单的物料编码已经更新或修改过,直接跳过当前循环
			if (adjustmentRecordMaterialCodeSet.contains(adjustmentRecordMaterialCode)){
				continue;
			}

			adjustmentRecordMaterialCodeSet.add(adjustmentRecordMaterialCode);

			if (CollectionUtil.isNotEmpty(priceListLinesList)) {
				// 价目表行表
				for (ProductPriceListLines priceListLines : priceListLinesList) {
					// 价目表行表id
					Long priceListLinesId = priceListLines.getId();

					// 价目表行表物料编码
					String materialCode = priceListLines.getMaterialCode();

					if (StringUtil.isNotBlank(materialCode) && Objects.equals(materialCode,adjustmentRecordMaterialCode)) {
						// 如果价目表中有当前物料编码,不做新增操作
						addFlag = false;

						// 匹配价目表数据
						LambdaUpdateChainWrapper<ProductPriceListLines> productPriceListLinesLambdaUpdateChainWrapper = priceListLinesService.lambdaUpdate()
							.eq(ProductPriceListLines::getHeadId, priceListId)
							.eq(ProductPriceListLines::getMaterialCode, adjustmentRecordMaterialCode);
						// 更新价目表数据
						productPriceListLinesLambdaUpdateChainWrapper
							.set(ProductPriceListLines::getPrice1, productAdjustmentRecordLines.getPrice1())
							.set(ProductPriceListLines::getBatchPrice, productAdjustmentRecordLines.getBatchPrice())
							.set(ProductPriceListLines::getQuantity, productAdjustmentRecordLines.getQuantity())
							.update();

						break;
					}
				}
			}

			if (addFlag){
				ProductPriceListLines productPriceListLines = new ProductPriceListLines();
				productPriceListLines.setHeadId(priceListId);
				productPriceListLines.setBrand(productAdjustmentRecordLines.getBrand());
				productPriceListLines.setModel(productAdjustmentRecordLines.getModel());
				productPriceListLines.setPosition(productAdjustmentRecordLines.getPosition());
				productPriceListLines.setProductModel(productAdjustmentRecordLines.getProductModel());
				productPriceListLines.setPrice1(productAdjustmentRecordLines.getPrice1());
				productPriceListLines.setBatchPrice(productAdjustmentRecordLines.getBatchPrice());
				productPriceListLines.setForeignCurrency(productAdjustmentRecordLines.getForeignCurrency());
				productPriceListLines.setQuantity(productAdjustmentRecordLines.getQuantity());
				productPriceListLines.setPrice2(productAdjustmentRecordLines.getPrice2());
				productPriceListLines.setFullContainerPrice(productAdjustmentRecordLines.getFullContainerPrice());
				productPriceListLines.setMaterialCode(productAdjustmentRecordLines.getMaterialCode());
				productPriceListLines.setItemChDescription(productAdjustmentRecordLines.getItemChDescription());
				productPriceListLines.setSpecifications(productAdjustmentRecordLines.getSpecifications());
				productPriceListLines.setDiagonal(productAdjustmentRecordLines.getDiagonal());
				productPriceListLines.setProductType(productAdjustmentRecordLines.getProductType());
				productPriceListLines.setMachiningType(productAdjustmentRecordLines.getMachiningType());
				productPriceListLines.setPackingQuantity1(productAdjustmentRecordLines.getPackingQuantity1());
				productPriceListLines.setOemModel(productAdjustmentRecordLines.getOemModel());
				productPriceListLines.setWidth(productAdjustmentRecordLines.getWidth());
				productPriceListLines.setLength(productAdjustmentRecordLines.getLength());
				// 保存价目表行表数据
				priceListLinesService.save(productPriceListLines);
			}
		}

	}*/

	@Override
	public void updateAdjustmentRecordToPriceList(Long id) throws IOException {
		// 获取头表数据
		ProductAdjustmentRecord productAdjustmentRecord = getById(id);
		if (productAdjustmentRecord == null){
			throw new ServiceException("未查询到当前调整单信息");
		}
		Long priceListId = productAdjustmentRecord.getPriceListId();
		if (priceListId == null){
			throw new ServiceException("当前价目表为空");
		}
		// 获取行表数据
		List<ProductAdjustmentRecordLines> productAdjustmentRecordLinesList = linesService.lambdaQuery().eq(ProductAdjustmentRecordLines::getHeadId, id).list();
		if (CollectionUtil.isEmpty(productAdjustmentRecordLinesList)) {
			return;
		}
		// 获取到所有的本厂型号
		List<String> productModels = productAdjustmentRecordLinesList.stream().map(ProductAdjustmentRecordLines::getProductModel).filter(StringUtil::isNotBlank).distinct().collect(Collectors.toList());
		// 获取到所有需要调整的价目表数据
		List<ProductPriceListLines> priceListLines = priceListLinesService.lambdaQuery()
			.eq(ProductPriceListLines::getHeadId, priceListId)
			.in(ProductPriceListLines::getMaterialCode, productModels)
			.list();
		if (CollectionUtil.isEmpty(priceListLines)) {
			List<ProductPriceListLines> productPriceListLines = new ArrayList<>();
			for (ProductAdjustmentRecordLines productAdjustmentRecordLine : productAdjustmentRecordLinesList) {
				ProductPriceListLines productPriceListLine = new ProductPriceListLines();
				productPriceListLine.setHeadId(priceListId);
				productPriceListLine.setBrand(productAdjustmentRecordLine.getBrand());
				productPriceListLine.setModel(productAdjustmentRecordLine.getModel());
				productPriceListLine.setPosition(productAdjustmentRecordLine.getPosition());
				productPriceListLine.setProductModel(productAdjustmentRecordLine.getProductModel());
				productPriceListLine.setPrice1(productAdjustmentRecordLine.getPrice1());
				productPriceListLine.setBatchPrice(productAdjustmentRecordLine.getBatchPrice());
				productPriceListLine.setForeignCurrency(productAdjustmentRecordLine.getForeignCurrency());
				productPriceListLine.setQuantity(productAdjustmentRecordLine.getQuantity());
				productPriceListLine.setPrice2(productAdjustmentRecordLine.getPrice2());
				productPriceListLine.setFullContainerPrice(productAdjustmentRecordLine.getFullContainerPrice());
				productPriceListLine.setMaterialCode(productAdjustmentRecordLine.getMaterialCode());
				productPriceListLine.setItemChDescription(productAdjustmentRecordLine.getItemChDescription());
				productPriceListLine.setSpecifications(productAdjustmentRecordLine.getSpecifications());
				productPriceListLine.setDiagonal(productAdjustmentRecordLine.getDiagonal());
				productPriceListLine.setProductType(productAdjustmentRecordLine.getProductType());
				productPriceListLine.setMachiningType(productAdjustmentRecordLine.getMachiningType());
				productPriceListLine.setPackingQuantity1(productAdjustmentRecordLine.getPackingQuantity1());
				productPriceListLine.setOemModel(productAdjustmentRecordLine.getOemModel());
				productPriceListLine.setWidth(productAdjustmentRecordLine.getWidth());
				productPriceListLine.setLength(productAdjustmentRecordLine.getLength());
				productPriceListLine.setCreatedBy(productAdjustmentRecordLine.getCreatedBy());
				productPriceListLine.setLastUpdatedBy(productAdjustmentRecordLine.getLastUpdatedBy());
				productPriceListLine.setBasePrice(productAdjustmentRecordLine.getBasePrice());
				productPriceListLine.setRebatePoint(productAdjustmentRecordLine.getRebatePoint());
				productPriceListLine.setStatus(productAdjustmentRecordLine.getStatus());
				productPriceListLines.add(productPriceListLine);
			}
			if (CollectionUtil.isNotEmpty(productPriceListLines)) {
				// 更新到价目中去
				if (priceListLinesService.saveBatch(productPriceListLines)) {
					// 同步erp
//					priceListSync.syncPriceList(id, priceListId, productPriceListLines);
				}
			}
			return;
		}
		// 记录需要修改或新增的数据
		List<ProductPriceListLines> productPriceListLines = new ArrayList<>();
		// 遍历调整单行数据
		for (ProductAdjustmentRecordLines productAdjustmentRecordLine : productAdjustmentRecordLinesList) {
			// 获取到需要调整的价目表数
			List<ProductPriceListLines> linesList = priceListLines.stream().filter(v -> v.getProductModel().equals(productAdjustmentRecordLine.getProductModel())).collect(Collectors.toList());
			// 不为空则证明只是更新价目表数据
			if (CollectionUtil.isNotEmpty(linesList)) {
				for (ProductPriceListLines line : linesList) {
					line.setPrice1(productAdjustmentRecordLine.getPrice1());
					line.setSuggestedPrice(productAdjustmentRecordLine.getSuggestedPrice());
					line.setSettlementPrice(productAdjustmentRecordLine.getSettlementPrice());
					line.setLastUpdatedBy(productAdjustmentRecordLine.getLastUpdatedBy());
					line.setBasePrice(productAdjustmentRecordLine.getBasePrice());
					line.setGrossRate(productAdjustmentRecordLine.getGrossRate());
					line.setStatus(productAdjustmentRecordLine.getStatus());
					line.setEstimatedProfit(productAdjustmentRecordLine.getEstimatedProfit());
					line.setPlatformEstimatedProfit(productAdjustmentRecordLine.getPlatformEstimatedProfit());
					line.setPlatformReceiptPrice(productAdjustmentRecordLine.getPlatformReceiptPrice());
					line.setPlatformSuggestedPrice(productAdjustmentRecordLine.getPlatformSuggestedPrice());
					line.setCompanyEstimatedProfit(productAdjustmentRecordLine.getCompanyEstimatedProfit());
					line.setRemark(productAdjustmentRecordLine.getRemark());
					// 将需要更新的数据记录到集合中去
					productPriceListLines.add(line);
				}
			} else {
				ProductPriceListLines productPriceListLine = BeanUtil.copyProperties(productAdjustmentRecordLine, ProductPriceListLines.class);
				productPriceListLine.setId(null);
				productPriceListLine.setCreatedBy(null);
				productPriceListLine.setCreationDate(null);
				productPriceListLine.setCreateDept(null);
				productPriceListLine.setLastUpdatedBy(null);
				productPriceListLine.setLastUpdateDate(null);
				productPriceListLine.setLastUpdateLogin(null);
				/*ProductPriceListLines productPriceListLine = new ProductPriceListLines();
				productPriceListLine.setHeadId(priceListId);
				productPriceListLine.setBrand(productAdjustmentRecordLine.getBrand());
				productPriceListLine.setModel(productAdjustmentRecordLine.getModel());
				productPriceListLine.setPosition(productAdjustmentRecordLine.getPosition());
				productPriceListLine.setProductModel(productAdjustmentRecordLine.getProductModel());
				productPriceListLine.setPrice1(productAdjustmentRecordLine.getPrice1());
				productPriceListLine.setBatchPrice(productAdjustmentRecordLine.getBatchPrice());
				productPriceListLine.setForeignCurrency(productAdjustmentRecordLine.getForeignCurrency());
				productPriceListLine.setQuantity(productAdjustmentRecordLine.getQuantity());
				productPriceListLine.setPrice2(productAdjustmentRecordLine.getPrice2());
				productPriceListLine.setFullContainerPrice(productAdjustmentRecordLine.getFullContainerPrice());
				productPriceListLine.setMaterialCode(productAdjustmentRecordLine.getMaterialCode());
				productPriceListLine.setItemChDescription(productAdjustmentRecordLine.getItemChDescription());
				productPriceListLine.setSpecifications(productAdjustmentRecordLine.getSpecifications());
				productPriceListLine.setDiagonal(productAdjustmentRecordLine.getDiagonal());
				productPriceListLine.setProductType(productAdjustmentRecordLine.getProductType());
				productPriceListLine.setMachiningType(productAdjustmentRecordLine.getMachiningType());
				productPriceListLine.setPackingQuantity1(productAdjustmentRecordLine.getPackingQuantity1());
				productPriceListLine.setOemModel(productAdjustmentRecordLine.getOemModel());
				productPriceListLine.setWidth(productAdjustmentRecordLine.getWidth());
				productPriceListLine.setLength(productAdjustmentRecordLine.getLength());
				productPriceListLine.setCreatedBy(productAdjustmentRecordLine.getCreatedBy());
				productPriceListLine.setLastUpdatedBy(productAdjustmentRecordLine.getLastUpdatedBy());
				productPriceListLine.setBasePrice(productAdjustmentRecordLine.getBasePrice());
				productPriceListLine.setRebatePoint(productAdjustmentRecordLine.getRebatePoint());
				productPriceListLine.setStatus(productAdjustmentRecordLine.getStatus());
				productPriceListLine.setSuggestedPrice(productAdjustmentRecordLine.getSuggestedPrice());
				productPriceListLine.setSettlementPrice(productAdjustmentRecordLine.getSettlementPrice());
				productPriceListLine.setGrossRate(productAdjustmentRecordLine.getGrossRate());
				productPriceListLine.setEstimatedProfit(productAdjustmentRecordLine.getEstimatedProfit());
				productPriceListLine.setPlatformEstimatedProfit(productAdjustmentRecordLine.getPlatformEstimatedProfit());
				productPriceListLine.setPlatformReceiptPrice(productAdjustmentRecordLine.getPlatformReceiptPrice());
				productPriceListLine.setPlatformSuggestedPrice(productAdjustmentRecordLine.getPlatformSuggestedPrice());
				productPriceListLine.setRemark(productAdjustmentRecordLine.getRemark());*/
				productPriceListLines.add(productPriceListLine);
			}
		}
		if (CollectionUtil.isNotEmpty(productPriceListLines)) {
			// 更新本地库数据
			if (priceListLinesService.saveOrUpdateBatch(productPriceListLines)) {
				// 同步erp
//				priceListSync.syncPriceList(id, priceListId, productPriceListLines);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ProductAdjustmentRecordVO submit(ProductAdjustmentRecordVO productAdjustmentRecordVO){

		if (CollectionUtil.isEmpty(productAdjustmentRecordVO.getProductAdjustmentRecordLinesList())){
			throw new ServiceException("当前行数据为空,提交失败");
		}

		// 生成单号
		if (Func.isEmpty(productAdjustmentRecordVO.getDocnum())) {
			String docnum = getDocnum();
			productAdjustmentRecordVO.setDocnum(docnum);
		}

		if (productAdjustmentRecordVO.getId() == null){
			save(productAdjustmentRecordVO);
		} else {
			updateById(productAdjustmentRecordVO);
		}

		// 保存行表信息
		List<ProductAdjustmentRecordLines> productAdjustmentRecordLinesList = productAdjustmentRecordVO.getProductAdjustmentRecordLinesList();
		if (CollectionUtil.isNotEmpty(productAdjustmentRecordLinesList)){
			for (ProductAdjustmentRecordLines productAdjustmentRecordLines : productAdjustmentRecordLinesList) {
				productAdjustmentRecordLines.setHeadId(productAdjustmentRecordVO.getId());
				linesService.saveOrUpdate(productAdjustmentRecordLines);
			}
		}

		return productAdjustmentRecordVO;
	}

	@Override
	public R<ProductAdjustmentRecordVO> getDetail(Long id){
		ProductAdjustmentRecordVO productAdjustmentRecordVO = new ProductAdjustmentRecordVO();
		// 获取调整单头表
		ProductAdjustmentRecord productAdjustmentRecord = getById(id);

		if (productAdjustmentRecord != null){
			productAdjustmentRecordVO = BeanUtil.copyProperties(productAdjustmentRecord, ProductAdjustmentRecordVO.class);

			// 获取客户名称
			String storefrontType = productAdjustmentRecord.getStorefrontType();
//			productAdjustmentRecordVO.setPartyName(getPartyById(partyId));

			// 翻译内外销
			Integer type = productAdjustmentRecordVO.getType();
			productAdjustmentRecordVO.setTypeName(getTypeName(type));

			// 申请人名称
			Long createdBy = productAdjustmentRecordVO.getCreatedBy();
			productAdjustmentRecordVO.setCreatedByName(getCreatedByName(createdBy));

			// 状态翻译
			Integer status = productAdjustmentRecordVO.getStatus();
			productAdjustmentRecordVO.setStatusName(getStautsName(status));

			// 区域翻译
			String region = productAdjustmentRecordVO.getRegion();
			productAdjustmentRecordVO.setRegionName(getRegionName(region));

			// erp状态名称
			String erpStatus = productAdjustmentRecordVO.getErpStatus();
			productAdjustmentRecordVO.setErpStatusName(getErpStatusName(erpStatus));

			// 获取调整单行表
			List<ProductAdjustmentRecordLines> productAdjustmentRecordLinesList = linesService.lambdaQuery()
				.eq(ProductAdjustmentRecordLines::getHeadId, id)
				.orderByDesc(DBEntity::getLastUpdateDate)
				.list();

			if (CollectionUtil.isNotEmpty(productAdjustmentRecordLinesList)){
				for (ProductAdjustmentRecordLines productAdjustmentRecordLines : productAdjustmentRecordLinesList) {
					// 获取价目表
					getOldLinesPriceByPriceListId(productAdjustmentRecordLines,productAdjustmentRecordVO.getPriceListId());
				}
			}

			// 返回行表数据
			productAdjustmentRecordVO.setProductAdjustmentRecordLinesList(productAdjustmentRecordLinesList);
		}
		return R.data(productAdjustmentRecordVO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ProductAdjustmentRecordVO importerProductAdjustmentRecord(List<ProductAdjustmentRecordExcel> data, Long headId){
		ProductAdjustmentRecordVO productAdjustmentRecordVO = new ProductAdjustmentRecordVO();

		if (CollectionUtil.isNotEmpty(data)){
			ProductPriceList productPriceList = productPriceListService.lambdaQuery()
				.eq(DBEntity::getId,headId)
				.select(DBEntity::getId,ProductPriceList::getPriceListName,ProductPriceList::getType,ProductPriceList::getExpiryDate,
					ProductPriceList::getRegion,ProductPriceList::getStorefrontType,ProductPriceList::getCurrency,ProductPriceList::getExchangeRate)
				.one();
			if (productPriceList != null){
				productAdjustmentRecordVO = BeanUtil.copyProperties(productPriceList,ProductAdjustmentRecordVO.class);
				productAdjustmentRecordVO.setId(null);
				productAdjustmentRecordVO.setStatus(EshopConstants.STATUS_SAVE);
				productAdjustmentRecordVO.setPriceListId(productPriceList.getId());

				// 新增需要设置调整单单号
				if (Func.isEmpty(productAdjustmentRecordVO.getDocnum())) {
					String docnum = getDocnum();
					productAdjustmentRecordVO.setDocnum(docnum);
				}
				save(productAdjustmentRecordVO);
			}

			for (ProductAdjustmentRecordExcel excel : data) {
				ProductAdjustmentRecordLines productAdjustmentRecordLines = BeanUtil.copyProperties(excel, ProductAdjustmentRecordLines.class);
				// 本厂型号
				String productModel = productAdjustmentRecordLines.getProductModel();
				// 根据本厂型号查询产品库信息
				ProductModelLines productModelLines = modelLinesService.lambdaQuery()
					.eq(ProductModelLines::getFactoryMode, productModel)
					.apply("rownum = 1")
					.one();

				if (productModelLines != null){
					productAdjustmentRecordLines.setHeadId(productAdjustmentRecordVO.getId());
					productAdjustmentRecordLines.setPosition(productModelLines.getLoadingPosition());
					productAdjustmentRecordLines.setMaterialCode(productModelLines.getMaterialCode());
					productAdjustmentRecordLines.setItemChDescription(productModelLines.getProductName());
					productAdjustmentRecordLines.setSpecifications(productModelLines.getSpecifications());
					productAdjustmentRecordLines.setDiagonal(productModelLines.getDiagonal());
					productAdjustmentRecordLines.setProductType(productModelLines.getProductType());
					productAdjustmentRecordLines.setMachiningType(productModelLines.getMachiningType());
					productAdjustmentRecordLines.setPackingQuantity1(productModelLines.getPackingQuantity1());
					productAdjustmentRecordLines.setOemModel(productModelLines.getOemModel());
					productAdjustmentRecordLines.setWidth(productModelLines.getWidth());
					productAdjustmentRecordLines.setLength(productModelLines.getLength());

					// 计算
					calculation(productAdjustmentRecordLines);

					linesService.save(productAdjustmentRecordLines);
				}
			}
		}
		return productAdjustmentRecordVO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<String> deleteLines(String ids){
		return R.status(linesService.deleteLogic(Func.toLongList(ids)));
	}

	@Override
	public R<IPage<ProductAdjustmentRecordVO>> getToPriceListPage(IPage<ProductAdjustmentRecordVO> page, Long id, Long priceListId,String docnum,String erpStatus){
		IPage<ProductAdjustmentRecordVO> toPriceListPage = baseMapper.getToPriceListPage(page, id, priceListId, docnum,erpStatus);
		List<ProductAdjustmentRecordVO> recordsList = toPriceListPage.getRecords();
		if (CollectionUtil.isNotEmpty(recordsList)){
			for (ProductAdjustmentRecordVO productAdjustmentRecordVO : recordsList) {
				// 翻译内外销
				Integer type = productAdjustmentRecordVO.getType();
				productAdjustmentRecordVO.setTypeName(getTypeName(type));

				// 翻译客户
				String storefrontType = productAdjustmentRecordVO.getStorefrontType();
//				productAdjustmentRecordVO.setPartyName(getPartyById(partyId));

				// 状态翻译
				Integer status = productAdjustmentRecordVO.getStatus();
				productAdjustmentRecordVO.setStatusName(getStautsName(status));

				// 查询申请人名称
				Long createdBy = productAdjustmentRecordVO.getCreatedBy();
				productAdjustmentRecordVO.setCreatedByName(getCreatedByName(createdBy));

				// 获取erp状态名称
				String erpStatus1 = productAdjustmentRecordVO.getErpStatus();
				productAdjustmentRecordVO.setErpStatusName(getErpStatusName(erpStatus1));
			}
		}
		return R.data(toPriceListPage);
	}

	/**
	 * 获取客户名称
	 * @param partyId 客户id
	 * @return 客户名称
	 */
	private String getPartyById(Long partyId){
		/*if (partyId != null){
			QbCustParties qbCustPotentialParties = custPartiesService.lambdaQuery().eq(DBEntity::getId, partyId).one();
			if (qbCustPotentialParties == null){
				R<CustParties> custPartiesR = custMainFeign.getPartyById(partyId);
				if (custPartiesR.isSuccess() && custPartiesR.getData() != null){
					return custPartiesR.getData().getPartyName();
				}
			} else {
				return qbCustPotentialParties.getPartyName();
			}
		}*/
		return null;
	}

	/**
	 * 获取内外销对应的字典value
	 * @param type 内外销
	 * @return 内外销字典value
	 */
	private String getTypeName(Integer type){
		if (type != null){
			R<Dict> dictR = dictClient.getDictInfo("quote_type", String.valueOf(type));
			if (dictR.isSuccess() && dictR.getData() != null){
				return dictR.getData().getDictValue();
			}
		}
		return null;
	}

	/**
	 * 获取状态对应的字典value
	 * @param status 状态
	 * @return 状态字典value值
	 */
	private String getStautsName(Integer status){
		if (status != null){
			R<Dict> dictR = dictClient.getDictInfo("crm2_doc_status", String.valueOf(status));
			if (dictR.isSuccess() && dictR.getData() != null){
				Dict dict = dictR.getData();
				return dict.getDictValue();
			}
		}
		return null;
	}

	/**
	 * 获取申请人名称
	 * @param createdBy 申请人id
	 * @return 申请人名称
	 */
	private String getCreatedByName(Long createdBy){
		if (createdBy != null){

			R<User> rInfo = userClient.userInfoById(createdBy);
			if (rInfo.isSuccess() && rInfo.getData() != null){
				User user = rInfo.getData();
				return user.getRealName();
			}
		}
		return null;
	}

	/**
	 * 获取区域名称
	 * @param region 区域编码
	 * @return 区域名称
	 */
	private String getRegionName(String region){
		/*if (StringUtil.isNotBlank(region)){
			SaleAreas saleAreas = saleAreasService.lambdaQuery()
				.eq(SaleAreas::getAreaCode, region)
				.select(SaleAreas::getId,SaleAreas::getAreaCode, SaleAreas::getAreaName)
				.one();
			if (saleAreas != null){
				return saleAreas.getAreaName();
			}
		}*/
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<String> delete(Long id){
		LambdaQueryWrapper<ProductAdjustmentRecordLines> deleteAdjustmentRecordLinesQW = new LambdaQueryWrapper<>();
		linesService.getBaseMapper().delete(deleteAdjustmentRecordLinesQW.eq(ProductAdjustmentRecordLines::getHeadId,id));
		baseMapper.deleteById(id);
		return R.status(true);
	}

	@Override
	public R<String> saveByProductIds(Long id,String productIds){
		// 查询产品信息
		List<ProductModelLines> productModelLinesList = modelLinesService.lambdaQuery().in(DBEntity::getId, JSON.parseArray(productIds)).list();

		if (CollectionUtil.isNotEmpty(productModelLinesList)){
			for (ProductModelLines productModelLines : productModelLinesList) {
				// 查询当前产品的物料编码是否存在于当前调整单
				Integer count = linesService.lambdaQuery()
					.eq(ProductAdjustmentRecordLines::getHeadId, id)
					.eq(ProductAdjustmentRecordLines::getProductModel, productModelLines.getFactoryMode())
					.count();

				// 添加调整单行表
				if (count == 0){
					ProductAdjustmentRecordLines productAdjustmentRecordLines = new ProductAdjustmentRecordLines();
					productAdjustmentRecordLines.setHeadId(id);
					productAdjustmentRecordLines.setProductModel(productModelLines.getFactoryMode());
					productAdjustmentRecordLines.setItemChDescription(productModelLines.getProductName());
					productAdjustmentRecordLines.setPosition(productModelLines.getLoadingPosition());
					productAdjustmentRecordLines.setSpecifications(productModelLines.getSpecifications());
					productAdjustmentRecordLines.setDiagonal(productModelLines.getDiagonal());
					productAdjustmentRecordLines.setProductType(productModelLines.getProductType());
					productAdjustmentRecordLines.setMachiningType(productModelLines.getMachiningType());
					productAdjustmentRecordLines.setPackingQuantity1(productModelLines.getPackingQuantity1());
					productAdjustmentRecordLines.setOemModel(productModelLines.getOemModel());
					productAdjustmentRecordLines.setMaterialCode(productModelLines.getMaterialCode());
					productAdjustmentRecordLines.setLength(productModelLines.getLength());
					productAdjustmentRecordLines.setWidth(productModelLines.getWidth());

					linesService.save(productAdjustmentRecordLines);
				}
			}
		}
		return R.status(true);
	}

	@Override
	public R<List<ProductAdjustmentRecordLines>> linesList(Long headId,
														   String productModel,
														   String position,
														   String brand,
														   String model,
														   String itemChDescription,
														   Integer machiningType,
														   Long maxWidth,
														   Long minWidth,
														   Long maxLength,
														   Long minLength,
														   String oemModel){
		LambdaQueryChainWrapper<ProductAdjustmentRecordLines> linesQW = linesService.lambdaQuery().eq(ProductAdjustmentRecordLines::getHeadId,headId);

		if (StringUtil.isNotBlank(productModel)){
			linesQW.like(ProductAdjustmentRecordLines::getProductModel,productModel);
		}

		if (StringUtil.isNotBlank(position)){
			linesQW.like(ProductAdjustmentRecordLines::getPosition,position);
		}

		if (StringUtil.isNotBlank(brand)){
			linesQW.like(ProductAdjustmentRecordLines::getBrand,brand);
		}

		if (StringUtil.isNotBlank(model)){
			linesQW.like(ProductAdjustmentRecordLines::getModel,model);
		}

		if (StringUtil.isNotBlank(itemChDescription)){
			linesQW.like(ProductAdjustmentRecordLines::getItemChDescription,itemChDescription);
		}

		if (machiningType != null){
			linesQW.eq(ProductAdjustmentRecordLines::getMachiningType,machiningType);
		}

		if (maxWidth != null){
			linesQW.le(ProductAdjustmentRecordLines::getWidth,maxWidth);
		}

		if (maxWidth != null){
			linesQW.le(ProductAdjustmentRecordLines::getWidth,maxWidth);
		}

		if (minWidth != null){
			linesQW.ge(ProductAdjustmentRecordLines::getWidth,minWidth);
		}

		if (maxLength != null){
			linesQW.le(ProductAdjustmentRecordLines::getLength,maxLength);
		}

		if (minLength != null){
			linesQW.ge(ProductAdjustmentRecordLines::getLength,minLength);
		}

		if (StringUtil.isNotBlank(oemModel)){
			linesQW.like(ProductAdjustmentRecordLines::getOemModel,oemModel);
		}

		linesQW.orderByDesc(DBEntity::getLastUpdateDate)
			.orderByDesc(DBEntity::getId);

		List<ProductAdjustmentRecordLines> linesList = linesQW.list();

		// 获取头表数据
		ProductAdjustmentRecord productAdjustmentRecord = getById(headId);

		if (CollectionUtil.isNotEmpty(linesList)){
			for (ProductAdjustmentRecordLines productAdjustmentRecordLines : linesList) {
				// 查询价目表
				getOldLinesPriceByPriceListId(productAdjustmentRecordLines,productAdjustmentRecord.getPriceListId());
			}
		}

		return R.data(linesList);
	}

	@Override
	public R<ProductAdjustmentRecordLines> linesUpdateById(ProductAdjustmentRecordLines productAdjustmentRecordLines){
		if (productAdjustmentRecordLines.getId() == null){
			return R.fail("id为空");
		}

		// 折扣点为空设置默认值
		if (productAdjustmentRecordLines.getRebatePoint() == null){
			productAdjustmentRecordLines.setRebatePoint(getRebatePointDefault());
		}

		// 计算
		if (productAdjustmentRecordLines.getBasePrice() != null){
			calculation(productAdjustmentRecordLines);
		}

		linesService.updateById(productAdjustmentRecordLines);
		return R.data(productAdjustmentRecordLines);
	}

	@Override
	public ProductAdjustmentRecordLines copySaveById(Long id){
		ProductAdjustmentRecordLines adjustmentRecordLines = linesService.lambdaQuery()
			.select(ProductAdjustmentRecordLines::getProductModel, ProductAdjustmentRecordLines::getItemChDescription, ProductAdjustmentRecordLines::getPosition,
				ProductAdjustmentRecordLines::getSpecifications, ProductAdjustmentRecordLines::getDiagonal, ProductAdjustmentRecordLines::getProductType,
				ProductAdjustmentRecordLines::getMachiningType, ProductAdjustmentRecordLines::getPackingQuantity1, ProductAdjustmentRecordLines::getOemModel,
				ProductAdjustmentRecordLines::getPrice1, ProductAdjustmentRecordLines::getBatchPrice, ProductAdjustmentRecordLines::getWidth,
				ProductAdjustmentRecordLines::getLength,ProductAdjustmentRecordLines::getHeadId,ProductAdjustmentRecordLines::getBasePrice,ProductAdjustmentRecordLines::getRebatePoint)
			.eq(DBEntity::getId, id)
			.one();

		if (adjustmentRecordLines == null){
			return null;
		}

		// 查询折扣点默认值
		if (adjustmentRecordLines.getRebatePoint() == null){
			adjustmentRecordLines.setRebatePoint(getRebatePointDefault());
		}

		// 计算
		calculation(adjustmentRecordLines);

		linesService.save(adjustmentRecordLines);

		return adjustmentRecordLines;
	}

	@Override
	public void startProcess(Long id){
		if(id == null) {
			return;
		}

		log.info("价目表:{},发起流程!", id);
		RabbitUser user = ProcessUtils.getUser();
		ProductAdjustmentRecord adjustmentRecord = getById(id);

		if (adjustmentRecord == null){
			log.info("价目表发起流程失败,未查询到价目表数据:{}",id);
			return;
		}

		// 如果状态为驳回,完成当前节点
		if (Objects.equals(adjustmentRecord.getStatus(),EshopConstants.STATUS_REJECTED)){
			log.info("内销价目表流程驳回后提交完成当前节点:{}",id);
			completeTaskByProcessInstanceId(adjustmentRecord.getProcessInstanceId());
			return;
		}

		// 发起人
		String initiator = ProcessUtils.ProcessConstant.ASSIGNEE_PREFIX + user.getUserId();
		// 流程定义key
		final String processDefinitionKey = "eshop_adjustment_record_approval";

		// 判断发起人和审批人是否相同,相同则不发起流程
		Boolean sameFlag = initiatorAndApproverSame(id,initiator,processDefinitionKey);
		if (sameFlag){
			return;
		}

		// 获取当前时间
		String nowDate = DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATETIME);

		// 添加流程变量
		Map<String, Object> variablesMap = new HashMap<>();
		//业务主键id
		variablesMap.put("data_id_", id);
		//流程标题
		variablesMap.put("business_name_", "价目表调整单:" + adjustmentRecord.getPriceListName() + "," + adjustmentRecord.getDocnum());
		//流程发起人
		variablesMap.put("createUserId", user.getUserId());
		//消息标题
		variablesMap.put("title_", "价目表调整单");
		//消息内容
		variablesMap.put("content_", user.getUserName() + "于" + nowDate + "新增一个价目表调整单:" + adjustmentRecord.getDocnum());
		//消息模板
		variablesMap.put("template_code_", "eshop_adjustment_record");
		//开始回调函数
		variablesMap.put("startCallbackMethod", "http://eshop-main/productAdjustmentRecord/flowInstanceExecutionStartCallback");
		//结束回调函数
		variablesMap.put("endCallbackMethod", "http://eshop-main/productAdjustmentRecord/flowInstanceExecutionEndCallback");


		// 调用流程发起方法
		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey(processDefinitionKey, initiator, true, "", variablesMap);
		// 保存流程id
		if (rabbitFlowR.isSuccess() && rabbitFlowR.getData() != null) {
			lambdaUpdate().eq(DBEntity::getId,id)
				.set(ProductAdjustmentRecord::getProcessInstanceId,rabbitFlowR.getData().getProcessInstanceId())
				.update();
		} else {
			log.error("流程绑定发起失败！,错误信息：{}", rabbitFlowR.getMsg());
			throw new RuntimeException("流程绑定发起失败");
		}
	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto){
		CallBackMethodResDto resDto = new CallBackMethodResDto();

		// 获取业务id
		Long id = Func.toLong(inDto.getBusinessId());
		// 获取当前节点id
		String activityId = inDto.getCurrentActivityId();

		if (Objects.equals(EShopMainConstant.DRAFT_NODE,activityId)){
			if (inDto.isCurrentActivityFromReject()){
				lambdaUpdate().eq(DBEntity::getId,id)
					.set(DBEntity::getStatus,EshopConstants.STATUS_REJECTED)
					.update();
			}
		} else {
			// 查询审批人条件
			ApproverRoles approverRoles = new ApproverRoles();
			approverRoles.setRoleCode(inDto.getDocumentation());
			approverRoles.setAttribute1(inDto.getProcessDefinitionKey());

			// 查询审批人
			List<String> candidateUsers = getApproverRoles(approverRoles);

			// 设置审批人
			resDto.setCandidateUsers(candidateUsers);
		}
		return resDto;
	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto){
		CallBackMethodResDto resDto = new CallBackMethodResDto();

		Long id = Func.toLong(inDto.getBusinessId());
		String activityId = inDto.getCurrentActivityId();

		if (inDto.isAbandon()){
			lambdaUpdate().eq(DBEntity::getId,id)
				.set(DBEntity::getStatus,EshopConstants.STATUS_DISUSE)
				.update();
			return resDto;
		}

		if (Objects.equals(EShopMainConstant.DRAFT_NODE,activityId)){
			lambdaUpdate().eq(DBEntity::getId,id)
				.set(DBEntity::getStatus,EshopConstants.STATUS_APPROVING)
				.update();
		} else if (Objects.equals(EShopMainConstant.END,activityId)){
			lambdaUpdate().eq(DBEntity::getId,id)
				.set(DBEntity::getStatus,EshopConstants.STATUS_NORMAL)
				.update();

			// 执行更新价目表
			executorUpdateAdjustmentRecordToPriceList(id);
		}

		return resDto;
	}

	/**
	 * 根据流程实例id完成当前节点
	 * @param processInstanceId 流程实id
	 */
	private void completeTaskByProcessInstanceId(String processInstanceId){
		R<List<Activity>> rRunningActivityNodes = flowOpenClient.getRunningActivityNodes(processInstanceId);
		if (rRunningActivityNodes.isSuccess() && CollectionUtil.isNotEmpty(rRunningActivityNodes.getData())){
			RabbitUser user = ProcessUtils.getUser();
			Activity activity = rRunningActivityNodes.getData().get(0);
			String taskId = activity.getTaskId();
			String actId = activity.getActId();
			if (StringUtil.isNotBlank(taskId) && Objects.equals(EShopMainConstant.DRAFT_NODE,actId)){
				TaskCompleteDto taskCompleteDto = new TaskCompleteDto();
				taskCompleteDto.setProcessInstanceId(processInstanceId);
				taskCompleteDto.setTaskId(activity.getTaskId());
				taskCompleteDto.setComment("【内销价目表调整单流程驳回后提交】");
				taskCompleteDto.setCommentType(CommentTypeEnum.TY.toString());
				taskCompleteDto.setUserId(ProcessUtils.ProcessConstant.ASSIGNEE_PREFIX + user.getUserId());
				flowOpenClient.complete(taskCompleteDto);
			}
		}
	}

	/**
	 * 获取erp状态的字典value
	 * @param erpStatus 内外销
	 * @return 内外销字典value
	 */
	private String getErpStatusName(String erpStatus){
		if (StringUtil.isNotBlank(erpStatus)){
			R<Dict> dictR = dictClient.getDictInfo("qbcrm_price_list_erpStatus", erpStatus);
			if (dictR.isSuccess() && dictR.getData() != null){
				return dictR.getData().getDictValue();
			}
		}
		return null;
	}

	/**
	 * 获取价目表信息
	 * @param productAdjustmentRecordLines 调整单行表数据
	 * @param priceListId 价目表id
	 */
	private void getOldLinesPriceByPriceListId(ProductAdjustmentRecordLines productAdjustmentRecordLines,Long priceListId){
		// 查询价目表
		ProductPriceListLines productPriceListLines = priceListLinesService.lambdaQuery()
			.select(DBEntity::getId,ProductPriceListLines::getPrice1,ProductPriceListLines::getSuggestedPrice,
				ProductPriceListLines::getSettlementPrice,ProductPriceListLines::getEstimatedProfit,ProductPriceListLines::getPlatformEstimatedProfit,
				ProductPriceListLines::getGrossRate,ProductPriceListLines::getPlatformReceiptPrice,ProductPriceListLines::getPlatformSuggestedPrice)
			.eq(ProductPriceListLines::getHeadId, priceListId)
			.eq(ProductPriceListLines::getProductModel, productAdjustmentRecordLines.getProductModel())
			.apply("rownum <= 1")
			.one();

		BigDecimal oldPrice1 = BigDecimal.ZERO;
		BigDecimal oldSuggestedPrice = BigDecimal.ZERO;
		BigDecimal oldSettlementPrice = BigDecimal.ZERO;
		BigDecimal oldEstimatedProfit = BigDecimal.ZERO;
		BigDecimal oldGrossRate = BigDecimal.ZERO;
		BigDecimal oldPlatformEstimatedProfit = BigDecimal.ZERO;
		BigDecimal oldPlatformReceiptPrice = BigDecimal.ZERO;
		BigDecimal oldPlatformSuggestedPrice = BigDecimal.ZERO;
		BigDecimal companyEstimatedProfit = BigDecimal.ZERO;

		if (productPriceListLines != null){
			oldPrice1 = productPriceListLines.getPrice1() == null ? oldPrice1 : productPriceListLines.getPrice1();
			oldSuggestedPrice = productPriceListLines.getSuggestedPrice() == null ? oldSuggestedPrice : productPriceListLines.getSuggestedPrice();
			oldSettlementPrice = productPriceListLines.getSettlementPrice() == null ? oldSettlementPrice : productPriceListLines.getSettlementPrice();
			oldEstimatedProfit = productPriceListLines.getEstimatedProfit() == null ? oldEstimatedProfit : productPriceListLines.getEstimatedProfit();
			oldGrossRate = productPriceListLines.getGrossRate() == null ? oldGrossRate : productPriceListLines.getGrossRate();
			oldPlatformEstimatedProfit = productPriceListLines.getPlatformEstimatedProfit() == null ? oldPlatformEstimatedProfit : productPriceListLines.getPlatformEstimatedProfit();
			oldPlatformReceiptPrice = productPriceListLines.getPlatformReceiptPrice() == null ? oldPlatformReceiptPrice : productPriceListLines.getPlatformReceiptPrice();
			oldPlatformSuggestedPrice = productPriceListLines.getPlatformSuggestedPrice() == null ? oldPlatformSuggestedPrice : productPriceListLines.getPlatformSuggestedPrice();
			companyEstimatedProfit = productPriceListLines.getCompanyEstimatedProfit() == null ? companyEstimatedProfit : productPriceListLines.getCompanyEstimatedProfit();
		}

		productAdjustmentRecordLines.setOldPrice1(oldPrice1);
		productAdjustmentRecordLines.setOldSuggestedPrice(oldSuggestedPrice);
		productAdjustmentRecordLines.setOldSettlementPrice(oldSettlementPrice);
		productAdjustmentRecordLines.setOldEstimatedProfit(oldEstimatedProfit);
		productAdjustmentRecordLines.setOldGrossRate(oldGrossRate);
		productAdjustmentRecordLines.setOldPlatformEstimatedProfit(oldPlatformEstimatedProfit);
		productAdjustmentRecordLines.setOldPlatformReceiptPrice(oldPlatformReceiptPrice);
		productAdjustmentRecordLines.setOldPlatformSuggestedPrice(oldPlatformSuggestedPrice);
		productAdjustmentRecordLines.setOldCompanyEstimatedProfit(companyEstimatedProfit);
	}

	/**
	 * 获取调整单默认折扣点
	 * @return
	 */
	private BigDecimal getRebatePointDefault(){
		BigDecimal rebatePoint = new BigDecimal("0.7");
		R<List<Dict>> rDictionary = dictClient.getList("qbcrm_price_list_rebatePoint");
		if (rDictionary.isSuccess() && rDictionary.getData() != null){
			List<Dict> dictList = rDictionary.getData();
			String dictValue = dictList.get(0).getDictValue();
			rebatePoint = StringUtil.isNotBlank(dictValue) ? new BigDecimal(dictValue) : rebatePoint;
		}
		return rebatePoint;
	}

	/**
	 * 根据id获取价目表行表数据
	 * @param priceListLineIds 价目表行表id,多个逗号分隔
	 * @return
	 */
	private List<ProductAdjustmentRecordLines> getAdjustmentRecordLinesByPriceListLineIds(String priceListLineIds){
		List<ProductAdjustmentRecordLines> adjustmentRecordLines = new ArrayList<>();
		if (StringUtil.isNotBlank(priceListLineIds)){
			List<Long> idList = Func.toLongList(priceListLineIds);

			List<ProductPriceListLines> productPriceListLines = priceListLinesService
				.lambdaQuery()
				.select(ProductPriceListLines::getBrand,ProductPriceListLines::getModel,ProductPriceListLines::getPosition,
					ProductPriceListLines::getProductModel,ProductPriceListLines::getInterval1,ProductPriceListLines::getInterval2,
					ProductPriceListLines::getPrice1,ProductPriceListLines::getBatchPrice,ProductPriceListLines::getForeignCurrency,
					ProductPriceListLines::getQuantity,ProductPriceListLines::getPrice2,ProductPriceListLines::getFullContainerPrice,
					ProductPriceListLines::getMaterialCode,ProductPriceListLines::getItemChDescription,ProductPriceListLines::getSpecifications,
					ProductPriceListLines::getDiagonal,ProductPriceListLines::getProductType,ProductPriceListLines::getMachiningType,
					ProductPriceListLines::getPackingQuantity1,ProductPriceListLines::getOemModel,ProductPriceListLines::getWidth,
					ProductPriceListLines::getLength,ProductPriceListLines::getBasePrice,ProductPriceListLines::getRebatePoint,
					ProductPriceListLines::getOrgId)
				.in(DBEntity::getId, idList)
				.list();

			// 处理数据
			if (CollectionUtil.isNotEmpty(productPriceListLines)){
				for (ProductPriceListLines productPriceListLine : productPriceListLines) {
					adjustmentRecordLines.add(BeanUtil.copy(productPriceListLine,ProductAdjustmentRecordLines.class));
				}
			}
		}
		return adjustmentRecordLines;
	}

	/**
	 * 发起人和审批人是否相同
	 * @param id 业务id
	 * @param initiator 流程发起人
	 * @return
	 */
	private Boolean initiatorAndApproverSame(Long id,String initiator,String processDefinitionKey){
		ApproverRoles approverRoles = new ApproverRoles();
		approverRoles.setRoleCode("Q001");
		approverRoles.setAttribute1(processDefinitionKey);

		List<String> userIdList = getApproverRoles(approverRoles);

		if (CollectionUtil.isNotEmpty(userIdList)){
			if (userIdList.contains(initiator)){
				log.info("内销价目表发起人和审批人相同,不发起流程");

				lambdaUpdate().eq(DBEntity::getId,id)
					.set(DBEntity::getStatus,EshopConstants.STATUS_NORMAL)
					.update();

				// 执行更新价目表
				executorUpdateAdjustmentRecordToPriceList(id);

				return true;
			}
		}
		return false;
	}

	/**
	 * 执行更新价目表
	 */
	private void executorUpdateAdjustmentRecordToPriceList(Long id){
		// 更新价目表
		executorService.submit(() -> {
			try {
				updateAdjustmentRecordToPriceList(id);
			} catch (IOException e) {
				log.error("内销价目表更新同步ERP出现错误: id {} -> {}",id,e);
			}
		});
	}

	/**
	 * 查询审批角色
	 * @param approverRoles 审批角色查询条件
	 * @return
	 */
	private List<String> getApproverRoles(ApproverRoles approverRoles){
		List<String> taskUserIdList = new ArrayList<>();

		R<List<String>> userIdListR = sysClient.getUserIdList(approverRoles);
		if (userIdListR.isSuccess() && CollectionUtil.isNotEmpty(userIdListR.getData())){
			taskUserIdList = userIdListR.getData();
		}

		return taskUserIdList;
	}

	/**
	 * 计算属性
	 * @param productAdjustmentRecordLines 调整单数据
	 */
	private void calculation(ProductAdjustmentRecordLines productAdjustmentRecordLines){
		calculationEstimatedProfit(productAdjustmentRecordLines);
		calculationPlatformEstimatedProfit(productAdjustmentRecordLines);
		calculationGrossRate(productAdjustmentRecordLines);
		calculationCompanyEstimatedProfit(productAdjustmentRecordLines);
	}

	/**
	 * 计算门店预估利润
	 * @param productAdjustmentRecordLines 调整单数据
	 */
	private void calculationEstimatedProfit(ProductAdjustmentRecordLines productAdjustmentRecordLines){
		// 门店预估利润
		BigDecimal estimatedProfit;

		// 出厂价
		BigDecimal basePrice = productAdjustmentRecordLines.getBasePrice() == null ? BigDecimal.ZERO : productAdjustmentRecordLines.getBasePrice();

		// 结算价
		BigDecimal settlementPrice = productAdjustmentRecordLines.getSettlementPrice() == null ? BigDecimal.ZERO : productAdjustmentRecordLines.getSettlementPrice();

		// 门店预估利润 = 结算价-出厂价*1.3
		estimatedProfit = settlementPrice.subtract(basePrice.multiply(new BigDecimal("1.3")))
			.setScale(0,BigDecimal.ROUND_HALF_UP);

		productAdjustmentRecordLines.setEstimatedProfit(estimatedProfit);
	}

	/**
	 * 计算平台预估利润
	 * @param productAdjustmentRecordLines 调整单数据
	 */
	private void calculationPlatformEstimatedProfit(ProductAdjustmentRecordLines productAdjustmentRecordLines){
		// 平台预估利润
		BigDecimal platformEstimatedProfit;

		// 建议价
		BigDecimal suggestedPrice = productAdjustmentRecordLines.getSuggestedPrice() == null ? BigDecimal.ZERO : productAdjustmentRecordLines.getSuggestedPrice();

		// 结算价
		BigDecimal settlementPrice = productAdjustmentRecordLines.getSettlementPrice() == null ? BigDecimal.ZERO : productAdjustmentRecordLines.getSettlementPrice();

		// 平台预估利润 = 建议价*0.9-结算价
		platformEstimatedProfit = suggestedPrice.multiply(new BigDecimal("0.9")).subtract(settlementPrice)
			.setScale(0,BigDecimal.ROUND_HALF_UP);

		productAdjustmentRecordLines.setPlatformEstimatedProfit(platformEstimatedProfit);
	}

	/**
	 * 计算毛利率
	 * @param productAdjustmentRecordLines 调整单数据
	 */
	private void calculationGrossRate(ProductAdjustmentRecordLines productAdjustmentRecordLines){
		// 毛利率
		BigDecimal grossRate;

		// 平台预估利润
		BigDecimal platformEstimatedProfit = productAdjustmentRecordLines.getPlatformEstimatedProfit() == null ? BigDecimal.ZERO : productAdjustmentRecordLines.getPlatformEstimatedProfit();

		// 建议价
		BigDecimal suggestedPrice = productAdjustmentRecordLines.getSuggestedPrice();

		// 毛利率 = 平台预估利润/建议价*0.9
		if (suggestedPrice != null && suggestedPrice.compareTo(BigDecimal.ZERO) != 0){
			grossRate = platformEstimatedProfit.divide(suggestedPrice.multiply(new BigDecimal("0.9")),1,BigDecimal.ROUND_HALF_UP);
		} else {
			grossRate = BigDecimal.ZERO;
		}

		productAdjustmentRecordLines.setGrossRate(grossRate);
	}

	/**
	 * 计算我司预估利润
	 * @param productAdjustmentRecordLines 调整单数据
	 */
	private void calculationCompanyEstimatedProfit(ProductAdjustmentRecordLines productAdjustmentRecordLines){
		// 我司预估利润
		BigDecimal companyEstimatedProfit;

		// 平台入库价
		BigDecimal platformReceiptPrice = productAdjustmentRecordLines.getPlatformReceiptPrice() == null ? BigDecimal.ZERO : productAdjustmentRecordLines.getPlatformReceiptPrice();

		// 建议价
		BigDecimal suggestedPrice = productAdjustmentRecordLines.getSuggestedPrice() == null ? BigDecimal.ZERO : productAdjustmentRecordLines.getSuggestedPrice();

		// 我司预估利润 = 平台入库价-建议价
		companyEstimatedProfit = platformReceiptPrice.subtract(suggestedPrice).setScale(0,BigDecimal.ROUND_HALF_UP);

		productAdjustmentRecordLines.setCompanyEstimatedProfit(companyEstimatedProfit);
	}

	/**
	 * 获取单号
	 * @return 单号
	 */
	private String getDocnum(){
		return valueService.nextDayValue(EShopMainConstant.ESHOP_PRODUCT_ADJUSTMENT_RECORD, 3);
	}

}

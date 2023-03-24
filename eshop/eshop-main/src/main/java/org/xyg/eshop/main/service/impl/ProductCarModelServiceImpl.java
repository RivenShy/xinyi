package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.protobuf.ServiceException;
import lombok.AllArgsConstructor;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.secure.utils.AuthUtil;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.ObjectUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.system.entity.Dict;
import org.springrabbit.system.feign.IDictClient;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.*;
import org.xyg.eshop.main.mapper.ProductCarModelMapper;
import org.xyg.eshop.main.service.*;
import org.xyg.eshop.main.vo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductCarModelServiceImpl extends BaseServiceImpl<ProductCarModelMapper, ProductCarModel> implements IProductCarModelService {

	private final IProductModelLinesService productModelLinesService;
	private final IProductModelAttachmentService productModelAttachmentService;
	private final IProductInventoryService productInventoryService ;
	private final IProductPriceListService productPriceListService ;
	private final IProductPriceListLinesService productPriceListLinesService ;
	private final IProductModelAdjunctService productModelAdjunctService;

	private final IDictClient dictClient;

	private final ITemplateRelationService relationService;

	@Override
	public R<IPage<ProductCarModel>> getPage(IPage<ProductCarModel> iPage) {
		IPage<ProductCarModel> pageProduct = page(iPage);
		return R.data(pageProduct);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> insertData(ProductModelMergeVO entity) throws ServiceException {
		save(entity);
		if (CollectionUtil.isNotEmpty(entity.getProductModelLinesList())) {
			for (ProductModelLines modelLine : entity.getProductModelLinesList()
			) {
				try {
					modelLine.setHeaderId(entity.getId());
					productModelLinesService.insertData(modelLine);
				} catch (Exception e) {
					throw new ServiceException("产品型号数据出现异常");
				}
			}
		}

		if (CollectionUtil.isNotEmpty(entity.getProductModelAttachmentsList())) {
			for (ProductModelAttachment modelAttachment : entity.getProductModelAttachmentsList()
			) {
				try {
					modelAttachment.setHeaderId(entity.getId());
					productModelAttachmentService.insertData(modelAttachment);
				} catch (Exception e) {
					throw new ServiceException("附件信息数据出现异常");
				}
			}
		}
		return R.data(true);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> deleteData(Long id) throws ServiceException {
		try {
			removeById(id);
		} catch (Exception e) {
			throw new ServiceException("车厂车型数据删除失败");
		}
		try {
			productModelAttachmentService.remove(new QueryWrapper<ProductModelAttachment>().lambda().eq(ProductModelAttachment::getHeaderId, id));
		} catch (Exception e) {
			throw new ServiceException("附件信息数据删除失败");
		}
		try {
			productModelLinesService.remove(new QueryWrapper<ProductModelLines>().lambda().eq(ProductModelLines::getHeaderId, id));
		} catch (Exception e) {
			throw new ServiceException("产品型号信息删除失败");
		}
		return R.data(true);
	}

	@Override
	public R<Boolean> updateDate(ProductModelMergeVO entity) throws ServiceException {
		updateById(entity);
		if (CollectionUtil.isNotEmpty(entity.getProductModelLinesList())) {
			for (ProductModelLines modelLine : entity.getProductModelLinesList()
			) {
				try {
					if (modelLine.getId() != null) {
						productModelLinesService.updateDate(modelLine);
					} else {
						modelLine.setHeaderId(entity.getId());
						productModelLinesService.insertData(modelLine);
					}

				} catch (Exception e) {
					throw new ServiceException("产品型号数据出现异常");
				}

			}
		}

		if (CollectionUtil.isNotEmpty(entity.getProductModelAttachmentsList())) {
			for (ProductModelAttachment modelAttachment : entity.getProductModelAttachmentsList()
			) {
				try {
					if (modelAttachment.getId() != null) {
						productModelAttachmentService.updateDate(modelAttachment);
					} else {
						modelAttachment.setHeaderId(entity.getId());
						productModelAttachmentService.insertData(modelAttachment);
					}

				} catch (Exception e) {
					throw new ServiceException("附件信息数据出现异常");
				}

			}
		}
		return R.data(true);
	}

	@Override
	public R<ProductCarModelVO> getDetail(Long id) {
		ProductCarModelVO detail = baseMapper.getDetail(id);
		return R.data(detail);
	}

	@Override
	public R<IPage<ProductCarModelVO>> getProductPage(IPage<ProductModelLines> iPage ,ProductCarModelVO param) throws ServiceException {

		IPage<ProductCarModelVO> productPage = baseMapper.getProductPage(iPage, param);

		if (CollectionUtil.isEmpty(productPage.getRecords())){
			return R.data(productPage);
		}

		for (ProductCarModelVO productCarModelVO:productPage.getRecords()) {
			//查询本地库存信息
			if(ObjectUtil.isNotEmpty(productCarModelVO)) {

				//根据库存组织和产品型号获取库存数据
				ProductInventoryVO productInventoryVO = new ProductInventoryVO();
				productInventoryVO.setProductMode(productCarModelVO.getFactoryMode());
				List<ProductInventoryVO> inventoryInformation = productInventoryService.getInventoryList(productInventoryVO);
				// 总库存
				Long totalInventory = 0L;
				if (CollectionUtil.isNotEmpty(inventoryInformation)) {
					productCarModelVO.setStock(inventoryInformation.get(0).getAvailableInventory() == null ? 0L : inventoryInformation.get(0).getAvailableInventory());

					//计算总库存信息
					totalInventory = inventoryInformation.get(0).getTotalInventory();
				} else {
					productCarModelVO.setStock(0L);
				}

//				productCarModelVO.setTotalInventory(totalInventory);

				/*Dict qbcrm_price_list = EshopConstants.getData(dictClient.getDictInfo("qbcrm_price_list", "1"));
				if(ObjectUtil.isNotEmpty(qbcrm_price_list)){
					//获取价格信息
					List<ProductPriceList> productPriceLists = productPriceListService.list(new QueryWrapper<ProductPriceList>().lambda().eq(ProductPriceList::getPriceListName, qbcrm_price_list.getDictValue()));
					if(CollectionUtil.isNotEmpty(productPriceLists)){
						ProductPriceList productPriceList = productPriceLists.get(0);
						List<ProductPriceListLines> productPriceListLines = productPriceListLinesService.list(new QueryWrapper<ProductPriceListLines>().lambda()
							.eq(ProductPriceListLines::getHeadId, productPriceList.getId())
							.eq(ProductPriceListLines::getMaterialCode, productCarModelVO.getMaterialCode()));

						if(CollectionUtil.isNotEmpty(productPriceListLines)){
							productCarModelVO.setPrice(productPriceListLines.get(0).getPrice1());
							productCarModelVO.setBulkPrice(productPriceListLines.get(0).getBatchPrice());
						}
					}
				}*/
			}
		}
		return R.data(productPage);
	}

	@Override
	public R<List<ProductCarModelVO>> getProductList(Long productLineId , String xygType , String attachment) throws ServiceException {
		if(AuthUtil.getUserId() == null){
			throw new ServiceException("未查询到客户登录信息");
		}

		List<ProductCarModelVO> productPage = baseMapper.getProductList(productLineId , xygType , attachment , null);

		for (ProductCarModelVO productCarModelVO:productPage
		) {
			if(ObjectUtil.isNotEmpty(productCarModelVO)){
				if(StringUtil.isNotBlank(productCarModelVO.getFactoryMode())){
					Dict qbcrm_price_list = EShopMainConstant.getData(dictClient.getDictInfo("qbcrm_price_list", "1"));
					if(ObjectUtil.isNotEmpty(qbcrm_price_list)){
						//获取价格信息
						List<ProductPriceList> productPriceLists = productPriceListService.list(new QueryWrapper<ProductPriceList>().lambda().eq(ProductPriceList::getPriceListName, qbcrm_price_list.getDictValue()));
						if(CollectionUtil.isNotEmpty(productPriceLists)){
							ProductPriceList productPriceList = productPriceLists.get(0);
							List<ProductPriceListLines> productPriceListLines = productPriceListLinesService.list(new QueryWrapper<ProductPriceListLines>().lambda()
								.eq(ProductPriceListLines::getHeadId, productPriceList.getId())
								.eq(ProductPriceListLines::getMaterialCode, productCarModelVO.getMaterialCode()));

							if(CollectionUtil.isNotEmpty(productPriceListLines)){
//								productCarModelVO.setPrice(productPriceListLines.get(0).getPrice1());
//								productCarModelVO.setBulkPrice(productPriceListLines.get(0).getBatchPrice());
							}
						}
					}
				}
			}
		}
		return R.data(productPage);
	}

	@Override
	public R<List<ProductCarModelVO>> getProductColor(Long productLineId, String xygType, String attachment , String colour) {
		if(AuthUtil.getUserId() == null){
			return R.fail("未查询到客户登录信息");
		}
		List<ProductCarModelVO> productPage = baseMapper.getProductList(productLineId , xygType , attachment , colour);

		return R.data(productPage);
	}

	@Override
	public R<List<ProductCarModelCascader>> findProductCarModel() {
		List<ProductCarModel> productCarModel = baseMapper.findProductCarModel();
		List<ProductCarModelCascader> productCarModelCascaderList = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		for (ProductCarModel carModel : productCarModel) {
			ProductCarModelCascader productCarModelCascader = new ProductCarModelCascader();
			String strCar = toFirstChar(carModel.getCarFactory()).toUpperCase();
			String substring = strCar.substring(0, 1);
			if(map.get(substring)==null){
				map.put(substring,substring);
			}
			productCarModelCascader.setLabel(carModel.getCarFactory());
			productCarModelCascader.setValue(carModel.getCarFactory());
			productCarModelCascader.setMsg(substring);
			List<ProductCarModel> list = list(new QueryWrapper<ProductCarModel>().lambda().eq(ProductCarModel::getCarFactory, carModel.getCarFactory()));
			List<ProductCarModelCascader> productCarModelCascaderListChildren = new ArrayList<>();
			for (ProductCarModel model : list) {
				ProductCarModelCascader productCarModelCascader1 = new ProductCarModelCascader();
				productCarModelCascader1.setLabel(model.getModel());
				productCarModelCascader1.setValue(model.getModel());
				productCarModelCascaderListChildren.add(productCarModelCascader1);
			}
			productCarModelCascader.setChildren(productCarModelCascaderListChildren);
			productCarModelCascaderList.add(productCarModelCascader);
		}
		List<ProductCarModelCascader> productCarModelCascaders = new ArrayList<>();
		Map<String, List<ProductCarModelCascader>> collect = productCarModelCascaderList.stream().collect(Collectors.groupingBy(ProductCarModelCascader::getMsg));
		for (String str:map.keySet()
			 ) {
			ProductCarModelCascader productCarModelCascader = new ProductCarModelCascader();
			productCarModelCascader.setLabel(str);
			productCarModelCascader.setValue(str);
			productCarModelCascader.setChildren(collect.get(str));
			productCarModelCascaders.add(productCarModelCascader);
		}
		return R.data(productCarModelCascaders);
	}

	/**
	 * 获取字符串拼音的第一个字母
	 * @param chinese
	 * @return
	 */
	public static String toFirstChar(String chinese){
		String pinyinStr = "";
		char[] newChar = chinese.toCharArray();  //转为单个字符
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < newChar.length; i++) {
			if (newChar[i] > 128) {
				try {
					pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			}else{
				pinyinStr += newChar[i];
			}
		}
		return pinyinStr;
	}

	@Override
	public IPage<ProductModelLinesToPrictListVO> getToPriceListPage(IPage<ProductModelLinesToPrictListVO> page, ProductModelLinesToPrictListVO linesToPrictListVO){
		return baseMapper.getToPriceListPage(page,linesToPrictListVO);
	}

	@Override
	public ProductModelLinesVO linesSave(ProductModelLinesVO productModelLinesVO){
		productModelLinesService.save(productModelLinesVO);
		// 保存模板关联数据
		saveTemplateRelation(productModelLinesVO.getId(),productModelLinesVO.getRelationList());

		return productModelLinesVO;
	}

	@Override
	public ProductModelLinesVO linesUpdate(ProductModelLinesVO productModelLinesVO){
		productModelLinesService.updateById(productModelLinesVO);
		return productModelLinesVO;
	}

	@Override
	public ProductModelLinesVO linesDetail(Long id){
		ProductModelLinesVO productModelLinesVO = new ProductModelLinesVO();

		ProductModelLines lines = productModelLinesService.getById(id);
		if (lines == null){
			return productModelLinesVO;
		}

		productModelLinesVO = BeanUtil.copyProperties(lines,ProductModelLinesVO.class);

		// 查询模板关联数据
		List<TemplateRelation> relationList = getRelationByHeadIdList(productModelLinesVO.getId());
		productModelLinesVO.setRelationList(relationList);

		return productModelLinesVO;
	}

	/**
	 * 保存模板关联数据
	 * @param list 模板关联数据
	 */
	private void saveTemplateRelation(Long headId,List<TemplateRelation> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}
		list.forEach(relation -> {
			relation.setHeadId(headId);
		});
		relationService.saveData(list);
	}

	/**
	 * 根据头表id获取模板数据
	 * @param headId 头表id
	 * @return
	 */
	private List<TemplateRelation> getRelationByHeadIdList(Long headId){
		return relationService.getRelationByHeadIdList(headId);
	}

}

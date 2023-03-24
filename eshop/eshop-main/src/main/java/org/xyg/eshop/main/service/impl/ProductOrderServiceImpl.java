package org.xyg.eshop.main.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.base.DBEntity;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.ehop.common.constants.EshopConstants;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.ProductOrder;
import org.xyg.eshop.main.entity.ProductOrderLines;
import org.xyg.eshop.main.excel.order.ProductOrderExcel;
import org.xyg.eshop.main.mapper.ProductOrderMapper;
import org.xyg.eshop.main.service.ICommonService;
import org.xyg.eshop.main.service.IProductOrderLinesService;
import org.xyg.eshop.main.service.IProductOrderService;
import org.xyg.eshop.main.vo.ProductOrderVO;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductOrderServiceImpl extends BaseServiceImpl<ProductOrderMapper, ProductOrder> implements IProductOrderService {

	private final IProductOrderLinesService linesService;

	private final AutoIncrementIDGenerator autoIncrementIDGenerator;

	private final ICommonService commonService;

	@Override
	public IPage<ProductOrderVO> getPage(IPage<ProductOrderVO> page, ProductOrderVO orderVO){
		// 处理日期搜索条件
		processingDate(orderVO);

		IPage<ProductOrderVO> resPage = baseMapper.getPage(page, orderVO);

		// 填充数据
		fillData(resPage.getRecords());

		return resPage;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveData(ProductOrderVO orderVO){
		// 生成订单号
		getOrderNo(orderVO);

		if (orderVO.getStatus() == null) {
			orderVO.setStatus(EshopConstants.STATUS_SAVE);
		}

		saveOrUpdate(orderVO);

		// 保存行表信息
		saveLinesList(orderVO);

		return orderVO.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long submit(ProductOrderVO orderVO){
		// 生成订单号
		getOrderNo(orderVO);

		saveOrUpdate(orderVO);

		// 保存行表信息
		saveLinesList(orderVO);

		return orderVO.getId();
	}

	@Override
	public ProductOrderVO detail(Long id, String processInstanceId){
		ProductOrder order = lambdaQuery()
			.eq(id != null, DBEntity::getId, id)
			.in(StringUtil.isNotBlank(processInstanceId), ProductOrder::getProcessInstanceId, Func.toStrList(processInstanceId))
			.one();

		Assert.notNull(order,"未查询到数据");

		ProductOrderVO productOrderVO = BeanUtil.copyProperties(order, ProductOrderVO.class);

		// 获取行表数据
		getLinesByHeadId(productOrderVO);

		// 填充数据
		fillDetailData(productOrderVO);

		return productOrderVO;
	}

	@Override
	public List<ProductOrderExcel> getExcelList(ProductOrderVO orderVO){
		return baseMapper.getExcelList(orderVO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<ProductOrderVO> importer(List<ProductOrderExcel> orderExcelList){
		if (CollectionUtil.isEmpty(orderExcelList)){
			return null;
		}

		List<ProductOrderVO> orderVOList = BeanUtil.copyProperties(orderExcelList, ProductOrderVO.class);
		for (ProductOrderVO orderVO : orderVOList) {
			saveOrUpdate(orderVO);
		}
		return orderVOList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean linesDelete(String ids){
		return linesService.deleteLogic(Func.toLongList(ids));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean distributeStore(List<ProductOrderVO> list){
		if (CollectionUtil.isEmpty(list)){
			return false;
		}

		for (ProductOrderVO orderVO : list) {
			orderVO.setStatus(EShopMainConstant.ORDER_STATUS_DISTRIBUTE_STORE);
			updateById(orderVO);
		}

		return true;
	}

	/**
	 * 处理日期搜索条件
	 * @param orderVO 搜索条件
	 */
	private void processingDate(ProductOrderVO orderVO){
		String startDate = orderVO.getStartDate();
		String endDate = orderVO.getEndDate();

		if (StringUtil.isNotBlank(startDate) && startDate.length() == 10){
			startDate += " 00:00:00";
			orderVO.setStartDate(startDate);
		}

		if (StringUtil.isNotBlank(endDate) && endDate.length() == 10){
			endDate += " 23:59:59";
			orderVO.setEndDate(endDate);
		}
	}

	/**
	 * 保存行表信息
	 * @param orderVO 头行表数据
	 */
	private void saveLinesList(ProductOrderVO orderVO){
		List<ProductOrderLines> linesList = orderVO.getLinesList();
		if (CollectionUtil.isEmpty(linesList)){
			return;
		}

		for (ProductOrderLines productOrderLines : linesList) {
			productOrderLines.setHeadId(orderVO.getId());
			linesService.saveOrUpdate(productOrderLines);
		}

	}

	/**
	 * 获取订单号
	 * @param orderVO 订单数据
	 */
	private void getOrderNo(ProductOrderVO orderVO){
		if (StringUtil.isBlank(orderVO.getOrderNo())){
			String orderNo = autoIncrementIDGenerator.nextDayValue(EShopMainConstant.PRODUCT_ORDER_NO, EShopMainConstant.PRODUCT_ORDER_MIN_LEN);
			orderVO.setOrderNo(orderNo);
		}
	}

	/**
	 * 根据头表id获取订单行表数据
	 * @param orderVO 订单数据
	 */
	private void getLinesByHeadId(ProductOrderVO orderVO){
		if (orderVO == null){
			return;
		}
		List<ProductOrderLines> linesList = linesService.lambdaQuery()
			.eq(ProductOrderLines::getHeadId, orderVO.getId())
			.list();

		orderVO.setLinesList(linesList);
	}

	/**
	 * 填充列表数据
	 * @param list 订单列表
	 */
	private void fillData(List<ProductOrderVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}

		for (ProductOrderVO orderVO : list) {
			fillDetailData(orderVO);
		}
	}

	/**
	 * 填充详情数据
	 * @param orderVO 订单详情数据
	 */
	private void fillDetailData(ProductOrderVO orderVO){
		if (orderVO == null){
			return;
		}

		// 订单状态字典值
		String dictStatus = orderVO.getStatus() == null ? null : Func.toStr(orderVO.getStatus());
		String statusName = commonService.getDictValue(EShopMainConstant.ORDER_STATUS_DICT_CODE, dictStatus);
		orderVO.setStatusName(statusName);

		// 查询 是否 字典值
		String visitWhetherSatisfiedName = commonService.getDictValue(EShopMainConstant.YES_NO_DICT_CODE, orderVO.getVisitWhetherSatisfied());
		orderVO.setVisitWhetherSatisfiedName(visitWhetherSatisfiedName);

		// 查询回访状态字典值
		String visitDictStatus = orderVO.getVisitStatus() == null ? null : Func.toStr(orderVO.getVisitStatus());
		String visitStatusName = commonService.getDictValue(EShopMainConstant.ORDER_VISIT_STATUS_DICT_CODE, visitDictStatus);
		orderVO.setVisitStatusName(visitStatusName);

	}

}

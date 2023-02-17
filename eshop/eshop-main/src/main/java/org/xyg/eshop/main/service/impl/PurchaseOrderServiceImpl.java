package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.Func;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.eshop.main.dao.IPurchaseOrderCommodityDao;
import org.xyg.eshop.main.dao.IPurchaseOrderDao;
import org.xyg.eshop.main.dto.PurchaseOrderDTO;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;
import org.xyg.eshop.main.enums.PurchaseOrderStatusEnum;
import org.xyg.eshop.main.mapper.PurchaseOrderCommodityMapper;
import org.xyg.eshop.main.mapper.PurchaseOrderMapper;
import org.xyg.eshop.main.service.IPurchaseOrderService;
import org.xyg.eshop.main.vo.ContractVO;
import org.xyg.eshop.main.vo.PurchaseOrderVO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.xyg.eshop.main.constants.EShopMainConstant.ESCRM_PURCHASE_ORDER_APPROVAL;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseOrderServiceImpl implements IPurchaseOrderService {

	@Autowired
	private AutoIncrementIDGenerator autoIncrementIDGenerator;

	@Autowired
	private IPurchaseOrderDao purchaseOrderDao;

	@Autowired
	private IPurchaseOrderCommodityDao purchaseOrderCommodityDao;

	@Autowired
	private PurchaseOrderMapper purchaseOrderMapper;

	@Autowired
	private PurchaseOrderCommodityMapper purchaseOrderCommodityMapper;

	@Override
	public R<Boolean> saveOrUpdate(PurchaseOrderDTO purchaseOrderDTO) {
		if (Func.isEmpty(purchaseOrderDTO.getStoreName())) {
			return R.fail("缺少必要的请求参数: storeName");
		}
		if (Func.isEmpty(purchaseOrderDTO.getPurchaseSupplier())) {
			return R.fail("缺少必要的请求参数: purchaseSupplier");
		}
		if (Func.isEmpty(purchaseOrderDTO.getPurchaseApplicant())) {
			return R.fail("缺少必要的请求参数: purchaseApplicant");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date()); // 格式化日期 date: 20210114
		String purchaseOrderNumber = autoIncrementIDGenerator.nextValueByPrependAndType(date, ESCRM_PURCHASE_ORDER_APPROVAL);
		purchaseOrderDTO.setPurchaseOrderNumber(purchaseOrderNumber);
		purchaseOrderDTO.setPurchaseStatus(PurchaseOrderStatusEnum.TO_BE_SUBMITTED.getName());
		purchaseOrderDTO.setPurchaseOrderDate(new Date());
		purchaseOrderDTO.setStatus(0);
		boolean savePurchaseOrder = purchaseOrderDao.saveOrUpdate(purchaseOrderDTO);
		if (savePurchaseOrder) {
			// 合同相关人员
			if (purchaseOrderDTO.getPurchaseOrderCommodityList() != null) {
				for (PurchaseOrderCommodity contractRelatePersonnel : purchaseOrderDTO.getPurchaseOrderCommodityList()) {
					contractRelatePersonnel.setPurchaseOrderId(purchaseOrderDTO.getId());
				}
				boolean savePurchaseOrderCommodity = purchaseOrderCommodityDao.saveOrUpdateBatch(purchaseOrderDTO.getPurchaseOrderCommodityList());
				if (!savePurchaseOrderCommodity) {
					return R.fail("新增采购订单成功，但新增采购订单商品失败");
				}
			}
			return R.success("新增采购订单成功");
		}
		return R.fail("新增采购订单失败");
	}

	@Override
	public IPage<PurchaseOrderVO> getPage(IPage page, PurchaseOrderDTO purchaseOrderDTO) {
		List<PurchaseOrderVO> purchaseOrderVOList = purchaseOrderMapper.selectPurchaseOrderPage(page, purchaseOrderDTO);
		return page.setRecords(purchaseOrderVOList);
	}

	@Override
	public R<PurchaseOrderVO> selectById(Long id) {
		if(Func.isEmpty(id)) {
			return R.fail("缺少必要的请求参数: id");
		}
		PurchaseOrder purchaseOrder = purchaseOrderDao.getById(id);
		if(purchaseOrder == null) {
			return R.fail("查询采购订单信息失败");
		}
		PurchaseOrderVO purchaseOrderVO = BeanUtil.copy(purchaseOrder, PurchaseOrderVO.class);

		List<PurchaseOrderCommodity> purchaseOrderCommodityList = purchaseOrderCommodityMapper.selectListByPurchaseOrderId(id);
		if(purchaseOrderCommodityList == null) {
			return R.fail("查询采购商品信息失败");
		}
		purchaseOrderVO.setPurchaseOrderCommodityList(purchaseOrderCommodityList);
		return R.data(purchaseOrderVO);
	}
}

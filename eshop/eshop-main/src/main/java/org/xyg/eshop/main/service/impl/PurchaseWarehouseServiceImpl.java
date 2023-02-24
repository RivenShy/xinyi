package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.dao.IPurchaseOrderDao;
import org.xyg.eshop.main.dao.IPurchaseWarehouseDao;
import org.xyg.eshop.main.dao.IWarehouseCommodityDao;
import org.xyg.eshop.main.dto.PurchaseWarehouseDTO;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;
import org.xyg.eshop.main.entity.PurchaseWarehouse;
import org.xyg.eshop.main.entity.WarehouseCommodity;
import org.xyg.eshop.main.service.IPurchaseWarehouseService;
import org.xyg.eshop.main.vo.PurchaseOrderVO;
import org.xyg.eshop.main.vo.PurchaseWarehouseVO;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseWarehouseServiceImpl implements IPurchaseWarehouseService {

	@Autowired
	private IPurchaseWarehouseDao purchaseWarehouseDao;

	@Autowired
	private IPurchaseOrderDao purchaseOrderDao;

	@Autowired
	private IWarehouseCommodityDao warehouseCommodityDao;


	@Override
	public R<Boolean> save(PurchaseWarehouseDTO purchaseWarehouseDTO) {
		if(Func.isEmpty(purchaseWarehouseDTO.getPurchaseOrderId())) {
			return R.fail("缺少必要的请求参数：purchaseOrderId");
		}
		if(Func.isEmpty(purchaseWarehouseDTO.getPurchaseOrderNumber())) {
			return R.fail("缺少必要的请求参数：purchaseOrderNumber");
		}
		if(Func.isEmpty(purchaseWarehouseDTO.getBillType())) {
			return R.fail("缺少必要的请求参数：billType");
		}
		if(Func.isEmpty(purchaseWarehouseDTO.getPurchaseOrderDate())) {
			return R.fail("缺少必要的请求参数：purchaseOrderDate");
		}
		if(Func.isEmpty(purchaseWarehouseDTO.getStoreName())) {
			return R.fail("缺少必要的请求参数：storeName");
		}
		if(Func.isEmpty(purchaseWarehouseDTO.getAssociateSaleOrder())) {
			return R.fail("缺少必要的请求参数：associateSaleOrder");
		}
		if(Func.isEmpty(purchaseWarehouseDTO.getWarehouseDate())) {
			purchaseWarehouseDTO.setWarehouseDate(new Date());
		}
		if(Func.isEmpty(purchaseWarehouseDTO.getWarehouseCommodityList())) {
			return R.fail("缺少必要的请求参数：warehouseCommodityList");
		}
		PurchaseOrder purchaseOrderGetFromDB = purchaseOrderDao.getById(purchaseWarehouseDTO.getPurchaseOrderId());
		if(purchaseOrderGetFromDB == null) {
			return R.fail("采购订单不存在,purchaseOrderId:" + purchaseWarehouseDTO.getPurchaseOrderId());
		}
		Boolean savePurchaseWarehouse = purchaseWarehouseDao.save(purchaseWarehouseDTO);
		if(!savePurchaseWarehouse) {
			return R.fail("新增采购入库信息失败");
		}
		for(WarehouseCommodity warehouseCommodity : purchaseWarehouseDTO.getWarehouseCommodityList()) {
			warehouseCommodity.setPurchaseWarehouseId(purchaseWarehouseDTO.getId());
		}
		Boolean saveWarehouseCommodityBatch = warehouseCommodityDao.saveBatch(purchaseWarehouseDTO.getWarehouseCommodityList());
		if(!saveWarehouseCommodityBatch) {
			return R.fail("新增采购入库商品信息失败");
		}
		// TODO 更新商品库存
		return R.success("新增采购入库信息成功");
	}

	@Override
	public R<IPage<PurchaseWarehouse>> getPage(Query query, PurchaseWarehouseDTO purchaseWarehouseDTO) {

		LambdaQueryChainWrapper<PurchaseWarehouse> wrapper = purchaseWarehouseDao.lambdaQuery();
		// 查询条件
//		if (StringUtil.isNotBlank(contractDTO.getCustomerName())) {
//			wrapper.like(Contract::getCustomerName, contractDTO.getCustomerName());
//		}
//		if (StringUtil.isNotBlank(contractDTO.getContractCode())) {
//			wrapper.like(Contract::getContractCode, contractDTO.getContractCode());
//		}
//		if (StringUtil.isNotBlank(contractDTO.getContractType())) {
//			wrapper.like(Contract::getContractType, contractDTO.getContractType());
//		}
		return R.data(wrapper.page(Condition.getPage(query)));
	}

	@Override
	public R<PurchaseWarehouseVO> selectById(Long id) {
		if(Func.isEmpty(id)) {
			return R.fail("缺少必要的请求参数: id");
		}
//		return R.data(contractService.selectById(id));
		PurchaseWarehouse purchaseWarehouse = purchaseWarehouseDao.getById(id);
		if(purchaseWarehouse == null) {
			return R.fail("查询采购入库单失败");
		}
		PurchaseWarehouseVO purchaseWarehouseVO = BeanUtil.copy(purchaseWarehouse, PurchaseWarehouseVO.class);
//		List<WarehouseCommodity> warehouseCommodityList = warehouseCommodityMapper.selectListByPurchaseWarehouseId(id);
		List<WarehouseCommodity> warehouseCommodityList = warehouseCommodityDao.lambdaQuery().eq(WarehouseCommodity::getPurchaseWarehouseId, id).list();
		if(warehouseCommodityList == null) {
			return R.fail("查询入库商品信息失败");
		}
		purchaseWarehouseVO.setWarehouseCommodityList(warehouseCommodityList);
		return R.data(purchaseWarehouseVO);
	}
}

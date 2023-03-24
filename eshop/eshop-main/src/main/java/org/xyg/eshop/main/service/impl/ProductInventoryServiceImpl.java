package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.base.DBEntity;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.InventoryAlert;
import org.xyg.eshop.main.entity.ProductInventory;
import org.xyg.eshop.main.mapper.ProductInventoryMapper;
import org.xyg.eshop.main.service.IInventoryAlertService;
import org.xyg.eshop.main.service.IProductInventoryService;
import org.xyg.eshop.main.vo.ProductInventoryVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductInventoryServiceImpl extends BaseServiceImpl<ProductInventoryMapper, ProductInventory> implements IProductInventoryService {

	private final IInventoryAlertService alertService;

	@Override
	public List<ProductInventoryVO> getInventoryList(ProductInventoryVO productInventoryVO) {
		return baseMapper.getInventoryList(productInventoryVO);
	}

	@Override
	public IPage<ProductInventoryVO> getPage(IPage<ProductInventoryVO> page, ProductInventoryVO productInventoryVO){
		IPage<ProductInventoryVO> resPage = baseMapper.getPage(page, productInventoryVO);
		fillData(resPage.getRecords());
		return resPage;
	}

	@Override
	public IPage<ProductInventoryVO> getMergePage(IPage<ProductInventoryVO> page, ProductInventoryVO productInventoryVO){

		IPage<ProductInventoryVO> resPage = baseMapper.getMergePage(page, productInventoryVO);

		fillData(resPage.getRecords());

		return resPage;
	}

	/**
	 * 补充库存数据
	 * @param list 库存数据
	 */
	private void fillData(List<ProductInventoryVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}

		// 查询到的所有产品编码
		Set<String> productModeSet = list.stream()
			.map(ProductInventoryVO::getProductMode)
			.filter(StringUtil::isNotBlank)
			.collect(Collectors.toSet());

		// 查询预警信息
		List<InventoryAlert> alertList = alertService.lambdaQuery()
			.in(InventoryAlert::getProductMode, productModeSet)
			.ne(DBEntity::getStatus,EShopMainConstant.INVENTORY_ALERT_STATUS)
			.list();

		Map<String, InventoryAlert> alertMap = new HashMap<>();
		if (CollectionUtil.isNotEmpty(alertList)){
			alertMap = alertList.stream().collect(Collectors.toMap(InventoryAlert::getProductMode, a -> a, (v1, v2) -> v2));
		}

		for (ProductInventoryVO productInventoryVO : list) {
			Long totalInventory = productInventoryVO.getTotalInventory() == null ? EShopMainConstant.INVENTORY_ZERO : productInventoryVO.getTotalInventory();
			Long occupyInventory = productInventoryVO.getOccupyInventory() == null ? EShopMainConstant.INVENTORY_ZERO : productInventoryVO.getOccupyInventory();

			// 可用库存计算
			Long availableInventory = totalInventory - occupyInventory;
			productInventoryVO.setAvailableInventory(availableInventory);

			// 预警状态计算
			InventoryAlert inventoryAlert = alertMap.get(productInventoryVO.getProductMode());
			if (inventoryAlert != null){
				String alertStatus = null;

				Long lowerLimitInventory = inventoryAlert.getLowerLimitInventory() == null ? EShopMainConstant.INVENTORY_ZERO : inventoryAlert.getLowerLimitInventory();
				Long inventoryCeiling = inventoryAlert.getInventoryCeiling() == null ? EShopMainConstant.INVENTORY_ZERO : inventoryAlert.getInventoryCeiling();

				if (totalInventory < lowerLimitInventory){
					alertStatus = "低库存";
				} else if (totalInventory > inventoryCeiling){
					alertStatus = "高库存";
				}

				productInventoryVO.setAlertStatus(alertStatus);
			}

		}
	}

}

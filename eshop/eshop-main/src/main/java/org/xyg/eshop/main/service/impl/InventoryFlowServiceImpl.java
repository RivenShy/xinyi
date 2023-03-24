package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.InventoryFlow;
import org.xyg.eshop.main.entity.ProductInventory;
import org.xyg.eshop.main.mapper.InventoryFlowMapper;
import org.xyg.eshop.main.service.IInventoryFlowService;
import org.xyg.eshop.main.service.IProductInventoryService;
import org.xyg.eshop.main.vo.InventoryFlowVO;

import java.util.*;

@Service
@AllArgsConstructor
public class InventoryFlowServiceImpl extends BaseServiceImpl<InventoryFlowMapper, InventoryFlow> implements IInventoryFlowService {

	private final IProductInventoryService inventoryService;

	@Override
	public IPage<InventoryFlowVO> getPage(IPage<InventoryFlowVO> page, InventoryFlowVO inventoryFlowVO){
		// 处理日期搜索条件
		processingDate(inventoryFlowVO);
		return baseMapper.getPage(page,inventoryFlowVO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveInventoryFlowList(List<InventoryFlow> list){
		if (CollectionUtil.isEmpty(list)){
			return false;
		}

		// 处理需要保存的数据
		handleFlowSaveData(list);

		saveBatch(list);

		// 更新库存
		updateInventory(list);

		return true;
	}

	/**
	 * 处理日期搜索条件
	 * @param inventoryFlowVO 搜索条件
	 */
	private void processingDate(InventoryFlowVO inventoryFlowVO){
		String startDate = inventoryFlowVO.getStartDate();
		String endDate = inventoryFlowVO.getEndDate();

		if (StringUtil.isNotBlank(startDate) && startDate.length() == 10){
			startDate += " 00:00:00";
			inventoryFlowVO.setStartDate(startDate);
		}

		if (StringUtil.isNotBlank(endDate) && endDate.length() == 10){
			endDate += " 23:59:59";
			inventoryFlowVO.setEndDate(endDate);
		}
	}

	/**
	 * 处理需要保存的库存流水数据
	 * @param list 库存流水数据
	 */
	private void handleFlowSaveData(List<InventoryFlow> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}

		// 库存搜索条件
		Set<String> productModeSet = new HashSet<>();
		Set<Long> storefrontIdSet = new HashSet<>();
		for (InventoryFlow inventoryFlow : list) {
			productModeSet.add(inventoryFlow.getProductMode());
			storefrontIdSet.add(inventoryFlow.getStorefrontId());
		}

		// 查询门店总库存
		List<ProductInventory> inventoryList = inventoryService.lambdaQuery()
			.in(ProductInventory::getStorefrontId, storefrontIdSet)
			.in(ProductInventory::getProductMode, productModeSet)
			.list();

		// 门店总库存map
		Map<String,Long> totalInventoryMap = new HashMap<>();
		Map<String,Long> transportationInventoryMap = new HashMap<>();
		if (CollectionUtil.isEmpty(inventoryList)){
			for (ProductInventory inventory : inventoryList) {
				totalInventoryMap.put(inventory.getProductMode(),inventory.getTotalInventory());
				transportationInventoryMap.put(inventory.getProductMode(),inventory.getTransportationInventory());
			}
		}

		for (InventoryFlow inventoryFlow : list) {
			String productMode = inventoryFlow.getProductMode();
			// 计算剩余库存
			Long totalInventory = totalInventoryMap.get(productMode);
			totalInventory = totalInventory == null ? EShopMainConstant.INVENTORY_ZERO : totalInventory;

			Long inventoryQuantity = inventoryFlow.getInventoryQuantity() == null ? EShopMainConstant.INVENTORY_ZERO : inventoryFlow.getInventoryQuantity();

			Long remainingInventory = null;
			String docnumType = inventoryFlow.getDocnumType();

			if(Objects.equals(EShopMainConstant.INVENTORY_OUTBOUND,docnumType)){
				remainingInventory = totalInventory - inventoryQuantity;
			} else if (Objects.equals(EShopMainConstant.INVENTORY_INBOUND,docnumType)){
				remainingInventory = totalInventory + inventoryQuantity;
			}

			inventoryFlow.setRemainingInventory(remainingInventory);

			// 在途库存
			Long transportationInventory = transportationInventoryMap.get(productMode);
			inventoryFlow.setTransportationInventory(transportationInventory);

		}
	}

	/**
	 * 更新库存数据
	 * @param list 流水数据
	 */
	private void updateInventory(List<InventoryFlow> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}

		for (InventoryFlow inventoryFlow : list) {
			String productMode = inventoryFlow.getProductMode();
			Long inventoryQuantity = inventoryFlow.getInventoryQuantity();

			// 更新门店总库存
			Long storefrontId = inventoryFlow.getStorefrontId();
			inventoryService.lambdaUpdate()
				.eq(ProductInventory::getProductMode,productMode)
				.eq(ProductInventory::getStorefrontId,storefrontId)
				.set(ProductInventory::getTotalInventory,inventoryFlow.getRemainingInventory())
				.update();

			// 更新门店在途库存
			Long toStorefrontId = inventoryFlow.getToStorefrontId();
			if (toStorefrontId != null){
				Long transportationInventory = inventoryFlow.getTransportationInventory();
				transportationInventory = transportationInventory == null ? EShopMainConstant.INVENTORY_ZERO : transportationInventory;

				// 在途库存 = 原在途库存 + 库存数量
				transportationInventory = transportationInventory + inventoryQuantity;

				inventoryService.lambdaUpdate()
					.eq(ProductInventory::getProductMode,productMode)
					.eq(ProductInventory::getStorefrontId,toStorefrontId)
					.set(ProductInventory::getTransportationInventory,transportationInventory)
					.update();
			}

		}
	}

}

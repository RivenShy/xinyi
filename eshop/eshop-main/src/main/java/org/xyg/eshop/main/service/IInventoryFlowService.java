package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.InventoryFlow;
import org.xyg.eshop.main.vo.InventoryFlowVO;

public interface IInventoryFlowService extends BaseService<InventoryFlow> {

	/**
	 * 分页查询库存流水列表
	 * @param page 分页参数
	 * @param inventoryFlowVO 搜索条件
	 * @return
	 */
	IPage<InventoryFlowVO> getPage(IPage<InventoryFlowVO> page, InventoryFlowVO inventoryFlowVO);

}

package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.ProductInventory;
import org.xyg.eshop.main.vo.ProductInventoryVO;

import java.util.List;

public interface IProductInventoryService extends BaseService<ProductInventory> {

	/**
	 * 查询库存列表
	 * @param productInventoryVO 搜索条件
	 * @return
	 */
	List<ProductInventoryVO> getInventoryList(ProductInventoryVO productInventoryVO);

	/**
	 * 查询库存分页列表
	 * @param page 分页参数
	 * @param productInventoryVO 搜索条件
	 * @return
	 */
	IPage<ProductInventoryVO> getPage(IPage<ProductInventoryVO> page, ProductInventoryVO productInventoryVO);

}

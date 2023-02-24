package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.entity.ProductInventory;
import org.xyg.eshop.main.mapper.ProductInventoryMapper;
import org.xyg.eshop.main.service.IProductInventoryService;
import org.xyg.eshop.main.vo.ProductInventoryVO;

import java.util.List;

@Service
public class ProductInventoryServiceImpl extends BaseServiceImpl<ProductInventoryMapper, ProductInventory> implements IProductInventoryService {

	@Override
	public List<ProductInventoryVO> getInventoryList(ProductInventoryVO productInventoryVO) {
		return baseMapper.getInventoryList(productInventoryVO);
	}

	@Override
	public IPage<ProductInventoryVO> getPage(IPage<ProductInventoryVO> page, ProductInventoryVO productInventoryVO){
		return baseMapper.getPage(page,productInventoryVO);
	}

}

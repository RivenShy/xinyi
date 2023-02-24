package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.service.IProductInventoryService;
import org.xyg.eshop.main.vo.ProductInventoryVO;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/productInventory")
@Api(value = "产品库存", tags = "产品库存")
public class ProductInventoryController extends RabbitController {

	private final IProductInventoryService productInventoryService;

	@GetMapping("/getInventoryList")
	@ApiOperation(value = "获取库存列表", notes = "获取库存列表")
	public R<List<ProductInventoryVO>> getInventoryList(ProductInventoryVO productInventoryVO) {
		return R.data(productInventoryService.getInventoryList(productInventoryVO));
	}

	@GetMapping("/getPage")
	@ApiOperation(value = "获取库存分页列表", notes = "获取库存分页列表")
	public R<IPage<ProductInventoryVO>> getPage(Query query , ProductInventoryVO productInventoryVO) {
		return R.data(productInventoryService.getPage(Condition.getPage(query) ,productInventoryVO));
	}

	@GetMapping("/save")
	@ApiOperation(value = "保存", notes = "保存")
	public R<String> save(ProductInventoryVO productInventoryVO) {
		return R.status(productInventoryService.save(productInventoryVO));
	}

}

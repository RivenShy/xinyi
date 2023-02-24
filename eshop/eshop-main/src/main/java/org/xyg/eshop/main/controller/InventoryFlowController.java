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
import org.xyg.eshop.main.service.IInventoryFlowService;
import org.xyg.eshop.main.vo.InventoryFlowVO;

@RestController
@AllArgsConstructor
@RequestMapping("/inventoryFlow")
@Api(value = "库存流水", tags = "库存流水")
public class InventoryFlowController extends RabbitController {

	private final IInventoryFlowService inventoryFlowService;

	@GetMapping("/getPage")
	@ApiOperation(value = "获取库存流水分页列表", notes = "获取库存流水分页列表")
	public R<IPage<InventoryFlowVO>> getPage(Query query , InventoryFlowVO inventoryFlowVO) {
		return R.data(inventoryFlowService.getPage(Condition.getPage(query) ,inventoryFlowVO));
	}

}

package org.xyg.eshop.main.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dto.StorefrontAcceptanceDTO;
import org.xyg.eshop.main.service.IStorefrontAcceptanceService;
import org.xyg.eshop.main.vo.StorefrontAcceptanceVO;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/acceptance")
@Api(value = "门店验收管理", tags = "门店验收管理")
public class StorefrontAcceptanceController extends RabbitController {

	private final IStorefrontAcceptanceService acceptanceService;

	@GetMapping("/getList")
	@ApiImplicitParam(name = "headId", value = "头id", paramType = "query", dataType = "long")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "根据头id查询集合数据")
	public R<List<StorefrontAcceptanceVO>> getList(@RequestParam(value = "headId") Long headId) {
		return R.data(acceptanceService.getList(headId));
	}

	@PostMapping("/save")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "新增或修改门店验收数据")
	public R<StorefrontAcceptanceVO> save(@Validated @RequestBody StorefrontAcceptanceDTO entity) {
		return R.data(acceptanceService.save(entity));
	}

	@GetMapping("/detail")
	@ApiImplicitParam(name = "id", value = "主键id", paramType = "query", dataType = "long")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "根据主键id查询详情")
	public R<StorefrontAcceptanceVO> detail(@RequestParam(value = "id") Long id) {
		return R.data(acceptanceService.detail(id));
	}
}

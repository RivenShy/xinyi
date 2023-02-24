package org.xyg.eshop.main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.entity.ProductModelAdjunct;
import org.xyg.eshop.main.service.IProductModelAdjunctService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/adjunct")
@Api(value = "技术图纸信息管理", tags = "技术图纸信息管理")
public class ProductModelAdjunctController extends RabbitController {

	private final IProductModelAdjunctService productModelAdjunctService;

	@GetMapping("/getList")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "技术资料id", name = "technologyId", type = "query"),
		@ApiImplicitParam(value = "库存组织id", name = "organizationId", type = "query"),
		@ApiImplicitParam(value = "图纸名称", name = "adjunct", type = "query")
	})
	@ApiOperation(value = "获取图纸信息集合")
	public R<List<ProductModelAdjunct>> getList(@RequestParam(value = "technologyId", required = false) Long technologyId,
												@RequestParam(value = "organizationId", required = false) Long organizationId,
												@RequestParam(value = "adjunct" ,required = false) String adjunct) {
		return R.data(productModelAdjunctService.getList(technologyId, organizationId , adjunct));
	}
}

package org.xyg.eshop.main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.entity.Template;
import org.xyg.eshop.main.entity.TemplateRelation;
import org.xyg.eshop.main.service.ITemplateRelationService;
import org.xyg.eshop.main.service.ITemplateService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/template")
@Api(value = "产品库模板",description = "产品库模板")
public class TemplateController extends RabbitController {

	private final ITemplateService templateService;

	private final ITemplateRelationService relationService;

	@GetMapping("/detail")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "模板id", type = "query"),
		@ApiImplicitParam(name = "templateName", value = "模板名称", type = "query"),
		@ApiImplicitParam(name = "type", value = "填写类型 0 填写， 1 选择", type = "query"),
		@ApiImplicitParam(name = "language", value = "语言 US或ZHS 默认ZHS")
	})
	@ApiOperation(value = "根据模板id获取模板详情数据，（包括模板问题、答案）")
	public R<Template> detail(@RequestParam(value = "id",required = false) Long id,
							  @RequestParam(value = "templateName",required = false) String templateName,
							  @RequestParam(value = "type", required = false) Integer type,
							  @RequestParam(value = "language", required = false) String language) {
		return R.data(templateService.detail(id,templateName, type, language));
	}

	@PostMapping("/relationSave")
	@ApiOperation(value = "新增或修改模板关联数据",notes = "模板数据关联")
	public R<List<TemplateRelation>> save(@RequestBody List<TemplateRelation> param) {
		return R.data(relationService.saveData(param));
	}

	@GetMapping("/getRelationByHeadIdList")
	@ApiImplicitParam(name = "headId", value = "头id", paramType = "query", dataType = "long")
	@ApiOperation(value = "根据headId获取模板数据列表", notes = "模板数据关联")
	public R<List<TemplateRelation>> getRelationByHeadIdList(@RequestParam("headId") Long headId) {
		return R.data(relationService.getRelationByHeadIdList(headId));
	}

}

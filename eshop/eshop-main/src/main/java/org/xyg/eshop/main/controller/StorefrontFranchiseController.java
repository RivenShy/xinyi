package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.eshop.main.dto.StorefrontFranchiseDTO;
import org.xyg.eshop.main.query.StorefrontFranchiseQuery;
import org.xyg.eshop.main.service.IStorefrontFranchiseService;
import org.xyg.eshop.main.vo.StorefrontFranchiseVO;

@RestController
@AllArgsConstructor
@RequestMapping("/franchise")
@Api(value = "门店-加盟管理", tags = "门店-加盟管理")
public class StorefrontFranchiseController extends RabbitController {

	private final IStorefrontFranchiseService franchiseService;

	@GetMapping("/page")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "分页查询接口")
	public R<IPage<StorefrontFranchiseVO>> page(Query page, StorefrontFranchiseQuery query) {
		return R.data(franchiseService.selectPage(Condition.getPage(page), query));
	}

	@PostMapping("/addData")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "新增数据, 支持流程发起")
	public R<StorefrontFranchiseVO> addData(@RequestBody StorefrontFranchiseDTO entity) {
		return R.data(franchiseService.addData(entity));
	}

	@PostMapping("/save")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "保存或修改数据")
	public R<StorefrontFranchiseVO> save(@RequestBody StorefrontFranchiseDTO entity) {
		return R.data(franchiseService.createOrModify(entity));
	}

	@PostMapping("/update")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "更新数据, 支持流程发起和驳回")
	public R<StorefrontFranchiseVO> update(@RequestBody StorefrontFranchiseDTO entity) {
		return R.data(franchiseService.update(entity));
	}

	@GetMapping("/detail")
	@ApiImplicitParam(name = "id", value = "主键id", paramType = "query", dataType = "long")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "查看详情接口")
	public R<StorefrontFranchiseVO> detail(@RequestParam(value = "id") Long id) {
		return R.data(franchiseService.detail(id));
	}

	@DeleteMapping("/trueRemoveById")
	@ApiImplicitParam(name = "id", value = "主键id", paramType = "query", dataType = "long")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "删除数据")
	public R<Boolean> trueRemoveById(@RequestParam(value = "id") Long id) {
		return R.status(franchiseService.trueRemove(id));
	}

	@PostMapping("/updateStatus")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "主键id", paramType = "query", dataType = "long"), @ApiImplicitParam(name = "status", value = "状态值", paramType = "query", dataType = "integer")})
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "修改数据状态")
	public R<Boolean> updateStatus(@RequestParam(value = "id") Long id, @RequestParam(value = "status") Integer status) {
		return R.status(franchiseService.updateStatus(id, status));
	}

	@PostMapping("/flowInstanceExecutionStartCallback")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "流程实例任务开始回调接口", hidden = true)
	public R<CallBackMethodResDto> flowInstanceExecutionStartCallback(@RequestBody CallbackMethodReqDto inDto) {
		return R.data(franchiseService.flowInstanceExecutionStartCallback(inDto));
	}

	@PostMapping("/flowInstanceExecutionEndCallback")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "流程实例任务结束回调接口", hidden = true)
	public R<CallBackMethodResDto> flowInstanceExecutionEndCallback(@RequestBody CallbackMethodReqDto inDto) {
		return R.data(franchiseService.flowInstanceExecutionEndCallback(inDto));
	}
}

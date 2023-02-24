package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.service.IInteractRecordService;
import org.xyg.eshop.main.vo.InteractRecordVO;

@RestController
@AllArgsConstructor
@RequestMapping("/interactRecord")
@Api(value = "易车-互动记录", tags = "易车-互动记录")
public class InteractRecordController extends RabbitController {

	private final IInteractRecordService interactRecordService;

	@PostMapping("/save")
	@ApiOperation(value = "保存互动记录", notes = "保存互动记录")
	public R<Boolean> save(@RequestBody InteractRecordVO interactRecordVO) {
		return R.status(interactRecordService.saveInteractRecord(interactRecordVO));
	}

	@DeleteMapping("/deleteId")
	@ApiOperation(value = "根据id删除互动记录", notes = "根据id删除互动记录")
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
	public R<Boolean> deleteId(@RequestParam("id") Long id) {
		return R.status(interactRecordService.deleteId(id));
	}

	@GetMapping("/getPage")
	@ApiOperation(value = "获取互动记录分页列表", notes = "获取互动记录分页列表")
	public R<IPage<InteractRecordVO>> getPage(Query query , InteractRecordVO interactRecordVO) {
		return R.data(interactRecordService.getPage(Condition.getPage(query) ,interactRecordVO));
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "获取互动记录详情", notes = "获取互动记录详情")
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
	public R<InteractRecordVO> getDetail(Long id) {
		return R.data(interactRecordService.getDetail(id));
	}

}

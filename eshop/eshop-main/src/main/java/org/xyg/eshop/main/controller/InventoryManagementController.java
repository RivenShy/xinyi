package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.service.IInventoryManagementService;
import org.xyg.eshop.main.vo.InventoryManagementVO;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/inventoryManagement")
@Api(value = "出入库管理", tags = "出入库管理")
public class InventoryManagementController extends RabbitController {

	private final IInventoryManagementService inventoryManagementService;

	@GetMapping("/getPage")
	@ApiOperation(value = "分页列表", notes = "分页列表")
	public R<IPage<InventoryManagementVO>> getPage(Query query , InventoryManagementVO inventoryManagementVO) {
		return R.data(inventoryManagementService.getPage(Condition.getPage(query) ,inventoryManagementVO));
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存",notes = "保存")
	public R<String> save(@RequestBody InventoryManagementVO inventoryManagementVO){
		try {
			return R.status(inventoryManagementService.saveInventoryManagement(inventoryManagementVO));
		} catch (Exception e) {
			log.error("保存出入库管理数据出现错误 -> {} {}",inventoryManagementVO ,e);
			return R.fail("保存失败");
		}
	}

	@PostMapping("/submit")
	@ApiOperation(value = "提交",notes = "提交")
	public R<String> submit(@RequestBody InventoryManagementVO inventoryManagementVO){
		try {
			Long id = inventoryManagementService.submit(inventoryManagementVO);
//			inventoryManagementService.startProcess(id);
			return R.status(true);
		} catch (Exception e) {
			log.error("提交出入库管理数据出现错误 -> {} {}",inventoryManagementVO ,e);
			return R.fail("提交失败");
		}
	}

	@PostMapping("/update")
	@ApiOperation(value = "修改",notes = "修改")
	public R<String> updateInventoryManagement(@RequestBody InventoryManagementVO inventoryManagementVO){
		try {
			return R.status(inventoryManagementService.updateInventoryManagement(inventoryManagementVO));
		} catch (Exception e) {
			log.error("修改出入库管理数据出现错误 -> {} {}",inventoryManagementVO ,e);
			return R.fail("修改失败");
		}
	}

	@GetMapping("/detail")
	@ApiOperation(value = "详情",notes = "详情")
	public R<InventoryManagementVO> detail(@RequestParam("id")Long id){
		return R.data(inventoryManagementService.detail(id));
	}

	@GetMapping("/delete")
	@ApiOperation(value = "删除",notes = "删除")
	public R<String> delete(@RequestParam("id") Long id){
		try {
			return R.status(inventoryManagementService.delete(id));
		} catch (Exception e) {
			log.error("删除出入库管理数据出现错误 -> {} {}", id ,e);
			return R.fail("删除失败");
		}
	}

	@GetMapping("/lines-delete")
	@ApiOperation(value = "行表删除",notes = "行表删除")
	public R<String> linesDelete(@RequestParam("ids") String ids){
		try {
			return R.status(inventoryManagementService.linesDelete(ids));
		} catch (Exception e) {
			log.error("删除出入库管理行表数据出现错误 -> {} {}", ids ,e);
			return R.fail("删除失败");
		}
	}

	/*@PostMapping("/flowInstanceExecutionStartCallback")
	@ApiOperation(value = "流程实例任务开始回调接口")
	public R<CallBackMethodResDto> flowInstanceExecutionStartCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			return R.data(inventoryManagementService.flowInstanceExecutionStartCallback(inDto));
		} catch (Exception e) {
			log.error("出入库管理流程实例任务开始回调接口出现错误 ",e);
			return R.fail(e.getMessage());
		}
	}

	@PostMapping("/flowInstanceExecutionEndCallback")
	@ApiOperation(value = "流程实例任务结束回调接口")
	public R<CallBackMethodResDto> flowInstanceExecutionEndCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			return R.data(inventoryManagementService.flowInstanceExecutionEndCallback(inDto));
		} catch (Exception e) {
			log.error("出入库管理流程实例任务结束回调接口出现错误 ",e);
			return R.fail(e.getMessage());
		}
	}*/

}

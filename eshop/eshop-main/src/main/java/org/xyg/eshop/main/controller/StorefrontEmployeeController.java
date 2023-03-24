package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.service.IStorefrontEmployeeService;
import org.xyg.eshop.main.vo.StorefrontEmployeeVO;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/storefrontEmployee")
@Api(value = "门店员工",tags = "门店员工")
public class StorefrontEmployeeController extends RabbitController {

	private final IStorefrontEmployeeService employeeService;

	@ApiOperation(value = "保存", notes = "保存")
	@PostMapping("/save")
	public R<Long> employeeDetail(@RequestBody StorefrontEmployeeVO employeeVO) {
		return R.data(employeeService.saveEmployee(employeeVO));
	}

	@ApiOperation("获取门店员工分页列表")
	@GetMapping("/getPage")
	public R<IPage<StorefrontEmployeeVO>> employeeList(StorefrontEmployeeVO employeeVO, Query query) {
		IPage<StorefrontEmployeeVO> page = new Page<>();
		page.setCurrent(query.getCurrent() == null ? 1 : query.getCurrent());
		page.setSize(query.getSize() == null ? 100 : query.getSize());
		return R.data(employeeService.getPage(page,employeeVO));
	}

	@ApiOperation(value = "获取门店员工详情", notes = "empno或id二选一")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "empno", value = "员工工号逗号分隔", type = "query"),
		@ApiImplicitParam(name = "id", value = "员工id逗号分隔", type = "query")
	})
	@GetMapping("/detail")
	public R<List<StorefrontEmployeeVO>> detail(@RequestParam(required = false,value = "empno") String empno,
										@RequestParam(required = false,value = "ids") String ids) {
		return R.data(employeeService.getDetail(ids, empno));
	}

	@ApiOperation(value = "修改", notes = "修改")
	@PostMapping("/update")
	public R<StorefrontEmployeeVO> update(@RequestBody StorefrontEmployeeVO employeeVO) {
		return R.data(employeeService.updateEmployee(employeeVO));
	}

	@ApiOperation(value = "删除", notes = "删除")
	@DeleteMapping("/delete")
	public R<String> delete(@RequestParam("ids") String ids) {
		return R.status(employeeService.delete(ids));
	}

}

package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.tenant.annotation.NonDS;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.service.IStorefrontService;
import org.xyg.eshop.main.vo.AdvancedSearchVO;
import org.xyg.eshop.main.vo.StorefrontVO;

import java.util.List;

@Slf4j
@NonDS
@RestController
@RequestMapping("/storefront")
@Api(value = "门店", tags = "门店")
public class StorefrontController extends RabbitController {
	@Autowired
	private IStorefrontService storefrontService;

	@PostMapping("/saveOrUpdate")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "修改参数", value = "Storefront.class", paramType = "update")
	})
	@ApiOperation(value = "新增或修改门店", notes = "传入storefrontVO,修改时只需要Storefront对象")
	public R<StorefrontVO> saveOrUpdate(@RequestBody StorefrontVO storefrontVO) {
		return R.data(storefrontService.saveOrUpdate(storefrontVO));
	}

	@PostMapping("/submit")
	@ApiOperation(value = "提交", notes = "提交")
	public R<Long> submit(@RequestBody StorefrontVO storefrontVO) {
		Long id = storefrontService.submit(storefrontVO);
		storefrontService.startProcess(id);
		return R.data(id);
	}

	@PostMapping("/update")
	@ApiOperation(value = "修改门店", notes = "传入storefrontVO对象")
	public R<String> update(@RequestBody StorefrontVO storefrontVO) {
		return R.status(storefrontService.updateById(storefrontVO));
	}

	@GetMapping("/getList")
	@ApiOperation(value = "获取门店列表信息")
	public R<IPage<Storefront>> getList(@RequestParam(value = "storefrontName", required = false) String storefrontName,
										 @RequestParam(value = "status", required = false) Integer status,
										 @RequestParam(value = "storefrontLevel", required = false) Integer storefrontLevel,
										 @RequestParam(value = "salesrepId", required = false) Long salesrepId,
										 @RequestParam(value = "selectStatus", defaultValue = "0") Integer selectStatus,
										 @RequestParam(value = "companyLogo", required = false) String companyLogo,
										 Query query, RabbitUser rabbitUser) {
		return storefrontService.getList(storefrontName, status, storefrontLevel, salesrepId, selectStatus, Condition.getPage(query), rabbitUser.getUserId(), companyLogo);
	}

	@GetMapping("/getDetail")
	@ApiImplicitParam(name = "partyId", value = "门店id", paramType = "query", required = true)
	@ApiOperation(value = "获取门店信息详情")
	public R<StorefrontVO> getDetail(@RequestParam(value = "id") Long id) {
		return storefrontService.getDetail(id);
	}

	@PostMapping("/updateStatus")
	@ApiOperation(value = "修改门店状态为注销或者黑名单,审批通过", notes = "门店对象,只需要id、status(注销为3,黑名单为4,正常2)、handleReason(拉入黑名单/注销的原因)三个参数")
	public R<String> updateStatus(@RequestBody Storefront storefront) {
		return storefrontService.updateStatus(storefront);
	}

	@PostMapping("/advancedSearch")
	@ApiOperation(value = "门店高级搜索")
	public R<IPage<Storefront>> advancedSearch(@RequestBody List<AdvancedSearchVO> advancedSearchVOList, Query query) {
		return storefrontService.advancedSearch(advancedSearchVOList, Condition.getPage(query));
	}

	@GetMapping("/getPage")
	@ApiOperation(value = "获取门店列表信息")
	public R<IPage<StorefrontVO>> getPage(Query query,StorefrontVO storefrontVO) {
		return R.data(storefrontService.getPage(Condition.getPage(query),storefrontVO));
	}

	@GetMapping("/getByStorefrontNameFullMatch")
	@ApiOperation(value = "根据门店名称全匹配搜索")
	public R<Storefront> getByStorefrontNameFullMatch(@RequestParam("storefrontName") String storefrontName) {
		return R.data(storefrontService.getByStorefrontNameFullMatch(storefrontName));
	}

	@GetMapping("/getAllByPage")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "companyLogo", value = "门店所属公司标识{'qb','jb','jt'}", paramType = "query"),
		@ApiImplicitParam(name = "storefrontName", value = "门店名称", paramType = "query"),
		@ApiImplicitParam(name = "status", value = "门店状态", paramType = "query"),
		@ApiImplicitParam(name = "storefrontLevel", value = "门店等级", paramType = "query"),
		@ApiImplicitParam(name = "salesprepName", value = "业务员名称", paramType = "query"),
		@ApiImplicitParam(name = "saleAreaName", value = "销售区域", paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query"),
		@ApiImplicitParam(name = "partyShortName" , value = "门店别名" , type = "query")
	})
	@ApiOperation(value = "获取全部门店列表信息")
	public R<IPage<StorefrontVO>> getAllByPage(@RequestParam(value = "companyLogo", required = false)	String companyLogo,
										   	  	@RequestParam(value = "storefrontName" , required = false) String storefrontName,
										      	@RequestParam(value = "status" , required = false) String status,
										      	@RequestParam(value = "storefrontLevel" , required = false) Integer storefrontLevel,
										      	@RequestParam(value = "salesprepName" , required = false) String salesprepName,
										      	@RequestParam(value = "saleAreaName" , required = false) String saleAreaName,
										      	@RequestParam(value = "startDate" , required = false) String startDate ,
										      	@RequestParam(value = "endDate" , required = false) String endDate,
										      	@RequestParam(value = "salesType" , required = false) String salesType,
												@RequestParam(value = "partyShortName" , required = false) String partyShortName,
												Query query) {
		return storefrontService.getAllByPage(companyLogo, storefrontName, status, storefrontLevel, salesprepName, saleAreaName, startDate, endDate, salesType, partyShortName, Condition.getPage(query));
	}

	@PostMapping("/flowInstanceExecutionStartCallback")
	@ApiOperation(value = "流程实例任务开始回调接口")
	public R<CallBackMethodResDto> flowInstanceExecutionStartCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			return R.data(storefrontService.flowInstanceExecutionStartCallback(inDto));
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	@PostMapping("/flowInstanceExecutionEndCallback")
	@ApiOperation(value = "流程实例任务结束回调接口")
	public R<CallBackMethodResDto> flowInstanceExecutionEndCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			return R.data(storefrontService.flowInstanceExecutionEndCallback(inDto));
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

}

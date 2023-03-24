package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dto.ActivityDTO;
import org.xyg.eshop.main.entity.ActivityStore;
import org.xyg.eshop.main.service.IActivityService;
import org.xyg.eshop.main.vo.ActivityStoreVO;
import org.xyg.eshop.main.vo.ActivityVO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/activity")
@Api(value = "活动", tags = "活动")
public class ActivityController extends RabbitController {
	@Autowired
	private IActivityService activityService;

	@PostMapping("/save")
	@ApiOperation(value = "新增活动", notes = "传入 activityDTO")
	public R<Boolean> save(@RequestBody ActivityDTO activityDTO) {
		return activityService.insertData(activityDTO);
	}

	@GetMapping("/getPage")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "activityName", value = "activityName", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "activityType", value = "activityType", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "activityStartDate", value = "activityStartDate", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "activityEndDate", value = "activityEndDate", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "activityStatusList", value = "activityStatusList", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "分页条件查询")
	public R<IPage<ActivityVO>> getPage(Query query, ActivityDTO activityDTO) {
		return activityService.selectActivityPage(query, activityDTO);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "活动详情", notes = "传入 活动id")
	@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true, dataType = "Long")
	public R<ActivityVO> getDetail(@RequestParam("id") Long id) {
		return activityService.getDetail(id);
	}

	@PostMapping("/deleteActivityCommodity")
	@ApiOperation(value = "删除活动商品", notes = "传入 活动商品id")
	@ApiImplicitParam(name = "id", value = "id", paramType = "delete", dataType = "Long")
	public R<Boolean> deleteActivityCommodity(@RequestParam("id") Long id) {
		return activityService.deleteActivityCommodity(id);
	}

	@PostMapping("/joinActivity")
	@ApiOperation(value = "门店参与活动", notes = "传入 id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "activityId", value = "activityId", paramType = "add", dataType = "Long"),
		@ApiImplicitParam(name = "storefrontId", value = "storefrontId", paramType = "add", dataType = "Long")
	})
	public R<Boolean> joinActivity(@RequestBody ActivityStore activityStore) {
		return activityService.joinActivity(activityStore);
	}

	@GetMapping("/getActivityStatistics")
	@ApiOperation(value = "活动统计", notes = "传入 活动id")
	@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true, dataType = "Long")
	public R<ActivityVO> getActivityStatistics(@RequestParam("id") Long id) {
		return activityService.selectActivityStatisticsById(id);
	}

	@PostMapping("/reviewActivityStore")
	@ApiOperation(value = "活动门店审核通过", notes = "传入 活动id")
	@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true, dataType = "Long")
	public R<Boolean> reviewActivityStore(@RequestParam("id") Long id) {
		return activityService.reviewActivityStore(id);
	}

	@GetMapping("/getActivityStorePage")
	@ApiOperation(value = "门店参与活动列表")
	public R<IPage<ActivityStoreVO>> getActivityStorePage(Query query) {
		return activityService.selectActivityStorePage(query);
	}

	@PostMapping("/reviewActivityStoreNotPass")
	@ApiOperation(value = "活动门店审核不通过", notes = "传入活动门店id")
	@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true, dataType = "Long")
	public R<Boolean> reviewActivityStoreNotPass(@RequestParam("id") Long id) {
		return activityService.reviewActivityStoreNotPass(id);
	}
}

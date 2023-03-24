package org.xyg.eshop.main.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xyg.eshop.main.service.IActivityService;

@Component
@Slf4j
public class ActivityScheduleJob {

	@Autowired
	private IActivityService activityService;

	@XxlJob("updateActivityStatus")
	public ReturnT<String> updateActivityStatus(String param) {
		XxlJobLogger.log("执行eshop更新活动状态任务》》》》》》开始");
		log.info("执行eshop更新活动状态任务》》》》》》开始");
		activityService.updateActivityStatus();
		log.info("执行eshop更新活动状态任务》》》》》》结束");
		XxlJobLogger.log("执行eshop更新活动状态任务》》》》》》结束");
		return ReturnT.SUCCESS;
	}
}

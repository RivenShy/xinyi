package org.xyg.eshop.main.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xyg.eshop.main.service.IMemberService;

@Component
@Slf4j
public class MemberScheduleJob {

	@Autowired
	private IMemberService memberService;

	@XxlJob("updateMemberStatus")
	public ReturnT<String> updateMemberStatus(String param) {
		XxlJobLogger.log("执行eshop查询到期会员，更新会员状态任务》》》》》》开始");
		log.info("执行eshop查询到期会员，更新会员状态任务》》》》》》开始");
		memberService.updateMemberStatus();
		log.info("执行eshop查询到期会员，更新会员状态任务》》》》》》结束");
		XxlJobLogger.log("执行eshop查询到期会员，更新会员状态任务》》》》》》结束");
		return ReturnT.SUCCESS;
	}
}

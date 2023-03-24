package org.xyg.eshop.main.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xyg.eshop.main.service.IErpBusinessEntityService;

@Component
@AllArgsConstructor
@Slf4j
public class TimerJob {

	private final IErpBusinessEntityService erpBusinessEntityService;

	@XxlJob("syncBusinessEntitiesFromErp")
	public ReturnT<String> syncBusinessEntitiesFromErp(String param) {
		XxlJobLogger.log("易车系统同步ERP业务实体数据任务》》》》》》开始");
		log.info("易车系统同步ERP业务实体数据任务》》》》》》开始");
		erpBusinessEntityService.syncBusinessEntitiesFromErp();
		log.info("易车系统同步ERP业务实体数据》》》》》》结束");
		XxlJobLogger.log("易车系统同步ERP业务实体数据任务》》》》》》结束");
		return ReturnT.SUCCESS;
	}

}

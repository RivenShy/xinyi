package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dto.ActivityDTO;
import org.xyg.eshop.main.entity.Activity;
import org.xyg.eshop.main.entity.ActivityStore;
import org.xyg.eshop.main.vo.ActivityStoreVO;
import org.xyg.eshop.main.vo.ActivityVO;

public interface IActivityService extends BaseService<Activity> {

	R<Boolean> insertData(ActivityDTO activityDTO);

	R<IPage<ActivityVO>> selectActivityPage(Query query, ActivityDTO activityDTO);

	R<ActivityVO> getDetail(Long id);

	R<Boolean> deleteActivityCommodity(Long id);

	R<Boolean> joinActivity(ActivityStore activityStore);

	R<ActivityVO> selectActivityStatisticsById(Long id);

	R<Boolean> reviewActivityStore(Long id);

	void updateActivityStatus();

	R<IPage<ActivityStoreVO>> selectActivityStorePage(Query query);

	R<Boolean> reviewActivityStoreNotPass(Long id);
}

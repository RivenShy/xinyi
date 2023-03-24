package org.xyg.eshop.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.xyg.eshop.main.entity.ActivityCommodity;
import org.xyg.eshop.main.mapper.ActivityCommodityMapper;
import org.xyg.eshop.main.service.IActivityCommodityService;
import org.xyg.eshop.main.vo.ActivityCommodityVO;

import java.util.List;

@Slf4j
@Service
public class ActivityCommodityServiceImpl extends BaseServiceImpl<ActivityCommodityMapper, ActivityCommodity> implements IActivityCommodityService {

	@Override
	public List<ActivityCommodityVO> selectByActivityId(Long id) {
		List<ActivityCommodity> activityCommodityList = lambdaQuery()
			.eq(ActivityCommodity::getActivityId, id)
			.list();
		List<ActivityCommodityVO> activityCommodityVOList = BeanUtil.copy(activityCommodityList, ActivityCommodityVO.class);
		return activityCommodityVOList;
	}
}

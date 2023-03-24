package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.ActivityCommodity;
import org.xyg.eshop.main.vo.ActivityCommodityVO;

import java.util.List;

public interface IActivityCommodityService extends BaseService<ActivityCommodity> {

	List<ActivityCommodityVO> selectByActivityId(Long id);
}

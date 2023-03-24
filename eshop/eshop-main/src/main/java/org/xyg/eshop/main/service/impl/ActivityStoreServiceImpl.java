package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.Activity;
import org.xyg.eshop.main.entity.ActivityStore;
import org.xyg.eshop.main.mapper.ActivityStoreMapper;
import org.xyg.eshop.main.service.IActivityService;
import org.xyg.eshop.main.service.IActivityStoreService;
import org.xyg.eshop.main.service.ICommonService;
import org.xyg.eshop.main.vo.ActivityStoreVO;
import org.xyg.eshop.main.vo.ActivityVO;

import java.util.List;

@Slf4j
@Service
public class ActivityStoreServiceImpl extends BaseServiceImpl<ActivityStoreMapper, ActivityStore> implements IActivityStoreService {

	@Autowired
	private ICommonService commonService;

//	@Autowired
//	private IActivityService activityService;

	@Override
	public List<ActivityStoreVO> selectActivityStoreByActivityId(Long activityId) {
		return baseMapper.selectActivityStoreByActivityId(activityId);
	}

	@Override
	public R<IPage<ActivityStoreVO>> selectActivityStorePage(Query query) {
		IPage<ActivityStoreVO> page = Condition.getPage(query);
		List<ActivityStoreVO> activityStoreVOList = baseMapper.selectActivityStorePage(page);
		fillData(activityStoreVOList);
		return R.data(page.setRecords(activityStoreVOList));
	}

	private void fillData(List<ActivityStoreVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}
		for (ActivityStoreVO activityStoreVO : list) {
			fillActivityCommonData(activityStoreVO);
		}
	}

	private void fillActivityCommonData(ActivityStoreVO activityStoreVO) {
		if (activityStoreVO == null){
			return;
		}
		String imageIsQualifiedName = commonService.getDictValue(EShopMainConstant.YES_NO_DICT_CODE, activityStoreVO.getImageIsQualified());
		activityStoreVO.setImageIsQualifiedName(imageIsQualifiedName);
//		Activity activity = activityService.getById(activityStoreVO.getActivityId());
//		if(activity != null) {
//			activityStoreVO.setActivityName(activity.getActivityName());
//		}
	}
}

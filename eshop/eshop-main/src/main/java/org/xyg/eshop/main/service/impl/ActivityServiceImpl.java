package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.secure.utils.AuthUtil;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.DateUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.system.cache.DictCache;
import org.springrabbit.system.entity.Dict;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dto.ActivityDTO;
import org.xyg.eshop.main.entity.*;
import org.xyg.eshop.main.enums.*;
import org.xyg.eshop.main.mapper.ActivityMapper;
import org.xyg.eshop.main.service.*;
import org.xyg.eshop.main.vo.ActivityCommodityVO;
import org.xyg.eshop.main.vo.ActivityStoreVO;
import org.xyg.eshop.main.vo.ActivityVO;
import org.xyg.eshop.main.vo.MemberVO;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ActivityServiceImpl extends BaseServiceImpl<ActivityMapper, Activity> implements IActivityService {

	@Autowired
	private IActivityCommodityService activityCommodityService;

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private IActivityStoreService activityStoreService;

	@Autowired
	private IStorefrontService storefrontService;

	@Autowired
	private ICommonService commonService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> insertData(ActivityDTO activityDTO) {
		if(Func.isEmpty(activityDTO.getActivityName())) {
			return R.fail("缺少必要的请求参数：activityName");
		}
		if(Func.isEmpty(activityDTO.getActivityScope())) {
			return R.fail("缺少必要的请求参数：activityScope");
		}
		if(Func.isEmpty(activityDTO.getActivityType())) {
			return R.fail("缺少必要的请求参数：activityType");
		}
		if(Func.isEmpty(activityDTO.getActivityStartDate())) {
			return R.fail("缺少必要的请求参数：activityStartDate");
		}
		if(Func.isEmpty(activityDTO.getActivityEndDate())) {
			return R.fail("缺少必要的请求参数：activityEndDate");
		}
		if(Func.isEmpty(activityDTO.getActivityCommodity())) {
			return R.fail("缺少必要的请求参数：activityCommodity");
		}
		activityDTO.setStatus(ActivityStatusEnum.NOT_STARTED.getIndex());
		boolean saveActivity = saveOrUpdate(activityDTO);
		if(saveActivity) {
			List<ActivityCommodity> activityCommodityList = activityDTO.getActivityCommodityList();
			if (!Func.isEmpty(activityCommodityList)) {
				for(ActivityCommodity activityCommodity : activityCommodityList) {
					if(Func.isEmpty(activityCommodity.getCommodityId())) {
						return R.fail("缺少必要的请求你参数：commodityId");
					}
					activityCommodity.setActivityId(activityDTO.getId());
				}
				boolean saveActivityCommodity = activityCommodityService.saveOrUpdateBatch(activityCommodityList);
				if (!saveActivityCommodity) {
					throw new RuntimeException("新增活动商品失败");
				}
			}
		}
		return R.status(saveActivity);
	}

	@Override
	public R<IPage<ActivityVO>> selectActivityPage(Query query, ActivityDTO activityDTO) {
		IPage<ActivityVO> page = Condition.getPage(query);
		List<ActivityVO> activityVOList = activityMapper.selectActivityPage(page, activityDTO);
		fillData(activityVOList);
		return R.data(page.setRecords(activityVOList));
	}

	private void fillData(List<ActivityVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}
		for (ActivityVO activityVO : list) {
			fillActivityCommonData(activityVO);
		}
	}

	@Override
	public R<ActivityVO> getDetail(Long id) {
		Activity activity = getById(id);
		if(activity == null) {
			return R.fail("查询会员信息失败");
		}
		ActivityVO activityVO = BeanUtil.copy(activity, ActivityVO.class);
		fillDetailData(activityVO);
		return R.data(activityVO);
	}


	private void fillDetailData(ActivityVO activityVO){
		if (activityVO == null){
			return;
		}
		// 查询活动商品
		List<ActivityCommodityVO> activityCommodityVOList = activityCommodityService.selectByActivityId(activityVO.getId());
		activityVO.setActivityCommodityList(activityCommodityVOList);
		fillActivityCommonData(activityVO);
	}

	private void fillActivityCommonData(ActivityVO activityVO){
		if (activityVO == null){
			return;
		}
		String dictStatus = activityVO.getStatus() == null ? null : activityVO.getStatus().toString();
		String statusName = commonService.getDictValue(EShopMainConstant.ESHOP_ACTIVITY_STATUS_DICT_CODE, dictStatus);
		activityVO.setStatusName(statusName);
		String activityTypeName = commonService.getDictValue(EShopMainConstant.ACTIVITY_TYPE_DICT_CODE, activityVO.getActivityType());
		activityVO.setActivityTypeName(activityTypeName);
		String activityScopeName = commonService.getDictValue(EShopMainConstant.ESHOP_ACTIVITY_SCOPE_DICT_CODE, activityVO.getActivityScope());
		activityVO.setActivityScopeName(activityScopeName);
	}

	@Override
	public R<Boolean> deleteActivityCommodity(Long id) {
		return R.status(activityCommodityService.removeById(id));
	}

	@Override
	public R<Boolean> joinActivity(ActivityStore activityStore) {
		if(Func.isEmpty(activityStore.getActivityId())) {
			return R.fail("缺少必要的请求参数：activityId");
		}
		if(Func.isEmpty(activityStore.getStorefrontId())) {
			return R.fail("缺少必要的请求参数：storefrontId");
		}
		Activity activity = getById(activityStore.getActivityId());
		if(activity == null) {
			return R.fail("查询活动信息失败");
		}
		String nowDateStr = DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATE);
		Date nowDate = DateUtil.parse(nowDateStr, DateUtil.PATTERN_DATE);
		if(nowDate.before(activity.getActivityStartDate())) {
			return R.fail("活动未开始");
		}
		if(nowDate.equals(activity.getActivityEndDate()) || nowDate.after(activity.getActivityEndDate())) {
			return R.fail("活动已过期");
		}
		Storefront storefront = storefrontService.getById(activityStore.getStorefrontId());
		if(storefront == null) {
			return R.fail("查询门店信息失败");
		}
		String storefrontType = DictCache.getValue(EShopMainConstant.ESHOP_STORE_NATURE_DICT_CODE, storefront.getType());
		if(storefrontType == null) {
			return R.fail("查询门店性质失败");
		}
		if(!storefrontType.equals(StoreNatureEnum.DIRECT_STORE.getName()) && !storefrontType.equals(StoreNatureEnum.JOIN_STORE.getName())) {
			return R.fail("该门店类型性质不在活动范围");
		}
		String activityScope = DictCache.getValue(EShopMainConstant.ESHOP_ACTIVITY_SCOPE_DICT_CODE, activity.getActivityScope());
		if(activityScope == null) {
			return R.fail("查询活动范围失败");
		}
		if(activityScope.equals(ActivityScopeEnum.DIRECT_STORE.getName())) {
			if(!storefrontType.equals(StoreNatureEnum.DIRECT_STORE.getName())) {
				return R.fail("活动范围仅直营店");
			}
		} else if(activityScope.equals(ActivityScopeEnum.JOIN_STORE.getName())) {
			if(!storefrontType.equals(StoreNatureEnum.JOIN_STORE.getName())) {
				return R.fail("活动范围仅加盟店");
			}
		}
		RabbitUser user = AuthUtil.getUser();
		if(!Func.isEmpty(user)) {
			activityStore.setImageUploadUserId(user.getUserId());
		}
		activityStore.setImageUploadDate(nowDate);
		// 默认合格，在后台可设置不合格
		activityStore.setImageIsQualified(String.valueOf(YesNoEnum.YES.getIndex()));
		return R.status(activityStoreService.save(activityStore));
	}

	@Override
	public R<ActivityVO> selectActivityStatisticsById(Long id) {
		ActivityVO activityVO = activityMapper.selectActivityStatisticsById(id);
//		List<ActivityStore> activityStoreList = activityStoreService.lambdaQuery()
//			.eq(ActivityStore::getActivityId, id)
//			.list();
//		List<ActivityStoreVO> activityStoreVOList = BeanUtil.copy(activityStoreList, ActivityStoreVO.class);
		List<ActivityStoreVO> activityStoreVOList = activityStoreService.selectActivityStoreByActivityId(id);
		activityVO.setActivityStoreList(activityStoreVOList);
		return R.data(activityVO);
	}

	@Override
	public R<Boolean> reviewActivityStore(Long id) {
		Long reviewerId = null;
		RabbitUser user = AuthUtil.getUser();
		if(!Func.isEmpty(user)) {
			reviewerId = user.getUserId();
		}
		boolean reviewActivityStore = activityStoreService.lambdaUpdate()
			.set(ActivityStore::getImageIsQualified, YesNoEnum.YES.getIndex())
			.set(ActivityStore::getReviewerId, reviewerId)
			.eq(ActivityStore::getId, id)
			.update();
		return R.status(reviewActivityStore);
	}

	@Override
	public void updateActivityStatus() {
		List<Dict> activityStatusDictList = DictCache.getList(EShopMainConstant.ESHOP_ACTIVITY_STATUS_DICT_CODE);
		String expireKey = null;
		String inProgressKey = null;
		for(Dict dict : activityStatusDictList) {
			if(dict.getDictValue().equals(ActivityStatusEnum.EXPIRED.getName())) {
				expireKey = dict.getDictKey();
			} else if(dict.getDictValue().equals(ActivityStatusEnum.IN_PROGRESS.getName())) {
				inProgressKey = dict.getDictKey();
			}
		}
		lambdaUpdate().set(Activity::getStatus, expireKey)
			.lt(Activity::getActivityEndDate, new Date())
			.ne(Activity::getStatus, expireKey)
			.update();
		lambdaUpdate().set(Activity::getStatus, inProgressKey)
			.lt(Activity::getActivityStartDate, new Date())
			.gt(Activity::getActivityEndDate, new Date())
			.ne(Activity::getStatus, inProgressKey)
			.update();
	}

	@Override
	public R<IPage<ActivityStoreVO>> selectActivityStorePage(Query query) {
		return activityStoreService.selectActivityStorePage(query);
	}

	@Override
	public R<Boolean> reviewActivityStoreNotPass(Long id) {
		Long reviewerId = null;
		RabbitUser user = AuthUtil.getUser();
		if(!Func.isEmpty(user)) {
			reviewerId = user.getUserId();
		}
		boolean reviewActivityStore = activityStoreService.lambdaUpdate()
			.set(ActivityStore::getImageIsQualified, YesNoEnum.NO.getIndex())
			.set(ActivityStore::getReviewerId, reviewerId)
			.eq(ActivityStore::getId, id)
			.update();
		return R.status(reviewActivityStore);
	}
}

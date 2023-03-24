package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.entity.ActivityStore;
import org.xyg.eshop.main.vo.ActivityStoreVO;
import org.xyg.eshop.main.vo.ActivityVO;

import java.util.List;

public interface IActivityStoreService extends BaseService<ActivityStore> {
    List<ActivityStoreVO> selectActivityStoreByActivityId(Long activityId);

	R<IPage<ActivityStoreVO>> selectActivityStorePage(Query query);
}

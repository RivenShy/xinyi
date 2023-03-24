package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.xyg.eshop.main.entity.ActivityStore;
import org.xyg.eshop.main.vo.ActivityStoreVO;

import java.util.List;

@Mapper
public interface ActivityStoreMapper extends BaseMapper<ActivityStore> {

    List<ActivityStoreVO> selectActivityStoreByActivityId(Long activityId);

	List<ActivityStoreVO> selectActivityStorePage(IPage<ActivityStoreVO> page);
}

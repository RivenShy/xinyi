package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.dto.ActivityDTO;
import org.xyg.eshop.main.entity.Activity;
import org.xyg.eshop.main.vo.ActivityVO;

import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    List<ActivityVO> selectActivityPage(IPage<ActivityVO> page, @Param("activity") ActivityDTO activityDTO);

	ActivityVO selectActivityStatisticsById(Long id);
}

package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.InteractRecord;
import org.xyg.eshop.main.vo.InteractRecordVO;

@Mapper
public interface InteractRecordMapper extends BaseMapper<InteractRecord> {

    IPage<InteractRecordVO> getPage(IPage<InteractRecordVO> page,@Param("vo") InteractRecordVO interactRecordVO);

}

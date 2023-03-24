package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xyg.eshop.main.entity.MemberFlow;
import org.xyg.eshop.main.vo.MemberFlowVO;

import java.util.List;

@Mapper
public interface MemberFlowMapper extends BaseMapper<MemberFlow> {
	List<MemberFlowVO> selectMemberFlowListByMemberId(Long memberId);
}

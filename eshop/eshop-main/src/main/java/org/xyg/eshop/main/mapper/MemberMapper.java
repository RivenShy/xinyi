package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.dto.MemberDTO;
import org.xyg.eshop.main.entity.Member;
import org.xyg.eshop.main.vo.MemberVO;

import java.util.List;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
    List<MemberVO> selectMemberPage(IPage<MemberVO> page, @Param("member") MemberDTO memberDTO);
}

package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dto.MemberDTO;
import org.xyg.eshop.main.dto.MemberFlowDTO;
import org.xyg.eshop.main.entity.Member;
import org.xyg.eshop.main.vo.MemberFlowVO;
import org.xyg.eshop.main.vo.MemberVO;

import java.util.List;

public interface IMemberService extends BaseService<Member> {
	R<Boolean> insertData(MemberDTO memberDTO);

	R<MemberVO> getDetail(Long id);

    R<IPage<MemberVO>> selectMemberPage(Query query, MemberDTO memberDTO);

	void checkDelayExpireMember(Member member);

	R<Boolean> renew(MemberFlowDTO memberFlowDTO);

	R<Boolean> activation(MemberFlowDTO memberFlowDTO);

	R<Boolean> vehicleInspection(MemberDTO membermemberFlowDTO);

	R<List<MemberFlowVO>> selectMemberFlowListByMemberId(Long id);

	void updateMemberStatus();
}

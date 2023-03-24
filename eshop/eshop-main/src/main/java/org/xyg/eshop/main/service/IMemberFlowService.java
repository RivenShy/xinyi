package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.MemberFlow;
import org.xyg.eshop.main.vo.MemberFlowVO;

import java.util.List;

public interface IMemberFlowService extends BaseService<MemberFlow> {

	List<MemberFlowVO> selectMemberFlowListByMemberId(Long memberId);
}

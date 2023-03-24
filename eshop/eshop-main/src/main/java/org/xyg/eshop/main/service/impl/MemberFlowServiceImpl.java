package org.xyg.eshop.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.entity.MemberFlow;
import org.xyg.eshop.main.mapper.MemberFlowMapper;
import org.xyg.eshop.main.service.IMemberFlowService;
import org.xyg.eshop.main.vo.MemberFlowVO;

import java.util.List;

@Slf4j
@Service
public class MemberFlowServiceImpl extends BaseServiceImpl<MemberFlowMapper, MemberFlow> implements IMemberFlowService {

	@Autowired
	private MemberFlowMapper memberFlowMapper;

	@Override
	public List<MemberFlowVO> selectMemberFlowListByMemberId(Long memberId) {
		return memberFlowMapper.selectMemberFlowListByMemberId(memberId);
	}
}

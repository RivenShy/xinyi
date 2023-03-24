package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.dto.MemberDTO;
import org.xyg.eshop.main.dto.MemberFlowDTO;
import org.xyg.eshop.main.entity.MemberFlow;
import org.xyg.eshop.main.service.IMemberService;
import org.xyg.eshop.main.vo.ContractVO;
import org.xyg.eshop.main.vo.MemberFlowVO;
import org.xyg.eshop.main.vo.MemberVO;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/member")
@Api(value = "会员信息",tags = "会员信息")
public class MemberController extends RabbitController {

	@Autowired
	private IMemberService memberService;

	@PostMapping("/save")
	@ApiOperation(value = "新增会员", notes = "传入 memberDTO")
	public R<Boolean> save(@RequestBody MemberDTO memberDTO) {
		return memberService.insertData(memberDTO);
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "会员详情", notes = "传入会员id")
	@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true, dataType = "Long")
	public R<MemberVO> getDetail(@RequestParam("id") Long id) {
		return memberService.getDetail(id);
	}

	@GetMapping("/getPage")
	@ApiOperation(value = "分页条件查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "memberCode", value = "memberCode", paramType = "query", required = false, dataType = "string"),
		@ApiImplicitParam(name = "memberName", value = "memberName", paramType = "query", required = false, dataType = "string"),
		@ApiImplicitParam(name = "plateNumber", value = "plateNumber", paramType = "query", required = false, dataType = "string"),
		@ApiImplicitParam(name = "memeberStatusList", value = "memeberStatusList", paramType = "query", required = false, dataType = "string")
	})
	public R<IPage<MemberVO>> getPage(Query query, MemberDTO memberDTO) {
		return memberService.selectMemberPage(query, memberDTO);
	}

	@PostMapping("/renew")
	@ApiOperation(value = "续费", notes = "传入 memberDTO")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "memberId", value = "memberId", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "carModel", value = "carModel", required = true, dataType = "string"),
		@ApiImplicitParam(name = "plateNumber", value = "plateNumber", required = true, dataType = "string"),
		@ApiImplicitParam(name = "termOfValidity", value = "termOfValidity", required = true, dataType = "string"),
		@ApiImplicitParam(name = "effectiveDate", value = "effectiveDate", required = true, dataType = "string"),
		@ApiImplicitParam(name = "expirationDate", value = "expirationDate", required = true, dataType = "string"),
		@ApiImplicitParam(name = "storefrontId", value = "storefrontId", required = true, dataType = "Long")
	})
	public R<Boolean> renew(@RequestBody MemberFlowDTO memberFlowDTO) {
		return memberService.renew(memberFlowDTO);
	}

	@PostMapping("/activation")
	@ApiOperation(value = "激活", notes = "传入 memberDTO")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "memberId", value = "memberId", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "carModel", value = "carModel", required = true, dataType = "string"),
		@ApiImplicitParam(name = "plateNumber", value = "plateNumber", required = true, dataType = "string"),
		@ApiImplicitParam(name = "termOfValidity", value = "termOfValidity", required = true, dataType = "string"),
		@ApiImplicitParam(name = "effectiveDate", value = "effectiveDate", required = true, dataType = "string"),
		@ApiImplicitParam(name = "expirationDate", value = "expirationDate", required = true, dataType = "string"),
		@ApiImplicitParam(name = "storefrontId", value = "storefrontId", required = true, dataType = "Long")
	})
	public R<Boolean> activation(@RequestBody MemberFlowDTO memberFlowDTO) {
		return memberService.activation(memberFlowDTO);
	}

	@PostMapping("/vehicleInspection")
	@ApiOperation(value = "验车", notes = "传入 memberDTO")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", paramType = "update", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "vehicleInspectDate", value = "vehicleInspectDate", paramType = "update", required = true, dataType = "date")
	})
	public R<Boolean> vehicleInspection(@RequestBody MemberDTO memberDTO) {
		return memberService.vehicleInspection(memberDTO);
	}

	@GetMapping("/getMemberFlowList")
	@ApiOperation(value = "查询会员流水", notes = "传入会员id")
	@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true, dataType = "Long")
	public R<List<MemberFlowVO>> getMemberFlowList(@RequestParam("id") Long id) {
		return memberService.selectMemberFlowListByMemberId(id);
	}
}

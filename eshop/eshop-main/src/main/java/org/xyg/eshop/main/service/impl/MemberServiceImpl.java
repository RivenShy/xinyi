package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.system.cache.DictCache;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dto.MemberDTO;
import org.xyg.eshop.main.dto.MemberFlowDTO;
import org.xyg.eshop.main.entity.Member;
import org.xyg.eshop.main.entity.MemberFlow;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.enums.CarModelStandardEnum;
import org.xyg.eshop.main.enums.MemberPaymentTypeEnum;
import org.xyg.eshop.main.enums.MemberStatusEnum;
import org.xyg.eshop.main.enums.MemberValidityEnum;
import org.xyg.eshop.main.mapper.MemberMapper;
import org.xyg.eshop.main.service.ICommonService;
import org.xyg.eshop.main.service.IMemberFlowService;
import org.xyg.eshop.main.service.IMemberService;
import org.xyg.eshop.main.service.IStorefrontService;
import org.xyg.eshop.main.vo.ContractVO;
import org.xyg.eshop.main.vo.MemberFlowVO;
import org.xyg.eshop.main.vo.MemberVO;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MemberServiceImpl extends BaseServiceImpl<MemberMapper, Member> implements IMemberService {
	@Autowired
	private IStorefrontService storefrontService;
	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private AutoIncrementIDGenerator autoIncrementIDGenerator;

	@Autowired
	private IMemberFlowService memberFlowService;

	@Autowired
	private ICommonService commonService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> insertData(MemberDTO memberDTO) {
		memberDTO.setEffectiveDate(null);
		memberDTO.setExpirationDate(null);
		memberDTO.setStatus(MemberStatusEnum.UNDER_REVIEW.getIndex());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate;
		try {
			newDate = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		memberDTO.setApplicationDate(newDate);
		boolean saveMember = save(memberDTO);
		if(saveMember) {
			MemberFlow memberFlow = new MemberFlow();
			memberFlow.setMemberId(memberDTO.getId());
			memberFlow.setSerialNumber(getSerialNumber());
			memberFlow.setPaymentType(MemberPaymentTypeEnum.NEW.getName());
			memberFlow.setCarModel(memberDTO.getCarModel());
			memberFlow.setPlateNumber(memberDTO.getPlateNumber());
			memberFlow.setApplicationDate(newDate);
			memberFlow.setTermOfValidity(memberDTO.getTermOfValidity());
			memberFlow.setStorefrontId(memberDTO.getStorefrontId());
			memberFlow.setPaymentAmount(getPaymentAmount(memberDTO.getCarModelStandard(), memberDTO.getTermOfValidity()));
			boolean saveMemberFlow = memberFlowService.save(memberFlow);
			if(!saveMemberFlow) {
				return R.fail("新增会员流水失败");
			}
		}
		return R.status(saveMember);
	}

	private static BigDecimal getPaymentAmount(String carModelStandardDicKey, Integer termOfValidityDickey) {
		String carModelStandard = DictCache.getValue(EShopMainConstant.ESHOP_CAR_MODEL_STANDARD_DICT_CODE, carModelStandardDicKey);
		String signingTime = DictCache.getValue(EShopMainConstant.ESHOP_MEMBER_SIGNING_TIME_DICT_CODE, termOfValidityDickey);
		if(CarModelStandardEnum.NON_OPERATE_VEHICLE.getName().equals(carModelStandard)) {
			if(MemberValidityEnum.ONE_YEAR.getName().equals(signingTime)) {
				return new BigDecimal(99);
			} else if(MemberValidityEnum.TWO_YEAR.getName().equals(signingTime)) {
				return new BigDecimal(168);
			} else if(MemberValidityEnum.THREE_YEAR.getName().equals(signingTime)) {
				return new BigDecimal(238);
			} else {
				throw new RuntimeException("会员签约时长有误");
			}
		} else if(CarModelStandardEnum.ADVANCE_ASSIST_DRIVE.getName().equals(carModelStandard)){
			if(MemberValidityEnum.ONE_YEAR.getName().equals(signingTime)) {
				return new BigDecimal(200);
			} else if(MemberValidityEnum.TWO_YEAR.getName().equals(signingTime)) {
				return new BigDecimal(328);
			} else if(MemberValidityEnum.THREE_YEAR.getName().equals(signingTime)) {
				return new BigDecimal(498);
			} else {
				throw new RuntimeException("会员签约时长有误");
			}
		} else {
			throw new RuntimeException("会员车型标准有误");
		}
	}

	private String getSerialNumber() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		String serialNumber = autoIncrementIDGenerator.nextValueByPrependAndType("YCMF" + date, EShopMainConstant.ESCRM_MEMBER_FLOW_SERIAL_NUMBER);
		return serialNumber;
	}

	@Override
	public R<MemberVO> getDetail(Long id) {
		Member member = getById(id);
		if(member == null) {
			return R.fail("查询会员信息失败");
		}
		MemberVO memberVO = BeanUtil.copy(member, MemberVO.class);
		fillDetailData(memberVO);
		return R.data(memberVO);
	}

	private void fillDetailData(MemberVO memberVO){
		if (memberVO == null){
			return;
		}
		Long storefrontId = memberVO.getStorefrontId();
		Storefront storefront = storefrontService.getById(storefrontId);
		if (storefront != null){
			memberVO.setStorefrontName(storefront.getStorefrontName());
		}
		String dictStatus = memberVO.getStatus() == null ? null : memberVO.getStatus().toString();
		String statusName = commonService.getDictValue(EShopMainConstant.MEMBER_STATUS_DICT_CODE, dictStatus);
		memberVO.setStatusName(statusName);
		String dictTermOfValidity = memberVO.getTermOfValidity() == null ? null : memberVO.getTermOfValidity().toString();
		String termOfValidityName = commonService.getDictValue(EShopMainConstant.ESHOP_MEMBER_SIGNING_TIME_DICT_CODE, dictTermOfValidity);
		memberVO.setTermOfValidityName(termOfValidityName);
		String dictCarModelStandard = memberVO.getCarModelStandard() == null ? null : memberVO.getCarModelStandard();
		String carModelStandardName = commonService.getDictValue(EShopMainConstant.ESHOP_CAR_MODEL_STANDARD_DICT_CODE, dictCarModelStandard);
		memberVO.setCarModelStandardName(carModelStandardName);
	}

	@Override
	public R<IPage<MemberVO>> selectMemberPage(Query query, MemberDTO memberDTO) {
		IPage<MemberVO> page = Condition.getPage(query);
		List<MemberVO> memberVOList = memberMapper.selectMemberPage(page, memberDTO);
		fillData(memberVOList);
		return R.data(page.setRecords(memberVOList));
	}

	private void fillData(List<MemberVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}
		for (MemberVO memberVO : list) {
			fillDetailData(memberVO);
		}
	}

	@Override
	public void checkDelayExpireMember(Member member) {

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> renew(MemberFlowDTO memberFlowDTO) {
		if(Func.isEmpty(memberFlowDTO.getMemberId())) {
			return R.fail("缺少必要的请求参数：memberId");
		}
		if(Func.isEmpty(memberFlowDTO.getCarModel())) {
			return R.fail("缺少必要的请求参数：carModel");
		}
		if(Func.isEmpty(memberFlowDTO.getPlateNumber())) {
			return R.fail("缺少必要的请求参数:plateNumber");
		}
		if(Func.isEmpty(memberFlowDTO.getTermOfValidity())) {
			return R.fail("缺少必要的请求参数:termOfValidity");
		}
		if(Func.isEmpty(memberFlowDTO.getEffectiveDate())) {
			return R.fail("缺少必要的请求参数:effectiveDate");
		}
		if(Func.isEmpty(memberFlowDTO.getExpirationDate())) {
			return R.fail("缺少必要的请求参数:expirationDate");
		}
		if(Func.isEmpty(memberFlowDTO.getStorefrontId())) {
			return R.fail("缺少必要的请求参数：storefrontId");
		}
		memberFlowDTO.setPaymentType(MemberPaymentTypeEnum.RENEW.getName());
		Member member = getById(memberFlowDTO.getMemberId());
		if(member == null) {
			return R.fail("查询会员信息失败");
		}
		memberFlowDTO.setPaymentAmount(getPaymentAmount(member.getCarModelStandard(), member.getTermOfValidity()));
		boolean saveMemberFlow = memberFlowService.save(memberFlowDTO);
		if(saveMemberFlow) {
			boolean renewResult = this.lambdaUpdate()
				.set(Member::getEffectiveDate, memberFlowDTO.getEffectiveDate())
				.set(Member::getExpirationDate, memberFlowDTO.getExpirationDate())
				.eq(Member::getId, memberFlowDTO.getMemberId())
				.update();
			if(!renewResult) {
				throw new RuntimeException("更新会员有效期失败");
			}
		}
		return R.status(saveMemberFlow);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> activation(MemberFlowDTO memberFlowDTO) {
		if(Func.isEmpty(memberFlowDTO.getMemberId())) {
			return R.fail("缺少必要的请求参数：memberId");
		}
		if(Func.isEmpty(memberFlowDTO.getCarModel())) {
			return R.fail("缺少必要的请求参数：carModel");
		}
		if(Func.isEmpty(memberFlowDTO.getPlateNumber())) {
			return R.fail("缺少必要的请求参数:plateNumber");
		}
		if(Func.isEmpty(memberFlowDTO.getTermOfValidity())) {
			return R.fail("缺少必要的请求参数:termOfValidity");
		}
		if(Func.isEmpty(memberFlowDTO.getEffectiveDate())) {
			return R.fail("缺少必要的请求参数:effectiveDate");
		}
		if(Func.isEmpty(memberFlowDTO.getExpirationDate())) {
			return R.fail("缺少必要的请求参数:expirationDate");
		}
		if(Func.isEmpty(memberFlowDTO.getStorefrontId())) {
			return R.fail("缺少必要的请求参数：storefrontId");
		}
		memberFlowDTO.setPaymentType(MemberPaymentTypeEnum.ACTIVATION.getName());
		Member member = getById(memberFlowDTO.getMemberId());
		if(member == null) {
			return R.fail("查询会员信息失败");
		}
		memberFlowDTO.setPaymentAmount(getPaymentAmount(member.getCarModelStandard(), member.getTermOfValidity()));
		boolean saveMemberFlow = memberFlowService.save(memberFlowDTO);
		if(saveMemberFlow) {
			boolean renewResult = this.lambdaUpdate()
				.set(Member::getEffectiveDate, memberFlowDTO.getEffectiveDate())
				.set(Member::getExpirationDate, memberFlowDTO.getExpirationDate())
				.set(Member::getStatus, MemberStatusEnum.NORMAL.getIndex())
				.eq(Member::getId, memberFlowDTO.getMemberId())
				.update();
			if(!renewResult) {
				throw new RuntimeException("更新会员有效期失败");
			}
		}
		return R.status(saveMemberFlow);
	}

	@Override
	public R<Boolean> vehicleInspection(MemberDTO memberDTO) {
		if(Func.isEmpty(memberDTO.getId())) {
			return R.fail("缺少必要的请求参数：id");
		}
		if(Func.isEmpty(memberDTO.getVehicleInspectDate())) {
			return R.fail("验车时间不能为空");
		}
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		Date vehicleInspectDate;
		try {
			vehicleInspectDate = sdf.parse(sdf.format(memberDTO.getVehicleInspectDate()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(vehicleInspectDate);
		cal.add(Calendar.DATE, 30);
		Date effectiveDate = cal.getTime();
		cal.setTime(effectiveDate);
		Member member = getById(memberDTO.getId());
		if(member == null) {
			return R.fail("查询会员信息失败");
		}
		String signingTime = DictCache.getValue(EShopMainConstant.ESHOP_MEMBER_SIGNING_TIME_DICT_CODE, member.getTermOfValidity());
		if(MemberValidityEnum.ONE_YEAR.getName().equals(signingTime)) {
			cal.add(Calendar.YEAR, 1);
		} else if(MemberValidityEnum.TWO_YEAR.getName().equals(signingTime)) {
			cal.add(Calendar.YEAR, 2);
		} else if(MemberValidityEnum.THREE_YEAR.getName().equals(signingTime)) {
			cal.add(Calendar.YEAR, 3);
		} else {
			throw new RuntimeException("会员签约时长字典异常");
		}
		Date expirationDate = cal.getTime();
		boolean updateStatus = this.lambdaUpdate()
			.set(Member::getEffectiveDate, effectiveDate)
			.set(Member::getExpirationDate, expirationDate)
			.set(Member::getStatus, MemberStatusEnum.OBSERVATION.getIndex())
			.eq(Member::getId, member.getId())
			.update();
		return R.status(updateStatus);
	}

	@Override
	public R<List<MemberFlowVO>> selectMemberFlowListByMemberId(Long id) {
		List<MemberFlowVO> memberFlowList = memberFlowService.selectMemberFlowListByMemberId(id);
		return R.data(memberFlowList);
	}

	@Override
	public void updateMemberStatus() {
		log.info("更新到期会员的状态");
		lambdaUpdate().set(Member::getStatus, MemberStatusEnum.EXPIRED.getIndex())
			.lt(Member::getExpirationDate, new Date())
			.eq(Member::getStatus, MemberStatusEnum.NORMAL.getIndex())
			.update();
		log.info("更新过了观察期的会员状态");
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate;
		try {
			nowDate = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		// 审核后30天内为观察期
		cal.add(Calendar.DATE, -30);
		lambdaUpdate().set(Member::getStatus, MemberStatusEnum.NORMAL.getIndex())
			.lt(Member::getVehicleInspectDate, cal.getTime())
			.eq(Member::getStatus, MemberStatusEnum.OBSERVATION.getIndex())
			.update();
	}
}

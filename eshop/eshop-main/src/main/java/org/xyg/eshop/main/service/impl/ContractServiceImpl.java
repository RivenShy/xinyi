package org.xyg.eshop.main.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.mp.base.DBEntity;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.secure.utils.AuthUtil;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.springrabbit.flow.core.entity.RabbitFlow;
import org.springrabbit.flow.core.feign.IFlowOpenClient;
import org.springrabbit.system.entity.ApproverRoles;
import org.springrabbit.system.feign.ISysClient;
import org.springrabbit.system.user.entity.User;
import org.springrabbit.system.user.feign.IUserClient;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.ehop.common.constants.EshopConstants;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dao.IContractDao;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;
import org.xyg.eshop.main.enums.ContractStatusEnum;
import org.xyg.eshop.main.enums.ContractTypeEnum;
import org.xyg.eshop.main.mapper.ContractMapper;
import org.xyg.eshop.main.service.ICommonService;
import org.xyg.eshop.main.service.IContractRelatePersonnelService;
import org.xyg.eshop.main.service.IContractService;
import org.xyg.eshop.main.vo.ContractVO;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.xyg.eshop.main.constants.EShopMainConstant.ESCRM_CONTRACT_APPROVAL;

@Slf4j
@Service
@AllArgsConstructor
public class ContractServiceImpl implements IContractService {

	@Autowired
	protected IFlowOpenClient flowOpenClient;

	@Autowired
	private IContractDao contractDao;

	@Autowired
	private AutoIncrementIDGenerator autoIncrementIDGenerator;

	@Autowired
	private ContractMapper contractMapper;

	@Autowired
	private IContractRelatePersonnelService contractRelatePersonnelService;

	@Autowired
	protected IUserClient userClient;

	@Autowired
	protected ISysClient sysClient;

	@Autowired
	private ICommonService commonService;

	@Override
	public IPage<ContractVO> selectContractPage(IPage<ContractVO> page, ContractDTO contractDTO) {
		List<String> contractExpireDateQueryList = contractDTO.getContractExpireDateQueryList();
		if(contractExpireDateQueryList !=  null) {
			for(String contractExpireDateQuery : contractExpireDateQueryList) {
				if(contractExpireDateQuery.equals("1")) {
					contractDTO.setQueryWithinWeek("一周内");
				} else if(contractExpireDateQuery.equals("2")) {
					contractDTO.setQueryWithinHalfMonth("半个月内");
				} else if(contractExpireDateQuery.equals("3")) {
					contractDTO.setQueryWithinMonth("一个月内");
				} else if(contractExpireDateQuery.equals("4")) {
					contractDTO.setQueryWithinTwoMonth("两个月内");
				}
			}
		}
		List<ContractVO> contractVOList = contractMapper.selectContractPage(page, contractDTO);
		fillData(contractVOList);
		return page.setRecords(contractVOList);

	}

	/**
	 * 补充合同列表数据
	 * @param list
	 */
	private void fillData(List<ContractVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}
		for (ContractVO contractVO : list) {
			fillDetailData(contractVO);
		}
	}

	/**
	 * 补充合同详情数据
	 * @param contractVO
	 */
	private void fillDetailData(ContractVO contractVO) {
		if (contractVO == null) {
			return;
		}
		String dictStatus = contractVO.getContractStatus() == null ? null : contractVO.getContractStatus().toString();
		String statusName = commonService.getDictValue(EShopMainConstant.CONTRACT_STATUS_DICT_CODE, dictStatus);
		contractVO.setStatusName(statusName);
		String dictContractType = contractVO.getContractType() == null ? null : contractVO.getContractType().toString();
		String contractTypeName = commonService.getDictValue(EShopMainConstant.CONTRACT_TYPE_DICT_CODE, dictContractType);
		contractVO.setContractTypeName(contractTypeName);
		String contractSceneName = commonService.getDictValue(EShopMainConstant.CONTRACT_SCENE_DICT_CODE, contractVO.getContractScene());
		contractVO.setContractSceneName(contractSceneName);
	}

	@Override
	public ContractVO selectById(Long id) {
		ContractVO contractVO = contractMapper.selectById(id);
		fillDetailData(contractVO);
		return contractVO;
	}

	@Override
	public void addProcess(Long id) {
		if(id == null) {
			return;
		}
		log.info("合同提交：{}，发起流程！", id);
		RabbitUser user = AuthUtil.getUser();
		Contract contract = contractDao.getById(id);
		// 添加流程变量
		Map<String, Object> variablesMap = new HashMap<>();
		//业务主键id
		variablesMap.put("data_id_", id);
		//流程标题
		variablesMap.put("business_name_", "合同新增" + contract.getContractCode());
		//流程发起人
		variablesMap.put("createUserId", user.getUserId());
		//消息标题
		variablesMap.put("title_", "合同新增");
		//消息内容
		variablesMap.put("content_", user.getUserName() +"于" + new Date() + "新增了合同");
		//消息模板
		variablesMap.put("template_code_", "eshop_contract_submit_template");
		//开始回调函数
		variablesMap.put("startCallbackMethod", "http://eshop-main/contract/contractSubmitExecutionStartCallback");
		//结束回调函数
		variablesMap.put("endCallbackMethod", "http://eshop-main/contract/contractSubmitExecutionEndCallback");
		// 调用流程发起方法
		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey("eshop_contract_submit_process", "taskUser_" + user.getUserId(), true, "", variablesMap);

		// 保存流程id
		if (rabbitFlowR.isSuccess()) {
			afterAddProcessSuccess(id, rabbitFlowR.getData());
		} else {
			log.error("发起合同审批流程失败！,错误信息：{}", rabbitFlowR.getMsg());
			throw new RuntimeException("发起合同审批流程失败");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> insertData(ContractDTO contractDTO, String saveOrSubmit) {
		String contractCodde = generateContractCodde();
		contractDTO.setContractCode(contractCodde);
		if (Func.isEmpty(contractDTO.getContractType())) {
			return R.fail("缺少必要的请求参数: contractType");
		}
		if (Func.isEmpty(contractDTO.getContractStartDate())) {
			return R.fail("缺少必要的请求参数: contractStartDate");
		}
		if (Func.isEmpty(contractDTO.getContractExpireDate())) {
			return R.fail("缺少必要的请求参数: contractExpireDate");
		}
		contractDTO.setContractStatus(ContractStatusEnum.NOT_SUBMITTED.getIndex());
		if(contractDTO.getContractType() == ContractTypeEnum.CHANGE.getIndex() ||
			contractDTO.getContractType() == ContractTypeEnum.SUPPLEMENT.getIndex()) {
			if(Func.isEmpty(contractDTO.getOriginContractId())) {
				return R.fail("原合同Id不能为空");
			}
			Contract contractOrigin = contractDao.getById(contractDTO.getOriginContractId());
			if(contractOrigin == null) {
				return R.fail("原合同不存在,请检查原合同Id");
			}
		}
		boolean saveContractResult = contractDao.saveOrUpdate(contractDTO);
		if(saveContractResult) {
			// 合同相关人员
			if(contractDTO.getContractType() == ContractTypeEnum.CHANGE.getIndex() ||
				contractDTO.getContractType() == ContractTypeEnum.SUPPLEMENT.getIndex()) {
				return R.success("新增合同成功");
			}
			if(!Func.isEmpty(contractDTO.getPersonnelList())) {
				for(ContractRelatePersonnel contractRelatePersonnel : contractDTO.getPersonnelList()) {
					contractRelatePersonnel.setContractId(contractDTO.getId());
				}
				boolean savePersonnel = contractRelatePersonnelService.saveOrUpdateBatch(contractDTO.getPersonnelList());
				if(!savePersonnel) {
					throw new RuntimeException("新增合同相关人员失败");
				}
			}
			if(saveOrSubmit.equals("submit")) {
				//发起审批流程
				addProcess(contractDTO.getId());
			}
			return R.success("新增合同成功");
		}
		return R.fail("新增合同失败");
	}

	private String generateContractCodde() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date()); // 格式化日期 date: 20210114
		String contractCodde = autoIncrementIDGenerator.nextValueByPrependAndType("YCHT" + date, ESCRM_CONTRACT_APPROVAL);
		return contractCodde;
	}

//	@Override
//	public R<Long> submit(ContractDTO contractDTO) {
//		String contractCodde = getContractCodde();
//		contractDTO.setContractCode(contractCodde);
////		if (Func.isEmpty(contractDTO.getStoreName())) {
////			return R.fail("缺少必要的请求参数: storeName");
////		}
////		if (Func.isEmpty(contractDTO.getUnifiedSocialCreditCode())) {
////			return R.fail("缺少必要的请求参数: unifiedSocialCreditCode");
////		}
////		if (Func.isEmpty(contractDTO.getStoreGrade())) {
////			return R.fail("缺少必要的请求参数: storeGrade");
////		}
////		if (Func.isEmpty(contractDTO.getStoreAddress())) {
////			return R.fail("缺少必要的请求参数: storeAddress");
////		}
////		if (Func.isEmpty(contractDTO.getContractScene())) {
////			return R.fail("缺少必要的请求参数: contractScene");
////		}
////		if (Func.isEmpty(contractDTO.getSupplier())) {
////			return R.fail("缺少必要的请求参数: supplier");
////		}
////		if (Func.isEmpty(contractDTO.getUseContractTemplate())) {
////			return R.fail("缺少必要的请求参数: useContractTemplate");
////		}
////		if (Func.isEmpty(contractDTO.getSalesman())) {
////			return R.fail("缺少必要的请求参数: salesman");
////		}
////		if (Func.isEmpty(contractDTO.getSalesmanCode())) {
////			return R.fail("缺少必要的请求参数: salesmanCode");
////		}
//		if (Func.isEmpty(contractDTO.getContractType())) {
//			return R.fail("缺少必要的请求参数: contractType");
//		}
//		if (Func.isEmpty(contractDTO.getContractStartDate())) {
//			return R.fail("缺少必要的请求参数: contractStartDate");
//		}
//		if (Func.isEmpty(contractDTO.getContractExpireDate())) {
//			return R.fail("缺少必要的请求参数: contractExpireDate");
//		}
////		if (Func.isEmpty(contractDTO.getContractParty())) {
////			return R.fail("缺少必要的请求参数: contractParty");
////		}
////		if (Func.isEmpty(contractDTO.getContractText())) {
////			return R.fail("缺少必要的请求参数: contractText");
////		}
//		contractDTO.setContractStatus(ContractStatusEnum.NOT_SUBMITTED.getIndex());
//		boolean saveContractResult = contractDao.saveOrUpdate(contractDTO);
//		if(saveContractResult) {
//			System.out.println("id:" + contractDTO.getId());
//			System.out.println(contractDTO);
//			// 合同相关人员
//			if(contractDTO.getPersonnelList() != null) {
//				for(ContractRelatePersonnel contractRelatePersonnel : contractDTO.getPersonnelList()) {
//					contractRelatePersonnel.setContractId(contractDTO.getId());
//				}
//				boolean savePersonnel = contractRelatePersonnelService.saveOrUpdateBatch(contractDTO.getPersonnelList());
//				if(!savePersonnel) {
//					return R.fail("新增合同信息成功，但新增合同相关人员失败");
//				}
//			}
//			try {
//				Contract contract = contractDao.getById(contractDTO.getId());
//				//发起审批流程
//				addProcess(contract.getId());
//				return R.data(contract.getId());
//			} catch (RuntimeException e) {
//				log.error(e.getMessage());
//				return R.fail(e.getMessage());
//			}
//		}
//		return R.fail("新增合同失败");
//	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto) {
		log.info("流程前置回调方法，前置参数：{}", JSONObject.toJSONString(inDto));
		if(inDto == null) {
			return null;
		}
		// 获取业务id
		String businessId = inDto.getBusinessId();
		if(StringUtils.isBlank(businessId)) {
			return null;
		}
		// 获取当前节点id
		String activityId = inDto.getCurrentActivityId();
		if (Objects.equals(EShopMainConstant.DRAFT_NODE,activityId)){
			if (inDto.isCurrentActivityFromReject()){
				contractDao.lambdaUpdate().eq(DBEntity::getId, businessId)
					.set(Contract::getContractStatus,EshopConstants.STATUS_REJECTED)
					.update();
			}
		}
		CallBackMethodResDto outDto = new CallBackMethodResDto();
		ApproverRoles approverRoles = new ApproverRoles();
		approverRoles.setAttribute1(inDto.getProcessDefinitionKey());
		approverRoles.setRoleCode(inDto.getDocumentation());
		log.info("流程前置回调方法，查询审批角色：{}", JSONObject.toJSONString(approverRoles));
		outDto.setCandidateUsers(Optional.ofNullable(getDataFromR(sysClient.getUserIdList(approverRoles))).orElse(new ArrayList<>()));
		return outDto;
	}


	@Override
	public CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto) {
		log.info("流程后置回调方法，后置参数：{}", JSONObject.toJSONString(inDto));
		CallBackMethodResDto outDto = new CallBackMethodResDto();
		if(inDto == null) {
			return outDto;
		}
		String businessId = inDto.getBusinessId();
		if(StringUtils.isBlank(businessId)) {
			return outDto;
		}
		Long id = Long.valueOf(businessId);
		String activityId = inDto.getCurrentActivityId();
		String processInstanceId = inDto.getProcessInstanceId();
		if(inDto.isAbandon()) {
			//如果流程废弃，则将合同的状态设置成"已驳回"，当前审批节点设置为"起草节点"
			contractDao.lambdaUpdate()
				.set(Contract::getContractStatus, ContractStatusEnum.REJECTED.getName())
				.set(Contract::getCurrApprovePhase, EShopMainConstant.DRAFT_NODE)
				.eq(Contract::getId, id)
				.update();
			return outDto;
		}
		if(!inDto.isPass()) {
			return outDto;
		}
//		String assignee = Optional.ofNullable(inDto.getAssignee()).orElse("");
//		if(StringUtils.isNotBlank(assignee)) {
//			User user = getUserByUserId(StringUtils.removeStart(assignee, "taskUser_"));
//			if(user != null) {
//				assignee = user.getCode();
//				log.info("assignee" + assignee);
//			}
//		}
		if (Objects.equals(EShopMainConstant.DRAFT_NODE,activityId)) {
			contractDao.lambdaUpdate().eq(DBEntity::getId, id)
				.set(Contract::getContractStatus, ContractStatusEnum.UNDER_APPROVAL.getIndex())
				.set(Contract::getProcessInstanceId, processInstanceId)
				.update();
		} else if (Objects.equals(EShopMainConstant.END,activityId)){
			contractDao.lambdaUpdate().eq(DBEntity::getId,id)
				.set(Contract::getContractStatus, ContractStatusEnum.PERFORMING.getIndex())
				.update();
		}
//		if("department_head".equals(activityId)) {
//			log.info("部门负责人审批");
//		} else if("leader_in_charge".equals(activityId)) {
//			log.info("分管领导审批");
//			contractDao.lambdaUpdate()
//				.set(Contract::getStatus, ContractStatusEnum.PERFORMING.getIndex())
//				.set(Contract::getContractStatus, ContractStatusEnum.PERFORMING.getName())
//				.eq(Contract::getId, id)
//				.update();
//		}
		return outDto;
	}

	protected User getUserByUserId(String userId) {
		if(StringUtils.isBlank(userId)) {
			return null;
		}
		return getDataFromR(userClient.userInfoById(Long.valueOf(userId)));
	}

	protected <T> T getDataFromR(R<T> r) {
		log.info("响应数据：{}" + JSONObject.toJSONString(r));
		if(!r.isSuccess()) {
			return null;
		}
		return r.getData();
	}


	private void afterAddProcessSuccess(Long id, RabbitFlow rabbitFlow) {
		contractDao.lambdaUpdate()
			.set(Contract::getProcessInstanceId, rabbitFlow.getProcessInstanceId())
//			.set(Contract::getStatus, ContractStatusEnum.UNDER_APPROVAL.getIndex())
			.set(Contract::getContractStatus, ContractStatusEnum.UNDER_APPROVAL.getIndex())
			.eq(Contract::getId, id).update();
	}

	@Override
	public void updateContractStatusIfExpired() {
		contractDao.lambdaUpdate()
//			.set(Contract::getStatus, ContractStatusEnum.EXPIRED.getIndex())
			.set(Contract::getContractStatus, ContractStatusEnum.EXPIRED.getIndex())
			.ne(Contract::getContractStatus, ContractStatusEnum.EXPIRED.getIndex())
			.le(Contract::getContractExpireDate, new Date())
			.update();
	}

	@Override
	public R<Boolean> deletePersonnelById(Long id) {
		if(Func.isEmpty(id)) {
			return R.fail("缺少必要的请求参数：id");
		}
		return R.status(contractRelatePersonnelService.deleteByPersonnelId(id));
	}

	@Override
	public R<List<ContractVO>> selectChangeOrSupplementContract(ContractDTO contractDTO) {
		if(Func.isEmpty(contractDTO.getContractType())) {
			return R.fail("合同类型不能为空");
		}
		if(contractDTO.getContractType() != ContractTypeEnum.CHANGE.getIndex() &&
			contractDTO.getContractType() != ContractTypeEnum.SUPPLEMENT.getIndex()) {
			return R.fail("合同类型必须是变更合同或补充协议");
		}
		if(Func.isEmpty(contractDTO.getOriginContractId())) {
			return R.fail("原合同Id不能为空");
		}
		List<ContractVO> contractVOList = contractMapper.selectChangeOrSupplementContract(contractDTO);
		return R.data(contractVOList);
	}
}

package org.xyg.eshop.main.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springrabbit.common.utils.AuthUtils2;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.tool.api.R;
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
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dao.IContractDao;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;
import org.xyg.eshop.main.enums.ContractStatusEnum;
import org.xyg.eshop.main.mapper.ContractMapper;
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

	@Override
	public IPage<ContractVO> selectContractPage(IPage<ContractVO> page, ContractDTO contractDTO) {
		List<String> contractExpireDateQueryList = contractDTO.getContractExpireDateQueryList();
		if(contractExpireDateQueryList !=  null) {
			for(String contractExpireDateQuery : contractExpireDateQueryList) {
				if(contractExpireDateQuery.equals("1")) {
					contractDTO.setQueryWithinWeek("一周内");
				} else 	if(contractExpireDateQuery.equals("2")) {
					contractDTO.setQueryWithinHalfMonth("半个月内");
				} else 	if(contractExpireDateQuery.equals("3")) {
					contractDTO.setQueryWithinMonth("一个月内");
				} else 	if(contractExpireDateQuery.equals("4")) {
					contractDTO.setQueryWithinTwoMonth("两个月内");
				}
			}
		}
		List<ContractVO> contractVOList = contractMapper.selectContractPage(page, contractDTO);
		return page.setRecords(contractVOList);

	}

	@Override
	public ContractVO selectById(Long id) {
		return contractMapper.selectById(id);
	}

	@Override
	public void addProcess(Long id) {
		if(id == null) {
			return;
		}
		log.info("合同提交：{}，发起流程！", id);
		RabbitUser user = AuthUtils2.currentUser();
		Contract contract = contractDao.getById(id);
		// 添加流程变量
//		Map<String, Object> variablesMap = getVariablesMap(id);
		Map<String, Object> variablesMap = new HashMap<>();
		//业务主键id
		variablesMap.put("data_id_", id);
		//流程标题
		variablesMap.put("business_name_", "合同新增" + contract.getContractCode());

//		variablesMap.put("business_name_", getBusinessName() + complaint.getComplaintNo());
		//流程发起人
		variablesMap.put("createUserId", user.getUserId());

		//消息标题
//		variablesMap.put("title_", getMessageTitle());
		variablesMap.put("title_", "合同新增");
		//消息内容
//		variablesMap.put("content_", getMessageContent(user, complaint));
		variablesMap.put("content_", user.getUserName() +"在" + new Date() + "新增了合同");
		//消息模板
//		variablesMap.put("template_code_", getMessageTemplate());
		variablesMap.put("template_code_", "eshop_contract_submit_template");
		//开始回调函数
//		variablesMap.put("startCallbackMethod", getStartCallbackMethod());
		variablesMap.put("startCallbackMethod", "http://eshop-main/contract/contractSubmitExecutionStartCallback");
		//结束回调函数
//		variablesMap.put("endCallbackMethod", getEndCallbackMethod());
		variablesMap.put("endCallbackMethod", "http://eshop-main/contract/contractSubmitExecutionEndCallback");

		// 调用流程发起方法
//		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey(getProcessDefinitionKey(), RasConstant.TASK_USER + user.getUserId(), true, "", variablesMap);
//		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey("eshop_contract_return_contract_status", RasConstant.TASK_USER + user.getUserId(), true, "", variablesMap);
		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey("eshop_contract_submit_process", "taskUser_" + user.getUserId(), true, "", variablesMap);

		// 保存流程id
		if (rabbitFlowR.isSuccess()) {
			afterAddProcessSuccess(id, rabbitFlowR.getData());
		} else {
			log.error("流程合同发起失败！,错误信息：{}", rabbitFlowR.getMsg());
			throw new RuntimeException("流程合同发起失败");
		}
	}

	@Override
	public R<Boolean> saveOrUpdate(ContractDTO contractDTO) {
		String contractCodde = getContractCodde();
		contractDTO.setContractCode(contractCodde);
//		if (Func.isEmpty(contractDTO.getStoreName())) {
//			return R.fail("缺少必要的请求参数: storeName");
//		}
//		if (Func.isEmpty(contractDTO.getUnifiedSocialCreditCode())) {
//			return R.fail("缺少必要的请求参数: unifiedSocialCreditCode");
//		}
//		if (Func.isEmpty(contractDTO.getStoreGrade())) {
//			return R.fail("缺少必要的请求参数: storeGrade");
//		}
//		if (Func.isEmpty(contractDTO.getStoreAddress())) {
//			return R.fail("缺少必要的请求参数: storeAddress");
//		}
//		if (Func.isEmpty(contractDTO.getContractScene())) {
//			return R.fail("缺少必要的请求参数: contractScene");
//		}
//		if (Func.isEmpty(contractDTO.getSupplier())) {
//			return R.fail("缺少必要的请求参数: supplier");
//		}
//		if (Func.isEmpty(contractDTO.getUseContractTemplate())) {
//			return R.fail("缺少必要的请求参数: useContractTemplate");
//		}
//		if (Func.isEmpty(contractDTO.getSalesman())) {
//			return R.fail("缺少必要的请求参数: salesman");
//		}
//		if (Func.isEmpty(contractDTO.getSalesmanCode())) {
//			return R.fail("缺少必要的请求参数: salesmanCode");
//		}
		if (Func.isEmpty(contractDTO.getContractType())) {
			return R.fail("缺少必要的请求参数: contractType");
		}
		if (Func.isEmpty(contractDTO.getContractStartDate())) {
			return R.fail("缺少必要的请求参数: contractStartDate");
		}
		if (Func.isEmpty(contractDTO.getContractExpireDate())) {
			return R.fail("缺少必要的请求参数: contractExpireDate");
		}
//		if (Func.isEmpty(contractDTO.getContractParty())) {
//			return R.fail("缺少必要的请求参数: contractParty");
//		}
//		if (Func.isEmpty(contractDTO.getContractText())) {
//			return R.fail("缺少必要的请求参数: contractText");
//		}
		contractDTO.setContractStatus(ContractStatusEnum.NOT_SUBMITTED.getIndex());
		boolean saveContractResult = contractDao.saveOrUpdate(contractDTO);
		if(saveContractResult) {
			System.out.println("id:" + contractDTO.getId());
			System.out.println(contractDTO);
			// 合同相关人员
			if(contractDTO.getPersonnelList() != null) {
				for(ContractRelatePersonnel contractRelatePersonnel : contractDTO.getPersonnelList()) {
					contractRelatePersonnel.setContractId(contractDTO.getId());
				}
				boolean savePersonnel = contractRelatePersonnelService.saveOrUpdateBatch(contractDTO.getPersonnelList());
				if(!savePersonnel) {
					return R.fail("新增合同信息成功，但新增合同相关人员失败");
				}
			}
			return R.success("新增合同成功");
		}
		return R.fail("新增合同失败");
	}

	private String getContractCodde() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date()); // 格式化日期 date: 20210114
		String contractCodde = autoIncrementIDGenerator.nextValueByPrependAndType("YC" + date, ESCRM_CONTRACT_APPROVAL);
		return contractCodde;
	}

	@Override
	public R<Long> submit(ContractDTO contractDTO) {
		String contractCodde = getContractCodde();
		contractDTO.setContractCode(contractCodde);
//		if (Func.isEmpty(contractDTO.getStoreName())) {
//			return R.fail("缺少必要的请求参数: storeName");
//		}
//		if (Func.isEmpty(contractDTO.getUnifiedSocialCreditCode())) {
//			return R.fail("缺少必要的请求参数: unifiedSocialCreditCode");
//		}
//		if (Func.isEmpty(contractDTO.getStoreGrade())) {
//			return R.fail("缺少必要的请求参数: storeGrade");
//		}
//		if (Func.isEmpty(contractDTO.getStoreAddress())) {
//			return R.fail("缺少必要的请求参数: storeAddress");
//		}
//		if (Func.isEmpty(contractDTO.getContractScene())) {
//			return R.fail("缺少必要的请求参数: contractScene");
//		}
//		if (Func.isEmpty(contractDTO.getSupplier())) {
//			return R.fail("缺少必要的请求参数: supplier");
//		}
//		if (Func.isEmpty(contractDTO.getUseContractTemplate())) {
//			return R.fail("缺少必要的请求参数: useContractTemplate");
//		}
//		if (Func.isEmpty(contractDTO.getSalesman())) {
//			return R.fail("缺少必要的请求参数: salesman");
//		}
//		if (Func.isEmpty(contractDTO.getSalesmanCode())) {
//			return R.fail("缺少必要的请求参数: salesmanCode");
//		}
		if (Func.isEmpty(contractDTO.getContractType())) {
			return R.fail("缺少必要的请求参数: contractType");
		}
		if (Func.isEmpty(contractDTO.getContractStartDate())) {
			return R.fail("缺少必要的请求参数: contractStartDate");
		}
		if (Func.isEmpty(contractDTO.getContractExpireDate())) {
			return R.fail("缺少必要的请求参数: contractExpireDate");
		}
//		if (Func.isEmpty(contractDTO.getContractParty())) {
//			return R.fail("缺少必要的请求参数: contractParty");
//		}
//		if (Func.isEmpty(contractDTO.getContractText())) {
//			return R.fail("缺少必要的请求参数: contractText");
//		}
		contractDTO.setContractStatus(ContractStatusEnum.NOT_SUBMITTED.getIndex());
		boolean saveContractResult = contractDao.saveOrUpdate(contractDTO);
		if(saveContractResult) {
			System.out.println("id:" + contractDTO.getId());
			System.out.println(contractDTO);
			// 合同相关人员
			if(contractDTO.getPersonnelList() != null) {
				for(ContractRelatePersonnel contractRelatePersonnel : contractDTO.getPersonnelList()) {
					contractRelatePersonnel.setContractId(contractDTO.getId());
				}
				boolean savePersonnel = contractRelatePersonnelService.saveOrUpdateBatch(contractDTO.getPersonnelList());
				if(!savePersonnel) {
					return R.fail("新增合同信息成功，但新增合同相关人员失败");
				}
			}
			try {
				Contract contract = contractDao.getById(contractDTO.getId());
				//发起审批流程
				addProcess(contract.getId());
				return R.data(contract.getId());
			} catch (RuntimeException e) {
				log.error(e.getMessage());
				return R.fail(e.getMessage());
			}
		}
		return R.fail("新增合同失败");
	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto) {
		log.info("流程前置回调方法，前置参数：{}", JSONObject.toJSONString(inDto));
		if(inDto == null) {
			return null;
		}
		String businessId = inDto.getBusinessId();
		if(StringUtils.isBlank(businessId)) {
			return null;
		}
		//根据流程数据更新合同数据
		contractDao.lambdaUpdate()
			.set(Contract::getCurrApprovePhase, inDto.getCurrentActivityId())
			.eq(Contract::getId, Long.valueOf(businessId))
			.update();
		Contract contract = contractDao.getById(Long.valueOf(businessId));
		CallBackMethodResDto outDto = new CallBackMethodResDto();
		//获取当前节点id
		String currentActivityId = inDto.getCurrentActivityId();
		// 判断当前审批节点
		if("xxx".equals(currentActivityId)) {

		}
		ApproverRoles approverRoles = new ApproverRoles();
		approverRoles.setAttribute1(inDto.getProcessDefinitionKey());
		approverRoles.setRoleCode(inDto.getDocumentation());
//		approverRoles.setOrgId(String.valueOf(complaint.getOrgId()));
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
		if(inDto.isAbandon()) {
			//如果流程废弃，则将合同的状态设置成"已驳回"，当前审批节点设置为"起草节点"
			contractDao.lambdaUpdate()
//				.set(Contract::getStatus, GFCRMConstant.COMPLAINT_STATUS_REFUSED)
				.set(Contract::getContractStatus, ContractStatusEnum.REJECTED.getName())
//				.set(Complaint::getCurrApprovePhase, GFCRMConstant.COMPLAINT_APPROVE_PHASE_DRAFT)
				.set(Contract::getCurrApprovePhase, EShopMainConstant.DRAFT_NODE)
				.eq(Contract::getId, id)
				.update();
			return outDto;
		}
		if(!inDto.isPass()) {
			return outDto;
		}
		String assignee = Optional.ofNullable(inDto.getAssignee()).orElse("");
		if(StringUtils.isNotBlank(assignee)) {
			User user = getUserByUserId(StringUtils.removeStart(assignee, "taskUser_"));
			if(user != null) {
				assignee = user.getCode();
				log.info("assignee" + assignee);
			}
		}
		if("start".equals(activityId)) {
			log.info("start流程节点--------------------------");
		}
		if("department_head".equals(activityId)) {
			log.info("部门负责人审批");
		} else if("leader_in_charge".equals(activityId)) {
			log.info("分管领导审批");
			contractDao.lambdaUpdate()
				.set(Contract::getStatus, ContractStatusEnum.PERFORMING.getIndex())
				.set(Contract::getContractStatus, ContractStatusEnum.PERFORMING.getName())
				.eq(Contract::getId, id)
				.update();
		}
		// else if......
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
//			.set(Contract::getSubmittedBy, AuthUtils2.currentUser().getOauthId())
//			.set(Contract::getSubmitDate, DateUtil.now())
//			.set(Contract::getQcEnteredBy, AuthUtils2.currentUser().getOauthId())
//			.set(Contract::getQcEnterDate, DateUtil.now())
			.set(Contract::getProcessInstanceId, rabbitFlow.getProcessInstanceId())
//			.set(Contract::getStatus, GFCRMConstant.COMPLAINT_STATUS_APPROVING)
			.set(Contract::getStatus, ContractStatusEnum.UNDER_APPROVAL.getIndex())
			.set(Contract::getContractStatus, ContractStatusEnum.UNDER_APPROVAL.getIndex())
			.eq(Contract::getId, id).update();
	}

	@Override
	public void updateContractStatusIfExpired() {
		contractDao.lambdaUpdate()
			.set(Contract::getStatus, ContractStatusEnum.EXPIRED.getIndex())
			.set(Contract::getContractStatus, ContractStatusEnum.EXPIRED.getIndex())
			.ne(Contract::getContractStatus, ContractStatusEnum.EXPIRED.getIndex())
			.lt(Contract::getContractExpireDate, new Date())
			.update();
	}

	@Override
	public R<Boolean> deletePersonnelById(Long id) {
		if(Func.isEmpty(id)) {
			return R.fail("缺少必要的请求参数：id");
		}
		return R.status(contractRelatePersonnelService.deleteByPersonnelId(id));
	}
}

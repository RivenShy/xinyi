package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springrabbit.common.utils.AuthUtils2;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.flow.core.entity.RabbitFlow;
import org.springrabbit.flow.core.feign.IFlowOpenClient;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public IPage<ContractVO> selectContractPage(IPage<ContractVO> page, ContractDTO contractDTO) {
//		return page.setRecords(baseMapper.selectContractPage(page, contractDTO));
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
		log.info("投诉单：{}，发起流程！", id);
		RabbitUser user = AuthUtils2.currentUser();
		Contract contract = contractMapper.selectById(id);
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
		variablesMap.put("content_", "xxx用户在xxx时间新增了合同");
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
		// 参数格式检查
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date()); // 格式化日期 date: 20210114
		// TODO 放在service类，新建dao类，继承baseService,service类再调用dao类
		String contractCodde = autoIncrementIDGenerator.nextValueByPrependAndType("YC" + date, ESCRM_CONTRACT_APPROVAL);
		contractDTO.setContractCode(contractCodde);
		if (Func.isEmpty(contractDTO.getCustomerName())) {
			return R.fail("缺少必要的请求参数: customerName");
		}
		if (Func.isEmpty(contractDTO.getUnifiedCreditCode())) {
			return R.fail("缺少必要的请求参数: unifiedCreditCode");
		}
		if (Func.isEmpty(contractDTO.getCustomerGrade())) {
			return R.fail("缺少必要的请求参数: customerGrade");
		}
		if (Func.isEmpty(contractDTO.getCustomerAddress())) {
			return R.fail("缺少必要的请求参数: customerAddress");
		}
		if (Func.isEmpty(contractDTO.getContractScene())) {
			return R.fail("缺少必要的请求参数: contractScene");
		}
		if (Func.isEmpty(contractDTO.getSupplier())) {
			return R.fail("缺少必要的请求参数: supplier");
		}
		if (Func.isEmpty(contractDTO.getUseContractTemplate())) {
			return R.fail("缺少必要的请求参数: useContractTemplate");
		}
		if (Func.isEmpty(contractDTO.getSalesman())) {
			return R.fail("缺少必要的请求参数: salesman");
		}
		if (Func.isEmpty(contractDTO.getSalesmanCode())) {
			return R.fail("缺少必要的请求参数: salesmanCode");
		}
		if (Func.isEmpty(contractDTO.getContractType())) {
			return R.fail("缺少必要的请求参数: contractType");
		}
		if (Func.isEmpty(contractDTO.getContractStartDate())) {
			return R.fail("缺少必要的请求参数: contractStartDate");
		}
		if (Func.isEmpty(contractDTO.getContractExpireDate())) {
			return R.fail("缺少必要的请求参数: contractExpireDate");
		}
		if (Func.isEmpty(contractDTO.getContractParty())) {
			return R.fail("缺少必要的请求参数: contractParty");
		}
		if (Func.isEmpty(contractDTO.getContractText())) {
			return R.fail("缺少必要的请求参数: contractText");
		}
		contractDTO.setContractStatus(ContractStatusEnum.NOT_SUBMITTED.getName());
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

	@Override
	public R<Long> submit(ContractDTO contractDTO) {
		try {
			// 提交合同
//			Long contractId = contractService.submit(contractDTO);
			Contract contract = contractDao.getById(contractDTO.getId());
			//发起审批流程
			addProcess(contract.getId());
			return R.data(contract.getId());
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	private void afterAddProcessSuccess(Long id, RabbitFlow data) {
		/**
		 * 合同状态：审批中
		 */
		Integer CONTRACT_STATUS_APPROVING_KEY = 1;
		String CONTRACT_STATUS_APPROVING_VALUE = "审批中";
//		contractDao.lambdaUpdate()
//			.set(Contract::getSubmittedBy, AuthUtils2.currentUser().getOauthId())
//			.set(Contract::getSubmitDate, DateUtil.now())
//			.set(Contract::getQcEnteredBy, AuthUtils2.currentUser().getOauthId())
//			.set(Contract::getQcEnterDate, DateUtil.now())
//			.set(Contract::getProcessInstanceId, flow.getProcessInstanceId())
//			.set(Contract::getStatus, GFCRMConstant.COMPLAINT_STATUS_APPROVING)
//			.eq(Contract::getId, id).update();
		contractDao.lambdaUpdate()
//			.set(Contract::getSubmittedBy, AuthUtils2.currentUser().getOauthId())
//			.set(Contract::getSubmitDate, DateUtil.now())
//			.set(Contract::getQcEnteredBy, AuthUtils2.currentUser().getOauthId())
//			.set(Contract::getQcEnterDate, DateUtil.now())
//			.set(Contract::getProcessInstanceId, flow.getProcessInstanceId())
//			.set(Contract::getStatus, GFCRMConstant.COMPLAINT_STATUS_APPROVING)
			.set(Contract::getStatus, CONTRACT_STATUS_APPROVING_KEY)
			.set(Contract::getContractStatus, CONTRACT_STATUS_APPROVING_VALUE)
			.eq(Contract::getId, id).update();
	}
}

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
		log.info("????????????{}??????????????????", id);
		RabbitUser user = AuthUtils2.currentUser();
		Contract contract = contractMapper.selectById(id);
		// ??????????????????
//		Map<String, Object> variablesMap = getVariablesMap(id);
		Map<String, Object> variablesMap = new HashMap<>();
		//????????????id
		variablesMap.put("data_id_", id);
		//????????????
		variablesMap.put("business_name_", "????????????" + contract.getContractCode());

//		variablesMap.put("business_name_", getBusinessName() + complaint.getComplaintNo());
		//???????????????
		variablesMap.put("createUserId", user.getUserId());

		//????????????
//		variablesMap.put("title_", getMessageTitle());
		variablesMap.put("title_", "????????????");
		//????????????
//		variablesMap.put("content_", getMessageContent(user, complaint));
		variablesMap.put("content_", "xxx?????????xxx?????????????????????");
		//????????????
//		variablesMap.put("template_code_", getMessageTemplate());
		variablesMap.put("template_code_", "eshop_contract_submit_template");
		//??????????????????
//		variablesMap.put("startCallbackMethod", getStartCallbackMethod());
		variablesMap.put("startCallbackMethod", "http://eshop-main/contract/contractSubmitExecutionStartCallback");
		//??????????????????
//		variablesMap.put("endCallbackMethod", getEndCallbackMethod());
		variablesMap.put("endCallbackMethod", "http://eshop-main/contract/contractSubmitExecutionEndCallback");

		// ????????????????????????
//		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey(getProcessDefinitionKey(), RasConstant.TASK_USER + user.getUserId(), true, "", variablesMap);
//		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey("eshop_contract_return_contract_status", RasConstant.TASK_USER + user.getUserId(), true, "", variablesMap);
		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey("eshop_contract_submit_process", "taskUser_" + user.getUserId(), true, "", variablesMap);

		// ????????????id
		if (rabbitFlowR.isSuccess()) {
			afterAddProcessSuccess(id, rabbitFlowR.getData());
		} else {
			log.error("???????????????????????????,???????????????{}", rabbitFlowR.getMsg());
			throw new RuntimeException("????????????????????????");
		}
	}

	@Override
	public R<Boolean> saveOrUpdate(ContractDTO contractDTO) {
		// ??????????????????
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date()); // ??????????????? date: 20210114
		// TODO ??????service????????????dao????????????baseService,service????????????dao???
		String contractCodde = autoIncrementIDGenerator.nextValueByPrependAndType("YC" + date, ESCRM_CONTRACT_APPROVAL);
		contractDTO.setContractCode(contractCodde);
		if (Func.isEmpty(contractDTO.getCustomerName())) {
			return R.fail("???????????????????????????: customerName");
		}
		if (Func.isEmpty(contractDTO.getUnifiedCreditCode())) {
			return R.fail("???????????????????????????: unifiedCreditCode");
		}
		if (Func.isEmpty(contractDTO.getCustomerGrade())) {
			return R.fail("???????????????????????????: customerGrade");
		}
		if (Func.isEmpty(contractDTO.getCustomerAddress())) {
			return R.fail("???????????????????????????: customerAddress");
		}
		if (Func.isEmpty(contractDTO.getContractScene())) {
			return R.fail("???????????????????????????: contractScene");
		}
		if (Func.isEmpty(contractDTO.getSupplier())) {
			return R.fail("???????????????????????????: supplier");
		}
		if (Func.isEmpty(contractDTO.getUseContractTemplate())) {
			return R.fail("???????????????????????????: useContractTemplate");
		}
		if (Func.isEmpty(contractDTO.getSalesman())) {
			return R.fail("???????????????????????????: salesman");
		}
		if (Func.isEmpty(contractDTO.getSalesmanCode())) {
			return R.fail("???????????????????????????: salesmanCode");
		}
		if (Func.isEmpty(contractDTO.getContractType())) {
			return R.fail("???????????????????????????: contractType");
		}
		if (Func.isEmpty(contractDTO.getContractStartDate())) {
			return R.fail("???????????????????????????: contractStartDate");
		}
		if (Func.isEmpty(contractDTO.getContractExpireDate())) {
			return R.fail("???????????????????????????: contractExpireDate");
		}
		if (Func.isEmpty(contractDTO.getContractParty())) {
			return R.fail("???????????????????????????: contractParty");
		}
		if (Func.isEmpty(contractDTO.getContractText())) {
			return R.fail("???????????????????????????: contractText");
		}
		contractDTO.setContractStatus(ContractStatusEnum.NOT_SUBMITTED.getName());
		boolean saveContractResult = contractDao.saveOrUpdate(contractDTO);
		if(saveContractResult) {
			System.out.println("id:" + contractDTO.getId());
			System.out.println(contractDTO);
			// ??????????????????
			if(contractDTO.getPersonnelList() != null) {
				for(ContractRelatePersonnel contractRelatePersonnel : contractDTO.getPersonnelList()) {
					contractRelatePersonnel.setContractId(contractDTO.getId());
				}
				boolean savePersonnel = contractRelatePersonnelService.saveOrUpdateBatch(contractDTO.getPersonnelList());
				if(!savePersonnel) {
					return R.fail("????????????????????????????????????????????????????????????");
				}
			}
			return R.success("??????????????????");
		}
		return R.fail("??????????????????");
	}

	@Override
	public R<Long> submit(ContractDTO contractDTO) {
		try {
			// ????????????
//			Long contractId = contractService.submit(contractDTO);
			Contract contract = contractDao.getById(contractDTO.getId());
			//??????????????????
			addProcess(contract.getId());
			return R.data(contract.getId());
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	private void afterAddProcessSuccess(Long id, RabbitFlow data) {
		/**
		 * ????????????????????????
		 */
		Integer CONTRACT_STATUS_APPROVING_KEY = 1;
		String CONTRACT_STATUS_APPROVING_VALUE = "?????????";
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

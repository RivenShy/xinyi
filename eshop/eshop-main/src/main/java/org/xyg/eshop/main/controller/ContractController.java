package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import oracle.ucp.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.ehop.common.constants.EshopConstants;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;
import org.xyg.eshop.main.enums.ContractStatusEnum;
import org.xyg.eshop.main.service.IContractRelatePersonnelService;
import org.xyg.eshop.main.service.IContractService;
import org.xyg.eshop.main.vo.ContractVO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.xyg.eshop.main.constants.EShopMainConstant.ESCRM_CONTRACT_APPROVAL;

@Slf4j
@RestController
@RequestMapping("/contract")
public class ContractController {

	@Autowired
	private IContractService contractService;





	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入 contract")
	public R<Boolean> save(@RequestBody ContractDTO contractDTO) {
		return contractService.saveOrUpdate(contractDTO);
	}

//	@PostMapping("/update")
//	@ApiOperation(value = "修改", notes = "传入 contract")
//	public R<Boolean> update(@RequestBody Contract contract) {
//		if (Func.isEmpty(contract.getId())) {
//			return R.fail("缺少必要的请求参数: id");
//		}
//		return R.status(contractService.updateById(contract));
//	}

//	@PostMapping("/remove")
//	@ApiOperation(value = "删除", notes = "传入 id")
//	public R<Boolean> remove(@RequestParam Long id) {
//		if (Func.isEmpty(id)) {
//			return R.fail("缺少必要的请求参数: id");
//		}
//		return R.status(contractService.removeById(id));
//	}

//	@GetMapping("/getDetail")
//	@ApiOperation(value = "查询详情", notes = "传入 id")
//	public R<Contract> getDetail(@RequestParam Long id) {
//		return R.data(contractService.getById(id));
//	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "查询详情", notes = "传入 id")
	public R<Contract> getDetail(@RequestParam Long id) {
		if(Func.isEmpty(id)) {
			return R.fail("缺少必要的请求参数: id");
		}
		return R.data(contractService.selectById(id));
	}

//	@GetMapping("/getList")
//	@ApiOperation(value = "查询集合")
//	public R<List<Contract>> getList() {
//		return R.data(contractService.list());
//	}

//	@GetMapping("/getPage")
//	@ApiOperation(value = "分页查询")
//	public R<IPage<Contract>> getPage(Query query) {
//		LambdaQueryChainWrapper<Contract> wrapper = contractService.lambdaQuery();
//		// 查询条件
//		return R.data(wrapper.page(Condition.getPage(query)));
//	}

//	@GetMapping("/getPage")
//	@ApiOperation(value = "分页条件查询")
//	public R<IPage<Contract>> getPage(Query query, ContractDTO contractDTO) {
//		LambdaQueryChainWrapper<Contract> wrapper = contractService.lambdaQuery();
//		// 查询条件
//		if (StringUtil.isNotBlank(contractDTO.getCustomerName())) {
//			wrapper.like(Contract::getCustomerName, contractDTO.getCustomerName());
//		}
//		if (StringUtil.isNotBlank(contractDTO.getContractCode())) {
//			wrapper.like(Contract::getContractCode, contractDTO.getContractCode());
//		}
//		if (StringUtil.isNotBlank(contractDTO.getContractType())) {
//			wrapper.like(Contract::getContractType, contractDTO.getContractType());
//		}
//		return R.data(wrapper.page(Condition.getPage(query)));
//	}

	/**
	 * 自定义分页
	 * @param query
	 * @param contractDTO
	 * @return
	 */
	@GetMapping("/getPage")
	@ApiOperation(value = "分页条件查询")
	public R<IPage<ContractVO>> getPage(Query query, ContractDTO contractDTO) {
		List<String> contractExpireDateQueryList = contractDTO.getContractExpireDateQueryList();
		if(contractExpireDateQueryList !=  null) {
			for(String contractExpireDateQuery : contractExpireDateQueryList) {
				if(contractExpireDateQuery.equals("一周内")) {
					contractDTO.setQueryWithinWeek("一周内");
				} else 	if(contractExpireDateQuery.equals("半个月内")) {
					contractDTO.setQueryWithinHalfMonth("半个月内");
				} else 	if(contractExpireDateQuery.equals("一个月内")) {
					contractDTO.setQueryWithinMonth("一个月内");
				} else 	if(contractExpireDateQuery.equals("两个月内")) {
					contractDTO.setQueryWithinTwoMonth("两个月内");
				}
			}
		}
		IPage<ContractVO> pages = contractService.selectContractPage(Condition.getPage(query), contractDTO);
		return R.data(pages);
	}

	@PostMapping("/submit")
	@ApiOperation(value = "合同提交接口", notes = "传入 id")
	public R<Long> submit(@RequestBody ContractDTO contractDTO) {
		return contractService.submit(contractDTO);
//		try {
//			// 提交合同
////			Long contractId = contractService.submit(contractDTO);
//			Contract contract = contractService.getById(contractDTO.getId());
//			//发起审批流程
//			contractService.addProcess(contract.getId());
//			return R.data(contract.getId());
//		} catch (RuntimeException e) {
//			log.error(e.getMessage());
//			return R.fail(e.getMessage());
//		}
	}

	@PostMapping("/contractSubmitExecutionStartCallback")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "流程实例任务结束回调接口")
	public R<CallBackMethodResDto> contractSubmitExecutionStartCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			log.info("合同提交任务开始回调接口");
			return R.data(null);
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	@PostMapping("/contractSubmitExecutionEndCallback")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "流程实例任务结束回调接口")
	public R<CallBackMethodResDto> contractSubmitExecutionEndCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			log.info("合同提交任务结束回调接口");
			return R.data(null);
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}
}

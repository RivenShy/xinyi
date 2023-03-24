package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.vo.ContractVO;

import java.util.List;

//public interface IContractService extends BaseService<Contract> {
public interface IContractService {
	IPage<ContractVO> selectContractPage(IPage<ContractVO> page, ContractDTO contractDTO);

	ContractVO selectById(Long id);

	void addProcess(Long contractId);

	R<Boolean> insertData(ContractDTO contractDTO, String saveOrSubmit);

//	R<Long> submit(ContractDTO contractDTO);

	CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto);

	CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto);

	void updateContractStatusIfExpired();

    R<Boolean> deletePersonnelById(Long id);

	R<List<ContractVO>> selectChangeOrSupplementContract(ContractDTO contractDTO);
}

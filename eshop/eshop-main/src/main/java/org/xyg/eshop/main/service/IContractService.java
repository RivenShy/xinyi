package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.vo.ContractVO;

//public interface IContractService extends BaseService<Contract> {
public interface IContractService {
	IPage<ContractVO> selectContractPage(IPage<ContractVO> page, ContractDTO contractDTO);

	ContractVO selectById(Long id);

	void addProcess(Long contractId);

	R<Boolean> saveOrUpdate(ContractDTO contractDTO);

	R<Long> submit(ContractDTO contractDTO);
}

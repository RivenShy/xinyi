package org.xyg.eshop.main.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;
import org.xyg.eshop.main.mapper.ContractMapper;
import org.xyg.eshop.main.mapper.ContractRelatePersonnelMapper;
import org.xyg.eshop.main.service.IContractRelatePersonnelService;
import org.xyg.eshop.main.service.IContractService;

@Slf4j
@Service
@AllArgsConstructor
public class ContractRelatePersonnelServiceImpl extends BaseServiceImpl<ContractRelatePersonnelMapper, ContractRelatePersonnel> implements IContractRelatePersonnelService {

	@Autowired
	private ContractRelatePersonnelMapper contractRelatePersonnelMapper;

	@Override
	public Boolean deleteByPersonnelId(Long id) {
		return contractRelatePersonnelMapper.deleteByPersonnelId(id);
	}
}

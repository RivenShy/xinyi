package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;

public interface IContractRelatePersonnelService  extends BaseService<ContractRelatePersonnel> {
	Boolean deleteByPersonnelId(Long id);
}

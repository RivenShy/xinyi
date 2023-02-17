package org.xyg.eshop.main.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.dao.IContractDao;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.mapper.ContractMapper;

@Slf4j
@Service
@AllArgsConstructor
public class ContractDaoImpl extends BaseServiceImpl<ContractMapper, Contract> implements IContractDao {

}

package org.xyg.eshop.main.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.dao.IContractDao;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractExpireRemindConfig;
import org.xyg.eshop.main.mapper.ContractExpireRemindConfigMapper;
import org.xyg.eshop.main.mapper.ContractMapper;
import org.xyg.eshop.main.service.IContractExpireRemindConfigService;

@Slf4j
@Service
@AllArgsConstructor
public class ContractExpireRemindConfigServiceImpl extends BaseServiceImpl<ContractExpireRemindConfigMapper, ContractExpireRemindConfig> implements IContractExpireRemindConfigService {

}

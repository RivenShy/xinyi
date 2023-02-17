package org.xyg.eshop.main.dao;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.secure.utils.AuthUtil;
import org.springrabbit.core.tool.utils.DateUtil;
import org.xyg.eshop.main.entity.Contract;

public interface IContractDao extends BaseService<Contract> {
	@Override
	default LambdaUpdateChainWrapper<Contract> lambdaUpdate() {
		RabbitUser user = AuthUtil.getUser();
		if(user != null) {
			return BaseService.super.lambdaUpdate()
				.set(Contract::getLastUpdatedBy, user.getUserId())
				.set(Contract::getLastUpdateLogin, user.getLoginId())
				.set(Contract::getLastUpdateDate, DateUtil.now());
		}
		return BaseService.super.lambdaUpdate().set(Contract::getLastUpdateDate, DateUtil.now());
	}
}

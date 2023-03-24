package org.xyg.eshop.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.entity.WxMpUser;
import org.xyg.eshop.main.mapper.WxMpUserMapper;
import org.xyg.eshop.main.service.IWxMpUserService;

@Slf4j
@Service
public class WxMpUserServiceImpl extends BaseServiceImpl<WxMpUserMapper, WxMpUser> implements IWxMpUserService {
	@Override
	public WxMpUser getByUserId(Long userId) {
		return lambdaQuery().eq(WxMpUser::getUserId, userId).one();
	}

	@Override
	public void updateOpenIdDeviceById(WxMpUser wxMpUser) {
		lambdaUpdate().set(WxMpUser::getOpenId, wxMpUser.getOpenId())
			.set(WxMpUser::getDevice, wxMpUser.getDevice())
			.eq(WxMpUser::getId, wxMpUser.getId())
			.update();
	}
}

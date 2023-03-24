package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.WxMpUser;

public interface IWxMpUserService extends BaseService<WxMpUser> {
	WxMpUser getByUserId(Long userId);

	void updateOpenIdDeviceById(WxMpUser wxMpUserGet);
}

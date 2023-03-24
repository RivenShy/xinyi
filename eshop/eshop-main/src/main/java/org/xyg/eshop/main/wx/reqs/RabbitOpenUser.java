package org.xyg.eshop.main.wx.reqs;

import lombok.Data;

/**
 * 微信已绑定的用户信息,user_id,devices必须
 */
@Data
public class RabbitOpenUser {
	private Long id;
	private Long userId;
	private String userCode;
	private String devices;
	private String loginDevice;
	private Integer accountShared;
}

package org.xyg.eshop.main.wx.resp;

import lombok.Data;
import org.xyg.eshop.main.wx.reqs.RabbitOpenUser;

import java.util.List;

@Data
public class SessionResp extends BaseResp {
	/**
	 * 用户唯一标识
	 */
	private String openid;
	/**
	 * 会话密钥
	 */
	private String session_key;
	/**
	 * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
	 */
	private String unionid;

	/**
	 * 绑定过的用户列表
	 */
	private List<RabbitOpenUser> userList;
}

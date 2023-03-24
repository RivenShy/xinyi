package org.xyg.eshop.main.wx.resp;

import lombok.Data;

@Data
public class TokenResp extends BaseResp {
	private String access_token;
	private int expires_in;
}

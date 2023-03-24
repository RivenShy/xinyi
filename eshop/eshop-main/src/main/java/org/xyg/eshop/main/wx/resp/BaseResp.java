package org.xyg.eshop.main.wx.resp;

import lombok.Data;

@Data
public class BaseResp {
	private int errcode;
	private String errmsg;

	public boolean success() {
		return errcode == 0;
	}

	public boolean needRetry() {
		return errcode == -1;
	}
}

package org.xyg.eshop.main.dto;

import lombok.Data;

@Data
public class DbchainToken {

	private String token_type;
	private String access_token;
	private int expires_in;
}

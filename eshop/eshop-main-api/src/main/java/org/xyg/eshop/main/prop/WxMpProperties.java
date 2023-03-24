package org.xyg.eshop.main.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wxmp")
//@ConfigurationProperties(prefix = "wxmp2")
@Data
public class WxMpProperties {

	private String appid;
	private String appsecret;

//	private String appid = "wx57a25de3c0434258";
//	private String appsecret = "63ac94ffc0e63d1d68b36243d35030a8";
}

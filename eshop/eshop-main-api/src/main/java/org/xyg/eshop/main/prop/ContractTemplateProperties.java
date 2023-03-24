package org.xyg.eshop.main.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "eshop.contract.template")
@Data
public class ContractTemplateProperties {
	private String baseurl;
	private String username;
	private String password;
	private String authorization;
	private String tenantId;
	private String grant_type;
	private String scope;
	private String type;
}

package org.xyg.eshop.main;

import org.springframework.cloud.client.SpringCloudApplication;
import org.springrabbit.core.cloud.feign.EnableRabbitFeign;
import org.springrabbit.core.launch.RabbitApplication;
import org.xyg.ehop.common.constants.EshopConstants;

@EnableRabbitFeign
@SpringCloudApplication
public class EshopMainApplication {

	public static void main(String[] args) {
		RabbitApplication.run(EshopConstants.APPLICATION_ESHOP_MAIN, EshopMainApplication.class, args);
	}
}

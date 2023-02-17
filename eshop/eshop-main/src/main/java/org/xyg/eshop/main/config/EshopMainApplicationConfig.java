package org.xyg.eshop.main.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(
	basePackages = {"org.springrabbit.resource.feign", "org.springrabbit.system.feign","org.springrabbit.flow.core.feign", "org.springrabbit.system.user.feign","org.xyg"}
)
public class EshopMainApplicationConfig {

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
			.readTimeout(60, TimeUnit.SECONDS)
			.connectTimeout(60, TimeUnit.SECONDS)
			.writeTimeout(60, TimeUnit.SECONDS)
			.build();
	}

	@Bean
	public ExecutorService executorService() {
		return Executors.newFixedThreadPool(16);
	}
}

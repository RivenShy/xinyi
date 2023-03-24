package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springrabbit.core.log.feign.ILogClient;
import org.springrabbit.core.log.model.LogUsual;
import org.springrabbit.core.tool.constant.RabbitConstant;
import org.xyg.ehop.common.constants.EshopConstants;
import org.xyg.eshop.main.erpentity.AloSync;
import org.xyg.eshop.main.mapper.AloSyncMapper;
import org.xyg.eshop.main.service.IAloSyncService;

import java.net.InetAddress;
import java.util.Date;

@Service
public class AloSyncServiceImpl extends ServiceImpl<AloSyncMapper, AloSync> implements IAloSyncService {

	@Autowired
	private ILogClient logClient;
	@Value("${rabbit.env}")
	private String env;

	@Override
	@Async
	public void sendCallError(Request request, String msg) {
		String params = null;
		if ("POST".equalsIgnoreCase(request.method())) {
			RequestBody body = request.body();
			if (body != null) {
				Buffer buffer = new Buffer();
				try {
					body.writeTo(buffer);
					params = buffer.readUtf8();
				} catch (Exception e) {
				}
			}
		} else {//只关心post
			return;
		}
		HttpUrl url = request.url();
		LogUsual logUsual = new LogUsual();
		logUsual.setLogData(msg);
		logUsual.setLogLevel("erp");
		try {
			logUsual.setServerIp(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
		}
		logUsual.setRequestUri(url.toString());
		logUsual.setServiceId(EshopConstants.APPLICATION_ERP_MAIN);
		logUsual.setTenantId(RabbitConstant.ADMIN_TENANT_ID);
		logUsual.setEnv(env);
		logUsual.setParams(params);
		logUsual.setMethod(request.method());
		logUsual.setCreateTime(new Date());
		logClient.saveUsualLog(logUsual);
	}

	@Override
	@Async
	public void sendCallError(Request request, String msg, String sys) {
		HttpUrl url = request.url();
		LogUsual logUsual = new LogUsual();
		logUsual.setLogData(msg);
		logUsual.setLogLevel(sys);
		try {
			logUsual.setServerIp(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
		}
		String params = null;
		if ("POST".equalsIgnoreCase(request.method())) {
			RequestBody body = request.body();
			if (body != null) {
				Buffer buffer = new Buffer();
				try {
					body.writeTo(buffer);
					params = buffer.readUtf8();
				} catch (Exception e) {
				}
			}
		}
		logUsual.setRequestUri(url.toString());
		logUsual.setServiceId(EshopConstants.APPLICATION_ERP_MAIN);
		logUsual.setTenantId(RabbitConstant.ADMIN_TENANT_ID);
		logUsual.setEnv(env);
		logUsual.setParams(params);
		logUsual.setMethod(request.method());
		logUsual.setCreateTime(new Date());
		logClient.saveUsualLog(logUsual);
	}

}

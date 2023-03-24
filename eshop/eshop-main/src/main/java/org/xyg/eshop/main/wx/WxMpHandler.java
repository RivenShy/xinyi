package org.xyg.eshop.main.wx;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springrabbit.core.tool.jackson.JsonUtil;
import org.springrabbit.core.tool.utils.AesUtil;
import org.springrabbit.core.tool.utils.Base64Util;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.prop.WxMpProperties;
import org.xyg.eshop.main.wx.reqs.SubscribeReq;
import org.xyg.eshop.main.wx.resp.BaseResp;
import org.xyg.eshop.main.wx.resp.SessionResp;
import org.xyg.eshop.main.wx.resp.TokenResp;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WxMpHandler {
	@Autowired
	private OkHttpClient okHttpClient;

	@Autowired
	private WxMpProperties wxMpProperty;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	private final String mixKey = "BnmAq!8^978moYzyExUA!VHBYEo#MLOp";

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	private <T extends BaseResp> T executeRequest(Call call, Class<T> clz) throws IOException {
//		log.info("请求 {}", call.request().url());
		Response execute = call.execute();
		if (execute.isSuccessful() && execute.body() != null) {
			String json = execute.body().string();
			execute.close();
			T t = JsonUtil.getInstance().readValue(json, clz);
			if (t.success()) {
				return t;
			} else if (t.needRetry()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				Call clone = call.clone();
				execute = clone.execute();
				if (execute.isSuccessful() && execute.body() != null) {
					json = execute.body().string();
					execute.close();
					t = JsonUtil.getInstance().readValue(json, clz);
					if (t.success()) {
						return t;
					}
				}
			}
			log.error("微信返回错误 {} -> {}:{}", call.request().url(), t.getErrcode(), t.getErrmsg());
			throw new IOException(String.format("微信端错误:%s", t.getErrmsg()));
		}
		String message = execute.message();
		execute.close();
		log.error("微信请求异常 {} : {}", execute.code(), message);
		throw new IOException("请求异常:" + execute.message());
	}

	public String decryptData(String data, String session, String iv) {
		byte[] mixBytes = AesUtil.decryptFormBase64(session, mixKey);
		byte[] keyBytes = Base64Util.decode(mixBytes);
		SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
			params.init(new IvParameterSpec(Base64Util.decodeFromString(iv)));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
			byte[] bytes = cipher.doFinal(Base64Util.decodeFromString(data));
			return new String(bytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			log.warn("微信解密失败", e);
		}
		return null;
	}


	public SessionResp code2Session(String code) throws IOException {
		if (StringUtil.isBlank(wxMpProperty.getAppid())) {
			throw new IOException("未配置小程序");
		}
		HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
		urlBuilder.scheme("https").host("api.weixin.qq.com").addPathSegment("sns").addPathSegment("jscode2session");
		urlBuilder.addQueryParameter("grant_type", "authorization_code");
		urlBuilder.addQueryParameter("appid", wxMpProperty.getAppid());
		urlBuilder.addQueryParameter("secret", wxMpProperty.getAppsecret());
		urlBuilder.addQueryParameter("js_code", code);
		Call call = okHttpClient.newCall(new Request.Builder().url(urlBuilder.build()).build());
		SessionResp resp = executeRequest(call, SessionResp.class);
		if (StringUtil.isNotBlank(resp.getSession_key())) {
			resp.setSession_key(AesUtil.encryptToBase64(resp.getSession_key(), mixKey));
		}
		return resp;
	}

	public String getAccessToken(boolean check) throws IOException {
		String tokenKey = String.format("WX:MP:TOKEN:%s", wxMpProperty.getAppid());
		String value = check ? null : stringRedisTemplate.opsForValue().get(tokenKey);
		if (StringUtil.isNotBlank(value)) {
			return value;
		}
		HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
		urlBuilder.scheme("https").host("api.weixin.qq.com").addPathSegment("cgi-bin").addPathSegment("token");
		urlBuilder.addQueryParameter("grant_type", "client_credential");
		urlBuilder.addQueryParameter("appid", wxMpProperty.getAppid());
		urlBuilder.addQueryParameter("secret", wxMpProperty.getAppsecret());
		Call call = okHttpClient.newCall(new Request.Builder().url(urlBuilder.build()).build());
		TokenResp resp = executeRequest(call, TokenResp.class);
		stringRedisTemplate.opsForValue().set(tokenKey, resp.getAccess_token(), resp.getExpires_in() - 12, TimeUnit.SECONDS);
		return resp.getAccess_token();
	}

	@Async
	public void checkAccessToken() {
		String tokenKey = String.format("WX:MP:TOKEN:%s", wxMpProperty.getAppid());
		Long expire = stringRedisTemplate.getExpire(tokenKey);
		if (expire == null || expire == -2) {
			try {
				getAccessToken(true);
			} catch (IOException e) {
				log.warn("检查token时,获取异常", e);
			}
		}
	}

	public boolean sendSubscribeMsg(SubscribeReq req) {
		try {
			String token = getAccessToken(false);
			HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
			urlBuilder.scheme("https").host("api.weixin.qq.com").addPathSegment("cgi-bin")
				.addPathSegment("message").addPathSegment("subscribe").addPathSegment("send");
			urlBuilder.addQueryParameter("access_token", token);
			RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.http.
				MediaType.APPLICATION_JSON_UTF8_VALUE), JsonUtil.toJson(req));
			Call call = okHttpClient.newCall(new Request.Builder()
				.post(requestBody).url(urlBuilder.build()).build());
			BaseResp resp = executeRequest(call, BaseResp.class);
			return resp.success();
		} catch (IOException e) {
			log.warn("发送订阅消息异常 {}", req, e);
		}
		return false;
	}

	public String decryptSessionKey(String sessionKeyCiphertext) {
		return AesUtil.decryptFormBase64ToString(sessionKeyCiphertext, mixKey);
	}
}

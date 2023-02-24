package org.xyg.eshop.main.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.type.SimpleType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.jackson.JsonUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.dto.ContractTemplateDTO;
import org.xyg.eshop.main.dto.DbchainToken;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractExpireRemindConfig;
import org.xyg.eshop.main.service.IContractExpireRemindConfigService;
import org.xyg.eshop.main.service.IContractService;
import org.xyg.eshop.main.vo.ContractVO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/contract")
@Api(value = "合同信息",description = "合同信息")
public class ContractController {

	@Autowired
	private IContractService contractService;

	@Autowired
	private IContractExpireRemindConfigService contractExpireRemindConfigService;

	@Autowired
	private OkHttpClient okHttpClient;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入 contract")
	public R<Boolean> save(@RequestBody ContractDTO contractDTO) {
		return contractService.saveOrUpdate(contractDTO);
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "查询详情", notes = "传入 id")
	public R<Contract> getDetail(@RequestParam Long id) {
		if(Func.isEmpty(id)) {
			return R.fail("缺少必要的请求参数: id");
		}
		return R.data(contractService.selectById(id));
	}

	/**
	 * 自定义分页
	 * @param query
	 * @param contractDTO
	 * @return
	 */
	@GetMapping("/getPage")
	@ApiOperation(value = "分页条件查询")
	public R<IPage<ContractVO>> getPage(Query query, ContractDTO contractDTO) {
		return R.data(contractService.selectContractPage(Condition.getPage(query), contractDTO));
	}

	@PostMapping("/submit")
	@ApiOperation(value = "合同提交接口", notes = "传入 id")
	public R<Long> submit(@RequestBody ContractDTO contractDTO) {
		return contractService.submit(contractDTO);
	}

	@PostMapping("/contractSubmitExecutionStartCallback")
	@ApiOperation(value = "流程实例任务开始回调接口")
	public R<CallBackMethodResDto> contractSubmitExecutionStartCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			log.info("合同提交任务开始回调接口");
			return R.data(contractService.flowInstanceExecutionStartCallback(inDto));
//			return R.data(null);
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	@PostMapping("/contractSubmitExecutionEndCallback")
	@ApiOperation(value = "流程实例任务结束回调接口")
	public R<CallBackMethodResDto> contractSubmitExecutionEndCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			log.info("合同提交任务结束回调接口");
			return R.data(contractService.flowInstanceExecutionEndCallback(inDto));
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	@PostMapping("/deletePersonnel")
	@ApiOperation(value = "删除合同相关人员", notes = "传入 id")
	public R<Boolean> deletePersonnel(@RequestParam("id") Long id) {
		return contractService.deletePersonnelById(id);
	}

	@GetMapping("/getExpireRemindConfig")
	@ApiOperation(value = "查询合同到期前提醒配置", notes = "无需传参数")
	public R<ContractExpireRemindConfig> getExpireRemindConfig() {
		return R.data(contractExpireRemindConfigService.getById(1));
	}

	@PostMapping("/updateExpireRemindConfig")
	@ApiOperation(value = "更新合同到期前提醒配置", notes = "传入 contractExpireRemindConfig")
	public R<ContractExpireRemindConfig> getExpireRemindConfig(@RequestBody ContractExpireRemindConfig contractExpireRemindConfig) {
		contractExpireRemindConfig.setId(1L);
		return R.status(contractExpireRemindConfigService.updateById(contractExpireRemindConfig));
	}

	@PostMapping("/getContractTemplateList")
	@ApiOperation(value = "获取合同列表", notes = "传入 query")
	public R getContractTemplateListFromRas(Query query) throws IOException {
		return R.data(getContractTemplateListFromRas());
	}

	@PostMapping("/addContractTemplate")
	@ApiOperation(value = "获取合同列表", notes = "传入 contractTemplateDTO")
	public R addContractTemplate(@RequestBody ContractTemplateDTO contractTemplateDTO) throws IOException {
		return R.data(addContractTemplateToRas(contractTemplateDTO));
	}

//	@PostMapping("/updateContractTemplate")
//	@ApiOperation(value = "获取合同列表", notes = "传入 contractTemplateDTO")
//	public R updateContractTemplate(@RequestBody ContractTemplateDTO contractTemplateDTO) throws IOException {
//		return R.data(updateContractTemplateInRas(contractTemplateDTO));
//	}

	@GetMapping("/getContractTemplateDetail")
	@ApiOperation(value = "获取合同列表", notes = "传入 id")
	public R addContractTemplate(@RequestParam("id") Long id) throws IOException {
		return R.data(getContractTemplateDetailFromRas(id));
	}

	private Object getContractTemplateDetailFromRas(Long id) throws IOException {
		HttpUrl httpUrl = HttpUrl
			.get(String.format("%s%s", "http://10.99.1.89/api/", "oldoc-main/template/detail"))
			.newBuilder()
			.addQueryParameter("id", String.valueOf(id))
			.build();
		//
		Call call = okHttpClient.newCall(new Request.Builder()
			.addHeader("rabbit-auth", getTokenFromRas("eshop_contract_template_get_token_from_ras"))
			.url(httpUrl)
			.build());
		try {
			Response response = null;
			try {
				response = call.execute();
				if (response.isSuccessful() && response.body() != null) {
					Map<String, Object> map = JsonUtil.toMap(response.body().string());
					Integer code = map == null ? 0 : (Integer) map.get("code");
					if (code != null && code == 200) {
						Object data = map.get("data");
						if (data == null) {
							throw new ServiceException("API返回格式异常");
						}
						return data;
					} else {
						Object msg = map == null ? null : map.get("msg");
						String o = msg == null ? "API接口异常" : msg.toString();
						throw new ServiceException(o);
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (IOException e) {
		}
		return null;
	}

	private Map addContractTemplateToRas(ContractTemplateDTO contractTemplateDTO) throws IOException {
		Map<String, Object> result = new HashMap<>();
		HttpUrl httpUrl = HttpUrl
			.get(String.format("%s%s", "http://10.99.1.89/api/", "oldoc-main/template/add"))
			.newBuilder()
			.build();
		//
		Request.Builder reqBuilder = new Request.Builder()
			.post(okhttp3.RequestBody.create(MediaType.get(org.springframework.
				http.MediaType.APPLICATION_JSON_UTF8_VALUE), new byte[0]))
			.url(httpUrl);
//		List<String> strings = httpUrl.pathSegments();
//		if (strings.size() > 2) {
//			if (strings.get(0).startsWith("rabbit-") || strings.get(1).startsWith("rabbit-")) {
//				reqBuilder.header("rabbit-auth", WebUtil.getHeader("rabbit-auth"));
//			}
//		}
		Map<String, String> mapArgs = new HashMap<>();
		mapArgs.put("businessType", contractTemplateDTO.getBusinessType());
		mapArgs.put("businessSystem", contractTemplateDTO.getBusinessSystem());
		mapArgs.put("originalName", String.valueOf(contractTemplateDTO.getOriginalName()));
		mapArgs.put("name", String.valueOf(contractTemplateDTO.getName()));
		mapArgs.put("link", String.valueOf(contractTemplateDTO.getLink()));
		mapArgs.put("attachId", String.valueOf(contractTemplateDTO.getAttachId()));
		mapArgs.put("seqNum", String.valueOf(contractTemplateDTO.getSeqNum()));
		mapArgs.put("dataRegionNames", contractTemplateDTO.getDataRegionNames());
		Call call = okHttpClient.newCall(new Request.Builder()
			.addHeader("rabbit-auth", getTokenFromRas("eshop_contract_template_get_token_from_ras"))
			.url(httpUrl)
			.post(okhttp3.RequestBody.create(MediaType.get(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE),
				JsonUtil.toJson(mapArgs))).build());
		try {
			Response response = null;
			try {
				response = call.execute();
				if (response.code() == 401) {
					response.close();
					throw new IOException("401无权限");
				}
				if (response.isSuccessful() && response.body() != null) {
					Map<String, Object> map = JsonUtil.toMap(response.body().string());
					Integer code = map == null ? 0 : (Integer) map.get("code");
					if (code != null && code == 200) {
						Object data = map.get("data");
						log.info("data:" + data);
						if (data == null) {
							throw new ServiceException("API返回格式异常");
						}
						if (data instanceof Map) {
							Map<String, Object> innerMap = (Map<String, Object>) data;
							result.put("records", innerMap.get("records"));
							if (innerMap.containsKey("current")) {
								result.put("current", innerMap.get("current"));
							}
							if (innerMap.containsKey("total")) {
								result.put("total", innerMap.get("total"));
							}
						} else if (data instanceof List) {
							result.put("records", data);
						}
					} else {
						Object msg = map == null ? null : map.get("msg");
						String o = msg == null ? "API接口异常" : msg.toString();
						throw new ServiceException(o);
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (IOException e) {
		}
		return result;
	}

	private Map getContractTemplateListFromRas() throws IOException {
		Map<String, Object> result = new HashMap<>();
		HttpUrl httpUrl = HttpUrl
			.get(String.format("%s%s", "http://10.99.1.89/api/", "oldoc-main/template/list"))
			.newBuilder()
			.build();
		//
		Request.Builder reqBuilder = new Request.Builder()
			.post(okhttp3.RequestBody.create(MediaType.get(org.springframework.
				http.MediaType.APPLICATION_JSON_UTF8_VALUE), new byte[0]))
			.url(httpUrl);
//		List<String> strings = httpUrl.pathSegments();
//		if (strings.size() > 2) {
//			if (strings.get(0).startsWith("rabbit-") || strings.get(1).startsWith("rabbit-")) {
//				reqBuilder.header("rabbit-auth", WebUtil.getHeader("rabbit-auth"));
//			}
//		}
		Call call = okHttpClient.newCall(new Request.Builder()
			.addHeader("rabbit-auth", getTokenFromRas("eshop_contract_template_get_token_from_ras"))
			.url(httpUrl)
//			.post(okhttp3.RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE), ""))
			.build());
		try {
			Response response = null;
			try {
				response = call.execute();
				if (response.isSuccessful() && response.body() != null) {
					Map<String, Object> map = JsonUtil.toMap(response.body().string());
					Integer code = map == null ? 0 : (Integer) map.get("code");
					if (code != null && code == 200) {
						Object data = map.get("data");
						log.info("data:" + data);
						if (data == null) {
							throw new ServiceException("API返回格式异常");
						}
						if (data instanceof Map) {
							Map<String, Object> innerMap = (Map<String, Object>) data;
							result.put("records", innerMap.get("records"));
							if (innerMap.containsKey("current")) {
								result.put("current", innerMap.get("current"));
							}
							if (innerMap.containsKey("total")) {
								result.put("total", innerMap.get("total"));
							}
						} else if (data instanceof List) {
							result.put("records", data);
						}
					} else {
						Object msg = map == null ? null : map.get("msg");
						String o = msg == null ? "API接口异常" : msg.toString();
						throw new ServiceException(o);
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (IOException e) {
		}
		return result;
	}

	private String getTokenFromRas(String tokenKey) throws IOException {
		String tokenValue = stringRedisTemplate.opsForValue().get(tokenKey);
		if(tokenValue != null) {
			log.info("从redis获取token");
			return tokenValue;
		}
		log.info("从ras获取token");
		HttpUrl httpUrl = HttpUrl
			.get(String.format("%s%s", "http://10.99.1.89/api/", "rabbit-auth/oauth/token"))
			.newBuilder()
			.addQueryParameter("tenantId", "000000")
			.addQueryParameter("username", "admin")
			.addQueryParameter("password", "21232f297a57a5a743894a0e4a801fc3")
			.addQueryParameter("grant_type", "password")
			.addQueryParameter("scope", "all")
			.addQueryParameter("type", "account")
			.build();

		// 设置请求格式，发送请求获取token
		Call call = okHttpClient.newCall(new Request.Builder()
			.addHeader("Authorization", "Basic c2FiZXI6c2FiZXJfc2VjcmV0")
			.url(httpUrl)
			.post(okhttp3.RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE), ""))
			.build());
		DbchainToken data = null;
		Response execute = null;
		try {
			execute = call.execute();
		} catch (IOException e) {
			log.error("请求ras token:{}", e.getMessage());
			e.printStackTrace();
		}
		if (execute.isSuccessful() && execute.body() != null) {
			String body = execute.body().string();
			execute.close();
			data = JsonUtil.getInstance().readValue(body, SimpleType.constructUnsafe(DbchainToken.class));
		} else {
			throw new IOException("toke获取异常");
		}
		String access_token = data.getAccess_token();
		String token_type = data.getToken_type();
		access_token = String.format("%s %s", token_type, access_token);
		int expires_in = data.getExpires_in();
		stringRedisTemplate.opsForValue().set(tokenKey, access_token, expires_in - 10, TimeUnit.SECONDS);
		return access_token;
	}
}

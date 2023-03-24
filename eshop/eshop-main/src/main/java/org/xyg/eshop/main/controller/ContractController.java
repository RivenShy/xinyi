package org.xyg.eshop.main.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.jackson.JsonUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.springrabbit.system.cache.DictCache;
import org.springrabbit.system.entity.Dict;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.dto.ContractTemplateDTO;
import org.xyg.eshop.main.dto.ContractTemplateToken;
import org.xyg.eshop.main.dto.DocumentCreateDto;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractExpireRemindConfig;
import org.xyg.eshop.main.entity.ContractTemplate;
import org.xyg.eshop.main.enums.ContractTemplateTypeEnum;
import org.xyg.eshop.main.prop.ContractTemplateProperties;
import org.xyg.eshop.main.service.IContractExpireRemindConfigService;
import org.xyg.eshop.main.service.IContractService;
import org.xyg.eshop.main.vo.ContractTemplateVO;
import org.xyg.eshop.main.vo.ContractVO;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/contract")
@Api(value = "合同信息", tags = "合同信息")

public class ContractController extends RabbitController {
	@Autowired
	private IContractService contractService;

	@Autowired
	private IContractExpireRemindConfigService contractExpireRemindConfigService;

	@Autowired
	private OkHttpClient okHttpClient;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	protected ContractTemplateProperties contractTemplateProperties;

	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入 contract")
	public R<Boolean> save(@RequestBody ContractDTO contractDTO) {
		return contractService.insertData(contractDTO, "save");
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "查询详情", notes = "传入 id")
	@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true, dataType = "Long")
	public R<ContractVO> getDetail(@RequestParam Long id) {
		if(Func.isEmpty(id)) {
			return R.fail("缺少必要的请求参数: id");
		}
		return R.data(contractService.selectById(id));
	}

	@GetMapping("/getChangeOrSupplementContract")
	@ApiOperation(value = "查询变更合同或补充协议", notes = "传入 contractDTO")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "contractType", value = "contractType", paramType = "query", required = true, dataType = "Integer"),
		@ApiImplicitParam(name = "originContractId", value = "originContractId", paramType = "query", required = true, dataType = "Long")
	})
	public R<List<ContractVO>> getChangeOrSupplementContract(ContractDTO contractDTO) {
		return contractService.selectChangeOrSupplementContract(contractDTO);
	}

	/**
	 * 自定义分页
	 * @param query
	 * @param contractDTO
	 * @return
	 */
	@GetMapping("/getPage")
	@ApiOperation(value = "分页条件查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "storeName", value = "storeName", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "contractCode", value = "contractCode", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "contractType", value = "contractType", paramType = "query", dataType = "Integer"),
		@ApiImplicitParam(name = "contractStatusList", value = "contractStatusList", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "contractExpireDateQueryList", value = "contractExpireDateQueryList", paramType = "query", dataType = "string")
	})
	public R<IPage<ContractVO>> getPage(Query query, ContractDTO contractDTO) {
		return R.data(contractService.selectContractPage(Condition.getPage(query), contractDTO));
	}

	@PostMapping("/submit")
	@ApiOperation(value = "合同提交接口", notes = "传入 contractDTO")
	public R<Boolean> submit(@RequestBody ContractDTO contractDTO) {
		return contractService.insertData(contractDTO, "submit");
	}

	@PostMapping("/contractSubmitExecutionStartCallback")
	@ApiOperation(value = "流程实例任务开始回调接口")
	public R<CallBackMethodResDto> contractSubmitExecutionStartCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			log.info("合同提交任务开始回调接口");
			return R.data(contractService.flowInstanceExecutionStartCallback(inDto));
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
	@ApiImplicitParam(name = "id", value = "id", paramType = "delete", dataType = "Long")
	public R<Boolean> deletePersonnel(@RequestParam("id") Long id) {
		return contractService.deletePersonnelById(id);
	}

	@GetMapping("/getExpireRemindConfig")
	@ApiOperation(value = "查询合同到期前提醒配置", notes = "无需传参数")
	public R<ContractExpireRemindConfig> getExpireRemindConfig() {
//		return R.data(contractExpireRemindConfigService.get(1));
		return R.data(contractExpireRemindConfigService.getOne(new QueryWrapper<>()));

	}

	@PostMapping("/updateExpireRemindConfig")
	@ApiOperation(value = "更新合同到期前提醒配置", notes = "传入 contractExpireRemindConfig")
	public R<ContractExpireRemindConfig> updateExpireRemindConfig(@RequestBody ContractExpireRemindConfig contractExpireRemindConfig) {
		ContractExpireRemindConfig contractExpireRemindConfigDB = contractExpireRemindConfigService.getOne(new QueryWrapper<>());
		contractExpireRemindConfig.setId(contractExpireRemindConfigDB.getId());
		return R.status(contractExpireRemindConfigService.updateById(contractExpireRemindConfig));
	}

	@PostMapping("/getContractTemplateList")
	@ApiOperation(value = "条件查询合同模板列表", notes = "传入 query")
	public R getContractTemplateList(Query query, ContractTemplateDTO contractTemplateDTO) throws IOException {
		return R.data(getContractTemplateListFromRas(query, contractTemplateDTO));
	}

	@PostMapping("/addContractTemplate")
	@ApiOperation(value = "新增合同模板", notes = "传入 contractTemplateDTO")
	public R addContractTemplate(@RequestBody ContractTemplateDTO contractTemplateDTO) throws IOException {
		return R.data(addContractTemplateToRas(contractTemplateDTO));
	}

//	@PostMapping("/addContractDocument")
//	@ApiOperation(value = "新增合同文档", notes = "传入 documentCreateDto")
//	public R addContractDocument(@RequestBody DocumentCreateDto documentCreateDto) throws IOException {
//		return R.data(addContractDocumentToRas(documentCreateDto));
//	}

	@GetMapping("/removeContractTemplate")
	@ApiOperation(value = "删除合同模板", notes = "传入ids")
	public R removeContractTemplate(@ApiParam(value = "主键集合", required = true)
					@RequestParam(value = "ids") String ids,
					@RequestParam(value = "deleteFile") Integer deleteFile) throws IOException {
		return R.status(removeContractTemplateInRas(ids, deleteFile));
	}

	@GetMapping("/getContractTemplateDetail")
	@ApiOperation(value = "查询合同模板详情", notes = "传入 id")
	public R addContractTemplate(@RequestParam("id") Long id) throws IOException {
		return R.data(getContractTemplateDetailFromRas(id));
	}

	private Boolean removeContractTemplateInRas(String ids, Integer deleteFile) throws IOException {
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("ids", ids);
		queryMap.put("deleteFile", deleteFile);
		Call call = getRequest("oldoc-main/template/remove", queryMap);
		try {
			Response response = null;
			try {
				response = call.execute();
				if (response.isSuccessful() && response.body() != null) {
					Map<String, Object> map = JsonUtil.toMap(response.body().string());
					Integer code = map == null ? 0 : (Integer) map.get("code");
					if (code != null && code == 200) {
						Boolean success = (Boolean) map.get("success");
						return success;
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

	protected Call getRequest(String path, Map<String, ? extends Object> query) throws IOException {
		HttpUrl httpUrl = HttpUrl.get(String.format("%s%s", contractTemplateProperties.getBaseurl(), path));
		if (CollectionUtil.isNotEmpty(query)) {
			HttpUrl.Builder newBuilder = httpUrl.newBuilder();
			query.forEach((k,v) -> {
				if(v != null) {
					newBuilder.addQueryParameter(k, String.valueOf(v));
				}
			});
			httpUrl = newBuilder.build();
		}
		Request request = new Request.Builder().url(httpUrl).addHeader("rabbit-auth", getTokenFromRas()).build();
		return okHttpClient.newCall(request);
	}

	protected Call postRequest(String path, Object obj) throws IOException {
		HttpUrl httpUrl = HttpUrl.get(String.format("%s%s", contractTemplateProperties.getBaseurl(), path));
		okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE),
			obj == null ? "" : JsonUtil.toJson(obj));
		Request.Builder builder = new Request.Builder().url(httpUrl).post(requestBody).addHeader("rabbit-auth", getTokenFromRas());
		return okHttpClient.newCall(builder.build());
	}

	private Object getContractTemplateDetailFromRas(Long id) throws IOException {
		Map queryMap = new HashMap();
		queryMap.put("id", id);
		Call call = getRequest("oldoc-main/template/detail", queryMap);
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
		contractTemplateDTO.setBusinessSystem("eshop");
		Map<String, Object> result = new HashMap<>();
		Call call = postRequest("oldoc-main/template/add", contractTemplateDTO);
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

	private Map addContractDocumentToRas(DocumentCreateDto documentCreateDto) throws IOException {
		documentCreateDto.setBusinessSystem("eshop");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("PO_StoreName", "芜湖门店");
		documentCreateDto.setBusinessData(JSON.toJSONString(jsonObject));
		Map<String, Object> result = new HashMap<>();
		Call call = postRequest("oldoc-main/document/add", documentCreateDto);
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

	private Map getContractTemplateListFromRas(Query query, ContractTemplateDTO contractTemplateDTO) throws IOException {
		Map<String, Object> queryMap = new HashMap();
		queryMap.put("current", query.getCurrent());
		queryMap.put("id", contractTemplateDTO.getId());
		queryMap.put("businessSystem", "eshop");
		queryMap.put("businessType", contractTemplateDTO.getBusinessType());
		queryMap.put("originalName", contractTemplateDTO.getOriginalName());
		Call call = getRequest("oldoc-main/template/list", queryMap);
		Map<String, Object> result = new HashMap<>();
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
						if (data == null) {
							throw new ServiceException("API返回格式异常");
						}
						if (data instanceof Map) {
							Map<String, Object> innerMap = (Map<String, Object>) data;
							List list = (List) innerMap.get("records");
							ObjectMapper objectMapper = new ObjectMapper();
							String json = objectMapper.writeValueAsString(list);
							List<ContractTemplateVO> contractTemplateList = JSON.parseArray(json, ContractTemplateVO.class);
							for(ContractTemplateVO contractTemplateVO : contractTemplateList) {
								if(contractTemplateVO.getBusinessType().equals("common_join_contract")) {
									String contractTemplateType = null;
									List<Dict> dictList = DictCache.getList(EShopMainConstant.CONTRACT_TEMPLATE_TYPE_DICT_CODE);
									for(Dict dict : dictList) {
										if(dict.getDictValue().equals(ContractTemplateTypeEnum.COMMON_JOIN.getName())) {
											contractTemplateType = dict.getDictKey();
										}
									}
									contractTemplateVO.setContractTemplateType(contractTemplateType);
								}
							}
							result.put("records", contractTemplateList);
							if (innerMap.containsKey("current")) {
								result.put("current", innerMap.get("current"));
							}
							if (innerMap.containsKey("total")) {
								result.put("total", innerMap.get("total"));
							}
						} else if (data instanceof List) {
//							List<ContractTemplate> contractTemplateList = JSON.parseArray(data, ContractTemplate.class);
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

	private String getTokenFromRas() throws IOException {
		final String tokenKey = "eshop_contract_template_get_token_from_ras";
		synchronized (tokenKey.intern()) {
			String value = stringRedisTemplate.opsForValue().get(tokenKey);
			if (StringUtil.isNotBlank(value)) {
				log.info("从redi获取token");
				return value;
			}
			log.info("从ras获取token");
			HttpUrl httpUrl = HttpUrl
				.get(String.format("%s%s", contractTemplateProperties.getBaseurl(), "rabbit-auth/oauth/token"))
				.newBuilder()
				.addQueryParameter("tenantId", contractTemplateProperties.getTenantId())
				.addQueryParameter("username", contractTemplateProperties.getUsername())
				.addQueryParameter("password", contractTemplateProperties.getPassword())
				.addQueryParameter("grant_type", contractTemplateProperties.getGrant_type())
				.addQueryParameter("scope", contractTemplateProperties.getScope())
				.addQueryParameter("type", contractTemplateProperties.getType())
				.build();

			// 设置请求格式，发送请求获取token
			Call call = okHttpClient.newCall(new Request.Builder()
				.addHeader("Authorization", contractTemplateProperties.getAuthorization())
				.url(httpUrl)
				.post(okhttp3.RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE), ""))
				.build());
			ContractTemplateToken data = null;
			Response execute = null;
			try {
				execute = call.execute();
				if (execute.code() == 401) {
					execute.close();
					throw new IOException("401无权限");
				}
			} catch (IOException e) {
				log.error("请求ras token:{}", e.getMessage());
				e.printStackTrace();
			}
			if (execute.isSuccessful() && execute.body() != null) {
				String body = execute.body().string();
				execute.close();
				data = JsonUtil.getInstance().readValue(body, SimpleType.constructUnsafe(ContractTemplateToken.class));
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
}

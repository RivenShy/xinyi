package org.xyg.eshop.main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.secure.utils.AuthUtil;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.ObjectUtil;
import org.xyg.eshop.main.entity.WxMpUser;
import org.xyg.eshop.main.service.IWxMpUserService;
import org.xyg.eshop.main.wx.WxMpHandler;
import org.xyg.eshop.main.wx.reqs.SubscribeReq;
import org.xyg.eshop.main.wx.resp.SessionResp;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@Api("微信小程序相关")
@RequestMapping("/wx")
public class WxController {

	@Autowired
	private WxMpHandler wxMpHandler;
//	@Autowired
//	private UserFeign userFeign;

	@Autowired
	private IWxMpUserService wxMpUserService;


	@PostMapping("/login")
	@ApiOperation(value = "更新session_key", notes = "传入 code")
	@ApiImplicitParam(name = "微信授权码", value = "code", paramType = "query", required = true, dataType = "string")
	public R<SessionResp> code2Session(@RequestParam("code") String code) {
		try {
			SessionResp data = wxMpHandler.code2Session(code);
			return R.data(data);
		} catch (IOException e) {
			return R.fail(e.getMessage());
		}
	}

	@PostMapping("/bindUserAccount")
	@ApiOperation(value = "账号密码登录后，令牌加上code完成绑定操作", notes = "传入 code、device")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "微信授权码", value = "code", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "小程序登录设备", value = "device", paramType = "query", dataType = "string"),
	})
	public R<SessionResp> bindUserAccount(@RequestParam("code") String code,
								@RequestParam(required = false,value = "device") String device) throws IOException {
		RabbitUser user = AuthUtil.getUser();
		if(ObjectUtil.isEmpty(user)) {
			return R.fail("未登录系统账号");
		}
		SessionResp data = wxMpHandler.code2Session(code);
		Long userId = user.getUserId();
		String openId = data.getOpenid();
		WxMpUser wxMpUserGetInDb = wxMpUserService.getByUserId(userId);
		if(!ObjectUtil.isEmpty(wxMpUserGetInDb)) {
			if(!openId.equals(wxMpUserGetInDb)) {
				wxMpUserGetInDb.setOpenId(openId);
				wxMpUserGetInDb.setDevice(device);
				wxMpUserService.updateOpenIdDeviceById(wxMpUserGetInDb);
			}
		} else {
			WxMpUser wxMpUser = new WxMpUser();
			wxMpUser.setUserId(userId);
			wxMpUser.setOpenId(openId);
			wxMpUser.setDevice(device);
			boolean saveWxMpUser = wxMpUserService.save(wxMpUser);
			if(!saveWxMpUser) {
				log.error("保存账号与小程序用户身份关系失败，用户账号=" + user.getAccount());
			}
		}
		return R.data(data);
	}

	@PostMapping("/getSignature")
	@ApiOperation(value = "获取数据签名signature,", notes = "传入 rawData、sessionKey密文")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "数据", value = "rawData", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "sessionKey密文", value = "sessionKey", paramType = "query", dataType = "string"),
	})
	public R<String> getSignature(@RequestBody Map<String, String> body) {
		String sessionKeyCiphertext =  body.get("sessionKey");
		String session_key = wxMpHandler.decryptSessionKey(sessionKeyCiphertext);
		return R.data(DigestUtils.sha1Hex(body.get("rawData") + session_key));
	}

	@PostMapping("/decrypt")
	@ApiOperation(value = "解密小程序用户的敏感信息", notes = "传入data, key, iv")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "数据", value = "data", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "sessionKey密文", value = "sessionKey", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "iv", value = "iv", paramType = "query", dataType = "string"),
	})
	public R<String> decryptData(@RequestBody Map<String, String> body) {
		String sessionKeyCiphertext =  body.get("sessionKey");
		String session_key = wxMpHandler.decryptSessionKey(sessionKeyCiphertext);
		return R.data(wxMpHandler.decryptData(body.get("data"), session_key, body.get("iv")));
	}

	@PostMapping("/sendSubscribe")
	public R<Boolean> sendSubscribeMsg(@RequestBody SubscribeReq req) {
		return R.data(wxMpHandler.sendSubscribeMsg(req));
	}

}

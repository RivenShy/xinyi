package org.xyg.eshop.main.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.resource.feign.ISmsClient;
import org.springrabbit.system.dto.MessageDTO;
import org.springrabbit.system.entity.Dict;
import org.springrabbit.system.feign.IDictClient;
import org.springrabbit.system.feign.IMessageClient;
import org.springrabbit.system.user.entity.User;
import org.springrabbit.system.user.feign.IUserClient;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dao.IContractDao;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractExpireRemindConfig;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.enums.ContractExpireDateRemindEnum;
import org.xyg.eshop.main.enums.ContractStatusEnum;
import org.xyg.eshop.main.enums.NotificationMethodEnum;
import org.xyg.eshop.main.enums.NotificationObjectEnum;
import org.xyg.eshop.main.service.IContractExpireRemindConfigService;
import org.xyg.eshop.main.service.IContractService;
import org.xyg.eshop.main.service.IStorefrontService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class ContractScheduleJob {

	private final IContractService contractService;

	private final IMessageClient messageClient;

	private final IUserClient userClient;

	private final IContractExpireRemindConfigService contractExpireRemindConfigService;

	private final IContractDao contractDao;

	private final IStorefrontService storefrontService;

	private final ISmsClient smsClient;

	private final IDictClient dictClient;

	@XxlJob("updateContractStatusIfExpired")
	public ReturnT<String> updateContractStatusIfExpired(String param) {
		XxlJobLogger.log("执行eshop查询到期合同，更新状态任务》》》》》》开始");
		log.info("执行eshop查询到期合同，更新状态任务》》》》》》开始");
		contractService.updateContractStatusIfExpired();
		log.info("执行eshop查询到期合同，更新状态任务》》》》》》结束");
		XxlJobLogger.log("执行eshop查询到期合同，更新状态任务》》》》》》结束");
		return ReturnT.SUCCESS;
	}

	@XxlJob("contractExpirationRemind")
	public ReturnT<String> contractExpirationRemind(String param) throws ParseException {
		XxlJobLogger.log("执行eshop合同到期前提醒任务》》》》》》开始");
		log.info("执行eshop合同到期前提醒任务》》》》》》开始");
		List<Contract> contractList = contractDao.lambdaQuery().
			ne(Contract::getContractStatus, ContractStatusEnum.EXPIRED.getIndex())
			.list();
		if(contractList == null) {
			log.error("查询合同信息失败");
			return ReturnT.FAIL;
		}
		ContractExpireRemindConfig contractExpireRemindConfig = contractExpireRemindConfigService.getOne(new QueryWrapper<>());
		if(contractExpireRemindConfig == null) {
			log.error("查询合同到期提醒配置信息失败");
			return ReturnT.FAIL;
		}
		String remindTimeBeforeContractExpire = contractExpireRemindConfig.getRemindTimeBeforeContractExpire();
		String notificationObject = contractExpireRemindConfig.getNotificationObject();
		String notificationMethod = contractExpireRemindConfig.getNotificationMethod();
		if(Func.isEmpty(remindTimeBeforeContractExpire) || Func.isEmpty(notificationObject)
			|| Func.isEmpty(notificationMethod)) {
			log.info("执行eshop查合同到期前提醒任务》》》》》》结束");
			XxlJobLogger.log("执行eshop合同到期前提醒任务》》》》》》结束");
			return ReturnT.SUCCESS;
		}
		String[] remindTimeBeforeContractExpireArr = remindTimeBeforeContractExpire.split(",");
		String[] notificationObjectArr = notificationObject.split(",");
		String[] notificationMethodArr = notificationMethod.split(",");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDateStr = sdf.format(new Date());
		Date nowDate = sdf.parse(nowDateStr);
		Calendar cal = Calendar.getInstance();
		for(String rtbce : remindTimeBeforeContractExpireArr) {
			String contractExpireRemindDictValue = getDictValueByKey(EShopMainConstant.ESHOP_CONTRACT_EXPIRATION_REMIND_DICT_CODE, rtbce);
			if(contractExpireRemindDictValue == null) {
				log.error("查询合同到期提醒字典信息失败");
				return ReturnT.FAIL;
			}
			for(Contract contract : contractList) {
				cal.setTime(contract.getContractExpireDate());
				if(ContractExpireDateRemindEnum.A_WEEK.getName().equals(contractExpireRemindDictValue)) {
					cal.add(Calendar.DATE, -7);
					Date aWeekBeforeExpire = sdf.parse(sdf.format(cal.getTime()));
					if(nowDate.compareTo(aWeekBeforeExpire) == 0) {
						sendMessageByConfig(ContractExpireDateRemindEnum.A_WEEK.getName(), notificationObjectArr, notificationMethodArr, contract);
					}
				} else if(ContractExpireDateRemindEnum.HALF_MONTH.getName().equals(contractExpireRemindDictValue)) {
					cal.add(Calendar.DATE, -15);
					Date twoWeekBeforeExpire = sdf.parse(sdf.format(cal.getTime()));
					if(nowDate.compareTo(twoWeekBeforeExpire) == 0) {
						sendMessageByConfig(ContractExpireDateRemindEnum.HALF_MONTH.getName(), notificationObjectArr, notificationMethodArr, contract);
					}
				} else if(ContractExpireDateRemindEnum.A_MONTH.getName().equals(contractExpireRemindDictValue)) {
					cal.add(Calendar.MONTH, -1);
					Date aMonthBeforeExpire = sdf.parse(sdf.format(cal.getTime()));
					if(nowDate.compareTo(aMonthBeforeExpire) == 0) {
						sendMessageByConfig(ContractExpireDateRemindEnum.A_MONTH.getName(), notificationObjectArr, notificationMethodArr, contract);
					}
				} else if(ContractExpireDateRemindEnum.TWO_MONTH.getName().equals(contractExpireRemindDictValue)) {
					cal.add(Calendar.MONTH, -2);
					Date twoMonthBeforeExpire = sdf.parse(sdf.format(cal.getTime()));
					if(nowDate.compareTo(twoMonthBeforeExpire) == 0) {
						sendMessageByConfig(ContractExpireDateRemindEnum.TWO_MONTH.getName(), notificationObjectArr, notificationMethodArr, contract);
					}
				}
			}
		}
		log.info("执行eshop查合同到期前提醒任务》》》》》》结束");
		XxlJobLogger.log("执行eshop合同到期前提醒任务》》》》》》结束");
		return ReturnT.SUCCESS;
	}

	private void sendMessageByConfig(String contractExpiredDateBefore, String[] notificationObjectArr, String[] notificationMethodArr, Contract contract) {
		Storefront storefront = storefrontService.getById(contract.getStorefrontId());
		for(String notFicObject : notificationObjectArr) {
			String idStr = null;
			String notificationObjecctDictValue = getDictValueByKey(EShopMainConstant.ESHOP_NOTIFICATION_OBJECT_DICT_CODE, notFicObject);
			if(notificationObjecctDictValue == null) {
				log.error("查询合同到期提醒设置通知对象字典信息失败");
				return;
			}
			if(NotificationObjectEnum.STORE.getName().equals(notificationObjecctDictValue)) {
				// 根据门店Id查询门店信息
				String principalNO = storefront.getPrincipal();
				List<User> userList = userClient.userByCode(principalNO);
				idStr = userList.stream().map(user -> String.valueOf(user.getId())).collect(Collectors.joining(","));
			} else if(NotificationObjectEnum.SALESMAN.getName().equals(notificationObjecctDictValue)) {
				String salesrepNo = storefront.getSalesrepNo();
				List<User> userList = userClient.userByCode(salesrepNo);
				idStr = userList.stream().map(user -> String.valueOf(user.getId())).collect(Collectors.joining(","));
			} else if(NotificationObjectEnum.APPLICANT.getName().equals(notificationObjecctDictValue)) {
				idStr = String.valueOf(storefront.getCreatedBy());
			}
			for(String notficMethod : notificationMethodArr) {
				String notficMethodDictValue = getDictValueByKey(EShopMainConstant.ESHOP_NOTIFICATION_DICT_CODE, notficMethod);
				if(notficMethodDictValue == null) {
					log.error("查询合同到期前提醒设置通知方式字典信息失败");
					return;
				}
				if(NotificationMethodEnum.SYSTEM.getName().equals(notficMethodDictValue)) {
					sendSystemMessage(idStr, contract, contractExpiredDateBefore);
				} else if(NotificationMethodEnum.WECHAT_APPLET.getName().equals(notficMethodDictValue)) {
					// todo 暂时还没启用小程序消息和短信
				} else if(NotificationMethodEnum.SHORT_MESSAGE.getName().equals(notficMethodDictValue)) {
//					smsClient.sendMessage()
				}
			}
		}
	}

	private void sendSystemMessage(String receiverIds, Contract contract, String contractExpiredDateBefore) {
		// 发送系统消息
		log.info("发送系统消息");
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setSendTime(new Date());
		//		messageDTO.setCreatedBy("系统消息");
		messageDTO.setMessageType(0);
		messageDTO.setMessageSource("1");
		messageDTO.setTemplateCode("ehsop_contract_expire_remind");
		messageDTO.setTitle("合同到期提醒");
		messageDTO.setOpenType(1);
		messageDTO.setMessageContent("您好，您有一个合同将在" + contractExpiredDateBefore + "到期，合同编号:" + contract.getContractCode());
		messageDTO.setReceivers(receiverIds);
		log.info(messageClient.sendMessage(messageDTO).toString());
	}

	private String getDictValueByKey(String dictCode,String dictKey){
		String dictValue = null;
		if (StringUtil.isBlank(dictKey)){
			return dictValue;
		}

		try {
			R<Dict> dictR = dictClient.getDictInfo(dictCode, dictKey);
			if (dictR.isSuccess() && dictR.getData() != null){
				dictValue = dictR.getData().getDictValue();
			}
		} catch (Exception e) {
			log.error("获取字典值出现错误 code = {}, key = {}  {}",dictCode,dictKey,e);
		}
		return dictValue;
	}
}

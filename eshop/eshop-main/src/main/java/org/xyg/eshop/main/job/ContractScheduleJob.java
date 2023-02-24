package org.xyg.eshop.main.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.resource.feign.ISmsClient;
import org.springrabbit.system.dto.MessageDTO;
import org.springrabbit.system.feign.IMessageClient;
import org.springrabbit.system.user.entity.User;
import org.springrabbit.system.user.feign.IUserClient;
import org.xyg.eshop.main.dao.IContractDao;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractExpireRemindConfig;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.enums.ContractExpireDateEnum;
import org.xyg.eshop.main.enums.ContractStatusEnum;
import org.xyg.eshop.main.service.IContractExpireRemindConfigService;
import org.xyg.eshop.main.service.IContractService;
import org.xyg.eshop.main.service.IStorefrontService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
		ContractExpireRemindConfig contractExpireRemindConfig = contractExpireRemindConfigService.getById(1);
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
		List<Integer> remindTimeBeforeContractExpireList = Arrays.stream(remindTimeBeforeContractExpire.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		String[] notificationObjectArr = notificationObject.split(",");
		String[] notificationMethodArr = notificationMethod.split(",");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDateStr = sdf.format(new Date());
		Date nowDate = sdf.parse(nowDateStr);
		Calendar cal = Calendar.getInstance();
		for(Contract contract : contractList) {
			Date contractExpireDate = contract.getContractExpireDate();
			for(Integer remindTimeBeforeContractExpireInt : remindTimeBeforeContractExpireList) {
				if(remindTimeBeforeContractExpireInt == ContractExpireDateEnum.A_WEEK.getIndex()) {
					cal.setTime(contractExpireDate);
					cal.add(Calendar.DATE, -7);
					Date aWeekBeforeExpire = sdf.parse(sdf.format(cal.getTime()));
					if(nowDate.compareTo(aWeekBeforeExpire) == 0) {
						sendMessageByConfig(ContractExpireDateEnum.A_WEEK.getName(), notificationObjectArr, notificationMethodArr, contract);
					}
				} else if(remindTimeBeforeContractExpireInt == ContractExpireDateEnum.HALF_MONTH.getIndex()) {
					cal.setTime(contractExpireDate);
					cal.add(Calendar.DATE, -15);
					Date twoWeekBeforeExpire = sdf.parse(sdf.format(cal.getTime()));
					if(nowDate.compareTo(twoWeekBeforeExpire) == 0) {
						sendMessageByConfig(ContractExpireDateEnum.HALF_MONTH.getName(), notificationObjectArr, notificationMethodArr, contract);
					}
				} else if(remindTimeBeforeContractExpireInt == ContractExpireDateEnum.A_MONTH.getIndex()) {
					cal.setTime(contractExpireDate);
					cal.add(Calendar.MONTH, -1);
					Date aMonthBeforeExpire = sdf.parse(sdf.format(cal.getTime()));
					if(nowDate.compareTo(aMonthBeforeExpire) == 0) {
						sendMessageByConfig(ContractExpireDateEnum.A_MONTH.getName(), notificationObjectArr, notificationMethodArr, contract);
					}
				} else if(remindTimeBeforeContractExpireInt == ContractExpireDateEnum.TWO_MONTH.getIndex()) {
					cal.setTime(contractExpireDate);
					cal.add(Calendar.MONTH, -2);
					Date twoMonthBeforeExpire = sdf.parse(sdf.format(cal.getTime()));
					if(nowDate.compareTo(twoMonthBeforeExpire) == 0) {
						sendMessageByConfig(ContractExpireDateEnum.TWO_MONTH.getName(), notificationObjectArr, notificationMethodArr, contract);
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
			if(notFicObject.equals("门店")) {
				// 根据门店Id查询门店信息
				String principalNO = storefront.getPrincipal();
				List<User> userList = userClient.userByCode(principalNO);
				idStr = userList.stream().map(user -> String.valueOf(user.getId())).collect(Collectors.joining(","));
			} else if(notFicObject.equals("业务员")) {
				String salesrepNo = storefront.getSalesrepNo();
				List<User> userList = userClient.userByCode(salesrepNo);
				idStr = userList.stream().map(user -> String.valueOf(user.getId())).collect(Collectors.joining(","));
			} else if(notFicObject.equals("申请人")) {
				idStr = String.valueOf(storefront.getCreatedBy());
			}
			for(String notficMethod : notificationMethodArr) {
				if(notficMethod.equals("系统消息")) {
					sendSystemMessage(idStr, contract, contractExpiredDateBefore);
				} else if(notficMethod.equals("小程序消息")) {
					// todo 暂时还没启用小程序消息
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
//		R<User> ruser =  userClient.userInfoById(1123598821738675201L);
//		log.info("==========" + ruser + "====================");
	}


	public static int differentDaysByMillisecond(Date date1,Date date2)
	{
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
		return days;
	}
}

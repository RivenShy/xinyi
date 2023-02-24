package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;
import java.io.Serializable;

@TableName(value = "ESHOP_CONTRACT_EXPIRED_REMIND_CONFIG")
@Data
@ApiModel(value = "合同到期提醒配置", description = "合同到期提醒配置")
public class ContractExpireRemindConfig extends DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "合同到期前提醒时间")
	@TableField(value = "REMIND_TIME_BEFORE_CONTRACT_EXPIRE")
	private String remindTimeBeforeContractExpire;

	@ApiModelProperty(value = "通知对象")
	@TableField(value = "NOTIFICATION_OBJECT")
	private String notificationObject;

	@ApiModelProperty(value = "通知方式")
	@TableField(value = "NOTIFICATION_METHOD")
	private String notificationMethod;
}

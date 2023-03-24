package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.BaseEntity;
import java.io.Serializable;

@TableName(value = "RABBIT_SYSTEM_USER_MP")
@Data
@ApiModel(value = "系统用户与小程序用户关系表", description = "系统用户与小程序用户关系表")
public class WxMpUser  extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户Id")
	@TableField(value = "USER_ID")
	private Long userId;

	@ApiModelProperty(value = "小程序用户OPEN_ID")
	@TableField(value = "OPEN_ID")
	private String openId;

	@ApiModelProperty(value = "绑定小程序的设备")
	@TableField(value = "DEVICE")
	private String device;
}


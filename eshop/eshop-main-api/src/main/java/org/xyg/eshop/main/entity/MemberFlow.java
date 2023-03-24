package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "ESHOP_MEMBER_FLOW")
@Data
@ApiModel(value = "会员流水信息", description = "会员流水信息")
public class MemberFlow extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "会员ID")
	@TableField(value = "MEMBER_ID")
	private Long memberId;

	@ApiModelProperty(value = "流水号")
	@TableField(value = "SERIAL_NUMBER")
	private String serialNumber;

	@ApiModelProperty(value = "付款类型")
	@TableField(value = "PAYMENT_TYPE")
	private String paymentType;

	@ApiModelProperty(value = "车型")
	@TableField(value = "CAR_MODEL")
	private String carModel;

	@ApiModelProperty(value = "车牌")
	@TableField(value = "PLATE_NUMBER")
	private String plateNumber;

	@ApiModelProperty(value = "申请日期")
	@TableField(value = "APPLICATION_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date applicationDate;

	@ApiModelProperty(value = "生效日期")
	@TableField(value = "EFFECTIVE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date effectiveDate;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@ApiModelProperty(value = "到期日期")
	@TableField(value = "EXPIRATION_DATE")
	private Date expirationDate;

	@ApiModelProperty(value = "有效期/签约时长")
	@TableField(value = "TERM_OF_VALIDITY")
	private Integer termOfValidity;

	@ApiModelProperty(value = "付款金额")
	@TableField(value = "PAYMENT_AMOUNT")
	private BigDecimal paymentAmount;

	@ApiModelProperty(value = "门店id")
	@TableField(value = "STORE_FRONT_ID")
	private Long storefrontId;

	@ApiModelProperty(value = "付款流水单号")
	@TableField(value = "PAYMENT_STATEMENT_NO")
	private String paymentStatementNo;
}

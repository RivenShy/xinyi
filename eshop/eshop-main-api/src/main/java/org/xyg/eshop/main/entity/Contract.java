package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "ESHOP_CONTRACT_CONTRACT")
@Data
@ApiModel(value = "eshop合同信息", description = "eshop合同信息")
public class Contract extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否已删除 0默认未删除 1删除
	 */
	@TableLogic
	@TableField(value = "IS_DELETED")
	private Integer isDeleted;

	@ApiModelProperty(value = "合同编号")
	@TableField(value = "CONTRACT_CODE")
	private String contractCode;

	@ApiModelProperty(value = "客户名称")
	@TableField(value = "CUSTOMER_NAME")
	private String customerName;

	@ApiModelProperty(value = "统一信用代码")
	@TableField(value = "UNIFIED_CREDIT_CODE")
	private String unifiedCreditCode;

	@ApiModelProperty(value = "合同场景")
	@TableField(value = "CONTRACT_SCENE")
	private String contractScene;


	@ApiModelProperty(value = "客户等级")
	@TableField(value = "CUSTOMER_GRADE")
	private String customerGrade;

	@ApiModelProperty(value = "客户地址")
	@TableField(value = "CUSTOMER_ADDRESS")
	private String customerAddress;

	@ApiModelProperty(value = "信义提供的供应商")
	@TableField(value = "SUPPLIER")
	private String supplier;

	@ApiModelProperty(value = "是否使用合同模板")
	@TableField(value = "USE_CONTRACT_TEMPLATE")
	private Integer useContractTemplate;

	@ApiModelProperty(value = "合同模板")
	@TableField(value = "CONTRACT_TEMPLATE")
	private String contractTemplate;

	@ApiModelProperty(value = "我方业务员")
	@TableField(value = "SALESMAN")
	private String salesman;

	@ApiModelProperty(value = "业务员工号")
	@TableField(value = "SALESMAN_CODE")
	private String salesmanCode;

	@ApiModelProperty(value = "合同类型")
	@TableField(value = "CONTRACT_TYPE")
	private String contractType;

	@ApiModelProperty(value = "合同状态")
	@TableField(value = "CONTRACT_STATUS")
	private String contractStatus;

	@ApiModelProperty(value = "合同开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@TableField(value = "CONTRACT_START_DATE")
	private Date contractStartDate;

	@ApiModelProperty(value = "合同到期时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@TableField(value = "CONTRACT_EXPIRE_DATE")
	private Date contractExpireDate;

	@ApiModelProperty(value = "我方签约主体")
	@TableField(value = "CONTRACT_PARTY")
	private String contractParty;

	@ApiModelProperty(value = "合同正文")
	@TableField(value = "CONTRACT_TEXT")
	private String contractText;

	@ApiModelProperty(value = "合同附件")
	@TableField(value = "CONTRACT_APPENDIX")
	private String contractAppendix;
}

package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.BaseEntity;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "ESHOP_CONTRACT_CONTRACT")
@Data
@ApiModel(value = "eshop合同信息", description = "eshop合同信息")
public class Contract extends BaseEntity implements Serializable {

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


	@ApiModelProperty(value = "门店id")
	@TableField(value = "STORE_FRONT_ID")
	private Long storefrontId;

	@ApiModelProperty(value = "统一社会信用代码")
	@TableField(value = "UNIFIED_SOCIAL_CREDIT_CODE")
	private String unifiedSocialCreditCode;

	@ApiModelProperty(value = "合同场景")
	@TableField(value = "CONTRACT_SCENE")
	private String contractScene;


	@ApiModelProperty(value = "门店等级")
	@TableField(value = "STORE_GRADE")
	private String storeGrade;

	@ApiModelProperty(value = "门店地址")
	@TableField(value = "STORE_ADDRESS")
	private String storeAddress;

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

	@ApiModelProperty(value = "合同分类")
	@TableField(value = "CONTRACT_TYPE")
	private Integer contractType;

	@ApiModelProperty(value = "合同状态")
	@TableField(value = "CONTRACT_STATUS")
	private Integer contractStatus;

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

	@ApiModelProperty(value = "流程实例ID")
	@TableField(value = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	/**
	 * 当前审批阶段（字典id）
	 */
	@ApiModelProperty(value = "当前审批阶段")
	@TableField(value = "CURR_APPROVEPHASE")
	private String currApprovePhase;

	@ApiModelProperty(value = "原合同Id")
	@TableField(value = "ORIGIN_CONTRACT_ID")
	private Long originContractId;
}

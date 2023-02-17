package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;

@TableName(value = "ESHOP_CONTRACT_RELATED_PERSONNEL")
@Data
@ApiModel(value = "合同相关人员", description = "合同相关人员")
public class ContractRelatePersonnel extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "合同表ID")
	@TableField(value = "CONTRACT_ID")
	private Long contractId;

	@ApiModelProperty(value = "人员姓名")
	@TableField(value = "PERSONNEL_NAME")
	private String personnelName;

	@ApiModelProperty(value = "人员联系方式")
	@TableField(value = "PERSONNEL_CONTACT")
	private String personnelContact;

	@ApiModelProperty(value = "人员邮箱")
	@TableField(value = "PERSONNEL_EMAIL")
	private String personnelEmail;

	@ApiModelProperty(value = "人员类型")
	@TableField(value = "PERSONNEL_TYPE")
	private String personnelType;

	@ApiModelProperty(value = "人员职务")
	@TableField(value = "PERSONNEL_JOB")
	private String personnelJob;
}

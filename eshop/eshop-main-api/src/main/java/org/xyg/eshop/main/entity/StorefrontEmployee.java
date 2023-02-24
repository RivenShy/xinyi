package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("eshop_storefront_employee")
@ApiModel(value = "易车-门店员工",description = "易车-门店员工")
public class StorefrontEmployee extends DBEntity implements Serializable {

	public static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "员工编号")
	private String empno;

	@ApiModelProperty(value = "员工姓名")
	private String empName;

	@ApiModelProperty(value = "联系方式")
	private String mobiletel;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "职务")
	private String fdLevel;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

	@ApiModelProperty(value = "门店id")
	private Long storefrontId;

	@ApiModelProperty(value = "入职日期")
	private Date joinDate;

	@ApiModelProperty(value = "离职日期")
	private Date outDate;

	@ApiModelProperty(value = "地址")
	private String address;

	@ApiModelProperty(value = "安装资质(0:无,1:有)")
	private String qualifications;

	@ApiModelProperty(value = "资质证书附件")
	private String certificate;

}

package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.StorefrontEmployee;

import java.io.Serializable;

@Data
@ApiModel(value = "易车-门店员工VO",description = "易车-门店员工VO")
public class StorefrontEmployeeVO extends StorefrontEmployee implements Serializable {

	public static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "部门名称")
	private String deptName;

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "状态名称")
	private String statusName;

}

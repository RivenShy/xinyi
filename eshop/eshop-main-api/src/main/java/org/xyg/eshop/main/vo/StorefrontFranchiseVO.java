package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xyg.eshop.main.entity.StorefrontFranchise;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "易车-加盟店vo对象", description = "易车-加盟店vo对象")
public class StorefrontFranchiseVO extends StorefrontFranchise implements Serializable {

	private static final long serialVersionUID = -7887027687600820631L;

	@ApiModelProperty(value = "字典名称")
	private String statusName;

	@ApiModelProperty(value = "加盟标识名称")
	private String franchiseFlagName;

}

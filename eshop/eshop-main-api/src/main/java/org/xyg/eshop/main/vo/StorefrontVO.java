package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.Storefront;

@Data
public class StorefrontVO extends Storefront {

	@ApiModelProperty(value = "状态搜索条件")
	private String statusSearch;

	@ApiModelProperty(value = "状态名称")
	private String statusName;

	@ApiModelProperty(value = "门店等级名称")
	private String storefrontLevelName;

}

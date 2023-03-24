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

	@ApiModelProperty(value = "业务实体名称")
	private String orgName;

	@ApiModelProperty(value = "门店类型名称")
	private String typeName;

	@ApiModelProperty(value = "国家名称")
	private String countryName;

	@ApiModelProperty(value = "省/市/区名称")
	private String pcaName;

}

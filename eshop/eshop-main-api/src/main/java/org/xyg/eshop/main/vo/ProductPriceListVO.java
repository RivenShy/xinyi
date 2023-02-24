package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.ProductPriceList;

@Data
@ApiModel(value = "易车-产品价目表VO对象", description = "易车-产品价目表VO对象")
public class ProductPriceListVO extends ProductPriceList {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "内外销名称")
	private String typeName;

	@ApiModelProperty(value = "申请人名称")
	private String createdByName;

	@ApiModelProperty(value = "状态名称")
	private String statusName;

	@ApiModelProperty(value = "区域名称")
	private String regionName;

}

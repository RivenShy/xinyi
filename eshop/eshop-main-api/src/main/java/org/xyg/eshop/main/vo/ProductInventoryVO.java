package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.ProductInventory;

@Data
@ApiModel(value = "产品库存VO")
public class ProductInventoryVO extends ProductInventory {

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "状态搜索条件")
	private String statusCondition;

}

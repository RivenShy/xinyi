package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.InventoryAlert;

@Data
@ApiModel(value = "库存预警VO对象",description = "库存预警VO对象")
public class InventoryAlertVO extends InventoryAlert {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

}

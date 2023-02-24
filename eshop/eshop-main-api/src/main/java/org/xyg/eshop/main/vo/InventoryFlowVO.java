package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.InventoryFlow;

import java.io.Serializable;

@Data
@ApiModel(value = "库存流水VO对象",description = "库存流水VO对象")
public class InventoryFlowVO extends InventoryFlow implements Serializable {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "起始日期")
	private String startDate;

	@ApiModelProperty(value = "截止日期")
	private String endDate;

}

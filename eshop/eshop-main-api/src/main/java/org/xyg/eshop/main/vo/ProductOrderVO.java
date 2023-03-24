package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.ProductOrder;
import org.xyg.eshop.main.entity.ProductOrderLines;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "订单VO对象", description = "订单VO对象")
public class ProductOrderVO extends ProductOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "订单行表信息")
	private List<ProductOrderLines> linesList;

	@ApiModelProperty(value = "状态搜索条件")
	private String statusCondition;

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "起始日期")
	private String startDate;

	@ApiModelProperty(value = "截止日期")
	private String endDate;

	@ApiModelProperty(value = "状态名称")
	private String statusName;

	@ApiModelProperty(value = "访问是否满意名称")
	private String visitWhetherSatisfiedName;

	@ApiModelProperty(value = "访问状态名称")
	private String visitStatusName;

}

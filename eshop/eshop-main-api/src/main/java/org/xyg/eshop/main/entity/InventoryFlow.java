package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("eshop_inventory_flow")
@ApiModel(value = "库存流水",description = "库存流水")
public class InventoryFlow extends DBEntity implements Serializable {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "流水单号")
	private String docnum;

	@ApiModelProperty(value = "单据类型")
	private String docnumType;

	@ApiModelProperty(value = "门店id")
	private Long storefrontId;

	@ApiModelProperty(value = "产品名称")
	private String productName;

	@ApiModelProperty(value = "产品型号")
	private String productMode;

	@ApiModelProperty(value = "入库")
	private Long inbound;

	@ApiModelProperty(value = "出库")
	private Long outbound;

	@ApiModelProperty(value = "剩余库存")
	private Long remainingInventory;

	@ApiModelProperty(value = "门店价格")
	private BigDecimal storefrontPrice;

}

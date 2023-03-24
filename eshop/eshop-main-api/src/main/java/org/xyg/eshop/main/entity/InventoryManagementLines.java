package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("eshop_inventory_management_lines")
@ApiModel(value = "易车库存管理行表",description = "易车库存管理行表")
public class InventoryManagementLines extends DBEntity implements Serializable {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "头表id")
	private Long headId;

	@ApiModelProperty(value = "产品名称")
	private String productName;

	@ApiModelProperty(value = "产品型号")
	private String productMode;

	@ApiModelProperty(value = "单位")
	private String units;

	@ApiModelProperty(value = "数量")
	private Long quantity;

	@ApiModelProperty(value = "批次号")
	private String batchNo;

	@ApiModelProperty(value = "货位")
	private String location;

	@ApiModelProperty(value = "门店价格")
	private BigDecimal storefrontPrice;

	@ApiModelProperty(value = "总金额")
	private Long totalPrice;

	@ApiModelProperty(value = "可用库存")
	private BigDecimal availableInventory;

	@ApiModelProperty(value = "规格")
	private String specifications;

	@ApiModelProperty(value = "预计数量")
	private BigDecimal scheduledQuantity;

	@ApiModelProperty(value = "申请数量")
	private BigDecimal applicationQuantity;

	@ApiModelProperty(value = "价格小计")
	private BigDecimal subtotalPrice;

	@ApiModelProperty(value = "调入方日均销售量")
	private BigDecimal callInDayAvgSales;

	@ApiModelProperty(value = "调入方剩余库存")
	private BigDecimal callInRemainingInventory;

	@ApiModelProperty(value = "调出方可用库存")
	private BigDecimal callOutAvailableInventory;

}

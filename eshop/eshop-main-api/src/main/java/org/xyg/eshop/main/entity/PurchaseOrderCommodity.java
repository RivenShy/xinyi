package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value = "ESHOP_PURCHASE_ORDER_COMMODITY")
@Data
@ApiModel(value = "采购订单商品信息", description = "采购订单商品信息")
public class PurchaseOrderCommodity extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "采购订单表ID")
	@TableField(value = "PURCHASE_ORDER_ID")
	private Long purchaseOrderId;

	@ApiModelProperty(value = "商品ID")
	@TableField(value = "COMMODITY_ID")
	private Long commodityId;

	@ApiModelProperty(value = "商品名称")
	@TableField(value = "COMMODITY_NAME")
	private String commodityName;

	@ApiModelProperty(value = "商品编码")
	@TableField(value = "COMMODITY_CODE")
	private String commodityCode;

	@ApiModelProperty(value = "商品规格")
	@TableField(value = "COMMODITY_SPECIFICATION")
	private String commoditySpecification;

	@ApiModelProperty(value = "库存单位")
	@TableField(value = "INVENTORY_UNIT")
	private String inventoryUnit;

	@ApiModelProperty(value = "采购数量")
	@TableField(value = "PURCHASE_QUANTITY")
	private Integer purchaseQuantity;

	@ApiModelProperty(value = "可用库存")
	@TableField(value = "AVAILABLE_INVENTORY")
	private Integer availableInventory;

	@ApiModelProperty(value = "月均销量")
	@TableField(value = "AVERAGE_MONTH_SALE")
	private BigDecimal averageMonthSale;

	@ApiModelProperty(value = "采购单位")
	@TableField(value = "PURCHASE_UNIT")
	private String purchaseUnit;

	@ApiModelProperty(value = "采购单价")
	@TableField(value = "PURCHASE_UNIT_PRICE")
	private BigDecimal purchaseUnitPrice;
	@ApiModelProperty(value = "采购总价")
	@TableField(value = "TOTAL_PURCHASE_PRICE")
	private BigDecimal totalPurchasePrice;
	@ApiModelProperty(value = "上一次采购单价")
	@TableField(value = "LAST_PURCHASE_UNIT_PRICE")
	private BigDecimal lastPurchaseUnitPrice;
}

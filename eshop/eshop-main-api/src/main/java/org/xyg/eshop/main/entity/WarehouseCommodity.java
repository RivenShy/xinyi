package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;

@TableName(value = "ESHOP_WAREHOUSE_COMMODITY")
@Data
@ApiModel(value = "采购入库商品信息", description = "采购入库商品信息")
public class WarehouseCommodity extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "采购入库单ID")
	@TableField(value = "PURCHASE_WAREHOUSE_ID")
	private Long purchaseWarehouseId;

	@ApiModelProperty(value = "商品ID")
	@TableField(value = "COMMODITY_ID")
	private Long commodityId;

	@ApiModelProperty(value = "入库方式")
	@TableField(value = "WAREHOUSE_MODE")
	private String warehouseMode;

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

	@ApiModelProperty(value = "申请采购数量")
	@TableField(value = "PURCHASE_QUANTITY")
	private Integer purchaseQuantity;

	@ApiModelProperty(value = "预计入库数量")
	@TableField(value = "ESTIMATE_WAREHOUSE_QUANTITY")
	private Integer estimateWarehouseQuantity;

	@ApiModelProperty(value = "入库批次号")
	@TableField(value = "WAREHOUSE_BATCH_NUMBER")
	private String warehouseBatchNumber;

	@ApiModelProperty(value = "实际入库量")
	@TableField(value = "ACTUAL_WAREHOUSE_QUANTITY")
	private Integer actualWarehouseQuantity;
}

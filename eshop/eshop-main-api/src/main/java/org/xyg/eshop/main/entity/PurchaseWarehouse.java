package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.util.Date;


@TableName(value = "ESHOP_PURCHASE_WAREHOUSE")
@Data
@ApiModel(value = "采购入库单信息", description = "采购入库单信息")
public class PurchaseWarehouse extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "采购订单ID")
	@TableField(value = "PURCHASE_ORDER_ID")
	private Long purchaseOrderId;

	@ApiModelProperty(value = "采购订单号")
	@TableField(value = "PURCHASE_ORDER_NUMBER")
	private String purchaseOrderNumber;

	@ApiModelProperty(value = "单据类型")
	@TableField(value = "BILL_TYPE")
	private String billType;

	@ApiModelProperty(value = "采购订单日期")
	@TableField(value = "PURCHASE_ORDER_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date purchaseOrderDate;

	@ApiModelProperty(value = "仓库/门店名称")
	@TableField(value = "STORE_NAME")
	private String storeName;

	@ApiModelProperty(value = "关联销售订单")
	@TableField(value = "ASSOCIATE_SALE_ORDER")
	private String associateSaleOrder;

	@ApiModelProperty(value = "入库日期")
	@TableField(value = "WAREHOUSE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date warehouseDate;

	@ApiModelProperty(value = "备注")
	@TableField(value = "REMARK")
	private String remark;

	@ApiModelProperty(value = "附件")
	@TableField(value = "APPENDIX")
	private String appendix;
}

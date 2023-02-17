package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "ESHOP_PURCHASE_ORDER")
@Data
@ApiModel(value = "采购订单信息", description = "采购订单信息")
public class PurchaseOrder extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否已删除 0默认未删除 1删除
	 */
	@TableLogic
	@TableField(value = "IS_DELETED")
	private Integer isDeleted;

	@ApiModelProperty(value = "采购订单号")
	@TableField(value = "PURCHASE_ORDER_NUMBER")
	private String purchaseOrderNumber;

	@ApiModelProperty(value = "仓库/门店名称")
	@TableField(value = "STORE_NAME")
	private String storeName;

	@ApiModelProperty(value = "采购总金额")
	@TableField(value = "PURCHASE_TOTAL_AMOUNT")
	private BigDecimal purchaseTotalAmount;

	@ApiModelProperty(value = "采购总数量")
	@TableField(value = "PURCHASE_TOTAL_QUANTITY")
	private Integer purchaseTotalQuantity;

	@ApiModelProperty(value = "供应商")
	@TableField(value = "PURCHASE_SUPPLIER")
	private String purchaseSupplier;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@ApiModelProperty(value = "订单日期")
	@TableField(value = "PURCHASE_ORDER_DATE")
	private Date purchaseOrderDate;

	@ApiModelProperty(value = "申请人")
	@TableField(value = "PURCHASE_APPLICANT")
	private String purchaseApplicant;

	@ApiModelProperty(value = "订单状态")
	@TableField(value = "PURCHASE_STATUS")
	private String purchaseStatus;
}

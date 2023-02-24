package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.BaseEntity;
import org.springrabbit.core.mp.base.DBEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "ESHOP_PURCHASE_ORDER")
@Data
@ApiModel(value = "采购订单信息", description = "采购订单信息")
public class PurchaseOrder extends BaseEntity implements Serializable {

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
	private Integer purchaseStatus;

	@ApiModelProperty(value = "流程实例ID")
	@TableField(value = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	@ApiModelProperty(value = "当前审批阶段")
	@TableField(value = "CURR_APPROVEPHASE")
	private String currApprovePhase;

	@ApiModelProperty(value = "期望到货日期")
	@TableField(value = "EXPECT_ARRIVAL_DATE")
	private Date expectArrivalDate;

	@ApiModelProperty(value = "仓库/门店编码")
	@TableField(value = "STORE_CODE")
	private String storeCode;

	@ApiModelProperty(value = "关联销售订单")
	@TableField(value = "ASSOCIATE_SALE_ORDER")
	private Long associateSaleOrder;

	@ApiModelProperty(value = "备注")
	@TableField(value = "REMARK")
	private String remark;

	@ApiModelProperty(value = "结算方式")
	@TableField(value = "SETTLEMENT_METHOD")
	private String settlementMethod;
}

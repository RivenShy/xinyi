package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("eshop_inventory_management")
@ApiModel(value = "出入库管理",description = "出入库管理")
public class InventoryManagement extends DBEntity implements Serializable {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "单号")
	private String docnum;

	@ApiModelProperty(value = "门店id")
	private Long storefrontId;

	@ApiModelProperty(value = "单据类型")
	private String documentType;

	@ApiModelProperty(value = "凭证")
	private String voucher;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "原因")
	private String reason;

	@ApiModelProperty(value = "期望送达日期")
	private Date expectedDeliveryDate;

}

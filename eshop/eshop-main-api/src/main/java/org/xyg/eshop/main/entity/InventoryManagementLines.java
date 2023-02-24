package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;

@Data
@TableName("eshop_inventory_management_lines")
@ApiModel(value = "易车库存管理行表",description = "易车库存管理行表")
public class InventoryManagementLines extends DBEntity implements Serializable {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "产品名称")
	private String productName;

	@ApiModelProperty(value = "产品型号")
	private String productMode;

	@ApiModelProperty(value = "单位")
	private String units;

	@ApiModelProperty(value = "入库数量")
	private Long inboundQuantity;

	@ApiModelProperty(value = "入库批次号")
	private String inboundBatchNo;

	@ApiModelProperty(value = "货位")
	private String location;

	@ApiModelProperty(value = "门店价格")
	private Long storefrontPrice;

	@ApiModelProperty(value = "入库总金额")
	private Long inboundTotalPrice;

	@ApiModelProperty(value = "头表id")
	private Long headId;

}

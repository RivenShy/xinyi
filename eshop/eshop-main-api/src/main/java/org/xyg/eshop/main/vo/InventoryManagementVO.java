package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.InventoryManagement;
import org.xyg.eshop.main.entity.InventoryManagementLines;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "出入库管理VO对象",description = "出入库管理VO对象")
public class InventoryManagementVO extends InventoryManagement implements Serializable {

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "开始日期")
	private String startDate;

	@ApiModelProperty(value = "结束日期")
	private String endDate;

	@ApiModelProperty(value = "状态搜索条件")
	private String statusCondition;

	@ApiModelProperty(value = "申请人名称")
	private String createdByName;

	@ApiModelProperty(value = "单据类型名称")
	private String documentTypeName;

	@ApiModelProperty(value = "状态名称")
	private String statusName;

	@ApiModelProperty(value = "库存管理行表")
	private List<InventoryManagementLines> linesList;

}

package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.ProductAdjustmentRecord;
import org.xyg.eshop.main.entity.ProductAdjustmentRecordLines;

import java.util.List;

@Data
@ApiModel(value = "易车-产品价目表调整记录VO对象", description = "易车-产品价目表调整记录VO对象")
public class ProductAdjustmentRecordVO extends ProductAdjustmentRecord {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "内外销名称")
	private String typeName;

	@ApiModelProperty(value = "区域名称")
	private String regionName;

	@ApiModelProperty(value = "状态名称")
	private String statusName;

	@ApiModelProperty(value = "申请人名称")
	private String createdByName;

	@ApiModelProperty(value = "erp状态名称")
	private String erpStatusName;

	@ApiModelProperty(value = "调整单行表")
	private List<ProductAdjustmentRecordLines> productAdjustmentRecordLinesList;

	@ApiModelProperty(value = "价目表行表id,多个逗号分隔")
	private String priceListLineIds;

}

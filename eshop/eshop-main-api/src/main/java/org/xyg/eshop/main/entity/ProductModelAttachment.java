package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("eshop_product_model_attachment")
@ApiModel(value = "易车附件信息维护表", description = "易车附件信息维护表")
public class ProductModelAttachment extends DBEntity implements Serializable {
	@ApiModelProperty(value = "头表Id")
	private Long headerId;
	@ApiModelProperty(value = "本厂型号")
	private String factoryMode;
	@ApiModelProperty(value = "附件类型")
	private String attachmentType;
	@ApiModelProperty(value = "组件编号")
	private String componentNumber;
	@ApiModelProperty(value = "组件说明")
	private String componentDescription;
	@ApiModelProperty(value = "用量")
	private Double amount;
	@ApiModelProperty(value = "单位")
	private String units;
	@ApiModelProperty(value = "重量")
	private String weight;
	@ApiModelProperty(value = "有效日期")
	private Date effectiveDate;
	@ApiModelProperty(value = "失效日期")
	private Date expiryDate;

	/**
	 * 库存组织id
	 */
	@ApiModelProperty(value = "库存组织id")
	private Long organizationId;
	/**
	 * 技术资料id
	 */
	@ApiModelProperty(value = "技术资料id")
	private Long technologyId;

	@ApiModelProperty(value = "重量单位")
	private String weightUnits;

	@ApiModelProperty(value = "附件id")
	private Long componentSequenceId;
	@ApiModelProperty(value = "附件价格")
	private BigDecimal attachmentPrice;
	@ApiModelProperty(value = "物料ID")
	private Long materialId;
	@ApiModelProperty(value = "币种")
	private String currencyCode;
}

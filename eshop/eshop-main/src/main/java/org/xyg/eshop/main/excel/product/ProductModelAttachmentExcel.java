package org.xyg.eshop.main.excel.product;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProductModelAttachmentExcel {

	@ApiModelProperty(value = "头表Id")
	@ExcelProperty("HEADER_ID")
	private Long headerId;
	@ApiModelProperty(value = "本厂型号")
	@ExcelProperty("FACTORY_MODE")
	private String factoryMode;
	@ApiModelProperty(value = "附件类型")
	@ExcelProperty("ATTACHMENT_TYPE")
	private String attachmentType;
	@ApiModelProperty(value = "组件编号")
	@ExcelProperty("COMPONENT_NUMBER")
	private String componentNumber;
	@ApiModelProperty(value = "组件说明")
	@ExcelProperty("COMPONENT_DESCRIPTION")
	private String componentDescription;
	@ApiModelProperty(value = "用量")
	@ExcelProperty("AMOUNT")
	private Long amount;
	@ApiModelProperty(value = "单位")
	@ExcelProperty("UNITS")
	private String units;
	@ApiModelProperty(value = "重量")
	@ExcelProperty("WEIGHT")
	private String weight;
	@ApiModelProperty(value = "有效日期")
	@ExcelProperty("EFFECTIVE_DATE")
	private Date effectiveDate;
	@ApiModelProperty(value = "失效日期")
	@ExcelProperty("EXPIRY_DATE")
	private Date expiryDate;
}

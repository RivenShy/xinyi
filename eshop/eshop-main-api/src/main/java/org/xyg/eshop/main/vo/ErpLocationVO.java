package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "国家省市区")
@Data
public class ErpLocationVO {
	@ApiModelProperty(value = "国家编码")
	private String countryCode;

	@ApiModelProperty(value = "国家名称")
	private String countryName;

	@ApiModelProperty(value = "省编码")
	private String provinceCode;

	@ApiModelProperty(value = "省名称")
	private String provinceName;

	@ApiModelProperty(value = "市编码")
	private String cityCode;

	@ApiModelProperty(value = "市名称")
	private String cityName;

	@ApiModelProperty(value = "区编码")
	private String areaCode;

	@ApiModelProperty(value = "区名称")
	private String areaName;
}

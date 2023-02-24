package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.ProductModelLines;

@Data
public class ProductModelLinesToPrictListVO extends ProductModelLines {
	@ApiModelProperty(value = "车厂")
	private String carFactory;

	@ApiModelProperty(value = "车型")
	private String model;

	@ApiModelProperty(value = "整车图")
	private String productCarModelAttachment;

	@ApiModelProperty(value = "玻璃图")
	private String glassDiagram;

	@ApiModelProperty(value = "年款")
	private String modelYear;
}

package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.ProductModelLines;
import org.xyg.eshop.main.entity.TemplateRelation;

import java.util.List;

@Data
public class ProductModelLinesVO extends ProductModelLines {

	@ApiModelProperty(value = "模板数据关联集合")
	private List<TemplateRelation> relationList;

}

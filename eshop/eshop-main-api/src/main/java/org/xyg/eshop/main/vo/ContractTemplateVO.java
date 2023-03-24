package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xyg.eshop.main.entity.ContractTemplate;
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTemplateVO extends ContractTemplate {
	private static final long serialVersionUID = -6586605807343218392L;


	@ApiModelProperty(value = "合同模板类型")
	private String contractTemplateType;
}

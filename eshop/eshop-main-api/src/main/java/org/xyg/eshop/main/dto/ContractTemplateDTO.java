package org.xyg.eshop.main.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xyg.eshop.main.entity.ContractTemplate;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTemplateDTO extends ContractTemplate {
	private static final long serialVersionUID = -6586605807343218392L;
}

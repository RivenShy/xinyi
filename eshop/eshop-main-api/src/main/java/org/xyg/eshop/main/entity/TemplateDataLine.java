package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;

@Data
@TableName("eshop_template_data_line")
@ApiModel(value = "易车-模板数据表",description = "易车-模板数据表")
public class TemplateDataLine extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "字段表id")
	private Long proptiesId;

	@ApiModelProperty(value = "数据值")
	private String dataValue;

	@ApiModelProperty(value = "数据键")
	private Integer dataKey;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "数据英文名称")
	private String dataValueEn;
}

package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("eshop_template_propties_line")
@ApiModel(value = "易车-模板字段表",description = "易车-模板字段表")
public class TemplateProptiesLine extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "模板id")
	private Long templateId;

	@ApiModelProperty(value = "大纲")
	private String outline;

	@ApiModelProperty(value = "属性名称")
	private String proptiesName;

	@ApiModelProperty(value = "填写类型 0 填写， 1 单选框，2 复选框，3 下拉框，4 文件上传，5 文本框")
	private Integer type;

	@ApiModelProperty(value = "语言 US、ZHS")
	private String language;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "属性英文名称")
	private String proptiesNameEn;

	@ApiModelProperty(value = "字段名称")
	private String columnName;

	@ApiModelProperty(value = "模板数据集合")
	@TableField(exist = false)
	private List<TemplateDataLine> dataLineList;

}

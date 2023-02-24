package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@TableName("eshop_template")
@ApiModel(value = "易车-产品库模板",description = "易车-产品库模板")
public class Template extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "模板名称")
	private String templateName;

	@ApiModelProperty(value = "中文模板标题")
	private String titleZh;

	@ApiModelProperty(value = "英文模板标题")
	private String titleEn;

	@ApiModelProperty(value = "页面地址")
	private String url;

	@ApiModelProperty(value = "模板字段集合")
	@TableField(exist = false)
	private Map<String, List<TemplateProptiesLine>> proptiesLineMap;

}

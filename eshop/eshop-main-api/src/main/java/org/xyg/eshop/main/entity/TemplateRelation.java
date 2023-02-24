package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;

@Data
@TableName("eshop_template_relation")
@ApiModel(value = "易车模板数据关联表",description = "易车模板数据关联表")
public class TemplateRelation extends DBEntity implements Serializable {

	public static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "头id")
	private Long headId;

	@ApiModelProperty(value = "字段id")
	private Long proptiesId;

	@ApiModelProperty(value = "字段数据id")
	private Long dataId;

	@ApiModelProperty(value = "字段值")
	private String proptiesValue;

	@ApiModelProperty(value = "其他值")
	private String other;

}

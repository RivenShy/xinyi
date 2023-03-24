package org.xyg.eshop.main.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.BaseEntity;
import java.io.Serializable;
import java.util.Date;


@TableName(value = "ESHOP_ACTIVITY_ACTIVITY")
@Data
@ApiModel(value = "eshop活动信息", description = "eshop活动信息")
public class Activity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "活动名称")
	@TableField(value = "ACTIVITY_NAME")
	private String activityName;

	@ApiModelProperty(value = "活动范围")
	@TableField(value = "ACTIVITY_SCOPE")
	private String activityScope;

	@ApiModelProperty(value = "活动类型")
	@TableField(value = "ACTIVITY_TYPE")
	private String activityType;

	@ApiModelProperty(value = "活动开始时间")
	@TableField(value = "ACTIVITY_START_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date activityStartDate;

	@ApiModelProperty(value = "活动结束时间")
	@TableField(value = "ACTIVITY_END_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date activityEndDate;

	@ApiModelProperty(value = "活动描述")
	@TableField(value = "ACTIVITY_DESCRIPTION")
	private String activityDescription;

	@ApiModelProperty(value = "活动商品")
	@TableField(value = "ACTIVITY_COMMODITY")
	private String activityCommodity;
}

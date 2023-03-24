package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.BaseEntity;
import java.io.Serializable;
import java.util.Date;

@TableName(value = "ESHOP_ACTIVITY_STORE")
@Data
@ApiModel(value = "eshop活动门店信息", description = "eshop活动门店信息")
public class ActivityStore extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "活动ID")
	@TableField(value = "ACTIVITY_ID")
	private Long activityId;

	@ApiModelProperty(value = "门店ID")
	@TableField(value = "STORE_FRONT_ID")
	private Long storefrontId;

	@ApiModelProperty(value = "图片上传日期")
	@TableField(value = "IMAGE_UPLOAD_DATE")
	private Date imageUploadDate;

	@ApiModelProperty(value = "图片上传人ID")
	@TableField(value = "IMAGE_UPLOAD_USER_ID")
	private Long imageUploadUserId;

	@ApiModelProperty(value = "图片详情")
	@TableField(value = "IMAGE_DETAIL")
	private String imageDetail;

	@ApiModelProperty(value = "图片是否合格")
	@TableField(value = "IMAGE_IS_QUALIFIED")
	private String imageIsQualified;

	@ApiModelProperty(value = "审核人ID")
	@TableField(value = "REVIEWER_ID")
	private String reviewerId;
}

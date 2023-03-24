package org.xyg.eshop.main.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.ActivityStore;

import java.io.Serializable;

@Data
public class ActivityStoreVO extends ActivityStore implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "审核人名称")
	private String reviewerName;

	@ApiModelProperty(value = "图片上传人名称")
	private String imageUploadUserName;

	@ApiModelProperty(value = "图片是否合格名称")
	private String imageIsQualifiedName;

	@ApiModelProperty(value = "活动名称")
	private String activityName;

	@ApiModelProperty(value = "参与门店名称")
	private String storefrontName;
}

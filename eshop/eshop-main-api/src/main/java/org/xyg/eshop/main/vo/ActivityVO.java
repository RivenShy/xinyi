package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.Activity;
import org.xyg.eshop.main.entity.ActivityCommodity;

import java.io.Serializable;
import java.util.List;

@Data
public class ActivityVO extends Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "活动商品集合")
	List<ActivityCommodityVO> activityCommodityList;

	@ApiModelProperty(value = "活动参与门店数量")
	private Integer storeJoinNumber;

	@ApiModelProperty(value = "参与总次数")
	private Integer joinTotalNumber;

	@ApiModelProperty(value = "活动门店集合")
	List<ActivityStoreVO> activityStoreList;

	@ApiModelProperty(value = "活动状态")
	private String statusName;

	@ApiModelProperty(value = "活动类型名称")
	private String activityTypeName;

	@ApiModelProperty(value = "活动范围名称")
	private String activityScopeName;
}

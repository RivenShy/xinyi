package org.xyg.eshop.main.dto;

import lombok.Data;
import org.xyg.eshop.main.entity.Activity;
import org.xyg.eshop.main.entity.ActivityCommodity;

import java.io.Serializable;
import java.util.List;

@Data
public class ActivityDTO extends Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ActivityCommodity> activityCommodityList;

	private List<Integer> activityStatusList;
}

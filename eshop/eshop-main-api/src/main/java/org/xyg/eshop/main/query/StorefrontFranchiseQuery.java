package org.xyg.eshop.main.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "加盟店查询条件对象", description = "加盟店查询条件对象")
public class StorefrontFranchiseQuery implements Serializable {

	private static final long serialVersionUID = 9043400569094162106L;

	@ApiModelProperty(value = "申请单号")
	private String franchiseNo;
	@ApiModelProperty(value = "门店名称")
	private String storefrontName;
	@ApiModelProperty(value = "申请日期起")
	private Date startApplyDate;
	@ApiModelProperty(value = "申请日期止")
	private Date endApplyDate;
	@ApiModelProperty(value = "门店性质")
	private String type;
	@ApiModelProperty(value = "数据状态")
	private List<Integer> status;

}

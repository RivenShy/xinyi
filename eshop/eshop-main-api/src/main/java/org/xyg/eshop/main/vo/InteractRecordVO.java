package org.xyg.eshop.main.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.InteractRecord;

import java.io.Serializable;

@Data
@TableName("yc_interact_record")
@ApiModel(value = "易车互动记录",description = "易车互动记录")
public class InteractRecordVO extends InteractRecord implements Serializable {

	public static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "业务员名称")
	private String salesmanName;

	@ApiModelProperty("开始日期搜索条件")
	private String startDateSearch;

	@ApiModelProperty("结束日期搜索条件")
	private String endDateSearch;

	@ApiModelProperty("门店类型")
	private Integer storefrontType;

	@ApiModelProperty("门店类型名称")
	private String storefrontTypeName;

	@ApiModelProperty("门店等级")
	private Integer storefrontLevel;

	@ApiModelProperty("门店等级名称")
	private String storefrontLevelName;

}

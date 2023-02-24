package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("eshop_interact_record")
@ApiModel(value = "易车互动记录",description = "易车互动记录")
public class InteractRecord extends DBEntity implements Serializable {

	public static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "门店id")
	private Long storefrontId;

	@ApiModelProperty(value = "业务员")
	private String salesman;

	@ApiModelProperty(value = "沟通日期")
	private Date startDate;

	@ApiModelProperty(value = "地点")
	private String address;

	@ApiModelProperty(value = "沟通对象")
	private String title;

	@ApiModelProperty(value = "内容")
	private String content;

	@ApiModelProperty(value = "备注")
	private String remark;

}

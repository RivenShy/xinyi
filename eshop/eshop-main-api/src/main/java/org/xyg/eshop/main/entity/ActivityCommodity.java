package org.xyg.eshop.main.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value = "ESHOP_ACTIVITY_COMMODITY")
@Data
@ApiModel(value = "eshop活动商品信息", description = "eshop活动商品信息")
public class ActivityCommodity extends DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "活动ID")
	@TableField(value = "ACTIVITY_ID")
	private Long activityId;

	@ApiModelProperty(value = "商品ID")
	@TableField(value = "COMMODITY_ID")
	private Long commodityId;

	@ApiModelProperty(value = "商品名称")
	@TableField(value = "COMMODITY_NAME")
	private String commodityName;

	@ApiModelProperty(value = "本厂型号")
	@TableField(value = "FACTORY_MODEL")
	private String factoryModel;

	@ApiModelProperty(value = "商品规格")
	@TableField(value = "COMMODITY_SPECIFICATION")
	private String commoditySpecification;

	@ApiModelProperty(value = "建议零售价")
	@TableField(value = "RECOMMEND_RETAIL_PRICE")
	private BigDecimal recommendRetailPrice;
}

package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;

@Data
@TableName("eshop_product_inventory")
@ApiModel(value = "产品库存表", description = "产品库存表")
public class ProductInventory extends DBEntity implements Serializable {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "产品名称")
	private String productName;

	@ApiModelProperty(value = "产品图片")
	private String productImage;

	@ApiModelProperty(value = "产品型号")
	private String productMode;

	@ApiModelProperty(value = "总库存")
	private Long totalInventory;

	@ApiModelProperty(value = "可用库存")
	private Long availableInventory;

	@ApiModelProperty(value = "占用库存")
	private Long occupyInventory;

	@ApiModelProperty(value = "在途库存")
	private Long transportationInventory;

	@ApiModelProperty(value = "门店id")
	private Long storefrontId;

}

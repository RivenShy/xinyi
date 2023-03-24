package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;

@Data
@TableName("ESHOP_INVENTORY_ALERT")
@ApiModel(value = "库存预警",description = "库存预警")
public class InventoryAlert extends DBEntity implements Serializable {

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

    @ApiModelProperty(value = "预警类型")
    private String alertType;

    @ApiModelProperty(value = "门店id")
    private Long storefrontId;

    @ApiModelProperty(value = "库存下限")
    private Long lowerLimitInventory;

    @ApiModelProperty(value = "库存上限")
    private Long inventoryCeiling;

}

package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("eshop_product_order_lines")
@ApiModel(value = "订单行表",description = "订单行表")
public class ProductOrderLines extends DBEntity implements Serializable {

    private static final long seriaVersionUID = 1L;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品型号")
    private String productMode;

    @ApiModelProperty(value = "规格")
    private String specifications;

    @ApiModelProperty(value = "建议价")
    private BigDecimal suggestedPrice;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "车型信息")
    private String modelInfo;

    @ApiModelProperty(value = "车牌号")
    private String carId;

    @ApiModelProperty(value = "车架号")
    private String carFrameNo;

    @ApiModelProperty(value = "平台派单价")
    private BigDecimal platformDispatchPrice;

    @ApiModelProperty("头表id")
    private Long headId;

    @ApiModelProperty("完成日期")
    private Date completionDate;

    @ApiModelProperty("空窗照")
    private String emptyWindowImage;

    @ApiModelProperty("左前照片")
    private String leftFrontImage;

    @ApiModelProperty("左后照片")
    private String leftRearImage;

    @ApiModelProperty("右前照片")
    private String rightFrontImage;

    @ApiModelProperty("右后照片")
    private String rightRearImage;

    @ApiModelProperty("信义logo照片")
    private String logoImage;

}

package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;

/**
 * 技术图纸信息表
 * @TableName eshop_product_model_adjunct
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="eshop_product_model_adjunct")
@Data
@ApiModel(value = "技术图纸信息表", description = "技术图纸信息表")
public class ProductModelAdjunct extends DBEntity implements Serializable {
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String fileTypeCode;

    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址")
    private String filePatch;

    /**
     * 图纸类型
     */
    @ApiModelProperty(value = "图纸类型")
    private String fileType;

    /**
     * 库存组织ID
     */
    @ApiModelProperty(value = "库存组织ID")
    private Long organizationId;

    /**
     * 技术资料id
     */
    @ApiModelProperty(value = "技术资料id")
    private Long technologyId;

    /**
     * 文件类型说明
     */
    @ApiModelProperty(value = "文件类型说明")
    private String fileTypeDescription;

    private static final long serialVersionUID = 1L;
}

package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;

@Data
@TableName("eshop_product_car_model")
@ApiModel(value = "产品库车厂车型维护表", description = "产品库车厂车型维护表")
public class ProductCarModel extends DBEntity implements Serializable {
	@ApiModelProperty(value = "车厂")
	private String carFactory;
	@ApiModelProperty(value = "种类")
	private String type;
	@ApiModelProperty(value = "车型")
	private String model;
	@ApiModelProperty(value = "年款")
	private String modelYear;
	@ApiModelProperty(value = "整车图")
	private String attachment;
	@ApiModelProperty(value = "整车描述")
	private String describe;
}

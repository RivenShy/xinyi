package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.ProductModelAdjunct;
import org.xyg.eshop.main.entity.ProductModelLines;
import org.xyg.eshop.main.entity.TemplateRelation;

import java.util.List;

@Data
@ApiModel(value = "产品库车型VO类" , description = "产品库车型VO类")
public class ProductCarModelVO extends ProductModelLines {

	@ApiModelProperty(value = "技术图纸信息表")
	private List<ProductModelAdjunct> productModelAdjuncts;

	@ApiModelProperty(value = "本地库存")
	private Long stock ;

	@ApiModelProperty(value = "颜色")
	private List<String> colorList ;

	@ApiModelProperty(value = "附件")
	private List<String> attachmentList ;

	@ApiModelProperty(value = "模板数据关联集合")
	private List<TemplateRelation> relationList;

	@ApiModelProperty(value = "是否可生产标识 Y 可生产、N 不可生产、 W 未分配")
	private String flag;

	@ApiModelProperty(value = "车厂")
	private String carFactory;

	@ApiModelProperty(value = "车型")
	private String model;

	@ApiModelProperty(value = "车型")
	private String easySearch;

	@ApiModelProperty(value = "产品规格开始长")
	private Integer specificationsStartLength;

	@ApiModelProperty(value = "产品规格结束长")
	private Integer specificationsEndLength;

	@ApiModelProperty(value = "产品规格开始宽")
	private Integer specificationsStartWidth;

	@ApiModelProperty(value = "产品规格结束宽")
	private Integer specificationsEndWidth;

	@ApiModelProperty(value = "中高对角开始长")
	private Integer diagonalStartLength;

	@ApiModelProperty(value = "中高对角结束长")
	private Integer diagonalEndLength;

	@ApiModelProperty(value = "中高对角开始宽")
	private Integer diagonalStartWidth;

	@ApiModelProperty(value = "中高对角结束宽")
	private Integer diagonalEndWidth;

}

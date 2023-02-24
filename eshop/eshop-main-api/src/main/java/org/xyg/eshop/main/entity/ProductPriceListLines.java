package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName(value = "eshop_product_price_list_lines")
@ApiModel(value = "易车-产品价目表行表", description = "易车-产品价目表行表")
public class ProductPriceListLines extends DBEntity implements Serializable {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "品牌")
	private String brand;

	@ApiModelProperty(value = "车型")
	private String model;

	@ApiModelProperty(value = "装车位置")
	private String position;

	@ApiModelProperty(value = "本厂型号")
	private String productModel;

	@ApiModelProperty(value = "区间大于等于")
	private String interval1;

	@ApiModelProperty(value = "区间小于")
	private String interval2;

	@ApiModelProperty(value = "基准价")
	private BigDecimal price1;

	@ApiModelProperty(value = "批量价格")
	private BigDecimal batchPrice;

	@ApiModelProperty(value = "外币")
	private String foreignCurrency;

	@ApiModelProperty(value = "片数/箱")
	private Integer quantity;

	@ApiModelProperty(value = "价格/外币")
	private BigDecimal price2;

	@ApiModelProperty(value = "整箱价/外币")
	private BigDecimal fullContainerPrice;

	@ApiModelProperty(value = "头表id")
	private Long headId;

	@ApiModelProperty(value = "物料编码")
	private String materialCode ;

	@ApiModelProperty(value = "产品名称(中文说明)")
	private String itemChDescription;

	@ApiModelProperty(value = "规格")
	private String specifications;

	@ApiModelProperty(value = "中高对角")
	private String diagonal;

	@ApiModelProperty(value = "产品种类")
	private Integer productType;

	@ApiModelProperty(value = "加工种类")
	private Integer machiningType;

	@ApiModelProperty(value = "装箱标准(木箱数量)")
	private String packingQuantity1;

	@ApiModelProperty(value = "OEM型号")
	private String oemModel;

	@ApiModelProperty(value = "宽度")
	private Long width;

	@ApiModelProperty(value = "长度")
	private Long length;

	@ApiModelProperty(value = "出厂价")
	private BigDecimal basePrice;

	@ApiModelProperty(value = "折扣点")
	private BigDecimal rebatePoint;

	@ApiModelProperty(value = "业务实体")
	private Integer orgId;

	@ApiModelProperty(value = "建议价")
	private BigDecimal suggestedPrice;

	@ApiModelProperty(value = "结算价")
	private BigDecimal settlementPrice;

	@ApiModelProperty(value = "毛利率")
	private BigDecimal grossRate;

	@ApiModelProperty(value = "预估利润")
	private BigDecimal estimatedProfit;

	@ApiModelProperty(value = "我司预估利润")
	private BigDecimal companyEstimatedProfit;

	@ApiModelProperty(value = "平台预估利润")
	private BigDecimal platformEstimatedProfit;

	@ApiModelProperty(value = "平台入库价")
	private BigDecimal platformReceiptPrice;

	@ApiModelProperty(value = "平台建议价")
	private BigDecimal platformSuggestedPrice;

	@ApiModelProperty(value = "备注")
	private String remark;

}

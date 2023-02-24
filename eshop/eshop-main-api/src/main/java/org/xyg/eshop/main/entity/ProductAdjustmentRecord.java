package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.util.Date;

@Data
@TableName(value = "eshop_product_adjustment_record")
@ApiModel(value = "易车-产品价目表调整记录", description = "易车-产品价目表调整记录")
public class ProductAdjustmentRecord extends DBEntity {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "价目表名称")
	private String priceListName;

	@ApiModelProperty(value = "0 内销 1外销")
	private Integer type;

	@ApiModelProperty(value = "失效日期")
	private Date expiryDate;

	@ApiModelProperty(value = "区域")
	private String region;

	@ApiModelProperty(value = "适用门店类型")
	private String storefrontType;

	@ApiModelProperty(value = "汇率")
	private Integer exchangeRate;

	@ApiModelProperty(value = "币种")
	private String currency;

	@ApiModelProperty(value = "价目表ID")
	private Long priceListId;

	@ApiModelProperty(value = "流程实例ID")
	private String processInstanceId;

	@ApiModelProperty(value = "单号")
	private String docnum;

	@ApiModelProperty(value = "记录同步是否成功 1/0")
	private String erpStatus;

	@ApiModelProperty(value = "记录同步错误信息")
	private String erpErrorLog;

	@ApiModelProperty(value = "适用平台类型")
	private String platformType;

}

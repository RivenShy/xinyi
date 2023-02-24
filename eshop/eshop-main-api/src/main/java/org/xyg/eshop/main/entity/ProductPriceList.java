package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "eshop_product_price_list")
@ApiModel(value = "易车-产品价目表", description = "易车-产品价目表")
public class ProductPriceList extends DBEntity {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty(value = "价目表名称")
	private String priceListName;

	@ApiModelProperty(value = "内外销:0内销 1外销")
	private Integer type;

	@ApiModelProperty(value = "失效日期")
	private Date expiryDate;

	@ApiModelProperty(value = "区域")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String region;

	@ApiModelProperty(value = "适用门店类型")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String storefrontType;

	@ApiModelProperty(value = "父id")
	private Long parentId;

	@ApiModelProperty(value = "币种")
	private String currency;

	@ApiModelProperty(value = "汇率")
	private BigDecimal exchangeRate;

	@ApiModelProperty(value = "业务实体id")
	private Long orgId;

	@ApiModelProperty(value = "有效开始日期")
	private Date effectiveStartDate;

	@ApiModelProperty(value = "erp价目表id")
	private Long erpPriceId;

	@ApiModelProperty(value = "是否有效(0:无效,1:有效)")
	private Integer isValid;

	@ApiModelProperty(value = "适用平台类型")
	private String platformType;

}

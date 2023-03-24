package org.xyg.eshop.main.erpentity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("erp_business_entities")
@ApiModel(value = "ERP业务实体对象", description = "ERP业务实体对象")
public class ErpBusinessEntity extends DBEntity implements Serializable {

	@ApiModelProperty(value = "表id")
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	@ApiModelProperty(value = "业务实体id")
	private Long orgId;
	@ApiModelProperty(value = "业务实体名")
	private String orgName;

	@ApiModelProperty(value = "产业")
	private String glassIndustry;

	@ApiModelProperty(value = "邮政编码")
	private String postalCode;
	@ApiModelProperty(value = "国家")
	private String country;
	@ApiModelProperty(value = "省/市/SAR")
	private String cityName;
	@ApiModelProperty(value = "省/市/SAR")
	private String townOrCity;
	@ApiModelProperty(value = "语言")
	private String language;

	@ApiModelProperty(value = "地址行1")
	private String addressLine1;
	@ApiModelProperty(value = "地址行2")
	private String addressLine2;

	@ApiModelProperty(value = "电话")
	private String telephoneNumber1;
	@ApiModelProperty(value = "传真")
	private String telephoneNumber2;

	@ApiModelProperty(value = "日期起")
	private Date dateFrom;
	@ApiModelProperty(value = "日期止")
	private Date dateTo;
	@ApiModelProperty(value = "创建")
	private Date creationDate;
	@ApiModelProperty(value = "最后更新时间")
	private Date lastUpdateDate;
	@ApiModelProperty(value = "6为导入")
	private Integer status;

	@ApiModelProperty(value = "园区")
	private String park;
	@ApiModelProperty(value = "销售电话")
	private String salesPhone;
	@ApiModelProperty(value = "打印名称")
	private String printName;

	@ApiModelProperty(value = "报价印章")
	private String attachment1;
	@ApiModelProperty(value = "合同印章")
	private String attachment2;
	@ApiModelProperty(value = "其它附件")
	private String attachment3;

	@ApiModelProperty(value = "订单分类")
	private String category1;
	@ApiModelProperty(value = "订单类型")
	private String category2;

	@ApiModelProperty(value = "英文名,language为both时返回")
	@TableField(exist = false)
	private String enName;
	@ApiModelProperty(value = "所得税税率")
	public Double ratio1 ;
	@ApiModelProperty(value = "三大费用比例")
	public Double ratio2 ;
	@ApiModelProperty(value = "业务扩展费比例")
	public Double ratio3 ;
	@ApiModelProperty(value = "统一社会信用代码")
	private String creditCode;
}

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
@TableName("eshop_product_order")
@ApiModel(value = "订单头表",description = "订单头表")
public class ProductOrder extends DBEntity implements Serializable {

	private static final long seriaVersionUID = 1L;

	@ApiModelProperty("订单类型")
	private String orderType;

	@ApiModelProperty("订单日期")
	private Date orderDate;

	@ApiModelProperty("平台订单号")
	private String orderNo;

	@ApiModelProperty("订单金额")
	private BigDecimal orderPrice;

	@ApiModelProperty("备注")
	private String remark;

	@ApiModelProperty("收货人")
	private String consignee;

	@ApiModelProperty("联系方式")
	private String contact;

	@ApiModelProperty("详细地址")
	private String address;

	@ApiModelProperty("距离信义门店")
	private String distance;

	@ApiModelProperty("门店id")
	private Long storefrontId;

	@ApiModelProperty("安装时间")
	private Date installDate;

	@ApiModelProperty("安装人员")
	private String installer;

	@ApiModelProperty("派单人")
	private String dispatcher;

	@ApiModelProperty("发票类型")
	private String billingType;

	@ApiModelProperty("抬头名称")
	private String headName;

	@ApiModelProperty("纳税人识别号")
	private String taxpayerNum;

	@ApiModelProperty("流程实例id")
	private String processInstanceId;

	@ApiModelProperty("门店服务金额")
	private BigDecimal storefrontServicePrice;

	@ApiModelProperty("国家")
	private String country;

	@ApiModelProperty("省编码")
	private String address1;

	@ApiModelProperty("市编码")
	private String address2;

	@ApiModelProperty("区编码")
	private String address3;

	@ApiModelProperty("访问状态")
	private Integer visitStatus;

	@ApiModelProperty("访问日期")
	private Date visitDate;

	@ApiModelProperty("回访员")
	private String visitPerson;

	@ApiModelProperty("访问是否满意")
	private String visitWhetherSatisfied;

	@ApiModelProperty("访问备注")
	private String visitRemark;

}

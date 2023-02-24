package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("eshop_storefront")
@ApiModel(value = "门店主数据", description = "storefront门店主数据对象")
public class Storefront extends DBEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// status客户状态 2正常,3 注销,4黑名单, 1 审批中

	@ApiModelProperty(value = "ERP客户id,批准回写后生成的")
	private Long erpPartyId;

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "客户别名")
	private String partyShortName;

	@ApiModelProperty(value = "社会信用代码")
	private String creditCode;

	@ApiModelProperty(value = "法人")
	private String artificialPerson;

	@ApiModelProperty(value = "固话")
	private String telephone;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "注册资本")
	private Double registerCapital;

	@ApiModelProperty(value = "门店性质")
	@TableField(value = "\"TYPE\"")
	private String type;

	@ApiModelProperty(value = "所属行业")
	private String industry;

	@ApiModelProperty(value = "门店地址")
	private String address;

	@ApiModelProperty(value = "母公司")
	private String parent;

	@ApiModelProperty(value = "门店简介")
	private String introduction;

	@ApiModelProperty(value = "经营范围")
	private String businessScope;

	@ApiModelProperty(value = "门店负责人")
	private String principal;

	@ApiModelProperty(value = "门店等级")
	private Integer storefrontLevel;

	@ApiModelProperty(value = "客户等级名称")
	@TableField(exist = false)
	private String partyLevelName;

	@ApiModelProperty(value = "总信用额度")
	private Double creditAmount;

	@ApiModelProperty(value = "营业执照附件")
	private String businessLicenseAttachment;

	@ApiModelProperty(value = "异常开户与否 1/0")
	private Integer unusual;

	@ApiModelProperty(value = "异常开户说明")
	private String unusualReason;

	@ApiModelProperty(value = "拉入黑名单/注销的原因")
	private String handleReason;

	@ApiModelProperty(value = "潜在客户id")
	private Long potentialPartyId;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "是否创建供应商")
	private Integer supplierFlag;

	@ApiModelProperty(value = "erp客户编码")
	private String partyNumber;

	@ApiModelProperty(value = "注册资本币种")
	private String capitalCurrency;

	@ApiModelProperty(value = "国家")
	private String country;

	@ApiModelProperty(value = "进程实例Id")
	private String processInstanceId;

	@ApiModelProperty(value = "同步erp状态 {Y 成功, N 失败}")
	private String attribute1;

	@ApiModelProperty(value = "同步报错日志")
	private String attribute2;
	@ApiModelProperty(value = "客户注册日期")
	private Date registerDate;

	@ApiModelProperty(value = "销售区域")
	private String saleAreaCode;
	@ApiModelProperty(value = "公司标识如【'qb','jb'】该字段为'jt'就是公共客户")
	private String companyLogo;
	@ApiModelProperty(value = "业务员")
	private Long salesrepId;

	@ApiModelProperty(value = "内外销")
	private String salesType ;

	@ApiModelProperty(value = "中信保额度")
	private BigDecimal zxbTotal ;

	@ApiModelProperty(value = "是否有中信保额度")
	private Long isZxb ;

	@ApiModelProperty(value = "申请人工号")
	private String salesrepNo;

	@ApiModelProperty(value = "品牌{xy 信义、bx 奔讯")
	private String brand;

	@ApiModelProperty(value = "企业类型")
	private String enterpriseType;

	@ApiModelProperty(value = "门店编码")
	private String storefrontCode;

	@ApiModelProperty(value = "销售公司id")
	private Long orgId;

	@ApiModelProperty(value = "当地易车门店距离(km)")
	private String distance;

	@ApiModelProperty(value = "负责人身份证")
	private String principalIdCard;

	@ApiModelProperty(value = "联系方式")
	private String contactInformation;

	@ApiModelProperty(value = "营业执照名称")
	private String businessLicenseName;

	@ApiModelProperty(value = "供应商名称")
	private String supplierName;

	@ApiModelProperty(value = "门店管理人数")
	private String manageNumber;

	@ApiModelProperty(value = "门店技师人数")
	private String artificerNumber;

	@ApiModelProperty(value = "客户休息室")
	private String custLounge;

	@ApiModelProperty(value = "服务车位")
	private String serviceParkingSpace;

	@ApiModelProperty(value = "服务车辆")
	private String serviceVehicles;

	@ApiModelProperty(value = "门店面积长")
	private BigDecimal areaLength;

	@ApiModelProperty(value = "门店面积深")
	private BigDecimal areaDepth;

	@ApiModelProperty(value = "招牌尺寸高")
	private BigDecimal signboardHeight;

	@ApiModelProperty(value = "招牌尺寸长")
	private BigDecimal signboardLength;

	@ApiModelProperty(value = "背景墙尺寸高")
	private BigDecimal backdropHeight;

	@ApiModelProperty(value = "背景墙尺寸长")
	private BigDecimal backdropLength;

	@ApiModelProperty(value = "外部照片")
	private String externalPhoto;

	@ApiModelProperty(value = "内部照片")
	private String interiorPhoto;

	/**
	 * 预计验收日期
	 */
	@ApiModelProperty(value = "预计验收日期")
	private Date expectAcceptanceDate;

	/**
	 * 月均更换数量
	 */
	@ApiModelProperty(value = "月均更换数量")
	private Double averageReplaceQuantity;

	/**
	 * 月均信义数量
	 */
	@ApiModelProperty(value = "月均信义数量")
	private Double averageQuantity;

	/**
	 * 业务来源
	 */
	@ApiModelProperty(value = "业务来源")
	private String businessSource;

	/**
	 * 现有信义库存金额
	 */
	@ApiModelProperty(value = "现有信义库存金额")
	private BigDecimal inventoryAmount;

	/**
	 * 去年信义进货金额
	 */
	@ApiModelProperty(value = "去年信义进货金额")
	private BigDecimal purchaseAmount;

	/**
	 * 本年信义进货目标金额
	 */
	@ApiModelProperty(value = "本年信义进货目标金额")
	private BigDecimal purchaseTargetAmount;

	@ApiModelProperty(value = "门店地址经度")
	private String longitude;

	@ApiModelProperty(value = "门店地址纬度")
	private String latitude;

}

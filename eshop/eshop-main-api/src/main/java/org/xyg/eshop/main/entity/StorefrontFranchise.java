package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 易车-加盟店主数据
 *
 * @TableName eshop_storefront_franchise
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "eshop_storefront_franchise")
@ApiModel(value = "易车-加盟店对象", description = "易车-加盟店对象")
public class StorefrontFranchise extends DBEntity implements Serializable {

	/**
	 * 门店性质
	 */
	@ApiModelProperty(value = "门店性质")
	@TableField(value = "\"TYPE\"")
	private String type;

	/**
	 * 申请单号
	 */
	@ApiModelProperty(value = "申请单号")
	private String franchiseNo;

	/**
	 * 申请日期，以审批开始日期为主
	 */
	@ApiModelProperty(value = "申请日期，以审批开始日期为主")
	private Date applyDate;

	/**
	 * 门店名称
	 */
	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	/**
	 * 门店编码
	 */
	@ApiModelProperty(value = "门店编码")
	private String storefrontCode;

	/**
	 * 门店等级
	 */
	@ApiModelProperty(value = "门店等级")
	private Integer storefrontLevel;

	/**
	 * 门店地址
	 */
	@ApiModelProperty(value = "门店地址")
	private String address;

	/**
	 * 门店地址经纬度
	 */
	@ApiModelProperty(value = "门店地址经纬度")
	private String longitudeAndLatitude;

	/**
	 * 当地易车门店距离(km)
	 */
	@ApiModelProperty(value = "当地易车门店距离(km)")
	private String distance;

	/**
	 * 申请人工号
	 */
	@ApiModelProperty(value = "申请人工号")
	private String salesrepNo;

	/**
	 * 业务实体id
	 */
	@ApiModelProperty(value = "业务实体id")
	private Long orgId;

	/**
	 * 门店负责人
	 */
	@ApiModelProperty(value = "门店负责人")
	private String principal;

	/**
	 * 负责人身份证
	 */
	@ApiModelProperty(value = "负责人身份证")
	private String principalIdCard;

	/**
	 * 联系方式
	 */
	@ApiModelProperty(value = "联系方式")
	private String contactInformation;

	/**
	 * 营业执照名称
	 */
	@ApiModelProperty(value = "营业执照名称")
	private String businessLicenseName;

	/**
	 * 社会信用代码
	 */
	@ApiModelProperty(value = "社会信用代码")
	private String creditCode;

	/**
	 * 供应商名称
	 */
	@ApiModelProperty(value = "供应商名称")
	private String supplierName;

	/**
	 * 门店管理人数
	 */
	@ApiModelProperty(value = "门店管理人数")
	private String manageNumber;

	/**
	 * 门店技师人数
	 */
	@ApiModelProperty(value = "门店技师人数")
	private String artificerNumber;

	/**
	 * 客户休息室
	 */
	@ApiModelProperty(value = "客户休息室")
	private String custLounge;

	/**
	 * 服务车位
	 */
	@ApiModelProperty(value = "服务车位")
	private String serviceParkingSpace;

	/**
	 * 服务车辆
	 */
	@ApiModelProperty(value = "服务车辆")
	private String serviceVehicles;

	/**
	 * 门店面积长
	 */
	@ApiModelProperty(value = "门店面积长")
	private BigDecimal areaLength;

	/**
	 * 门店面积深
	 */
	@ApiModelProperty(value = "门店面积深")
	private BigDecimal areaDepth;

	/**
	 * 招牌尺寸高
	 */
	@ApiModelProperty(value = "招牌尺寸高")
	private BigDecimal signboardHeight;

	/**
	 * 招牌尺寸长
	 */
	@ApiModelProperty(value = "招牌尺寸长")
	private BigDecimal signboardLength;

	/**
	 * 背景墙尺寸高
	 */
	@ApiModelProperty(value = "背景墙尺寸高")
	private BigDecimal backdropHeight;

	/**
	 * 背景墙尺寸长
	 */
	@ApiModelProperty(value = "背景墙尺寸长`")
	private BigDecimal backdropLength;

	/**
	 * 外部照片
	 */
	@ApiModelProperty(value = "外部照片")
	private String externalPhoto;

	/**
	 * 内部照片
	 */
	@ApiModelProperty(value = "内部照片")
	private String interiorPhoto;

	/**
	 * 门店简介
	 */
	@ApiModelProperty(value = "门店简介")
	private String introduction;

	/**
	 * 国家
	 */
	@ApiModelProperty(value = "国家")
	private String country;

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

	@ApiModelProperty(value = "流程实例ID")
	private String processInstanceId;

	private static final long serialVersionUID = 1L;

}

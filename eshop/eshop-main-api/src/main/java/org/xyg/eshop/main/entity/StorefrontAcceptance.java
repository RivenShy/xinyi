package org.xyg.eshop.main.entity;

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
 * 易车-门店验收主数据
 *
 * @TableName eshop_storefront_acceptance
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "eshop_storefront_acceptance")
@ApiModel(value = "易车-门店验收对象", description = "易车-门店验收对象")
public class StorefrontAcceptance extends DBEntity implements Serializable {

	/**
	 * 头id
	 */
	@ApiModelProperty(value = "头id")
	private Long headId;

	/**
	 * 验收人工号
	 */
	@ApiModelProperty(value = "验收人工号")
	private String acceptanceEmpno;

	/**
	 * 验收日期
	 */
	@ApiModelProperty(value = "验收日期")
	private Date acceptanceDate;

	/**
	 * 技术要求
	 */
	@ApiModelProperty(value = "技术要求")
	private String technicalRequirement;

	/**
	 * 服务要求
	 */
	@ApiModelProperty(value = "服务要求")
	private String serviceRequirements;

	/**
	 * 保证金是否缴纳
	 */
	@ApiModelProperty(value = "保证金是否缴纳")
	private Integer depositPayment;

	/**
	 * 物料包缴纳金额
	 */
	@ApiModelProperty(value = "物料包缴纳金额")
	private BigDecimal premiumReceived;

	/**
	 * 公众号定位
	 */
	@ApiModelProperty(value = "公众号定位")
	private String officialAccountLocate;

	/**
	 * 公众号名称
	 */
	@ApiModelProperty(value = "公众号名称")
	private String officialName;

	/**
	 * 设计效果图
	 */
	@ApiModelProperty(value = "设计效果图")
	private String designEffectPhoto;

	/**
	 * 背景墙（背景墙全景照）
	 */
	@ApiModelProperty(value = "背景墙（背景墙全景照）")
	private String backgroundWallPhoto;

	/**
	 * 装修图（干净整洁照）
	 */
	@ApiModelProperty(value = "装修图（干净整洁照）")
	private String renovationPhoto;

	/**
	 * 客户休息区（区域整体照）
	 */
	@ApiModelProperty(value = "客户休息区（区域整体照）")
	private String loungePhoto;

	/**
	 * 发光招牌（整体发光照）
	 */
	@ApiModelProperty(value = "发光招牌（整体发光照）")
	private String signPhoto;

	/**
	 * 服务区（带车整体照）
	 */
	@ApiModelProperty(value = "服务区（带车整体照）")
	private String serviceAreaPhoto;

	/**
	 * 验收结论
	 */
	@ApiModelProperty(value = "验收结论")
	private String acceptanceConclusion;

	private static final long serialVersionUID = 1L;
}

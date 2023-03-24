package org.xyg.eshop.main.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xyg.eshop.main.dto.BaseDocument;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTemplate extends BaseDocument {
	private static final long serialVersionUID = -6586605807343218392L;

	/**
	 * 业务系统
	 */
	@ApiModelProperty(value = "业务系统")
	private String businessType;

	/**
	 * 排序号
	 */
	@ApiModelProperty(value = "排序号")
	private Integer seqNum;

	/**
	 * 数据区域名称（json字符串）
	 */
	@ApiModelProperty(value = "数据区域名称（json字符串）")
	private String dataRegionNames;
}

package org.xyg.eshop.main.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//@TableName("oldoc_template")
@EqualsAndHashCode(callSuper = true)
//@ApiModel(value = "template对象", description = "模板表")
public class ContractTemplateDTO extends BaseDocument {
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

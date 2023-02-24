package org.xyg.eshop.main.vo;

import lombok.Data;

/**
 * @author ww
 * @date 2023-02-03
 */
@Data
public class AdvancedSearchVO {

	/**
	 * 字段名
	 */
	private String fieldName;

	/**
	 * 操作
	 */
	private String operation;

	/**
	 * 输入值
	 */
	private String value;
}

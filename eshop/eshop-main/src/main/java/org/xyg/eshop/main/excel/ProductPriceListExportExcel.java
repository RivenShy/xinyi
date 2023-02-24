package org.xyg.eshop.main.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ColumnWidth(25)
@HeadRowHeight(25)
@ContentRowHeight(18)
public class ProductPriceListExportExcel implements Serializable {

	/**
	 * 产品名称
	 */
	@ExcelProperty("*产品名称")
	@ColumnWidth(20)
	private String productName;

	/**
	 * 本厂型号
	 */
	@ExcelProperty("*本厂型号")
	@ColumnWidth(30)
	private String productModel;

	/**
	 * 本厂价
	 */
	@ExcelProperty("*出厂成本价")
	@ColumnWidth(20)
	private BigDecimal basePrice;

	/**
	 * 线上建议售价
	 */
	@ExcelProperty("*线上建议售价")
	@ColumnWidth(20)
	private BigDecimal suggestedPrice;

	/**
	 * 门店建议结算价
	 */
	@ExcelProperty("*门店建议结算价")
	@ColumnWidth(20)
	private BigDecimal settlementPrice;

	/**
	 * 平台入库价
	 */
	@ExcelProperty("*平台入库价")
	@ColumnWidth(20)
	private BigDecimal platformReceiptPrice;

	/**
	 * 平台建议派单价
	 */
	@ExcelProperty("*平台建议派单价")
	@ColumnWidth(20)
	private BigDecimal platformSuggestedPrice;

	/**
	 * 备注
	 */
	@ExcelProperty("备注")
	@ColumnWidth(20)
	private String remark;

}

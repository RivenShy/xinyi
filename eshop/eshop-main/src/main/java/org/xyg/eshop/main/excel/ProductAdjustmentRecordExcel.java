package org.xyg.eshop.main.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductAdjustmentRecordExcel implements Serializable {

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
	 * 门店预估利润
	 */
	@ExcelProperty("门店预估利润")
	@ColumnWidth(20)
	private BigDecimal estimatedProfit;

	/**
	 * 平台预估利润
	 */
	@ExcelProperty("平台预估利润")
	@ColumnWidth(20)
	private BigDecimal platformEstimatedProfit;

	/**
	 * 毛利率
	 */
	@ExcelProperty("毛利率")
	@ColumnWidth(20)
	private BigDecimal grossRate;

	/**
	 * 我司预估利润
	 */
	@ExcelProperty("我司预估利润")
	@ColumnWidth(20)
	private BigDecimal companyEstimatedProfit;

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

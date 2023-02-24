package org.xyg.eshop.main.excel.product;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ColumnWidth(25)
@HeadRowHeight(25)
@ContentRowHeight(18)
public class ProductModelLinesExportExcel {

	@ExcelProperty("产品名称")
	private String productName;
	@ExcelProperty("本厂型号")
	private String factoryMode ;
	@ExcelProperty(value = "规格")
	private String specifications;
	@ExcelProperty("商品分类")
	private Long productType;
	@ExcelProperty("商品零售价")
	private BigDecimal retailPrice;
	@ExcelProperty(value = "商品库存单位")
	private String units;
	@ExcelProperty("商品库存数量")
	private BigDecimal inventoryQuantity;
	@ExcelProperty("商品供应商")
	private String supplier;
	@ExcelProperty("商品描述")
	private String description;
}

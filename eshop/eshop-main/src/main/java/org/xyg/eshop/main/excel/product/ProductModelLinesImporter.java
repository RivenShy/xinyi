package org.xyg.eshop.main.excel.product;

import lombok.RequiredArgsConstructor;
import org.springrabbit.core.excel.support.ExcelImporter;
import org.xyg.eshop.main.service.IProductModelLinesService;

import java.util.List;

@RequiredArgsConstructor
public class ProductModelLinesImporter implements ExcelImporter<ProductModelLinesExcel> {

	private final IProductModelLinesService productModelLinesService;

	private final Long headId;

	@Override
	public void save(List<ProductModelLinesExcel> data) {
		productModelLinesService.savaExcelImporter(data, headId);
	}
}

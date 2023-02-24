package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.excel.util.ExcelUtil;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.xyg.eshop.main.entity.ProductModelLines;
import org.xyg.eshop.main.excel.FileEntityUtil;
import org.xyg.eshop.main.excel.product.ProductModelAttachmentExcel;
import org.xyg.eshop.main.excel.product.ProductModelLinesExcel;
import org.xyg.eshop.main.excel.product.ProductModelLinesExportExcel;
import org.xyg.eshop.main.service.IProductModelAttachmentService;
import org.xyg.eshop.main.service.IProductModelLinesService;
import org.xyg.eshop.main.vo.ProductModelLinesVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/productModelLines")
@Api(value = "产品库型号维护", tags = "产品库型号维护")
@AllArgsConstructor
public class ProductModelLinesController {

	private final IProductModelLinesService productModelLinesService;

	private final IProductModelAttachmentService productModelAttachmentService;


	@GetMapping("/getPage")
	@ApiOperation(value = "分页查询")
	public R<IPage<ProductModelLines>> getPage(Query query) {
		return productModelLinesService.getPage(Condition.getPage(query));
	}

	@PostMapping("/insertData")
	@ApiOperation("插入数据")
	public R<Boolean> insertData(@RequestBody ProductModelLines entity) {
		return productModelLinesService.insertData(entity);
	}

	@GetMapping("/deleteData")
	@ApiOperation(value = "删除数据")
	@ApiImplicitParam(value = "id", name = "id", required = true)
	public R<Boolean> deleteData(@RequestParam("id") Long id) {
		return productModelLinesService.deleteData(id);
	}

	@PutMapping("/updateData")
	@ApiOperation(value = "更新数据")
	public R<Boolean> updateData(@RequestBody ProductModelLines entity) {
		return productModelLinesService.updateDate(entity);
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "获取详情数据")
	@ApiImplicitParam(value = "id", name = "id", required = true)
	public R<ProductModelLinesVO> getDetail(@RequestParam("id") Long id) {
		return productModelLinesService.getDetail(id);
	}

	@GetMapping("/getList")
	@ApiOperation(value = "获取列表数据")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "headId", name = "头表ID")
	})
	public R<List<ProductModelLines>> getList(@RequestParam(value = "headId", required = false) Long headId) {
		return productModelLinesService.getList(headId);
	}

	@PostMapping("/importExcel")
	@ApiOperation(value = "导入型号数据")
	public R<String> importExcel(FileEntityUtil fileUtil) {
		/*ProductModelLinesImporter productModelLinesImporter = new ProductModelLinesImporter(productModelLinesService , fileUtil.getHeadId());
		ExcelUtil.save(fileUtil.getFile(),productModelLinesImporter, ProductModelLinesExcel.class);*/
		List<ProductModelLinesExcel> productModelLinesExcels = ExcelUtil.read(fileUtil.getFile(), 0, ProductModelLinesExcel.class);
		if (CollectionUtil.isNotEmpty(productModelLinesExcels)) {
			productModelLinesService.savaExcelImporter(productModelLinesExcels, fileUtil.getHeadId());
		}
		List<ProductModelAttachmentExcel> productModelAttachmentExcels = ExcelUtil.read(fileUtil.getFile(), 1, ProductModelAttachmentExcel.class);
		if (CollectionUtil.isNotEmpty(productModelAttachmentExcels)) {
			productModelAttachmentService.savaExcelImporter(productModelAttachmentExcels, fileUtil.getHeadId());
		}
		return R.success("导入成功");
	}

	@GetMapping("/exportExcel")
	@ApiOperation(value = "导出型号数据")
	public void exportExcel(HttpServletResponse response) {
		List<ProductModelLinesExportExcel> productModelLinesExportExcelList = productModelLinesService.exportExcelList();
		ExcelUtil.export(response, productModelLinesExportExcelList, ProductModelLinesExportExcel.class);
	}

}

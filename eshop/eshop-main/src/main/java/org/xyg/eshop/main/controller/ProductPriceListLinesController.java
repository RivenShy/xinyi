package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.excel.util.ExcelUtil;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.DateUtil;
import org.xyg.eshop.main.entity.ProductPriceListLines;
import org.xyg.eshop.main.excel.ProductPriceListExportExcel;
import org.xyg.eshop.main.service.IProductPriceListLinesService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping(value = "/productPriceListLines")
@RestController
@AllArgsConstructor
@Api(value = "汽玻-产品价目表行表", tags = "汽玻-产品价目表行表")
public class ProductPriceListLinesController extends RabbitController {
	private final IProductPriceListLinesService productPriceListLinesService;

	@GetMapping("/getPage")
	@ApiOperation(value = "分页查询产品价目行表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "头表id",value = "headId"),
		@ApiImplicitParam(name = "本厂型号",value = "productModel"),
		@ApiImplicitParam(name = "装车位置",value = "position"),
		@ApiImplicitParam(name = "品牌",value = "brand"),
		@ApiImplicitParam(name = "车型",value = "model"),
		@ApiImplicitParam(name = "产品名称",value = "itemChDescription"),
		@ApiImplicitParam(name = "宽:最大值",value = "maxWidth"),
		@ApiImplicitParam(name = "宽:最小值",value = "minWidth"),
		@ApiImplicitParam(name = "长:最大值",value = "maxLength"),
		@ApiImplicitParam(name = "长:最小值",value = "minLength"),
		@ApiImplicitParam(name = "OEM型号",value = "oemModel"),
		@ApiImplicitParam(name = "加工种类",value = "machiningType")
	})
	public R<IPage<ProductPriceListLines>> getPage(@RequestParam(value = "headId", required = false) Long headId,
												   @RequestParam(value = "productModel", required = false) String productModel,
												   @RequestParam(value = "position", required = false) String position,
												   @RequestParam(value = "brand", required = false) String brand,
												   @RequestParam(value = "model", required = false) String model,
												   @RequestParam(value = "itemChDescription" , required = false) String itemChDescription,
												   @RequestParam(value = "maxWidth" , required = false) Long maxWidth,
												   @RequestParam(value = "minWidth" , required = false) Long minWidth,
												   @RequestParam(value = "maxLength" , required = false) Long maxLength,
												   @RequestParam(value = "minLength" , required = false) Long minLength,
												   @RequestParam(value = "oemModel" , required = false) String oemModel,
												   @RequestParam(value = "machiningType",required = false) Integer machiningType,
												   Query query){
		return productPriceListLinesService.getPage(Condition.getPage(query),headId,productModel,position,brand,model,itemChDescription,maxWidth,minWidth,maxLength,minLength,oemModel,machiningType);
	}

	@PostMapping("/submit")
	@ApiOperation(value = "提交产品价目行表")
	public R<String> submit(@RequestBody ProductPriceListLines productPriceListLines){
		return productPriceListLinesService.submit(productPriceListLines);
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存产品价目行表")
	public R<String> save(@RequestBody ProductPriceListLines productPriceListLines){
		return productPriceListLinesService.saveProductPriceListLines(productPriceListLines);
	}

	@PutMapping("/update")
	@ApiOperation(value = "修改产品价目行表")
	public R<String> update(@RequestBody ProductPriceListLines productPriceListLines){
		return productPriceListLinesService.updateProductPriceListLines(productPriceListLines);
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "查询详情")
	@ApiImplicitParam(value = "id", name = "id")
	public R<ProductPriceListLines> getDetail(@RequestParam(value = "id") Long id) {
		return productPriceListLinesService.getDetail(id);
	}

	@GetMapping("/exportPriceListLines")
	@ApiOperation(value = "价目表导出")
	public void exportPriceListLines(HttpServletResponse response ,  ProductPriceListLines lines){
		List<ProductPriceListExportExcel> exportLinesList = productPriceListLinesService.exportPriceListLines(lines);
		ExcelUtil.export(response, "价目表" + DateUtil.time(), "价目表", exportLinesList, ProductPriceListExportExcel.class);
	}

}

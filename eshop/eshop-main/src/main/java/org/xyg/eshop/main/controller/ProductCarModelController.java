package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.protobuf.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.entity.ProductCarModel;
import org.xyg.eshop.main.service.IProductCarModelService;
import org.xyg.eshop.main.vo.*;

import java.util.List;

@RequestMapping("/productCarModel")
@RestController
@Api(value = "产品库车产车型维护", tags = "产品库车厂车型维护")
@AllArgsConstructor
public class ProductCarModelController extends RabbitController {
	private final IProductCarModelService productCarModelService;

	@GetMapping("/getPage")
	@ApiOperation(value = "分页查询")
	public R<IPage<ProductCarModel>> getPage(Query query) {
		return productCarModelService.getPage(Condition.getPage(query));
	}

	@PostMapping("/insertData")
	@ApiOperation("插入数据")
	public R<Boolean> insertData(@RequestBody ProductModelMergeVO entity) {
		try {
			return productCarModelService.insertData(entity);
		} catch (ServiceException e) {
			return R.fail(e.getMessage());
		}
	}

	@GetMapping("/deleteData")
	@ApiOperation(value = "删除数据")
	@ApiImplicitParam(value = "id", name = "id", required = true)
	public R<Boolean> deleteData(@RequestParam("id") Long id) {
		try {
			return productCarModelService.deleteData(id);
		} catch (ServiceException e) {
			return R.fail(e.getMessage());
		}
	}

	@PutMapping("/updateData")
	@ApiOperation(value = "更新数据")
	public R<Boolean> updateData(@RequestBody ProductModelMergeVO entity) {
		try {
			return productCarModelService.updateDate(entity);
		} catch (ServiceException e) {
			return R.fail(e.getMessage());
		}
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "获取详情数据")
	@ApiImplicitParam(value = "id", name = "id", required = true)
	public R<ProductCarModelVO> getDetail(@RequestParam("id") Long id) {
		return productCarModelService.getDetail(id);
	}

	@GetMapping("/getProductPage")
	@ApiOperation(value = "获取产品数据")
	public R<IPage<ProductCarModelVO>> getProductPage(Query query , ProductCarModelVO productCarModelVO){
		try {
			return productCarModelService.getProductPage(Condition.getPage(query) , productCarModelVO) ;
		} catch (ServiceException e) {
			return R.fail(e.getMessage());
		}
	}

	@GetMapping("/getProductList")
	@ApiOperation(value = "获取产品数据")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "产品行ID" , name = "productLineId" , required = false),
		@ApiImplicitParam(value = "xyg型号" , name = "xygType" , required = false),
		@ApiImplicitParam(value = "附件" , name = "attachment" , required = false)
	})
	public R<List<ProductCarModelVO>> getProductList(@RequestParam(value = "productLineId" , required = false)Long productLineId ,
													 @RequestParam(value = "xygType" , required = false)String xygType ,
													 @RequestParam(value = "attachment" , required = false)String attachment,
													 @RequestParam(value = "colour" , required = false)String colour){
			return productCarModelService.getProductColor(productLineId,xygType,attachment , colour);
	}

	@GetMapping("/findProductCarModel")
	@ApiOperation(value = "获取品牌下拉树")
	public R<List<ProductCarModelCascader>> findProductCarModel(){
		return productCarModelService.findProductCarModel() ;
	}

	@GetMapping("/getToPriceListPage")
	@ApiOperation(value = "产品价目表分页搜索产品")
	public R<IPage<ProductModelLinesToPrictListVO>> getToPriceListPage(Query query, ProductModelLinesToPrictListVO linesToPrictListVO) {
		return R.data(productCarModelService.getToPriceListPage(Condition.getPage(query),linesToPrictListVO));
	}

	@PostMapping("/lines-save")
	@ApiOperation(value = "行表保存接口")
	public R<ProductModelLinesVO> linesSave(@RequestBody ProductModelLinesVO productModelLinesVO){
		return R.data(productCarModelService.linesSave(productModelLinesVO));
	}

	@PostMapping("/lines-update")
	@ApiOperation(value = "行表修改接口")
	public R<ProductModelLinesVO> linesUpdate(@RequestBody ProductModelLinesVO productModelLinesVO){
		return R.data(productCarModelService.linesUpdate(productModelLinesVO));
	}

	@GetMapping("/lines-detail")
	@ApiOperation(value = "行表详情接口")
	public R<ProductModelLinesVO> linesDetail(@RequestParam("id") Long id){
		return R.data(productCarModelService.linesDetail(id));
	}

}

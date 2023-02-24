package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.xyg.eshop.main.entity.ProductPriceList;
import org.xyg.eshop.main.service.IProductPriceListService;
import org.xyg.eshop.main.vo.ProductPriceListTreeVO;
import org.xyg.eshop.main.vo.ProductPriceListVO;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/productPriceList")
@Api(value = "易车-产品价目表", tags = "易车-产品价目表")
public class ProductPriceListController extends RabbitController {

	private final IProductPriceListService productPriceListService;

	@GetMapping("/getPage")
	@ApiOperation(value = "分页查询数据")
	public R<IPage<ProductPriceList>> getPage(Query query) {
		return R.data(productPriceListService.getPage(Condition.getPage(query)));
	}

	@PostMapping("/submit")
	@ApiOperation(value = "提交产品价目表")
	public R<String> submit(@RequestBody ProductPriceList productPriceList){
		return productPriceListService.submit(productPriceList);
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存产品价目表")
	public R<String> save(@RequestBody ProductPriceList productPriceList){
		return productPriceListService.saveProductPriceList(productPriceList);
	}

	@GetMapping("/getTreeList")
	@ApiOperation(value = "获取价目表树型列表数据")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "parentId", name = "父id"),
		@ApiImplicitParam(value = "priceListName", name = "价目表名称"),
		@ApiImplicitParam(value = "type" , name = "内外销"),
		@ApiImplicitParam(value = "partyName" , name = "区域/客户")
	})
	public R<List<ProductPriceListTreeVO>> getTreeList(@RequestParam(value = "parentId",required = false) Long parentId,
													   @RequestParam(value = "priceListName",required = false) String priceListName,
													   @RequestParam(value = "type",required = false) Integer type,
													   @RequestParam(value = "regionOrPartyName",required = false) String regionOrPartyName){
		return productPriceListService.getTreeList(parentId,priceListName,type,regionOrPartyName);
	}

	@PutMapping("/update")
	@ApiOperation(value = "修改产品价目表")
	public R<String> update(@RequestBody ProductPriceList productPriceList){
		return productPriceListService.updateProductPriceList(productPriceList);
	}

	@GetMapping("/getList")
	@ApiOperation(value = "获取价目表列表")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "parentId", name = "父id"),
		@ApiImplicitParam(value = "priceListName", name = "价目表名称"),
		@ApiImplicitParam(value = "type" , name = "内外销"),
		@ApiImplicitParam(value = "partyName" , name = "区域/客户"),
		@ApiImplicitParam(value = "id" , name = "id"),
		@ApiImplicitParam(value = "partyId" , name = "客户ID"),
		@ApiImplicitParam(value = "isValid" , name = "是否有效(0:无效,1:有效)")
	})
	public R<List<ProductPriceList>> getList(@RequestParam(value = "parentId",required = false) Long parentId,
											 @RequestParam(value = "priceListName",required = false) String priceListName,
											 @RequestParam(value = "type",required = false) Integer type,
											 @RequestParam(value = "regionOrPartyName",required = false) String regionOrPartyName,
											 @RequestParam(value = "id" , required = false) Long id,
											 @RequestParam(value = "storefrontType" , required = false) String storefrontType,
											 @RequestParam(value = "isValid" , required = false) Integer isValid){
		return productPriceListService.getList(parentId,priceListName,type,regionOrPartyName , id ,storefrontType,isValid);
	}

	@GetMapping("/detail")
	@ApiOperation(value = "获取价目表详情")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "id", name = "主键id")
	})
	public R<ProductPriceListVO> detail(@RequestParam(value = "id") Long id){
		return productPriceListService.detail(id);
	}

	@GetMapping("/getByTypePage")
	@ApiOperation(value = "分页获取内外销价目表列表")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "id", name = "主键id")
	})
	public R<IPage<ProductPriceListVO>> getByTypePage(@RequestParam(value = "type",required = false,defaultValue = "0") Integer type,
													  @RequestParam(value = "priceListName",required = false) String priceListName,
											          Query query){
		return R.data(productPriceListService.getByTypePage(Condition.getPage(query),type,priceListName));
	}

}

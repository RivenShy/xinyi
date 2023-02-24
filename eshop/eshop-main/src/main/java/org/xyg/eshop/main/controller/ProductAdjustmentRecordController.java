package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.excel.util.ExcelUtil;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.constant.RabbitConstant;
import org.springrabbit.core.tool.utils.DateUtil;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.eshop.main.entity.ProductAdjustmentRecordLines;
import org.xyg.eshop.main.excel.ProductAdjustmentRecordExcel;
import org.xyg.eshop.main.service.IProductAdjustmentRecordService;
import org.xyg.eshop.main.vo.ProductAdjustmentRecordVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/productAdjustmentRecord")
@Api(value = "易车-产品价目表调整单", tags = "易车-产品价目表调整单")
public class ProductAdjustmentRecordController extends RabbitController {

	private final IProductAdjustmentRecordService productAdjustmentRecordService;

	@PostMapping("/save")
	@ApiOperation(value = "保存产品调整单")
	public R<ProductAdjustmentRecordVO> save(@RequestBody ProductAdjustmentRecordVO productAdjustmentRecordVO){
		return productAdjustmentRecordService.saveProductAdjustmentRecord(productAdjustmentRecordVO);
	}

	@PostMapping("/submit")
	@ApiOperation(value = "提交产品调整单")
	public R<ProductAdjustmentRecordVO> submit(@RequestBody ProductAdjustmentRecordVO productAdjustmentRecordVO) throws IOException {
		ProductAdjustmentRecordVO resultVO = productAdjustmentRecordService.submit(productAdjustmentRecordVO);
		productAdjustmentRecordService.startProcess(productAdjustmentRecordVO.getId());
		return R.data(resultVO);
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "调整单详情")
	public R<ProductAdjustmentRecordVO> getDetail(@RequestParam(value = "id") Long id){
		return productAdjustmentRecordService.getDetail(id);
	}

	@PostMapping("/importerProductAdjustmentRecord")
	@ApiOperation(value = "导入调整单", notes = "传入excel表格")
	public R<ProductAdjustmentRecordVO> importerProductAdjustmentRecord(@RequestParam("file") MultipartFile file,
																		@RequestParam("headId") Long headId) {
		// 解析excel
		List<ProductAdjustmentRecordExcel> productAdjustmentRecordExcelList = ExcelUtil.read(file, 0, ProductAdjustmentRecordExcel.class);
		ProductAdjustmentRecordVO productAdjustmentRecordVO = productAdjustmentRecordService.importerProductAdjustmentRecord(productAdjustmentRecordExcelList,headId);
		return R.data(productAdjustmentRecordVO,"导入成功");
	}

	@GetMapping("/deleteLines")
	@ApiOperation(value = "删除调整单行表",notes = "传入ids,多个逗号分隔")
	public R<String> deleteLines(@RequestParam("ids") String ids){
		return productAdjustmentRecordService.deleteLines(ids);
	}

	@GetMapping("/getToPriceListPage")
	@ApiOperation(value = "价目表查询调整单分页查询列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "调整单id", type = "query"),
		@ApiImplicitParam(name = "priceListId", value = "价目表id", type = "query"),
		@ApiImplicitParam(name = "docnum", value = "单号", type = "query"),
		@ApiImplicitParam(name = "erpStatus", value = "erp状态", type = "query"),
	})
	public R<IPage<ProductAdjustmentRecordVO>> getToPriceListPage(@RequestParam(value = "id",required = false) Long id,
													   @RequestParam(value = "priceListId",required = false) Long priceListId,
													   @RequestParam(value = "docnum",required = false) String docnum,
													   @RequestParam(value = "erpStatus",required = false) String erpStatus,
													   Query query){
		return productAdjustmentRecordService.getToPriceListPage(Condition.getPage(query),id,priceListId,docnum,erpStatus);
	}

	@GetMapping("/delete")
	@ApiOperation(value = "删除整个调整单",notes = "传入ids,多个逗号分隔")
	public R<String> delete(@RequestParam("id") Long id){
		return productAdjustmentRecordService.delete(id);
	}

	@GetMapping("/saveByProductIds")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "调整单id", type = "query"),
		@ApiImplicitParam(name = "productIds", value = "产品id", type = "query")
	})
	@ApiOperation(value = "根据产品id添加调整单,id多个逗号分隔")
	public R<String> saveByProductIds(@RequestParam("id") Long id,@RequestParam("productIds") String productIds){
		return productAdjustmentRecordService.saveByProductIds(id,productIds);
	}

	@GetMapping("/lines-list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "头表id",value = "headId"),
		@ApiImplicitParam(name = "本厂型号",value = "productModel"),
		@ApiImplicitParam(name = "装车位置",value = "position"),
		@ApiImplicitParam(name = "品牌",value = "brand"),
		@ApiImplicitParam(name = "车型",value = "model"),
		@ApiImplicitParam(name = "产品名称",value = "itemChDescription"),
		@ApiImplicitParam(name = "加工种类",value = "machiningType"),
		@ApiImplicitParam(name = "宽:最大值",value = "maxWidth"),
		@ApiImplicitParam(name = "宽:最小值",value = "minWidth"),
		@ApiImplicitParam(name = "长:最大值",value = "maxLength"),
		@ApiImplicitParam(name = "长:最小值",value = "minLength"),
		@ApiImplicitParam(name = "OEM型号",value = "oemModel")
	})
	@ApiOperation(value = "查询行表集合")
	public R<List<ProductAdjustmentRecordLines>> linesList(@RequestParam(value = "headId") Long headId,
														   @RequestParam(value = "productModel", required = false) String productModel,
														   @RequestParam(value = "position", required = false) String position,
														   @RequestParam(value = "brand", required = false) String brand,
														   @RequestParam(value = "model", required = false) String model,
														   @RequestParam(value = "itemChDescription" , required = false) String itemChDescription,
														   @RequestParam(value = "machiningType" , required = false) Integer machiningType,
														   @RequestParam(value = "maxWidth" , required = false) Long maxWidth,
														   @RequestParam(value = "minWidth" , required = false) Long minWidth,
														   @RequestParam(value = "maxLength" , required = false) Long maxLength,
														   @RequestParam(value = "minLength" , required = false) Long minLength,
														   @RequestParam(value = "oemModel" , required = false) String oemModel){
		return productAdjustmentRecordService.linesList(headId,productModel,position,brand,model,itemChDescription,machiningType,maxWidth,minWidth,maxLength,minLength,oemModel);
	}

	@PostMapping("/lines-update-by-id")
	@ApiOperation(value = "保存产品调整单")
	public R<ProductAdjustmentRecordLines> linesUpdateById(@RequestBody ProductAdjustmentRecordLines productAdjustmentRecordLines){
		return productAdjustmentRecordService.linesUpdateById(productAdjustmentRecordLines);
	}

	@GetMapping("/copySaveById")
	@ApiOperation(value = "保存复制的调整单行表信息")
	public R<ProductAdjustmentRecordLines> copySaveById(@RequestParam("id") Long id){
		return R.data(productAdjustmentRecordService.copySaveById(id));
	}

	@PostMapping("/flowInstanceExecutionStartCallback")
	@ApiOperation(value = "流程实例任务开始回调接口")
	public R<CallBackMethodResDto> flowInstanceExecutionStartCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			return R.data(productAdjustmentRecordService.flowInstanceExecutionStartCallback(inDto));
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	@PostMapping("/flowInstanceExecutionEndCallback")
	@ApiOperation(value = "流程实例任务结束回调接口")
	public R<CallBackMethodResDto> flowInstanceExecutionEndCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			return R.data(productAdjustmentRecordService.flowInstanceExecutionEndCallback(inDto));
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	@GetMapping("/templateDownload")
	@ApiOperation(value = "导入模板下载")
	public void templateDownload(HttpServletResponse response){
		ExcelUtil.export(response, "价目表导入模板" + DateUtil.time(), "价目表", new ArrayList<>(1), ProductAdjustmentRecordExcel.class);
	}

	@PostMapping("/retrySyncToErp")
	@ApiOperation("重新集成价目表至erp")
	public R<Object> retrySyncToErp(@RequestParam("id") Long id) throws IOException {
		productAdjustmentRecordService.updateAdjustmentRecordToPriceList(id);
		return R.success(RabbitConstant.DEFAULT_SUCCESS_MESSAGE);
	}

}

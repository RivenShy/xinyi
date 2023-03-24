package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.excel.util.ExcelUtil;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.DateUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.excel.order.ProductOrderExcel;
import org.xyg.eshop.main.service.IProductOrderService;
import org.xyg.eshop.main.vo.ProductOrderVO;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/productOrder")
@Api(value = "订单管理",tags = "订单管理")
public class ProductOrderController extends RabbitController {

	private final IProductOrderService orderService;

	@GetMapping("/getPage")
	@ApiOperation(value = "分页列表",notes = "分页列表")
	public R<IPage<ProductOrderVO>> getPage(Query query,ProductOrderVO orderVO){
		return R.data(orderService.getPage(Condition.getPage(query),orderVO));
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存",notes = "保存")
	public R<String> saveData(@RequestBody ProductOrderVO orderVO){
		orderService.saveData(orderVO);
		return R.status(true);
	}

	@PostMapping("/submit")
	@ApiOperation(value = "提交",notes = "提交")
	public R<String> submit(@RequestBody ProductOrderVO orderVO){
		orderService.submit(orderVO);
		return R.status(true);
	}

	@PostMapping("/detail")
	@ApiOperation(value = "详情",notes = "详情")
	public R<ProductOrderVO> detail(@RequestParam(value = "id",required = false) Long id,
									@RequestParam(value = "processInstanceId",required = false) String processInstanceId){
		if (id == null && StringUtil.isBlank(processInstanceId)){
			return R.fail("搜索条件为空");
		}
		return R.data(orderService.detail(id,processInstanceId));
	}

	@GetMapping("/templateDownload")
	@ApiOperation(value = "导入模板下载",notes = "导入模板下载")
	public void templateDownload(HttpServletResponse response){
		ExcelUtil.export(response, "订单导入模板" + DateUtil.time(), "订单", new ArrayList<>(1), ProductOrderExcel.class);
	}

	@GetMapping("/export")
	@ApiOperation(value = "订单导出",notes = "订单导出")
	public void exportPriceListLines(HttpServletResponse response ,  ProductOrderVO orderVO){
		List<ProductOrderExcel> exportList = orderService.getExcelList(orderVO);
		ExcelUtil.export(response, "订单" + DateUtil.time(), "订单", exportList, ProductOrderExcel.class);
	}

	@PostMapping("/importer")
	@ApiOperation(value = "导入订单", notes = "传入excel表格")
	public R<List<ProductOrderVO>> importer(@RequestParam("file") MultipartFile file) {
		// 解析excel
		List<ProductOrderExcel> orderExcelList = ExcelUtil.read(file, 0, ProductOrderExcel.class);
		List<ProductOrderVO> orderVOList = orderService.importer(orderExcelList);
		return R.data(orderVOList,"导入成功");
	}

	@DeleteMapping("/lines-delete")
	@ApiOperation(value = "删除行表数据", notes = "删除行表数据")
	public R<String> linesDelete(@RequestParam("ids") String ids) {
		return R.status(orderService.linesDelete(ids));
	}

	@PostMapping("/distributeStore")
	@ApiOperation(value = "分配门店", notes = "分配门店")
	public R<String> distributeStore(@RequestBody List<ProductOrderVO> list) {
		return R.status(orderService.distributeStore(list));
	}

}

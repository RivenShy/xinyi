package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.dto.PurchaseOrderDTO;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;
import org.xyg.eshop.main.service.IPurchaseOrderCommodityService;
import org.xyg.eshop.main.service.IPurchaseOrderService;
import org.xyg.eshop.main.vo.ContractVO;
import org.xyg.eshop.main.vo.PurchaseOrderCommodityVO;
import org.xyg.eshop.main.vo.PurchaseOrderVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.xyg.eshop.main.constants.EShopMainConstant.ESCRM_PURCHASE_ORDER_APPROVAL;

@RestController
@RequestMapping("/purchaseOrder")
@Api(value = "采购订单",tags = "采购订单")
@Slf4j
public class PurchaseOrderController extends RabbitController {

	@Autowired
	private IPurchaseOrderService purchaseOrderService;

	@Autowired
	private AutoIncrementIDGenerator autoIncrementIDGenerator;

	@Autowired
	private IPurchaseOrderCommodityService purchaseOrderCommodityService;

	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入 purchaseOrder")
	public R<Boolean> save(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
		return purchaseOrderService.saveOrUpdate(purchaseOrderDTO, "save");
	}

	/**
	 * 自定义分页
	 * @param query
	 * @param purchaseOrderDTO
	 * @return
	 */
	@GetMapping("/getPage")
	@ApiOperation(value = "分页条件查询")
	public R<IPage<PurchaseOrderVO>> getPage(Query query, PurchaseOrderDTO purchaseOrderDTO) {
		return R.data(purchaseOrderService.getPage(Condition.getPage(query), purchaseOrderDTO));
	}

	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "查询详情", notes = "传入 id")
	public R<PurchaseOrderVO> getDetail(@RequestParam("id") Long id) {
		return purchaseOrderService.selectById(id);
	}

	@GetMapping("/getPurchaseOrderCommodityList")
	@ApiOperation(value = "查询采购订单商品", notes = "传入采购订单id")
	public R<List<PurchaseOrderCommodityVO>> getPurchaseOrderCommodityList(@RequestParam("id") Long id) {
		return purchaseOrderService.selectPurchaseOrderCommodityList(id);
	}

	@PostMapping("/purchaseOrderSaveExecutionStartCallback")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "流程实例任务结束回调接口")
	public R<CallBackMethodResDto> purchaseOrderSaveExecutionStartCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			log.info("合同提交任务开始回调接口");
			return R.data(purchaseOrderService.flowInstanceExecutionStartCallback(inDto));
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	@PostMapping("/purchaseOrderSaveExecutionEndCallback")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "流程实例任务结束回调接口")
	public R<CallBackMethodResDto> purchaseOrderSaveExecutionEndCallback(@RequestBody CallbackMethodReqDto inDto) {
		try {
			log.info("合同提交任务结束回调接口");
			return R.data(purchaseOrderService.flowInstanceExecutionEndCallback(inDto));
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return R.fail(e.getMessage());
		}
	}

	@PostMapping("/submit")
	@ApiOperation(value = "提交采购管理", notes = "传入 purchaseOrder")
	public R<Boolean> submit(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
		return purchaseOrderService.saveOrUpdate(purchaseOrderDTO, "submit");
	}

	@PostMapping("/deletePurchaseOrderCommodity")
	@ApiOperation(value = "删除采购商品", notes = "传入 id")
	public R<Boolean> deletePurchaseOrderCommodity(@RequestParam("id") Long id) {
		return R.status(purchaseOrderCommodityService.removeById(id));
	}
}

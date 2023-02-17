package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.Func;
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
import org.xyg.eshop.main.vo.PurchaseOrderVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.xyg.eshop.main.constants.EShopMainConstant.ESCRM_PURCHASE_ORDER_APPROVAL;

@RestController
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController {

	@Autowired
	private IPurchaseOrderService purchaseOrderService;

	@Autowired
	private AutoIncrementIDGenerator autoIncrementIDGenerator;

	@Autowired
	private IPurchaseOrderCommodityService purchaseOrderCommodityService;

	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入 purchaseOrder")
	public R<Boolean> save(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
		return purchaseOrderService.saveOrUpdate(purchaseOrderDTO);
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
	public R<PurchaseOrderVO> getDetail(@RequestParam Long id) {
		return purchaseOrderService.selectById(id);
	}
}

package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.Func;
import org.xyg.eshop.main.dto.PurchaseWarehouseDTO;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.PurchaseWarehouse;
import org.xyg.eshop.main.service.IPurchaseWarehouseService;
import org.xyg.eshop.main.vo.PurchaseWarehouseVO;

@RestController
@RequestMapping("/purchaseWarehouse")
@Api(value = "采购入库",description = "采购入库")
public class PurchaseWarehouseController {

	@Autowired
	private IPurchaseWarehouseService purchaseWarehouseService;

	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入 purchaseWarehouseDTO")
	public R<Boolean> save(@RequestBody PurchaseWarehouseDTO purchaseWarehouseDTO) {
		return purchaseWarehouseService.save(purchaseWarehouseDTO);
	}

	@GetMapping("/getPage")
	@ApiOperation(value = "分页查询")
	public R<IPage<PurchaseWarehouse>> getPage(Query query, PurchaseWarehouseDTO purchaseWarehouseDTO) {
		return purchaseWarehouseService.getPage(query, purchaseWarehouseDTO);
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "查询详情", notes = "传入 id")
	public R<PurchaseWarehouseVO> getDetail(@RequestParam Long id) {
		return purchaseWarehouseService.selectById(id);
	}
}

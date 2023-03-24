package org.xyg.eshop.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.mp.support.Condition;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.service.IInventoryAlertService;
import org.xyg.eshop.main.vo.InventoryAlertVO;

@RestController
@AllArgsConstructor
@RequestMapping("/inventoryAlert")
@Api(value = "库存预警",tags = "库存预警")
public class InventoryAlertController extends RabbitController {

    private final IInventoryAlertService alertService;

    @GetMapping("/getPage")
    @ApiOperation(value = "分页列表",notes = "分页列表")
    public R<IPage<InventoryAlertVO>> getPage(Query query,InventoryAlertVO alertVO){
        return R.data(alertService.getPage(Condition.getPage(query),alertVO));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存",notes = "保存")
    public R<String> save(@RequestBody InventoryAlertVO alertVO){
        return R.status(alertService.saveOrUpdate(alertVO));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改",notes = "修改")
    public R<String> update(@RequestBody InventoryAlertVO alertVO){
		return R.status(alertService.updateById(alertVO));
    }

}

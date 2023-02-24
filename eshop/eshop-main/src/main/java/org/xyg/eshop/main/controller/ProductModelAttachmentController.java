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
import org.xyg.eshop.main.entity.ProductModelAttachment;
import org.xyg.eshop.main.service.IProductModelAttachmentService;
import org.xyg.eshop.main.vo.ProductModelAttachmentVO;

import java.util.List;

@RestController
@RequestMapping("/productModelAttachment")
@Api(value = "产品库附件信息维护", tags = "产品库附件信息维护")
@AllArgsConstructor
public class ProductModelAttachmentController extends RabbitController {

	private final IProductModelAttachmentService productModelAttachmentService;

	@GetMapping("/getPage")
	@ApiOperation(value = "分页查询")
	public R<IPage<ProductModelAttachment>> getPage(Query query) {
		return productModelAttachmentService.getPage(Condition.getPage(query));
	}

	@PostMapping("/insertData")
	@ApiOperation("插入数据")
	public R<Boolean> insertData(@RequestBody ProductModelAttachment entity) {
		return productModelAttachmentService.insertData(entity);
	}

	@GetMapping("/deleteData")
	@ApiOperation(value = "删除数据")
	@ApiImplicitParam(value = "id", name = "id", required = true)
	public R<Boolean> deleteData(@RequestParam("id") Long id) {
		return productModelAttachmentService.deleteData(id);
	}

	@PutMapping("/updateData")
	@ApiOperation(value = "更新数据")
	public R<Boolean> updateData(@RequestBody ProductModelAttachment entity) {
		return productModelAttachmentService.updateDate(entity);
	}

	@GetMapping("/getDetail")
	@ApiOperation(value = "获取详情数据")
	@ApiImplicitParam(value = "id", name = "id", required = true)
	public R<ProductModelAttachmentVO> getDetail(@RequestParam("id") Long id) {
		return productModelAttachmentService.getDetail(id);
	}

	@GetMapping("/getList")
	@ApiOperation(value = "获取列表数据")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "headId", name = "头表ID"),
		@ApiImplicitParam(value = "factoryMode", name = "产品型号"),
	})
	public R<List<ProductModelAttachment>> getList(@RequestParam(value = "headId", required = false) Long headId, @RequestParam(value = "factoryMode", required = false) String factoryMode) {
		return productModelAttachmentService.getList(headId, factoryMode);
	}
}

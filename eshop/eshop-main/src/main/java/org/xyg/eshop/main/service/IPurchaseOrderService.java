package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.eshop.main.dto.PurchaseOrderDTO;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.vo.PurchaseOrderCommodityVO;
import org.xyg.eshop.main.vo.PurchaseOrderVO;

import java.util.List;

public interface IPurchaseOrderService {

	R<Boolean> saveOrUpdate(PurchaseOrderDTO purchaseOrderDTO, String saveOrSubmit);

	IPage<PurchaseOrderVO> getPage(IPage page, PurchaseOrderDTO purchaseOrderDTO);

	R<PurchaseOrderVO> selectById(Long id);

	CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto);

	CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto);

	R<List<PurchaseOrderCommodityVO>> selectPurchaseOrderCommodityList(Long id);
}

package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dto.PurchaseOrderDTO;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.vo.PurchaseOrderVO;

public interface IPurchaseOrderService {

	R<Boolean> saveOrUpdate(PurchaseOrderDTO purchaseOrderDTO);

	IPage<PurchaseOrderVO> getPage(IPage page, PurchaseOrderDTO purchaseOrderDTO);

	R<PurchaseOrderVO> selectById(Long id);
}

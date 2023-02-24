package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.support.Query;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dto.PurchaseWarehouseDTO;
import org.xyg.eshop.main.entity.PurchaseWarehouse;
import org.xyg.eshop.main.vo.PurchaseWarehouseVO;

public interface IPurchaseWarehouseService {
	R<Boolean> save(PurchaseWarehouseDTO purchaseWarehouseDTO);

	R<IPage<PurchaseWarehouse>> getPage(Query query, PurchaseWarehouseDTO purchaseWarehouseDTO);

	R<PurchaseWarehouseVO> selectById(Long id);
}

package org.xyg.eshop.main.vo;

import lombok.Data;
import org.xyg.eshop.main.entity.PurchaseWarehouse;
import org.xyg.eshop.main.entity.WarehouseCommodity;

import java.util.List;

@Data
public class PurchaseWarehouseVO extends PurchaseWarehouse {
	private static final long serialVersionUID = 1L;

	private List<WarehouseCommodity> warehouseCommodityList;
}

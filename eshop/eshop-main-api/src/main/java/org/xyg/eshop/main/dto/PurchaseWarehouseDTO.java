package org.xyg.eshop.main.dto;

import lombok.Data;
import org.xyg.eshop.main.entity.PurchaseWarehouse;
import org.xyg.eshop.main.entity.WarehouseCommodity;

import java.util.List;

@Data
public class PurchaseWarehouseDTO extends PurchaseWarehouse {
	private static final long serialVersionUID = 1L;

	private List<WarehouseCommodity> warehouseCommodityList;
}

package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;

import java.io.Serializable;
import java.util.List;

@Data
public class PurchaseOrderVO extends PurchaseOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<PurchaseOrderCommodity> purchaseOrderCommodityList;

	@ApiModelProperty(value = "采购订单状态")
	private String statusName;
}

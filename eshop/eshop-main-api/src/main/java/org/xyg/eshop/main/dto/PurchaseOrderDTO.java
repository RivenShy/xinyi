package org.xyg.eshop.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PurchaseOrderDTO extends PurchaseOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<PurchaseOrderCommodity> purchaseOrderCommodityList;
//	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date purchaseOrderDateStart;
//	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date purchaseOrderDateEnd;
	private List<String> purchaseOrderStatusList;
}

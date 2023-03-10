package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;

import java.util.List;

public interface PurchaseOrderCommodityMapper extends BaseMapper<PurchaseOrderCommodity> {

//	@SqlParser(filter=true)
	List<PurchaseOrderCommodity> selectListByPurchaseOrderId(Long id);
}

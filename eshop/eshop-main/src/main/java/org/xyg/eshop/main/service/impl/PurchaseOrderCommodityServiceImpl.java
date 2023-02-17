package org.xyg.eshop.main.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;
import org.xyg.eshop.main.mapper.PurchaseOrderCommodityMapper;
import org.xyg.eshop.main.mapper.PurchaseOrderMapper;
import org.xyg.eshop.main.service.IPurchaseOrderCommodityService;
import org.xyg.eshop.main.service.IPurchaseOrderService;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseOrderCommodityServiceImpl extends BaseServiceImpl<PurchaseOrderCommodityMapper, PurchaseOrderCommodity> implements IPurchaseOrderCommodityService {

}

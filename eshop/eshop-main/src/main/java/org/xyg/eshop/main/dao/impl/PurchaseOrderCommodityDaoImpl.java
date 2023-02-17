package org.xyg.eshop.main.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.dao.IPurchaseOrderCommodityDao;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;
import org.xyg.eshop.main.mapper.PurchaseOrderCommodityMapper;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseOrderCommodityDaoImpl extends BaseServiceImpl<PurchaseOrderCommodityMapper, PurchaseOrderCommodity> implements IPurchaseOrderCommodityDao {

}

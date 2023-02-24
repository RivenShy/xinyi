package org.xyg.eshop.main.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.dao.IPurchaseWarehouseDao;
import org.xyg.eshop.main.dao.IWarehouseCommodityDao;
import org.xyg.eshop.main.entity.PurchaseWarehouse;
import org.xyg.eshop.main.entity.WarehouseCommodity;
import org.xyg.eshop.main.mapper.PurchaseWarehouseMapper;
import org.xyg.eshop.main.mapper.WarehouseCommodityMapper;

@Slf4j
@Service
@AllArgsConstructor
public class WarehouseCommodityDaoImpl extends BaseServiceImpl<WarehouseCommodityMapper, WarehouseCommodity> implements IWarehouseCommodityDao {
}

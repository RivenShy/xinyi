package org.xyg.eshop.main.service.impl;

import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.entity.InventoryManagementLines;
import org.xyg.eshop.main.mapper.InventoryManagementLinesMapper;
import org.xyg.eshop.main.service.IInventoryManagementLinesService;

@Service
public class InventoryManagementLinesServiceImpl extends BaseServiceImpl<InventoryManagementLinesMapper,InventoryManagementLines> implements IInventoryManagementLinesService {
}

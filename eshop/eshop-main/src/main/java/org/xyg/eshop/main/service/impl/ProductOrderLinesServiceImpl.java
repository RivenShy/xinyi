package org.xyg.eshop.main.service.impl;

import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.entity.ProductOrderLines;
import org.xyg.eshop.main.mapper.ProductOrderLinesMapper;
import org.xyg.eshop.main.service.IProductOrderLinesService;

@Service
public class ProductOrderLinesServiceImpl extends BaseServiceImpl<ProductOrderLinesMapper, ProductOrderLines> implements IProductOrderLinesService {
}

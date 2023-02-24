package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xyg.eshop.main.entity.PurchaseWarehouse;
import org.xyg.eshop.main.entity.WarehouseCommodity;

@Mapper
public interface WarehouseCommodityMapper extends BaseMapper<WarehouseCommodity> {
}

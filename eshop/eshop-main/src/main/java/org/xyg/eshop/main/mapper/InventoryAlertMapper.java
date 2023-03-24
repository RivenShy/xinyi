package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.InventoryAlert;
import org.xyg.eshop.main.vo.InventoryAlertVO;

@Mapper
public interface InventoryAlertMapper extends BaseMapper<InventoryAlert> {

    IPage<InventoryAlertVO> getPage(IPage<InventoryAlertVO> page, @Param("alertVO") InventoryAlertVO alertVO);

}

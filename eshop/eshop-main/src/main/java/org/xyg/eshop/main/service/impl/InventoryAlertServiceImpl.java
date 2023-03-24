package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.entity.InventoryAlert;
import org.xyg.eshop.main.mapper.InventoryAlertMapper;
import org.xyg.eshop.main.service.IInventoryAlertService;
import org.xyg.eshop.main.vo.InventoryAlertVO;

@Service
public class InventoryAlertServiceImpl extends BaseServiceImpl<InventoryAlertMapper, InventoryAlert> implements IInventoryAlertService {

    @Override
    public IPage<InventoryAlertVO> getPage(IPage<InventoryAlertVO> page, InventoryAlertVO alertVO){
        return baseMapper.getPage(page,alertVO);
    }

}

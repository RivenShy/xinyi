package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.InventoryAlert;
import org.xyg.eshop.main.vo.InventoryAlertVO;

public interface IInventoryAlertService extends BaseService<InventoryAlert> {

    /**
     * 分页列表
     * @param page 分页参数
     * @param alertVO 搜索条件
     * @return
     */
    IPage<InventoryAlertVO> getPage(IPage<InventoryAlertVO> page, InventoryAlertVO alertVO);

}

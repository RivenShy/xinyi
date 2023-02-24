package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.InventoryManagement;
import org.xyg.eshop.main.vo.InventoryManagementVO;

import java.rmi.ServerException;

public interface IInventoryManagementService extends BaseService<InventoryManagement> {

    /**
     * 分页查询出入库管理列表
     * @param page 分页参数
     * @param inventoryManagementVO 搜索条件
     * @return
     */
	IPage<InventoryManagementVO> getPage(IPage<InventoryManagementVO> page, InventoryManagementVO inventoryManagementVO);

    /**
     * 保存出入库管理数据
     * @param inventoryManagementVO 出入库管理数据
     */
    Boolean saveInventoryManagement(InventoryManagementVO inventoryManagementVO);

    /**
     * 提交出入库管理数据
     * @param inventoryManagementVO 出入库管理数据
     * @return
     */
    Long submit(InventoryManagementVO inventoryManagementVO);

    /**
     * 发起流程
     * @param id 出入库管理id
     */
    void startProcess(Long id) throws ServerException;

    /**
     * 更新出入库管理数据
     * @param inventoryManagementVO 出入库管理数据
     * @return
     */
    Boolean updateInventoryManagement(InventoryManagementVO inventoryManagementVO);

    /**
     * 删除出入库管理数据
     * @param id id
     * @return
     */
    Boolean delete(Long id);

    /**
     * 删除行表数据
     * @param ids 行表id,多个逗号分割
     * @return
     */
    Boolean linesDelete(String ids);

}

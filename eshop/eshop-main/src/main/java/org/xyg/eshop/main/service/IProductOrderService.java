package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.ProductOrder;
import org.xyg.eshop.main.excel.order.ProductOrderExcel;
import org.xyg.eshop.main.vo.ProductOrderVO;

import java.util.List;

public interface IProductOrderService extends BaseService<ProductOrder> {

    /**
     * 分页列表
     * @param page 分页参数
     * @param orderVO 搜索条件
     * @return
     */
    IPage<ProductOrderVO> getPage(IPage<ProductOrderVO> page, ProductOrderVO orderVO);

    /**
     * 保存订单信息
     * @param orderVO 订单数据
     * @return
     */
    Long saveData(ProductOrderVO orderVO);

    /**
     * 提交订单信息
     * @param orderVO 订单数据
     * @return
     */
    Long submit(ProductOrderVO orderVO);

    /**
     * 详情
     * @param id id
     * @param processInstanceId 流程实例id
     * @return
     */
    ProductOrderVO detail(Long id, String processInstanceId);

    /**
     * 订单导出
     * @param orderVO 搜索条件
     * @return
     */
    List<ProductOrderExcel> getExcelList(ProductOrderVO orderVO);

    /**
     * 订单导入
     * @param orderExcelList 订单导入数据
     * @return
     */
	List<ProductOrderVO> importer(List<ProductOrderExcel> orderExcelList);

    /**
     * 删除行表数据
     * @param ids ids 多个逗号分隔
     * @return
     */
	Boolean linesDelete(String ids);

    /**
     * 分配门店
     * @param list 订单数据
     * @return
     */
    Boolean distributeStore(List<ProductOrderVO> list);

}

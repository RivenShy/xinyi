package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.ProductOrder;
import org.xyg.eshop.main.excel.order.ProductOrderExcel;
import org.xyg.eshop.main.vo.ProductOrderVO;

import java.util.List;

@Mapper
public interface ProductOrderMapper extends BaseMapper<ProductOrder> {

    IPage<ProductOrderVO> getPage(IPage<ProductOrderVO> page,@Param("orderVO") ProductOrderVO orderVO);

    List<ProductOrderExcel> getExcelList(@Param("orderVO") ProductOrderVO orderVO);

}

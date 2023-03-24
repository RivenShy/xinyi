package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.ProductInventory;
import org.xyg.eshop.main.vo.ProductInventoryVO;

import java.util.List;

@Mapper
public interface ProductInventoryMapper extends BaseMapper<ProductInventory> {

	List<ProductInventoryVO> getInventoryList(@Param("inventoryVO")ProductInventoryVO productInventoryVO);

	IPage<ProductInventoryVO> getPage(IPage<ProductInventoryVO> page,@Param("inventoryVO") ProductInventoryVO productInventoryVO);

    IPage<ProductInventoryVO> getMergePage(IPage<ProductInventoryVO> page,@Param("inventoryVO") ProductInventoryVO productInventoryVO);

}

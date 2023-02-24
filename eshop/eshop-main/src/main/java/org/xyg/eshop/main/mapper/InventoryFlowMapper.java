package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.InventoryFlow;
import org.xyg.eshop.main.vo.InventoryFlowVO;

@Mapper
public interface InventoryFlowMapper extends BaseMapper<InventoryFlow> {

	IPage<InventoryFlowVO> getPage(IPage<InventoryFlowVO> page,@Param("flowVO") InventoryFlowVO inventoryFlowVO);

}

package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.InventoryManagement;
import org.xyg.eshop.main.vo.InventoryManagementVO;

@Mapper
public interface InventoryManagementMapper extends BaseMapper<InventoryManagement> {

	IPage<InventoryManagementVO> getPage(IPage<InventoryManagementVO> page,
										 @Param("managementVO") InventoryManagementVO inventoryManagementVO);

}

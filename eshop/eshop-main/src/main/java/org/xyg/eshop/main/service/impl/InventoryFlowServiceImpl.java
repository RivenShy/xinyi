package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.entity.InventoryFlow;
import org.xyg.eshop.main.mapper.InventoryFlowMapper;
import org.xyg.eshop.main.service.IInventoryFlowService;
import org.xyg.eshop.main.vo.InventoryFlowVO;

@Service
public class InventoryFlowServiceImpl extends BaseServiceImpl<InventoryFlowMapper, InventoryFlow> implements IInventoryFlowService {

	@Override
	public IPage<InventoryFlowVO> getPage(IPage<InventoryFlowVO> page, InventoryFlowVO inventoryFlowVO){
		// 处理日期搜索条件
		processingDate(inventoryFlowVO);
		return baseMapper.getPage(page,inventoryFlowVO);
	}

	/**
	 * 处理日期搜索条件
	 * @param inventoryFlowVO 搜索条件
	 */
	private void processingDate(InventoryFlowVO inventoryFlowVO){
		String startDate = inventoryFlowVO.getStartDate();
		String endDate = inventoryFlowVO.getEndDate();

		if (StringUtil.isNotBlank(startDate)){
			startDate += " 00:00:00";
			inventoryFlowVO.setStartDate(startDate);
		}

		if (StringUtil.isNotBlank(endDate)){
			endDate += " 23:59:59";
			inventoryFlowVO.setEndDate(endDate);
		}
	}

}

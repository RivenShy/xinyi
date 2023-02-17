package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.dto.PurchaseOrderDTO;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;
import org.xyg.eshop.main.vo.ContractVO;
import org.xyg.eshop.main.vo.PurchaseOrderVO;

import java.util.List;

@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {

//	@SqlParser(filter=true)
	List<PurchaseOrderVO> selectPurchaseOrderPage(IPage page, @Param("purchaseOrder") PurchaseOrderDTO purchaseOrderDTO);
}

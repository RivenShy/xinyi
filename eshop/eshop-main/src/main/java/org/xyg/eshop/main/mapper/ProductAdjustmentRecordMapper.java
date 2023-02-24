package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.ProductAdjustmentRecord;
import org.xyg.eshop.main.vo.ProductAdjustmentRecordVO;

@Mapper
public interface ProductAdjustmentRecordMapper extends BaseMapper<ProductAdjustmentRecord> {

	IPage<ProductAdjustmentRecordVO> getToPriceListPage(IPage<ProductAdjustmentRecordVO> page,
										 				@Param("id") Long id,
										 				@Param("priceListId") Long priceListId,
														@Param("docnum") String docnum,
														@Param("erpStatus") String erpStatus);

}

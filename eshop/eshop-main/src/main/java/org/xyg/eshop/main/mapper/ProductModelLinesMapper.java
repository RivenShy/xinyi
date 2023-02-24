package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.ProductModelLines;
import org.xyg.eshop.main.excel.product.ProductModelLinesExportExcel;
import org.xyg.eshop.main.vo.ProductModelLinesVO;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProductModelLinesMapper extends BaseMapper<ProductModelLines> {
	ProductModelLinesVO getDetail(Long id) ;

	List<ProductModelLinesExportExcel> exportExcelList();

	LocalDateTime findMaxUpdate();

	List<ProductModelLines> getModelLines(@Param("xygType") String xygType ,
										  @Param("partyColor") String partyColor ,
										  @Param("attachment") String attachment ,
										  @Param("features") String features,
										  @Param("orgId") Integer orgId);

	List<ProductModelLines> getModelList(@Param("materialCodes") List<String> materialCodes,
										 @Param("materialIds") List<String> materialIds,
										 @Param("factoryModes") List<String> factoryModes);
}

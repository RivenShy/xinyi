package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.ProductPriceListLines;
import org.xyg.eshop.main.excel.ProductPriceListExportExcel;

import java.util.List;

/**
 * @author lianghaichang
 */
@Mapper
public interface ProductPriceListLinesMapper extends BaseMapper<ProductPriceListLines> {

	List<ProductPriceListExportExcel> exportPriceListLines(@Param("lines")ProductPriceListLines lines);

}

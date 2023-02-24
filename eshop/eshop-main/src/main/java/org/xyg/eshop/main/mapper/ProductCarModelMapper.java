package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.ProductCarModel;
import org.xyg.eshop.main.entity.ProductModelLines;
import org.xyg.eshop.main.vo.ProductCarModelVO;
import org.xyg.eshop.main.vo.ProductModelLinesToPrictListVO;

import java.util.List;

@Mapper
public interface ProductCarModelMapper extends BaseMapper<ProductCarModel> {
	ProductCarModelVO getDetail(Long id) ;

	IPage<ProductCarModelVO> getProductPage(IPage<ProductModelLines> iPage,@Param("param") ProductCarModelVO param);

	IPage<ProductCarModelVO> getProductPageEn(IPage<ProductModelLines> iPage,
											@Param("factoryMode") String factoryMode ,
											@Param("productName")String productName ,
											@Param("carFactory")String carFactory ,
											@Param("xygType")String xygType ,
											@Param("europeanModels")String europeanModels ,
											@Param("usModels")String usModels ,
											@Param("specifications")String specifications,
											@Param("model")String model,
											@Param("easySearch") String easySearch,
											@Param("productLineId") Long productLineId,
											@Param("easySearchEn") String easySearchEn,
											@Param("width") Long width,
											@Param("length") Long length,
											@Param("organizationIds") String organizationIds);

	List<ProductCarModelVO> getProductList(@Param("productLineId") Long productLineId ,
										   @Param("xygType") String xygType ,
										   @Param("attachment") String attachment ,
										   @Param("colour") String colour);

	List<ProductCarModel> findProductCarModel();

	IPage<ProductModelLinesToPrictListVO> getToPriceListPage(IPage<ProductModelLinesToPrictListVO> page,
															 @Param("lines") ProductModelLinesToPrictListVO linesToPrictListVO);

}

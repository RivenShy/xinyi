package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.ProductPriceList;
import org.xyg.eshop.main.vo.ProductPriceListTreeVO;
import org.xyg.eshop.main.vo.ProductPriceListVO;

import java.util.List;

@Mapper
public interface ProductPriceListMapper extends BaseMapper<ProductPriceList> {

	List<ProductPriceListTreeVO> getTreeList(@Param("parentId") Long parentId,
											 @Param("priceListName") String priceListName,
											 @Param("type")Integer type,
											 @Param("regionOrPartyName")String regionOrPartyName);

	List<ProductPriceList> getList(@Param("parentId") Long parentId,
								   @Param("priceListName") String priceListName,
								   @Param("type")Integer type,
								   @Param("regionOrPartyName")String regionOrPartyName,
								   @Param("id") Long id ,
								   @Param("storefrontType") String storefrontType,
								   @Param("isValid") Integer isValid);

    IPage<ProductPriceListVO> getByTypePage(IPage<ProductPriceListVO> page, @Param("type") Integer type, @Param("priceListName") String priceListName);

}

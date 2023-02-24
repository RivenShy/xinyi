package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.vo.StorefrontVO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface StorefrontMapper extends BaseMapper<Storefront> {

	LocalDateTime findMaxUpdateDate();

	StorefrontVO selectByPotentialPartyId(@Param("partyId") Long partyId);

	IPage<Storefront> syncParty(IPage<Storefront> page, @Param("partyId") List<Long> partyId , @Param("start") Date start, @Param("end") Date end);

	IPage<StorefrontVO> getPage(IPage<StorefrontVO> page,@Param("storefrontVO") StorefrontVO storefrontVO);

	IPage<StorefrontVO> getAllByPage(@Param("companyLogo") String companyLogo,@Param("partyName") String partyName, @Param("status")String status,@Param("partyLevel") Integer partyLevel,@Param("salesrepName") String salesrepName, @Param("saleAreaName")String saleAreaName, @Param("startDate")String startDate , @Param("endDate")String endDate, @Param("salesType") String salesType , @Param("partyShortName") String partyShortName , IPage<StorefrontVO> page);

}

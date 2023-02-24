package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.StorefrontFranchise;
import org.xyg.eshop.main.query.StorefrontFranchiseQuery;
import org.xyg.eshop.main.vo.StorefrontFranchiseVO;

/**
 * @author ww
 * @description 针对表【ESHOP_STORE_FRANCHISE(易车-加盟店主数据)】的数据库操作Mapper
 * @createDate 2023-01-09 14:20:40
 * @Entity org.springrabbit.eshop.entity.StoreFranchise
 */
@Mapper
public interface StorefrontFranchiseMapper extends BaseMapper<StorefrontFranchise> {

	/**
	 * 查询加盟店分页数据
	 *
	 * @param page  分页
	 * @param query 查询条件
	 * @return IPage<StoreFranchiseVO>
	 */
	IPage<StorefrontFranchiseVO> findPage(IPage<StorefrontFranchiseVO> page, @Param("query") StorefrontFranchiseQuery query);
}





package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.dto.StorefrontAcceptanceDTO;
import org.xyg.eshop.main.entity.StorefrontAcceptance;
import org.xyg.eshop.main.vo.StorefrontAcceptanceVO;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ww
 * @description 针对表【eshop_storefront_acceptance(易车-门店验收主数据)】的数据库操作Service
 * @createDate 2023-01-11 10:53:10
 */
public interface IStorefrontAcceptanceService extends BaseService<StorefrontAcceptance> {

	/**
	 * 根据头表id查询验收集合数据
	 *
	 * @param headId 头id
	 * @return List<StorefrontAcceptanceVO>
	 */
	List<StorefrontAcceptanceVO> getList(@NotNull(message = "头id不能为空") Long headId);

	/**
	 * 新增或更新数据
	 *
	 * @param entity 验收数据
	 * @return StorefrontAcceptanceVO
	 */
	StorefrontAcceptanceVO save(StorefrontAcceptanceDTO entity);

	/**
	 * 根据主键id查询详情
	 *
	 * @param id 主键id
	 * @return StorefrontAcceptanceVO
	 */
	StorefrontAcceptanceVO detail(@NotNull(message = "主键id不能为空") Long id);
}

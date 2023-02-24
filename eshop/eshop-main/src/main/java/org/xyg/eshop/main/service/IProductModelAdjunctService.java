package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.ProductModelAdjunct;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 技术图纸信息业务接口
 */
public interface IProductModelAdjunctService extends BaseService<ProductModelAdjunct> {

	/**
	 * 获取技术图纸集合数据
	 *
	 * @param technologyId   技术资料id
	 * @param organizationId 库存组织ID
	 * @return
	 */
	List<ProductModelAdjunct> getList(Long technologyId, Long organizationId, String adjunct);

	/**
	 * 获取最大的更新日期
	 *
	 * @return {@link LocalDateTime}
	 */
	LocalDateTime findMaxUpdateDate();

}

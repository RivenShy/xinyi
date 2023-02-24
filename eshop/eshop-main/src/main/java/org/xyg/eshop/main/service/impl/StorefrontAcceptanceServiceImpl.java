package org.xyg.eshop.main.service.impl;

import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.DateUtil;
import org.xyg.ehop.common.constants.EshopConstants;
import org.xyg.eshop.main.dto.StorefrontAcceptanceDTO;
import org.xyg.eshop.main.entity.StorefrontAcceptance;
import org.xyg.eshop.main.mapper.StorefrontAcceptanceMapper;
import org.xyg.eshop.main.service.IStorefrontAcceptanceService;
import org.xyg.eshop.main.vo.StorefrontAcceptanceVO;

import java.util.Collections;
import java.util.List;

/**
 * @author ww
 * @description 针对表【eshop_storefront_acceptance(易车-门店验收主数据)】的数据库操作Service实现
 * @createDate 2023-01-11 10:53:10
 */
@Service
public class StorefrontAcceptanceServiceImpl extends BaseServiceImpl<StorefrontAcceptanceMapper, StorefrontAcceptance>
	implements IStorefrontAcceptanceService {

	@Override
	public List<StorefrontAcceptanceVO> getList(Long headId) {
		List<StorefrontAcceptance> acceptances = this.lambdaQuery()
			.eq(StorefrontAcceptance::getHeadId, headId)
			.orderByDesc(StorefrontAcceptance::getAcceptanceDate)
			.list();
		if (CollectionUtil.isEmpty(acceptances)) {
			return Collections.emptyList();
		}
		return BeanUtil.copyProperties(acceptances, StorefrontAcceptanceVO.class);
	}

	@Override
	public StorefrontAcceptanceVO save(StorefrontAcceptanceDTO entity) {
		StorefrontAcceptanceVO vo = new StorefrontAcceptanceVO();
		BeanUtil.copyProperties(entity, vo);
		vo.setStatus(EshopConstants.STATUS_NORMAL);
		if (entity.getId() == null) {
			create(vo);
		} else {
			this.updateById(vo);
		}
		return vo;
	}

	@Override
	public StorefrontAcceptanceVO detail(Long id) {
		StorefrontAcceptanceVO vo = new StorefrontAcceptanceVO();
		StorefrontAcceptance acceptance = this.getById(id);
		BeanUtil.copyProperties(acceptance, vo);
		return vo;
	}

	private void create(StorefrontAcceptanceVO vo) {
		if (vo.getAcceptanceDate() == null) {
			// 获取当前日期
			vo.setAcceptanceDate(DateUtil.now());
		}
		this.save(vo);
	}

}





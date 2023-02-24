package org.xyg.eshop.main.wrapper;

import org.springrabbit.core.mp.support.BaseEntityWrapper;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.xyg.eshop.main.dto.StorefrontFranchiseDTO;
import org.xyg.eshop.main.vo.StorefrontFranchiseVO;

public class StoreFranchiseDtoWrapper extends BaseEntityWrapper<StorefrontFranchiseDTO, StorefrontFranchiseVO> {

	public static StoreFranchiseDtoWrapper build() {
		return new StoreFranchiseDtoWrapper();
	}

	@Override
	public StorefrontFranchiseVO entityVO(StorefrontFranchiseDTO entity) {
		return BeanUtil.copyProperties(entity, StorefrontFranchiseVO.class);
	}
}

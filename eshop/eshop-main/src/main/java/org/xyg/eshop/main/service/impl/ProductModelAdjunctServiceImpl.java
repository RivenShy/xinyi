package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.entity.ProductModelAdjunct;
import org.xyg.eshop.main.mapper.ProductModelAdjunctMapper;
import org.xyg.eshop.main.service.IProductModelAdjunctService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductModelAdjunctServiceImpl extends BaseServiceImpl<ProductModelAdjunctMapper, ProductModelAdjunct>
	implements IProductModelAdjunctService {

	@Override
	public List<ProductModelAdjunct> getList(Long technologyId, Long organizationId , String adjunct) {
		LambdaQueryChainWrapper<ProductModelAdjunct> wrapper = super.lambdaQuery();
		if (technologyId != null) {
			wrapper.eq(ProductModelAdjunct::getTechnologyId, technologyId);
		}
		if (organizationId != null) {
			wrapper.eq(ProductModelAdjunct::getOrganizationId, organizationId);
		}
		if(StringUtil.isNotBlank(adjunct)){
			wrapper.eq(ProductModelAdjunct::getFileTypeDescription,adjunct);
		}
		return wrapper.list();
	}

	@Override
	public LocalDateTime findMaxUpdateDate() {
		return getBaseMapper().findMaxUpdateDate();
	}
}

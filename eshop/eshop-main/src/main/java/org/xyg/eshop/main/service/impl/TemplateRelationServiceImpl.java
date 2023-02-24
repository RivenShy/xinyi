package org.xyg.eshop.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.xyg.eshop.main.entity.TemplateRelation;
import org.xyg.eshop.main.mapper.TemplateRelationMapper;
import org.xyg.eshop.main.service.ITemplateRelationService;

import java.util.List;

@Slf4j
@Service
public class TemplateRelationServiceImpl extends BaseServiceImpl<TemplateRelationMapper, TemplateRelation> implements ITemplateRelationService {

	@Override
	public List<TemplateRelation> saveData(List<TemplateRelation> param){
		if (CollectionUtil.isEmpty(param)) {
			log.error("模板关联数据 is null: {}", param);
			throw new ServiceException("模板关联数据不能为空");
		}
		saveOrUpdateBatch(param);
		return param;
	}

	@Override
	public List<TemplateRelation> getRelationByHeadIdList(Long headId){
		return lambdaQuery().eq(TemplateRelation::getHeadId,headId).list();
	}

}

package org.xyg.eshop.main.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.entity.TemplateProptiesLine;
import org.xyg.eshop.main.mapper.TemplateProptiesLineMapper;
import org.xyg.eshop.main.service.ITemplateDataLineService;
import org.xyg.eshop.main.service.ITemplateProptiesLineService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TemplateProptiesLineServiceImpl extends BaseServiceImpl<TemplateProptiesLineMapper, TemplateProptiesLine> implements ITemplateProptiesLineService {

	private final ITemplateDataLineService dataLineService;

	@Override
	public List<TemplateProptiesLine> getByTemplateId(Long templateId, Integer type, String language){
		List<TemplateProptiesLine> proptiesLineList = lambdaQuery()
			.eq(TemplateProptiesLine::getTemplateId,templateId)
			.eq(type != null,TemplateProptiesLine::getType,type)
			.eq(StringUtil.isNotBlank(language),TemplateProptiesLine::getLanguage,language)
			.orderByAsc(TemplateProptiesLine::getSort)
			.list();

		if (CollectionUtil.isEmpty(proptiesLineList)){
			return new ArrayList<>(1);
		}

		proptiesLineList.forEach(propties -> {
			propties.setDataLineList(dataLineService.getByProptiesId(propties.getId()));
		});

		return proptiesLineList;
	}

}

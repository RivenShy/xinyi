package org.xyg.eshop.main.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.base.DBEntity;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.entity.Template;
import org.xyg.eshop.main.entity.TemplateProptiesLine;
import org.xyg.eshop.main.mapper.TemplateMapper;
import org.xyg.eshop.main.service.ITemplateProptiesLineService;
import org.xyg.eshop.main.service.ITemplateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TemplateServiceImpl extends BaseServiceImpl<TemplateMapper, Template> implements ITemplateService {

	private final ITemplateProptiesLineService proptiesLineService;

	@Override
	public Template detail(Long id,String templateName, Integer type, String language){
		Template template = lambdaQuery()
			.eq(id != null, DBEntity::getId,id)
			.eq(StringUtil.isNotBlank(templateName),Template::getTemplateName,templateName)
			.one();

		if (template == null){
			return null;
		}

		List<TemplateProptiesLine> proptiesLineList = proptiesLineService.getByTemplateId(id,type,language);
		template.setProptiesLineMap(buildProptiesLineData(proptiesLineList));
		return template;
	}

	/**
	 * 根据大纲分类
	 * @param problemLines 字段
	 * @return
	 */
	private Map<String, List<TemplateProptiesLine>> buildProptiesLineData(List<TemplateProptiesLine> problemLines) {
		if (CollectionUtil.isEmpty(problemLines)) {
			return new HashMap<>(1);
		}
		Map<String, List<TemplateProptiesLine>> result = new HashMap<>();
		List<String> outlines = problemLines.stream().map(TemplateProptiesLine::getOutline).filter(StringUtil::isNotBlank).distinct().collect(Collectors.toList());
		for (String outline : outlines) {
			List<TemplateProptiesLine> lines = problemLines.stream().filter(v -> StringUtil.isNotBlank(v.getOutline()) && outline.equals(v.getOutline())).collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(lines)) {
				result.put(outline, lines);
			}
		}
		return result;
	}

}

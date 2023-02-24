package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.TemplateProptiesLine;

import java.util.List;

public interface ITemplateProptiesLineService extends BaseService<TemplateProptiesLine> {

	/**
	 * 根据模板id获取模板字段
	 * @param templateId 模板id
	 * @param type 类型
	 * @param language 语言
	 * @return
	 */
    List<TemplateProptiesLine> getByTemplateId(Long templateId, Integer type, String language);

}

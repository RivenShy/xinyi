package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.TemplateDataLine;

import java.util.List;

public interface ITemplateDataLineService extends BaseService<TemplateDataLine> {

	/**
	 * 根据模板字段查询模板数据
	 * @param proptiesId 模板属性id
	 * @return
	 */
    List<TemplateDataLine> getByProptiesId(Long proptiesId);

}

package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.Template;

public interface ITemplateService extends BaseService<Template> {

	/**
	 * 获取模板详情
	 * @param id 模板id
	 * @param templateName 模板名称
	 * @param type 填写类型 0 填写， 1 单选框，2 复选框，3 下拉框，4 文件上传，5 文本框
	 * @param language 语言
	 * @return
	 */
	Template detail(Long id, String templateName, Integer type, String language);

}

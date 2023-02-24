package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.TemplateRelation;

import java.util.List;

public interface ITemplateRelationService extends BaseService<TemplateRelation> {

	/**
	 * 新增或修改模板关联数据
	 * @param param 模板数据
	 * @return
	 */
	List<TemplateRelation> saveData(List<TemplateRelation> param);

	/**
	 * 根据头id获取模板数据列表
	 * @param headId 头id
	 * @return
	 */
	List<TemplateRelation> getRelationByHeadIdList(Long headId);

}

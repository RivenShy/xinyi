package org.xyg.eshop.main.service.impl;

import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.xyg.eshop.main.entity.TemplateDataLine;
import org.xyg.eshop.main.mapper.TemplateDataLineMapper;
import org.xyg.eshop.main.service.ITemplateDataLineService;

import java.util.List;

@Service
public class TemplateDataLineServiceImpl extends BaseServiceImpl<TemplateDataLineMapper, TemplateDataLine> implements ITemplateDataLineService {

	@Override
	public List<TemplateDataLine> getByProptiesId(Long proptiesId){
		return lambdaQuery().eq(TemplateDataLine::getProptiesId,proptiesId).orderByAsc(TemplateDataLine::getSort).list();
	}

}

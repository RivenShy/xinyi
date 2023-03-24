package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springrabbit.common.utils.I18nUtils;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.erpentity.ErpCountry;
import org.xyg.eshop.main.mapper.ErpCountryMapper;
import org.xyg.eshop.main.service.IErpCountryService;

import java.util.List;

@Service
public class ErpCountryServiceImpl extends ServiceImpl<ErpCountryMapper, ErpCountry> implements IErpCountryService {

	@Override
	public List<ErpCountry> findByName(String name, String language) {
		if ("a".equals(language)) {
			return lambdaQuery().eq(ErpCountry::getCountryName, name)
				.eq(ErpCountry::getLanguage, "ZHS").list();
		}
		if (StringUtil.isBlank(language)) {
			language = "ZHS";
		}
		List<ErpCountry> list = lambdaQuery().eq(ErpCountry::getCountryName, name)
			.eq(ErpCountry::getLanguage, language).list();
		if (CollectionUtil.isEmpty(list)) {
			if (name.length() > 2) {
				name = name.substring(0, 2);
			}
			list = lambdaQuery().likeRight(ErpCountry::getCountryName, name)
				.eq(ErpCountry::getLanguage, language).list();
		}
		return list;
	}

	@Override
	public List<ErpCountry> getList(String name, String ids,int leaf){
		QueryWrapper<ErpCountry> wrapper = new QueryWrapper<>();
		String lang = I18nUtils.getCurrentLang();
		lang = lang.startsWith("zh") ? "ZHS" : "US";
		wrapper.select("country_code", "country_name", "obsolete_flag", "language",
				leaf > 0 ? "(select count(1) from erp_provinces where erp_countries.country_code =erp_provinces.country_code and erp_countries.language =erp_provinces.language and ROWNUM =1 ) has_children" : "1");
		wrapper.eq("language", lang);
		if (StringUtil.isNotBlank(ids)) {
			String[] values = ids.split(",");
			wrapper.in("country_code", values);
			return list(wrapper);
		}
		wrapper.eq("obsolete_flag", "N");
		wrapper.like(StringUtil.isNotBlank(name),"country_name", name);
		Page<ErpCountry> page = new Page<>(1, 300);
		Page<ErpCountry> pageResult = page(page, wrapper);
		return pageResult.getRecords();
	}
}

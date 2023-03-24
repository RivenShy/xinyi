package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springrabbit.common.utils.I18nUtils;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.erpentity.ErpCity;
import org.xyg.eshop.main.mapper.ErpCityMapper;
import org.xyg.eshop.main.service.IErpCityService;

import java.util.List;

@Service
public class ErpCityServiceImpl extends ServiceImpl<ErpCityMapper, ErpCity> implements IErpCityService {

	@Override
	public List<ErpCity> findByName(String name, String language) {
		if ("a".equals(language)) {
			return lambdaQuery().eq(ErpCity::getCityName, name)
				.eq(ErpCity::getLanguage, "ZHS").list();
		}
		if (StringUtil.isBlank(language)) {
			language = "ZHS";
		}
		List<ErpCity> list = lambdaQuery().eq(ErpCity::getCityName, name)
			.eq(ErpCity::getLanguage, language).list();
		if (CollectionUtil.isEmpty(list)) {
			if (name.length() > 2) {
				name = name.substring(0, 2);
			}
			list = lambdaQuery().likeRight(ErpCity::getCityName, name)
				.eq(ErpCity::getLanguage, language).list();
		}
		return list;
	}

	@Override
	public List<ErpCity> getList(String name, String province, String ids, int leaf){
		QueryWrapper<ErpCity> wrapper = new QueryWrapper<>();
		wrapper.select("city_code", "province_code", "city_name", "enable_flag", "language",
				leaf > 0 ? "(select count(1) from erp_areas where erp_cities.city_code =erp_areas.city_code and erp_cities.language =erp_areas.language and ROWNUM =1 ) has_children" : "1");
		String lang = I18nUtils.getCurrentLang();
		lang = lang.startsWith("zh") ? "ZHS" : "US";
		wrapper.eq("language", lang);
		if (StringUtil.isNotBlank(ids)) {
			String[] values = ids.split(",");
			wrapper.in("city_code", values);
			list(wrapper);
		}
		wrapper.eq("enable_flag", "Y");
		wrapper.eq(StringUtil.isNotBlank(province),"province_code", province);
		wrapper.like(StringUtil.isNotBlank(name),"city_name", name);
		Page<ErpCity> page = new Page<>(1, 300);
		Page<ErpCity> pageResult = page(page, wrapper);
		return pageResult.getRecords();
	}
}

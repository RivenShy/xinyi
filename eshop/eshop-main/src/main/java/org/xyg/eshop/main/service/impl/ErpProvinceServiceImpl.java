package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springrabbit.common.utils.I18nUtils;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.erpentity.ErpProvince;
import org.xyg.eshop.main.mapper.ErpProvinceMapper;
import org.xyg.eshop.main.service.IErpProvinceService;

import java.util.List;

@Service
public class ErpProvinceServiceImpl extends ServiceImpl<ErpProvinceMapper, ErpProvince> implements IErpProvinceService {

	@Override
	public List<ErpProvince> findByName(String name, String language) {
		if ("a".equals(language)) {
			return lambdaQuery().eq(ErpProvince::getProvinceName, name)
				.eq(ErpProvince::getLanguage, "ZHS").list();
		}
		if (StringUtil.isBlank(language)) {
			language = "ZHS";
		}
		List<ErpProvince> list = lambdaQuery().eq(ErpProvince::getProvinceName, name)
			.eq(ErpProvince::getLanguage, language).list();
		if (CollectionUtil.isEmpty(list)) {
			if (name.length() > 2) {
				name = name.substring(0, 2);
			}
			list = lambdaQuery().likeRight(ErpProvince::getProvinceName, name)
				.eq(ErpProvince::getLanguage, language).list();
		}
		return list;
	}

	@Override
	public List<ErpProvince> getList(String name, String country,String ids,int leaf){
		QueryWrapper<ErpProvince> wrapper = new QueryWrapper<>();
		wrapper.select("country_code", "province_code", "province_name", "enable_flag", "language",
				leaf > 0 ? "(select count(1) from erp_cities where erp_cities.province_code =erp_provinces.province_code and erp_cities.language =erp_provinces.language and ROWNUM =1 ) has_children" : "1");
		String lang = I18nUtils.getCurrentLang();
		lang = lang.startsWith("zh") ? "ZHS" : "US";
		wrapper.eq("language", lang);
		if (StringUtil.isNotBlank(ids)) {
			String[] values = ids.split(",");
			wrapper.in("province_code", values);
			return list(wrapper);
		}
		wrapper.eq("enable_flag", "Y")
				.eq(StringUtil.isNotBlank(country),"country_code", country)
				.like(StringUtil.isNotBlank(name),"province_name", name);
		Page<ErpProvince> page = new Page<>(1, 300);
		Page<ErpProvince> pageResult = page(page, wrapper);
		return pageResult.getRecords();
	}
}

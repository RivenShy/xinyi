package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springrabbit.common.utils.I18nUtils;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.erpentity.ErpArea;
import org.xyg.eshop.main.mapper.ErpAreaMapper;
import org.xyg.eshop.main.service.IErpAreaService;
import org.xyg.eshop.main.vo.ErpLocationVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ErpAreaServiceImpl extends ServiceImpl<ErpAreaMapper, ErpArea> implements IErpAreaService {

	@Override
	public List<ErpLocationVO> findAddressByArea(String codes, String language) {
		return getBaseMapper().findAddressByArea(codes, language);
	}

	@Override
	public boolean save(ErpArea entity) {
		log.warn("新增地区: " + entity.toString());
		return super.save(entity);
	}

	@Override
	public List<ErpArea> findByNameAndProvinceOrCity(String provinceCode, String cityCode, String name, String language, String adcode) {
		List<ErpArea> list;
		if (StringUtil.isNotBlank(cityCode)) {
			QueryWrapper<ErpArea> wrapper = new QueryWrapper<>();
			wrapper.eq("city_code", cityCode);
			wrapper.eq("replace(area_name,' ','')", name);
			wrapper.eq("language", language);
			list = list(wrapper);
		} else if (StringUtil.isNotBlank(provinceCode)) {
			list = baseMapper.findListByNameAndProvince(provinceCode, name, language);
		} else {
			QueryWrapper<ErpArea> wrapper = new QueryWrapper<>();
			wrapper.eq("replace(area_name,' ','')", name);
			wrapper.eq("language", language);
			list = list(wrapper);
		}
		if (CollectionUtil.isEmpty(list) && adcode != null && adcode.length() == 6) {
			ErpArea erpArea = new ErpArea();
			erpArea.setAreaName(name);
			erpArea.setAreaCode(adcode);
			if (StringUtil.isBlank(cityCode)) {
				erpArea.setCityCode(adcode.substring(0, 4) + "00");
			} else {
				erpArea.setCityCode(cityCode);
			}
			erpArea.setLanguage(language);
			erpArea.setEnableFlag("Y");
			erpArea.setLastUpdateDate(new Date());
			save(erpArea);
			list = new ArrayList<>(2);
			list.add(erpArea);
		}
		return list;
	}

	@Override
	public List<ErpArea> getList(String name, String city, String ids){
		QueryWrapper<ErpArea> wrapper = new QueryWrapper<>();
		String lang = I18nUtils.getCurrentLang();
		wrapper.eq("language", lang.startsWith("zh") ? "ZHS" : "US");
		if (StringUtil.isNotBlank(ids)) {
			String[] values = ids.split(",");
			wrapper.in("area_code", values);
			return list(wrapper);
		}
		wrapper.eq("enable_flag", "Y");
		wrapper.eq(StringUtil.isNotBlank(city),"city_code", city);
		wrapper.like(StringUtil.isNotBlank(name),"area_name", name);
		Page<ErpArea> page = new Page<>(1, 300);
		Page<ErpArea> pageResult = page(page, wrapper);
		return pageResult.getRecords();
	}

}

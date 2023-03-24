package org.xyg.eshop.main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springrabbit.common.utils.I18nUtils;
import org.springrabbit.core.boot.ctrl.RabbitController;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.erpentity.*;
import org.xyg.eshop.main.service.*;
import org.xyg.eshop.main.vo.ErpLocationVO;

import java.util.Date;
import java.util.List;

@Api(value = "erp", tags = "erp查询和导入相关")
@RequestMapping("/erp")
@RestController
@AllArgsConstructor
public class ErpController extends RabbitController {

	private final IErpBusinessEntityService erpBusinessEntityService;

	private final IErpCountryService erpCountryService;

	private final IErpCityService erpCityService;

	private final IErpAreaService erpAreaService;

	private final IErpProvinceService erpProvinceService;

	@ApiOperation(value = "查询业务实体", notes = "传入name模糊查询, 传入ids回显")
	@ApiImplicitParams({@ApiImplicitParam(name = "name", type = "query", value = "业务实体名称"),
		@ApiImplicitParam(name = "ids", type = "query", value = "业务实体id英文逗号分隔"),
		@ApiImplicitParam(name = "language", type = "query", value = "语言,ZHS中文,US英文")})
	@GetMapping("/getBusinessEntity")
	public R<List<ErpBusinessEntity>> getBusinessEntities(@RequestParam(required = false, value = "name") String name, @RequestParam(required = false, value = "ids") String ids,
														  @RequestParam(required = false, value = "language") String language) {
		if (StringUtil.isBlank(language)) {
			String lang = I18nUtils.getCurrentLang();
			language = lang.contains("zh") ? "ZHS" : "US";
		} else {
			language = language.toUpperCase();
		}
		return R.data(erpBusinessEntityService.getList(name,ids,language));
	}


	@ApiOperation(value = "查询地址的国家", notes = "传入name模糊查询, 传入ids回显")
	@ApiImplicitParams({@ApiImplicitParam(name = "name", type = "query", value = "国家名称,不传获取全部"),
			@ApiImplicitParam(name = "leaf", type = "query", dataType = "int", value = "是否需要检查是否有子节点", defaultValue = "0"),
			@ApiImplicitParam(name = "ids", type = "query", value = "国家编码英文逗号分隔")})
	@GetMapping("/getCountry")
	public R<List<ErpCountry>> getCountry(@RequestParam(required = false, value = "name") String name,
										  @RequestParam(required = false, value = "ids") String ids,
										  @RequestParam(required = false, defaultValue = "0", value = "leaf") int leaf) {
		return R.data(erpCountryService.getList(name,ids,leaf));
	}

	@ApiOperation(value = "查询地址的省或州", notes = "传入name和country模糊查询, 传入ids回显")
	@ApiImplicitParams({@ApiImplicitParam(name = "name", type = "query", value = "省名称"),
			@ApiImplicitParam(name = "leaf", type = "query", dataType = "int", value = "是否需要检查是否有子节点", defaultValue = "0"),
			@ApiImplicitParam(name = "country", type = "query", value = "国家编码,根据此项获取关联省/州,默认CN", defaultValue = "CN"),
			@ApiImplicitParam(name = "ids", type = "query", value = "省编码英文逗号分隔")})
	@GetMapping("/getProvince")
	public R<List<ErpProvince>> getProvince(@RequestParam(required = false, value = "name") String name,
											@RequestParam(required = false, defaultValue = "CN", value = "country") String country,
											@RequestParam(required = false, value = "ids") String ids,
											@RequestParam(required = false, defaultValue = "0", value = "leaf") int leaf) {
		return R.data(erpProvinceService.getList(name,country,ids,leaf));
	}

	@ApiOperation(value = "查询地址的市", notes = "传入name和province模糊查询, 传入ids回显")
	@ApiImplicitParams({@ApiImplicitParam(name = "name", type = "query", value = "市名称,暂时不传"),
			@ApiImplicitParam(name = "province", type = "query", value = "省编码,根据此项获取关联市"),
			@ApiImplicitParam(name = "leaf", type = "query", dataType = "int", value = "是否需要检查是否有子节点", defaultValue = "0"),
			@ApiImplicitParam(name = "ids", type = "query", value = "市编码英文逗号分隔")})
	@GetMapping("/getCity")
	public R<List<ErpCity>> getCity(@RequestParam(required = false, value = "name") String name,
									@RequestParam(required = false, value = "province") String province,
									@RequestParam(required = false, value = "ids") String ids,
									@RequestParam(required = false, defaultValue = "0", value = "leaf") int leaf) {
		return R.data(erpCityService.getList(name,province,ids,leaf));
	}

	@ApiOperation(value = "查询地址的区", notes = "传入name和city模糊查询, 传入ids回显")
	@ApiImplicitParams({@ApiImplicitParam(name = "name", type = "query", value = "区名称"),
			@ApiImplicitParam(name = "city", type = "query", value = "市编码,根据此项获取关联区"),
			@ApiImplicitParam(name = "ids", type = "query", value = "区编码英文逗号分隔")})
	@GetMapping("/getArea")
	public R<List<ErpArea>> getArea(@RequestParam(required = false, value = "name") String name,
									@RequestParam(required = false, value = "city") String city,
									@RequestParam(required = false, value = "ids") String ids) {
		return R.data(erpAreaService.getList(name,city,ids));
	}

	@ApiOperation(value = "根据名称获取国家省市区编码", notes = "根据名称获取国家省市区编码")
	@ApiImplicitParams({@ApiImplicitParam(name = "n", type = "query", value = "国家名称"),
			@ApiImplicitParam(name = "p", type = "query", value = "省名"),
			@ApiImplicitParam(name = "c", type = "query", value = "市名"),
			@ApiImplicitParam(name = "a", type = "query", value = "地区名称")})
	@GetMapping("/getAreaCode")
	public R<ErpLocationVO> getAreaCode(@RequestParam(required = false, value = "n") String n,
										@RequestParam(required = false, value = "p") String p,
										@RequestParam(required = false, value = "c") String c,
										@RequestParam(required = false, value = "a") String a,
										@RequestParam(required = false, value = "d") String code) {
//		String lang = I18nUtils.getCurrentLang();
//		String language = lang.startsWith("zh") ? "ZHS" : "US";
		String language = "ZHS";
		if (StringUtil.isNotBlank(code)) {
			List<ErpLocationVO> areaList = erpAreaService.findAddressByArea(String.format("'%s'", code), language);
			if (CollectionUtil.isNotEmpty(areaList)) {
				return R.data(areaList.get(0));
			}
		}
		ErpLocationVO locationVO = new ErpLocationVO();
		if (StringUtil.isNotBlank(c)) {
			List<ErpCity> list = erpCityService.findByName(c, language);
			if (CollectionUtil.isNotEmpty(list)) {
				ErpCity erpCity = list.get(0);
				locationVO.setCityCode(erpCity.getCityCode());
				locationVO.setCityName(erpCity.getCityName());
				ErpProvince erpProvince = erpProvinceService.lambdaQuery().eq(ErpProvince::getProvinceCode,
						erpCity.getProvinceCode()).eq(ErpProvince::getLanguage, language).one();
				if (erpProvince != null) {
					locationVO.setProvinceCode(erpCity.getProvinceCode());
					locationVO.setProvinceName(erpProvince.getProvinceName());
					locationVO.setCountryCode(erpProvince.getCountryCode());
					locationVO.setCountryName(n);
				} else {
					locationVO.setProvinceName(p);
					locationVO.setCountryName(n);
				}
				List<ErpArea> areaList = StringUtil.isBlank(a) ? null : erpAreaService.findByNameAndProvinceOrCity(null, locationVO.getCityCode(), a, language, code);
				if (CollectionUtil.isNotEmpty(areaList)) {
					locationVO.setAreaCode(areaList.get(0).getAreaCode());
					locationVO.setAreaName(areaList.get(0).getAreaName());
				} else {
					locationVO.setAreaName(a);
				}
			} else if (StringUtil.isNotBlank(a) && code != null && code.length() == 6) {
				ErpArea erpArea = new ErpArea();
				erpArea.setLanguage(language);
				erpArea.setLastUpdateDate(new Date());
				erpArea.setEnableFlag("Y");
				erpArea.setAreaCode(code);
				erpArea.setAreaName(a);
				erpArea.setCityCode(code.substring(0, 4) + "00");
				erpAreaService.save(erpArea);
				locationVO.setAreaCode(code);
				locationVO.setAreaName(a);
				locationVO.setCityCode(erpArea.getCityCode());
				locationVO.setCityName(c);
				locationVO.setProvinceCode(code.substring(0, 2) + "0000");
				locationVO.setProvinceName(p);
				locationVO.setCountryCode("CN");
				locationVO.setCountryName("中国");
			}
		} else if (StringUtil.isNotBlank(n)) {
			List<ErpCountry> list = erpCountryService.findByName(n, language);
			if (CollectionUtil.isNotEmpty(list)) {
				locationVO.setCountryCode(list.get(0).getCountryCode());
				locationVO.setCountryName(list.get(0).getCountryName());
			} else {
				locationVO.setCountryName(n);
			}
			if (StringUtil.isNotBlank(p)) {//中国香港特别行政区湾仔区
				List<ErpProvince> provinces = erpProvinceService.findByName(p, language);
				if (CollectionUtil.isNotEmpty(provinces)) {
					locationVO.setProvinceName(provinces.get(0).getProvinceName());
					locationVO.setProvinceCode(provinces.get(0).getProvinceCode());
					if (locationVO.getCountryCode() == null) {
						locationVO.setCountryCode(provinces.get(0).getCountryCode());
						locationVO.setCountryName(n);
					}
				} else {
					locationVO.setProvinceName(p);
				}
			}
			if (StringUtil.isNotBlank(a)) {
				List<ErpArea> areaList = erpAreaService.findByNameAndProvinceOrCity(locationVO.getProvinceCode(), locationVO.getCityCode(), a, language, code);
				if (CollectionUtil.isNotEmpty(areaList)) {
					locationVO.setAreaCode(areaList.get(0).getAreaCode());
					locationVO.setAreaName(areaList.get(0).getAreaName());
					if (StringUtil.isBlank(locationVO.getCityCode())) {
						locationVO.setCityCode(areaList.get(0).getCityCode());
						if (!language.equals(areaList.get(0).getLanguage())) {
							locationVO.setCityName(areaList.get(0).getLanguage());
						} else {
							List<ErpCity> cityList = erpCityService.lambdaQuery().eq(ErpCity::getCityCode, locationVO.getCityCode())
									.eq(ErpCity::getLanguage, language).list();
							if (CollectionUtil.isNotEmpty(cityList)) {
								locationVO.setCityName(cityList.get(0).getCityName());
							}
						}
					}
				} else {
					locationVO.setAreaCode(code);
					locationVO.setAreaName(a);
				}
			}
		}
		return R.data(locationVO);
	}

	@ApiOperation(value = "根据区域编码获取国家省市区", notes = "codes地区编码(areaCode),逗号分隔")
	@ApiImplicitParams({@ApiImplicitParam(name = "codes", type = "query", value = "地区编码,逗号分隔")})
	@GetMapping("/getAreaAll")
	public R<List<ErpLocationVO>> getAllByArea(@RequestParam("codes") String codes) {
		String lang = I18nUtils.getCurrentLang();
		String language = lang.startsWith("zh") ? "ZHS" : "US";
		codes = codes.replaceAll(",", "','");
		codes = String.format("'%s'", codes);
		return R.data(erpAreaService.findAddressByArea(codes, language));
	}

	@ApiOperation(value = "根据国家省市区编码获取各自详情", notes = "参数为简写")
	@ApiImplicitParams({@ApiImplicitParam(name = "n", type = "query", value = "国家编码,默认中国", defaultValue = "CN"), @ApiImplicitParam(name = "p", type = "query", value = "省码"),
			@ApiImplicitParam(name = "c", type = "query", value = "市编码"), @ApiImplicitParam(name = "a", type = "query", value = "地区编码")})
	@GetMapping("/getAreaInfo")
	public R<ErpLocationVO> getAllInfo(@RequestParam(required = false, defaultValue = "CN", value = "n") String n,
									   @RequestParam(required = false, value = "p") String p,
									   @RequestParam(required = false, value = "c") String c,
									   @RequestParam(required = false, value = "a") String a) {
		String lang = I18nUtils.getCurrentLang();
		String language = lang.startsWith("zh") ? "ZHS" : "US";
		ErpCountry country = erpCountryService.lambdaQuery().eq(ErpCountry::getCountryCode, n)
				.eq(ErpCountry::getLanguage, language).one();

		ErpLocationVO locationVO = new ErpLocationVO();

		if (StringUtil.isNotBlank(p)) {
			ErpProvince province = erpProvinceService.lambdaQuery().eq(ErpProvince::getProvinceCode, p)
					.eq(ErpProvince::getLanguage, language).one();
			if (province == null) {
				throw new ServiceException("省不存在");
			}
			locationVO.setProvinceCode(province.getProvinceCode());
			locationVO.setProvinceName(province.getProvinceName());
			if (country == null) {
				country = erpCountryService.lambdaQuery().eq(ErpCountry::getCountryCode, province.getCountryCode())
						.eq(ErpCountry::getLanguage, language).one();
			}
		}
		if (country == null) {
			throw new ServiceException("国家不存在");
		}
		locationVO.setCountryCode(country.getCountryCode());
		locationVO.setCountryName(country.getCountryName());
		if (StringUtil.isNotBlank(c)) {
			ErpCity city = erpCityService.lambdaQuery().eq(ErpCity::getCityCode, c)
					.eq(ErpCity::getLanguage, language).one();
			if (city == null) {
				throw new ServiceException("市不存在");
			}
			locationVO.setCityCode(city.getCityCode());
			locationVO.setCityName(city.getCityName());
		}
		if (StringUtil.isNotBlank(a)) {
			ErpArea area = erpAreaService.lambdaQuery().eq(ErpArea::getAreaCode, a)
					.eq(ErpArea::getLanguage, language).one();
			if (area == null) {
				throw new ServiceException("地区不存在");
			}
			locationVO.setAreaCode(area.getAreaCode());
			locationVO.setAreaName(area.getAreaName());
		}
		return R.data(locationVO);
	}

}

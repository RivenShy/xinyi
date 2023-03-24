package org.xyg.eshop.main.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.system.feign.IDictClient;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.erpentity.ErpArea;
import org.xyg.eshop.main.erpentity.ErpCity;
import org.xyg.eshop.main.erpentity.ErpProvince;
import org.xyg.eshop.main.service.ICommonService;
import org.xyg.eshop.main.service.IErpAreaService;
import org.xyg.eshop.main.service.IErpCityService;
import org.xyg.eshop.main.service.IErpProvinceService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CommonServiceImpl implements ICommonService {

    private final IDictClient dictClient;

    private final IErpProvinceService provinceService;

    private final IErpCityService cityService;

    private final IErpAreaService areaService;

    @Override
    public String getDictValue(String dictCode,String dictKey){
        if (StringUtil.isBlank(dictCode) || StringUtil.isBlank(dictKey)){
            return null;
        }
        try {
            return EShopMainConstant.getData(dictClient.getValue(dictCode, dictKey));
        } catch (Exception e) {
            log.error("查询字典值出现错误, code -> {} key -> {} ",dictCode,dictKey);
            return null;
        }
    }

    @Override
    public String getPcaName(String provincesCode,String cityCode,String areasCode){
        StringBuilder pcaName = new StringBuilder();

        try {
            // 省
            if (StringUtil.isNotBlank(provincesCode)){
                List<ErpProvince> provinceList = provinceService.getList(null, null, provincesCode, 1);
                if (CollectionUtil.isNotEmpty(provinceList)){
                    pcaName.append(provinceList.get(0).getProvinceName());
                }
            }

            // 市
            if (StringUtil.isNotBlank(cityCode)){
                List<ErpCity> cityList = cityService.getList(null, null, cityCode, 1);
                if (CollectionUtil.isNotEmpty(cityList)){
                    pcaName.append(cityList.get(0).getCityName());
                }
            }

            // 区
            if (StringUtil.isNotBlank(areasCode)){
                List<ErpArea> areaList = areaService.getList(null, null, areasCode);
                if (CollectionUtil.isNotEmpty(areaList)){
                    pcaName.append(areaList.get(0).getAreaName());
                }
            }
        } catch (Exception e) {
            log.error("commonService查询省市区名称出现错误:provincesCode -> {} cityCode -> {} areasCode -> {}",provincesCode,cityCode,areasCode, e);
        }

        return pcaName.toString();
    }

}

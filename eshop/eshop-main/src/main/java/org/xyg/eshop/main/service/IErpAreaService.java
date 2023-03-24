package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xyg.eshop.main.erpentity.ErpArea;
import org.xyg.eshop.main.vo.ErpLocationVO;

import java.util.List;

/**
 * @author wekin
 * erp地区
 */
public interface IErpAreaService extends IService<ErpArea> {

	List<ErpLocationVO> findAddressByArea(String codes, String language);

	List<ErpArea> findByNameAndProvinceOrCity(String provinceCode, String cityCode, String name, String language, String adcode);

	List<ErpArea> getList(String name, String city, String ids);

}

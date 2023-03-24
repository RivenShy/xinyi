package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xyg.eshop.main.erpentity.ErpProvince;

import java.util.List;

/**
 * @author wekin
 * erp省/州
 */
public interface IErpProvinceService extends IService<ErpProvince> {

	List<ErpProvince> findByName(String name, String language);

	List<ErpProvince> getList(String name, String country,String ids,int leaf);
}

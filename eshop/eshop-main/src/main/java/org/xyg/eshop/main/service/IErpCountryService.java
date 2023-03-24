package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xyg.eshop.main.erpentity.ErpCountry;

import java.util.List;

/**
 * @author wekin
 * erp国家
 */
public interface IErpCountryService extends IService<ErpCountry> {

	List<ErpCountry> findByName(String name, String language);

	List<ErpCountry> getList(String name, String ids,int leaf);

}

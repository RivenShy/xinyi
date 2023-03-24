package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xyg.eshop.main.erpentity.ErpCity;

import java.util.List;

/**
 * @author wekin
 * erp市
 */
public interface IErpCityService extends IService<ErpCity> {

	List<ErpCity> findByName(String name, String language);

	List<ErpCity> getList(String name, String province, String ids, int leaf);
}

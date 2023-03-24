package org.xyg.eshop.main.service;

import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.erpentity.ErpBusinessEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IErpBusinessEntityService extends BaseService<ErpBusinessEntity> {

	LocalDateTime findMaxUpdateDate();

	Map<Long, ErpBusinessEntity> findOrgLanguageNames(String orgIds, String lang);

	List<ErpBusinessEntity> getList(String name, String ids, String language);

	void syncBusinessEntitiesFromErp();

}

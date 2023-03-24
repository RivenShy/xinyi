package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.DateUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.erp.main.client.IErpSyncClient;
import org.xyg.erp.main.dto.ErpBusinessQueryDto;
import org.xyg.erp.main.entity.PageResult;
import org.xyg.erp.main.vo.ErpBusinessResVO;
import org.xyg.eshop.main.erpentity.AloSync;
import org.xyg.eshop.main.erpentity.ErpBusinessEntity;
import org.xyg.eshop.main.mapper.ErpBusinessEntityMapper;
import org.xyg.eshop.main.service.IAloSyncService;
import org.xyg.eshop.main.service.IErpBusinessEntityService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
@AllArgsConstructor
public class ErpBusinessEntityServiceImpl extends BaseServiceImpl<ErpBusinessEntityMapper, ErpBusinessEntity> implements IErpBusinessEntityService {

	private final IErpSyncClient erpSyncClient;

	private final IAloSyncService aloSyncService;

	@Override
	public LocalDateTime findMaxUpdateDate(){
		return getBaseMapper().findMaxUpdateDate();
	}

	@Override
	public Map<Long, ErpBusinessEntity> findOrgLanguageNames(String orgIds, String lang) {
		if (StringUtil.isBlank(lang)){
			lang = "US";
		}
		return getBaseMapper().findOrgLanguageNames(orgIds, lang);
	}

	@Override
	public List<ErpBusinessEntity> getList(String name, String ids, String language){
		QueryWrapper<ErpBusinessEntity> wrapper = new QueryWrapper<>();
		boolean both = "BOTH".equals(language);
		wrapper.eq("language", both ? "ZHS" : language);
		if (StringUtil.isNotBlank(ids)) {
			String[] values = ids.split(",");
			wrapper.in("org_id", values);
			List<ErpBusinessEntity> list = list(wrapper);
			Map<Long, ErpBusinessEntity> langMap = !both ? null : findOrgLanguageNames(ids, "US");
			if (CollectionUtil.isNotEmpty(langMap)) {
				for (ErpBusinessEntity entity : list) {
					ErpBusinessEntity business = langMap.get(entity.getOrgId());
					if (business != null) {
						entity.setEnName(StringUtil.isNotBlank(business.getPrintName()) ? business.getPrintName() : business.getOrgName());
					}
				}
			}
			return list;
		} else {
			if (StringUtil.isNotBlank(name)) {
				wrapper.like("org_name", name);
			}
			Page<ErpBusinessEntity> page = new Page<>(1, 120);
			Page<ErpBusinessEntity> pageResult = page(page, wrapper);
			List<ErpBusinessEntity> list = pageResult.getRecords();
			if (CollectionUtil.isEmpty(list)) {
				return list;
			}
			StringBuilder builder = new StringBuilder();
			if (both) {
				for (ErpBusinessEntity entity : list) {
					if (builder.length() > 0) {
						builder.append(",");
					}
					builder.append(entity.getOrgId());
				}
			}
			Map<Long, ErpBusinessEntity> langMap = !both ? null : findOrgLanguageNames(builder.toString(), "US");
			if (CollectionUtil.isNotEmpty(langMap)) {
				for (ErpBusinessEntity entity : list) {
					ErpBusinessEntity business = langMap.get(entity.getOrgId());
					if (business != null) {
						entity.setEnName(StringUtil.isNotBlank(business.getPrintName()) ? business.getPrintName() : business.getOrgName());
					}
				}
			}
			return list;
		}
	}

	@Override
	public void syncBusinessEntitiesFromErp(){
		ErpBusinessQueryDto queryDto = new ErpBusinessQueryDto();
		String path = queryDto.getPath();
		AloSync aloSync = aloSyncService.getById(path);
		Date date = aloSync == null ? null : aloSync.getLastTime();
		long max = 0;
		if (date != null) {
			max = date.getTime();
			LocalDateTime plus = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime() + 1), ZoneId.systemDefault());
			queryDto.setDateFrom(DateUtil.formatDateTime(plus));
			queryDto.setDateTo(DateUtil.formatDateTime(LocalDateTime.now()));
		}

		Integer page = 1;
		Integer pages = 1;
		Pattern zhPattern = Pattern.compile("[\u4e00-\u9fa5]{2,}");
		try {
			while (page <= pages) {
				queryDto.setPageNum(page);
				R<PageResult<ErpBusinessResVO>> pageResultR = erpSyncClient.syncBusinessEntities(queryDto);
				if (!pageResultR.isSuccess() || pageResultR.getData() == null){
					log.error("易车同步业务实体出现错误 -> {} ",pageResultR);
					return;
				}
				PageResult<ErpBusinessResVO> pageResult = pageResultR.getData();
				List<ErpBusinessResVO> list = pageResult.getList();
				if (CollectionUtil.isEmpty(list)) {
					break;
				}
				for (ErpBusinessResVO v : list) {
					if (StringUtil.isBlank(v.getOrgName())) {
						continue;
					}
					ErpBusinessEntity erpBusinessEntity = BeanUtil.copyProperties(v, ErpBusinessEntity.class);
					if (v.getLastUpdateDate() != null && v.getLastUpdateDate().getTime() > max) {
						max = v.getLastUpdateDate().getTime();
					}
					v.setStatus(6);
					ErpBusinessEntity old;
					try {
						old = lambdaQuery().eq(ErpBusinessEntity::getOrgId, v.getOrgId())
							.eq(ErpBusinessEntity::getLanguage, v.getLanguage()).one();
					} catch (Exception e) {//有人会动数据库,动了就忽略吧
						log.error("查询业务实体异常 {}:{}", v.getOrgId(), v.getLanguage(), e);
						continue;
					}
					if (old == null) {
						save(erpBusinessEntity);
					} else {
						if ("US".equals(v.getLanguage())) {
							if (v.getOrgName() == null || (zhPattern.matcher(v.getOrgName()).find()
								&& !zhPattern.matcher(old.getOrgName()).find())) {//本地修改为英文,继续使用英文
								v.setOrgName(old.getOrgName());
							}
							if (v.getAddressLine1() == null || (zhPattern.matcher(v.getAddressLine1()).find()
								&& !zhPattern.matcher(old.getAddressLine1()).find())) {//本地修改为英文,继续使用英文
								v.setAddressLine1(old.getAddressLine1());
							}
						}
						if (StringUtil.isBlank(v.getAddressLine1()) && old.getAddressLine2() != null) {
							v.setAddressLine1(old.getAddressLine1());
						}
						if (StringUtil.isBlank(v.getAddressLine2()) && old.getAddressLine2() != null) {
							v.setAddressLine2(old.getAddressLine2());
						}
						if (StringUtil.isBlank(v.getTelephoneNumber1()) && old.getTelephoneNumber1() != null) {
							v.setTelephoneNumber1(old.getTelephoneNumber1());
						}
						if (StringUtil.isBlank(v.getTelephoneNumber2()) && old.getTelephoneNumber2() != null) {
							v.setTelephoneNumber2(old.getTelephoneNumber2());
						}
						erpBusinessEntity.setId(old.getId());
						updateById(erpBusinessEntity);
					}
				}
				pages = pageResult.getPages();
				page += 1;
			}
			if (max > 0) {
				if (aloSync == null) {
					aloSync = new AloSync();
					aloSync.setUrl(path);
					aloSync.setLastTime(new Date(max));
					aloSyncService.save(aloSync);
				} else {
					aloSync.setLastTime(new Date(max));
					aloSyncService.updateById(aloSync);
				}
			}
		} catch (Exception e) {
			log.error("同步业务实体异常", e);
		}
	}

}

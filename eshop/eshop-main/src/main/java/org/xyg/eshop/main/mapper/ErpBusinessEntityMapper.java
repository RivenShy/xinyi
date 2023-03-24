package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xyg.eshop.main.erpentity.ErpBusinessEntity;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface ErpBusinessEntityMapper extends BaseMapper<ErpBusinessEntity> {

	@Select("select max(last_update_date) from erp_business_entities where status = 6")
	LocalDateTime findMaxUpdateDate();

	@MapKey("orgId")
	@Select("select id,org_id,org_name,language from erp_business_entities where org_id in(${ids}) and language=#{lang}")
	Map<Long, ErpBusinessEntity> findOrgLanguageNames(@Param("ids") String orgIds, @Param("lang") String lang);

}

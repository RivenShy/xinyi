package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.xyg.eshop.main.entity.ProductModelAdjunct;

import java.time.LocalDateTime;

/**
 * @Entity org.springrabbit.qbcrm.entity.ProductModelAdjunct
 */
@Mapper
public interface ProductModelAdjunctMapper extends BaseMapper<ProductModelAdjunct> {

	@Select("select max(last_update_date) from qbcrm_product_model_adjunct")
	LocalDateTime findMaxUpdateDate();

}





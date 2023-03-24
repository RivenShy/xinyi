package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.erpentity.ErpArea;
import org.xyg.eshop.main.vo.ErpLocationVO;

import java.util.List;

public interface ErpAreaMapper extends BaseMapper<ErpArea> {

	List<ErpLocationVO> findAddressByArea(@Param("codes") String codes, @Param("language") String language);

    List<ErpArea> findListByNameAndProvince( @Param("code") String provinceCode,  @Param("name") String name, @Param("language") String language);
}

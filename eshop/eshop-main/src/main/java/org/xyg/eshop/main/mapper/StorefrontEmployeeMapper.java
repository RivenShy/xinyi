package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.entity.StorefrontEmployee;
import org.xyg.eshop.main.vo.StorefrontEmployeeVO;

import java.util.List;

@Mapper
public interface StorefrontEmployeeMapper extends BaseMapper<StorefrontEmployee> {

    IPage<StorefrontEmployeeVO> getPage(IPage<StorefrontEmployeeVO> page, @Param("emp") StorefrontEmployeeVO employeeVO);

	List<StorefrontEmployeeVO> getDetail(@Param("ids") String ids, @Param("empno") String empno);
}

package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.StorefrontEmployee;
import org.xyg.eshop.main.vo.StorefrontEmployeeVO;

import java.util.List;

public interface IStorefrontEmployeeService extends BaseService<StorefrontEmployee> {

	/**
	 * 保存
	 * @param employeeVO 门店员工数据
	 * @return
	 */
    Long saveEmployee(StorefrontEmployeeVO employeeVO);

	/**
	 *
	 * @param page 分页参数
	 * @param employeeVO 搜索条件
	 * @return
	 */
	IPage<StorefrontEmployeeVO> getPage(IPage<StorefrontEmployeeVO> page, StorefrontEmployeeVO employeeVO);

	/**
	 * 根据id或者工号获取详情
	 * @param ids ids
	 * @param empno 工号
	 * @return
	 */
	List<StorefrontEmployeeVO> getDetail(String ids, String empno);

	/**
	 * 更新门店员工
	 * @param employeeVO 门店员工数据
	 * @return
	 */
	StorefrontEmployeeVO updateEmployee(StorefrontEmployeeVO employeeVO);

	/**
	 * 删除门店员工信息
	 * @param ids id
	 * @return
	 */
	Boolean delete(String ids);

}

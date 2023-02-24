package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.system.feign.IDictClient;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.StorefrontEmployee;
import org.xyg.eshop.main.mapper.StorefrontEmployeeMapper;
import org.xyg.eshop.main.service.IStorefrontEmployeeService;
import org.xyg.eshop.main.vo.StorefrontEmployeeVO;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class StorefrontEmployeeServiceImpl extends BaseServiceImpl<StorefrontEmployeeMapper, StorefrontEmployee> implements IStorefrontEmployeeService {

	private final IDictClient dictClient;

	@Override
	public Long saveEmployee(StorefrontEmployeeVO employeeVO){
		String empno = employeeVO.getEmpno();

		Integer empnoCount = lambdaQuery().eq(StorefrontEmployee::getEmpno, empno).count();
		if (empnoCount >= 1){
			throw new ServiceException("该工号已存在,新增失败");
		}

		save(employeeVO);
		return employeeVO.getId();
	}

	@Override
	public IPage<StorefrontEmployeeVO> getPage(IPage<StorefrontEmployeeVO> page, StorefrontEmployeeVO employeeVO){
		IPage<StorefrontEmployeeVO> pageList = baseMapper.getPage(page, employeeVO);
		// 补充数据
		fillData(pageList.getRecords());
		return pageList;
	}

	@Override
	public List<StorefrontEmployeeVO> getDetail(String ids, String empno){
		if (StringUtil.isNotBlank(empno)) {
			empno = String.format("'%s'", empno.replaceAll(",", "','"));
		}else if (StringUtil.isBlank(ids)){
			return Collections.emptyList();
		}
		List<StorefrontEmployeeVO> list = baseMapper.getDetail(ids, empno);
		// 补充数据
		fillData(list);
		return list;
	}

	@Override
	public StorefrontEmployeeVO updateEmployee(StorefrontEmployeeVO employeeVO){
		updateById(employeeVO);
		return employeeVO;
	}

	/**
	 * 补充数据
	 * @param list 门店数据
	 */
	private void fillData(List<StorefrontEmployeeVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}

		for (StorefrontEmployeeVO employeeVO : list) {
			fillDetailData(employeeVO);
		}
	}

	/**
	 * 补充详情数据
	 * @param employeeVO 门店员工数据
	 */
	private void fillDetailData(StorefrontEmployeeVO employeeVO){
		if (employeeVO == null){
			return;
		}

		String dictStatus = employeeVO.getStatus() == null ? null : employeeVO.getStatus().toString();
		String statusName = getDictValue(EShopMainConstant.STOREFRONT_EMPLOYEE_STATUS_DICT_CODE, dictStatus);

		employeeVO.setStatusName(statusName);
	}

	/**
	 * 获取字典值
	 * @param dictCode 字典编码
	 * @param dictKey 字典键
	 * @return
	 */
	private String getDictValue(String dictCode,String dictKey){
		if (StringUtil.isBlank(dictCode) || StringUtil.isBlank(dictKey)){
			return null;
		}
		R<String> dictValueR = dictClient.getValue(dictCode, dictKey);
		return EShopMainConstant.getData(dictValueR);
	}

}

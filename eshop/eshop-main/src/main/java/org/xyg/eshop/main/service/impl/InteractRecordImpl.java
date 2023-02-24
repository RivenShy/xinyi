package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.system.feign.IDictClient;
import org.springrabbit.system.feign.IEmployeeClient;
import org.springrabbit.system.vo.EmployeeVO;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.InteractRecord;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.mapper.InteractRecordMapper;
import org.xyg.eshop.main.service.IInteractRecordService;
import org.xyg.eshop.main.service.IStorefrontService;
import org.xyg.eshop.main.vo.InteractRecordVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class InteractRecordImpl extends BaseServiceImpl<InteractRecordMapper,InteractRecord> implements IInteractRecordService {

	private final IStorefrontService storefrontService;
	private final IEmployeeClient employeeClient;
	private final IDictClient dictClient;

	@Override
	public Boolean saveInteractRecord(InteractRecordVO interactRecordVO){
		return saveOrUpdate(interactRecordVO);
	}

	@Override
	public Boolean deleteId(Long id){
		return removeById(id);
	}

	@Override
	public IPage<InteractRecordVO> getPage(IPage<InteractRecordVO> page, InteractRecordVO interactRecordVO){
		handleDateSearch(interactRecordVO);
		IPage<InteractRecordVO> pages = baseMapper.getPage(page, interactRecordVO);

		// 补充数据
		fillData(pages.getRecords());

		return pages;
	}

	@Override
	public InteractRecordVO getDetail(Long id){
		InteractRecord interactRecord = getById(id);
		if (interactRecord == null){
			throw new ServiceException("未查询到数据");
		}

		InteractRecordVO interactRecordVO = BeanUtil.copyProperties(interactRecord, InteractRecordVO.class);
		// 填充数据
		fillDetailData(interactRecordVO);
		return interactRecordVO;
	}

	/**
	 * 补充列表数据
	 * @param list 列表数据
	 */
	private void fillData(List<InteractRecordVO> list) {
		if (CollectionUtil.isEmpty(list)){
			return;
		}

		for (InteractRecordVO interactRecordVO : list) {
			// 业务员名称
			String salesmanMame = getSalesmanMame(interactRecordVO.getSalesman());
			interactRecordVO.setSalesmanName(salesmanMame);

			// 门店类型名称
			String dictStorefrontType = interactRecordVO.getStorefrontType() == null ? null : interactRecordVO.getStorefrontType().toString();
			String storefrontTypeName = getDictValue(EShopMainConstant.STOREFRONT_TYPE_DICT_CODE, dictStorefrontType);
			interactRecordVO.setStorefrontTypeName(storefrontTypeName);

			// 门店等级名称
			String dictStorefrontLevel = interactRecordVO.getStorefrontLevel() == null ? null : interactRecordVO.getStorefrontLevel().toString();
			String storefrontLevelName = getDictValue(EShopMainConstant.STOREFRONT_LEVEL_DICT_CODE, dictStorefrontLevel);
			interactRecordVO.setStorefrontLevelName(storefrontLevelName);
		}

	}

	/**
	 * 补充详情数据
	 * @param interactRecordVO 互动记录
	 */
	private void fillDetailData(InteractRecordVO interactRecordVO){
		if (interactRecordVO == null){
			return;
		}

		// 查询门店名称
		Long storefrontId = interactRecordVO.getStorefrontId();
		Storefront storefront = storefrontService.getById(storefrontId);
		if (storefront != null){
			interactRecordVO.setStorefrontName(storefront.getStorefrontName());
		}

		// 查询业务员名称
		String salesmanMame = getSalesmanMame(interactRecordVO.getSalesman());
		interactRecordVO.setSalesmanName(salesmanMame);

	}

	/**
	 * 处理时间搜索条件
	 * @param interactRecordVO 搜索条件
	 */
	private void handleDateSearch(InteractRecordVO interactRecordVO) {
		String startDateSearch = interactRecordVO.getStartDateSearch();
		String endDateSearch = interactRecordVO.getEndDateSearch();
		if (StringUtil.isNotBlank(startDateSearch)) {
			// 开始时间添加时分秒 00:00:00
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date parse = simpleDateFormat.parse(startDateSearch);
				calendar.setTime(parse);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				Date time = calendar.getTime();
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				startDateSearch = simpleDateFormat.format(time);
				interactRecordVO.setStartDateSearch(startDateSearch);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (StringUtil.isNotBlank(endDateSearch)) {
			// 结束时间添加时分秒 23:59:59
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date parse = simpleDateFormat.parse(endDateSearch);
				calendar.setTime(parse);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				Date time = calendar.getTime();
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				endDateSearch = simpleDateFormat.format(time);
				interactRecordVO.setEndDateSearch(endDateSearch);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据员工id查询员工数据
	 * @param id 员工id
	 * @return
	 */
	private String getSalesmanMame(String id){
		String salesmanMame = null;
		R<List<EmployeeVO>> employeeVoListR = employeeClient.employeeDetail(null, id);
		List<EmployeeVO> employeeVoList = EShopMainConstant.getData(employeeVoListR);
		if (CollectionUtil.isEmpty(employeeVoList)){
			return salesmanMame;
		}

		return employeeVoList.get(0).getEmpName();
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

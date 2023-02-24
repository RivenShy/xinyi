package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.base.DBEntity;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.secure.utils.AuthUtil;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.*;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.springrabbit.flow.core.dto.TaskCompleteDto;
import org.springrabbit.flow.core.entity.Activity;
import org.springrabbit.flow.core.entity.RabbitFlow;
import org.springrabbit.flow.core.feign.IFlowOpenClient;
import org.springrabbit.flow.core.utils.CommentTypeEnum;
import org.springrabbit.system.entity.ApproverRoles;
import org.springrabbit.system.feign.IDictClient;
import org.springrabbit.system.feign.ISysClient;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.ehop.common.constants.EshopConstants;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.mapper.StorefrontMapper;
import org.xyg.eshop.main.service.IStorefrontService;
import org.xyg.eshop.main.util.ProcessUtils.ProcessConstant;
import org.xyg.eshop.main.vo.AdvancedSearchVO;
import org.xyg.eshop.main.vo.StorefrontVO;

import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class StorefrontServiceImpl extends BaseServiceImpl<StorefrontMapper, Storefront> implements IStorefrontService {

	private final IFlowOpenClient flowOpenClient;
	private final ISysClient sysClient;
	private final IDictClient dictClient;

	private static final String PREFIX = "MD";

	private final AutoIncrementIDGenerator autoIncrementIDGenerator;

	/**
	 * 保存或修改门店信息
	 *
	 * @param storefrontVO 门店vo
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public StorefrontVO saveOrUpdate(StorefrontVO storefrontVO) {

		// 解决特殊字符 [&]
		storefrontVO.setStorefrontName(decod(storefrontVO.getStorefrontName()));
		decodString(storefrontVO);

		// 如果门店id是空为新增
		if (storefrontVO.getId() != null) {
			// 更新门店表数据
			updateById(storefrontVO);
			return storefrontVO;
		}

		// 查询当前门店名称或社会信用代码在数据库中是否存在
		LambdaQueryChainWrapper<Storefront> queryChainWrapper = lambdaQuery().ne(Storefront::getStatus,EshopConstants.STATUS_DISUSE).in(Storefront::getStatus, EshopConstants.STATUS_NORMAL,EshopConstants.STATUS_APPROVING);
		queryChainWrapper.apply("lower(replace(storefront_name, ' ', '')) = lower(replace('"+storefrontVO.getStorefrontName()+"', ' ',''))");
		Integer count = queryChainWrapper.count();
		if (count > 0) {
			// 门店名称在数据库中已存在不允许新增
			throw new ServiceException("门店名称已存在，请前往门店新建");
		}

		if (storefrontVO.getStatus() == null){
			storefrontVO.setStatus(EshopConstants.STATUS_SAVE);
		}

		// 保存门店信息
		save(storefrontVO);
		return storefrontVO;
	}

	@Override
	public Long submit(StorefrontVO storefrontVO){

		// 生成门店编码
		if (StringUtil.isBlank(storefrontVO.getStorefrontCode())){
			storefrontVO.setStorefrontCode(generateNo(EShopMainConstant.STOREFRONT_GENERATE_CODE,3));
		}

		saveOrUpdate(storefrontVO);
		return storefrontVO.getId();
	}

	/**
	 * 获取门店列表信息
	 *
	 * @param storefrontName    门店名称
	 * @param selectStatus 查询状态
	 * @param page         分页参数
	 * @param userId       用户id
	 * @return
	 */
	@Override
	public R<IPage<Storefront>> getList(String storefrontName, Integer status, Integer storefrontLevel, Long salesrepId, Integer selectStatus, IPage<Storefront> page, Long userId, String companyLogo) {
		// 门店分页查询
		QueryWrapper<Storefront> storefrontQW = new QueryWrapper<>();

		storefrontQW.lambda()
			.like(StringUtil.isNotBlank(storefrontName),Storefront::getStorefrontName,storefrontName)// 如果门店名称不为空，加入查询条件
			.eq(status != null,DBEntity::getStatus,status)// 如果门店状态不为空,加入查询条件
			.eq(storefrontLevel != null,Storefront::getStorefrontLevel,storefrontLevel);// 如果门店等级不为空,加入查询条件

		// 状态为1,查询最近一个月创建记录
		if (selectStatus == 1) {
			// 获取当前年月
			String nowYM = DateUtil.format(LocalDate.now(), "yyyy-MM");
			storefrontQW.eq("TO_CHAR(CREATION_DATE,'yyyy-MM')", nowYM);
		} else if (selectStatus == 2) {
			// 状态为2,查询我的
			storefrontQW.and(Wrapper -> Wrapper.eq("CREATED_BY", userId)
				.or()
				.eq("LAST_UPDATED_BY", userId));
		} else if (selectStatus == 3) {
			// 状态为3,查询我最近一个月创建的
			// 获取当前年月
			String nowYM = DateUtil.format(LocalDate.now(), "yyyy-MM");
			storefrontQW.eq("CREATED_BY", userId)
				.eq("TO_CHAR(CREATION_DATE,'yyyy-MM')", nowYM);
		} else if (selectStatus == 4) {
			// 状态为4,查询我最近一个月更新的
			// 获取当前年月
			String nowYM = DateUtil.format(LocalDate.now(), "yyyy-MM");
			storefrontQW.eq("LAST_UPDATED_BY", userId)
				.eq("TO_CHAR(LAST_UPDATE_DATE,'yyyy-MM')", nowYM);
		} else if (selectStatus == 5) {
			// 状态为5,查询本周新转换门店
			// 当前时间
			LocalDate today = LocalDate.now();
			// 本周周一
			LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
			// 本周周日
			LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

			storefrontQW.apply("CREATION_DATE >= to_date('" + monday + "','yyyy-MM-dd')")
				.apply("CREATION_DATE <= to_date('" + sunday + "','yyyy-MM-dd')");
		} else if (selectStatus == 6) {
			// 状态为6,查询本月新转换门店
			// 获取当前年月
			String nowYM = DateUtil.format(LocalDate.now(), "yyyy-MM");
			storefrontQW.eq("TO_CHAR(CREATION_DATE,'yyyy-MM')", nowYM);
		}
		storefrontQW.orderByDesc("CREATION_DATE", "ID");

		// 查询门店信息
		IPage<Storefront> storefrontPage = page(page, storefrontQW);

		return R.data(storefrontPage);
	}

	/**
	 * 获取门店详情
	 *
	 * @param id 门店id
	 * @return
	 */
	@Override
	public R<StorefrontVO> getDetail(Long id) {
		// 获取门店数据
		Storefront storefront = getById(id);
		if (storefront == null) {
			return R.fail("门店已失效");
		}

		StorefrontVO storefrontVO = BeanUtil.copyProperties(storefront,StorefrontVO.class);

		// 补充数据
		fillDetailData(storefrontVO);
		// 返回门店详情信息
		return R.data(storefrontVO);
	}

	/**
	 * 修改门店状态为注销或者黑名单,审批通过
	 *
	 * @param storefront 门店数据
	 * @return
	 */
	@Override
	public R<String> updateStatus(Storefront storefront) {
		Storefront old = getById(storefront.getId());
		if (old == null) {
			throw new ServiceException("门店不存在");
		}
		return R.status(updateById(storefront));
	}

	/**
	 * 高级搜索
	 *
	 * @param advancedSearchVOList 搜索字段、运算符、值集合
	 * @param page                 分页信息
	 * @return
	 */
	@Override
	public R<IPage<Storefront>> advancedSearch(List<AdvancedSearchVO> advancedSearchVOList, IPage page) {

		if (CollectionUtil.isNotEmpty(advancedSearchVOList)) {
			QueryWrapper<Storefront> storefrontQW = new QueryWrapper<>();
			// 状态值
//			int status = 2;
			for (AdvancedSearchVO advancedSearchVO : advancedSearchVOList) {
				// 获取字段名称
				String fieldName = advancedSearchVO.getFieldName();
				// 获取操作
				String operation = advancedSearchVO.getOperation();
				if (StringUtil.isNotBlank(fieldName) && StringUtil.isNotBlank(operation) && StringUtil.isNotBlank(advancedSearchVO.getValue())) {
					// 保证只会添加一次状态条件
					/*if (status == 2) {
						storefrontQW.eq("STATUS", EshopConstants.STATUS_NORMAL);
							*//*.and(Wrapper -> Wrapper.eq("STATUS", EshopConstants.STATUS_NORMAL)
							.or().eq("STATUS", EshopConstants.STATUS_APPROVING));*//*
						status++;
					}*/

					// 负责人
					if ("PRINCIPAL".equals(fieldName) && StringUtil.isNotBlank(operation)) {
						/*List<Long> userIdList = userFeign.getByFormalCust(advancedSearchVO.getValue(), operation);
						// 如果人员信息不为空,人员id加入查询条件
						if (CollectionUtil.isNotEmpty(userIdList)) {
							storefrontQW.in(fieldName, userIdList);
						}*/
						continue;
					}

					// 获取值
					String[] valueSplit = advancedSearchVO.getValue().split(",");

					for (String value : valueSplit) {
						// 等于
						if ("eq".equals(operation)) {
							storefrontQW.eq(fieldName, value);
						} else if ("ne".equals(operation)) {
							// 不等于
							storefrontQW.ne(fieldName, value);
						} else if ("gt".equals(operation)) {
							// 大于
							storefrontQW.gt(fieldName, value);
						} else if ("ge".equals(operation)) {
							// 大于或等于
							storefrontQW.ge(fieldName, value);
						} else if ("lt".equals(operation)) {
							// 小于
							storefrontQW.lt(fieldName, value);
						} else if ("le".equals(operation)) {
							// 小于或等于
							storefrontQW.le(fieldName, value);
						} else if ("like".equals(operation)) {
							// 包括
							storefrontQW.like(fieldName, value);
						} else if ("notLike".equals(operation)) {
							// 不包括
							storefrontQW.notLike(fieldName, value);
						}
					}
				}
			}
			// 获取门店信息
			IPage<Storefront> storefrontPage = page(page, storefrontQW);
			return R.data(storefrontPage);
		}
		return R.fail("未查询到数据");
	}

	private String getDateString(String remark) {
		String format = DateUtil.format(LocalDateTime.now(), DateUtil.PATTERN_DATE);
		if (StringUtil.isNotBlank(remark)) {
			format = String.format("%s:%S", format, remark);
		} else {
			format = String.format("%s:%s", format, "导入");
		}
		return format;
	}

//	@Override
//	public IPage<SynchronizeErpLogsVO> getSynchronizeErpLogs(String storefrontName, String accountNumber, String isOK, String startDate, String endDate, IPage<SynchronizeErpLogsVO> page) {
//		return getSynchronizeErpLogs(storefrontName, accountNumber, isOK, startDate, endDate, page);
//	}

	/**
	 * <p>
	 * 处理门店名称中包含 & 的特殊字符;
	 * 只处理 & 特殊字符
	 * </p>
	 */
	@Override
	public String decod(String var) {
		if (StringUtil.isNotBlank(var)) {
			return StringEscapeUtils.unescapeXml(var);
		}
		return null;
	}

	private void decodString(Storefront storefront) {
		if (StringUtil.isNotBlank(storefront.getBusinessScope())) {
			storefront.setBusinessScope(StringUtil.uriDecode(storefront.getBusinessScope(), StandardCharsets.UTF_8));
		}
		if (StringUtil.isNotBlank(storefront.getIntroduction())) {
			storefront.setIntroduction(StringUtil.uriDecode(storefront.getIntroduction(), StandardCharsets.UTF_8));
		}
	}

	@Override
	public IPage<StorefrontVO> getPage(IPage<StorefrontVO> page,StorefrontVO storefrontVO) {
		IPage<StorefrontVO> pages = baseMapper.getPage(page,storefrontVO);
		fillData(pages.getRecords());
		return pages;
	}

	@Override
	public Storefront getByStorefrontNameFullMatch(String storefrontName){
		LambdaQueryChainWrapper<Storefront> partyQW = lambdaQuery().eq(Storefront::getStorefrontName, storefrontName);
		return partyQW.apply("rownum <= 1").one();
	}

	@Override
	public R<IPage<StorefrontVO>> getAllByPage(String companyLogo, String storefrontName, String status, Integer storefrontLevel, String salesrepName, String saleAreaName, String startDate , String endDate, String salesType ,String partyShortName , IPage<StorefrontVO> page) {
		IPage<StorefrontVO> pages = baseMapper.getAllByPage(companyLogo, storefrontName, status, storefrontLevel, salesrepName, saleAreaName, startDate, endDate, salesType, partyShortName, page);
		return R.data(pages);
	}

	@Override
	public LocalDateTime findMaxUpdateDate() {
		return getBaseMapper().findMaxUpdateDate();
	}

	@Override
	public IPage<Storefront> syncParty(IPage<Storefront> page, List<Long> partyId, String dateFrom, String dateTo) {
		Date startDate = null;
		Date endDate = null;

		if (StringUtil.isNotBlank(dateFrom)) {
			if (dateFrom.length() == 10) {
				dateFrom += " 00:00:00";
			}
			startDate = DateUtil.parse(dateFrom, DateUtil.PATTERN_DATETIME);
		}

		if (StringUtil.isNotBlank(dateTo)) {
			if (dateTo.length() == 10) {
				dateTo += " 23:59:59";
			}
			endDate = DateUtil.parse(dateTo, DateUtil.PATTERN_DATETIME);
		}
		return getBaseMapper().syncParty(page, partyId, startDate, endDate);
	}

	@Override
	public List<Storefront> getBySalesmanIdOrSalesmanCodeParty(String ids, String salesmanId, String salesmanCode) {
		LambdaQueryChainWrapper<Storefront> wrapper = this.lambdaQuery();
		if (StringUtil.isNotBlank(ids)) {
			wrapper.in(DBEntity::getId, Arrays.asList(ids.split(",")));
		}
		if (StringUtil.isNotBlank(salesmanId)) {
			wrapper.in(Storefront::getSalesrepId, Arrays.asList(salesmanId.split(",")));
		}
		if (StringUtil.isNotBlank(salesmanCode)) {
			wrapper.in(Storefront::getSalesrepNo, Arrays.asList(salesmanCode.split(",")));
		}
		return wrapper.list();
	}

	@Override
	public void startProcess(Long id){
		if(id == null) {
			return;
		}

		log.info("门店:{},发起流程!", id);
		RabbitUser user = AuthUtil.getUser();
		Storefront storefront = getById(id);

		if (storefront == null){
			log.info("门店发起流程失败,未查询到门店数据:{}",id);
			return;
		}

		// 如果状态为驳回,完成当前节点
		if (Objects.equals(storefront.getStatus(),EshopConstants.STATUS_REJECTED)){
			log.info("门店申请流程驳回后提交完成当前节点:{}",id);
			completeTaskByProcessInstanceId(storefront.getProcessInstanceId());
			return;
		}

		// 发起人
		String initiator = ProcessConstant.ASSIGNEE_PREFIX + user.getUserId();

		// 获取当前时间
		String nowDate = DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATETIME);

		// 添加流程变量
		Map<String, Object> variablesMap = new HashMap<>();
		//业务主键id
		variablesMap.put("data_id_", id);
		//流程标题
		variablesMap.put("business_name_", "门店申请:" + storefront.getStorefrontName());
		//流程发起人
		variablesMap.put("createUserId", user.getUserId());
		//消息标题
		variablesMap.put("title_", "门店申请");
		//消息内容
		variablesMap.put("content_", user.getUserName() + "于" + nowDate + "新增一个门店申请:" + storefront.getStorefrontName());
		//消息模板
		variablesMap.put("template_code_", "eshop_storefront");
		//开始回调函数
		variablesMap.put("startCallbackMethod", "http://eshop-main/storefront/flowInstanceExecutionStartCallback");
		//结束回调函数
		variablesMap.put("endCallbackMethod", "http://eshop-main/storefront/flowInstanceExecutionEndCallback");

		// 流程定义key
		final String processDefinitionKey = "eshop_storefront";

		// 调用流程发起方法
		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey(processDefinitionKey, initiator, true, "", variablesMap);
		// 保存流程id
		if (!rabbitFlowR.isSuccess()) {
			log.error("流程绑定发起失败！,错误信息：{}", rabbitFlowR.getMsg());
			throw new RuntimeException("流程绑定发起失败");
		}

		// 流程发起成功,更新门店数据
		lambdaUpdate().eq(DBEntity::getId,id)
			.set(DBEntity::getStatus,EshopConstants.STATUS_APPROVING)
			.set(Storefront::getProcessInstanceId,rabbitFlowR.getData().getProcessInstanceId())
			.update();

	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto){
		CallBackMethodResDto resDto = new CallBackMethodResDto();

		// 获取业务id
		Long id = Func.toLong(inDto.getBusinessId());
		// 获取当前节点id
		String activityId = inDto.getCurrentActivityId();

		if (Objects.equals(EShopMainConstant.DRAFT_NODE,activityId)){
			if (inDto.isCurrentActivityFromReject()){
				lambdaUpdate().eq(DBEntity::getId,id)
					.set(DBEntity::getStatus,EshopConstants.STATUS_REJECTED)
					.update();
			}
		} else {
			// 查询审批人条件
			ApproverRoles approverRoles = new ApproverRoles();
			approverRoles.setRoleCode(inDto.getDocumentation());
			approverRoles.setAttribute1(inDto.getProcessDefinitionKey());

			// 查询审批人
			List<String> candidateUsers = getApproverRoles(approverRoles);

			// 设置审批人
			resDto.setCandidateUsers(candidateUsers);
		}
		return resDto;
	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto){
		CallBackMethodResDto resDto = new CallBackMethodResDto();

		Long id = Func.toLong(inDto.getBusinessId());
		String activityId = inDto.getCurrentActivityId();
		String processInstanceId = inDto.getProcessInstanceId();

		if (inDto.isAbandon()){
			lambdaUpdate().eq(DBEntity::getId,id)
				.set(DBEntity::getStatus,EshopConstants.STATUS_DISUSE)
				.update();
			return resDto;
		}

		if (Objects.equals(EShopMainConstant.DRAFT_NODE,activityId)){
			lambdaUpdate().eq(DBEntity::getId,id)
				.set(DBEntity::getStatus,EshopConstants.STATUS_APPROVING)
				.set(Storefront::getProcessInstanceId,processInstanceId)
				.update();
		} else if (Objects.equals(EShopMainConstant.END,activityId)){
			lambdaUpdate().eq(DBEntity::getId,id)
				.set(DBEntity::getStatus,EshopConstants.STATUS_NORMAL)
				.update();
		}

		return resDto;
	}

	/**
	 * 查询审批角色
	 * @param approverRoles 审批角色查询条件
	 * @return
	 */
	private List<String> getApproverRoles(ApproverRoles approverRoles){
		List<String> taskUserIdList = new ArrayList<>();

		R<List<String>> userIdListR = sysClient.getUserIdList(approverRoles);
		if (userIdListR.isSuccess() && CollectionUtil.isNotEmpty(userIdListR.getData())){
			taskUserIdList = userIdListR.getData();
		}

		return taskUserIdList;
	}

	/**
	 * 根据流程实例id完成当前节点
	 * @param processInstanceId 流程实id
	 */
	private void completeTaskByProcessInstanceId(String processInstanceId){
		R<List<Activity>> rRunningActivityNodes = flowOpenClient.getRunningActivityNodes(processInstanceId);
		if (rRunningActivityNodes.isSuccess() && CollectionUtil.isNotEmpty(rRunningActivityNodes.getData())){
			RabbitUser user = AuthUtil.getUser();
			Activity activity = rRunningActivityNodes.getData().get(0);
			String taskId = activity.getTaskId();
			String actId = activity.getActId();
			if (StringUtil.isNotBlank(taskId) && Objects.equals(EShopMainConstant.DRAFT_NODE,actId)){
				TaskCompleteDto taskCompleteDto = new TaskCompleteDto();
				taskCompleteDto.setProcessInstanceId(processInstanceId);
				taskCompleteDto.setTaskId(activity.getTaskId());
				taskCompleteDto.setComment("【门店申请流程驳回后提交】");
				taskCompleteDto.setCommentType(CommentTypeEnum.TY.toString());
				taskCompleteDto.setUserId(ProcessConstant.ASSIGNEE_PREFIX + user.getUserId());
				flowOpenClient.complete(taskCompleteDto);
			}
		}
	}

	/**
	 * 补充数据
	 * @param list 门店数据
	 */
	private void fillData(List<StorefrontVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}

		for (StorefrontVO storefrontVO : list) {
			fillDetailData(storefrontVO);
		}
	}

	/**
	 * 补充详情数据
	 * @param storefrontVO 门店数据
	 */
	private void fillDetailData(StorefrontVO storefrontVO){
		if (storefrontVO == null){
			return;
		}

		String dictStatus = storefrontVO.getStatus() == null ? null : storefrontVO.getStatus().toString();
		String dictStorefrontLevel = storefrontVO.getStorefrontLevel() == null ? null : storefrontVO.getStorefrontLevel().toString();

		String statusName = getDictValue(EShopMainConstant.STOREFRONT_STATUS_DICT_CODE, dictStatus);
		String storefrontLevelName = getDictValue(EShopMainConstant.STOREFRONT_LEVEL_DICT_CODE, dictStorefrontLevel);

		storefrontVO.setStatusName(statusName);
		storefrontVO.setStorefrontLevelName(storefrontLevelName);
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

	public String generateNo(int type, int minLen) {
		return PREFIX + autoIncrementIDGenerator.nextDayValue(type, minLen);
	}

}

package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.system.entity.Dict;
import org.springrabbit.system.feign.IDictClient;
import org.springrabbit.system.user.entity.User;
import org.springrabbit.system.user.feign.IUserClient;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.InventoryFlow;
import org.xyg.eshop.main.entity.InventoryManagement;
import org.xyg.eshop.main.entity.InventoryManagementLines;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.mapper.InventoryManagementMapper;
import org.xyg.eshop.main.service.IInventoryFlowService;
import org.xyg.eshop.main.service.IInventoryManagementLinesService;
import org.xyg.eshop.main.service.IInventoryManagementService;
import org.xyg.eshop.main.service.IStorefrontService;
import org.xyg.eshop.main.vo.InventoryManagementVO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InventoryManagementServiceImpl extends BaseServiceImpl<InventoryManagementMapper, InventoryManagement> implements IInventoryManagementService {

	private final IUserClient userClient;
	private final IDictClient dictClient;
	private final IInventoryManagementLinesService linesService;

//	private final IFlowOpenClient flowOpenClient;

	private final AutoIncrementIDGenerator autoIncrementIDGenerator;

	private final IStorefrontService storefrontService;

	private final IInventoryFlowService inventoryFlowService;

	@Override
	public IPage<InventoryManagementVO> getPage(IPage<InventoryManagementVO> page, InventoryManagementVO inventoryManagementVO){
		// 处理日期搜索条件
		processingDate(inventoryManagementVO);

		IPage<InventoryManagementVO> resultPage = baseMapper.getPage(page, inventoryManagementVO);

		// 填充数据
		fillData(resultPage.getRecords());

		return resultPage;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveInventoryManagement(InventoryManagementVO inventoryManagementVO){
		boolean save = saveOrUpdate(inventoryManagementVO);
		// 保存行表数据
		saveLines(inventoryManagementVO);

		// 更新库存流水
		updateInventoryFlow(inventoryManagementVO);

		return save;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long submit(InventoryManagementVO inventoryManagementVO){

		// 生成单号
		if (StringUtil.isBlank(inventoryManagementVO.getDocnum())){
			inventoryManagementVO.setDocnum(generatorDocnum());
		}

		saveOrUpdate(inventoryManagementVO);
		// 保存行表数据
		saveLines(inventoryManagementVO);

		// 更新库存流水
		updateInventoryFlow(inventoryManagementVO);

		return inventoryManagementVO.getId();
	}

	/*@Override
	public void startProcess(Long id) throws ServerException{
		InventoryManagement inventoryManagement = getById(id);
		if (inventoryManagement == null){
			throw new ServerException("发起流程失败,未查询到数据 id = " + id);
		}

		log.info("出入库管理:{},发起流程!", id);
		RabbitUser user = AuthUtil.getUser();

		// 如果状态为驳回,完成当前节点
		if (Objects.equals(inventoryManagement.getStatus(), EshopConstants.STATUS_REJECTED)){
			log.info("门店申请流程驳回后提交完成当前节点:{}",id);
			completeTaskByProcessInstanceId(inventoryManagement.getProcessInstanceId());
			return;
		}

		// 发起人
		String initiator = ProcessUtils.ProcessConstant.ASSIGNEE_PREFIX + user.getUserId();

		// 获取当前时间
		String nowDate = DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATETIME);

		// 添加流程变量
		Map<String, Object> variablesMap = new HashMap<>();
		//业务主键id
		variablesMap.put("data_id_", id);
		//流程标题
		variablesMap.put("business_name_", "出入库申请:" + inventoryManagement.getDocnum());
		//流程发起人
		variablesMap.put("createUserId", user.getUserId());
		//消息标题
		variablesMap.put("title_", "出入库申请");
		//消息内容
		variablesMap.put("content_", user.getUserName() + "于" + nowDate + "新增一个出入库申请:" + inventoryManagement.getDocnum());
		//消息模板
		variablesMap.put("template_code_", "eshop_inventory_management");
		//开始回调函数
		variablesMap.put("startCallbackMethod", "http://eshop-main/inventoryManagement/flowInstanceExecutionStartCallback");
		//结束回调函数
		variablesMap.put("endCallbackMethod", "http://eshop-main/inventoryManagement/flowInstanceExecutionEndCallback");

		// 流程定义key
		final String processDefinitionKey = "eshop_inventory_management";

		// 调用流程发起方法
		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey(processDefinitionKey, initiator, true, "", variablesMap);
		// 保存流程id
		if (!rabbitFlowR.isSuccess()) {
			log.error("流程绑定发起失败！,错误信息：{}", rabbitFlowR.getMsg());
			throw new RuntimeException("流程绑定发起失败");
		}

		// 流程发起成功,修改数据
		lambdaUpdate().eq(DBEntity::getId,id)
			.set(DBEntity::getStatus,EshopConstants.STATUS_APPROVING)
			.set(InventoryManagement::getProcessInstanceId,rabbitFlowR.getData().getProcessInstanceId())
			.update();

	}*/

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateInventoryManagement(InventoryManagementVO inventoryManagementVO){

		boolean update = updateById(inventoryManagementVO);

		saveLines(inventoryManagementVO);

		// 更新库存流水
		updateInventoryFlow(inventoryManagementVO);

		return update;
	}

	@Override
	public InventoryManagementVO detail(Long id){
		InventoryManagementVO inventoryManagementVO = new InventoryManagementVO();

		InventoryManagement inventoryManagement = getById(id);

		if (inventoryManagement == null){
			return inventoryManagementVO;
		}

		inventoryManagementVO = BeanUtil.copyProperties(inventoryManagement,InventoryManagementVO.class);

		// 根据id获取行表数据
		inventoryManagementVO.setLinesList(getLinesByHeadId(id));

		// 补充数据
		fillDetailData(inventoryManagementVO);

		return inventoryManagementVO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean delete(Long id){
		boolean remove = removeById(id);
		// 删除行表数据
		linesService.remove(linesService.lambdaQuery().eq(InventoryManagementLines::getHeadId,id));
		return remove;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean linesDelete(String ids){
		return linesService.removeByIds(Func.toLongList(ids));
	}

	/*@Override
	public CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto){
		CallBackMethodResDto resDto = new CallBackMethodResDto();

		String businessId = inDto.getBusinessId();
		String activityId = inDto.getCurrentActivityId();
		String documentation = inDto.getDocumentation();
		String processDefinitionKey = inDto.getProcessDefinitionKey();

		// 是否起草节点
		if (EShopMainConstant.DRAFT_NODE.equals(activityId)){
			// 是否驳回操作
			if (inDto.isCurrentActivityFromReject()){
				lambdaUpdate().eq(DBEntity::getId,businessId)
					.set(DBEntity::getStatus,EshopConstants.STATUS_REJECTED);
			}
		}

		return resDto;
	}*/

	/*@Override
	public CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto){
		CallBackMethodResDto resDto = new CallBackMethodResDto();

		String businessId = inDto.getBusinessId();
		String activityId = inDto.getCurrentActivityId();

		// 是否结束节点
		if (EShopMainConstant.END.equals(activityId)){
			Integer status = EshopConstants.STATUS_NORMAL;
			// 如果是废弃流程,修改为废弃状态
			if (inDto.isAbandon()){
				status = EshopConstants.STATUS_DISUSE;
			}

			lambdaUpdate().eq(DBEntity::getId,businessId)
				.set(DBEntity::getStatus,status)
				.update();
		}
		return resDto;
	}*/

	/**
	 * 处理日期搜索条件
	 * @param inventoryManagementVO 搜索条件
	 */
	private void processingDate(InventoryManagementVO inventoryManagementVO){
		String startDate = inventoryManagementVO.getStartDate();
		String endDate = inventoryManagementVO.getEndDate();

		if (StringUtil.isNotBlank(startDate) && startDate.length() == 10){
			startDate += " 00:00:00";
			inventoryManagementVO.setStartDate(startDate);
		}

		if (StringUtil.isNotBlank(endDate) && endDate.length() == 10){
			endDate += " 23:59:59";
			inventoryManagementVO.setEndDate(endDate);
		}
	}

	/**
	 * 补充库存管理列表数据
	 * @param list 库存管理列表数据
	 */
	private void fillData(List<InventoryManagementVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}

		for (InventoryManagementVO inventoryManagementVO : list) {
			fillDetailData(inventoryManagementVO);
		}
	}

	/**
	 * 补充详情数据
	 * @param inventoryManagementVO 出入库管理数据
	 */
	private void fillDetailData(InventoryManagementVO inventoryManagementVO){
		if (inventoryManagementVO == null){
			return;
		}
		// 获取创建人名称
		inventoryManagementVO.setCreatedByName(getUserNameById(inventoryManagementVO.getCreatedBy()));

		// 获取单据类型字典值
		String documentType = inventoryManagementVO.getDocumentType();
		if (StringUtil.isNotBlank(documentType)){
			String documentTypeName = getDictValueByKey(EShopMainConstant.INVENTORY_MANAGEMENT_DOCUMENT_TYPE_DICT_CODE, documentType);
			inventoryManagementVO.setDocumentTypeName(documentTypeName);
		}

		// 获取状态字典值
		Integer status = inventoryManagementVO.getStatus();
		if (status != null){
			String statusName = getDictValueByKey(EShopMainConstant.INVENTORY_MANAGEMENT_STATUS_DICT_CODE, status.toString());
			inventoryManagementVO.setStatusName(statusName);
		}

		// 获取门店名称
		if (StringUtil.isBlank(inventoryManagementVO.getStorefrontName())){
			Storefront storefront = getStorefrontById(inventoryManagementVO.getStorefrontId());
			if (storefront != null){
				inventoryManagementVO.setStorefrontName(storefront.getStorefrontName());
			}
		}

		// 查询至门店名称
		Long toStorefrontId = inventoryManagementVO.getToStorefrontId();
		Storefront toStorefront = getStorefrontById(toStorefrontId);
		if (toStorefront != null){
			inventoryManagementVO.setToStorefrontName(toStorefront.getStorefrontName());
		}

	}

	/**
	 * 根据用户id获取用户名称
	 * @param id 用户id
	 * @return
	 */
	private String getUserNameById(Long id){
		String userName = null;
		if (id == null){
			return userName;
		}

		try {
			User user = EShopMainConstant.getData(userClient.userInfoById(id));
			if (user != null){
				userName = user.getRealName();
			}
		} catch (Exception e) {
			log.error("获取出入库管理创建人名称出现错误, id = {}  {}",id,e);
		}
		return userName;
	}

	/**
	 * 根据字典编码和字典key获取字典值
	 * @param dictCode 字典编码
	 * @param dictKey 字典key值
	 * @return
	 */
	private String getDictValueByKey(String dictCode,String dictKey){
		String dictValue = null;
		if (StringUtil.isBlank(dictKey)){
			return dictValue;
		}

		try {
			Dict dict = EShopMainConstant.getData(dictClient.getDictInfo(dictCode, dictKey));
			if (dict != null){
				dictValue = dict.getDictValue();
			}
		} catch (Exception e) {
			log.error("获取出入库管理字典值出现错误 code = {}, key = {}  {}",dictCode,dictKey,e);
		}
		return dictValue;
	}

	/**
	 * 保存出入库管理行表数据
	 * @param inventoryManagementVO 出入库管理数据
	 */
	private void saveLines(InventoryManagementVO inventoryManagementVO){
		if (inventoryManagementVO == null){
			return;
		}
		List<InventoryManagementLines> linesList = inventoryManagementVO.getLinesList();
		if (CollectionUtil.isEmpty(linesList)){
			return;
		}
		for (InventoryManagementLines inventoryManagementLines : linesList) {
			inventoryManagementLines.setHeadId(inventoryManagementVO.getId());
			linesService.saveOrUpdate(inventoryManagementLines);
		}
	}

	/**
	 * 获取行表数据
	 * @param headId 头表id
	 * @return
	 */
	private List<InventoryManagementLines> getLinesByHeadId(Long headId){
		return linesService.lambdaQuery().eq(InventoryManagementLines::getHeadId, headId).list();
	}

	/**
	 * 生成单号
	 * @return
	 */
	public String generatorDocnum(){
		return autoIncrementIDGenerator.nextDayValue(EShopMainConstant.INVENTORY_MANAGEMENT_DOCNUM,3);
	}

	/**
	 * 根据流程实例id完成当前节点
	 * @param processInstanceId 流程实id
	 */
	/*private void completeTaskByProcessInstanceId(String processInstanceId){
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
				taskCompleteDto.setComment("【出入库申请流程驳回后提交】");
				taskCompleteDto.setCommentType(CommentTypeEnum.TY.toString());
				taskCompleteDto.setUserId(ProcessUtils.ProcessConstant.ASSIGNEE_PREFIX + user.getUserId());
				flowOpenClient.complete(taskCompleteDto);
			}
		}
	}*/

	/**
	 * 根据门店id获取门店信息
	 * @param id 门店id
	 * @return
	 */
	private Storefront getStorefrontById(Long id){
		if (id == null){
			return null;
		}
		return storefrontService.getById(id);
	}

	/**
	 * 更新库存流水
	 * @param inventoryManagementVO 出入库管理数据
	 */
	private void updateInventoryFlow(InventoryManagementVO inventoryManagementVO){
		if (inventoryManagementVO == null){
			return;
		}

		List<InventoryManagementLines> linesList = inventoryManagementVO.getLinesList();
		if (CollectionUtil.isEmpty(linesList)){
			return;
		}

		List<InventoryFlow> inventoryFlowList = new ArrayList<>();
		for (InventoryManagementLines inventoryManagementLines : linesList) {
			InventoryFlow inventoryFlow = new InventoryFlow();

			inventoryFlow.setDocnum(inventoryManagementVO.getDocnum());
			inventoryFlow.setDocnumType(inventoryManagementVO.getDocumentType());
			inventoryFlow.setStorefrontId(inventoryManagementVO.getStorefrontId());
			inventoryFlow.setToStorefrontId(inventoryManagementVO.getToStorefrontId());
			inventoryFlow.setStatus(inventoryManagementVO.getStatus());

			inventoryFlow.setProductName(inventoryManagementLines.getProductName());
			inventoryFlow.setProductMode(inventoryManagementLines.getProductMode());
			inventoryFlow.setInventoryQuantity(inventoryManagementLines.getQuantity());
			inventoryFlow.setStorefrontPrice(inventoryManagementLines.getStorefrontPrice());

			inventoryFlowList.add(inventoryFlow);
		}

		inventoryFlowService.saveInventoryFlowList(inventoryFlowList);
	}

}

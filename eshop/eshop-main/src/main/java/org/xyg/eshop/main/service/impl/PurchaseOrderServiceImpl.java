package org.xyg.eshop.main.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.secure.utils.AuthUtil;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.springrabbit.flow.core.entity.RabbitFlow;
import org.springrabbit.flow.core.feign.IFlowOpenClient;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dao.IPurchaseOrderCommodityDao;
import org.xyg.eshop.main.dao.IPurchaseOrderDao;
import org.xyg.eshop.main.dto.PurchaseOrderDTO;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.entity.PurchaseOrderCommodity;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.enums.ContractStatusEnum;
import org.xyg.eshop.main.enums.PurchaseOrderStatusEnum;
import org.xyg.eshop.main.mapper.PurchaseOrderCommodityMapper;
import org.xyg.eshop.main.mapper.PurchaseOrderMapper;
import org.xyg.eshop.main.service.ICommonService;
import org.xyg.eshop.main.service.IPurchaseOrderService;
import org.xyg.eshop.main.service.IStorefrontService;
import org.xyg.eshop.main.vo.PurchaseOrderCommodityVO;
import org.xyg.eshop.main.vo.PurchaseOrderVO;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.xyg.eshop.main.constants.EShopMainConstant.ESCRM_PURCHASE_ORDER_APPROVAL;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseOrderServiceImpl implements IPurchaseOrderService {

	@Autowired
	private AutoIncrementIDGenerator autoIncrementIDGenerator;

	@Autowired
	private IPurchaseOrderDao purchaseOrderDao;

	@Autowired
	private IPurchaseOrderCommodityDao purchaseOrderCommodityDao;

	@Autowired
	private PurchaseOrderMapper purchaseOrderMapper;

	@Autowired
	private PurchaseOrderCommodityMapper purchaseOrderCommodityMapper;

	@Autowired
	protected IFlowOpenClient flowOpenClient;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IStorefrontService storefrontService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> saveOrUpdate(PurchaseOrderDTO purchaseOrderDTO, String saveOrSubmit) {
		if (Func.isEmpty(purchaseOrderDTO.getStorefrontId())) {
			return R.fail("缺少必要的请求参数: storefrontId");
		}
//		if (Func.isEmpty(purchaseOrderDTO.getPurchaseSupplier())) {
//			return R.fail("缺少必要的请求参数: purchaseSupplier");
//		}
//		if (Func.isEmpty(purchaseOrderDTO.getPurchaseApplicant())) {
//			return R.fail("缺少必要的请求参数: purchaseApplicant");
//		}
		purchaseOrderDTO.setPurchaseOrderNumber(generatePurchaseOrderNumber());
		purchaseOrderDTO.setPurchaseStatus(PurchaseOrderStatusEnum.UN_DELIVER.getIndex());
		purchaseOrderDTO.setPurchaseOrderDate(new Date());
		purchaseOrderDTO.setStatus(0);
		boolean savePurchaseOrder = purchaseOrderDao.saveOrUpdate(purchaseOrderDTO);
		if (savePurchaseOrder) {
			// 采购订单商品
			if (purchaseOrderDTO.getPurchaseOrderCommodityList() != null) {
				for (PurchaseOrderCommodity purchaseOrderCommodity : purchaseOrderDTO.getPurchaseOrderCommodityList()) {
					purchaseOrderCommodity.setPurchaseOrderId(purchaseOrderDTO.getId());
				}
				boolean savePurchaseOrderCommodity = purchaseOrderCommodityDao.saveOrUpdateBatch(purchaseOrderDTO.getPurchaseOrderCommodityList());
				if (!savePurchaseOrderCommodity) {
					throw new RuntimeException("新增采购订单商品失败");
				}
			}
			// 不用发起审批流程
//			if(saveOrSubmit.equals("submit")) {
//				addProcess(purchaseOrderDTO.getId());
//			}
			return R.success("新增采购订单成功");
		}
		return R.fail("新增采购订单失败");
	}

	private String generatePurchaseOrderNumber() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date()); // 格式化日期 date: 20210114
		return autoIncrementIDGenerator.nextValueByPrependAndType("YCCG" + date, ESCRM_PURCHASE_ORDER_APPROVAL);
	}

	public void addProcess(Long id) {
		if(id == null) {
			return;
		}
		log.info("采购订单：{}，发起流程！", id);
		RabbitUser user = AuthUtil.getUser();;
		PurchaseOrder purchaseOrder = purchaseOrderDao.getById(id);
		// 添加流程变量
		Map<String, Object> variablesMap = new HashMap<>();
		//业务主键id
		variablesMap.put("data_id_", id);
		//流程标题
		variablesMap.put("business_name_", "新增采购订单" + purchaseOrder.getPurchaseOrderNumber());
		//流程发起人
		variablesMap.put("createUserId", user.getUserId());
		//消息标题
		variablesMap.put("title_", "新增采购订单");
		//消息内容
		variablesMap.put("content_", user.getUserName() +"在" + new Date() + "新增了采购订单");
		//消息模板
		variablesMap.put("template_code_", "eshop_purchase_order_save_template");
		//开始回调函数
		variablesMap.put("startCallbackMethod", "http://eshop-main/purchaseOrder/purchaseOrderSaveExecutionStartCallback");
		//结束回调函数
		variablesMap.put("endCallbackMethod", "http://eshop-main/purchaseOrder/purchaseOrderSaveExecutionEndCallback");

		// 调用流程发起方法
		R<RabbitFlow> rabbitFlowR = flowOpenClient.startProcessByKey("eshop_purchase_order_save_process", "taskUser_" + user.getUserId(), true, "", variablesMap);

		// 保存流程id
		if (rabbitFlowR.isSuccess()) {
			afterAddProcessSuccess(id, rabbitFlowR.getData());
		} else {
			log.error("流程合同发起失败！,错误信息：{}", rabbitFlowR.getMsg());
			throw new RuntimeException("流程合同发起失败");
		}
	}

	private void afterAddProcessSuccess(Long id, RabbitFlow rabbitFlow) {
		purchaseOrderDao.lambdaUpdate()
			.set(PurchaseOrder::getProcessInstanceId, rabbitFlow.getProcessInstanceId())
			.set(PurchaseOrder::getPurchaseStatus, PurchaseOrderStatusEnum.PARTIAL_DELIVER.getIndex())
			.eq(PurchaseOrder::getId, id).update();
	}

	@Override
	public IPage<PurchaseOrderVO> getPage(IPage page, PurchaseOrderDTO purchaseOrderDTO) {
		List<PurchaseOrderVO> purchaseOrderVOList = purchaseOrderMapper.selectPurchaseOrderPage(page, purchaseOrderDTO);
		fillData(purchaseOrderVOList);
		return page.setRecords(purchaseOrderVOList);
	}

	@Override
	public R<PurchaseOrderVO> selectById(Long id) {
		if(Func.isEmpty(id)) {
			return R.fail("缺少必要的请求参数: id");
		}
		PurchaseOrder purchaseOrder = purchaseOrderDao.getById(id);
		if(purchaseOrder == null) {
			return R.fail("查询采购订单信息失败");
		}
		PurchaseOrderVO purchaseOrderVO = BeanUtil.copy(purchaseOrder, PurchaseOrderVO.class);
		fillDetailData(purchaseOrderVO);
		return R.data(purchaseOrderVO);
	}

	private void fillData(List<PurchaseOrderVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}
		for (PurchaseOrderVO purchaseOrderVO : list) {
			fillDetailData(purchaseOrderVO);
		}
	}

	private void fillDetailData(PurchaseOrderVO purchaseOrderVO) {
		if (purchaseOrderVO == null) {
			return;
		}
		String dicStatus = purchaseOrderVO.getPurchaseStatus() == null ? null : purchaseOrderVO.getPurchaseStatus().toString();
		String statusName = commonService.getDictValue(EShopMainConstant.PURCHASE_ORDER_STATUS_DICT_CODE, dicStatus);
		purchaseOrderVO.setStatusName(statusName);
		Storefront storefront = storefrontService.getById(purchaseOrderVO.getStorefrontId());
		if(storefront != null) {
			purchaseOrderVO.setStoreName(storefront.getStorefrontName());
		}
	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto) {
		log.info("流程前置回调方法，前置参数：{}", JSONObject.toJSONString(inDto));
		if(inDto == null) {
			return null;
		}
		String businessId = inDto.getBusinessId();
		if(StringUtils.isBlank(businessId)) {
			return null;
		}
		//根据流程数据更新合同数据
		purchaseOrderDao.lambdaUpdate()
			.set(PurchaseOrder::getCurrApprovePhase, inDto.getCurrentActivityId())
			.eq(PurchaseOrder::getId, Long.valueOf(businessId))
			.update();
		CallBackMethodResDto outDto = new CallBackMethodResDto();
		return outDto;
	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto) {
		log.info("流程后置回调方法，后置参数：{}", JSONObject.toJSONString(inDto));
		CallBackMethodResDto outDto = new CallBackMethodResDto();
		if(inDto == null) {
			return outDto;
		}
		String businessId = inDto.getBusinessId();
		if(StringUtils.isBlank(businessId)) {
			return outDto;
		}
		Long id = Long.valueOf(businessId);
		String activityId = inDto.getCurrentActivityId();
		if(inDto.isAbandon()) {
			//如果流程废弃，则将合同的状态设置成"已驳回"，当前审批节点设置为"起草节点"
			purchaseOrderDao.lambdaUpdate()
				.set(PurchaseOrder::getPurchaseStatus, ContractStatusEnum.REJECTED.getName())
				.set(PurchaseOrder::getCurrApprovePhase, EShopMainConstant.DRAFT_NODE)
				.eq(PurchaseOrder::getId, id)
				.update();
			return outDto;
		}
		if(!inDto.isPass()) {
			return outDto;
		}
		if("store_manager".equals(activityId)) {
			log.info("门店负责人审批");
			purchaseOrderDao.lambdaUpdate()
				.set(PurchaseOrder::getStatus, PurchaseOrderStatusEnum.ALL_DELIVER.getIndex())
				.set(PurchaseOrder::getPurchaseStatus, PurchaseOrderStatusEnum.ALL_DELIVER.getName())
				.eq(PurchaseOrder::getId, id)
				.update();
		}
		return outDto;
	}

	@Override
	public R<List<PurchaseOrderCommodityVO>> selectPurchaseOrderCommodityList(Long id) {
		List<PurchaseOrderCommodityVO> purchaseOrderCommodityVOList = purchaseOrderCommodityMapper.selectListByPurchaseOrderId(id);
		if(purchaseOrderCommodityVOList == null) {
			return R.fail("查询采购商品信息失败");
		}
		return R.data(purchaseOrderCommodityVOList);
	}
}

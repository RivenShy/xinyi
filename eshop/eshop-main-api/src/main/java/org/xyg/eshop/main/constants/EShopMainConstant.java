package org.xyg.eshop.main.constants;

import org.springrabbit.core.tool.api.R;

public interface EShopMainConstant {

	String APPLICATION_ESHOP_MAIN = "eshop-main";

	/**
	 * 易车合同编码
	 */
	Integer ESCRM_CONTRACT_APPROVAL = 3030;

	/**
	 * 易车采购订单号
	 */
	Integer ESCRM_PURCHASE_ORDER_APPROVAL = 3031;

	/**
	 * 易车会员流水号
	 */
	Integer ESCRM_MEMBER_FLOW_SERIAL_NUMBER = 3032;

	/**
	 * 易车调整单编码
	 * 自增-生成调整单编码 以100开头
	 */
	Integer ESHOP_PRODUCT_ADJUSTMENT_RECORD = 1001;

	/**
	 * 起草节点id
	 */
	String DRAFT_NODE = "draft_node";

	/**
	 * 价目表调整单销售单元领导审批节点id
	 */
	String ADJUSTMENT_RECORD_SALES_UNIT = "sales_unit";

	/**
	 * 结束节点id
	 */
	String END = "end";

	/**
	 * 内销价目表固定查询产品库的库存组织id
	 */
	Long PRICE_LIST_ORGANIZATION_ID = 104L;

	/**
	 * 门店状态字典编码
	 */
	String STOREFRONT_STATUS_DICT_CODE = "eshop_signing_status";

	/**
	 * 门店等级字典编码
	 */
	String STOREFRONT_LEVEL_DICT_CODE = "eshop_store_level";

	/**
	 * 门店类型字典编码
	 */
	String STOREFRONT_TYPE_DICT_CODE = "eshop_store_nature";

	/**
	 * 门店员工状态字典编码
	 */
	String STOREFRONT_EMPLOYEE_STATUS_DICT_CODE = "storeEmployee_status";

	/**
	 * 出入库管理单据类型字典编码
	 */
	String INVENTORY_MANAGEMENT_DOCUMENT_TYPE_DICT_CODE = "eshop_inventory_document_type";

	/**
	 * 出入库管理状态字典编码
	 */
	String INVENTORY_MANAGEMENT_STATUS_DICT_CODE = "eshop_inventory_status";

	/**
	 * 门店生成编码标识
	 */
	Integer STOREFRONT_GENERATE_CODE = 8215;

	/**
	 * 出入库管理生成单号标识
	 */
	Integer INVENTORY_MANAGEMENT_DOCNUM = 8310;

	/**
	 * 库存-出库
	 */
	String INVENTORY_OUTBOUND = "2";

	/**
	 * 库存-入库
	 */
	String INVENTORY_INBOUND = "1";

	/**
	 * 库存 0默认值
	 */
	Long INVENTORY_ZERO = 0L;

	/**
	 * 停止库存预警状态
	 */
	Integer INVENTORY_ALERT_STATUS = 4;

	/**
	 * 门店加盟申请中状态
	 */
	Integer STOREFRONT_JOINING_STATUS = 15;

	/**
	 * 门店等待验收状态
	 */
	Integer STOREFRONT_WAIT_ACCEPT_STATUS = 4;

	/**
	 * 门店退出加盟状态
	 */
	Integer STOREFRONT_EXIT_APP_STATUS = 16;

	/**
	 * 加盟申请标识-退出申请
	 */
	String FRANCHISE_FLAG_EXIT_APP = "0";

	/**
	 * 加盟申请状态字典编码
	 */
	String FRANCHISE_STATUS_DICT_CODE = "eshop_storeApplication_status";

	static <T> T getData(R<T> r) {
		if (!r.isSuccess()) {
			return null;
		}
		return r.getData();
	}

	/**
	 * 合同模板类型字典编码
	 */
	String CONTRACT_TEMPLATE_TYPE_DICT_CODE = "eshop_contract_template";

	/**
	 * 合同到期前提醒字典编码
	 */
	String ESHOP_CONTRACT_EXPIRATION_REMIND_DICT_CODE = "eshop_contract_date";

	/**
	 * 合同到期提醒设置通知对象字典编码
	 */
	String ESHOP_NOTIFICATION_OBJECT_DICT_CODE = "eshop_notification_object";

	/**
	 * 合同到期前提醒设置通知方式字典编码
	 */
	String ESHOP_NOTIFICATION_DICT_CODE = "eshop_notification_method";

	/**
	 * 订单生成单号标识
	 */
	Integer PRODUCT_ORDER_NO = 8315;

	/**
	 * 订单生成单号最小长度标识
	 */
	Integer PRODUCT_ORDER_MIN_LEN = 3;


	/**
	 * 门店性质字典编码
	 */
	String ESHOP_STORE_NATURE_DICT_CODE = "eshop_store_nature";

	/**
	 * 活动范围字典编码
	 */
	String ESHOP_ACTIVITY_SCOPE_DICT_CODE = "eshop_activity_scope";

	/**
	 * 活动状态字典编码
	 */
	String ESHOP_ACTIVITY_STATUS_DICT_CODE = "eshop_activity_status";

	/**
	 * 会员签约时长字典编码
	 */
	String ESHOP_MEMBER_SIGNING_TIME_DICT_CODE = "eshop_member_SigningTime";

	/**
	 * 会员车型标准字典编码
	 */
	String ESHOP_CAR_MODEL_STANDARD_DICT_CODE = "eshop_car_model_standard" ;

	/**
	 * 加盟申请标识字典编码
	 */
	String FRANCHISE_FLAG_DICT_CODE = "eshop_franchise_flag";

	/**
	 * 是否字典编码
	 */
	String YES_NO_DICT_CODE = "yes_no";

	/**
	 * 订单状态字典编码
	 */
	String ORDER_STATUS_DICT_CODE = "eshop_order_orderStatus";

	/**
	 * 订单访问状态字典编码
	 */
	String ORDER_VISIT_STATUS_DICT_CODE = "eshop_order_returnVisit";

	/**
	 * 订单状态-待安装
	 */
	Integer ORDER_STATUS_DISTRIBUTE_STORE = 2;


	/**
	 * 合同状态字典编码
	 */
	String CONTRACT_STATUS_DICT_CODE = "eshop_contract_status";

	/**
	 * 合同分类字典编码
	 */
	String CONTRACT_TYPE_DICT_CODE = "eshop_contract_type";

	/**
	 * 采购订单状态字典编码
	 */
	String PURCHASE_ORDER_STATUS_DICT_CODE = "eshop_order_status";

	/**
	 * 会员状态字典编码
	 */
	String MEMBER_STATUS_DICT_CODE = "eshop_member_status";

	/**
	 * 活动类型字典编码
	 */
	String ACTIVITY_TYPE_DICT_CODE = "eshop_activityMG_type";

	String CONTRACT_SCENE_DICT_CODE = "eshop_contract_scenario_add";
}

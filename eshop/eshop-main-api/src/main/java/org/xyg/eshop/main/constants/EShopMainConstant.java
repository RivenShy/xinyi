package org.xyg.eshop.main.constants;

import org.springrabbit.core.tool.api.R;

public interface EShopMainConstant {

	String APPLICATION_ESHOP_MAIN = "eshop-main";

	public static final int ESCRM_CONTRACT_APPROVAL = 3030;

	public static final int ESCRM_PURCHASE_ORDER_APPROVAL = 3031;

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
	String STOREFRONT_STATUS_DICT_CODE = "status";

	/**
	 * 门店等级字典编码
	 */
	String STOREFRONT_LEVEL_DICT_CODE = "storefrontLevel";

	/**
	 * 门店等级字典编码
	 */
	String STOREFRONT_TYPE_DICT_CODE = "store_nature";

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

	static <T> T getData(R<T> r) {
		if (!r.isSuccess()) {
			return null;
		}
		return r.getData();
	}
}

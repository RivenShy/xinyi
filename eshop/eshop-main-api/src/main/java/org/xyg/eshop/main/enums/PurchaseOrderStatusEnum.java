package org.xyg.eshop.main.enums;

public enum PurchaseOrderStatusEnum {
	TO_BE_SUBMITTED("待提交"),
	UNDER_APPROVAL("审批中"),
	REJECTED("已驳回"),
	TO_BE_WAREHOUSED("待入库"),
	WAREHOUSE_COMPLETED("已入库"),
	VOIDED("已作废");

	private String name;

	PurchaseOrderStatusEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}

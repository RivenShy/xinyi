package org.xyg.eshop.main.enums;

public enum ContractStatusEnum {
	NOT_SUBMITTED("未提交"),
	UNDER_APPROVAL("审批中"),
	REJECTED("已驳回"),
	PERFORMING("履行中"),
	EXPIRED("已过期");

	private String name;

	ContractStatusEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}

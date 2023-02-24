package org.xyg.eshop.main.enums;

public enum PurchaseOrderStatusEnum {
	TO_BE_SUBMITTED(0,"未提交"),
	UNDER_APPROVAL(1,"审批中"),
	NORMAL(2,"正常"),
	REJECTED(8,"已驳回"),
	VOIDED(11,"废弃");

	private String name;

	private int index;

	private PurchaseOrderStatusEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return this.name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

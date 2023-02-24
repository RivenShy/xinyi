package org.xyg.eshop.main.enums;

import lombok.Data;

public enum ContractStatusEnum {
	NOT_SUBMITTED(0,"未提交"),
	UNDER_APPROVAL(1,"审批中"),
	REJECTED(2,"已驳回"),
	PERFORMING(3,"履行中"),
	EXPIRED(4,"已过期");

	private String name;
	private int index;

	private ContractStatusEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	ContractStatusEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

package org.xyg.eshop.main.enums;

public enum ContractExpireDateRemindEnum {
	A_WEEK(1, "一周"),
	HALF_MONTH(2, "半个月"),
	A_MONTH(3, "一个月"),
	TWO_MONTH(4, "两个月");

	private String name;
	private int index;

	private ContractExpireDateRemindEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	ContractExpireDateRemindEnum(String name) {
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

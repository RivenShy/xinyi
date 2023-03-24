package org.xyg.eshop.main.enums;

public enum NotificationObjectEnum {
	STORE(1, "门店"),
	SALESMAN(2, "业务员"),
	APPLICANT(3, "申请人");

	private String name;
	private int index;

	private NotificationObjectEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	NotificationObjectEnum(String name) {
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

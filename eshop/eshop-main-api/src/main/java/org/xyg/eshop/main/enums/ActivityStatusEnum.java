package org.xyg.eshop.main.enums;

public enum ActivityStatusEnum {
	NOT_STARTED(1,"未开始"),
	IN_PROGRESS(2,"进行中"),
	EXPIRED(3,"已过期");

	private String name;
	private int index;

	private ActivityStatusEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	ActivityStatusEnum(String name) {
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

package org.xyg.eshop.main.enums;


public enum YesNoEnum {
	YES(2, "是"),
	NO(1, "否");

	private String name;
	private int index;

	private YesNoEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	YesNoEnum(String name) {
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

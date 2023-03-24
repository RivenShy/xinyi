package org.xyg.eshop.main.enums;

public enum ActivityScopeEnum {
	DIRECT_STORE(1, "仅直营店"),
	JOIN_STORE(2, "仅加盟店"),
	DIRECT_AND_JOIN(3, "直营店+加盟店");

	private String name;
	private int index;

	private ActivityScopeEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	ActivityScopeEnum(String name) {
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

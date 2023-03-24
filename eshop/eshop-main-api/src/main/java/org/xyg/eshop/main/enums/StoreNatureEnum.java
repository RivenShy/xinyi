package org.xyg.eshop.main.enums;

public enum StoreNatureEnum {
	DIRECT_STORE(1, "直营店"),
	JOIN_STORE(2, "加盟店");

	private String name;
	private int index;

	private StoreNatureEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	StoreNatureEnum(String name) {
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

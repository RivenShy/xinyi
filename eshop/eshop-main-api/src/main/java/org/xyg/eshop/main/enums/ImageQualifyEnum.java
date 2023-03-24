package org.xyg.eshop.main.enums;

public enum ImageQualifyEnum {
	NO(1, "否"),
	YES(2, "是");

	private String name;
	private int index;

	private ImageQualifyEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	ImageQualifyEnum(String name) {
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

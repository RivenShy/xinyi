package org.xyg.eshop.main.enums;

public enum MemberValidityEnum {
	ONE_YEAR(1, "一年"),
	TWO_YEAR(2, "二年"),
	THREE_YEAR(3, "三年");

	private String name;
	private int index;

	private MemberValidityEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	MemberValidityEnum(String name) {
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

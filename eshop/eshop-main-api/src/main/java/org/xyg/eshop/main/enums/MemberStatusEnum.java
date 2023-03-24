package org.xyg.eshop.main.enums;

public enum MemberStatusEnum {
	UNDER_REVIEW(1, "审核中"),

	NORMAL(2, "正常"),

	OBSERVATION(3, "观察期"),

	EXPIRED(4, "已过期");

	private String name;
	private int index;

	private MemberStatusEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	MemberStatusEnum(String name) {
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

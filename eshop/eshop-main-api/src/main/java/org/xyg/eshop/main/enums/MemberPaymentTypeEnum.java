package org.xyg.eshop.main.enums;

public enum MemberPaymentTypeEnum {
	NEW("1","新增"),
	RENEW("2","续签"),
	ACTIVATION("3","激活");

	private String name;
	private String index;

	private MemberPaymentTypeEnum(String index, String name) {
		this.name = name;
		this.index = index;
	}

	MemberPaymentTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}

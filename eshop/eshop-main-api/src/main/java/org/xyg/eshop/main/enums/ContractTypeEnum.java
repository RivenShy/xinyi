package org.xyg.eshop.main.enums;
public enum ContractTypeEnum {
	JOIN(1, "加盟合同"),
	SELL(2, "销售合同"),
	PURCHASE(3, "采购合同"),
	RENT(4, "租赁合同"),
	ENGINEERING(5, "工程合同"),
	TECHNOLOGY(6, "技术合同"),
	SERVICE(7, "服务合同"),
	CHANGE(8, "变更合同"),
	SUPPLEMENT(9, "补充协议");

	private String name;
	private int index;

	private ContractTypeEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	ContractTypeEnum(String name) {
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

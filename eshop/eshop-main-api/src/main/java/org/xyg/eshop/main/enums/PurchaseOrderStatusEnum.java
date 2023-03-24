package org.xyg.eshop.main.enums;

public enum PurchaseOrderStatusEnum {
	UN_DELIVER(0,"未交货"),
	PARTIAL_DELIVER(1,"部分交货"),
	ALL_DELIVER(2,"全部交货");

	private String name;

	private int index;

	private PurchaseOrderStatusEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return this.name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

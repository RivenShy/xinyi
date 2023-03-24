package org.xyg.eshop.main.enums;

public enum CarModelStandardEnum {
	NON_OPERATE_VEHICLE(1, "30万以下非营运车"),
	ADVANCE_ASSIST_DRIVE(2, "30万以下高级辅助驾驶功能");

	private String name;
	private int index;

	private CarModelStandardEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	CarModelStandardEnum(String name) {
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

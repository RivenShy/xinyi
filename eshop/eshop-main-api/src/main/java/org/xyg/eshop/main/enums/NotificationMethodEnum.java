package org.xyg.eshop.main.enums;

public enum NotificationMethodEnum {
	SYSTEM(1, "系统消息"),
	WECHAT_APPLET(2, "小程序消息"),
	SHORT_MESSAGE(3, "短信");

	private String name;
	private int index;

	private NotificationMethodEnum(int index, String name) {
		this.name = name;
		this.index = index;
	}

	NotificationMethodEnum(String name) {
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

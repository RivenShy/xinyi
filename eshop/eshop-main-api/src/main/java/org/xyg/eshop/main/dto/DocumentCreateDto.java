package org.xyg.eshop.main.dto;

public class DocumentCreateDto {

	/**
	 * 业务系统
	 */
	private String businessSystem;

	/**
	 * 业务id
	 */
	private String businessId;

	/**
	 * 模板id
	 */
	private Long templateId;

	/**
	 * 业务数据（json字符串）
	 */
	private String businessData;

	/**
	 * 文档类型，1：word，2：excel，99：其他
	 */
	private Integer type;

	public String getBusinessSystem() {
		return businessSystem;
	}

	public void setBusinessSystem(String businessSystem) {
		this.businessSystem = businessSystem;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getBusinessData() {
		return businessData;
	}

	public void setBusinessData(String businessData) {
		this.businessData = businessData;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}


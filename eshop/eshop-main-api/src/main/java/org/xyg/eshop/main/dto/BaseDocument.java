package org.xyg.eshop.main.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springrabbit.core.mp.base.BaseEntity;

public class BaseDocument extends BaseEntity {

	/**
	 * 业务系统
	 */
	@ApiModelProperty(value = "业务系统")
	private String businessSystem;

	/**
	 * 文档附件ID
	 */
	@ApiModelProperty(value = "文档附件ID")
	private Long attachId;

	/**
	 * 文档附件地址
	 */
	@ApiModelProperty(value = "文档附件地址")
	private String link;

	/**
	 * 文档名称
	 */
	@ApiModelProperty(value = "文档名称")
	private String name;

	/**
	 * 文档原名
	 */
	@ApiModelProperty(value = "文档原名")
	private String originalName;

	/**
	 * 文档类型，1：word，2：excel，99：其他
	 */
	@ApiModelProperty(value = "文档类型，1：word，2：excel，99：其他")
	private Integer type;

	/**
	 * 文档类型，1：word，2：excel，99：其他
	 */
	@ApiModelProperty(value = "文档扩展名")
	private String extendName;

	public String getBusinessSystem() {
		return businessSystem;
	}

	public void setBusinessSystem(String businessSystem) {
		this.businessSystem = businessSystem;
	}

	public Long getAttachId() {
		return attachId;
	}

	public void setAttachId(Long attachId) {
		this.attachId = attachId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getExtendName() {
		return extendName;
	}

	public void setExtendName(String extendName) {
		this.extendName = extendName;
	}
}

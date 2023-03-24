package org.xyg.eshop.main.enums;

import lombok.Data;

public enum ContractTemplateTypeEnum {
	COMMON_JOIN("普通加盟合同"),
	COMMON_PURCHASE("普通采购合同"),
	COMMON_SERVICE("普通服务合同"),
	COMMON_DEALER("普通经销商合同");

	private String name;

	private ContractTemplateTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}

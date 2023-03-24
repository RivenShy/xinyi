package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.MemberFlow;

import java.io.Serializable;

@Data
public class MemberFlowVO extends MemberFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "服务门店名称")
	private String storefrontName;
}

package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.Member;
import java.io.Serializable;

@Data
public class MemberVO extends Member implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "门店名称")
	private String storefrontName;

	@ApiModelProperty(value = "状态名称")
	private String statusName;

	@ApiModelProperty(value = "有效期/签约时长名称")
	private String termOfValidityName;

	@ApiModelProperty(value = "车型标准名称")
	private String carModelStandardName;
}

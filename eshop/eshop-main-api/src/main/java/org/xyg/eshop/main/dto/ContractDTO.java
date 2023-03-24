package org.xyg.eshop.main.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;

import java.io.Serializable;
import java.util.List;

@Data
public class ContractDTO extends Contract implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "合同相关人员")

	private List<ContractRelatePersonnel> personnelList;

	@ApiModelProperty(value = "状态列表")
	private List<String> contractStatusList;

	@ApiModelProperty(value = "合同过期时间查询列表")

	private List<String> contractExpireDateQueryList;

	@ApiModelProperty(value = "查询一周内过期合同")
	private String queryWithinWeek;

	@ApiModelProperty(value = "查询一个月内过期合同")
	private String queryWithinMonth;

	@ApiModelProperty(value = "查询半个月内过期合同")
	private String queryWithinHalfMonth;

	@ApiModelProperty(value = "查询两个月内过期合同")
	private String queryWithinTwoMonth;

	@ApiModelProperty(value = "门店名称")
	private String storeName;
}

package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;

import java.io.Serializable;
import java.util.List;

@Data
public class ContractVO extends Contract implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "合同相关人员")
	private List<ContractRelatePersonnelVO> personnelList;

	@ApiModelProperty(value = "门店名称")
	private String storeName;

	@ApiModelProperty(value = "状态名称")
	private String statusName;

	@ApiModelProperty(value = "合同分类名称")
	private String contractTypeName;

	@ApiModelProperty(value = "合同场景名称")
	private String contractSceneName;
}

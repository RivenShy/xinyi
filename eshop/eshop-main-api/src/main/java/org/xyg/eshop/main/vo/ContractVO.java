package org.xyg.eshop.main.vo;

import lombok.Data;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;

import java.io.Serializable;
import java.util.List;

@Data
public class ContractVO extends Contract implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ContractRelatePersonnelVO> personnelList;

	private String storeName;
}

package org.xyg.eshop.main.dto;

import lombok.Data;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.entity.ContractRelatePersonnel;

import java.io.Serializable;
import java.util.List;

@Data
public class ContractDTO extends Contract implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ContractRelatePersonnel> personnelList;

	private List<String> contractStatusList;

	private List<String> contractExpireDateQueryList;

	private String queryWithinWeek;

	private String queryWithinMonth;

	private String queryWithinHalfMonth;

	private String queryWithinTwoMonth;

	private String contractExpireDateQuerySql;

	private String storeName;
}

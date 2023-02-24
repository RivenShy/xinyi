package org.xyg.eshop.main.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xyg.eshop.main.entity.StorefrontFranchise;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "易车-加盟店数据传输对象", description = "易车-加盟店数据传输对象")
public class StorefrontFranchiseDTO extends StorefrontFranchise implements Serializable {

	private static final long serialVersionUID = -671026117494107833L;
}

package org.xyg.eshop.main.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xyg.eshop.main.entity.StorefrontAcceptance;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "易车-门店验收据传输对象", description = "易车-门店验收据传输对象")
public class StorefrontAcceptanceDTO extends StorefrontAcceptance implements Serializable {

	private static final long serialVersionUID = 2379053218319765011L;
}

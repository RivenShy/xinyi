package org.xyg.eshop.main.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xyg.eshop.main.entity.StorefrontAcceptance;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "易车-门店验收vo对象", description = "易车-门店验收vo对象")
public class StorefrontAcceptanceVO extends StorefrontAcceptance implements Serializable {

	private static final long serialVersionUID = 1215378282426641341L;
}

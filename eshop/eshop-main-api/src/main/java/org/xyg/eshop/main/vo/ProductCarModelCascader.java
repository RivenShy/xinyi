package org.xyg.eshop.main.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductCarModelCascader {
	private String label ;
	private String value ;
	private String msg ;
	private List<ProductCarModelCascader> children ;
}

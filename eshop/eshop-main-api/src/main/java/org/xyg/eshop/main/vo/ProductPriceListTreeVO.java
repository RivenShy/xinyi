package org.xyg.eshop.main.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springrabbit.core.tool.node.INode;
import org.xyg.eshop.main.entity.ProductPriceList;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "易车-产品价目表懒加载VO对象", description = "ProductPriceListLazyVO")
public class ProductPriceListTreeVO extends ProductPriceList implements INode<ProductPriceListTreeVO> {

	private static final long seriaVersionUID = 1L;

	/**
	 * 父节点ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

	/**
	 * 子孙节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<ProductPriceListTreeVO> children;

	/**
	 * 是否有子孙节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Boolean hasChildren;

	@Override
	public List<ProductPriceListTreeVO> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		return this.children;
	}

}

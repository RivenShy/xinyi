package org.xyg.eshop.main.vo;

import lombok.Data;
import org.xyg.eshop.main.entity.ProductCarModel;
import org.xyg.eshop.main.entity.ProductModelAttachment;
import org.xyg.eshop.main.entity.ProductModelLines;

import java.util.List;

@Data
public class ProductModelMergeVO extends ProductCarModel {
	private List<ProductModelAttachment> productModelAttachmentsList;

	private List<ProductModelLines> productModelLinesList;
}

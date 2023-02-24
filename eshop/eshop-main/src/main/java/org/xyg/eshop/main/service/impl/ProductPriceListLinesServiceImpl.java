package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.base.DBEntity;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.entity.ProductPriceListLines;
import org.xyg.eshop.main.excel.ProductPriceListExportExcel;
import org.xyg.eshop.main.mapper.ProductPriceListLinesMapper;
import org.xyg.eshop.main.service.IProductPriceListLinesService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductPriceListLinesServiceImpl extends BaseServiceImpl<ProductPriceListLinesMapper, ProductPriceListLines> implements IProductPriceListLinesService {

	@Override
	public R<IPage<ProductPriceListLines>> getPage(IPage<ProductPriceListLines> page,
												   Long headId,
												   String productModel,
												   String position,
												   String brand,
												   String model,
												   String itemChDescription,
												   Long maxWidth,
												   Long minWidth,
												   Long maxLength,
												   Long minLength,
												   String oemModel,
												   Integer machiningType){
		LambdaQueryChainWrapper<ProductPriceListLines> ppLinesQW = lambdaQuery().eq(ProductPriceListLines::getHeadId, headId);

		if (StringUtil.isNotBlank(productModel)){
			ppLinesQW.like(ProductPriceListLines::getProductModel,productModel);
		}

		if (StringUtil.isNotBlank(position)){
			ppLinesQW.like(ProductPriceListLines::getPosition,position);
		}

		if (StringUtil.isNotBlank(brand)){
			ppLinesQW.like(ProductPriceListLines::getBrand,brand);
		}

		if (StringUtil.isNotBlank(model)){
			ppLinesQW.like(ProductPriceListLines::getModel,model);
		}

		if (StringUtil.isNotBlank(itemChDescription)){
			ppLinesQW.like(ProductPriceListLines::getItemChDescription,itemChDescription);
		}

		if (maxWidth != null){
			ppLinesQW.le(ProductPriceListLines::getWidth,maxWidth);
		}

		if (minWidth != null){
			ppLinesQW.ge(ProductPriceListLines::getWidth,minWidth);
		}

		if (maxLength != null){
			ppLinesQW.le(ProductPriceListLines::getLength,maxLength);
		}

		if (minLength != null){
			ppLinesQW.ge(ProductPriceListLines::getLength,minLength);
		}

		if (StringUtil.isNotBlank(oemModel)){
			ppLinesQW.like(ProductPriceListLines::getOemModel,oemModel);
		}

		if (machiningType != null){
			ppLinesQW.eq(ProductPriceListLines::getMachiningType,machiningType);
		}

		ppLinesQW.orderByDesc(DBEntity::getLastUpdateDate)
			.orderByDesc(DBEntity::getId);

		return R.data(ppLinesQW.page(page));
	}

	@Override
	public R<String> submit(ProductPriceListLines productPriceListLines){
		return R.status(save(productPriceListLines));
	}

	@Override
	public R<String> saveProductPriceListLines(ProductPriceListLines productPriceListLines){
		return R.status(save(productPriceListLines));
	}

	@Override
	public R<String> updateProductPriceListLines(ProductPriceListLines productPriceListLines){
		return R.status(updateById(productPriceListLines));
	}

	@Override
	public R<ProductPriceListLines> getDetail(Long id) {
		ProductPriceListLines productPriceListLines = getById(id);
		return R.data(productPriceListLines);
	}

	@Override
	public List<ProductPriceListExportExcel> exportPriceListLines(ProductPriceListLines lines){
		if (lines.getHeadId() == null){
			return new ArrayList<>();
		}
		return baseMapper.exportPriceListLines(lines);
	}
}

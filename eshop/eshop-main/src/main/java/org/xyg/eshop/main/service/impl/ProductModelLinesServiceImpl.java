package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.xyg.eshop.main.entity.ProductModelLines;
import org.xyg.eshop.main.excel.product.ProductModelLinesExcel;
import org.xyg.eshop.main.excel.product.ProductModelLinesExportExcel;
import org.xyg.eshop.main.mapper.ProductModelLinesMapper;
import org.xyg.eshop.main.service.IProductModelLinesService;
import org.xyg.eshop.main.vo.ProductModelLinesVO;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductModelLinesServiceImpl extends BaseServiceImpl<ProductModelLinesMapper, ProductModelLines> implements IProductModelLinesService {
	@Override
	public R<IPage<ProductModelLines>> getPage(IPage<ProductModelLines> iPage) {
		IPage ipage1 = page(iPage);
		return R.data(ipage1);
	}

	@Override
	public R<Boolean> insertData(ProductModelLines entity) {
		boolean saveData = save(entity);
		return R.data(saveData);
	}

	@Override
	public R<Boolean> deleteData(Long id) {
		boolean removeData = removeById(id);
		return R.data(removeData);
	}

	@Override
	public R<Boolean> updateDate(ProductModelLines entity) {
		boolean updateById = updateById(entity);
		return R.data(updateById);
	}

	@Override
	public R<ProductModelLinesVO> getDetail(Long id) {
		ProductModelLinesVO detail = baseMapper.getDetail(id);
		return R.data(detail);
	}

	@Override
	public R<List<ProductModelLines>> getList(Long headId) {
		LambdaQueryWrapper<ProductModelLines> queryWrapper = new LambdaQueryWrapper<>();
		if (headId != null) {
			queryWrapper.eq(ProductModelLines::getHeaderId, headId);
		}
		List<ProductModelLines> modelAttachments = list(queryWrapper);
		return R.data(modelAttachments);
	}

	@Override
	public void savaExcelImporter(List<ProductModelLinesExcel> entity, Long headId) {
		if (CollectionUtil.isNotEmpty(entity)) {
			List<ProductModelLines> productModelLinesList = BeanUtil.copyProperties(entity, ProductModelLines.class);
			for (ProductModelLines productModelLines : productModelLinesList) {
				productModelLines.setHeaderId(headId);
				save(productModelLines);
			}
		} else {
			throw new ServiceException("excel产品型号数据为空");
		}
	}

	@Override
	public List<ProductModelLinesExportExcel> exportExcelList() {
		return baseMapper.exportExcelList();
	}

	@Override
	public LocalDateTime findMaxUpdate() {
		return getBaseMapper().findMaxUpdate();
	}

	@Override
	public List<ProductModelLines> getModelLines(String xygType, String partyColor, String attachment, String features , Integer orgId) {
		List<ProductModelLines> baseMapperModelLines = baseMapper.getModelLines(xygType, partyColor, attachment, features , orgId);
		return baseMapperModelLines;
	}

	@Override
	public List<ProductModelLines> getModelList(List<String> materialCodes, List<String> materialIds, List<String> factoryModes) {
		return getBaseMapper().getModelList(materialCodes, materialIds, factoryModes);
	}
}

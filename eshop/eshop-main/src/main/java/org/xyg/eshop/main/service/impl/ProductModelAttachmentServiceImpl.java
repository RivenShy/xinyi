package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springrabbit.core.log.exception.ServiceException;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.xyg.eshop.main.entity.ProductModelAttachment;
import org.xyg.eshop.main.excel.product.ProductModelAttachmentExcel;
import org.xyg.eshop.main.mapper.ProductModelAttachmentMapper;
import org.xyg.eshop.main.service.IProductModelAttachmentService;
import org.xyg.eshop.main.vo.ProductModelAttachmentVO;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductModelAttachmentServiceImpl extends BaseServiceImpl<ProductModelAttachmentMapper, ProductModelAttachment> implements IProductModelAttachmentService {
	@Override
	public R<IPage<ProductModelAttachment>> getPage(IPage<ProductModelAttachment> iPage) {
		IPage<ProductModelAttachment> pageProduct = page(iPage);
		return R.data(pageProduct);
	}

	@Override
	public R<Boolean> insertData(ProductModelAttachment entity) {
		boolean saveProduct = save(entity);
		return R.data(saveProduct);
	}

	@Override
	public R<Boolean> deleteData(Long id) {
		boolean removeProduct = removeById(id);
		return R.data(removeProduct);
	}

	@Override
	public R<Boolean> updateDate(ProductModelAttachment entity) {
		boolean updateProduct = updateById(entity);
		return R.data(updateProduct);
	}

	@Override
	public R<ProductModelAttachmentVO> getDetail(Long id) {
		ProductModelAttachmentVO detail = baseMapper.getDetail(id);
		return R.data(detail);
	}

	@Override
	public R<List<ProductModelAttachment>> getList(Long headId, String factoryMode) {
		LambdaQueryWrapper<ProductModelAttachment> queryWrapper = new LambdaQueryWrapper<>();
		if (headId != null) {
			queryWrapper.eq(ProductModelAttachment::getHeaderId, headId);
		}
		if (StringUtil.isNotBlank(factoryMode)) {
			queryWrapper.eq(ProductModelAttachment::getFactoryMode, factoryMode);
		}
		queryWrapper.orderByDesc(ProductModelAttachment::getCreationDate);
		List<ProductModelAttachment> modelAttachments = list(queryWrapper);
		return R.data(modelAttachments);
	}

	@Override
	public void savaExcelImporter(List<ProductModelAttachmentExcel> entity, Long headId) {
		if (CollectionUtil.isNotEmpty(entity)) {
			if (headId != null) {
				List<ProductModelAttachment> productModelAttachmentList = BeanUtil.copyProperties(entity, ProductModelAttachment.class);
				for (ProductModelAttachment productModelAttachment : productModelAttachmentList) {
					productModelAttachment.setHeaderId(headId);
					save(productModelAttachment);
				}
			} else {
				throw new ServiceException("产品库车产车型ID为空");
			}
		} else {
			throw new ServiceException("excel附件信息数据为空");
		}
	}

	@Override
	public LocalDateTime findMaxUpdateDate() {
		return getBaseMapper().findMaxUpdateDate();
	}
}

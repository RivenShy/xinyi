package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.entity.ProductModelAttachment;
import org.xyg.eshop.main.excel.product.ProductModelAttachmentExcel;
import org.xyg.eshop.main.vo.ProductModelAttachmentVO;

import java.time.LocalDateTime;
import java.util.List;

public interface IProductModelAttachmentService extends BaseService<ProductModelAttachment> {

	/**
	 * 分页查询数据
	 *
	 * @param iPage
	 * @return
	 */
	R<IPage<ProductModelAttachment>> getPage(IPage<ProductModelAttachment> iPage);

	/**
	 * 插入数据
	 *
	 * @param entity
	 * @return
	 */
	R<Boolean> insertData(ProductModelAttachment entity);

	/**
	 * 根据ID删除数据
	 *
	 * @param id
	 * @return
	 */
	R<Boolean> deleteData(Long id);

	/**
	 * 更新数据
	 *
	 * @param entity
	 * @return
	 */
	R<Boolean> updateDate(ProductModelAttachment entity);

	/**
	 * 获取详情
	 *
	 * @param id
	 * @return
	 */
	R<ProductModelAttachmentVO> getDetail(Long id);

	/**
	 * 获取列表数据
	 *
	 * @param headId
	 * @return
	 */
	R<List<ProductModelAttachment>> getList(Long headId, String factoryMode);

	void savaExcelImporter(List<ProductModelAttachmentExcel> entity, Long headId);

	/**
	 *
	 * 获取最大的最后更新日期
	 *
	 * @return {@link LocalDateTime}
	 */
	LocalDateTime findMaxUpdateDate();
}

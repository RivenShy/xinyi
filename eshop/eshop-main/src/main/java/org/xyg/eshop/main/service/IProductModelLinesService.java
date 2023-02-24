package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.entity.ProductModelLines;
import org.xyg.eshop.main.excel.product.ProductModelLinesExcel;
import org.xyg.eshop.main.excel.product.ProductModelLinesExportExcel;
import org.xyg.eshop.main.vo.ProductModelLinesVO;

import java.time.LocalDateTime;
import java.util.List;

public interface IProductModelLinesService extends BaseService<ProductModelLines> {

	/**
	 * 分页查询数据
	 *
	 * @param iPage
	 * @return
	 */
	R<IPage<ProductModelLines>> getPage(IPage<ProductModelLines> iPage);

	/**
	 * 插入数据
	 *
	 * @param entity
	 * @return
	 */
	R<Boolean> insertData(ProductModelLines entity);

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
	R<Boolean> updateDate(ProductModelLines entity);

	/**
	 * 获取详情
	 *
	 * @param id
	 * @return
	 */
	R<ProductModelLinesVO> getDetail(Long id);

	/**
	 * 获取列表数据
	 *
	 * @param headId
	 * @return
	 */
	R<List<ProductModelLines>> getList(Long headId);

	void savaExcelImporter(List<ProductModelLinesExcel> entity, Long headId);

	/**
	 * 导出产品型号数据
	 *
	 * @return
	 */
	List<ProductModelLinesExportExcel> exportExcelList();

	/**
	 * 获取最大的更新日期
	 *
	 * @return {@link LocalDateTime}
	 */
	LocalDateTime findMaxUpdate();

	/**
	 * 根据xyg型号、颜色、附件、特性查询行数据
	 * @param xygType
	 * @param partyColor
	 * @param attachment
	 * @param features
	 * @return
	 */
	List<ProductModelLines> getModelLines(String xygType , String partyColor , String attachment , String features , Integer orgId);

	/**
	 * 根据物料编码、物料id、本厂型号查询数据,会对数据进行去重
	 *
	 * @param materialCodes 物料编码
	 * @param materialIds 物料id
	 * @param factoryModes 本厂型号
	 * @return
	 */
	List<ProductModelLines> getModelList(List<String> materialCodes,
										 List<String> materialIds,
										 List<String> factoryModes);
}

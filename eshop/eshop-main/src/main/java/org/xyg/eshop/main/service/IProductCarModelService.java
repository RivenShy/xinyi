package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.protobuf.ServiceException;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.entity.ProductCarModel;
import org.xyg.eshop.main.entity.ProductModelLines;
import org.xyg.eshop.main.vo.*;

import java.util.List;

public interface IProductCarModelService extends BaseService<ProductCarModel> {

	/**
	 * 分页查询数据
	 * @param iPage
	 * @return
	 */
	R<IPage<ProductCarModel>> getPage(IPage<ProductCarModel> iPage);

	/**
	 * 插入数据
	 *
	 * @param entity
	 * @return
	 */
	R<Boolean> insertData(ProductModelMergeVO entity) throws ServiceException;

	/**
	 * 根据ID删除数据
	 *
	 * @param id
	 * @return
	 */
	R<Boolean> deleteData(Long id) throws ServiceException;

	/**
	 * 更新数据
	 *
	 * @param entity
	 * @return
	 */
	R<Boolean> updateDate(ProductModelMergeVO entity) throws ServiceException;


	/**
	 * 获取详情
	 *
	 * @param id
	 * @return
	 */
	R<ProductCarModelVO> getDetail(Long id);

	R<IPage<ProductCarModelVO>> getProductPage(IPage<ProductModelLines> iPage, ProductCarModelVO param) throws ServiceException;

	R<List<ProductCarModelVO>> getProductList(Long productLineId , String xygType , String attachment) throws ServiceException;


	R<List<ProductCarModelVO>> getProductColor(Long productLineId , String xygType , String attachment , String colour);

	R<List<ProductCarModelCascader>> findProductCarModel();

	/**
	 * 产品价目表查询车厂车型
	 * @param linesToPrictListVO 搜索条件
	 * @param page 分页参数
	 * @return
	 */
	IPage<ProductModelLinesToPrictListVO> getToPriceListPage(IPage<ProductModelLinesToPrictListVO> page, ProductModelLinesToPrictListVO linesToPrictListVO);

	/**
	 * 保存行表数据
	 * @param productModelLinesVO 产品库行表数据
	 * @return
	 */
	ProductModelLinesVO linesSave(ProductModelLinesVO productModelLinesVO);

	/**
	 * 更新行表数据
	 * @param productModelLinesVO 行表数据
	 * @return
	 */
	ProductModelLinesVO linesUpdate(ProductModelLinesVO productModelLinesVO);

	/**
	 * 获取行表详情
	 * @param id 行表id
	 * @return
	 */
	ProductModelLinesVO linesDetail(Long id);

}

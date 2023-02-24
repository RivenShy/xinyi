package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.entity.ProductPriceListLines;
import org.xyg.eshop.main.excel.ProductPriceListExportExcel;

import java.util.List;

public interface IProductPriceListLinesService extends BaseService<ProductPriceListLines> {

	/**
	 * 分页查询产品价目行表
	 * @param page 分页参数
	 * @param headId 头表id
	 * @param productModel 本厂型号
	 * @param position 装车位置
	 * @param brand 品牌
	 * @param model 车型
	 * @param itemChDescription 产品名称
	 * @param maxWidth 宽:最大值
	 * @param minWidth 宽:最小值
	 * @param maxLength 长:最大值
	 * @param minLength 长:最小值
	 * @param oemModel OEM型号
	 * @param machiningType 加工类型
	 */
	R<IPage<ProductPriceListLines>> getPage(IPage<ProductPriceListLines> page,Long headId, String productModel, String position, String brand, String model, String itemChDescription, Long maxWidth, Long minWidth, Long maxLength, Long minLength, String oemModel,Integer machiningType);

	/**
	 * 提交产品价目表行表
	 * @param productPriceListLines 产品价目表行表数据
	 * @return
	 */
	R<String> submit(ProductPriceListLines productPriceListLines);

	/**
	 * 保存产品价目表行表
	 * @param productPriceListLines 产品价目表行表数据
	 * @return
	 */
	R<String> saveProductPriceListLines(ProductPriceListLines productPriceListLines);

	/**
	 * 修改产品价目表行表
	 * @param productPriceListLines 产品价目表行表数据
	 * @return
	 */
	R<String> updateProductPriceListLines(ProductPriceListLines productPriceListLines);

	/**
	 * 查询详情
	 *
	 * @param id
	 * @return
	 */
	R<ProductPriceListLines> getDetail(Long id);

	/**
	 * 查询价目表导出数据
	 * @param lines 搜索条件
	 * @return
	 */
    List<ProductPriceListExportExcel> exportPriceListLines(ProductPriceListLines lines);

}

package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.entity.ProductPriceList;
import org.xyg.eshop.main.vo.ProductPriceListTreeVO;
import org.xyg.eshop.main.vo.ProductPriceListVO;

import java.util.List;

public interface IProductPriceListService extends BaseService<ProductPriceList> {

	/**
	 * 分页查询数据
	 *
	 * @param page
	 * @return
	 */
	IPage<ProductPriceList> getPage(IPage<ProductPriceList> page);

	/**
	 * 提交产品价目表数据
	 * @param productPriceList 产品价目表数据
	 * @return
	 */
	R<String> submit(ProductPriceList productPriceList);

	/**
	 * 保存产品价目表数据
	 * @param productPriceList 产品价目表数据
	 * @return
	 */
	R<String> saveProductPriceList(ProductPriceList productPriceList);

	/**
	 * 更新产品价目表数据
	 * @param productPriceList 产品价目表数据
	 * @return
	 */
	R<String> updateProductPriceList(ProductPriceList productPriceList);

	/**
	 * 获取价目表懒加载树型列表数据
	 * @param parentId 父id
	 * @param priceListName 价目表名称
	 * @param type 内外销
	 * @param regionOrPartyName 区域/客户
	 * @return
	 */
	R<List<ProductPriceListTreeVO>> getTreeList(Long parentId, String priceListName, Integer type, String regionOrPartyName);

	/**
	 * 获取价目表集合
	 * @param parentId 父id
	 * @param priceListName 价目表名称
	 * @param type 内外销
	 * @param regionOrPartyName 区域/客户
	 * @return
	 */
	R<List<ProductPriceList>> getList(Long parentId, String priceListName, Integer type, String regionOrPartyName , Long id , String storefrontType , Integer isValid);

	/**
	 * 查询产品价目表详情
	 * @param id 主键id
	 * @return
	 */
	R<ProductPriceListVO> detail(Long id);

	/**
	 * 根据内外销获取价目表分页列表
	 * @param page 分页参数
	 * @param type 类型
	 * @param priceListName 价目表名称
	 * @return
	 */
	IPage<ProductPriceListVO> getByTypePage(IPage<ProductPriceListVO> page, Integer type, String priceListName);

}

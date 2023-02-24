package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.mp.base.DBEntity;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.node.ForestNodeMerger;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.system.entity.Dict;
import org.springrabbit.system.feign.IDictClient;
import org.springrabbit.system.user.entity.User;
import org.springrabbit.system.user.feign.IUserClient;
import org.xyg.eshop.main.entity.ProductPriceList;
import org.xyg.eshop.main.mapper.ProductPriceListMapper;
import org.xyg.eshop.main.service.IProductPriceListService;
import org.xyg.eshop.main.vo.ProductPriceListTreeVO;
import org.xyg.eshop.main.vo.ProductPriceListVO;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductPriceListServiceImpl extends BaseServiceImpl<ProductPriceListMapper, ProductPriceList> implements IProductPriceListService {

	private final IDictClient dictClient;

	private final IUserClient userClient;

//	private final ISaleAreasService saleAreasService;

	@Override
	public IPage<ProductPriceList> getPage(IPage<ProductPriceList> page) {
		IPage<ProductPriceList> productPriceListIPage = page(page);
		return productPriceListIPage;
	}

	@Override
	public R<String> submit(ProductPriceList productPriceList){
		return R.status(saveOrUpdate(productPriceList));
	}

	@Override
	public R<String> saveProductPriceList(ProductPriceList productPriceList){
		// 名称不能重复
		if (productPriceList.getId() == null){
			Integer count = lambdaQuery().eq(ProductPriceList::getPriceListName, productPriceList.getPriceListName()).count();
			if (count > 0){
				return R.fail("当前价目表名称已存在！！");
			}
		}

		return R.status(save(productPriceList));
	}

	@Override
	public R<String> updateProductPriceList(ProductPriceList productPriceList){
		if (productPriceList.getParentId() == null){
			productPriceList.setParentId(0L);
		}
		return R.status(updateById(productPriceList));
	}

	@Override
	public R<List<ProductPriceListTreeVO>> getTreeList(Long parentId, String priceListName, Integer type, String regionOrPartyName){
		if (parentId == null){
			parentId = 0L;
		}
		return R.data(ForestNodeMerger.merge(baseMapper.getTreeList(parentId,priceListName,type,regionOrPartyName)));
	}

	@Override
	public R<List<ProductPriceList>> getList(Long parentId, String priceListName, Integer type, String regionOrPartyName , Long id , String storefrontType , Integer isValid){
		return R.data(baseMapper.getList(parentId, priceListName, type, regionOrPartyName , id , storefrontType,isValid));
	}

	@Override
	public R<ProductPriceListVO> detail(Long id){

		ProductPriceList productPriceList = lambdaQuery().eq(DBEntity::getId, id).one();

		if (productPriceList == null ){
			return R.data(new ProductPriceListVO());
		}

		// 封装返回值
		ProductPriceListVO productPriceListVO = BeanUtil.copy(productPriceList, ProductPriceListVO.class);
		// 翻译内外销
		Integer type = productPriceListVO.getType();
		if (type != null){
			R<Dict> dictR = dictClient.getDictInfo("quote_type", String.valueOf(type));
			if (dictR.isSuccess() && dictR.getData() != null){
				productPriceListVO.setTypeName(dictR.getData().getDictValue());
			}
		}
		// 翻译客户
		String storefrontType = productPriceListVO.getStorefrontType();
		/*if (partyId != null){
			R<CustParties> custPartiesR = partyClient.getPartyById(partyId);
			if (custPartiesR.isSuccess() && custPartiesR.getData() != null){
				productPriceListVO.setPartyName(custPartiesR.getData().getPartyName());
			}
		}*/

		// 申请人名称
		Long createdBy = productPriceListVO.getCreatedBy();
		if (createdBy != null){
			R<User> infoR = userClient.userInfoById(createdBy);
			if (infoR.isSuccess() && infoR.getData() != null){
				User user = infoR.getData();
				productPriceListVO.setCreatedByName(user.getName());
			}
		}

		// 状态翻译
		Integer status = productPriceListVO.getStatus();
		if (status != null){
			R<Dict> dictR = dictClient.getDictInfo("crm2_doc_status", String.valueOf(status));
			if (dictR.isSuccess() && dictR.getData() != null){
				Dict dict = dictR.getData();
				productPriceListVO.setStatusName(dict.getDictValue());
			}
		}

		// 区域翻译
		/*String region = productPriceListVO.getRegion();
		if (StringUtil.isNotBlank(region)){
			SaleAreas saleAreas = saleAreasService.lambdaQuery()
				.eq(SaleAreas::getAreaCode, region)
				.select(SaleAreas::getId,SaleAreas::getAreaCode, SaleAreas::getAreaName)
				.one();
			productPriceListVO.setRegionName(saleAreas == null ? null : saleAreas.getAreaName());
		}*/

		return R.data(productPriceListVO);
	}

	@Override
	public IPage<ProductPriceListVO> getByTypePage(IPage<ProductPriceListVO> page, Integer type, String priceListName){
		return baseMapper.getByTypePage(page,type,priceListName);
	}

}

package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.eshop.main.entity.Storefront;
import org.xyg.eshop.main.vo.AdvancedSearchVO;
import org.xyg.eshop.main.vo.StorefrontVO;

import java.time.LocalDateTime;
import java.util.List;

public interface IStorefrontService extends BaseService<Storefront> {

	/**
	 * 保存或修改门店信息
	 *
	 * @param storefrontVO 门店vo
	 * @return
	 */
	StorefrontVO saveOrUpdate(StorefrontVO storefrontVO);

	/**
	 * 提交
	 * @param storefrontVO 门店数据
	 * @return
	 */
	Long submit(StorefrontVO storefrontVO);

	/**
	 * 获取门店列表信息
	 *
	 * @param storefrontName    门店名称
	 * @param selectStatus 查询状态
	 * @param companyLogo 门店所属公司标识{'qb','jb','jt'}
	 * @param page         分页参数
	 * @param userId       用户id
	 * @return
	 */
	R<IPage<Storefront>> getList(String storefrontName, Integer status, Integer storefrontLevel, Long salesrepId, Integer selectStatus, IPage<Storefront> page, Long userId, String companyLogo);

	/**
	 * 获取门店详情
	 *
	 * @param id 门店id
	 * @param processInstanceId 流程实例id
	 * @return
	 */
	StorefrontVO getDetail(Long id,String processInstanceId);

	/**
	 * 修改门店状态为注销或者黑名单
	 *
	 * @param storefront 门店数据
	 * @return
	 */
	R<String> updateStatus(Storefront storefront);

	/**
	 * 高级搜索
	 *
	 * @param advancedSearchVOList 搜索字段、运算符、值集合
	 * @param page                 分页信息
	 * @return
	 */
	R<IPage<Storefront>> advancedSearch(List<AdvancedSearchVO> advancedSearchVOList, IPage page);

	String decod(String storefrontName);

	/**
	 * 分页查询门店列表
	 * @param page 分页参数
	 * @param storefrontVO 查询条件
	 * @return
	 */
	IPage<StorefrontVO> getPage(IPage<StorefrontVO> page, StorefrontVO storefrontVO);

	/**
	 * 根据门店名称全匹配搜索
	 * @param storefrontName 门店名称
	 * @return
	 */
	Storefront getByStorefrontNameFullMatch(String storefrontName);

	/**
	 * 获取全部门店列表信息
	 * @param companyLogo 门店所属公司标识{'qb','jb','jt'}
	 * @param storefrontName 门店名称
	 * @param status 门店状态
	 * @param storefrontLevel 门店等级
	 * @param salesrepName 业务员名称
	 * @param saleAreaName 销售区域
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param salesType
	 * @param partyShortName 门店别名
	 * @param page 分页参数
	 * @return
	 */
	R<IPage<StorefrontVO>> getAllByPage(String companyLogo, String storefrontName, String status, Integer storefrontLevel, String salesrepName, String saleAreaName, String startDate , String endDate, String salesType ,String partyShortName , IPage<StorefrontVO> page);

	LocalDateTime findMaxUpdateDate();

	/**
	 * 根据业务员id或业务员工号查询门店数据
	 *
	 * @param ids      门店id
	 * @param salesmanIds   业务员id
	 * @param salesmanCodes 业务员工号
	 * @return {@link List<Storefront>}
	 */
	List<Storefront> getBySalesmanIdOrSalesmanCodeParty(String ids, String salesmanIds, String salesmanCodes);

	/**
	 * 发起流程
	 * @param id id
	 */
	void startProcess(Long id);

	/**
	 * 流程实例任务开始回调接口
	 * @param inDto 回调函数入参dto
	 * @return
	 */
	CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto);

	/**
	 * 流程实例任务结束回调接口
	 * @param inDto 回调函数入参dto
	 * @return
	 */
	CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto);

}

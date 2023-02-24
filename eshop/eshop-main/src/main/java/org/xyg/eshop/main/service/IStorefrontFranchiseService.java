package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.eshop.main.dto.StorefrontFranchiseDTO;
import org.xyg.eshop.main.entity.StorefrontFranchise;
import org.xyg.eshop.main.query.StorefrontFranchiseQuery;
import org.xyg.eshop.main.vo.StorefrontFranchiseVO;

import javax.validation.constraints.NotNull;

/**
 * @author ww
 * @description 针对表【eshop_storefront_franchise(易车-加盟店主数据)】的数据库操作Service
 * @createDate 2023-01-09 14:20:40
 */
public interface IStorefrontFranchiseService extends BaseService<StorefrontFranchise> {

	/**
	 * 查询加盟店分页数据
	 *
	 * @param page  分页
	 * @param query 查询条件
	 * @return IPage<StoreFranchiseVO>
	 */
	IPage<StorefrontFranchiseVO> selectPage(IPage<StorefrontFranchiseVO> page, StorefrontFranchiseQuery query);

	/**
	 * 新增加盟店数据
	 *
	 * @param entity 新增数据
	 * @return StoreFranchiseVO
	 */
	StorefrontFranchiseVO addData(StorefrontFranchiseDTO entity);

	/**
	 * 新增或修改加盟店数据
	 *
	 * @param entity 新增数据
	 * @return StoreFranchiseVO
	 */
	StorefrontFranchiseVO createOrModify(StorefrontFranchiseDTO entity);

	/**
	 * 更新加盟店数据
	 *
	 * @param entity 更新数据
	 * @return StoreFranchiseVO
	 */
	StorefrontFranchiseVO update(StorefrontFranchiseDTO entity);

	/**
	 * 物理删除数据
	 *
	 * @param id 主键id
	 * @return Boolean
	 */
	Boolean trueRemove(@NotNull(message = "主键id不能为空") Long id);

	/**
	 * 查看详情
	 *
	 * @param id 主键id
	 * @return StoreFranchiseVO
	 */
	StorefrontFranchiseVO detail(@NotNull(message = "主键id不能为空") Long id);

	/**
	 * 修改状态
	 *
	 * @param id     主键id
	 * @param status 状态值
	 * @return Boolean
	 */
	Boolean updateStatus(@NotNull(message = "主键id不能为空") Long id, @NotNull(message = "状态值不能为空") Integer status);

	/**
	 * 生成编码
	 *
	 * @param type   自增业务类型 在{@link org.springrabbit.ras.constant.CRMConstant} 添加完常量后使用
	 * @param minLen 数字最小长度
	 * @return String
	 */
	String generateNo(int type, int minLen);

	/**
	 * 发起流程
	 *
	 * @param id 主键id
	 */
	void addProcess(@NotNull(message = "主键id不能为空") Long id);

	/**
	 * 流程实例任务开始回调接口
	 *
	 * @param inDto 回调函数入参dto
	 * @return CallBackMethodResDto
	 */
	CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto);

	/**
	 * 流程实例任务结束回调接口
	 *
	 * @param inDto 回调函数入参dto
	 * @return CallBackMethodResDto
	 */
	CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto);
}

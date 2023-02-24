package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.xyg.eshop.main.entity.ProductAdjustmentRecord;
import org.xyg.eshop.main.entity.ProductAdjustmentRecordLines;
import org.xyg.eshop.main.excel.ProductAdjustmentRecordExcel;
import org.xyg.eshop.main.vo.ProductAdjustmentRecordVO;

import java.io.IOException;
import java.util.List;

public interface IProductAdjustmentRecordService extends BaseService<ProductAdjustmentRecord> {

	/**
	 * 保存调整单数据
	 * @param productAdjustmentRecordVO 调整单数据
	 * @return
	 */
    R<ProductAdjustmentRecordVO> saveProductAdjustmentRecord(ProductAdjustmentRecordVO productAdjustmentRecordVO);

	/**
	 * 提交调整单
	 * @param productAdjustmentRecordVO 调整单数据
	 * @return
	 */
	ProductAdjustmentRecordVO submit(ProductAdjustmentRecordVO productAdjustmentRecordVO);

	ProductAdjustmentRecordVO importerProductAdjustmentRecord(List<ProductAdjustmentRecordExcel> data, Long headId);

	/**
	 * 删除调整单行表
	 * @param ids 调整单id
	 * @return
	 */
	R<String> deleteLines(String ids);

	/**
	 * 调整单详情
	 * @param id 主键id
	 * @return
	 */
    R<ProductAdjustmentRecordVO> getDetail(Long id);

	/**
	 * 分页查询调整单列表
	 * @param page 分页参数
	 * @param id 调整单id
	 * @param priceListId 价目表id
	 * @param docnum 单号
	 * @param erpStatus erp状态
	 * @return
	 */
    R<IPage<ProductAdjustmentRecordVO>> getToPriceListPage(IPage<ProductAdjustmentRecordVO> page, Long id, Long priceListId,String docnum,String erpStatus);

	/**
	 * 删除整个调整单
	 * @param id id
	 */
    R<String> delete(Long id);

	/**
	 * 根据产品id添加调整单
	 * @param id 调整单id
	 * @param productIds 产品id,多个逗号分隔
	 */
	R<String> saveByProductIds(Long id,String productIds);

	/**
	 * 查询行表集合
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
	 */
	R<List<ProductAdjustmentRecordLines>> linesList(Long headId, String productModel, String position, String brand, String model, String itemChDescription, Integer machiningType, Long maxWidth, Long minWidth, Long maxLength, Long minLength, String oemModel);

	/**
	 * 保存调整单行表
	 * @param productAdjustmentRecordLines 行表数据
	 * @return
	 */
	R<ProductAdjustmentRecordLines> linesUpdateById(ProductAdjustmentRecordLines productAdjustmentRecordLines);

	/**
	 * 保存复制的调整单行表信息
	 * @param id 行表id
	 * @return
	 */
	ProductAdjustmentRecordLines copySaveById(Long id);

	void updateAdjustmentRecordToPriceList(Long id) throws IOException;

	/**
	 * 发起内销调整单流程
	 * @param id 主键id
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

package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springrabbit.core.mp.base.BaseService;
import org.xyg.eshop.main.entity.InteractRecord;
import org.xyg.eshop.main.vo.InteractRecordVO;

public interface IInteractRecordService extends BaseService<InteractRecord> {

	/**
	 * 保存互动记录
	 * @param interactRecordVO 互动记录
	 * @return
	 */
    Boolean saveInteractRecord(InteractRecordVO interactRecordVO);

	/**
	 * 删除互动记录
	 * @param id 主键id
	 * @return
	 */
	Boolean deleteId(Long id);

	/**
	 * 获取互动记录分页列表
	 * @param page 分页参数
	 * @param interactRecordVO 搜索条件
	 * @return
	 */
	IPage<InteractRecordVO> getPage(IPage<InteractRecordVO> page, InteractRecordVO interactRecordVO);

	/**
	 * 获取互动记录详情
	 * @param id 主键id
	 * @return
	 */
	InteractRecordVO getDetail(Long id);

}

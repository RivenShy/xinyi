package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.Func;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.flow.core.feign.IFlowOpenClient;
import org.springrabbit.system.entity.Dict;
import org.springrabbit.system.feign.IDictClient;
import org.springrabbit.system.user.entity.User;
import org.springrabbit.system.user.feign.IUserClient;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.entity.InventoryManagement;
import org.xyg.eshop.main.entity.InventoryManagementLines;
import org.xyg.eshop.main.mapper.InventoryManagementMapper;
import org.xyg.eshop.main.service.IInventoryManagementLinesService;
import org.xyg.eshop.main.service.IInventoryManagementService;
import org.xyg.eshop.main.vo.InventoryManagementVO;

import java.rmi.ServerException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InventoryManagementServiceImpl extends BaseServiceImpl<InventoryManagementMapper, InventoryManagement> implements IInventoryManagementService {

	private final IUserClient userClient;
	private final IDictClient dictClient;
	private final IInventoryManagementLinesService linesService;

	private final IFlowOpenClient flowOpenClient;

	@Override
	public IPage<InventoryManagementVO> getPage(IPage<InventoryManagementVO> page, InventoryManagementVO inventoryManagementVO){
		// 处理日期搜索条件
		processingDate(inventoryManagementVO);

		IPage<InventoryManagementVO> resultPage = baseMapper.getPage(page, inventoryManagementVO);

		// 填充数据
		fillData(resultPage.getRecords());

		return resultPage;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveInventoryManagement(InventoryManagementVO inventoryManagementVO){
		boolean save = save(inventoryManagementVO);
		// 保存行表数据
		saveLines(inventoryManagementVO);
		return save;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long submit(InventoryManagementVO inventoryManagementVO){
		save(inventoryManagementVO);
		// 保存行表数据
		saveLines(inventoryManagementVO);
		return inventoryManagementVO.getId();
	}

	@Override
	public void startProcess(Long id) throws ServerException{
		InventoryManagement inventoryManagement = getById(id);
		if (inventoryManagement == null){
			throw new ServerException("发起流程失败,未查询到数据 id = " + id);
		}


	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateInventoryManagement(InventoryManagementVO inventoryManagementVO){
		boolean update = updateById(inventoryManagementVO);

		saveLines(inventoryManagementVO);

		return update;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean delete(Long id){
		boolean remove = removeById(id);
		// 删除行表数据
		linesService.remove(linesService.lambdaQuery().eq(InventoryManagementLines::getHeadId,id));
		return remove;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean linesDelete(String ids){
		return linesService.removeByIds(Func.toLongList(ids));
	}

	/**
	 * 处理日期搜索条件
	 * @param inventoryManagementVO 搜索条件
	 */
	private void processingDate(InventoryManagementVO inventoryManagementVO){
		String startDate = inventoryManagementVO.getStartDate();
		String endDate = inventoryManagementVO.getEndDate();

		if (StringUtil.isNotBlank(startDate)){
			startDate += " 00:00:00";
			inventoryManagementVO.setStartDate(startDate);
		}

		if (StringUtil.isNotBlank(endDate)){
			endDate += " 23:59:59";
			inventoryManagementVO.setEndDate(endDate);
		}
	}

	/**
	 * 补充库存管理列表数据
	 * @param list 库存管理列表数据
	 */
	private void fillData(List<InventoryManagementVO> list){
		if (CollectionUtil.isEmpty(list)){
			return;
		}

		for (InventoryManagementVO inventoryManagementVO : list) {
			// 获取创建人名称
			inventoryManagementVO.setCreatedByName(getUserNameById(inventoryManagementVO.getCreatedBy()));

			// 获取单据类型字典值
			String documentType = inventoryManagementVO.getDocumentType();
			if (StringUtil.isNotBlank(documentType)){
				String documentTypeName = getDictValueByKey(EShopMainConstant.INVENTORY_MANAGEMENT_DOCUMENT_TYPE_DICT_CODE, documentType);
				inventoryManagementVO.setDocumentTypeName(documentTypeName);
			}

			// 获取状态字典值
			Integer status = inventoryManagementVO.getStatus();
			if (status != null){
				String statusName = getDictValueByKey(EShopMainConstant.INVENTORY_MANAGEMENT_STATUS_DICT_CODE, status.toString());
				inventoryManagementVO.setStatusName(statusName);
			}

		}
	}

	/**
	 * 根据用户id获取用户名称
	 * @param id 用户id
	 * @return
	 */
	private String getUserNameById(Long id){
		String userName = null;
		if (id == null){
			return userName;
		}

		try {
			R<User> userR = userClient.userInfoById(id);
			if (userR.isSuccess() && userR.getData() != null){
				userName = userR.getData().getRealName();
			}
		} catch (Exception e) {
			log.error("获取出入库管理创建人名称出现错误, id = {}  {}",id,e);
		}
		return userName;
	}

	/**
	 * 根据字典编码和字典key获取字典值
	 * @param dictCode 字典编码
	 * @param dictKey 字典key值
	 * @return
	 */
	private String getDictValueByKey(String dictCode,String dictKey){
		String dictValue = null;
		if (StringUtil.isBlank(dictKey)){
			return dictValue;
		}

		try {
			R<Dict> dictR = dictClient.getDictInfo(dictCode, dictKey);
			if (dictR.isSuccess() && dictR.getData() != null){
				dictValue = dictR.getData().getDictValue();
			}
		} catch (Exception e) {
			log.error("获取出入库管理字典值出现错误 code = {}, key = {}  {}",dictCode,dictKey,e);
		}
		return dictValue;
	}

	/**
	 * 保存出入库管理行表数据
	 * @param inventoryManagementVO 出入库管理数据
	 */
	private void saveLines(InventoryManagementVO inventoryManagementVO){
		if (inventoryManagementVO == null){
			return;
		}
		List<InventoryManagementLines> linesList = inventoryManagementVO.getLinesList();
		if (CollectionUtil.isEmpty(linesList)){
			return;
		}
		for (InventoryManagementLines inventoryManagementLines : linesList) {
			inventoryManagementLines.setHeadId(inventoryManagementVO.getId());
			linesService.saveOrUpdate(inventoryManagementLines);
		}
	}

}

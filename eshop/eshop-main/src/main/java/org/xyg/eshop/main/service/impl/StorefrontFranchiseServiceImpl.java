package org.xyg.eshop.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.secure.utils.AuthUtil;
import org.springrabbit.core.tool.utils.BeanUtil;
import org.springrabbit.core.tool.utils.DateUtil;
import org.springrabbit.core.tool.utils.ObjectUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.flow.core.callback.CallBackMethodResDto;
import org.springrabbit.flow.core.callback.CallbackMethodReqDto;
import org.springrabbit.flow.core.entity.RabbitFlow;
import org.springrabbit.flow.core.feign.IFlowOpenClient;
import org.xyg.ehop.common.component.generator.AutoIncrementIDGenerator;
import org.xyg.ehop.common.constants.EshopConstants;
import org.xyg.eshop.main.constants.EShopMainConstant;
import org.xyg.eshop.main.dto.StorefrontFranchiseDTO;
import org.xyg.eshop.main.entity.StorefrontFranchise;
import org.xyg.eshop.main.mapper.StorefrontFranchiseMapper;
import org.xyg.eshop.main.query.StorefrontFranchiseQuery;
import org.xyg.eshop.main.service.IStorefrontFranchiseService;
import org.xyg.eshop.main.util.ProcessHandle;
import org.xyg.eshop.main.util.ProcessUtils;
import org.xyg.eshop.main.vo.StorefrontFranchiseVO;
import org.xyg.eshop.main.wrapper.StoreFranchiseDtoWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ww
 * @description 针对表【eshop_storefront_franchise(易车-加盟店主数据)】的数据库操作Service实现
 * @createDate 2023-01-09 14:20:40
 */

@Service
@AllArgsConstructor
public class StorefrontFranchiseServiceImpl extends BaseServiceImpl<StorefrontFranchiseMapper, StorefrontFranchise> implements IStorefrontFranchiseService, ProcessHandle {

	// 编码前缀
	private static final String PREFIX = "MD";

	private final AutoIncrementIDGenerator autoIncrementIDGenerator;
	private final IFlowOpenClient flowOpenClient;

	private final Map<Long, StorefrontFranchise> entityMap = new HashMap<>();

	@Override
	public String getProcessDefinitionKey() {
		return "eshop_storefront_franchise_approval";
	}

	@Override
	public String getBusinessName(Long id) {
		String businessName = "易车门店加盟审批 ";
		StorefrontFranchise franchise = entityMap.get(id);
		if (franchise != null && StringUtil.isNotBlank(franchise.getFranchiseNo())) {
			businessName += franchise.getFranchiseNo();
		}
		return businessName;
	}

	@Override
	public String getTitle() {
		return "易车门店加盟";
	}

	@Override
	public String getContent(RabbitUser user, Long id) {
		String userName = user.getUserName();
		if (StringUtil.isBlank(userName)) {
			userName = user.getNickName();
		}
		StorefrontFranchise franchise = entityMap.get(id);
		if (franchise != null && StringUtil.isNotBlank(franchise.getFranchiseNo())) {
			return userName + "于" + DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATETIME) + "新增一个易车门店加盟: " + franchise.getFranchiseNo();
		}
		return userName + "于" + DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATETIME) + "新增一个易车门店加盟: ";
	}

	@Override
	public String getTemplateCode() {
		return "eshop_storefront_franchise_code";
	}

	@Override
	public String getStartCallbackMethod() {
		return "http://eshop-main/franchise/flowInstanceExecutionStartCallback";
	}

	@Override
	public String getEndCallbackMethod() {
		return "http://eshop-main/franchise/flowInstanceExecutionEndCallback";
	}

	@Override
	public void afterAddProcessSuccess(Long id, RabbitFlow flow) {
		LambdaUpdateChainWrapper<StorefrontFranchise> updateWrapper = this.lambdaUpdate();
		updateWrapper.eq(StorefrontFranchise::getId, id);
		updateWrapper.set(StorefrontFranchise::getStatus, EshopConstants.STATUS_APPROVING);
		updateWrapper.set(StorefrontFranchise::getApplyDate, DateUtil.now());
		updateWrapper.set(StorefrontFranchise::getProcessInstanceId, flow.getProcessInstanceId());
		if (updateWrapper.update()) {
			if (entityMap.size() > 0) {
				entityMap.clear();
			}
		}
	}

	@Override
	public IPage<StorefrontFranchiseVO> selectPage(IPage<StorefrontFranchiseVO> page, StorefrontFranchiseQuery query) {
		return getBaseMapper().findPage(page, query);
	}

	@Override
	public StorefrontFranchiseVO addData(StorefrontFranchiseDTO entity) {
		setCoding(entity);
		setDefaultValue(entity);
		this.save(entity);
		return StoreFranchiseDtoWrapper.build().entityVO(entity);
	}

	@Override
	public StorefrontFranchiseVO createOrModify(StorefrontFranchiseDTO entity) {
		if (entity.getId() == null) {
			entity.setStatus(EshopConstants.STATUS_SAVE);
			return addData(entity);
		}
		return update(entity);
	}

	@Override
	public StorefrontFranchiseVO update(StorefrontFranchiseDTO entity) {
		Assert.notNull(entity.getId(), "failed to update with null id");
		StorefrontFranchise franchise = this.getById(entity.getId());
		if (ObjectUtil.isNotEmpty(franchise)) {
			if (!franchise.getStatus().equals(EshopConstants.STATUS_SAVE)) {
				entity.setStatus(franchise.getStatus());
			}
			entity.setFranchiseNo(franchise.getFranchiseNo());
			entity.setStorefrontCode(franchise.getStorefrontCode());
		}
		setCoding(entity);
		this.updateById(entity);
		return StoreFranchiseDtoWrapper.build().entityVO(entity);
	}

	@Override
	public Boolean trueRemove(Long id) {
		return this.removeById(id);
	}

	@Override
	public StorefrontFranchiseVO detail(Long id) {
		StorefrontFranchise franchise = this.getById(id);
		Assert.notNull(franchise, "未查询出数据" + id);
		return BeanUtil.copyProperties(franchise, StorefrontFranchiseVO.class);
	}

	@Override
	public Boolean updateStatus(Long id, Integer status) {
		return this.lambdaUpdate().eq(StorefrontFranchise::getId, id).set(StorefrontFranchise::getStatus, status).update();
	}

	public String generateNo(int type, int minLen) {
		return PREFIX + autoIncrementIDGenerator.nextDayValue(type, minLen);
	}

	@Override
	public void addProcess(Long id) {
		// 获取门店加盟数据
		StorefrontFranchise franchise = this.getById(id);
		Assert.notNull(franchise, "流程发起时门店加盟数据为空");
		ProcessUtils build = new ProcessUtils.Builder()
			.id(id)
			.processHandle(this)
			.flowOpenClient(flowOpenClient)
			.build();
		if (franchise.getStatus().equals(EshopConstants.STATUS_REJECTED)) {
			build.completeTaskByProcessInstanceId(franchise.getProcessInstanceId(), "【门店加盟流程驳回后提交】");
		} else {
			build.startProcess();
			entityMap.put(id, franchise);
		}
	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionStartCallback(CallbackMethodReqDto inDto) {
		CallBackMethodResDto outDto = new CallBackMethodResDto();
		return outDto;
	}

	@Override
	public CallBackMethodResDto flowInstanceExecutionEndCallback(CallbackMethodReqDto inDto) {
		CallBackMethodResDto outDto = new CallBackMethodResDto();
		return outDto;
	}

	/**
	 * 设置默认值
	 */
	private void setDefaultValue(StorefrontFranchise entity) {
		// 补充申请人工号
		if (StringUtil.isBlank(entity.getSalesrepNo())) {
			String code = AuthUtil.getUser().getCode();
			if (StringUtil.isNotBlank(code)) {
				entity.setSalesrepNo(code);
			}
		}
	}

	/**
	 * 设置编码
	 */
	private void setCoding(StorefrontFranchise entity) {
		if (StringUtil.isBlank(entity.getFranchiseNo())) {
			entity.setFranchiseNo(generateNo(EShopMainConstant.STOREFRONT_GENERATE_CODE, 3));
		}
		if (StringUtil.isBlank(entity.getStorefrontCode())) {
			entity.setStorefrontCode(generateNo(EShopMainConstant.STOREFRONT_GENERATE_CODE, 3));
		}
	}
}





package org.xyg.eshop.main.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.core.secure.utils.AuthUtil;
import org.springrabbit.core.tool.api.R;
import org.springrabbit.core.tool.jackson.JsonUtil;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.flow.core.dto.TaskCompleteDto;
import org.springrabbit.flow.core.entity.Activity;
import org.springrabbit.flow.core.entity.RabbitFlow;
import org.springrabbit.flow.core.feign.IFlowOpenClient;
import org.springrabbit.flow.core.utils.CommentTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * 流程发起工具类
 *
 * @see org.xyg.eshop.main.util.ProcessHandle
 * @see ProcessUtils
 */

@Slf4j
public final class ProcessUtils {

	/**
	 * 业务数据主键id
	 */
	private final Long            id;

	/**
	 * 是否自动完成起草节点
	 */
	private final Boolean         completeDraft;

	private final ProcessHandle   handle;
	private final IFlowOpenClient flowOpenClient;


	public ProcessUtils(Builder builder) {
		this.id             = builder.id;
		this.completeDraft  = builder.completeDraft;
		this.handle         = builder.processHandle;
		this.flowOpenClient = builder.flowOpenClient;
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * 开启流程
	 *
	 * <p>
	 * 由该类 {@link  org.xyg.eshop.main.util.ProcessHandle} 来处理流程发起时所需要的流程数据
	 * <li>initiateApproval({@link Long}) 该方法用于处理流程是否继续发起，默认返回 false</li>
	 * </p>
	 *
	 * @see org.xyg.eshop.main.util.ProcessHandle
	 */
	public void startProcess() {
		try {
			// 流程是否开启处理
			if (handle.initiateApproval(id)) {
				log.warn("发起流程结束 请正确设置该方法返回值 initiateApproval()");
				return;
			}
			log.info("{} 开始发起流程 id: {} ->>>>> ProcessHandle implements class {}", handle.getTitle(), id, handle.getClass().getName());
			R<RabbitFlow> flow = flowOpenClient.startProcessByKey(handle.getProcessDefinitionKey(), ProcessConstant.ASSIGNEE_PREFIX + getUser().getUserId(), this.completeDraft, null, initialVariablesMap(id));
			log.info("流程发起结束");
			if (flow.isSuccess()) {
				handle.afterAddProcessSuccess(id, flow.getData());
			}
		} catch (Exception e) {
			throw new RuntimeException("发起流程失败");
		}
	}

	/**
	 * 根据流程实例id完成当前节点
	 *
	 * @param processInstanceId 流程实id
	 * @param comment           审批意见
	 * @param variables         流程变量
	 */
	public void completeTaskByProcessInstanceId(@NotNull String processInstanceId, String comment, Map<String, Object> variables) {
		if (handle.isUseSubclassCompleteMethod()) {
			handle.completeTaskByProcessInstanceId(processInstanceId, comment, variables);
		} else {
			R<List<Activity>> runningActivityNodes = flowOpenClient.getRunningActivityNodes(processInstanceId);
			if (runningActivityNodes.isSuccess() && CollectionUtil.isNotEmpty(runningActivityNodes.getData())) {
				RabbitUser user = getUser();
				Activity activity = runningActivityNodes.getData().get(0);
				String taskId = activity.getTaskId();
				String actId = activity.getActId();
				if (StringUtil.isNotBlank(actId)) {
					actId = actId.toUpperCase(Locale.ROOT);
				}
				// 没有任务id或不是起草节点,不执行完成任务代码
				if (StringUtil.isNotBlank(taskId) && Objects.equals(ProcessConstant.DRAFT_NODE, actId)) {
					if (StringUtil.isBlank(comment)) {
						comment = String.format("【%s 流程驳回后提交】", handle.getTitle());
					}
					TaskCompleteDto taskCompleteDto = new TaskCompleteDto();
					taskCompleteDto.setProcessInstanceId(processInstanceId);
					taskCompleteDto.setTaskId(taskId);
					taskCompleteDto.setComment(comment);
					taskCompleteDto.setCommentType(CommentTypeEnum.TY.getName());
					taskCompleteDto.setUserId(ProcessConstant.ASSIGNEE_PREFIX + user.getUserId());
					if (CollectionUtil.isNotEmpty(variables)) {
						taskCompleteDto.setVariables(variables);
					}
					flowOpenClient.complete(taskCompleteDto);
				}
			}
		}
	}

	/**
	 * 根据流程实例id完成当前节点
	 *
	 * @param processInstanceId 流程实id
	 * @param comment           审批意见
	 */
	public void completeTaskByProcessInstanceId(@NotNull String processInstanceId, String comment) {
		completeTaskByProcessInstanceId(processInstanceId, comment, null);
	}

	private Map<String, Object> initialVariablesMap(Long id) {
		RabbitUser user = getUser();
		Map<String, Object> variablesMap = handle.initialVariablesMap(id);
		try {
			variablesMap.put(ProcessConstant.DATA_ID_, id);
			variablesMap.put(ProcessConstant.BUSINESS_NAME_, handle.getBusinessName(id));
			variablesMap.put(ProcessConstant.CREATE_USER_ID, user.getUserId());
			variablesMap.put(ProcessConstant.TITLE_, handle.getTitle());
			variablesMap.put(ProcessConstant.CONTENT_, handle.getContent(user, id));
			variablesMap.put(ProcessConstant.TEMPLATE_CODE_, handle.getTemplateCode());
		} catch (Exception e) {
			throw new RuntimeException("initialVariablesMap 初始流程变量失败");
		}
		if (StringUtil.isNotBlank(handle.getStartCallbackMethod())) {
			variablesMap.put(ProcessConstant.START_CALLBACK_METHOD, handle.getStartCallbackMethod());
		}
		if (StringUtil.isNotBlank(handle.getEndCallbackMethod())) {
			variablesMap.put(ProcessConstant.END_CALLBACK_METHOD, handle.getEndCallbackMethod());
		}
		log.info("流程变量数据 ->>>> variablesMap: {}", JsonUtil.toJson(variablesMap));
		return variablesMap;
	}

	/**
	 * 获取当前登入用户信息
	 *
	 * @return RabbitUser
	 * @see RabbitUser
	 */
	public static RabbitUser getUser() {
		return AuthUtil.getUser();
	}

	public static class Builder {

		@NotNull
		private Long            id;
		/**
		 * 是否自动完成起草节点
		 */
		private Boolean         completeDraft;

		@NotNull
		private ProcessHandle   processHandle;
		@NotNull
		private IFlowOpenClient flowOpenClient;

		public Builder() {
			log.info("Process build");
			this.completeDraft = Boolean.TRUE;
		}

		public Builder(ProcessUtils utils) {
			this.id             = utils.id;
			this.completeDraft  = utils.completeDraft;
			this.processHandle  = utils.handle;
			this.flowOpenClient = utils.flowOpenClient;
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		/**
		 * 是否自动完成起草节点<p/>
		 * default true
		 *
		 * @param completeDraft {@link Boolean}
		 * @return Builder
		 */
		public Builder completeDraft(boolean completeDraft) {
			this.completeDraft = completeDraft;
			return this;
		}

		public Builder processHandle(ProcessHandle processHandle) {
			this.processHandle = processHandle;
			return this;
		}

		public Builder flowOpenClient(IFlowOpenClient flowOpenClient) {
			this.flowOpenClient = flowOpenClient;
			return this;
		}

		public ProcessUtils build() {
			Assert.notNull(this.id, "id cannot be empty");
			Assert.notNull(this.processHandle, "ProcessHandle cannot be empty");
			Assert.notNull(this.flowOpenClient, "IFlowOpenClient cannot be empty");
			return new ProcessUtils(this);
		}
	}

	public Builder newBuilder() {
		return new Builder(this);
	}

	public interface ProcessConstant {

		/**
		 * 起草节点id
		 */
		String DRAFT_NODE            = "DRAFT_NODE";

		/**
		 * 流程审批人前缀
		 */
		String ASSIGNEE_PREFIX       = "taskUser_";

		/**
		 * 业务主键id
		 */
		String DATA_ID_              = "data_id_";

		/**
		 * 流程标题
		 */
		String BUSINESS_NAME_        = "business_name_";

		/**
		 * 流程发起人
		 */
		String CREATE_USER_ID        = "createUserId";

		/**
		 * 消息标题
		 */
		String TITLE_                = "title_";

		/**
		 * 消息内容
		 */
		String CONTENT_              = "content_";

		/**
		 * 消息模板
		 */
		String TEMPLATE_CODE_        = "template_code_";

		/**
		 * 开始回调函数
		 */
		String START_CALLBACK_METHOD = "startCallbackMethod";

		/**
		 * 结束回调函数
		 */
		String END_CALLBACK_METHOD  = "endCallbackMethod";
	}
}

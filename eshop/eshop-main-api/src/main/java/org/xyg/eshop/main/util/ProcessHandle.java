package org.xyg.eshop.main.util;


import org.springrabbit.core.secure.RabbitUser;
import org.springrabbit.flow.core.entity.RabbitFlow;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public interface ProcessHandle {

	/**
	 * 用于处理流程是否发起
	 *
	 * @param id 也业务数据主键id
	 * @return default return false
	 */
	default boolean initiateApproval(@NotNull Long id) {
		return Boolean.FALSE;
	}

	/**
	 * 初始流程变量数据，也可通过业务数据id设置流程变量
	 *
	 * @param id 也业务数据主键id
	 * @return Map<String, Object>
	 */
	default Map<String, Object> initialVariablesMap(@NotNull Long id) {
		return new HashMap<>();
	}

	/**
	 * 获取流程模型标识
	 *
	 * @return String
	 */
	String getProcessDefinitionKey();

	/**
	 * 获取流程标题
	 *
	 * @return String
	 */
	String getBusinessName(@NotNull Long id);

	/**
	 * 获取消息标题
	 *
	 * @return String
	 */
	String getTitle();

	/**
	 * 获取消息内容
	 *
	 * @param user 当前登入用户
	 * @param id   业务数据主键id
	 * @return String
	 */
	String getContent(@NotNull RabbitUser user, @NotNull Long id);

	/**
	 * 获取消息模板
	 *
	 * @return String
	 */
	String getTemplateCode();

	/**
	 * 获取开始回调函数
	 *
	 * @return String
	 */
	default String getStartCallbackMethod() {
		return null;
	}

	/**
	 * 获取结束回调函数
	 *
	 * @return String
	 */
	default String getEndCallbackMethod() {
		return null;
	}

	/**
	 * 流程发成功后所需要处理的事情
	 *
	 * @param id   业务数据主键id
	 * @param flow {@link RabbitFlow} 流程响应数据
	 */
	void afterAddProcessSuccess(@NotNull Long id, RabbitFlow flow);

	/**
	 * 是否使用自己的完成当前节点方法
	 *
	 * @return default return false
	 */
	default boolean isUseSubclassCompleteMethod() {
		return Boolean.FALSE;
	}

	/**
	 * 根据流程实例id完成当前节点
	 *
	 * @param processInstanceId 流程实id
	 * @param comment           审批意见
	 * @param variables         流程变量
	 */
	default void completeTaskByProcessInstanceId(@NotNull String processInstanceId, String comment, Map<String, Object> variables) {
		throw new RuntimeException("根据流程实例id完成当前节点 失败 请自行实现该方法");
	}
}

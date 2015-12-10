package com.hp.idc.itsm.task;

import java.util.Map;

import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;

/**
 * 表示任务流转信息
 * @author 梅园
 *
 */
public class TaskUpdateInfo {
	/**
	 * 流转方式:正常
	 */
	public static int TYPE_NORMAL = 0;

	/**
	 * 流转方式:回退
	 */
	public static int TYPE_ROLLBACK = 1;

	/**
	 * 流转方式:发表信息
	 */
	public static int TYPE_MESSAGE = 2;

	/**
	 * 流转方式:接受
	 */
	public static int TYPE_ACCEPT = 3;
	
	/**
	 * 流转方式:保存待发送，保存数据，但流程不流转
	 */
	public static int TYPE_SAVE_FOR_EDIT = 4;
	
	public static int TYPE_FORCE_CLOSE = 5;//强制关闭
	
	public static int TYPE_OVERTIME = 6;//超时
	
	public static int TYPE_UPDATE_FIELD = 7;//直接更新某个字段
	
	public static int TYPE_READ = 8;//阅知
	
	public static int TYPE_DYNAMIC_BRENCH = 9;
	
	/**
	 * 批量更新工单，是对于单个工单的多个草稿分支的批量提交
	 * 使用环境：分支开始节点处流转时，可把每一个分支，都保留为草稿，最后一起提交
	 */
	public static int TYPE_UPDATE_BATCH = 8;
	

	/**
	 * 存储流转方式
	 */
	protected int type = TYPE_NORMAL;
	
	/**
	 * 存储相关任务对象
	 */
	protected TaskInfo taskInfo;
	
	/**
	 * 存储相关任务节点对象
	 */
	protected TaskData taskData;
	
	/**
	 * 存储相关流程对象
	 */
	protected WorkflowData workflow;
	
	/**
	 * 存储提交的数据表
	 */
	protected Map<String,String> map;
	
	/**
	 * 存储提交的目标节点
	 */
	protected NodeInfo toNode;
	
	/**
	 * 存储所执行的动作ID
	 */
	protected String action;
	
	/**
	 * 存储提交的用户
	 */
	protected String[] users;
	
	/**
	 * 存储操作人
	 */
	protected String operName;
	
	protected String toNodePath;

	public String getToNodePath() {
		return toNodePath;
	}

	public void setToNodePath(String toNodePath) {
		this.toNodePath = toNodePath;
	}

	/**
	 * 获取提交的数据表
	 * @return 返回提交的数据表
	 */
	public Map<String,String> getMap() {
		return this.map;
	}
	
	/**
	 * 设置提交的数据表
	 * @param map 提交的数据表
	 */
	public void setMap(Map<String,String> map) {
		this.map = map;
	}
	
	/**
	 * 获取操作人 
	 * @return 返回操作人
	 */
	public String getOperName() {
		return this.operName;
	}
	
	/**
	 * 设置操作人 
	 * @param operName 操作人 
	 */
	public void setOperName(String operName) {
		this.operName = operName;
	}
	
	/**
	 * 获取相关任务节点对象
	 * @return 返回相关任务节点对象
	 */
	public TaskData getTaskData() {
		return this.taskData;
	}
	
	/**
	 * 设置相关任务节点对象
	 * @param taskData 相关任务节点对象
	 */
	public void setTaskData(TaskData taskData) {
		this.taskData = taskData;
	}
	
	/**
	 * 获取相关任务对象 
	 * @return 返回相关任务对象 
	 */
	public TaskInfo getTaskInfo() {
		return this.taskInfo;
	}
	
	/**
	 * 设置相关任务对象 
	 * @param taskInfo 相关任务对象 
	 */
	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}
	
	/**
	 * 获取提交的目标节点
	 * @return 返回提交的目标节点
	 */
	public NodeInfo getToNode() {
		return this.toNode;
	}
	
	/**
	 * 设置提交的目标节点
	 * @param toNode 提交的目标节点
	 */
	public void setToNode(NodeInfo toNode) {
		this.toNode = toNode;
	}
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * 获取提交的用户
	 * @return 返回提交的用户
	 */
	public String[] getUsers() {
		return this.users;
	}
	
	/**
	 * 设置提交的用户
	 * @param users 提交的用户
	 */
	public void setUsers(String[] users) {
		this.users = users;
	}
	
	/**
	 * 获取相关流程对象
	 * @return 返回相关流程对象
	 */
	public WorkflowData getWorkflow() {
		return this.workflow;
	}
	
	/**
	 * 设置相关流程对象
	 * @param workflow 相关流程对象
	 */
	public void setWorkflow(WorkflowData workflow) {
		this.workflow = workflow;
	}
	
	/**
	 * 获取流转方式
	 * @see #TYPE_NORMAL
	 * @see #TYPE_ROLLBACK
	 * @return 返回流转方式
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * 设置流转方式
	 * @see #TYPE_NORMAL
	 * @see #TYPE_ROLLBACK
	 * @param type 流转方式 
	 */
	public void setType(int type) {
		this.type = type;
	}
}

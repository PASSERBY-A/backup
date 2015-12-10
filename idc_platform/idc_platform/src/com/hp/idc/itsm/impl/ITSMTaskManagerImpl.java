package com.hp.idc.itsm.impl;

import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import oracle.jdbc.OraclePreparedStatement;

import org.dom4j.DocumentException;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dbo.ResultSetOperation;
import com.hp.idc.itsm.inter.TaskManagerInterface;
import com.hp.idc.itsm.message.MessageManager;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskEvent;
import com.hp.idc.itsm.task.TaskFactory;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskManager;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.task.TaskWaitingInfo;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.workflow.ActionInfo;
import com.hp.idc.itsm.workflow.ColumnFieldInfo;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

public final class ITSMTaskManagerImpl implements TaskManagerInterface {

	public static final String ITSM = "ITSM";

	private static TaskFactory taskFactory;

	public List<String> dataTableList = null;

	/**
	 * 初始化缓存
	 * 
	 * @throws Exception
	 */
	public void initCache() throws Exception {

		TaskManager.classInsab.put("ITSM", this.getClass().getName());
		TaskManager.classIns.put("ITSM", this);

		Cache.Tasks = new HashMap<String, TaskInfo>();
		Cache.TasksHis = new HashMap<String, TaskInfo>();
		Cache.Workflow_Tasks = new HashMap<String, Map<String, TaskInfo>>();
		Cache.Workflow_TasksHis = new HashMap<String, Map<String, TaskInfo>>();
		Cache.WaitData = new HashMap<String, TaskData>();

		System.out.println("loading ITSM task...");
		loadTasks();
		System.out.println("loading ITSM task...end");
	}

	/**
	 * 读取数据库记录，格式化成TaskInfo对象
	 * 
	 * @param rs
	 * @param readHistory
	 *            是否读取clob字段
	 * @return
	 * @throws SQLException
	 * @throws DocumentException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static TaskInfo getTaskInfoFromResultSet(ResultSet rs,
			boolean readHistory) throws SQLException, DocumentException,
			IOException, ParseException {
		TaskInfo taskInfo = new TaskInfo();
		taskInfo.setOid(rs.getInt("TASK_OID"));
		taskInfo.setOrigin(ITSM);
		taskInfo.setParentOid(rs.getInt("TASK_PARENT_OID"));
		taskInfo.setCreatedBy(rs.getString("TASK_CREATE_BY"));
		if (rs.getTimestamp("TASK_CREATE_TIME") != null)
			taskInfo.setCreateTime(rs.getTimestamp("TASK_CREATE_TIME")
					.getTime());
		taskInfo.setWfOid(rs.getInt("TASK_WF_OID"));
		taskInfo.setWfVer(rs.getInt("TASK_WF_VER"));
		taskInfo.setPwfOid(rs.getInt("TASK_PWF_OID"));
		taskInfo.setPwfVer(rs.getInt("TASK_PWF_VER"));
		taskInfo.setTaskNodeId(rs.getString("TASK_NODE_ID"));
		taskInfo.setLastUpdateBy(rs.getString("TASK_UPDATE_BY"));
		if (rs.getTimestamp("TASK_UPDATE_TIME") != null)
			taskInfo.setLastUpdate(rs.getTimestamp("TASK_UPDATE_TIME")
					.getTime());
		taskInfo.setRelations(rs.getString("TASK_RELATIONS"));
		taskInfo.setLinkedTaskStr(rs.getString("TASK_LINKED"));
		taskInfo.setUser(rs.getString("TASK_USER"));
		taskInfo.setStatus(rs.getInt("TASK_STATUS"));

		// System.out.println("load..."+taskInfo.getWfOid()+".."+taskInfo.getOid());
		// 数据记录太多的话，读取clob太慢
		if (readHistory) {
			Clob clob = rs.getClob("TASK_HISTORY");
			taskInfo.setXmlData(ResultSetOperation.clobToString(clob));
		}
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		List columns = wfInfo.getColumnFieldsList();
		for (int i = 0; i < columns.size(); i++) {
			ColumnFieldInfo cinfo = (ColumnFieldInfo) columns.get(i);
			taskInfo.addValue(cinfo.getFieldName(), rs.getString(cinfo
					.getColumnName()));
		}
		return taskInfo;
	}

	/**
	 * 加载工单数据
	 * 
	 * @throws Exception
	 */
	public void loadTasks() throws Exception {

		dataTableList = WorkflowManager.getAllDataTablesName(ITSM, true);

		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		// u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		try {
			List<WorkflowInfo> wfList = WorkflowManager.getRuningWorkflow(true);
			for (int i = 0; i < wfList.size(); i++) {
				WorkflowInfo wfInfo = wfList.get(i);
				if (!wfInfo.getOrigin().equals(ITSM)) {
					continue;
				}
				Object[] tabelNames = wfInfo.getDataTablesName().keySet()
						.toArray();
				for (int tni = 0; tni < tabelNames.length; tni++) {
					String dtName = (String) tabelNames[tni];
					System.out.println("loading task datas(table=" + dtName
							+ ").");
					String sql = "select * from " + dtName;
					if (!wfInfo.isLoadHisToCache())
						sql += " where task_status=" + TaskInfo.STATUS_OPEN;
					sql += " order by TASK_CREATE_TIME desc";
					ps = u.getSelectStatement(sql);
					rs = ps.executeQuery();
					while (rs.next()) {
						try {
							TaskInfo taskInfo = getTaskInfoFromResultSet(rs,
									true);
							if (taskInfo.getParentOid() == -1)
								taskInfo.parse();
							taskInfo.setOrigin(ITSM);
							taskFactory.updateCache(taskInfo);
						} catch (Exception ex) {
							ex.printStackTrace();
							// throw ex;
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

	/**
	 * 工单更新内部处理
	 * 
	 * @param info
	 *            工单对象
	 * @param workflow
	 *            使用流程
	 * @param map
	 *            提交数据
	 * @param fromData
	 *            来源流程节点
	 * @param toNode
	 *            目标流程节点
	 * @param assignTo
	 *            分配至用户
	 * @param operName
	 *            操作人
	 * @param operType
	 *            操作类型
	 * @return 返回生成的工单数据
	 * @throws Exception
	 */
	public TaskData updateTaskInternal(TaskInfo info, WorkflowData workflow,
			Map map, TaskData fromData, NodeInfo toNode, String toNodePath,
			String actionId, String assignTo, String operName, int operType)
			throws Exception {
		TaskData newData = null;
		TaskData newData2 = null;
		if (toNode == null)
			throw new Exception("无法执行目标流程");

		if (fromData == null) { // 新建任务数据
			if (info.getRootData() != null)
				throw new Exception("无法重复创建开始节点");

			NodeInfo rootNode = workflow.getRootNode();
			newData2 = new TaskData(info, null, rootNode, actionId, operName,
					operName);
			info.setRootData(newData2);
			info.addRelation(operName);
			newData2.setNodeDesc(rootNode.getCaption());
			newData2.setNewAdded(false);

			newData = new TaskData(info, newData2, toNode, actionId, assignTo,
					operName);
			newData.setNodeDesc(toNode.getCaption());
			if (operType == TaskUpdateInfo.TYPE_SAVE_FOR_EDIT) {
				newData2.setStatus(TaskData.STATUS_OPEN);
			} else {
				if (newData2.isBranchBegin())
					newData2.setStatus(TaskData.STATUS_PRE_CLOSE);
				else
					newData2.setStatus(TaskData.STATUS_CLOSE);
			}
		} else { // 执行已有任务
			if (operType == TaskUpdateInfo.TYPE_SAVE_FOR_EDIT) {
				if (!fromData.isBranchBegin())
					fromData.removeChildEditing(toNodePath);

				// 如果是根节点的保存为草稿，则还需把跟节点的表单信息做更新，否则有地方显示不对，还是第一次的信息。
				if (fromData.getParent() == null) {
					newData2 = fromData;
				}
			}
			String toNodeCaption = toNode.getCaption();
			newData = new TaskData(info, fromData, toNode, actionId, assignTo,
					operName);
			newData.setNodeDesc(toNodeCaption);
			newData.setActually_task_oid(fromData.getActually_task_oid());
		}

		Object[] fieldIds = map.keySet().toArray();

		String noticeTo = null;
		String noticeMessage = null;
		for (int i = 0; i < fieldIds.length; i++) {
			String key = fieldIds[i].toString();
			if (key.startsWith("_"))
				continue;
			Object obj = map.get(fieldIds[i]);
			if (key.equals(Consts.NOTICE_TO))
				noticeTo = obj.toString();
			else if (key.equals(Consts.NOTICE_MESSAGE))
				noticeMessage = obj.toString();
			if (obj != null) {
				if (key.equals(Consts.FLD_READ_USER))
					newData.setReadUser(obj.toString());

				newData.setValue(key, obj.toString());
				if (newData2 != null) {
					newData2.setValue(key, obj.toString());
				}
				info.addValue(key.toUpperCase(), obj.toString());
			}
			// else
			// System.out.println("debug: taskmgr, " + fieldIds[i] +
			// " is null");
		}

		// 发送短信给通知人
		if (noticeTo != null && noticeMessage != null
				&& noticeMessage.length() > 0) {
			String[] users = noticeTo.split(",");
			for (int i = 0; i < users.length; i++)
				MessageManager.sendSms(noticeMessage, users[i], operName,
						new Date(), operName);
		}

		if (operType == TaskUpdateInfo.TYPE_SAVE_FOR_EDIT) {
			newData.setStatus(TaskData.STATUS_EDIT);
		} else {

			// 如果目标节点没连出的线，则关闭
			// 如果当前节点所处的流程是“子流程节点”相关联的流程，则单单判断toNode.getActions().size() ==
			// 0进行工单关闭是不行的
			// 此情况下，必须还得判断是不是在“子流程节点”相关联的流程里（taskData.getSubNodeId().equals("")）
			if (toNode.getActions().size() == 0
					&& newData.getSubNodeId().equals(""))
				newData.setStatus(TaskData.STATUS_CLOSE);

		}
		newData.setWait(checkWait(workflow.getWorkflowOid(), toNode, assignTo));
		return newData;
	}

	/**
	 * 从指定的节点查找分支开始节点
	 * 
	 * @param taskData
	 *            指定的节点
	 * @param workflow
	 *            流程数据
	 * @return 找到的分支开始节点
	 * @throws Exception
	 */
	public TaskData getBranchBegin(TaskData taskData, WorkflowData workflow)
			throws Exception {
		TaskData b = taskData;
		int adj = 1;
		for (;;) {
			b = b.getParent();
			if (b == null)
				throw new Exception("找不到分支开始结点，请检查流程配置");
			int t = workflow.getNode(b.getNodeId()).getType();
			if (t == NodeInfo.TYPE_BRANCH_END)
				adj++;
			else if (t == NodeInfo.TYPE_BRANCH_BEGIN) {
				adj--;
				if (adj == 0)
					break;
			}
		}
		return b;
	}

	/**
	 * 检查分支结束结点，达到条件时进行合并
	 * 
	 * @param taskInfo
	 * @param bbtaskData
	 *            分支开始节点的taskData
	 * @param workflow
	 * @param operName
	 * @throws Exception
	 */
	public void checkBranchEnd(TaskInfo taskInfo, TaskData bbtaskData,
			WorkflowData workflow, String operName) throws Exception {
		// 取出分支结点所有最后一个结点
		List<TaskData> l = new ArrayList<TaskData>();
		bbtaskData.getLastData(l);
		if (l.size() == 0)
			return;
		List<TaskData> l1 = new ArrayList<TaskData>();
		bbtaskData.getBranchEnd(l1, workflow, 0);
		if(l1.size() == 0)
			return;
		//TaskData newData = taskInfo.getTaskData(taskInfo.getLatestTaskDataId());		
		TaskData newData = null;
		if (!l.containsAll(l1)) {
			return;
		}
		for (Iterator<TaskData> iterator = l.iterator(); iterator.hasNext();) {
			TaskData d = iterator.next();
			if (d.isBranchEnd()){// 过滤子分支已经处理过的节点				
				continue;
			}
			if (d.getStatus() != TaskData.STATUS_CLOSE)
				return;
			// if (newData == null) {
			// newData = d;
			// }
			//if (!newData.getNodeId().equals(d.getNodeId()))
				//return;
			d.setBranchEnd(true);
			newData = d;
		}
		if (bbtaskData.getStatus() == TaskData.STATUS_PRE_CLOSE)
			bbtaskData.setStatus(TaskData.STATUS_CLOSE);

		List<ActionInfo> al = workflow.getNode(newData.getNodeId())
				.getActions();
		if (al.size() > 1) {
			throw new Exception("合并结点只能有一个输出");
		}

		if (al.size() > 0) { // 有下一个结点，开始合并
			newData.setNewAdded(false);
			ActionInfo action = al.get(0);
			NodeInfo toNode1 = action.getToNode();

			TaskUpdateInfo tuInfo = new TaskUpdateInfo();
			tuInfo.setType(TaskUpdateInfo.TYPE_NORMAL);
			tuInfo.setMap(new HashMap<String, String>());
			tuInfo.setOperName(operName);
			tuInfo.setTaskInfo(taskInfo);
			tuInfo.setTaskData(newData);
			tuInfo.setToNode(toNode1);
			tuInfo.setToNodePath(toNode1.getId());
			//tuInfo.setUsers(bbtaskData.getAssignTo().split(",")); //to the start branchBegin user
			tuInfo.setUsers(newData.getAssignTo().split(","));
			tuInfo.setWorkflow(workflow);
			/*
			 * toNode1.onUpdate(tuInfo);
			 * 
			 * TaskData nextTD = new TaskData(taskInfo, newData, toNode1,
			 * action.getActionId(), bbtaskData .getAssignTo(), operName);
			 * nextTD.setNewAdded(true);
			 * nextTD.setNodeDesc(toNode1.getCaption());
			 * 
			 * tuInfo.setTaskData(nextTD); toNode1.onUpdateEnd(tuInfo);
			 */
			updateTask(tuInfo);
		}
	}

	/**
	 * 强制关闭工单
	 * 
	 * @param taskOid
	 * @param message
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int forceCloseTask(int wfOid, int taskOid, String message,
			String operName) throws Exception {
		TaskInfo taskInfo = null;
		taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null)
			throw new Exception("无相关任务数据");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("无法强制关闭工单");
		if (!taskInfo.getCreatedBy().equals(operName))
			throw new Exception("您没有权限强制关闭本工单");
		taskInfo.setStatus(TaskInfo.STATUS_FORCE_CLOSE);
		List<TaskData> l = taskInfo.getTaskData();
		for (int i = 0; i < l.size(); i++) {
			TaskData d = (TaskData) l.get(i);
			d.setStatus(TaskData.STATUS_CLOSE);
		}
		taskInfo.setForceCloseMessage(message);
		// 调用外部接口
		// TaskDataOutputManager.outputTaskData(taskInfo, taskInfo
		// .getLatestTaskDataId(), false, TaskUpdateInfo.TYPE_FORCE_CLOSE);
		TaskEvent event = new TaskEvent(this, taskInfo, taskInfo
				.getLatestTaskDataId(), false, operName,
				TaskUpdateInfo.TYPE_FORCE_CLOSE);
		taskFactory.publishEvent(event);

		updateTaskInfo(taskInfo, operName);
		return 0;
	}

	/**
	 * 强制关闭数据节点，分支开始节点，可对所属分支做操作
	 * 
	 * @param wfOid
	 *            流程OID
	 * @param taskOid
	 *            操作的工单OID
	 * @param taskDataId
	 *            分支开始的数据节点
	 * @param branchTaskDataId
	 *            操作的分支数据节点
	 * @param message
	 *            强制关闭原因
	 * @param operName
	 *            操作人
	 * @return
	 * @throws Exception
	 */
	public int forceCloseTaskData(int wfOid, int taskOid, int taskDataId,
			int branchTaskDataId, String message, String operName)
			throws Exception {
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null)
			throw new Exception("无相关任务数据");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("无法进行操作，工单已关闭");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("无相关任务详细数据");
		if (!taskData.canDealThisNode(operName))
			throw new Exception("您没有权限操作本工单");

		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		NodeInfo node = workflow.getNode(taskData.getNodeId());
		if (node.getType() != NodeInfo.TYPE_BRANCH_BEGIN)
			throw new Exception("无法进行操作，内部错误");

		TaskData branchTD = taskInfo.getTaskData(branchTaskDataId);
		if (branchTD == null)
			throw new Exception("无相关任务详细数据");
		if (branchTD.getStatus() == TaskData.STATUS_CLOSE
				|| branchTD.getStatus() == TaskData.STATUS_FORCE_CLOSE)
			throw new Exception("目标节点已关闭，无法进行操作");

		TaskData bbData = getBranchBegin(branchTD, workflow);
		if (bbData.getDataId() != taskData.getDataId()) {
			throw new Exception("目标节点不属于此分支可控范围内，无法进行操作");
		}

		branchTD.setValue("forceCloseMessage", message);
		branchTD.setStatus(TaskData.STATUS_FORCE_CLOSE);
		branchTD.setNewAdded(true);
		if (taskData.getStatus() == TaskData.STATUS_PRE_CLOSE) {
			taskData.setStatus(TaskData.STATUS_OPEN);
		}

		// checkBranchEnd(taskInfo, taskData, workflow, operName);
		TaskEvent event = new TaskEvent(this, taskInfo, taskData.getDataId(),
				false, operName, TaskUpdateInfo.TYPE_NORMAL);
		taskFactory.publishEvent(event);
		// updateTaskInfo(taskInfo, operName);
		return 0;
	}

	/**
	 * 移除指定节点下的指定子节点
	 * 
	 * @param wfOid
	 * @param taskOid
	 * @param taskDataId
	 * @param removeTaskDataId
	 * @param operName
	 * @return
	 * @throws Exception
	 */
	public int removeTaskData(int wfOid, int taskOid, int taskDataId,
			int removeTaskDataId, String operName) throws Exception {
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null)
			throw new Exception("无相关任务数据");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("无法进行操作，工单已关闭");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("无相关任务详细数据");
		if (!taskData.canDealThisNode(operName))
			throw new Exception("您没有操作权限");
		TaskData removeData = taskInfo.getTaskData(removeTaskDataId);
		taskData.removeChild(removeTaskDataId);
		if (removeData.getActually_task_oid() != taskInfo.getOid())
			deleteTaskInfo(taskInfo.getWfOid(), taskInfo.getWfVer(), removeData
					.getActually_task_oid(), operName);

		if (taskData.getStatus() == TaskData.STATUS_PRE_CLOSE) {
			taskData.setStatus(TaskData.STATUS_OPEN);
		}
		TaskEvent event = new TaskEvent(this, taskInfo, taskData.getDataId(),
				false, operName, TaskUpdateInfo.TYPE_NORMAL);
		taskFactory.publishEvent(event);
		return 0;
	}

	/**
	 * 关闭分支开始节点，不再进行分配
	 * 
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int closeTaskData(int wfOid, int taskOid, int taskDataId,
			String operName) throws Exception {
		TaskInfo taskInfo = null;
		taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null)
			throw new Exception("无相关任务数据");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("无法进行操作，工单已关闭");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("无相关任务详细数据");
		if (!taskData.canDealThisNode(operName))
			throw new Exception("您没有权限关闭本工单");

		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		NodeInfo node = workflow.getNode(taskData.getNodeId());
		if (node.getType() != NodeInfo.TYPE_BRANCH_BEGIN)
			throw new Exception("无法进行操作，内部错误");
		taskData.setStatus(TaskData.STATUS_PRE_CLOSE);
		taskData.removeChildEditing(null);
		taskData.setNewAdded(true);

		checkBranchEnd(taskInfo, taskData, workflow, operName);
		TaskEvent event = new TaskEvent(this, taskInfo, taskData.getDataId(),
				false, operName, TaskUpdateInfo.TYPE_NORMAL);
		taskFactory.publishEvent(event);
		// updateTaskInfo(taskInfo, operName);
		return 0;
	}

	/**
	 * 更新工单数据
	 * 
	 * @param taskOid
	 *            工单ID，新建时为-1
	 * @param workflowOid
	 *            流程ID
	 * @param map
	 *            前台传过来的表单MAP数组
	 * @param dataId
	 *            当前数据节点ID，新建时为-1
	 * @param toNodeId
	 *            目标流程节点ID，新建时为流程配置里面根节点下的第二个节点的ID
	 * @param actionId
	 *            执行的动作ID
	 * @param assignTo
	 *            工单下一步处理人
	 * @param operName
	 *            工单当前操作人
	 * @param operType
	 *            操作类型，如果＝TaskUpdateInfo.TYPE_SAVE，则为仅保存更新不流转（2008-03-28新加）
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int updateTask(int taskOid, int workflowOid, Map map, int dataId,
			String toNodeId, String actionId, String assignTo, String operName,
			int operType) throws Exception {
		TaskUpdateInfo updateInfo = new TaskUpdateInfo();
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(workflowOid);
		WorkflowData workflow = null;
		TaskInfo taskInfo = null;
		TaskData taskData = null;
		NodeInfo toNode = null;
		NodeInfo fromNode = null;

		if (taskOid == -1) {
			workflow = wfInfo.getCurrentVersion();
			if (toNodeId == null || toNodeId.equals("")) {
				ActionInfo action = workflow.getRootNode().getActions().get(0);
				actionId = action.getActionId();
				toNode = action.getToNode();
				toNodeId = toNode.getId();
			} else {
				toNode = workflow.getNode(toNodeId);
			}
		} else {
			taskInfo = getTaskInfoByOid(taskOid, workflowOid, -1);
			if (taskInfo == null)
				throw new Exception("无相关任务数据");
			workflow = wfInfo.getVersion(taskInfo.getWfVer());
			taskData = taskInfo.getTaskData(dataId);
			toNode = workflow.getNode(toNodeId);
			fromNode = workflow.getNode(taskData.getNodeId());
			if (taskInfo.getWfOid() != workflowOid)
				throw new Exception("传入参数不正确");
			if (taskData.getStatus() == TaskData.STATUS_CLOSE
					|| taskData.getStatus() == TaskData.STATUS_FORCE_CLOSE)
				throw new Exception("请不要重复提交数据");
			if (taskData.getStatus() == TaskData.STATUS_WAITING) {
				TaskWaitingInfo twInfo = taskData.getWaitInfo();
				throw new Exception("正在等待其他的工单(" + twInfo.getWaiting_task_oid()
						+ ")的处理结束");
			}
			// 1、不是当前处理人并且当前节点不是是共享处理节点，没权限
			// 2、当前节点时是共享节点且操作类型不是保存草稿，没权限
			if (!taskData.canDealThisNode(operName)
					&& (!fromNode.isShareDeal() || fromNode.isShareDeal()
							&& operType != TaskUpdateInfo.TYPE_SAVE_FOR_EDIT))
				throw new Exception("您无权操作本工单");
		}
		String[] users = assignTo.split(",");

		updateInfo.setType(operType);
		updateInfo.setMap(map);
		updateInfo.setOperName(operName);
		updateInfo.setTaskData(taskData);
		updateInfo.setTaskInfo(taskInfo);
		updateInfo.setAction(actionId);
		updateInfo.setToNode(toNode);
		updateInfo.setToNodePath(toNodeId);
		updateInfo.setUsers(users);
		updateInfo.setWorkflow(workflow);
		return updateTask(updateInfo);
	}

	/**
	 * 更新工单流转（适用于无分支的外部系统调用）
	 * 
	 * @param taskOid
	 *            工单OID 新建时为-1
	 * @param workflowOid
	 *            流程OID
	 * @param map
	 *            提交的数据map数组
	 * @param dataId
	 *            更新的数据节点，为-1时，取最新的数据节点更新
	 * @param assignTo
	 *            下一步执行人，为""则当前操作人
	 * @param operName
	 *            操作人
	 * @param operType
	 *            更新类型（正常流转、回退等等）
	 * @return
	 * @throws Exception
	 */
	public int updateToNext(int taskOid, int workflowOid,
			Map<String, String> map, int dataId, String assignTo,
			String operName, int operType) throws Exception {
		TaskUpdateInfo updateInfo = new TaskUpdateInfo();
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(workflowOid);
		WorkflowData workflow = null;
		TaskInfo taskInfo = null;
		TaskData taskData = null;
		NodeInfo toNode = null;
		String toNodePath = "";

		if (taskOid == -1) {
			workflow = wfInfo.getCurrentVersion();
			toNode = workflow.getRootNode().getActions().get(0).getToNode();
		} else {
			taskInfo = getTaskInfoByOid(taskOid, workflowOid, -1);
			if (taskInfo == null)
				throw new Exception("无相关任务数据");
			if (taskInfo.getWfOid() != workflowOid)
				throw new Exception("传入参数不正确");

			if (dataId != -1)
				taskData = taskInfo.getTaskData(dataId);
			else {
				// 取第一个打开的节点；
				List<TaskData> activeList = taskInfo.getTaskData();
				if (activeList != null && activeList.size() > 0)
					taskData = activeList.get(0);
				// taskData =
				// taskInfo.getTaskData(taskInfo.getLatestTaskDataId());
			}
			if (taskData == null)
				throw new Exception("找不到对应的数据信息");
			if (operType == TaskUpdateInfo.TYPE_ROLLBACK) {
				if (taskData.getParent() == null)
					throw new Exception("当前为首节点，无法回退本工单");
				if (!taskData.canDealThisNode(operName))
					throw new Exception("您无权回退本工单");
				if (taskData.isRollback())
					throw new Exception("已回退，请不要重复提交");
				if (taskData.getStatus() == TaskData.STATUS_CLOSE)
					throw new Exception("已关闭状态无法回退");
				if (!taskData.isRollbackable())
					throw new Exception("当前节点不允许回退，如需回退请与管理员联系");
			}
			if (taskData.getStatus() == TaskData.STATUS_CLOSE)
				throw new Exception("请不要重复提交数据");
			if (taskData.getStatus() == TaskData.STATUS_WAITING) {
				TaskWaitingInfo twInfo = taskData.getWaitInfo();
				throw new Exception("正在等待其他的工单(" + twInfo.getWaiting_task_oid()
						+ ")的处理结束");
			}

			if (!taskData.canDealThisNode(operName))
				throw new Exception("您无权操作本工单");

			workflow = wfInfo.getVersion(taskInfo.getWfVer());
			NodeInfo fromNode = WorkflowData.getNodeByPath(workflow, taskData
					.getNodeId());
			List<ActionInfo> aiList = fromNode.getActions();
			if (aiList.size() > 0)
				toNode = WorkflowData.getNodeByPath(workflow, aiList.get(0)
						.getToNodeId());
		}
		toNodePath = toNode.getId();

		String[] users = assignTo.split(",");

		updateInfo.setType(operType);
		updateInfo.setMap(map);
		updateInfo.setOperName(operName);
		updateInfo.setTaskData(taskData);
		updateInfo.setTaskInfo(taskInfo);
		updateInfo.setToNode(toNode);
		updateInfo.setToNodePath(toNodePath);
		updateInfo.setUsers(users);
		updateInfo.setWorkflow(workflow);
		return updateTask(updateInfo);
	}

	/**
	 * 更新工单，仅仅更新工单里的字段信息，不进行工单的流转驱动
	 * 
	 * @param taskOid
	 * @param workflowOid
	 * @param map
	 * @param operName
	 * @return
	 * @throws Exception
	 */
	public int updateTask(int taskOid, int workflowOid,
			Map<String, String> map, String operName) throws Exception {
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, workflowOid, -1);
		if (taskInfo == null)
			throw new Exception("无相关任务数据");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("工单已关闭");

		int lastTaskDataId = taskInfo.getLatestTaskDataId();
		TaskData taskData = taskInfo.getTaskData(lastTaskDataId);
		if (taskData == null)
			throw new Exception("找不到所请求的数据节点");

		Set<String> key = map.keySet();
		for (Iterator<String> ite = key.iterator(); ite.hasNext();) {
			String fieldId = ite.next();
			String value = map.get(fieldId);

			taskData.setValue(fieldId, value);
		}
		taskData.setNewAdded(true);

		// taskFactory.updateCache(taskInfo);
		TaskEvent event = new TaskEvent(this, taskInfo, lastTaskDataId, false,
				operName, TaskUpdateInfo.TYPE_UPDATE_FIELD);
		taskFactory.publishEvent(event);
		return 0;
	}

	/**
	 * 回退工单
	 * 
	 * @param taskOid
	 * @param taskDataId
	 * @param rollbackMessage
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int rollbackTask(int wfOid, int taskOid, int taskDataId,
			String rollbackMessage, String operName) throws Exception {
		TaskUpdateInfo updateInfo = new TaskUpdateInfo();
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null) {
			throw new Exception("找不到任务");
		}
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		if (wfInfo == null)
			throw new Exception("找不到流程信息");
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		if (workflow == null)
			throw new Exception("找不到流程版本信息");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("找不到对应的数据信息");
		if (taskData.getParent() == null)
			throw new Exception("当前为首节点，无法回退本工单");
		if (!taskData.canDealThisNode(operName))
			throw new Exception("您无权回退本工单");
		if (taskData.isRollback())
			throw new Exception("已回退，请不要重复提交");
		if (taskData.getStatus() == TaskData.STATUS_CLOSE)
			throw new Exception("已关闭状态无法回退");
		if (!taskData.isRollbackable())
			throw new Exception("当前节点不允许回退，如需回退请与管理员联系");
		Map map = new HashMap();
		map.put(Consts.FLD_ROLLBACK, rollbackMessage);
		updateInfo.setType(TaskUpdateInfo.TYPE_ROLLBACK);
		updateInfo.setMap(map);
		updateInfo.setOperName(operName);
		updateInfo.setTaskData(taskData);
		updateInfo.setTaskInfo(taskInfo);
		updateInfo.setToNode(null);
		updateInfo.setUsers(null);
		updateInfo.setWorkflow(workflow);
		return updateTask(updateInfo);
	}

	/**
	 * 对工单添加意见
	 * 
	 * @param taskOid
	 * @param taskDataId
	 * @param message
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int addTaskMessage(int wfOid, int taskOid, int taskDataId,
			String message, String operName) throws Exception {
		TaskUpdateInfo updateInfo = new TaskUpdateInfo();
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null) {
			throw new Exception("找不到任务");
		}
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		if (wfInfo == null)
			throw new Exception("找不到流程信息");
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		if (workflow == null)
			throw new Exception("找不到流程版本信息");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("找不到对应的数据信息");
		Map map = new HashMap();
		map.put(Consts.FLD_MESSAGE, message);
		updateInfo.setType(TaskUpdateInfo.TYPE_MESSAGE);
		updateInfo.setMap(map);
		updateInfo.setOperName(operName);
		updateInfo.setTaskData(taskData);
		updateInfo.setTaskInfo(taskInfo);
		updateInfo.setToNode(null);
		updateInfo.setUsers(null);
		updateInfo.setWorkflow(workflow);
		return updateTask(updateInfo);
	}

	/**
	 * 接受工单（分配到自己组的）
	 * 
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int acceptTask(int wfOid, int taskOid, int taskDataId,
			String operName) throws Exception {
		TaskUpdateInfo updateInfo = new TaskUpdateInfo();
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null) {
			throw new Exception("找不到任务");
		}
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("无法进行操作，工单已关闭");

		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		if (wfInfo == null)
			throw new Exception("找不到流程信息");
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		if (workflow == null)
			throw new Exception("找不到流程版本信息");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("找不到对应的数据信息");
		if (taskData.getStatus() != TaskData.STATUS_OPEN)
			throw new Exception("无法进行操作，工单已处理");
		Map map = new HashMap();
		updateInfo.setType(TaskUpdateInfo.TYPE_ACCEPT);
		updateInfo.setMap(map);
		updateInfo.setOperName(operName);
		updateInfo.setTaskData(taskData);
		updateInfo.setTaskInfo(taskInfo);
		updateInfo.setToNode(null);
		updateInfo.setUsers(null);
		updateInfo.setWorkflow(workflow);
		return updateTask(updateInfo);
	}

	/**
	 * 增加阅知信息
	 * 
	 * @param wfOid
	 * @param taskOid
	 * @param taskDataId
	 * @param message
	 * @param operName
	 * @return
	 * @throws Exception
	 */
	public int readApply(int wfOid, int taskOid, int taskDataId,
			String message, String operName) throws Exception {
		TaskUpdateInfo updateInfo = new TaskUpdateInfo();
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null) {
			throw new Exception("找不到任务");
		}
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		if (wfInfo == null)
			throw new Exception("找不到流程信息");
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		if (workflow == null)
			throw new Exception("找不到流程版本信息");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("找不到对应的数据信息");
		Map<String, String> map = new HashMap<String, String>();
		map.put(Consts.FLD_MESSAGE, message);
		updateInfo.setType(TaskUpdateInfo.TYPE_READ);
		updateInfo.setMap(map);
		updateInfo.setOperName(operName);
		updateInfo.setTaskData(taskData);
		updateInfo.setTaskInfo(taskInfo);
		updateInfo.setToNode(null);
		updateInfo.setUsers(null);
		updateInfo.setWorkflow(workflow);
		return updateTask(updateInfo);
	}

	/**
	 * 更新工单
	 * 
	 * @param updateInfo
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public synchronized int updateTask(TaskUpdateInfo updateInfo)
			throws Exception {
		TaskData taskData = updateInfo.getTaskData();
		TaskInfo taskInfo = updateInfo.getTaskInfo();
		WorkflowData workflow = updateInfo.getWorkflow();
		NodeInfo toNode = updateInfo.getToNode();
		String actionId = updateInfo.getAction();
		String toNodePath = updateInfo.getToNodePath();
		String operName = updateInfo.getOperName();

		if (updateInfo.getType() != TaskUpdateInfo.TYPE_NORMAL
				&& updateInfo.getType() != TaskUpdateInfo.TYPE_SAVE_FOR_EDIT
				&& updateInfo.getType() != TaskUpdateInfo.TYPE_DYNAMIC_BRENCH) {

			if (updateInfo.getType() == TaskUpdateInfo.TYPE_ACCEPT) {
				taskData.setStatus(TaskData.STATUS_CLOSE);
				NodeInfo node = workflow.getNode(taskData.getNodeId());
				TaskData newData = new TaskData(taskInfo, taskData, node, null,
						operName, operName);
				newData.setNodeDesc(taskData.getNodeDesc());
			} else if (updateInfo.getType() == TaskUpdateInfo.TYPE_MESSAGE) {
				String msg = (String) updateInfo.getMap().get(
						Consts.FLD_MESSAGE);
				taskData.addMessage(msg, operName);
				taskData.setNewAdded(true);
			} else if (updateInfo.getType() == TaskUpdateInfo.TYPE_READ) {
				String msg = (String) updateInfo.getMap().get(
						Consts.FLD_MESSAGE);
				taskData.addReadMessage(msg, operName);
				taskData.setNewAdded(true);
			} else if (updateInfo.getType() == TaskUpdateInfo.TYPE_ROLLBACK) {
				String msg = (String) updateInfo.getMap().get(
						Consts.FLD_ROLLBACK);
				taskData.rollback(msg, workflow, operName);
				taskData.setNewAdded(true);
			} else if (updateInfo.getType() == TaskUpdateInfo.TYPE_UPDATE_BATCH) {
				List<TaskData> childEdit = taskData.getChildEditing();
				for (int i = 0; i < childEdit.size(); i++) {
					TaskData td = childEdit.get(i);
					updateInfo.setMap(td.getValues());
					NodeInfo toNode_ = workflow.getNode(td.getNodeId());
					updateInfo.setToNode(toNode);
					updateInfo.setToNodePath(td.getNodeId());
					updateInfo.setUsers(new String[] { td.getAssignTo() });
					td.setStatus(TaskData.STATUS_OPEN);
					toNode_.onUpdate(updateInfo);

					updateInfo.setTaskData(td);
					toNode_ = updateInfo.getToNode();
					toNode_.onUpdateEnd(updateInfo);
				}

				List l0 = new ArrayList();
				taskData.getNextAction(l0, workflow);
				if (l0.size() == 0) {
					taskData.setStatus(TaskData.STATUS_CLOSE);
				}
			}
			// 调用输出接口
			TaskEvent event = new TaskEvent(this, taskInfo, taskData
					.getDataId(), false, operName, updateInfo.getType());
			taskFactory.publishEvent(event);
			return 0;
		}
		NodeInfo tempNode = updateInfo.getToNode();
		if (updateInfo.getType() != TaskUpdateInfo.TYPE_SAVE_FOR_EDIT && tempNode != null) {
			tempNode.onUpdate(updateInfo);
		}
		String[] users = updateInfo.getUsers();
		Map<String, String> map = updateInfo.getMap();

		if (taskInfo == null) { // 新建任务
			TaskData rootData = null;
			// TaskInfo info = null;
			for (int i = 0; i < users.length; i++) {
				if (users[i].trim().length() == 0)
					continue;

				if (taskInfo == null) {
					taskInfo = new TaskInfo(workflow, operName);
					taskInfo.setOid(ItsmUtil.getSequence("task"));
					updateInfo.setTaskInfo(taskInfo);
				}
				// 关联工单字段
				if (map.get("_sys_linkTaskStr") != null)
					taskInfo.setLinkedTaskStr((String) map
							.get("_sys_linkTaskStr"));

				// 第三方传进来的工单ID
				if (map.get("_sys_thirdId") != null)
					taskInfo.setThirdID((String) map.get("_sys_thirdId"));

				TaskData td = updateTaskInternal(taskInfo, workflow, map,
						rootData, toNode, toNodePath, actionId,
						users[i].trim(), operName, updateInfo.getType());

				// 分支分记录存放
				if (rootData != null)
					td.setNewBranch(true);
				// 处理目标节点是子流程节点的情况
				doSubflowNode(td);
				if (rootData == null) {
					rootData = td.getParent();
				}
				updateInfo.setTaskData(td);
				if (updateInfo.getType() != TaskUpdateInfo.TYPE_SAVE_FOR_EDIT) {
					// 保存草稿的时候此动态函数不执行
					toNode.onUpdateEnd(updateInfo);
				}

			}

			TaskEvent event = new TaskEvent(this, taskInfo, rootData
					.getDataId(), true, operName, updateInfo.getType());
			taskFactory.publishEvent(event);
			return taskInfo.getOid();
		}

		for (int i = 0; i < users.length; i++) {
			if (users[i].trim().length() == 0)
				continue;
			boolean newBranch = false;
			// 如果开始分支
			List ac = taskData.getActivateChilds();
			if (ac.size() > 0) {
				newBranch = true;
			}
			
			// 关联工单字段
			if (map.get("_sys_linkTaskStr") != null)
				taskInfo.setLinkedTaskStr((String) map
						.get("_sys_linkTaskStr"));
			// 第三方传进来的工单ID
			if (map.get("_sys_thirdId") != null)
				taskInfo.setThirdID((String) map.get("_sys_thirdId"));
			
			TaskData td = updateTaskInternal(taskInfo, workflow, map, taskData,
					toNode, toNodePath, actionId, users[i].trim(), operName,
					updateInfo.getType());
			// 处理目标节点是子流程节点的情况
			doSubflowNode(td);

			// 如果是子流程节点，则分记录保存，保存到相应的流程数据表里
			if (taskData.getActuallyWorkflowOid() != -1
					&& toNode.getOwner().getWorkflowOid() != taskData
							.getActuallyWorkflowOid()
					&& toNode.getOwner().getRootNode().getId().equals(
							toNode.getId()))
				newBranch = true;

			td.setNewBranch(newBranch);
			updateInfo.setTaskData(td);

			if (updateInfo.getType() != TaskUpdateInfo.TYPE_SAVE_FOR_EDIT) {
				toNode.onUpdateEnd(updateInfo);
			}
		}

		List l0 = new ArrayList();
		taskData.getNextAction(l0, workflow);

		if (l0.size() == 0
				&& updateInfo.getType() != TaskUpdateInfo.TYPE_SAVE_FOR_EDIT
				&& updateInfo.getType() != TaskUpdateInfo.TYPE_DYNAMIC_BRENCH) {
			if (taskData.isBranchBegin()
					&& taskData.branchBeginPreClose(workflow)) 
				taskData.setStatus(TaskData.STATUS_PRE_CLOSE);
			 else
				taskData.setStatus(TaskData.STATUS_CLOSE);

			taskData.removeChildEditing(toNode.getId());
		}

		if (updateInfo.getType() != TaskUpdateInfo.TYPE_SAVE_FOR_EDIT) {
			// 判断分支结束
			TaskData newData = taskInfo.getTaskData(taskInfo
					.getLatestTaskDataId());
			NodeInfo newNode = workflow.getNode(newData.getNodeId());
			if (newNode.getType() == NodeInfo.TYPE_BRANCH_END) {
				newData.setStatus(TaskData.STATUS_CLOSE);
				// 取开始分支节点
				TaskData b = getBranchBegin(newData, workflow);

				// 分支结点关闭时才可以做合并
				// 由以前的判断STATUS_CLOSE状态，改为判断STATUS_CLOSE_PRE
				if (b.getStatus() == TaskData.STATUS_CLOSE
						|| b.getStatus() == TaskData.STATUS_PRE_CLOSE) {

					checkBranchEnd(taskInfo, b, workflow, operName);
				}
			}
		}
		//make the parent taskdata wait false
		taskData.setWait(false);
		TaskEvent event = new TaskEvent(this, taskInfo, taskData.getDataId(),
				false, operName, updateInfo.getType());
		taskFactory.publishEvent(event);

		return taskInfo.getOid();
	}

	/**
	 * 如果节点是子流程节点，则把当前节点信息改为真实的节点信息。
	 * 
	 * @param td
	 */
	public void doSubflowNode(TaskData td) {
		try {
			TaskInfo ti = td.getOwner();
			WorkflowInfo wi = WorkflowManager.getWorkflowByOid(ti.getWfOid());
			WorkflowData wd = wi.getVersion(ti.getWfVer());
			NodeInfo node = WorkflowData.getNodeByPath(wd, td.getNodePath());
			td.setNodeDesc(node.getCaption());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List getTaskInfosByUser(String operName) throws IOException,
			DocumentException, ParseException, SQLException {
		TaskInfo taskInfo = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		List l = new ArrayList();
		// u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		try {
			dataTableList = WorkflowManager.getAllDataTablesName(ITSM, true);
			for (int i = 0; i < dataTableList.size(); i++) {
				String dtName = (String) dataTableList.get(i);
				String sql = "select * from " + dtName
						+ " where TASK_USERS like ? order by TASK_CREATED desc";
				ps = u.getSelectStatement(sql);
				ps.setString(1, "%/" + operName + "/%");
				rs = ps.executeQuery();
				while (rs.next()) {
					taskInfo = getTaskInfoFromResultSet(rs, false);
					taskInfo.setOrigin(ITSM);
					l.add(taskInfo);
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return l;

	}

	/**
	 * @deprecated
	 */
	public TaskInfo getTaskInfoByOid(int oid, int wfOid) throws SQLException,
			IOException, DocumentException, ParseException {
		return getTaskInfoByOid(oid, wfOid, -1);
	}

	/**
	 * 通过oid查找工单
	 * 
	 * @param oid
	 * @return 找到的工单
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParseException
	 * @throws DocumentException
	 */
	public TaskInfo getTaskInfoByOid(int oid, int wfOid, int wfVer)
			throws SQLException, IOException, DocumentException, ParseException {
		if (oid == -1)
			return null;
		TaskInfo taskInfo = null;
		if (Cache.Tasks.get(ITSM + "_" + wfOid + "_" + oid) != null) {
			taskInfo = (TaskInfo) Cache.Tasks.get(ITSM + "_" + wfOid + "_"
					+ oid);
			return taskInfo.cloneInfo();
		} else if (Cache.TasksHis.get(ITSM + "_" + wfOid + "_" + oid) != null) {
			taskInfo = (TaskInfo) Cache.TasksHis.get(ITSM + "_" + wfOid + "_"
					+ oid);
			return taskInfo.cloneInfo();
		}

		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
		if (wfInfo == null)
			return null;
		String dataTable = "itsm_task_" + wfOid;
		WorkflowData wfData = wfInfo.getVersion(wfVer);
		if (wfData != null)
			dataTable = wfData.getDataTable();
		String sql = "select * from " + dataTable + " where TASK_OID=?";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		// u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		try {
			ps = u.getSelectStatement(sql);
			ps.setInt(1, oid);
			rs = ps.executeQuery();
			if (rs.next()) {
				taskInfo = getTaskInfoFromResultSet(rs, true);
				taskInfo.parse();
				taskInfo.setOrigin(ITSM);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return taskInfo;
	}

	/**
	 * 更新工单至数据库 更新记录：分支另开记录，xml数据统一存在主记录上，分支记录仅供检索，没实际意义
	 * 
	 * @param incInfo
	 * @param operName
	 * @return 数据库中工单的oid
	 * @throws SQLException
	 * @throws DocumentException
	 * @throws IOException
	 */
	public int updateTaskInfo(TaskInfo incInfo, String operName)
			throws SQLException, IOException, DocumentException {
		return 0;
	}

	/**
	 * 把节点数据更新至表列
	 * 
	 * @param taskData
	 * @param operName
	 * @throws SQLException
	 */
	public void updateTaskDataToDB(TaskData taskData, String operName)
			throws SQLException {

	}

	/**
	 * 删除工单
	 * 
	 * @param oid
	 *            记录ID
	 * @param operName
	 * @throws SQLException
	 */
	public void deleteTaskInfo(int wfOid, int oid, String operName)
			throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("itsm_task_" + wfOid, operName);
		try {
			ps = u.getStatement("TASK_OID=?");
			ps.setInt(1, oid);
			rs = ps.executeQuery();
			u.executeDelete(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

	public void deleteTaskInfo(int wfOid, int wfVer, int oid, String operName)
			throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
		if (wfInfo == null)
			return;
		WorkflowData wfData = wfInfo.getVersion(wfVer);
		if (wfData == null)
			return;
		OracleOperation u = new OracleOperation(wfData.getDataTable(), operName);
		try {
			ps = u.getStatement("TASK_OID=?");
			ps.setInt(1, oid);
			rs = ps.executeQuery();
			u.executeDelete(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

	/**
	 * 获取参与过的任务
	 * 
	 * @param user
	 * @return
	 */
	public List getHistoryTaskInfoByUser(String operName) throws IOException,
			DocumentException, ParseException, SQLException {
		TaskInfo taskInfo = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		List l = new ArrayList();
		// u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		try {
			dataTableList = WorkflowManager.getAllDataTablesName(ITSM, true);
			for (int i = 0; i < dataTableList.size(); i++) {
				String dtName = (String) dataTableList.get(i);
				String sql = "select * from "
						+ dtName
						+ " where TASK_RELATIONS like ? order by TASK_CREATED desc";
				ps = u.getSelectStatement(sql);
				ps.setString(1, "%/" + operName + "/%");
				rs = ps.executeQuery();
				while (rs.next()) {
					taskInfo = getTaskInfoFromResultSet(rs, false);
					taskInfo.setOrigin(ITSM);
					l.add(taskInfo);
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return l;
	}

	/**
	 * 获取所有的
	 * 
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getAllTaskInfo() throws IOException, DocumentException,
			ParseException {
		List retList = new ArrayList();

		// 取所有流程配置中，所有流程版本数据存放的表名
		dataTableList = WorkflowManager.getAllDataTablesName(ITSM, true);
		System.out.println(System.currentTimeMillis() + "===1");

		for (int i = 0; i < dataTableList.size(); i++) {
			String dtName = (String) dataTableList.get(i);
			String sql = "select * from " + dtName + " order by TASK_OID desc";
			try {
				retList.addAll(getTaskInfoBySQL(sql));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(System.currentTimeMillis() + "===1");

		return retList;
	}

	public List getAllTaskInfo(String beginTime, String endTime)
			throws IOException, DocumentException, ParseException, SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from itsm_task_147 ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (endTime == null || endTime.equals("")) {
			sql.append(" where to_char(TASK_CREATE_TIME,'yyyy-MM-dd') <= '"
					+ sdf.format(new Date(System.currentTimeMillis())) + "' ");
		} else {
			sql.append(" where to_char(TASK_CREATE_TIME,'yyyy-MM-dd') <= '"
					+ endTime + "' ");
		}
		if (beginTime != null && !beginTime.equals("")) {
			sql.append(" and to_char(TASK_CREATE_TIME,'yyyy-MM-dd') >= '"
					+ beginTime + "' ");
		}
		sql.append(" order by TASK_CREATE_TIME desc");

		List l = new ArrayList();
		dataTableList = WorkflowManager.getAllDataTablesName(ITSM, true);
		for (int i = 0; i < dataTableList.size(); i++) {
			String dtName = (String) dataTableList.get(i);
			l.addAll(getTaskInfoBySQL(sql.toString().replaceFirst(
					"itsm_task_147", dtName)));
		}
		return l;
	}

	/**
	 * 获取所有打开的工单
	 * 
	 * @return 返回所有打开的工单List[TaskData]
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List<TaskData> getAllOpenedTasks(int wfOid) {
		List<TaskInfo> l = getAllOpenedTaskInfos(wfOid);
		List<TaskData> ret = new ArrayList<TaskData>();
		for (Iterator<TaskInfo> iterator = l.iterator(); iterator.hasNext();) {
			TaskInfo info = (TaskInfo) iterator.next();
			List<TaskData> l1 = info.getTaskData();
			ret.addAll(l1);
		}		
		return ret;
	}

	/**
	 * 获取所有打开的工单
	 * 
	 * @return 返回所有打开的工单List[TaskInfo]
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List<TaskInfo> getAllOpenedTaskInfos(int wfOid) {
		List<TaskInfo> l = new ArrayList<TaskInfo>();
		if (wfOid == -1) {
			l.addAll(Cache.Tasks.values());
		} else {
			Map<String, TaskInfo> m = Cache.Workflow_Tasks.get(wfOid + "");
			if (m != null)
				l.addAll(m.values());
		}
		return l;
	}

	/**
	 * 根据sql查询
	 * 
	 * @param sql
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List<TaskInfo> getTaskInfoBySQL(String sql) throws IOException,
			DocumentException, ParseException, SQLException {
		return getTaskInfoBySQL(sql, false);
	}

	public List<TaskInfo> getTaskInfoBySQL(String sql, boolean readClob)
			throws IOException, DocumentException, ParseException, SQLException {

		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		List<TaskInfo> l = new ArrayList<TaskInfo>();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				try {
					TaskInfo taskInfo = getTaskInfoFromResultSet(rs, readClob);
					taskInfo.setOrigin(ITSM);
					l.add(taskInfo);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return l;
	}

	/**
	 * 2008-01-02 10:02:20新加
	 * 
	 * @param sql
	 *            可以带:p参数的sql串
	 * @param request
	 * @return
	 * @throws SQLException
	 * 
	 */
	public List<TaskInfo> getTaskInfoBySQL(String user, String password,
			String url, String sql, HttpServletRequest request) {
		ResultSet rs = null;
		OraclePreparedStatement ps = null;
		Connection conn = null;
		List<TaskInfo> l = new ArrayList<TaskInfo>();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			if (url != null) {
				if (!url.startsWith("jdbc:oracle:thin:@"))
					url = "jdbc:oracle:thin:@" + url;
			}
			conn = DriverManager.getConnection(url, user, password);

			ps = (OraclePreparedStatement) conn.prepareStatement(sql);
			List parameters = ItsmUtil.getSQLParameters(sql);
			for (int i = 0; i < parameters.size(); i++) {
				String fieldName = (String) parameters.get(i);
				String value = null;
				if (request != null)
					value = request.getParameter("form_search_" + fieldName);
				if (value == null || value.equals(""))
					value = null;
				if (value != null)
					value = StringUtil.getGBStr(value);
				ps.setObjectAtName(fieldName, value);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				try {
					TaskInfo taskInfo = getTaskInfoFromResultSet(rs, false);
					taskInfo.setOrigin(ITSM);
					if (taskInfo.getStatus() == TaskInfo.STATUS_OPEN) {
						TaskInfo t_ = getTaskInfoByOid(taskInfo.getOid(),
								taskInfo.getWfOid(), taskInfo.getPwfVer());
						l.add(t_);
					} else
						l.add(taskInfo);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return l;
	}

	public List<TaskInfo> getAllClosedTaskInfos(String beginTime,
			String endTime, int wfOid) throws IOException, DocumentException,
			ParseException, SQLException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (endTime == null || endTime.equals("")) {
			endTime = sdf.format(new Date(System.currentTimeMillis()));
		} else {
			if (endTime.length() > 10)
				endTime = endTime.substring(0, 10);
		}

		if (beginTime == null || beginTime.equals("")) {
			beginTime = "1980-12-03";					// the birthdy of lihz ??? yeah, I think so. hahaaa
		} else {
			if (beginTime.length() > 10)
				beginTime = beginTime.substring(0, 10);
		}

		beginTime = beginTime + " 00:00:00";
		endTime = endTime + " 23:59:59";
		long bt = sdf.parse(beginTime).getTime();
		long et = sdf.parse(endTime).getTime();

		StringBuffer sql = new StringBuffer();
		sql
				.append("select * from itsm_task_147 where TASK_STATUS<>0 and TASK_STATUS<>3 ");//lihz so loves the snoke
		sql.append(" and TASK_CREATE_TIME <= to_date('" + endTime
				+ "','yyyy-MM-dd hh24:mi:ss') ");
		sql.append(" and TASK_CREATE_TIME >= to_date('" + beginTime
				+ "','yyyy-MM-dd hh24:mi:ss') ");
		sql.append(" order by TASK_CREATE_TIME desc");

		String sqlStr = sql.toString();
		List<TaskInfo> ret = new ArrayList<TaskInfo>();
		if (wfOid == -1) {// 取所有流程的
			// 取所有流程配置中，所有流程版本数据存放的表名

			List<WorkflowInfo> wfList = WorkflowManager.getRuningWorkflow(true);
			for (int i = 0; i < wfList.size(); i++) {
				WorkflowInfo wfInfo = wfList.get(i);
				if (wfInfo == null || !wfInfo.getOrigin().equals(ITSM)) {
					continue;
				}
				if (wfInfo.isLoadHisToCache()) {
					Map<String, TaskInfo> m = Cache.Workflow_TasksHis
							.get(wfInfo.getOid() + "");
					if (m != null) {
						Object[] mv = m.values().toArray();
						for (int mi = 0; mi < mv.length; mi++) {
							TaskInfo ti = (TaskInfo) mv[mi];
							if (ti.getCreateTime() >= bt
									&& ti.getCreateTime() <= et)
								ret.add(ti);
						}
					}
				} else {
					Object[] tabelNames = wfInfo.getDataTablesName().keySet()
							.toArray();
					for (int tni = 0; tni < tabelNames.length; tni++) {
						String dtName = (String) tabelNames[tni];
						ret.addAll(getTaskInfoBySQL(sqlStr.replaceFirst(
								"itsm_task_147", dtName)));
					}
				}
			}
		} else {
			WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
			if (wfInfo != null && wfInfo.getOrigin().equals(ITSM)) {
				if (wfInfo.isLoadHisToCache()) {
					Map<String, TaskInfo> m = Cache.Workflow_TasksHis
							.get(wfInfo.getOid() + "");
					if (m != null) {
						Object[] mv = m.values().toArray();
						for (int mi = 0; mi < mv.length; mi++) {
							TaskInfo ti = (TaskInfo) mv[mi];
							if (ti.getCreateTime() >= bt
									&& ti.getCreateTime() <= et)
								ret.add(ti);
						}
					}
				} else {
					Map<String, String> map_ = wfInfo.getDataTablesName();
					for (Iterator<String> ite = map_.keySet().iterator(); ite
							.hasNext();) {
						String dtName = (String) ite.next();
						ret.addAll(getTaskInfoBySQL(sql.toString()
								.replaceFirst("itsm_task_147", dtName)));
					}
				}
			}
		}
		return ret;
	}

	public List getTaskBranch(TaskInfo info) {
		List retList = new ArrayList();
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(info.getWfOid());
		WorkflowData wfData = wfInfo.getVersion(info.getWfVer());
		String sql = "select * from " + wfData.getDataTable()
				+ " where TASK_PARENT_OID=" + info.getOid();
		try {
			retList = getTaskInfoBySQL(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retList;
	}

	public List getLinkedTask(String linkOidStr) {
		List retList = new ArrayList();

		return retList;
	}
	
	/**
	 * get all wait task in workflow wfoid on the nodeid
	 * @param wfOid
	 * @param nodeId
	 * @return
	 * @author <a href="mailto:zhongaj@asiainfo-linkage.com">Forrest</a>
	 */
	public List<TaskData> getWaitTaskData(int wfOid, String nodeId){
		List<TaskData> l = new ArrayList<TaskData>();
		for(Map.Entry<String, TaskData> entry : Cache.WaitData.entrySet()){
			if (entry.getKey().startsWith(wfOid+"_"+nodeId)) {
				l.add(entry.getValue());
			}
		}
		return l;
	}
	
	/**
	 * get somebody all wait task in the wfoid workflow on nodeid
	 * @param wfOid
	 * @param nodeId
	 * @param userId
	 * @return
	 * @author <a href="mailto:zhongaj@asiainfo-linkage.com">Forrest</a>
	 */
	public List<TaskData> getWaitTaskData(int wfOid, String nodeId, String userId){
		List<TaskData> l = new ArrayList<TaskData>();
		for(Map.Entry<String, TaskData> entry : Cache.WaitData.entrySet()){
			if (entry.getKey().startsWith(wfOid+"_"+nodeId+"_"+userId)) {
				l.add(entry.getValue());
			}
		}
		return l;
	}
	
	/**
	 * get somebody all opent taskdata in wfoid on nodeid
	 * @param wfOid
	 * @param nodeId
	 * @param userId
	 * @return
	 * @author <a href="mailto:zhongaj@asiainfo-linkage.com">Forrest</a>
	 */
	public List<TaskData> getOpenNoWaitTaskData(int wfOid, String nodeId, String userId) {
		List<TaskData> l = getAllOpenedTasks(wfOid);
		for (Iterator<TaskData> iterator = l.iterator(); iterator.hasNext();) {
			TaskData taskData = iterator.next();
			if(!(!taskData.isWait() && taskData.getNodeId().equals(nodeId) && taskData.getAssignTo().equals(userId))) {
				iterator.remove();
			}
		}
		return l;
	}
	
	/**
	 * to decide if the task have to wait
	 * @param wfOid
	 * @param node
	 * @param userId
	 * @return
	 */
	public boolean checkWait(int wfOid, NodeInfo node, String userId) {
		String queue = node.getQueue();
		if (queue == null || queue.length() <= 0) {
			return false;
		}
		String num = "";
		int pos = queue.indexOf(userId);
		if (pos > -1) {
			int pos1 =  pos + userId.length() + 1;
			num = queue.substring(pos1, queue.indexOf(";",pos1));		
		}
		if (num.length() > 0 || num != null) {
			return Integer.valueOf(num) <= getOpenNoWaitTaskData(wfOid, node.getId(), userId).size();
		}
		return false;
	}
	
	public void setTaskFactory(TaskFactory taskFactory) {
		ITSMTaskManagerImpl.taskFactory = taskFactory;
	}

	public TaskFactory getTaskFactory() {
		return taskFactory;
	}

}

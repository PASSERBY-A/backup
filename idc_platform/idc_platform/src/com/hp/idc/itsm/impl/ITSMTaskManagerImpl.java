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
	 * ��ʼ������
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
	 * ��ȡ���ݿ��¼����ʽ����TaskInfo����
	 * 
	 * @param rs
	 * @param readHistory
	 *            �Ƿ��ȡclob�ֶ�
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
		// ���ݼ�¼̫��Ļ�����ȡclob̫��
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
	 * ���ع�������
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
	 * ���������ڲ�����
	 * 
	 * @param info
	 *            ��������
	 * @param workflow
	 *            ʹ������
	 * @param map
	 *            �ύ����
	 * @param fromData
	 *            ��Դ���̽ڵ�
	 * @param toNode
	 *            Ŀ�����̽ڵ�
	 * @param assignTo
	 *            �������û�
	 * @param operName
	 *            ������
	 * @param operType
	 *            ��������
	 * @return �������ɵĹ�������
	 * @throws Exception
	 */
	public TaskData updateTaskInternal(TaskInfo info, WorkflowData workflow,
			Map map, TaskData fromData, NodeInfo toNode, String toNodePath,
			String actionId, String assignTo, String operName, int operType)
			throws Exception {
		TaskData newData = null;
		TaskData newData2 = null;
		if (toNode == null)
			throw new Exception("�޷�ִ��Ŀ������");

		if (fromData == null) { // �½���������
			if (info.getRootData() != null)
				throw new Exception("�޷��ظ�������ʼ�ڵ�");

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
		} else { // ִ����������
			if (operType == TaskUpdateInfo.TYPE_SAVE_FOR_EDIT) {
				if (!fromData.isBranchBegin())
					fromData.removeChildEditing(toNodePath);

				// ����Ǹ��ڵ�ı���Ϊ�ݸ壬����Ѹ��ڵ�ı���Ϣ�����£������еط���ʾ���ԣ����ǵ�һ�ε���Ϣ��
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

		// ���Ͷ��Ÿ�֪ͨ��
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

			// ���Ŀ��ڵ�û�������ߣ���ر�
			// �����ǰ�ڵ������������ǡ������̽ڵ㡱����������̣��򵥵��ж�toNode.getActions().size() ==
			// 0���й����ر��ǲ��е�
			// ������£����뻹���ж��ǲ����ڡ������̽ڵ㡱������������taskData.getSubNodeId().equals("")��
			if (toNode.getActions().size() == 0
					&& newData.getSubNodeId().equals(""))
				newData.setStatus(TaskData.STATUS_CLOSE);

		}
		newData.setWait(checkWait(workflow.getWorkflowOid(), toNode, assignTo));
		return newData;
	}

	/**
	 * ��ָ���Ľڵ���ҷ�֧��ʼ�ڵ�
	 * 
	 * @param taskData
	 *            ָ���Ľڵ�
	 * @param workflow
	 *            ��������
	 * @return �ҵ��ķ�֧��ʼ�ڵ�
	 * @throws Exception
	 */
	public TaskData getBranchBegin(TaskData taskData, WorkflowData workflow)
			throws Exception {
		TaskData b = taskData;
		int adj = 1;
		for (;;) {
			b = b.getParent();
			if (b == null)
				throw new Exception("�Ҳ�����֧��ʼ��㣬������������");
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
	 * ����֧������㣬�ﵽ����ʱ���кϲ�
	 * 
	 * @param taskInfo
	 * @param bbtaskData
	 *            ��֧��ʼ�ڵ��taskData
	 * @param workflow
	 * @param operName
	 * @throws Exception
	 */
	public void checkBranchEnd(TaskInfo taskInfo, TaskData bbtaskData,
			WorkflowData workflow, String operName) throws Exception {
		// ȡ����֧����������һ�����
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
			if (d.isBranchEnd()){// �����ӷ�֧�Ѿ�������Ľڵ�				
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
			throw new Exception("�ϲ����ֻ����һ�����");
		}

		if (al.size() > 0) { // ����һ����㣬��ʼ�ϲ�
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
	 * ǿ�ƹرչ���
	 * 
	 * @param taskOid
	 * @param message
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int forceCloseTask(int wfOid, int taskOid, String message,
			String operName) throws Exception {
		TaskInfo taskInfo = null;
		taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null)
			throw new Exception("�������������");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("�޷�ǿ�ƹرչ���");
		if (!taskInfo.getCreatedBy().equals(operName))
			throw new Exception("��û��Ȩ��ǿ�ƹرձ�����");
		taskInfo.setStatus(TaskInfo.STATUS_FORCE_CLOSE);
		List<TaskData> l = taskInfo.getTaskData();
		for (int i = 0; i < l.size(); i++) {
			TaskData d = (TaskData) l.get(i);
			d.setStatus(TaskData.STATUS_CLOSE);
		}
		taskInfo.setForceCloseMessage(message);
		// �����ⲿ�ӿ�
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
	 * ǿ�ƹر����ݽڵ㣬��֧��ʼ�ڵ㣬�ɶ�������֧������
	 * 
	 * @param wfOid
	 *            ����OID
	 * @param taskOid
	 *            �����Ĺ���OID
	 * @param taskDataId
	 *            ��֧��ʼ�����ݽڵ�
	 * @param branchTaskDataId
	 *            �����ķ�֧���ݽڵ�
	 * @param message
	 *            ǿ�ƹر�ԭ��
	 * @param operName
	 *            ������
	 * @return
	 * @throws Exception
	 */
	public int forceCloseTaskData(int wfOid, int taskOid, int taskDataId,
			int branchTaskDataId, String message, String operName)
			throws Exception {
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null)
			throw new Exception("�������������");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("�޷����в����������ѹر�");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("�����������ϸ����");
		if (!taskData.canDealThisNode(operName))
			throw new Exception("��û��Ȩ�޲���������");

		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		NodeInfo node = workflow.getNode(taskData.getNodeId());
		if (node.getType() != NodeInfo.TYPE_BRANCH_BEGIN)
			throw new Exception("�޷����в������ڲ�����");

		TaskData branchTD = taskInfo.getTaskData(branchTaskDataId);
		if (branchTD == null)
			throw new Exception("�����������ϸ����");
		if (branchTD.getStatus() == TaskData.STATUS_CLOSE
				|| branchTD.getStatus() == TaskData.STATUS_FORCE_CLOSE)
			throw new Exception("Ŀ��ڵ��ѹرգ��޷����в���");

		TaskData bbData = getBranchBegin(branchTD, workflow);
		if (bbData.getDataId() != taskData.getDataId()) {
			throw new Exception("Ŀ��ڵ㲻���ڴ˷�֧�ɿط�Χ�ڣ��޷����в���");
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
	 * �Ƴ�ָ���ڵ��µ�ָ���ӽڵ�
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
			throw new Exception("�������������");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("�޷����в����������ѹر�");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("�����������ϸ����");
		if (!taskData.canDealThisNode(operName))
			throw new Exception("��û�в���Ȩ��");
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
	 * �رշ�֧��ʼ�ڵ㣬���ٽ��з���
	 * 
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int closeTaskData(int wfOid, int taskOid, int taskDataId,
			String operName) throws Exception {
		TaskInfo taskInfo = null;
		taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null)
			throw new Exception("�������������");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("�޷����в����������ѹر�");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("�����������ϸ����");
		if (!taskData.canDealThisNode(operName))
			throw new Exception("��û��Ȩ�޹رձ�����");

		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		NodeInfo node = workflow.getNode(taskData.getNodeId());
		if (node.getType() != NodeInfo.TYPE_BRANCH_BEGIN)
			throw new Exception("�޷����в������ڲ�����");
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
	 * ���¹�������
	 * 
	 * @param taskOid
	 *            ����ID���½�ʱΪ-1
	 * @param workflowOid
	 *            ����ID
	 * @param map
	 *            ǰ̨�������ı�MAP����
	 * @param dataId
	 *            ��ǰ���ݽڵ�ID���½�ʱΪ-1
	 * @param toNodeId
	 *            Ŀ�����̽ڵ�ID���½�ʱΪ��������������ڵ��µĵڶ����ڵ��ID
	 * @param actionId
	 *            ִ�еĶ���ID
	 * @param assignTo
	 *            ������һ��������
	 * @param operName
	 *            ������ǰ������
	 * @param operType
	 *            �������ͣ������TaskUpdateInfo.TYPE_SAVE����Ϊ��������²���ת��2008-03-28�¼ӣ�
	 * @return ���ز������(δʹ��)
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
				throw new Exception("�������������");
			workflow = wfInfo.getVersion(taskInfo.getWfVer());
			taskData = taskInfo.getTaskData(dataId);
			toNode = workflow.getNode(toNodeId);
			fromNode = workflow.getNode(taskData.getNodeId());
			if (taskInfo.getWfOid() != workflowOid)
				throw new Exception("�����������ȷ");
			if (taskData.getStatus() == TaskData.STATUS_CLOSE
					|| taskData.getStatus() == TaskData.STATUS_FORCE_CLOSE)
				throw new Exception("�벻Ҫ�ظ��ύ����");
			if (taskData.getStatus() == TaskData.STATUS_WAITING) {
				TaskWaitingInfo twInfo = taskData.getWaitInfo();
				throw new Exception("���ڵȴ������Ĺ���(" + twInfo.getWaiting_task_oid()
						+ ")�Ĵ������");
			}
			// 1�����ǵ�ǰ�����˲��ҵ�ǰ�ڵ㲻���ǹ�����ڵ㣬ûȨ��
			// 2����ǰ�ڵ�ʱ�ǹ���ڵ��Ҳ������Ͳ��Ǳ���ݸ壬ûȨ��
			if (!taskData.canDealThisNode(operName)
					&& (!fromNode.isShareDeal() || fromNode.isShareDeal()
							&& operType != TaskUpdateInfo.TYPE_SAVE_FOR_EDIT))
				throw new Exception("����Ȩ����������");
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
	 * ���¹�����ת���������޷�֧���ⲿϵͳ���ã�
	 * 
	 * @param taskOid
	 *            ����OID �½�ʱΪ-1
	 * @param workflowOid
	 *            ����OID
	 * @param map
	 *            �ύ������map����
	 * @param dataId
	 *            ���µ����ݽڵ㣬Ϊ-1ʱ��ȡ���µ����ݽڵ����
	 * @param assignTo
	 *            ��һ��ִ���ˣ�Ϊ""��ǰ������
	 * @param operName
	 *            ������
	 * @param operType
	 *            �������ͣ�������ת�����˵ȵȣ�
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
				throw new Exception("�������������");
			if (taskInfo.getWfOid() != workflowOid)
				throw new Exception("�����������ȷ");

			if (dataId != -1)
				taskData = taskInfo.getTaskData(dataId);
			else {
				// ȡ��һ���򿪵Ľڵ㣻
				List<TaskData> activeList = taskInfo.getTaskData();
				if (activeList != null && activeList.size() > 0)
					taskData = activeList.get(0);
				// taskData =
				// taskInfo.getTaskData(taskInfo.getLatestTaskDataId());
			}
			if (taskData == null)
				throw new Exception("�Ҳ�����Ӧ��������Ϣ");
			if (operType == TaskUpdateInfo.TYPE_ROLLBACK) {
				if (taskData.getParent() == null)
					throw new Exception("��ǰΪ�׽ڵ㣬�޷����˱�����");
				if (!taskData.canDealThisNode(operName))
					throw new Exception("����Ȩ���˱�����");
				if (taskData.isRollback())
					throw new Exception("�ѻ��ˣ��벻Ҫ�ظ��ύ");
				if (taskData.getStatus() == TaskData.STATUS_CLOSE)
					throw new Exception("�ѹر�״̬�޷�����");
				if (!taskData.isRollbackable())
					throw new Exception("��ǰ�ڵ㲻������ˣ���������������Ա��ϵ");
			}
			if (taskData.getStatus() == TaskData.STATUS_CLOSE)
				throw new Exception("�벻Ҫ�ظ��ύ����");
			if (taskData.getStatus() == TaskData.STATUS_WAITING) {
				TaskWaitingInfo twInfo = taskData.getWaitInfo();
				throw new Exception("���ڵȴ������Ĺ���(" + twInfo.getWaiting_task_oid()
						+ ")�Ĵ������");
			}

			if (!taskData.canDealThisNode(operName))
				throw new Exception("����Ȩ����������");

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
	 * ���¹������������¹�������ֶ���Ϣ�������й�������ת����
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
			throw new Exception("�������������");
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("�����ѹر�");

		int lastTaskDataId = taskInfo.getLatestTaskDataId();
		TaskData taskData = taskInfo.getTaskData(lastTaskDataId);
		if (taskData == null)
			throw new Exception("�Ҳ�������������ݽڵ�");

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
	 * ���˹���
	 * 
	 * @param taskOid
	 * @param taskDataId
	 * @param rollbackMessage
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int rollbackTask(int wfOid, int taskOid, int taskDataId,
			String rollbackMessage, String operName) throws Exception {
		TaskUpdateInfo updateInfo = new TaskUpdateInfo();
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null) {
			throw new Exception("�Ҳ�������");
		}
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		if (wfInfo == null)
			throw new Exception("�Ҳ���������Ϣ");
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		if (workflow == null)
			throw new Exception("�Ҳ������̰汾��Ϣ");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("�Ҳ�����Ӧ��������Ϣ");
		if (taskData.getParent() == null)
			throw new Exception("��ǰΪ�׽ڵ㣬�޷����˱�����");
		if (!taskData.canDealThisNode(operName))
			throw new Exception("����Ȩ���˱�����");
		if (taskData.isRollback())
			throw new Exception("�ѻ��ˣ��벻Ҫ�ظ��ύ");
		if (taskData.getStatus() == TaskData.STATUS_CLOSE)
			throw new Exception("�ѹر�״̬�޷�����");
		if (!taskData.isRollbackable())
			throw new Exception("��ǰ�ڵ㲻������ˣ���������������Ա��ϵ");
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
	 * �Թ���������
	 * 
	 * @param taskOid
	 * @param taskDataId
	 * @param message
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int addTaskMessage(int wfOid, int taskOid, int taskDataId,
			String message, String operName) throws Exception {
		TaskUpdateInfo updateInfo = new TaskUpdateInfo();
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null) {
			throw new Exception("�Ҳ�������");
		}
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		if (wfInfo == null)
			throw new Exception("�Ҳ���������Ϣ");
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		if (workflow == null)
			throw new Exception("�Ҳ������̰汾��Ϣ");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("�Ҳ�����Ӧ��������Ϣ");
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
	 * ���ܹ��������䵽�Լ���ģ�
	 * 
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int acceptTask(int wfOid, int taskOid, int taskDataId,
			String operName) throws Exception {
		TaskUpdateInfo updateInfo = new TaskUpdateInfo();
		TaskInfo taskInfo = getTaskInfoByOid(taskOid, wfOid, -1);
		if (taskInfo == null) {
			throw new Exception("�Ҳ�������");
		}
		if (taskInfo.getStatus() != TaskInfo.STATUS_OPEN)
			throw new Exception("�޷����в����������ѹر�");

		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		if (wfInfo == null)
			throw new Exception("�Ҳ���������Ϣ");
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		if (workflow == null)
			throw new Exception("�Ҳ������̰汾��Ϣ");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("�Ҳ�����Ӧ��������Ϣ");
		if (taskData.getStatus() != TaskData.STATUS_OPEN)
			throw new Exception("�޷����в����������Ѵ���");
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
	 * ������֪��Ϣ
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
			throw new Exception("�Ҳ�������");
		}
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo
				.getWfOid());
		if (wfInfo == null)
			throw new Exception("�Ҳ���������Ϣ");
		WorkflowData workflow = wfInfo.getVersion(taskInfo.getWfVer());
		if (workflow == null)
			throw new Exception("�Ҳ������̰汾��Ϣ");
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		if (taskData == null)
			throw new Exception("�Ҳ�����Ӧ��������Ϣ");
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
	 * ���¹���
	 * 
	 * @param updateInfo
	 * @return ���ز������(δʹ��)
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
			// ��������ӿ�
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

		if (taskInfo == null) { // �½�����
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
				// ���������ֶ�
				if (map.get("_sys_linkTaskStr") != null)
					taskInfo.setLinkedTaskStr((String) map
							.get("_sys_linkTaskStr"));

				// �������������Ĺ���ID
				if (map.get("_sys_thirdId") != null)
					taskInfo.setThirdID((String) map.get("_sys_thirdId"));

				TaskData td = updateTaskInternal(taskInfo, workflow, map,
						rootData, toNode, toNodePath, actionId,
						users[i].trim(), operName, updateInfo.getType());

				// ��֧�ּ�¼���
				if (rootData != null)
					td.setNewBranch(true);
				// ����Ŀ��ڵ��������̽ڵ�����
				doSubflowNode(td);
				if (rootData == null) {
					rootData = td.getParent();
				}
				updateInfo.setTaskData(td);
				if (updateInfo.getType() != TaskUpdateInfo.TYPE_SAVE_FOR_EDIT) {
					// ����ݸ��ʱ��˶�̬������ִ��
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
			// �����ʼ��֧
			List ac = taskData.getActivateChilds();
			if (ac.size() > 0) {
				newBranch = true;
			}
			
			// ���������ֶ�
			if (map.get("_sys_linkTaskStr") != null)
				taskInfo.setLinkedTaskStr((String) map
						.get("_sys_linkTaskStr"));
			// �������������Ĺ���ID
			if (map.get("_sys_thirdId") != null)
				taskInfo.setThirdID((String) map.get("_sys_thirdId"));
			
			TaskData td = updateTaskInternal(taskInfo, workflow, map, taskData,
					toNode, toNodePath, actionId, users[i].trim(), operName,
					updateInfo.getType());
			// ����Ŀ��ڵ��������̽ڵ�����
			doSubflowNode(td);

			// ����������̽ڵ㣬��ּ�¼���棬���浽��Ӧ���������ݱ���
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
			// �жϷ�֧����
			TaskData newData = taskInfo.getTaskData(taskInfo
					.getLatestTaskDataId());
			NodeInfo newNode = workflow.getNode(newData.getNodeId());
			if (newNode.getType() == NodeInfo.TYPE_BRANCH_END) {
				newData.setStatus(TaskData.STATUS_CLOSE);
				// ȡ��ʼ��֧�ڵ�
				TaskData b = getBranchBegin(newData, workflow);

				// ��֧���ر�ʱ�ſ������ϲ�
				// ����ǰ���ж�STATUS_CLOSE״̬����Ϊ�ж�STATUS_CLOSE_PRE
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
	 * ����ڵ��������̽ڵ㣬��ѵ�ǰ�ڵ���Ϣ��Ϊ��ʵ�Ľڵ���Ϣ��
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
	 * ͨ��oid���ҹ���
	 * 
	 * @param oid
	 * @return �ҵ��Ĺ���
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
	 * ���¹��������ݿ� ���¼�¼����֧����¼��xml����ͳһ��������¼�ϣ���֧��¼����������ûʵ������
	 * 
	 * @param incInfo
	 * @param operName
	 * @return ���ݿ��й�����oid
	 * @throws SQLException
	 * @throws DocumentException
	 * @throws IOException
	 */
	public int updateTaskInfo(TaskInfo incInfo, String operName)
			throws SQLException, IOException, DocumentException {
		return 0;
	}

	/**
	 * �ѽڵ����ݸ���������
	 * 
	 * @param taskData
	 * @param operName
	 * @throws SQLException
	 */
	public void updateTaskDataToDB(TaskData taskData, String operName)
			throws SQLException {

	}

	/**
	 * ɾ������
	 * 
	 * @param oid
	 *            ��¼ID
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
	 * ��ȡ�����������
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
	 * ��ȡ���е�
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

		// ȡ�������������У��������̰汾���ݴ�ŵı���
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
	 * ��ȡ���д򿪵Ĺ���
	 * 
	 * @return �������д򿪵Ĺ���List[TaskData]
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
	 * ��ȡ���д򿪵Ĺ���
	 * 
	 * @return �������д򿪵Ĺ���List[TaskInfo]
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
	 * ����sql��ѯ
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
	 * 2008-01-02 10:02:20�¼�
	 * 
	 * @param sql
	 *            ���Դ�:p������sql��
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
		if (wfOid == -1) {// ȡ�������̵�
			// ȡ�������������У��������̰汾���ݴ�ŵı���

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

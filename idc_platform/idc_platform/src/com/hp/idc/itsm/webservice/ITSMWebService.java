package com.hp.idc.itsm.webservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.idc.itsm.impl.ITSMTaskManagerImpl;
import com.hp.idc.itsm.security.PersonInfo;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskEvent;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.util.ConnectManager;
import com.hp.idc.json.JSONObject;

public class ITSMWebService {

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
	 * @param toNodePath
	 *            目标流程节点ID，新建时为流程配置里面根节点下的第二个节点的ID
	 * @param assignTo
	 *            工单下一步处理人
	 * @param operName
	 *            工单当前操作人
	 * @param operType
	 *            操作类型，如果＝TaskUpdateInfo.TYPE_SAVE，则为仅保存更新不流转
	 * @return
	 * @throws Exception
	 */
	public static int updateTask(int taskOid, int workflowOid,
			Map<String, String> map, int dataId, String toNodePath,
			String actionId, String assignTo, String operName, int operType)
			throws Exception {
		ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
		int ret = -1;
		try {
			ret = itmi.updateTask(taskOid, workflowOid, map, dataId,
					toNodePath, actionId, assignTo, operName, operType);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return ret;
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
	public static int updateTask(int taskOid, int workflowOid,
			Map<String, String> map, String operName) throws Exception {
		ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
		return itmi.updateTask(taskOid, workflowOid, map, operName);
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
	public static int updateTask(int taskOid, int workflowOid,
			Map<String, String> map, int dataId, String assignTo,
			String operName, int operType) throws Exception {
		ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
		return itmi.updateToNext(taskOid, workflowOid, map, dataId, assignTo,
				operName, operType);
	}

	public static boolean taskIsClosed(int taskOid, int wfOid) throws Exception {
		ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
		TaskInfo ti = itmi.getTaskInfoByOid(taskOid, wfOid, -1);
		if (ti.getStatus() == TaskInfo.STATUS_OPEN)
			return false;
		return true;
	}

	/**
	 * toptea告警前传使用，获取帮助台人员信息
	 * 
	 * @param hdId
	 *            帮助太工作组ID
	 * @return
	 */
	public static Map<String, String> getHelpDeskUser(String hdId) {
		Map<String, String> ret = new HashMap<String, String>();
		if (hdId == null || hdId.equals(""))
			return ret;
		List<PersonInfo> l = PersonManager.getPersonsByWorkgoupId("ITSM", hdId,
				false);
		if (l == null)
			return ret;
		for (PersonInfo p : l) {
			ret.put(p.getId(), p.getName());
		}
		return ret;
	}

	//
	// public static List<TaskInfo> getTaskListByWorkflowOid(int wfOid,
	// String beginTime, String endTime) throws Exception {
	// List<TaskInfo> ret = new ArrayList<TaskInfo>();
	// ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
	// ret.addAll(itmi.getAllOpenedTaskInfos(wfOid));
	// ret.addAll(itmi.getAllClosedTaskInfos(beginTime, endTime, wfOid));
	// return ret;
	// }
	//
	/**
	 * @param taskOid
	 *            工单OID 新建时为-1
	 * @param workflowOid
	 *            流程OID
	 * @param wfver
	 *            版本号, -1 is the last workflow
	 * @return
	 */
	public static String getTaskInfoByOid(int oid, int wfOid, int wfVer)
			throws Exception {
		ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
		TaskInfo ti = itmi.getTaskInfoByOid(oid, wfOid, wfVer);
		JSONObject jo = new JSONObject("{}");
		if (ti != null) {
			if (ti.getStatus() != TaskInfo.STATUS_OPEN) {
				jo.put("task_status", "已关闭");
				jo.put("task_dealUser", "");
				jo.put("task_dealUserId", "");
				jo.put("task_dataId", "");
			} else {
				TaskData td = ti.getTaskData(ti.getLatestTaskDataId());
				if (td != null) {
					jo.put("task_status", td.getNodeDesc());
					jo.put("task_dealUserId", td.getAssignTo());
					jo.put("task_dealUser", PersonManager.getPersonNameById(td
							.getAssignTo()));
					jo.put("task_dataId", td.getDataId());
				} else {
					jo.put("task_status", "");
					jo.put("task_dealUserId", "");
					jo.put("task_dealUser", "");
					jo.put("task_dataId", "");
				}
			}

		}
		return jo.toString();
	}

	/**
	 * 更新变更管理中, 工单关闭后的"已关闭且上发"字段为"是"
	 * 
	 * @param taskOid
	 * @param workflowOid
	 * @param operName
	 * @return
	 * @throws Exception
	 */
	public static int updateIsUploadAndClose(int taskOid, int workflowOid,
			Map<String, String> m, String operName) throws Exception {
		ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
		TaskInfo taskInfo = itmi.getTaskInfoByOid(taskOid, workflowOid, -1);

		int lastTaskDataId = taskInfo.getLatestTaskDataId();
		TaskData taskData = taskInfo.getTaskData(lastTaskDataId);

		String sql = null;
		int ret = 0;
		Set<Map.Entry<String, String>> entry = m.entrySet();
		for (Iterator<Map.Entry<String, String>> iterator = entry.iterator(); iterator
				.hasNext();) {
			Map.Entry<String, String> entry2 = iterator.next();
			taskData.setValue(entry2.getKey(), entry2.getValue());
			sql = "update itsm_task_" + workflowOid + " set fld_"
					+ entry2.getKey() + "='" + entry2.getValue()
					+ "' where task_oid=" + taskOid;
		}

		TaskEvent event = new TaskEvent("ITSM", taskInfo, lastTaskDataId,
				false, operName, TaskUpdateInfo.TYPE_UPDATE_FIELD);
		new ITSMTaskManagerImpl().getTaskFactory().publishEvent(event);

		Connection con = ConnectManager.getConnection();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sql);
			ret = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ps.close();
			con.close();
		}
		return ret;
	}
}

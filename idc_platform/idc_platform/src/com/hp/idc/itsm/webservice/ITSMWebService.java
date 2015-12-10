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
	 * @param toNodePath
	 *            Ŀ�����̽ڵ�ID���½�ʱΪ��������������ڵ��µĵڶ����ڵ��ID
	 * @param assignTo
	 *            ������һ��������
	 * @param operName
	 *            ������ǰ������
	 * @param operType
	 *            �������ͣ������TaskUpdateInfo.TYPE_SAVE����Ϊ��������²���ת
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
	 * ���¹������������¹�������ֶ���Ϣ�������й�������ת����
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
	 * toptea�澯ǰ��ʹ�ã���ȡ����̨��Ա��Ϣ
	 * 
	 * @param hdId
	 *            ����̫������ID
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
	 *            ����OID �½�ʱΪ-1
	 * @param workflowOid
	 *            ����OID
	 * @param wfver
	 *            �汾��, -1 is the last workflow
	 * @return
	 */
	public static String getTaskInfoByOid(int oid, int wfOid, int wfVer)
			throws Exception {
		ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
		TaskInfo ti = itmi.getTaskInfoByOid(oid, wfOid, wfVer);
		JSONObject jo = new JSONObject("{}");
		if (ti != null) {
			if (ti.getStatus() != TaskInfo.STATUS_OPEN) {
				jo.put("task_status", "�ѹر�");
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
	 * ���±��������, �����رպ��"�ѹر����Ϸ�"�ֶ�Ϊ"��"
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

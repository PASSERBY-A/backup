package com.hp.idc.itsm.task;

import java.io.IOException;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.dom4j.DocumentException;

import com.hp.idc.itsm.common.OperationResult;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FieldManager;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dbo.ResultSetOperation;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.workflow.ColumnFieldInfo;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

public class FormatDBTable {

	/**
	 * 根据流程格式化具体的数据存储表结构
	 * @throws SQLException
	 */
	public static void formatWFTable() throws SQLException{
		List wf = WorkflowManager.getWorkflows(true);
		for (int i = 0; i < wf.size(); i++) {
			WorkflowInfo wfInfo = (WorkflowInfo)wf.get(i);
			Map structure = new HashMap();
			structure.put("SYS_TABLE_NAME", "ITSM_TASK_"+wfInfo.getOid());
			List cols = wfInfo.getColumnFieldsList();
			for (int colIndex = 0; colIndex < cols.size(); colIndex++) {
				ColumnFieldInfo cfInfo = (ColumnFieldInfo)cols.get(colIndex);
				structure.put("SYS_COL_"+cfInfo.getColumnName(), "varchar2(1024)");
			}
			BaseTableStructure.updateTableStructure(structure);
		}
	}
	
	public static void formatWFTable(int wfOid) throws Exception{
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
		if(wfInfo==null) {
			throw new Exception("找不到流程（"+wfOid+"）");
		}
		Map structure = new HashMap();
		structure.put("SYS_TABLE_NAME", "ITSM_TASK_"+wfInfo.getOid());
		List cols = wfInfo.getColumnFieldsList();
		for (int colIndex = 0; colIndex < cols.size(); colIndex++) {
			ColumnFieldInfo cfInfo = (ColumnFieldInfo)cols.get(colIndex);
			structure.put("SYS_COL_"+cfInfo.getColumnName(), "varchar2(1024)");
		}
		BaseTableStructure.updateTableStructure(structure);
	}
	
	public static void formatWFData() throws Exception{
		String sql = "select * from itsm_task";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		//u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		List taskList = new ArrayList();
		try {
			//Object[] wf = Cache.Workflows.keySet().toArray();
			//for (int i = 0; i < )
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				try{
					TaskInfo taskInfo = new TaskInfo();
					taskInfo.setOid(rs.getInt("TASK_OID"));
					taskInfo.setCreateTime(rs.getTimestamp("TASK_CREATED").getTime());
					taskInfo.setCreatedBy(rs.getString("TASK_CREATEDBY"));
					taskInfo.setWfOid(rs.getInt("TASK_WF_OID"));
					taskInfo.setWfVer(rs.getInt("TASK_WF_VER"));
					taskInfo.setRelations(rs.getString("TASK_RELATIONS"));
					taskInfo.setUser(rs.getString("TASK_USERS"));
					taskInfo.setStatus(rs.getInt("TASK_STATUS"));
					taskInfo.setLastUpdate(rs.getTimestamp("TASK_LASTUPDATE").getTime());
					Clob clob = rs.getClob("TASK_DATA");
					taskInfo.setXmlData(ResultSetOperation.clobToString(clob));
					taskList.add(taskInfo);
				}catch (Exception ex){
					ex.printStackTrace();
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Map wfMap = new HashMap();
		for (int i = 0; i < taskList.size(); i++) {
			TaskInfo ti = (TaskInfo)taskList.get(i);
			try {
				System.out.println("format task data......"+ti.getOid());
				ti.parse();
				WorkflowInfo wfInfo = null;
				if (wfMap.get(ti.getWfOid()+"") == null) {
					wfInfo = getWorkflowByOid(ti.getWfOid());//(WorkflowInfo)Cache.Workflows.get(new Integer(ti.getOid()));
					wfMap.put(ti.getWfOid()+"", wfInfo);
					formatWFTable(ti.getWfOid());
				} else
					wfInfo = (WorkflowInfo)wfMap.get(ti.getWfOid()+"");
				
				List tdList = getAllLeafTaskData(ti.getRootData());
				TaskData lastData = null;
				for (int tdIndex = 0; tdIndex < tdList.size(); tdIndex++) {
					TaskData td = (TaskData)tdList.get(tdIndex);

					//把第一个最后存在主记录里面，（因为涉及到所有打开的节点的属性改变）
					if (lastData == null && (td.getStatus()==TaskData.STATUS_OPEN || tdIndex == tdList.size()-1)) {
						lastData = td;
						continue;
					}
					int oid = ItsmUtil.getSequence("task");
					td.setActually_task_oid(oid);
					insertDataToNewTable(td,wfInfo);
				}
				lastData.setActually_task_oid(ti.getOid());
				insertDataToNewTable(lastData,wfInfo);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("format end.");
	}
	
	private static  List getAllLeafTaskData(TaskData root) {
		List retList = new ArrayList();
		Stack s = new Stack();
		s.push(root);
		while (s.size() > 0) {
			TaskData t = (TaskData)s.pop();
			List l = t.getChilds();
			if (l == null || l.size() == 0) {
				retList.add(t);
			} else {
				boolean hasOne = false;
				for (int i = 0; i < l.size(); i++){
					TaskData td = (TaskData)l.get(i);
					if (td.isRollback())
						continue;
					s.push(td);
					hasOne = true;
				}
				if (!hasOne)
					retList.add(t);
			}
		}
		return retList;
	}
	
	private static void  insertDataToNewTable(TaskData td,WorkflowInfo wfInfo) throws SQLException{
		TaskInfo incInfo = td.getOwner();
		OperationResult ret = new OperationResult();
		OracleOperation u = new OracleOperation("itsm_task_"+incInfo.getWfOid(), "system");
		//u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		int oid = td.getActually_task_oid();
		ResultSet rs = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			u.setColumnData("TASK_OID", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(oid)));
			if (oid == incInfo.getOid()) {
				u.setColumnData("TASK_PARENT_OID", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(-1)));
			} else {
				u.setColumnData("TASK_PARENT_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(incInfo.getOid())));
			}
			u.setColumnData("TASK_CREATE_BY", new ColumnData(
					ColumnData.TYPE_STRING, incInfo.getCreatedBy()));
			u.setColumnData("TASK_CREATE_TIME", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(new Date(incInfo.getCreateTime()))));
			
			u.setColumnData("TASK_WF_OID", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(incInfo.getWfOid())));
			u.setColumnData("TASK_WF_VER", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(incInfo.getWfVer())));
			
			
			u.setColumnData("TASK_RELATIONS", new ColumnData(
					ColumnData.TYPE_STRING, incInfo.getRelations()));
			//更新分支记录的时候，在主记录保存xml数据的时候，不更新此字段
			u.setColumnData("TASK_USER", new ColumnData(
					ColumnData.TYPE_STRING, td.getAssignTo()));
			
			incInfo.setLastUpdate(System.currentTimeMillis());
			u.setColumnData("TASK_UPDATE_TIME", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(new Date(System.currentTimeMillis()))));
			u.setColumnData("TASK_UPDATE_BY", new ColumnData(
					ColumnData.TYPE_STRING, "system"));
			u.setColumnData("TASK_NODE_ID", new ColumnData(
					ColumnData.TYPE_STRING, ""+td.getNodeId()));
			u.setColumnData("TASK_STATUS", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(td.getStatus())));
			if (oid == incInfo.getOid()) {
				u.setColumnData("TASK_LINKED", new ColumnData(
						ColumnData.TYPE_STRING, incInfo.getLinkedTaskStr()));
				u.setColumnData("TASK_HISTORY", new ColumnData(
						ColumnData.TYPE_CLOB, incInfo.getXmlData()));
			} else {
				u.setColumnData("TASK_HISTORY", new ColumnData(
						ColumnData.TYPE_CLOB, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><workflow/>"));
			}
			List column = wfInfo.getColumnFieldsList();
			for (int i = 0; i < column.size(); i++) {
				ColumnFieldInfo cinfo = (ColumnFieldInfo)column.get(i);
				String value = (String)td.getAttribute(cinfo.getFieldName());
				FieldInfo fieldInfo = FieldManager.getFieldById("ITSM",cinfo.getFieldName());
				if(fieldInfo!=null) {
					value = fieldInfo.getHtmlCode(value==null?"":value);
				}
				if (value!=null && !value.equals("")) {
					byte[] valueB = value.getBytes();
					if (valueB.length>512) {
						byte[] tempB_ = new byte[512];
						for (int bi = 0; bi<512; bi++)
							tempB_[bi] = valueB[bi];
						value = new String(tempB_);
					}
					u.setColumnData(cinfo.getColumnName(), new ColumnData(cinfo.getType(), value));
				}
			}
			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			ret.setSuccess(false);
			ret.setMessage(e.toString());
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	private static WorkflowInfo getWorkflowByOid(int oid) throws SQLException{
		WorkflowInfo wfInfo = new WorkflowInfo();
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from ITSM_CFG_WORKFLOW where WF_OID="+oid;
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				try {
					wfInfo.parse(rs);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return wfInfo;
	}
}

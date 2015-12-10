/**
 * 
 */
package com.hp.idc.itsm.task.listener;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.DocumentException;

import com.hp.idc.itsm.common.OperationResult;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FieldManager;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.message.MessageManager;
import com.hp.idc.itsm.task.Event;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskEvent;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.workflow.ColumnFieldInfo;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

/**
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class DBListener extends TaskListener{

	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.task.TaskListener#accept(com.hp.idc.itsm.task.TaskInfo, int, boolean, int)
	 */
	@Override
	public boolean accept(Event event) {
		if (event instanceof TaskEvent) {
			
//			TaskEvent e = (TaskEvent)event;
//			int type = e.getOperType();
//			if (type == TaskUpdateInfo.TYPE_MESSAGE)
//				return false;
//			else
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.task.TaskListener#output(com.hp.idc.itsm.task.TaskInfo, int, boolean, int)
	 */
	@Override
	public void handleEvent	(Event event) throws Exception{
		TaskEvent event_ = (TaskEvent)event;
		try {
			TaskInfo ti = event_.getTaskInfo();
			String operUser = event_.getOperUser();
			boolean updateToDB = true;
			if (event_.getOperType() == TaskUpdateInfo.TYPE_SAVE_FOR_EDIT)
				updateToDB = false;
			//先把主记录入库
			if (updateToDB) {
				ti.setUser(operUser);
				updateTaskInfo(ti, operUser);
			} else {
				taskFactory.updateCache(ti);
			}
			
			List<TaskData> tdList = ti.getNewAdded();
			TaskData firstTD = null;
			if (tdList.size() == 0) {
				firstTD = ti.getTaskData(ti.getLatestTaskDataId());
			} else  {
				logger.debug("开始处理工单："+ti.getOid()+"\n事件来源："
						+event.getSource()+"\n变化数据节点个数："+tdList.size());
				for (int i = 0; i < tdList.size(); i++) {
					TaskData td = tdList.get(i);
					logger.debug("数据节点是否分支："+td.isNewBranch());
					if (td.isEditing()){
						continue;
					}
	
					///多分支,分记录存放
					if (td.isNewBranch() && updateToDB){
						TaskInfo cloneInfo = ti.cloneInfo();
						cloneInfo.setOid(-1);
						cloneInfo.setTaskNodeId(td.getNodeId());
						cloneInfo.setParentOid(ti.getOid());
						cloneInfo.setRelations(td.getAssignTo());
						cloneInfo.setUser(td.getAssignTo());
						TaskData tempTD_ = cloneInfo.getTaskData(td.getDataId());
						cloneInfo.setXmlData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><workflow/>");
						if(tempTD_.getActually_task_oid() == -1) {
							cloneInfo.setNewTaskInfo(true);
							int tOid_ = updateTaskInfo(cloneInfo, operUser);
							td.setActually_task_oid(tOid_);
							tempTD_.setActually_task_oid(tOid_);
						}
						updateTaskDataToDB(tempTD_,operUser);
					} else {
						firstTD = td;
						ti.setTaskNodeId(td.getNodeId());
						ti.setUser(td.getAssignTo());
					}
					td.setNewAdded(false);
				}
			}

			if (firstTD!=null && !firstTD.isEditing() && updateToDB){
				updateTaskDataToDB(firstTD,operUser);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	/**
	 * 更新工单至数据库
	 * 更新记录：分支另开记录，xml数据统一存在主记录上，分支记录仅供检索，没实际意义
	 * @param incInfo
	 * @param operName
	 * @return 数据库中工单的oid
	 * @throws SQLException
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public int updateTaskInfo(TaskInfo incInfo, String operName) throws SQLException, IOException, DocumentException {
		int oid = incInfo.getOid();
		ResultSet rs = null;
		if (oid == -1)
			oid = ItsmUtil.getSequence("task");
		
		OperationResult ret = new OperationResult();
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(incInfo.getWfOid());
		WorkflowData wfData = wfInfo.getVersion(incInfo.getWfVer());
		
		OracleOperation u = new OracleOperation(wfData.getDataTable(), operName);
		//u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		boolean isNew = createNewRecord(wfData.getDataTable(),oid);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			if (isNew) {
				incInfo.setOid(oid);
				u.setColumnData("TASK_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(oid)));
				u.setColumnData("TASK_PARENT_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(incInfo.getParentOid())));
				u.setColumnData("TASK_CREATE_BY", new ColumnData(
						ColumnData.TYPE_STRING, incInfo.getCreatedBy()));
				long createTime = System.currentTimeMillis();
				incInfo.setCreateTime(createTime);
				u.setColumnData("TASK_CREATE_TIME", new ColumnData(
						ColumnData.TYPE_DATETIME, sdf.format(new Date(createTime))));
				u.setColumnData("TASK_WF_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(incInfo.getWfOid())));
				u.setColumnData("TASK_WF_VER", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(incInfo.getWfVer())));
				u.setColumnData("TASK_PWF_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(incInfo.getPwfOid())));
				u.setColumnData("TASK_PWF_VER", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(incInfo.getPwfVer())));
			}
			
			u.setColumnData("TASK_RELATIONS", new ColumnData(
					ColumnData.TYPE_STRING, incInfo.getRelations()));
			//更新分支记录的时候，在主记录保存xml数据的时候，不更新此字段
			
			u.setColumnData("TASK_USER", new ColumnData(
					ColumnData.TYPE_STRING, incInfo.getUser()));
			
			
			incInfo.setLastUpdate(System.currentTimeMillis());
			u.setColumnData("TASK_UPDATE_TIME", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(new Date(incInfo.getLastUpdate()))));
			u.setColumnData("TASK_UPDATE_BY", new ColumnData(
					ColumnData.TYPE_STRING, operName));
			
			u.setColumnData("TASK_LINKED", new ColumnData(
					ColumnData.TYPE_STRING, incInfo.getLinkedTaskStr()));
			
			/*工单关闭后，增加发送短信的规则*/
			int status = incInfo.getStatus();
			if (status == TaskInfo.STATUS_CLOSE){
				MessageManager.sendSms("您创建的工单(ID:"+incInfo.getOid()+",标题:"+incInfo.getValue("title")+")已处理完毕。",
						incInfo.getCreatedBy(), "system", new Date(), "system");
			}
			u.setColumnData("TASK_STATUS", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(status)));
			
			u.setColumnData("TASK_HISTORY", new ColumnData(
					ColumnData.TYPE_CLOB, incInfo.getXmlData()));
			
			if (isNew) {
				rs = u.getResultSet(null);
				u.executeInsert(rs);
			}
			else {
				PreparedStatement ps  = u.getStatement("task_oid=?");
				ps.setInt(1, incInfo.getOid());
				rs = ps.executeQuery();
				u.executeUpdate(rs);
			}
		} catch (SQLException e) {
			ret.setSuccess(false);
			ret.setMessage(e.toString());
			throw e;
		} finally {
			u.commit(rs);
		}
		//更新缓存 && incInfo.getParentOid()==-1
		if (ret.isSuccess()) {
			taskFactory.updateCache(incInfo);
		}
		return oid;
	}
	
	/**
	 * 把节点数据更新至表列
	 * @param taskData
	 * @param operName
	 * @throws SQLException
	 */
	public void updateTaskDataToDB(TaskData taskData,String operName) throws SQLException{
		int actTaskOid = -1;
		if (taskData != null) {
			actTaskOid = taskData.getActually_task_oid();
		}
		if (actTaskOid == -1) {
			actTaskOid = taskData.getOwner().getOid();
		}
		
		TaskInfo saveInfo = taskData.getOwner();
		
		//配置的字段与数据库字段对应的列表
		int wfOid = saveInfo.getWfOid();
		int wfVer = saveInfo.getWfVer();
		if (taskData.getActuallyWorkflowOid()!=-1)
			wfOid = taskData.getActuallyWorkflowOid();
		if (taskData.getActuallyWorkflowVer()!=-1)
			wfVer = taskData.getActuallyWorkflowVer();
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
		WorkflowData wfData = wfInfo.getVersion(wfVer);
		NodeInfo node = wfData.getNode(taskData.getNodeId());
		int status = TaskInfo.STATUS_OPEN;
		if (node.getType() == NodeInfo.TYPE_BRANCH_END) {
			status = TaskInfo.STATUS_CLOSE;
		}
		
		List<ColumnFieldInfo> column = wfInfo.getColumnFieldsList();
		
		OperationResult ret = new OperationResult();
		ResultSet rs = null;
		OracleOperation u = new OracleOperation(wfData.getDataTable(), operName);
		//u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			
			u.setColumnData("TASK_RELATIONS", new ColumnData(
					ColumnData.TYPE_STRING, saveInfo.getRelations()));
			u.setColumnData("TASK_USER", new ColumnData(
					ColumnData.TYPE_STRING, taskData.getAssignTo()));
			
			saveInfo.setLastUpdate(System.currentTimeMillis());
			u.setColumnData("TASK_UPDATE_TIME", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(new Date(saveInfo.getLastUpdate()))));
			u.setColumnData("TASK_UPDATE_BY", new ColumnData(
					ColumnData.TYPE_STRING, operName));
			if (actTaskOid!=saveInfo.getOid()) {
				u.setColumnData("TASK_STATUS", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(status)));
			}
			
			u.setColumnData("TASK_NODE_ID", new ColumnData(
					ColumnData.TYPE_STRING, ""+taskData.getNodeId()));
			for (int i = 0; i < column.size(); i++) {
				ColumnFieldInfo cinfo = (ColumnFieldInfo)column.get(i);
				String value = taskData.getAttribute(cinfo.getFieldName());
				if (value!=null && !value.equals("")) {
					FieldInfo fieldInfo = FieldManager.getFieldById("ITSM",cinfo.getFieldName(),false);
					if(fieldInfo!=null) {
							value = fieldInfo.getRestoreCode(value==null?"":value);
					}
					byte[] valueB = value.getBytes();
					if (valueB.length>1000) {
						byte[] tempB_ = new byte[1000];
						System.arraycopy(valueB, 0, tempB_, 0, 1000);
						value = new String(tempB_);
					}
					u.setColumnData(cinfo.getColumnName(), new ColumnData(cinfo.getType(), value));
					saveInfo.addValue(cinfo.getFieldName(), value);
				}
			}

			PreparedStatement ps  = u.getStatement("task_oid=?");
			ps.setInt(1, actTaskOid);
			rs = ps.executeQuery();
			u.executeUpdate(rs);

		} catch (SQLException e) {
			ret.setSuccess(false);
			ret.setMessage(e.toString());
			//throw e;
		} finally {
			u.commit(rs);
		}
		
		//更新缓存
		//if (ret.isSuccess() && saveInfo.getParentOid()==-1) {
		//	updateCache(saveInfo);
		//}
	}
	
	public boolean createNewRecord(String dataTable, int taskOid) throws SQLException{
		String sql = "select * from " + dataTable + " where TASK_OID=?";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		boolean ret = true;
		try {
			ps = u.getSelectStatement(sql);
			ps.setInt(1, taskOid);
			rs = ps.executeQuery();
			if (rs.next()) {
				ret = false;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return ret;
	}
	

}

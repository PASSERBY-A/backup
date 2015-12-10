package com.hp.idc.itsm.inter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;

import com.hp.idc.itsm.task.TaskInfo;

public interface TaskManagerInterface {
	
	/**
	 * 强制关闭工单
	 * @param taskOid
	 * @param message
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int forceCloseTask(
			int wfOid,int taskOid, String message, String operName) throws Exception;
	
	/**
	 * 关闭分支开始节点，不再进行分配
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int closeTaskData(int wfOid,int taskOid, int taskDataId, String operName)  throws Exception;
	
	/**
	 * 更新工单数据
	 * @param taskOid
	 * @param workflowOid
	 * @param map
	 * @param dataId
	 * @param toNodeId
	 * @param assignTo
	 * @param operName
	 * @param operType 操作类型 如果＝TaskUpdateInfo.TYPE_SAVE，则为仅保存更新不流转（2008-03-28新加）
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int updateTask(
			int taskOid, int workflowOid, Map map, int dataId, 
			String toNodeId,String actionId, String assignTo, String operName,int operType) throws Exception;


	/**
	 * 回退工单
	 * @param taskOid
	 * @param taskDataId
	 * @param rollbackMessage
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int rollbackTask(int wfOid,int taskOid, int taskDataId, 
			String rollbackMessage, String operName)
		throws Exception;

	/**
	 * 对工单添加意见
	 * @param taskOid
	 * @param taskDataId
	 * @param message
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int addTaskMessage(int wfOid,int taskOid, int taskDataId, 
			String message, String operName)
		throws Exception;

	/**
	 * 接受工单（分配到自己组的）
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public int acceptTask(int wfOid,int taskOid, int taskDataId, 
			String operName)
		throws Exception;


	/**
	 * 获取参与的任务
	 * @param operName
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getTaskInfosByUser(String operName) throws IOException, DocumentException, ParseException, SQLException;

	/**
	 * 通过oid查找工单
	 * @param oid
	 * @param wfOid 流程id
	 * @return 找到的工单
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParseException 
	 * @throws DocumentException 
	 * @deprecated
	 */
	public TaskInfo getTaskInfoByOid(int oid,int wfOid) throws SQLException,IOException, DocumentException, ParseException;
	public TaskInfo getTaskInfoByOid(int oid,int wfOid,int wfVer) throws SQLException,IOException, DocumentException, ParseException;
	
	/**
	 * 更新工单至数据库
	 * @param incInfo
	 * @param operName
	 * @return 数据库中工单的oid
	 * @throws SQLException
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public int updateTaskInfo(TaskInfo incInfo, String operName) throws SQLException, IOException, DocumentException;
	
	/**
	 * 删除工单
	 * @param oid 记录ID
	 * @param operName
	 * @throws SQLException
	 * @deprecated
	 */
	public void deleteTaskInfo(int wfOid,int oid, String operName) throws SQLException;
	public void deleteTaskInfo(int wfOid,int wfVer,int oid, String operName) throws SQLException;

	/**
	 * 获取参与过的任务
	 * @param user
	 * @return
	 */
	public List getHistoryTaskInfoByUser(String operName) throws IOException, DocumentException, ParseException, SQLException;
		
	
	/**
	 * 获取所有的
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getAllTaskInfo() throws IOException, DocumentException, ParseException, SQLException;

	/**
	 * 获取所有状态的工单
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getAllTaskInfo(String beginTime,String endTime) throws IOException, DocumentException, ParseException, SQLException;

	/**
	 * 获取所有打开的工单
	 * @return 返回所有打开的工单List[TaskData]
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getAllOpenedTasks(int wfOid);

	/**
	 * 获取所有打开的工单
	 * @return 返回所有打开的工单List[TaskInfo]
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getAllOpenedTaskInfos(int wfOid);
	
	/**
	 * 获取所有关闭的工单
	 * @return 返回所有关闭的工单List[TaskInfo]
	 * @throws ParseException 
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws SQLException
	 */
	public List getAllClosedTaskInfos(String beginTime,String endTime,int wfOid) throws IOException, DocumentException, ParseException, SQLException;

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
	public List getTaskInfoBySQL(String sql) throws IOException, DocumentException, ParseException, SQLException;
	
	/**
	 * 2008-01-02 10:02:20新加
	 * @param sql 可以带:p参数的sql串
	 * @param request
	 * @return
	 * @throws SQLException 
	 * 
	 */
	public List getTaskInfoBySQL(String user,String password,String url,String sql,HttpServletRequest request);

	/**
	 * 获取主记录的分支记录
	 * @param info
	 * @return
	 */
	public List getTaskBranch(TaskInfo info);
	
	/**
	 * 获取关联的工单
	 * @param oid
	 * @return
	 */
	public List getLinkedTask(String linkOidStr);
}

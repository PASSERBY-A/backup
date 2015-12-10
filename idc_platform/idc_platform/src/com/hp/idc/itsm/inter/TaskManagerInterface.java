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
	 * ǿ�ƹرչ���
	 * @param taskOid
	 * @param message
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int forceCloseTask(
			int wfOid,int taskOid, String message, String operName) throws Exception;
	
	/**
	 * �رշ�֧��ʼ�ڵ㣬���ٽ��з���
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int closeTaskData(int wfOid,int taskOid, int taskDataId, String operName)  throws Exception;
	
	/**
	 * ���¹�������
	 * @param taskOid
	 * @param workflowOid
	 * @param map
	 * @param dataId
	 * @param toNodeId
	 * @param assignTo
	 * @param operName
	 * @param operType �������� �����TaskUpdateInfo.TYPE_SAVE����Ϊ��������²���ת��2008-03-28�¼ӣ�
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int updateTask(
			int taskOid, int workflowOid, Map map, int dataId, 
			String toNodeId,String actionId, String assignTo, String operName,int operType) throws Exception;


	/**
	 * ���˹���
	 * @param taskOid
	 * @param taskDataId
	 * @param rollbackMessage
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int rollbackTask(int wfOid,int taskOid, int taskDataId, 
			String rollbackMessage, String operName)
		throws Exception;

	/**
	 * �Թ���������
	 * @param taskOid
	 * @param taskDataId
	 * @param message
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int addTaskMessage(int wfOid,int taskOid, int taskDataId, 
			String message, String operName)
		throws Exception;

	/**
	 * ���ܹ��������䵽�Լ���ģ�
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public int acceptTask(int wfOid,int taskOid, int taskDataId, 
			String operName)
		throws Exception;


	/**
	 * ��ȡ���������
	 * @param operName
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getTaskInfosByUser(String operName) throws IOException, DocumentException, ParseException, SQLException;

	/**
	 * ͨ��oid���ҹ���
	 * @param oid
	 * @param wfOid ����id
	 * @return �ҵ��Ĺ���
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParseException 
	 * @throws DocumentException 
	 * @deprecated
	 */
	public TaskInfo getTaskInfoByOid(int oid,int wfOid) throws SQLException,IOException, DocumentException, ParseException;
	public TaskInfo getTaskInfoByOid(int oid,int wfOid,int wfVer) throws SQLException,IOException, DocumentException, ParseException;
	
	/**
	 * ���¹��������ݿ�
	 * @param incInfo
	 * @param operName
	 * @return ���ݿ��й�����oid
	 * @throws SQLException
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public int updateTaskInfo(TaskInfo incInfo, String operName) throws SQLException, IOException, DocumentException;
	
	/**
	 * ɾ������
	 * @param oid ��¼ID
	 * @param operName
	 * @throws SQLException
	 * @deprecated
	 */
	public void deleteTaskInfo(int wfOid,int oid, String operName) throws SQLException;
	public void deleteTaskInfo(int wfOid,int wfVer,int oid, String operName) throws SQLException;

	/**
	 * ��ȡ�����������
	 * @param user
	 * @return
	 */
	public List getHistoryTaskInfoByUser(String operName) throws IOException, DocumentException, ParseException, SQLException;
		
	
	/**
	 * ��ȡ���е�
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getAllTaskInfo() throws IOException, DocumentException, ParseException, SQLException;

	/**
	 * ��ȡ����״̬�Ĺ���
	 * @param beginTime ��ʼʱ��
	 * @param endTime ����ʱ��
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getAllTaskInfo(String beginTime,String endTime) throws IOException, DocumentException, ParseException, SQLException;

	/**
	 * ��ȡ���д򿪵Ĺ���
	 * @return �������д򿪵Ĺ���List[TaskData]
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getAllOpenedTasks(int wfOid);

	/**
	 * ��ȡ���д򿪵Ĺ���
	 * @return �������д򿪵Ĺ���List[TaskInfo]
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public List getAllOpenedTaskInfos(int wfOid);
	
	/**
	 * ��ȡ���йرյĹ���
	 * @return �������йرյĹ���List[TaskInfo]
	 * @throws ParseException 
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws SQLException
	 */
	public List getAllClosedTaskInfos(String beginTime,String endTime,int wfOid) throws IOException, DocumentException, ParseException, SQLException;

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
	public List getTaskInfoBySQL(String sql) throws IOException, DocumentException, ParseException, SQLException;
	
	/**
	 * 2008-01-02 10:02:20�¼�
	 * @param sql ���Դ�:p������sql��
	 * @param request
	 * @return
	 * @throws SQLException 
	 * 
	 */
	public List getTaskInfoBySQL(String user,String password,String url,String sql,HttpServletRequest request);

	/**
	 * ��ȡ����¼�ķ�֧��¼
	 * @param info
	 * @return
	 */
	public List getTaskBranch(TaskInfo info);
	
	/**
	 * ��ȡ�����Ĺ���
	 * @param oid
	 * @return
	 */
	public List getLinkedTask(String linkOidStr);
}

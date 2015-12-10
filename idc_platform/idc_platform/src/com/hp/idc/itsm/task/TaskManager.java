package com.hp.idc.itsm.task;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.inter.PersonInfoInterface;
import com.hp.idc.itsm.inter.TaskManagerInterface;
import com.hp.idc.itsm.security.OrganizationInfo;
import com.hp.idc.itsm.security.OrganizationManager;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.security.WorkgroupInfo;
import com.hp.idc.itsm.security.WorkgroupManager;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;

/**
 * ����������
 * @author �����
 *
 */
public class TaskManager{
	/**
	 * �̳��������["ITSM"="com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl"..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	public static Map<String,String> classInsab = new HashMap<String,String>();
	
	/**
	 * �̳����map����["ITSM"=TaskManagerInterface..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	
	public static Map<String,TaskManagerInterface> classIns = new HashMap<String,TaskManagerInterface>();
	/**
	 * ��ָ���Ľڵ���ҷ�֧��ʼ�ڵ�
	 * @param taskData ָ���Ľڵ�
	 * @param workflow ��������
	 * @return �ҵ��ķ�֧��ʼ�ڵ�
	 * @throws Exception
	 */
	public static TaskData getBranchBegin(TaskData taskData, WorkflowData workflow) throws Exception {
		TaskData b = taskData;
		int adj = 1;
		for ( ; ; ) {
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
	 * ǿ�ƹرչ���
	 * @param taskOid
	 * @param message
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public static int forceCloseTask(String className,int wfOid,
			int taskOid, String message, String operName) throws Exception {
		
		int retInt = -1;
		TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
		retInt = tmi.forceCloseTask(wfOid,taskOid, message, operName);
		
		return retInt;
	}
	
	/**
	 * �رշ�֧��ʼ�ڵ㣬���ٽ��з���
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public static int closeTaskData(String className,int wfOid,int taskOid, int taskDataId, String operName)  throws Exception {
		int retInt = -1;
		TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
		retInt = tmi.closeTaskData(wfOid,taskOid, taskDataId, operName);
		
		return retInt;
	}
	
	/**
	 * ���¹�������
	 * @param className ��������ϵͳ��ʶ��ʹ�ñ�ϵͳ������"ITSM"
	 * @param taskOid ����ID���½�ʱΪ-1
	 * @param workflowOid ����ID
	 * @param map ǰ̨�������ı�MAP����
	 * @param dataId ��ǰ���ݽڵ�ID���½�ʱΪ-1
	 * @param toNodeId Ŀ�����̽ڵ�ID���½�ʱΪ��������������ڵ��µĵڶ����ڵ��ID
	 * @param assignTo ������һ��������
	 * @param operName ������ǰ������
	 * @param operType �������ͣ������TaskUpdateInfo.TYPE_SAVE����Ϊ��������²���ת��2008-03-28�¼ӣ�
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public static int updateTask(String className,
			int taskOid, int workflowOid, Map map, int dataId, 
			String toNodeId,String actionId, String assignTo, String operName,int operType) throws Exception {
		int retInt = -1;
		TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
		retInt = tmi.updateTask(taskOid, workflowOid, map, dataId, toNodeId,actionId, assignTo, operName,operType);
		
		return retInt;
	}
	
	/**
	 * ���˹���
	 * @param taskOid
	 * @param taskDataId
	 * @param rollbackMessage
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public static int rollbackTask(String className,int wfOid,int taskOid, int taskDataId, 
			String rollbackMessage, String operName)
		throws Exception {
		int retInt = -1;
		TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
		retInt = tmi.rollbackTask(wfOid,taskOid, taskDataId, rollbackMessage, operName);
		
		return retInt;
	}

	/**
	 * �Թ���������
	 * @param taskOid
	 * @param taskDataId
	 * @param message
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public static int addTaskMessage(String className,int wfOid,int taskOid, int taskDataId, 
			String message, String operName)
		throws Exception {
		int retInt = -1;
		TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
		retInt = tmi.addTaskMessage(wfOid,taskOid, taskDataId, message, operName);
		
		return retInt;
	}

	/**
	 * ���ܹ��������䵽�Լ���ģ�
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return ���ز������(δʹ��)
	 * @throws Exception
	 */
	public static int acceptTask(String className,int wfOid,int taskOid, int taskDataId, 
			String operName)
		throws Exception {
		int retInt = -1;
		TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
		retInt = tmi.acceptTask(wfOid,taskOid, taskDataId, operName);
		return retInt;
	}
	
	/**
	 * ��ȡ��ǰ��¼����Ҫ����Ĺ�������һ����ϵͳ�ģ�����itsm�ġ�ovsd��.....��
	 * @param className ���������Ҫ��ȡitsm�ľ��ǡ�com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl��
	 * @param operName
	 * @return
	 * @throws Exception
	 */
	public static List getTaskInfosByUser(String className,String operName) throws Exception{
		if (className!=null && className.equalsIgnoreCase("ALL"))
			return getTaskInfosByUser(operName);
		List retList = new ArrayList();
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			retList = tmi.getTaskInfosByUser(operName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retList;
	}

	/**
	 * ��ȡ��ǰ��¼����Ҫ����Ĺ��������й���ϵͳ�ģ�
	 * @param operName
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static List getTaskInfosByUser(String operName){
		List retList = new ArrayList();
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(keys[i]);
			try {
				retList.addAll(tmi.getTaskInfosByUser(operName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retList;

	}

	/**
	 * ͨ��oid���ҹ���
	 * @param oid
	 * @param wfOid:����id
	 * @return �ҵ��Ĺ���
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParseException 
	 * @throws DocumentException 
	 */
	public static TaskInfo getTaskInfoByOid(String className,int oid,int wfOid,int wfVer) throws Exception{
		if (oid == -1)
			return null;
		if (className!=null && className.equalsIgnoreCase("ALL"))
			return null;
		TaskInfo taskInfo = null;
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			taskInfo = tmi.getTaskInfoByOid(oid,wfOid,wfVer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInfo;
	}
	
	/**
	 * ���¹��������ݿ�
	 * @param incInfo
	 * @param operName
	 * @return ���ݿ��й�����oid
	 * @throws SQLException
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static int updateTaskInfo(TaskInfo incInfo, String operName) throws Exception{
		int retInt = -1;
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(incInfo.getOrigin());
			retInt = tmi.updateTaskInfo(incInfo, operName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retInt;
	}
	
	/**
	 * ɾ������
	 * @param oid ��¼ID
	 * @param operName
	 * @throws SQLException
	 */
	public static void deleteTaskInfo(String className,int wfOid,int wfVer,int oid, String operName) throws Exception {
		if (className!=null && className.equalsIgnoreCase("ALL"))
			return;
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			tmi.deleteTaskInfo(wfOid,wfVer,oid, operName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�����������
	 * @param user
	 * @return
	 */
	public static List getHistoryTaskInfoByUser(String className,String operName) throws Exception{
		if (className!=null && className.equalsIgnoreCase("ALL"))
			return getHistoryTaskInfoByUser(operName);
		List retList = new ArrayList();
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			retList = tmi.getHistoryTaskInfoByUser(operName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retList;
	}
	
	public static List getHistoryTaskInfoByUser(String operName){
		List retList = new ArrayList();
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(keys[i]);
			try {
				retList.addAll(tmi.getHistoryTaskInfoByUser(operName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retList;
	}
	
	/**
	 * ��ȡ���й���ϵͳ�����еĹ���
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static List getAllTaskInfo(){
		List retList = new ArrayList();
		
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			System.out.println(System.currentTimeMillis());
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(keys[i]);
			System.out.println(System.currentTimeMillis());
			try {
				retList.addAll(tmi.getAllTaskInfo());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(System.currentTimeMillis());
		}
		
		return retList;
	}
	
	/**
	 * ȡ��һ����ϵͳ�����й���
	 * @param className 
	 * @return
	 * @throws Exception
	 */
	public static List getAllTaskInfo(String className){
		if (className!=null && className.equalsIgnoreCase("ALL"))
			return getAllTaskInfo();
		List retList = new ArrayList();
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			retList = tmi.getAllTaskInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
	}
	
	/**
	 * ��ȡ���д򿪵Ĺ����ڵ�
	 * @return �������д򿪵Ĺ���List[TaskData]
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static List getAllOpenedTasks(int wfOid){
		List ret = new ArrayList();
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(keys[i]);
			ret.addAll(tmi.getAllOpenedTasks(wfOid));
		}
		return ret;
	}
	
	public static List getAllOpenedTasks(String className,int wfOid){
		if (className!=null && className.equalsIgnoreCase("ALL"))
			return getAllOpenedTasks(wfOid);
		List retList = new ArrayList();
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			if (tmi!=null)
				retList = tmi.getAllOpenedTasks(wfOid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
	}

	/**
	 * ��ȡ���д򿪵Ĺ���
	 * @return �������д򿪵Ĺ���List[TaskInfo]
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static List getAllOpenedTaskInfos(int wfOid){
		List ret = new ArrayList();
		
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(keys[i]);
			ret.addAll(tmi.getAllOpenedTaskInfos(wfOid));
		}
		return ret;
	}
	
	/**
	 * ȡ��һ����ϵͳ�����д򿪵Ĺ���
	 * @return
	 */
	public static List getAllOpenedTaskInfos(String className,int wfOid){
		if (className==null || className.equals("") || className.equalsIgnoreCase("ALL"))
			return getAllOpenedTaskInfos(wfOid);
		List retList = new ArrayList();
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			if (tmi!=null)
				retList = tmi.getAllOpenedTaskInfos(wfOid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
	}
	
	/**
	 * ȡ���йرյĹ���
	 * @return
	 */
	public static List getAllClosedTaskInfos(String beginTime,String endTime,int wfOid){
		List ret = new ArrayList();
		
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(keys[i]);
			try {
				ret.addAll(tmi.getAllClosedTaskInfos(beginTime,endTime,wfOid));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public static List getAllClosedTaskInfos(String className,String beginTime,String endTime,int wfOid){
		if (className!=null && className.equalsIgnoreCase("ALL"))
			return getAllClosedTaskInfos(beginTime,endTime,wfOid);
		List retList = new ArrayList();
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			if (tmi!=null)
				retList = tmi.getAllClosedTaskInfos(beginTime,endTime,wfOid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
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
	public static List getTaskInfoBySQL(String className,String sql) throws IOException, DocumentException, ParseException, SQLException {
		List retList = new ArrayList();
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			if (tmi!=null)
				retList = tmi.getTaskInfoBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
	}
	
	/**
	 * 2008-01-02 10:02:20�¼�
	 * @param sql ���Դ�:p������sql��
	 * @param request
	 * @return
	 * @throws SQLException 
	 * 
	 */
	public static List getTaskInfoBySQL(String className,String user,String password,String url,String sql,HttpServletRequest request){
		List l = new ArrayList();
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
			if (tmi!=null)
				l = tmi.getTaskInfoBySQL(user, password, url, sql, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	};
	
	/**
	 * ��ȡ�����ķ�֧��¼
	 * @param className ����ϵͳ��ʶ
	 * @param taskOid ������id
	 * @return List[TaskInfo,...]
	 */
	public static List getTaskBranch(TaskInfo info){
		List retList = new ArrayList();
		try {
			TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(info.getOrigin());
			if (tmi!=null)
				retList = tmi.getTaskBranch(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
	}
	
	/**
	 * ��ȡ��������
	 * @param linkOidStr ������������ITSM_1_2,OVSD_23_343��
	 * @return
	 */
	public static List getLinkedTask(String linkOidStr){
		List retList = new ArrayList();
		String[] links = linkOidStr.split(",");
		for (int i = 0; i < links.length; i++) {
			try {
				//taskOri[0]������Դ,taskOri[1]:����Id,taskOri[2]:����ID
				String[] taskOri = links[i].split("_");
				int taskOid = -1;
				int wfOid = -1;
				int wfVer = -1;
				if (taskOri.length == 3) {
					//����OID
					if (taskOri[1] == null || taskOri[1].equals(""))
						continue;
					wfOid = Integer.parseInt(taskOri[1]);
					//����OID
					if (taskOri[2] == null || taskOri[2].equals(""))
						continue;
					taskOid = Integer.parseInt(taskOri[2]);
					
				} else if (taskOri.length == 4) {
					//����OID
					if (taskOri[1] == null || taskOri[1].equals(""))
						continue;
					wfOid = Integer.parseInt(taskOri[1]);
					//����Version
					if (taskOri[2] == null || taskOri[2].equals(""))
						continue;
					wfVer = Integer.parseInt(taskOri[2]);
					//����OID
					if (taskOri[3] == null || taskOri[3].equals(""))
						continue;
					taskOid = Integer.parseInt(taskOri[3]);
				}
				retList.add(TaskManager.getTaskInfoByOid(taskOri[0], taskOid,wfOid,wfVer));
			} catch (Exception e) {
				System.out.println("��ȡ������������:"+links[i]);
				e.printStackTrace();
			}
		}
		return retList;
	}
	
	/**
	 * ��������֧���������ȣ���ֹ�ڷ�֧�����ڵ㣩
	 * @param tdBegin ��֧��ʼ�ڵ�
	 * @return
	 */
	public static int getBranchMaxDepth(TaskData tdBegin,WorkflowData wfData,int j){
		int depth = 1;
		List childs = tdBegin.getChilds();
		if (childs.size()==0 || (tdBegin.isBranchEnd()&&j==0))
			return 1;
		if (tdBegin.isBranchEnd())
			j--;
		int subDepth = 0;
		for(int i = 0; i < childs.size(); i++) {
			TaskData td_ = (TaskData)childs.get(i);
			NodeInfo n_ = wfData.getNode(td_.getNodeId());
			if(n_.getType() == NodeInfo.TYPE_BRANCH_BEGIN)
				j++;
			int sub = getBranchMaxDepth(td_,wfData,j);
			subDepth = subDepth>sub?subDepth:sub;
		}
		depth += subDepth;
		
		return depth;
	}
	
	/**
	 * �����ӽڵ�������
	 * @param tdBegin
	 * @return
	 */
	public static int getBranchMaxWidth(TaskData tdBegin,WorkflowData wfData,int dataid){
		int width = 0;
		List childs = tdBegin.getChilds();
		boolean end = false;
		if (tdBegin.isBranchEnd()){
			try {
				TaskData bb = getBranchBegin(tdBegin,wfData);
				if (bb.getDataId() == dataid)
					end = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (childs.size()==0 || end)
			return 1;
		for(int i = 0; i < childs.size(); i++) {
			TaskData td_ = (TaskData)childs.get(i);
			width += getBranchMaxWidth(td_,wfData,dataid);
		}
		
		return width;
	}
	
	/**
	 * ���ݹ�����Ϣ������vml������ie�ϻ�ͼ
	 * @param td
	 * @param wfData
	 * @param bbPosition
	 * @param parentX
	 * @param parentY
	 * @param x
	 * @param y
	 * @param canChange
	 * @return
	 */
	public static String printVMLNode(TaskData td,WorkflowData wfData,Map bbPosition, int parentX, int parentY,int x, int y,boolean canChange){
		StringBuffer sb = new StringBuffer();
		int rectPH = 15;//��Ա���ֿ�ĸ߶�
		int rectNH = 22;//�ڵ���Ϣ��ĸ߶�
		int rectEH = 0;//�հ׿�ĸ߶�
		int rectW = 72;//�ڵ����
		int rectEW = 25;//ˮƽ�հ�
		
		sb.append("<v:RoundRect  stroked='false' filled='false' style='position:relative;left:"+x+";top:"+(y+5)+";width:"+rectW+"px;height:"+rectPH+"px;'>");
		String piName = "";

		if (td.getAssignType() == TaskData.ASSIGN_PERSON) {
			String dealBy = td.getDealedBy();
			if (dealBy == null || dealBy.equals(""))
				dealBy = td.getAssignTo();
			PersonInfoInterface pi = PersonManager.getPersonById(td.getOwner().getOrigin(),dealBy);
			if (!td.isBranchEnd())
				piName = pi==null?dealBy:pi.getName();
		} else if (td.getAssignType() == TaskData.ASSIGN_ORGANIZATION) {
			OrganizationInfo oInfo = OrganizationManager.getOrganizationById(td.getAssignTo());
			if (!td.isBranchEnd())
				piName = oInfo==null?td.getAssignTo():oInfo.getName();
		} else if (td.getAssignType() == TaskData.ASSIGN_WORKGROUP) {
			WorkgroupInfo wInfo = WorkgroupManager.getWorkgroupById(td.getAssignTo());
			if (!td.isBranchEnd())
				piName = wInfo==null?td.getAssignTo():wInfo.getName();
		}
		sb.append("<v:TextBox inset='0pt,0pt,0pt,0pt' fill='no'><table border='0' height='100%' width='100%'><tr><td align='center' valign='middle'>"+piName+"</td></tr></table></v:TextBox>");
		sb.append("</v:RoundRect>");
		sb.append("<v:RoundRect  title='"+td.getNodeDesc() +(td.isRollback()?"(�ѻ���)":"")+"'");
		sb.append("style='position:relative;left:"+x+";top:"+(y+rectPH+5)+";width:"+rectW+"px;height:"+rectNH+"px;"+((canChange && !td.isBranchEnd())?" cursor:hand ":"")+"'");
		if (canChange && !td.isBranchEnd() && !td.isEditing()) {
			sb.append("onclick='displayNode("+td.getOwner().getOid()+","+td.getDataId()+",\""+td.getOwner().getOrigin()+"\","+td.getOwner().getWfOid()+")'");
		}
		sb.append(" stroked='f'>");
		
		if (td.isRollback() || td.isEditing() || td.isBranchEnd()) {
			sb.append("<v:TextBox inset='0pt,0pt,0pt,0pt' fill='no'><table border='0' height='100%' width='100%'><tr><td align='center' valign='middle' disabled='true'>"+td.getNodeDesc()+"</td></tr></table></v:TextBox>");
			sb.append("<v:imagedata id='node_"+td.getDataId()+"_e' src='"+Consts.ITSM_HOME+"/images/g_back.gif' status='2'/>");
		} else {
			sb.append("<v:TextBox inset='0pt,0pt,0pt,0pt' fill='no' style='FILTER: Dropshadow(color=#FFFFFF,offX=1,offY=1);'><table border='0' height='100%' width='100%'><tr><td align='center' valign='middle'>"+td.getNodeDesc()+"</td></tr></table></v:TextBox>");
			if(td.getStatus()==TaskData.STATUS_CLOSE)
				sb.append("<v:imagedata id='node_"+td.getDataId()+"_e' src='"+Consts.ITSM_HOME+"/images/g_close.gif' status='"+td.getStatus()+"'/>");
			else
				sb.append("<v:imagedata id='node_"+td.getDataId()+"_e' src='"+Consts.ITSM_HOME+"/images/g_open.gif' status='"+td.getStatus()+"'/>");
		}
		sb.append("</v:RoundRect> ");
		
		if (parentX!=-1&& parentY!=-1) {
			int x1 = parentX+rectW;
			int y1 = parentY+5+rectPH+rectNH/2;
			int x2 = x;
			int y2 = y+5+rectPH+rectNH/2;
			String points = x1+","+y1+","+x2+","+y2;
	    	if (y1!=y2)
	    		points = x1+","+y1+","+(x2-12)+","+y1+","+(x2-12)+","+y2+","+x2+","+y2;
	    	sb.append("<v:polyline points='"+points+"' filled='false'>");
	    	if (td.isEditing())
	    		sb.append("<v:stroke EndArrow='Classic' dashstyle='dash'/>");
	    	else
	    		sb.append("<v:stroke EndArrow='Classic'/>");
	    	sb.append("</v:polyline>");
		}
		
		List childs = td.getChilds();
		int width = 0;//����ͼ���ݿ��
		NodeInfo nInfo = null;
		if (wfData!=null)
			nInfo = wfData.getNode(td.getNodeId());
		//�����֧��ʼ�ڵ�λ����Ϣ��Ϊ�����֧�����ڵ�λ��ʹ��
		if (nInfo!=null && nInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN){
			int depth = getBranchMaxDepth(td,wfData,0)-1;//��֧��������
			int[] p = new int[3];
			p[0] = x;//xλ��
			p[1] = y;//yλ��
			p[2] = depth;
			bbPosition.put(td.getDataId()+"", p);
		}
		for (int i = 0; i < childs.size(); i++) {
			TaskData t = (TaskData)childs.get(i);
			int subx = x+rectW+rectEW;
			int suby = y+width*(rectPH+rectNH+rectEH+5);
			
			//���÷�֧�����ڵ㣨x=��֧��ʼ�ڵ�x+��֧������;y=��֧��ʼ�ڵ�y��
			if (t.isBranchEnd()){
				TaskData tdbb = null;//��֧�Ŀ�ʼ�ڵ�
				try {
					if (wfData!=null)
						tdbb = getBranchBegin(t, wfData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println("��������ͼ"+t.getNodeDesc()+"/��ʼ�ڵ㣺"+tdbb.getNodeDesc());
				if(tdbb!=null){
					int[] bbp = (int[])bbPosition.get(tdbb.getDataId()+"");
					subx = bbp[0] + bbp[2]*(rectW+rectEW);
					suby = bbp[1];
				}
			}
			
			sb.append(printVMLNode(t,wfData,bbPosition,x,y,subx,suby,canChange));
			width += getBranchMaxWidth(t,wfData,t.getDataId());
		}
		return sb.toString();
	}
	
	/**
	 * �ѹ������ݣ����¼��㣬����λ����Ϣ
	 * @param td
	 * @param wfData
	 * @param bbPosition
	 * @param parentX
	 * @param parentY
	 * @param x
	 * @param y
	 * @param canChange
	 * @return
	 * @throws DocumentException 
	 */
	public static void printXML(Element rootEl,TaskData td,WorkflowData wfData,Map bbPosition,int x, int y,boolean canChange) throws DocumentException{
		int rectPH = 15;//��Ա���ֿ�ĸ߶�
		int rectNH = 22;//�ڵ���Ϣ��ĸ߶�
		int rectEH = 0;//�հ׿�ĸ߶�
		int rectW = 72;//�ڵ����
		int rectEW = 25;//ˮƽ�հ�
		

		Element nodeEl = rootEl.addElement("node");
		nodeEl.addAttribute("id", "node_"+td.getDataId());
		nodeEl.addAttribute("name", td.getNodeDesc() +(td.isRollback()?"(�ѻ���)":""));
		nodeEl.addAttribute("type", "normal");
		nodeEl.addAttribute("px", x+"");
		nodeEl.addAttribute("py", (y+5)+"");
		nodeEl.addAttribute("width", rectW+"");
		nodeEl.addAttribute("height", rectNH+"");
		nodeEl.addAttribute("locked", "true");
		
		String piName = "";

		if (td.getAssignType() == TaskData.ASSIGN_PERSON) {
			String dealBy = td.getDealedBy();
			if (dealBy == null || dealBy.equals(""))
				dealBy = td.getAssignTo();
			PersonInfoInterface pi = PersonManager.getPersonById(td.getOwner().getOrigin(),dealBy);
			if (!td.isBranchEnd())
				piName = pi==null?dealBy:pi.getName();
		} else if (td.getAssignType() == TaskData.ASSIGN_ORGANIZATION) {
			OrganizationInfo oInfo = OrganizationManager.getOrganizationById(td.getAssignTo());
			if (!td.isBranchEnd())
				piName = oInfo==null?td.getAssignTo():oInfo.getName();
		} else if (td.getAssignType() == TaskData.ASSIGN_WORKGROUP) {
			WorkgroupInfo wInfo = WorkgroupManager.getWorkgroupById(td.getAssignTo());
			if (!td.isBranchEnd())
				piName = wInfo==null?td.getAssignTo():wInfo.getName();
		}
		
		nodeEl.addAttribute("subName", piName);
		if (canChange && !td.isBranchEnd() && !td.isEditing()) {		
			nodeEl.addAttribute("jsAction","displayNode");
			nodeEl.addAttribute("jsParams",td.getOwner().getOid()+","+td.getDataId()+","+td.getOwner().getOrigin()+","+td.getOwner().getWfOid());
		}
		TaskData tdParent = td.getParent();
		if (tdParent!=null) {
			Element lineEl = rootEl.addElement("line");
			lineEl.addAttribute("id", "line_"+td.getDataId());
			lineEl.addAttribute("arrowDis", "5");
			lineEl.addAttribute("fromNodeId", "node_"+tdParent.getDataId());
			lineEl.addAttribute("toNodeId", "node_"+td.getDataId());
		}
		
		List childs = td.getChilds();
		int width = 0;//����ͼ���ݿ��
		NodeInfo nInfo = null;
		if (wfData!=null)
			nInfo = wfData.getNode(td.getNodeId());
		//�����֧��ʼ�ڵ�λ����Ϣ��Ϊ�����֧�����ڵ�λ��ʹ��
		if (nInfo!=null && nInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN){
			int depth = getBranchMaxDepth(td,wfData,0)-1;//��֧��������
			int[] p = new int[3];
			p[0] = x;//xλ��
			p[1] = y;//yλ��
			p[2] = depth;
			bbPosition.put(td.getDataId()+"", p);
		}
		for (int i = 0; i < childs.size(); i++) {
			TaskData t = (TaskData)childs.get(i);
			int subx = x+rectW+rectEW;
			int suby = y+width*(rectPH+rectNH+rectEH+5);
			
			//���÷�֧�����ڵ㣨x=��֧��ʼ�ڵ�x+��֧������;y=��֧��ʼ�ڵ�y��
			if (t.isBranchEnd()){
				TaskData tdbb = null;//��֧�Ŀ�ʼ�ڵ�
				try {
					if (wfData!=null)
						tdbb = getBranchBegin(t, wfData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println("��������ͼ"+t.getNodeDesc()+"/��ʼ�ڵ㣺"+tdbb.getNodeDesc());
				if(tdbb!=null){
					int[] bbp = (int[])bbPosition.get(tdbb.getDataId()+"");
					subx = bbp[0] + bbp[2]*(rectW+rectEW);
					suby = bbp[1];
				}
			}
			
			printXML(rootEl,t,wfData,bbPosition,subx,suby,canChange);
			width += getBranchMaxWidth(t,wfData,t.getDataId());
		}
	}

}

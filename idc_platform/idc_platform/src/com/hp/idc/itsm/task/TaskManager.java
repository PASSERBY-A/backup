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
 * 工单管理类
 * @author 李会争
 *
 */
public class TaskManager{
	/**
	 * 继承类的类名["ITSM"="com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl"..]
	 * 所有实现TaskManagerInterface接口的类注册一下，就可以自动实现方法的执行
	 */
	public static Map<String,String> classInsab = new HashMap<String,String>();
	
	/**
	 * 继承类的map数组["ITSM"=TaskManagerInterface..]
	 * 所有实现TaskManagerInterface接口的类注册一下，就可以自动实现方法的执行
	 */
	
	public static Map<String,TaskManagerInterface> classIns = new HashMap<String,TaskManagerInterface>();
	/**
	 * 从指定的节点查找分支开始节点
	 * @param taskData 指定的节点
	 * @param workflow 流程数据
	 * @return 找到的分支开始节点
	 * @throws Exception
	 */
	public static TaskData getBranchBegin(TaskData taskData, WorkflowData workflow) throws Exception {
		TaskData b = taskData;
		int adj = 1;
		for ( ; ; ) {
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
	 * 强制关闭工单
	 * @param taskOid
	 * @param message
	 * @param operName
	 * @return 返回操作结果(未使用)
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
	 * 关闭分支开始节点，不再进行分配
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return 返回操作结果(未使用)
	 * @throws Exception
	 */
	public static int closeTaskData(String className,int wfOid,int taskOid, int taskDataId, String operName)  throws Exception {
		int retInt = -1;
		TaskManagerInterface tmi = (TaskManagerInterface)classIns.get(className);
		retInt = tmi.closeTaskData(wfOid,taskOid, taskDataId, operName);
		
		return retInt;
	}
	
	/**
	 * 更新工单数据
	 * @param className 处理工单的系统标识，使用本系统处理，填"ITSM"
	 * @param taskOid 工单ID，新建时为-1
	 * @param workflowOid 流程ID
	 * @param map 前台传过来的表单MAP数组
	 * @param dataId 当前数据节点ID，新建时为-1
	 * @param toNodeId 目标流程节点ID，新建时为流程配置里面根节点下的第二个节点的ID
	 * @param assignTo 工单下一步处理人
	 * @param operName 工单当前操作人
	 * @param operType 操作类型，如果＝TaskUpdateInfo.TYPE_SAVE，则为仅保存更新不流转（2008-03-28新加）
	 * @return 返回操作结果(未使用)
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
	 * 回退工单
	 * @param taskOid
	 * @param taskDataId
	 * @param rollbackMessage
	 * @param operName
	 * @return 返回操作结果(未使用)
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
	 * 对工单添加意见
	 * @param taskOid
	 * @param taskDataId
	 * @param message
	 * @param operName
	 * @return 返回操作结果(未使用)
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
	 * 接受工单（分配到自己组的）
	 * @param taskOid
	 * @param taskDataId
	 * @param operName
	 * @return 返回操作结果(未使用)
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
	 * 获取当前登录者需要处理的工单（单一工单系统的，比如itsm的、ovsd的.....）
	 * @param className 类名，如果要获取itsm的就是“com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl”
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
	 * 获取当前登录者需要处理的工单（所有工单系统的）
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
	 * 通过oid查找工单
	 * @param oid
	 * @param wfOid:流程id
	 * @return 找到的工单
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
	 * 更新工单至数据库
	 * @param incInfo
	 * @param operName
	 * @return 数据库中工单的oid
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
	 * 删除工单
	 * @param oid 记录ID
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
	 * 获取参与过的任务
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
	 * 获取所有工单系统的所有的工单
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
	 * 取单一工单系统的所有工单
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
	 * 获取所有打开的工单节点
	 * @return 返回所有打开的工单List[TaskData]
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
	 * 获取所有打开的工单
	 * @return 返回所有打开的工单List[TaskInfo]
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
	 * 取单一工单系统的所有打开的工单
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
	 * 取所有关闭的工单
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
	 * 根据sql查询
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
	 * 2008-01-02 10:02:20新加
	 * @param sql 可以带:p参数的sql串
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
	 * 获取工单的分支记录
	 * @param className 工单系统标识
	 * @param taskOid 主工单id
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
	 * 获取关联工单
	 * @param linkOidStr 关联工单串（ITSM_1_2,OVSD_23_343）
	 * @return
	 */
	public static List getLinkedTask(String linkOidStr){
		List retList = new ArrayList();
		String[] links = linkOidStr.split(",");
		for (int i = 0; i < links.length; i++) {
			try {
				//taskOri[0]工单来源,taskOri[1]:流程Id,taskOri[2]:工单ID
				String[] taskOri = links[i].split("_");
				int taskOid = -1;
				int wfOid = -1;
				int wfVer = -1;
				if (taskOri.length == 3) {
					//流程OID
					if (taskOri[1] == null || taskOri[1].equals(""))
						continue;
					wfOid = Integer.parseInt(taskOri[1]);
					//工单OID
					if (taskOri[2] == null || taskOri[2].equals(""))
						continue;
					taskOid = Integer.parseInt(taskOri[2]);
					
				} else if (taskOri.length == 4) {
					//流程OID
					if (taskOri[1] == null || taskOri[1].equals(""))
						continue;
					wfOid = Integer.parseInt(taskOri[1]);
					//流程Version
					if (taskOri[2] == null || taskOri[2].equals(""))
						continue;
					wfVer = Integer.parseInt(taskOri[2]);
					//工单OID
					if (taskOri[3] == null || taskOri[3].equals(""))
						continue;
					taskOid = Integer.parseInt(taskOri[3]);
				}
				retList.add(TaskManager.getTaskInfoByOid(taskOri[0], taskOid,wfOid,wfVer));
			} catch (Exception e) {
				System.out.println("获取关联工单出错:"+links[i]);
				e.printStackTrace();
			}
		}
		return retList;
	}
	
	/**
	 * 检索【分支】的最大深度（中止于分支结束节点）
	 * @param tdBegin 分支开始节点
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
	 * 检索子节点的最宽宽度
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
	 * 根据工单信息，生成vml串，在ie上画图
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
		int rectPH = 15;//人员名字框的高度
		int rectNH = 22;//节点信息框的高度
		int rectEH = 0;//空白框的高度
		int rectW = 72;//节点框宽度
		int rectEW = 25;//水平空白
		
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
		sb.append("<v:RoundRect  title='"+td.getNodeDesc() +(td.isRollback()?"(已回退)":"")+"'");
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
		int width = 0;//流程图的纵宽度
		NodeInfo nInfo = null;
		if (wfData!=null)
			nInfo = wfData.getNode(td.getNodeId());
		//保存分支开始节点位置信息，为计算分支结束节点位置使用
		if (nInfo!=null && nInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN){
			int depth = getBranchMaxDepth(td,wfData,0)-1;//分支的最大深度
			int[] p = new int[3];
			p[0] = x;//x位置
			p[1] = y;//y位置
			p[2] = depth;
			bbPosition.put(td.getDataId()+"", p);
		}
		for (int i = 0; i < childs.size(); i++) {
			TaskData t = (TaskData)childs.get(i);
			int subx = x+rectW+rectEW;
			int suby = y+width*(rectPH+rectNH+rectEH+5);
			
			//重置分支结束节点（x=分支开始节点x+分支最大深度;y=分支开始节点y）
			if (t.isBranchEnd()){
				TaskData tdbb = null;//分支的开始节点
				try {
					if (wfData!=null)
						tdbb = getBranchBegin(t, wfData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println("构造流程图"+t.getNodeDesc()+"/开始节点："+tdbb.getNodeDesc());
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
	 * 把工单数据，重新计算，加入位置信息
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
		int rectPH = 15;//人员名字框的高度
		int rectNH = 22;//节点信息框的高度
		int rectEH = 0;//空白框的高度
		int rectW = 72;//节点框宽度
		int rectEW = 25;//水平空白
		

		Element nodeEl = rootEl.addElement("node");
		nodeEl.addAttribute("id", "node_"+td.getDataId());
		nodeEl.addAttribute("name", td.getNodeDesc() +(td.isRollback()?"(已回退)":""));
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
		int width = 0;//流程图的纵宽度
		NodeInfo nInfo = null;
		if (wfData!=null)
			nInfo = wfData.getNode(td.getNodeId());
		//保存分支开始节点位置信息，为计算分支结束节点位置使用
		if (nInfo!=null && nInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN){
			int depth = getBranchMaxDepth(td,wfData,0)-1;//分支的最大深度
			int[] p = new int[3];
			p[0] = x;//x位置
			p[1] = y;//y位置
			p[2] = depth;
			bbPosition.put(td.getDataId()+"", p);
		}
		for (int i = 0; i < childs.size(); i++) {
			TaskData t = (TaskData)childs.get(i);
			int subx = x+rectW+rectEW;
			int suby = y+width*(rectPH+rectNH+rectEH+5);
			
			//重置分支结束节点（x=分支开始节点x+分支最大深度;y=分支开始节点y）
			if (t.isBranchEnd()){
				TaskData tdbb = null;//分支的开始节点
				try {
					if (wfData!=null)
						tdbb = getBranchBegin(t, wfData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println("构造流程图"+t.getNodeDesc()+"/开始节点："+tdbb.getNodeDesc());
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

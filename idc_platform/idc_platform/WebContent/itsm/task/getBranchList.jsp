<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.inter.*"%>
<%@ page import="org.apache.velocity.*"%>
<%@ page import="org.apache.velocity.app.*"%>
<%@ include file="../getUser.jsp"%>
<%
int taskOid = -1;
if (request.getParameter("taskOid")!=null && !request.getParameter("taskOid").equals(""))
	taskOid = Integer.parseInt(request.getParameter("taskOid"));
int taskDataId = -1;
if (request.getParameter("taskDataId")!=null && !request.getParameter("taskDataId").equals(""))
	taskDataId = Integer.parseInt(request.getParameter("taskDataId"));

int wfOid = -1;
if (request.getParameter("wfOid")!=null && !request.getParameter("wfOid").equals(""))
	wfOid = Integer.parseInt(request.getParameter("wfOid"));
int wfVer = -1;
if (request.getParameter("wfVer")!=null && !request.getParameter("wfVer").equals(""))
	wfVer = Integer.parseInt(request.getParameter("wfVer"));

String origin = request.getParameter("origin");
TaskInfo taskInfo = TaskManager.getTaskInfoByOid(origin,taskOid,wfOid,wfVer);
TaskData taskData = taskInfo.getTaskData(taskDataId);
boolean canDeal = taskData.canDealThisNode(userId);
List<TaskData> childs = taskData.getChilds();
%>
<table class='embed2' border=0 cellspacing=1 align='center' width="99%">
<tr><th>分支序号</th><th>当前处理人</th><th>当前状态</th><th>操作</th></tr>
<%
for (int i = 0; i < childs.size(); i++) {
	TaskData ctd = childs.get(i);
	TaskData lasttd = ctd;
	while (lasttd.getChilds().size()>0){
		int csize = lasttd.getChilds().size();
		int j =0;
		for (; j < csize; j++) {
			TaskData ttd = (TaskData)lasttd.getChilds().get(j);
			if (ttd.getStatus() != TaskData.STATUS_EDIT) {
				lasttd = ttd;
				break;
			}
		}
		if (j == csize)
			break;
	}
	
	out.println("<tr><td width='50'>"+(i+1)+"</td>");
	out.println("<td>"+PersonManager.getPersonNameById(lasttd.getAssignTo())+"</td>");
	String status = lasttd.getNodeDesc();
	if (lasttd.getStatus() == TaskData.STATUS_CLOSE)
		status = "已关闭";
	else if (lasttd.getStatus() == TaskData.STATUS_FORCE_CLOSE)
		status = "强制关闭";
	out.println("<td>"+status+"</td>");
	out.print("<td width='90'>");
	if (lasttd.getStatus() == TaskData.STATUS_OPEN && canDeal) {
		if (lasttd.getParent().equals(taskData))
			out.print("<a href='#' onclick='javascript:removeTaskData("+wfOid+","+taskOid+","+taskDataId+","+lasttd.getDataId()+")'>删除</a>&nbsp;|&nbsp;");
		out.print("<a href='#' onclick='javascript:taskForceCloseTaskData("+wfOid+","+taskOid+","+taskDataId+","+lasttd.getDataId()+")'>强制关闭</a>");
	}
	out.print("</td>");
	out.println("</tr>");
}
%>
</table>
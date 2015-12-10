<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ include file="../getUser.jsp"%>


<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=gbk");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");


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

TaskInfo taskInfo = TaskManager.getTaskInfoByOid("ITSM",taskOid,wfOid,wfVer);
TaskData taskData = taskInfo.getTaskData(taskDataId);
session.setAttribute("copyCreateTaskData",taskData);
response.sendRedirect("taskInfo.jsp?wfOid="+wfOid+"&taskOid=-1&origin=ITSM&operType=copyCreate");

%>

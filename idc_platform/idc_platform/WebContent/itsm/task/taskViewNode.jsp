<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="org.apache.velocity.*"%>
<%@ page import="org.apache.velocity.app.*"%>
<%@ include file="../getUser.jsp"%>


<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

try {
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


//int wfOid = taskInfo.getWfOid();
WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
WorkflowData wfData = null;
if (wfInfo == null || (wfData = wfInfo.getVersion(taskInfo.getWfVer())) == null) {
	out.println("<script defer>Ext.MessageBox.alert('错误', '找不到流程信息(id=" + taskOid + "."
		+ taskDataId + "),<br>请与管理员联系.');</script>");
	return;
}
NodeInfo nodeInfo = wfData.getNode(taskData.getNodeId());

String formId = nodeInfo.getFormId();
FormInfo formInfo = null;
if (wfData.getForms().containsKey(formId)){
	formInfo = (FormInfo)wfData.getForms().get(formId);
} else {
	int formOid = 0;
	try {
		formOid = Integer.parseInt(formId);
	} catch(Exception e) {
		e.printStackTrace();
	}
	formInfo = FormManager.getFormByOid(formOid);
}
//FormInfo formInfo = FormManager.getFormByOid(formId);
List fieldList = null;
if (formInfo != null)
	fieldList = formInfo.getFields();
else {
	fieldList = new ArrayList();
	out.println("<script defer>alert('找不到表单信息,请与管理员联系.');</script>");
}

	Map context = new HashMap();
	context.put("taskData", taskData);
	context.put("DateTimeUtil", new DateTimeUtil());
	context.put("PersonManager", new PersonManager());
	context.put("taskInfo", taskInfo);
	context.put("StringUtil", new StringUtil());
	context.put("fieldList", fieldList);
	context.put("Integer", new Integer(0));
	context.put("workflowData", wfData);
	context.put("nodeInfo", nodeInfo);
	context.put("FieldManager", new FieldManager());
	context.put("request", request);
	context.put("userId", userId);
	context.put("FormManager", new FormManager());

	out.println(TemplateUtil.getHTMLStrFromTemplate(nodeInfo.getTemplate(),context));


} catch (Exception ex) { ex.printStackTrace(); } %>
<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ include file="../getUser.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

int taskOid = -1;
if (request.getParameter("taskOid")!=null && !request.getParameter("taskOid").equals(""))
	taskOid = Integer.parseInt(request.getParameter("taskOid"));
int dataId = 0;
if (request.getParameter("dataId")!=null && !request.getParameter("dataId").equals(""))
	dataId = Integer.parseInt(request.getParameter("dataId"));
int wfOid = -1;
if (request.getParameter("wfOid")!=null && !request.getParameter("wfOid").equals(""))
	wfOid = Integer.parseInt(request.getParameter("wfOid"));
int wfVer = -1;
if (request.getParameter("wfVer")!=null && !request.getParameter("wfVer").equals(""))
	wfVer = Integer.parseInt(request.getParameter("wfVer"));

boolean canChange = false;
if (request.getParameter("canChangeNode")!=null)
	canChange = true;

String origin = request.getParameter("origin");

try {

		TaskInfo taskInfo = null;
		TaskData taskData = null;
		WorkflowInfo wfInfo = null;
		WorkflowData wfData = null;
		NodeInfo nodeInfo = null;

		int formId = -1;
		if (taskOid == -1)
		{
			return;
		}
		taskInfo = TaskManager.getTaskInfoByOid(origin,taskOid,wfOid,wfVer);
		taskData = taskInfo.getRootData();
		wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
		if (wfInfo != null)
			wfData = wfInfo.getVersion(taskInfo.getWfVer());
		/*//if (wfInfo == null ||(wfData = wfInfo.getVersion(taskInfo.getWfVer()))==null)
		{
			out.println("<script defer>Ext.MessageBox.alert('错误', '找不到流程信息(id=" + taskOid + "),<br>请与管理员联系.');</script>");
			return;
		}
		nodeInfo = wfData.getNode(taskData.getNodeId());
		formId = nodeInfo.getFormId();
		FormInfo formInfo = FormManager.getFormByOid(formId);
		List fieldList = new ArrayList();
		if (formInfo!=null)
			fieldList = formInfo.getFields();
		*/

	%>
	<%
	int maxHeight = 40;
		if (!taskInfo.isShowHisGraphics()){
			String str = taskInfo.getHistoryStr();
			if (str!=null)
				str = str.replaceAll("\\n","<br>");
			out.println(str);
		} else {
	%>
	<v:group id="drawArea" style="WIDTH: 700px; HEIGHT: 100px; v-text-anchor: top-left" coordsize = "700,100" >
		<%
			try {
				String vmlStr = TaskManager.printVMLNode(taskData,wfData,new HashMap(),-1,-1,5,0,canChange);
				out.println(vmlStr);
			}catch (Exception e) {
				e.printStackTrace();
			}
		%>
	</v:group>
	<%}%>

<% } catch (Exception ex) { ex.printStackTrace(); } %>

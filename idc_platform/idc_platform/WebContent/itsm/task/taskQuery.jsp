<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.context.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

try{
List taskList = new ArrayList();
String title = request.getParameter("task_title");
String wfOid = request.getParameter("task_wf");
String status = request.getParameter("task_status");
String task_create_by = request.getParameter("task_create_by");
String task_create_time_b = request.getParameter("task_create_time_b");
String task_create_time_e = request.getParameter("task_create_time_e");

String start_s = request.getParameter("start");
int start = 0;
if (start_s != null)
	start = Integer.parseInt(start_s);

String limit_s = request.getParameter("limit");
int limit = 25;
if (limit_s != null)
	limit = Integer.parseInt(limit_s);

if (wfOid!=null && !wfOid.equals("-1") && !wfOid.equals("")) {
	WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(Integer.parseInt(wfOid));
	Map columnFields = wfInfo.getColumnFieldsMap();
	String sql = "select * from itsm_task_"+wfOid;
	if (status.equals("0")||status.equals("1")||status.equals("2"))
		sql += " where TASK_STATUS="+status;
	else
		sql += " where TASK_NODE_ID='"+status+"' and TASK_STATUS=0";

	if (title!=null && !title.equals("") && columnFields.get("TITLE")!=null)
		sql += " and fld_title like '%"+title+"%'";

	if (task_create_by!=null && !task_create_by.equals(""))
		sql += " and task_create_by ='"+task_create_by+"'";

	if (task_create_time_e!=null && !task_create_time_e.equals("")){
		sql +=" and to_char(TASK_CREATE_TIME,'yyyy-MM-dd') <= '"+task_create_time_e+"' ";
	}
	if (task_create_time_b!=null && !task_create_time_b.equals("")){
		sql +=" and to_char(TASK_CREATE_TIME,'yyyy-MM-dd') >= '"+task_create_time_b+"' ";
	}
	sql +=" order by TASK_CREATE_TIME desc";

	taskList = TaskManager.getTaskInfoBySQL("ITSM", sql);
}
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

{ "totalCount" : <%=taskList.size()%>,
	"items" : [
	<%
	for (int i = start; i < taskList.size() && i < (start+limit); i++)
	{
		TaskInfo taskInfo = (TaskInfo)taskList.get(i);
		Map values = taskInfo.getValues();
		if (i > start)
			out.print(",");
		out.print("{");
		out.print("'oid':'" + taskInfo.getOid() + "',");
		out.print("'origin':'ITSM',");
		out.print("'wfOid':'"+values.get("TASK_WF_OID")+"',");
		out.print("'wfVer':'"+values.get("TASK_WF_VER")+"',");
		out.print("'wfName':'" + StringUtil.escapeJavaScript((String)values.get("WORKFLOW")) + "',");
		out.print("'title':'" + StringUtil.escapeJavaScript((String)values.get("TITLE")) + "',");
		out.print("'create_by':'" + StringUtil.escapeJavaScript(PersonManager.getPersonNameById(taskInfo.getCreatedBy())) + "',");
		out.print("'create_time':'" + sdf.format(new Date(taskInfo.getCreateTime())) + "',");
		out.print("'status':'" + StringUtil.escapeJavaScript((String)values.get("TASK_STATUS")) + "'");
		out.print("}");
	}
	%>
	]
}

<%}catch (Exception e){e.printStackTrace();}%>
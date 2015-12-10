<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<%
String includeAll_s = request.getParameter("includeAll");

boolean includeAll = true;
if (includeAll_s!=null && includeAll_s.equals("false"))
	includeAll = false;

List workflows = WorkflowManager.getWorkflows(includeAll);

%>

[
{'id':'-1','text':'<%=StringUtil.escapeJavaScript("Ыљга")%>',leaf: 'true', _click: 'true'},
	<%
	for (int i = 0; i < workflows.size(); i++)
	{
		WorkflowInfo info = (WorkflowInfo)workflows.get(i);
		if (i > 0)
			out.print(",");
		out.print("{");
		out.print("'id':'" + info.getOid() + "',");
		out.print("'text':'" + StringUtil.escapeJavaScript(info.getName()) + "',");
		out.print("leaf: 'true', _click: 'true'");
		out.print("}");
	}
	%>
]

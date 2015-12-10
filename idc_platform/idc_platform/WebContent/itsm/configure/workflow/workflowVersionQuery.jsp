<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<%
int wfOid = -1;
if (request.getParameter("wfOid")!=null && !request.getParameter("wfOid").equals(""))
	wfOid = Integer.parseInt(request.getParameter("wfOid"));

WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
List wfDataList = new ArrayList();
if (wfInfo!=null)
	wfDataList = wfInfo.getWorkflows();
%>

{"totalCount":<%=wfDataList.size()%>,
	"items" : [
	<%
	for (int i = 0; i < wfDataList.size(); i++) 
	{
		WorkflowData wfData = (WorkflowData)wfDataList.get(i);
		if (i >0 )
			out.println(",");
		out.print("{");
		out.print("'id':'" + wfData.getVersionId() + "',");
		out.print("'name':'" + wfData.getVersionId() + "'");
		out.print("}");
	}
	%>
	]
}

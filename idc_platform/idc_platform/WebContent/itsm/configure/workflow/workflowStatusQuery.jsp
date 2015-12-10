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
int wfOid = -1;
if (request.getParameter("wfOid")!=null && !request.getParameter("wfOid").equals(""))
	wfOid = Integer.parseInt(request.getParameter("wfOid"));

String includeAllStatus = request.getParameter("includeAll");

WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
WorkflowData wfData = wfInfo.getCurrentVersion();
List nodeList = wfData.getNodes();
%>

{"totalCount":<%=(nodeList.size()+3)%>,
	"items" : [
	<%if ("true".equals(includeAllStatus)) {%>
	{'id':'0','caption':'<%=StringUtil.escapeJavaScript("所有打开的")%>'},
	{'id':'1','caption':'<%=StringUtil.escapeJavaScript("关闭")%>'},
	{'id':'2','caption':'<%=StringUtil.escapeJavaScript("强制关闭")%>'}
	<%
	}
	if ("true".equals(includeAllStatus)&& nodeList.size()>0)
		out.print(",");
	for (int i = 0; i < nodeList.size(); i++) 
	{
		NodeInfo info = (NodeInfo)nodeList.get(i);
		if (i>0)
			out.println(",");
		out.print("{");
		out.print("'id':'" + info.getId() + "',");
		out.print("'caption':'" + StringUtil.escapeJavaScript(info.getCaption()) + "'");
		out.print("}");
	}
	%>
	]
}

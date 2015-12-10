<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
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
String limit_s = request.getParameter("limit");
String start_s = request.getParameter("start");
String sort = request.getParameter("sort");
String dir = request.getParameter("dir");
int catalogOid = 6;
if (request.getParameter("catalogOid")!=null || !request.getParameter("catalogOid").equals(""))
	catalogOid = Integer.parseInt(request.getParameter("catalogOid"));
int limit = Consts.ITEMS_PER_PAGE;
if (limit_s != null)
	limit = Integer.parseInt(limit_s);
int start = 0;
if (start_s != null)
	start = Integer.parseInt(start_s);

List fields = CIManager.getCodeTypesOfCatalog(catalogOid);
if (sort != null)
	ItsmUtil.sort(fields, sort, "DESC".equals(dir));
//System.out.println(fields.size());
%>

{ "totalCount" : <%=fields.size()%>,
	"items" : [
	<%
	for (int i = start; i < start + limit && i < fields.size(); i++) 
	{
		CodeTypeInfo info = (CodeTypeInfo)fields.get(i);
		if (i > start)
			out.print(",");
		out.print("{");
		out.print("'id':'" + info.getOid() + "',");
		out.print("'name':'" + StringUtil.escapeJavaScript(info.getName()) + "',");
		out.print("'type':'" + info.getType() + "'");
		//out.print("'className':'" + StringUtil.escapeJavaScript(info.getObjectClass().getName()) + "'");
		out.print("}");
	}
	%>
	]
}
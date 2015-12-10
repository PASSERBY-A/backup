<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("UTF-8");
%>
<%
String limit_s = request.getParameter("limit");
String start_s = request.getParameter("start");
String sort = request.getParameter("sort");
String dir = request.getParameter("dir");
String includeAll =  request.getParameter("includeAll");
String filter = request.getParameter("filter");
String origin = request.getParameter("origin");
int limit = 0;
if (limit_s != null)
	limit = Integer.parseInt(limit_s);
int start = 0;
if (start_s != null)
	start = Integer.parseInt(start_s);
//PageInfo pageInfo = new PageInfo();
//pageInfo.setRowsPerPage(limit);
//pageInfo.setStartPage(start / limit);
int module = Integer.parseInt(request.getParameter("module"));

List fields = new ArrayList();
if (origin!=null && !origin.equals(""))
	fields = FieldManager.getFieldsOfModule(origin,module,"true".equals(includeAll)?true:false);
else
	fields = FieldManager.getFieldsOfModule(module,"true".equals(includeAll)?true:false);
if (sort != null)
	ItsmUtil.sort(fields, sort, "DESC".equals(dir));
if (filter != null && filter.length() > 0)
	ItsmUtil.filter(fields, "name", filter);
%>

{ "totalCount" : <%=fields.size()%>,
	"items" : [
	<%
	if (limit==0)
		limit = fields.size();
	else
		limit = limit+start;
	limit = limit>fields.size()?fields.size():limit;

	for (int i = start;  i < limit; i++)
	{
		FieldInfo info = (FieldInfo)fields.get(i);
		if (i > start)
			out.print(",");
		out.print("{");
		out.print("'id':'" + StringUtil.escapeJavaScript(info.getId()) + "',");
		out.print("'name':'" + StringUtil.escapeJavaScript(info.getName()) + "',");
		out.print("'type':'" + StringUtil.escapeJavaScript(info.getTypeDesc()==null?"":info.getTypeDesc()) + "',");
		out.print("'origin':'" + info.getOrigin() + "',");
		out.print("'category':'" + StringUtil.escapeJavaScript(info.getApplyToName()==null?"":info.getApplyToName()) + "',");
		out.print("'origin_category':'" + info.getOrigin()+"-"+StringUtil.escapeJavaScript(info.getApplyToName()) + "'");
		out.print("}");
	}
	%>
	]
}
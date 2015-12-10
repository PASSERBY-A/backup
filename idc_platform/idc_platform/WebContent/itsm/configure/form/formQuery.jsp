<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
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
int limit = Consts.ITEMS_PER_PAGE;
if (limit_s != null)
	limit = Integer.parseInt(limit_s);
int start = 0;
if (start_s != null)
	start = Integer.parseInt(start_s);

int module = Integer.parseInt(request.getParameter("module"));
List forms = FormManager.getFormsOfModule(module);
if (sort != null)
	ItsmUtil.sort(forms, sort, "DESC".equals(dir));
//System.out.println("" + forms.size());
%>

{ "totalCount" : <%=forms.size()%>,
	"items" : [
	<%
	for (int i = start; i < start + limit && i < forms.size(); i++) 
	{
		FormInfo info = (FormInfo)forms.get(i);
		if (i > start)
			out.print(",");
		out.print("{");
		out.print("'id':'" + info.getOid() + "',");
		out.print("'name':'" + StringUtil.escapeJavaScript(info.getName()) + "',");
		out.print("'category':'" + info.getOrigin()+"-"+StringUtil.escapeJavaScript(info.getApplyToName()) + "'");
		out.print("}");
	}
	%>
	]
}
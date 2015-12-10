<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.model.*"%>
<%@ page import="com.hp.idc.resm.expression.*"%>
<%@ page import="com.hp.idc.resm.resource.*"%>
<%@ page import="com.hp.idc.resm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
%>
<%
String message = "";

String expr = request.getParameter("expr");

Object object = null;

try {
	if (expr != null)
	{
		object = ServiceManager.getResourceService().findResource(expr, 1);
		message = "操作成功";
	}
} catch (Exception e) {
	message = e.getMessage();
	e.printStackTrace();
}

if (expr == null)
	expr = "true";
%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
</head>
<body>

<font color="red"><%=message%></font>

<form method="post" action="expression.jsp">
	表达式: <input name="expr" size="100" value="<%=StringUtil.escapeHtml(expr)%>" /> <input type="submit" value="查询" />
</form>

<%
if (object != null)
{
	out.print("返回结果:" + object.getClass().getName());
	if (object instanceof ArrayList)
	{
		List l = (List)object;
		out.println("count=" + l.size() + "<br/>");
		for (Object oo : l) {
			if (!(oo instanceof ResourceObject))
			{
					out.println(oo.toString() + "<br/>");
					continue;
			}
			ResourceObject res = (ResourceObject)oo;
			out.println(res.getId());
			List<AttributeBase> lab = res.getAllAttributes();
			for (AttributeBase ab : lab)
			{
				out.println("/" + ab.getAttribute().getAttrId() + "=" + ab.getText());
			}
			out.println("<br/>");
		}
	}
}
%>

</body>

</html>

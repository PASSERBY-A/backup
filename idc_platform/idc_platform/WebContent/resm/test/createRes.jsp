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
%>
<%
String message = "";

String modelId = request.getParameter("modelId");
Model model = ServiceManager.getModelService().getModelById(modelId);
List<ModelAttribute> list = model.getAttributes();

try {
	if ("add".equals(request.getParameter("_action")))
	{
		List<AttributeBase> ulist = new ArrayList<AttributeBase>();
		for (ModelAttribute m : list)
		{
			AttributeBase ab = m.getDefine().createInstance(modelId);
						if (ab == null)
				System.out.println("ERRRR: " + modelId + "/" + m.getDefine().getId());
			ab.setText((String)request.getParameter(m.getDefine().getId()));
			ulist.add(ab);
		}
		int newId = ServiceManager.getResourceUpdateService().addResource(modelId, ulist, 1);
		message = "操作成功，<a href=resource.jsp?id=" + newId + ">新对象 id=" + newId + "</a>";
	}
} catch (Exception e) {
	message = e.getMessage();
	e.printStackTrace();
}

%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
</head>
<body>

<font color="red"><%=message%></font>

<form method="post" action="createRes.jsp">
	<input type="hidden" name="_action" size="20" value="add" />
	modelId: <input name="modelId" size="20" value="<%=modelId%>" /><Br/>
	<table border=0 cellspacing=0 cellpadding=2>
<%
	for (ModelAttribute m : list)
	{
		String v = m.getDefaultValue();
		if (request.getParameter(m.getDefine().getId()) != null)
			v = (String)request.getParameter(m.getDefine().getId());
		if (v == null)
			v = "";
	out.println("<tr color='red'><td>");
	if (!m.isNullable())
		out.print("<b>");
	out.println(m.getDefine().getName());
	if (!m.isNullable())
		out.print("</b>");
	out.println("</td><td>");
	if (!m.isNullable())
		out.print("<b>");
	out.print(m.getDefine().getId());
	if (!m.isNullable())
		out.print("</b>");
		out.println("</td>");
	out.println("<td><input name='" + m.getDefine().getId() + "' value='" + v + "'/></td></tr>");
	
	}
%>
</table>
	<input type="submit" value="提交" />
</form>

</body>

</html>

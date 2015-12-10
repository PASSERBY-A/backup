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

String id = request.getParameter("id");

ResourceObject object = null;

try {
	if (id != null)
	{
		object = ServiceManager.getResourceService().getResourceById(Integer.parseInt(id));
		if (object != null)
			message = "操作成功";
		else
			message = "找不到对象" + id;
	}
} catch (Exception e) {
	message = e.getMessage();
	e.printStackTrace();
}

if (id == null)
	id = "";
%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
</head>
<body>

<font color="red"><%=message%></font>

<form method="post" action="resource.jsp">
	id: <input name="id" size="20" value="<%=id%>" /> <input type="submit" value="查询" />
</form>

<%
if (object != null)
{
	out.print("返回结果:" + object.getClass().getName());
	ResourceObject res = (ResourceObject)(object.clone());
	out.println(res.getId());
	List<AttributeBase> lab = res.getAllAttributes();
	for (AttributeBase ab : lab)
	{
		out.println("/" + ab.getAttribute().getAttrId() + "=" + ab.getText());
	}
	out.print("<br/><pre>");
	out.print(StringUtil.escapeXml(XmlUtil.getPrettyXmlString(res.getData())));
	out.println("</pre>");
}
%>

</body>

</html>

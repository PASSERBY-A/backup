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

try {
	if (request.getParameter("r1") != null) {
		ResourceRelation rr = new ResourceRelation();
		List<ResourceRelation> list = new ArrayList<ResourceRelation>();
		list.add(rr);
		rr.setItemId(Integer.parseInt(request.getParameter("r1")));
		rr.setItemId2(Integer.parseInt(request.getParameter("r2")));
		rr.setRelationId(request.getParameter("rel"));
		ServiceManager.getRelationUpdateService().updateResourceRelation(rr.getItemId(), list, 1);
		message = "操作成功.";
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

<form method="post" action="resRelation.jsp">
	id1: <input name="r1" size="20" />
	id2: <input name="r2" size="20" />
	relationId: <input name="rel" size="20" />
	<input type="submit" value="提交" />
</form>

</body>

</html>

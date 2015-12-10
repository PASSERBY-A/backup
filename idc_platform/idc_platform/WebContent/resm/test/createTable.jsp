<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.model.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<%
String message = "";

String modelId = request.getParameter("modelId");
try {
	if (modelId != null)
	{
		ModelUpdateService ms = (ModelUpdateService)ServiceManager.getModelUpdateService();
		Model m = ServiceManager.getModelService().getModelById(modelId);
		if (m == null)
			throw new Exception("ģ��" + modelId + "δ�ҵ���");
		if ("drop".equals(request.getParameter("type")))
			ms.dropTable(m);
		else
			ms.createTable(m);
		message = "�����ɹ�";
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

<form method="post" action="createTable.jsp">
	<input type="hidden" name="type" value="create" />
	modelId: <input name="modelId" value="<%=modelId%>" /> <input type="submit" value="����ģ����ϸ��" />
</form>

<form method="post" action="createTable.jsp">
	<input type="hidden" name="type" value="drop" />
	modelId: <input name="modelId" value="<%=modelId%>" /> <input type="submit" value="ɾ��ģ����ϸ��" />
</form>

</body>

</html>

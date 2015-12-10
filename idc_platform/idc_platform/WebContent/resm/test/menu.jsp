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
<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
</head>
<body>

<br/>
<a href="createTable.jsp" target="Content">创建/删除模型明细表</a><br/>
<a href="expression.jsp" target="Content">表达式测试</a><br/>
<a href="resource.jsp" target="Content">资源对象查询测试</a><br/>
<a href="createRes.jsp?modelId=server" target="Content">创建资源对象测试</a><br/>
<a href="attributeType.jsp" target="Content">属性类型测试</a><br/>
<a href="code.jsp" target="Content">资源代码测试</a><br/>
<a href="model.jsp" target="Content">资源模型测试</a><br/>
</body>

</html>

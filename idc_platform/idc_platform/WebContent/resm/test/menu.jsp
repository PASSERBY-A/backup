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
<a href="createTable.jsp" target="Content">����/ɾ��ģ����ϸ��</a><br/>
<a href="expression.jsp" target="Content">���ʽ����</a><br/>
<a href="resource.jsp" target="Content">��Դ�����ѯ����</a><br/>
<a href="createRes.jsp?modelId=server" target="Content">������Դ�������</a><br/>
<a href="attributeType.jsp" target="Content">�������Ͳ���</a><br/>
<a href="code.jsp" target="Content">��Դ�������</a><br/>
<a href="model.jsp" target="Content">��Դģ�Ͳ���</a><br/>
</body>

</html>

<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.resm.util.*"%>
<%@ page import="com.mchange.v2.c3p0.ComboPooledDataSource"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

ComboPooledDataSource dataSource = DbUtil.getDataSource();
if (request.getParameter("minPoolSize") != null)
	dataSource.setMinPoolSize(Integer.parseInt(request.getParameter("minPoolSize")));
if (request.getParameter("maxPoolSize") != null)
	dataSource.setMaxPoolSize(Integer.parseInt(request.getParameter("maxPoolSize")));
if (request.getParameter("acquireIncrement") != null)
	dataSource.setAcquireIncrement(Integer.parseInt(request.getParameter("acquireIncrement")));
if (request.getParameter("maxIdleTime") != null)
	dataSource.setMaxIdleTime(Integer.parseInt(request.getParameter("maxIdleTime")));
if (request.getParameter("unreturnedConnectionTimeout") != null) {
	dataSource.setDebugUnreturnedConnectionStackTraces(true);
	dataSource.setUnreturnedConnectionTimeout(Integer.parseInt(request.getParameter("unreturnedConnectionTimeout")));
}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>c3p0数据库连接信息</title>
</head>
<style>
body,td { font-size: 10.5pt }
</style>
<body>
	
<form method="post" action="c3p0.jsp">
c3p0 配置信息：<%=new Date()%><br/>
<table border=0>
<tr><td>最少保持连接数</td><td>minPoolSize</td><td><input name="minPoolSize" size="10" value="<%=dataSource.getMinPoolSize()%>" /></td></tr>
<tr><td>最大连接数</td><td>maxPoolSize</td><td><input name="maxPoolSize" size="10" value="<%=dataSource.getMaxPoolSize()%>" /></td></tr>
<tr><td>每次获取连接数</td><td>acquireIncrement</td><td><input name="acquireIncrement" size="10" value="<%=dataSource.getAcquireIncrement()%>" /></td></tr>
<tr><td>最长等待时间（秒）</td><td>maxIdleTime</td><td><input name="maxIdleTime" size="10" value="<%=dataSource.getMaxIdleTime()%>" /></td></tr>
<tr><td>未关连接自动释放时间（秒）</td><td>unreturnedConnectionTimeout</td><td><input name="unreturnedConnectionTimeout" size="10" value="<%=dataSource.getUnreturnedConnectionTimeout()%>" /></td></tr>
</tr>
</table>
<input type="submit" value="修改" /> <input type="button" value="刷新" onclick="location.href=location.href"/>
</form>

运行时信息：<br/>
getNumBusyConnections: <%=dataSource.getNumBusyConnections()%><br/>
getNumConnections: <%=dataSource.getNumConnections()%><br/>
getNumIdleConnections: <%=dataSource.getNumIdleConnections()%><br/>
getNumUnclosedOrphanedConnections: <%=dataSource.getNumUnclosedOrphanedConnections()%><br/>
getNumUserPools: <%=dataSource.getNumUserPools()%><br/>
<%
//dataSource.getConnection().getClass().getName()
%>
</pre>
</body>
</html>

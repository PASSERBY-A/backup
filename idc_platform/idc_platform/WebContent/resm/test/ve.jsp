<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.context.util.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.resource.*"%>
<%@ page import="org.apache.velocity.Template"%>
<%@ page import="org.apache.velocity.VelocityContext"%>
<%@ page import="org.apache.velocity.app.Velocity"%>
<%@ page import="org.apache.velocity.app.VelocityEngine"%>
<%@ page import="java.io.StringWriter"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
	<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="/extjs/ext-all.js"></script>
	<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="/extjs/ext-ext.js"></script>
	<script type="text/javascript" src="/extjs/json2.js"></script>
</head>

<body>


<%
Long l1 = System.currentTimeMillis();
%>


<%
List<ResourceObject> r = ServiceManager.getResourceService().getAllResources(1);
for (int i = 0; i < 15; i++)
			r.addAll(r);

	VelocityEngine ve = new VelocityEngine();
	ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, ContextUtil.getContextPath() + "/test/");
	ve.init();
	Template t = ve.getTemplate("1.vm", "GBK");
	/* create a context and add data */
	VelocityContext context = new VelocityContext();
	context.put("r", r);
	StringWriter writer = new StringWriter();
	t.merge(context, writer);
	/* show the World */
	out.println(writer.toString());
	
	out.println("this.objects end=" + (System.currentTimeMillis() - l1));
%>

</body>
</html>

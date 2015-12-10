<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.runtime.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>

<%@ include file="../../getUser.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=UTF-8");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
%>
<%
	OperationResult or = new OperationResult();
	String wfOid = request.getParameter("wfOid");
	String wfVer = request.getParameter("wfVer");
	String wfNode = request.getParameter("wfNode");

	String jars = request.getParameter("jars");
	String imports = request.getParameter("imports");
	String javasource = request.getParameter("javasource");
	String home = System.getProperty("catalina.home"); // EnvUtil.getEnv("CATALINA_HOME");
	String tempPath = home + "/temp";
	String j1 = "../webapps" + request.getContextPath() + "/WEB-INF/classes";
	if (jars != null && jars.length() > 0)
	{
		String[] te = jars.split(";");
		for (int i = 0; i < te.length; i++)
		{
			j1 += ":" + "../webapps" + request.getContextPath() + "/WEB-INF/lib/" + te[i];
		}
	}
	jars = j1;
	if (imports == null || imports.length() == 0)
		imports = "import com.hp.idc.itsm.task.*;import com.hp.idc.itsm.workflow.*;";
	else
		imports = "import com.hp.idc.itsm.task.*;import com.hp.idc.itsm.workflow.*;" + imports;
	String className = null;
try {
	com.hp.idc.itsm.runtime.Runtime r = new com.hp.idc.itsm.runtime.Runtime(tempPath);
	className = r.compile(imports, jars, javasource,"DynCode_"+wfOid+"_"+wfVer+"_"+wfNode);
	or.setMessage(className);
}catch(Exception e) {
		e.printStackTrace();
		or.setSuccess(false);
		or.setMessage(e.toString());
}
%>
<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>
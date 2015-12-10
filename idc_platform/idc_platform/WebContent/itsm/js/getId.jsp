<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=UTF-8");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
%>

<%
	String id = request.getParameter("id");
	OperationResult or = new OperationResult();
	if (id == null || id.length() == 0)
	{
		or.setSuccess(false);
		or.setMessage("id ·Ƿ¨");
	}
	try {
		or.setMessage("" + ItsmUtil.getSequence(id));
	}
	catch (Exception e) { 
		e.printStackTrace(); 
		or.setSuccess(false);
		or.setMessage(e.toString());
	}
%>
<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>

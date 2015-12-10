<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@page import="com.hp.idc.portal.mgr.SystemConfig"%>
<%@page import="com.hp.idc.portal.util.OperationResult"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>

<%
	response.setContentType("application/xml;charset=gbk");
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");

	String admin = ","+request.getParameter("admin")+",";
	
	OperationResult or = new OperationResult();
	
	try {
		SystemConfig.updateAttribute("admin",admin);
	} catch (Exception e) {
		e.printStackTrace();
		or.setSuccess(false);
		or.setMessage(e.toString());
	}
%>
<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>
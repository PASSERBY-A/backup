<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=gbk");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
%>

<%
	String userId = request.getParameter("userId");
	String factors = request.getParameter("fld_factors");
	String sendsms = request.getParameter("fld_sendSMS");
	int ss = 0;
	if (sendsms!=null)
		ss = 1;
	OperationResult or = new OperationResult();
	try {
		if(userId == null || userId.equals(""))
			throw new Exception("被代理人不能为空");
		
		PersonManager.setFactor(userId, factors,ss);
	}
	catch (Exception e) {
		e.printStackTrace();
		or.setSuccess(false);
		or.setMessage(e.toString());
	}

%>
<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>

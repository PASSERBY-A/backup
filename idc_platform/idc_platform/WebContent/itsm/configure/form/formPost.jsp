<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
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
	Object c[] = request.getParameterMap().keySet().toArray();
	//System.out.println("-");
	Map map = new HashMap();
	for (int i = 0; i < c.length; i++)
	{
		//System.out.println("" + c[i].toString() + ":" + request.getParameter(c[i].toString()).toString());
		map.put(c[i].toString(), request.getParameter(c[i].toString()).toString());
	}
	//System.out.println("-");
	OperationResult or = new OperationResult();
	try {
		or = FormManager.updateForm(map, "root");
	}
	catch (Exception e) { 
		e.printStackTrace(); 
		or.setSuccess(false);
		or.setMessage(e.toString());
	}

%>
<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>

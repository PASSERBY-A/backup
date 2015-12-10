<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
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
	//System.out.println((request.getParameter("fld_enableSMS"))+"==============");
	//System.out.println("-");
	Map map = new HashMap();
	for (int i = 0; i < c.length; i++)
	{
		System.out.println("" + c[i].toString() + ":" + request.getParameter(c[i].toString()).toString());
		map.put(c[i].toString(), request.getParameter(c[i].toString()).toString());
	}
	//System.out.println("-");
	OperationResult or = new OperationResult();
	String type = request.getParameter("type");
	try {
		if ("removeVersion".equals(type)) {
			String wfOid_ =  request.getParameter("wfOid");
			String wfVer_ =  request.getParameter("wfVer");
			int wfOid = (wfOid_ == null || wfOid_.equals(""))?-1:Integer.parseInt(wfOid_);
			int wfVer = (wfVer_ == null || wfVer_.equals(""))?-1:Integer.parseInt(wfVer_);
			or = WorkflowManager.removeWorkflowVersion(wfOid,wfVer, "root");
		} else {
			or = WorkflowManager.updateWorkflow(map, "root");
		}
	}
	catch (Exception e) {
		e.printStackTrace();
		or.setSuccess(false);
		or.setMessage(e.toString());
	}

%>
<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>

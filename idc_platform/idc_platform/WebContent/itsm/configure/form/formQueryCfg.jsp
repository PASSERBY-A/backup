<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="org.dom4j.*"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<%
int oid = -1;
if (request.getParameter("oid")!=null && !request.getParameter("oid").equals(""))
	oid = Integer.parseInt(request.getParameter("oid"));
FormInfo formInfo = FormManager.getFormByOid(oid);
if (formInfo != null){
	Document doc = formInfo.getXmlDoc();
	doc.getRootElement().setAttributeValue("name",formInfo.getName());
	out.print(doc.getRootElement().asXML());
}
%>
<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.json.*"%>

<%
String param = request.getParameter("param");
String url = request.getQueryString();
String returnUrl = url.substring(url.indexOf("returnURL=")+10);
session.setAttribute("returnURL",request.getQueryString().substring(url.indexOf("returnURL=")+10));
System.out.println(returnUrl);
session.setAttribute("returnURL","http://bomctest.yn.cmcc:9090/cmdb/");
System.out.println(param);
//JSONObject js = new JSONObject(param);

//response.sendRedirect("taskInfo.jsp?origin=ITSM&taskOid="+js.getString("taskOid")+"&taskDataId="+js.getString("taskDataId")+"&wfOid="+js.getString("wfOid")+"&wfVer="+js.getString("wfVer"));
%>
<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.mgr.TopData"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Expires","0");
	response.setContentType("application/json;charset=UTF-8");
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
%>
<%
	String smsUsers = request.getParameter("smsUsers");
	String smsContent = request.getParameter("smsContent");
	TopData topData = (TopData)ContextUtil.getBean("topData");
	int ret = topData.sendSms(smsUsers,smsContent,"portal");
	if(ret>0){
		out.println("{\"success\":\"true\",\"data\":{\"msg\":\"短信发送成功\"}}");
	}else{
		out.println("{\"success\":\"faile\",\"data\":{\"msg\":\"短信发送失败\"}}");
	}
%>

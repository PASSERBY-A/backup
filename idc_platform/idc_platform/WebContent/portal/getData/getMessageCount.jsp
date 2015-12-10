<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.message.Message"%>
<%@page import="com.hp.idc.portal.mgr.MessageMgr"%>
<%@page import="com.hp.idc.json.*"%>
<%@include file="../getUser.jsp" %>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Expires","0");
	response.setContentType("application/json;charset=UTF-8");
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	
	boolean result = false;
	int count = 0;
	try{
		count = MessageMgr.getUnreadCount(userId);
		result = true;
	}catch(Exception e){
		result = false;
		e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",count);
	out.print(obj.toString());
%>

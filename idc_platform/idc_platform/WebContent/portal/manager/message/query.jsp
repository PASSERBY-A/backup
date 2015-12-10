<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.*"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.json.*"%>
<%@page import="com.hp.idc.portal.message.*"%>
<%@include file="../../getUser.jsp" %>

<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Expires","0");
	response.setContentType("application/json;charset=UTF-8");
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");

	String statusStr = request.getParameter("status");
	int status = -1;
	try{
		status = Integer.parseInt(statusStr);
	}catch(NumberFormatException e){
		status = -1;
	}
	List<Message> list = MessageMgr.getBeanByUserId(userId,status);
	
	Message bean = null;
	
	String limit_s = request.getParameter("limit");
	String start_s = request.getParameter("start");
	int limit = 9;
	if (limit_s != null)
		limit = Integer.parseInt(limit_s);
	int start = 0;
	if (start_s != null)
		start = Integer.parseInt(start_s);
	
	JSONObject obj = new JSONObject();
	JSONArray arr = new JSONArray();
	JSONObject temp = null;
	obj.put("totalCount", list.size());
	for (int i = 0; i <  list.size(); i++) {
		bean = list.get(i);
		temp = new JSONObject();

		temp.put("oid", bean.getOid());
		temp.put("title", bean.getTitle());
		temp.put("content", bean.getContent());
		temp.put("url", bean.getUrl());
		temp.put("userId", bean.getUserId());
		temp.put("status", bean.getStatus());
		temp.put("module", bean.getModule());
		
		arr.put(temp);
	}
	obj.put("items", arr);
	out.println(obj.toString());
%>
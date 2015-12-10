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
	
	List<Message> list = MessageMgr.getBeanByUserId(userId,1);
	List<Message> readArr = new ArrayList<Message>();
	Message bean = null;
	
	String limit_s = request.getParameter("limit");
	String start_s = request.getParameter("start");
	int limit = 5;
	if (limit_s != null)
		limit = Integer.parseInt(limit_s);
	int start = 0;
	if (start_s != null)
		start = Integer.parseInt(start_s);
	
	JSONArray arr = new JSONArray();
	JSONObject temp = null;
	for (int i = 0; i < limit && i < list.size(); i++) {
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
		readArr.add(bean);
	}
	MessageMgr.updateStatus(readArr,2);
	out.println(arr);
%>

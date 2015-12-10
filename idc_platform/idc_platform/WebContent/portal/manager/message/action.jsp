<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.json.JSONObject"%>
<%@page import="com.hp.idc.portal.mgr.MessageMgr"%>
<%@include file="../../getUser.jsp" %>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Expires","0");
	response.setContentType("application/json;charset=UTF-8");
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
%>
<%
	String action = request.getParameter("action");
	String resultStr = "未知错误";
	boolean result = false;
	try{
		if("delete".equals(action)){
			//删除操作处理
			String oids = request.getParameter("oids");
			List<String> list = Arrays.asList(oids.split(","));
			int ret = MessageMgr.deleteMessage(list);
			if(ret>0)
				result = true;
			if(result)
				resultStr="消息删除成功";
			else
				resultStr="消息删除失败";
		}
	}catch(Exception e){
		resultStr=e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	out.print(obj.toString());
%>
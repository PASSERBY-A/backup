<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.*"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.bean.*"%>
<%@page import="com.hp.idc.json.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.File"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		FavoritesMgr mgr = (FavoritesMgr)ContextUtil.getBean("favoritesMgr");
		if("commit".equals(action)){
			
			String menuIds = request.getParameter("menuIds");
			
			menuIds = ","+menuIds.replaceAll("=on","")+",";
			menuIds = menuIds.replaceAll("&",",");
			
			Favorites bean = new Favorites();
			bean.setUserId(userId);
			bean.setMenuIds(menuIds);
			result=mgr.update(bean);
			if(result){
				resultStr="信息提交成功";
			}else{
				resultStr="信息提交失败";
			}
		}
	}catch(Exception e){
		resultStr = e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	out.print(obj.toString());
%>
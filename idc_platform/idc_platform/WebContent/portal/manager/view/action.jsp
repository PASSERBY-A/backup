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

	String action = request.getParameter("action");
	String resultStr = "未知错误";
	boolean result = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ViewMgr mgr = (ViewMgr)ContextUtil.getBean("viewMgr");
	try{
		if("add".equals(action)){
			String name = request.getParameter("name");
			String layoutId = request.getParameter("layoutId");
			String nodes = request.getParameter("nodes");
			
			nodes=nodes==null?"":nodes;
			
			View bean = new View();
			bean.setName(name);
			bean.setLayoutId(layoutId);
			bean.setNodes(nodes);
			bean.setCreator(userId);
			bean.setCreateTime(new java.util.Date());
			result=mgr.add(bean);
			if(result){
				resultStr="新增视图成功";
			}else{
				resultStr="新增视图失败";
			}
		}
		if("update".equals(action)){
			String oid = request.getParameter("oid");
			String name = request.getParameter("name");
			String layoutId = request.getParameter("layoutId");
			String nodes = request.getParameter("nodes");
			
			View bean = mgr.getBeanById(oid);
			if(name!=null)
				bean.setName(name);
			if(layoutId!=null)
				bean.setLayoutId(layoutId);
			if(nodes!=null)
				bean.setNodes(nodes);
			
			result=mgr.update(bean);
			if(result){
				resultStr="编辑视图成功";
			}else{
				resultStr="编辑视图失败";
			}
		}
		if("delete".equals(action)){
			String oid = request.getParameter("oid");
			result = mgr.delete(oid);
			if(result)
				resultStr="视图删除成功";
			else
				resultStr="视图删除失败";
		}
	}catch(Exception e){
		resultStr=e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	out.print(obj.toString());
%>
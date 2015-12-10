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
	try{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LayoutMgr mgr = (LayoutMgr)ContextUtil.getBean("layoutMgr");
		if("add".equals(action)){
			
			String name = request.getParameter("name");
			String path = request.getParameter("path");
			int areanum = Integer.parseInt(request.getParameter("areanum"));
			
			name=name==null?"":name;
			path=path==null?"":path;
			
			Layout bean = new Layout();
			bean.setName(name);
			bean.setPath(path);
			bean.setAreaNum(areanum);
			result=mgr.add(bean,userId);
			if(result){
				resultStr="新增布局成功";
			}else{
				resultStr="新增布局失败";
			}
		}
		if("update".equals(action)){
			String oid = request.getParameter("oid");
			String name = request.getParameter("name");
			String path = request.getParameter("path");
			int areanum = Integer.parseInt(request.getParameter("areanum"));
			
			name=name==null?"":name;
			path=path==null?"":path;
			
			Layout bean = mgr.getBeanById(oid);
			bean.setName(name);
			bean.setPath(path);
			bean.setAreaNum(areanum);
			result=mgr.update(bean,userId);
			if(result){
				resultStr="编辑布局成功";
			}else{
				resultStr="编辑布局失败";
			}
		}
		if("delete".equals(action)){
			String oid = request.getParameter("oid");
			result = mgr.delete(oid,userId);
			if(result)
				resultStr="布局删除成功";
			else
				resultStr="布局删除失败";
		}
	}catch(Exception e){
		resultStr = e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	out.print(obj.toString());
%>
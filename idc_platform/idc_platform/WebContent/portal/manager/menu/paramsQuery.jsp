<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.*"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.bean.*"%>
<%@page import="com.hp.idc.json.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
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
	String mode = request.getParameter("mode");
	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
	List<Menu> list = new ArrayList<Menu>();
	if("all".equals(mode)&&mode!=null){
		list = mgr.getSubBeans(); 
	}else if("sub".equals(mode)&&mode!=null){
		String type = request.getParameter("type");
		list = mgr.getBeanByFilter(userId,type); 	
	}
	
	Menu bean = null;
	
	/*String limit_s = request.getParameter("limit");
	String start_s = request.getParameter("start");
	int limit = 9;
	if (limit_s != null)
		limit = Integer.parseInt(limit_s);
	int start = 0;
	if (start_s != null)
		start = Integer.parseInt(start_s);*/
	
	JSONObject obj = new JSONObject();
	JSONArray arr = new JSONArray();
	JSONObject temp = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	obj.put("totalCount", list.size());
	for (int i = 0; i <  list.size(); i++) {
		bean = list.get(i);
		MenuParams mp = MenuParamsMgr.getBeanById(userId,Integer.parseInt(bean.getOid()));
		/**
		
		х╗очеп╤о
		
		**/
		temp = new JSONObject();
		
		temp.put("oid", bean.getOid());
		temp.put("title", bean.getTitle());
		temp.put("url", bean.getUrl());
		temp.put("type", bean.getType());
		temp.put("alt", bean.getAlt());
		if(mp!=null)
			temp.put("params", mp.getParams());
		arr.put(temp);
	}
	obj.put("items", arr);
	out.println(obj.toString());
%>
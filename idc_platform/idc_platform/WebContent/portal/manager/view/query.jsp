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

	String type = request.getParameter("type");
	ViewMgr mgr = (ViewMgr)ContextUtil.getBean("viewMgr");
	LayoutMgr layoutMgr = (LayoutMgr)ContextUtil.getBean("layoutMgr");
	List<View> list = mgr.getBeanByUserId(userId);
	
	View bean = null;
	
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
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	obj.put("totalCount", list.size());
	for (int i = 0; i <  list.size(); i++) {
		bean = list.get(i);
		temp = new JSONObject();
		
		temp.put("oid", bean.getOid());
		temp.put("name", bean.getName());
		temp.put("layout", layoutMgr.getBeanById(bean.getLayoutId()).getName());
		temp.put("creator", bean.getCreator());
		temp.put("createTime", sdf.format(bean.getCreateTime()));
		
		
		arr.put(temp);
	}
	obj.put("items", arr);
	out.println(obj.toString());
%>
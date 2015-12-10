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
	WorkPlanMgr mgr = (WorkPlanMgr)ContextUtil.getBean("workPlanMgr");
	List<WorkPlan> list = null;
	
	if("all".equals(mode)){
		list = mgr.getBeanByUserId(userId); 
	} else if("filter".equals(mode)){
		String type = request.getParameter("type");
		String keyWords = request.getParameter("keyWords");
		list = mgr.getBeanByFilter(userId,type,keyWords);
	}
	
	WorkPlan bean = null;
	
	String limit_s = request.getParameter("limit");
	String start_s = request.getParameter("start");
	int limit = 20;
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
	for (int i = start; i < start + limit && i < list.size(); i++) {
		bean = list.get(i);
		temp = new JSONObject();
		
		temp.put("oid", bean.getOid());
		temp.put("userId", bean.getUserId());
		String title = bean.getTitle();
		title=title.replaceAll(" ","&nbsp;");
		temp.put("title", title);
		temp.put("type", bean.getType());
		if(bean.getFinishTime()==null&&(bean.getPlanTime().getTime()-new java.util.Date().getTime()<3*24*60*60*1000)&&(bean.getPlanTime().getTime()-new java.util.Date().getTime()>0))
			temp.put("status","临近");
		else if(bean.getFinishTime()==null&&bean.getPlanTime().getTime()-new java.util.Date().getTime()<0)
			temp.put("status","滞后");
		else if(bean.getFinishTime()!=null)
			temp.put("status","完成");
		else
			temp.put("status","未完成");
		temp.put("planTime", sdf.format(bean.getPlanTime()));
		if(bean.getFinishTime()!=null)
			temp.put("finishTime", sdf.format(bean.getFinishTime()));
		else
			temp.put("finishTime", "--");
		temp.put("createTime", sdf.format(bean.getCreateTime()));
		temp.put("description", bean.getDescription());
		arr.put(temp);
	}
	obj.put("items", arr);
	out.println(obj.toString());
%>
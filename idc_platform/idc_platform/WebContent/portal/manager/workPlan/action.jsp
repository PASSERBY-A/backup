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
		WorkPlanMgr mgr = (WorkPlanMgr)ContextUtil.getBean("workPlanMgr");
		if("add".equals(action)){
			String title = request.getParameter("title");
			String type = request.getParameter("type");
			String planTime = request.getParameter("planTime");
			String desc = request.getParameter("desc");
			desc=desc==null?"":desc;
			WorkPlan bean = new WorkPlan();
			bean.setTitle(title);
			bean.setType(type);
			bean.setPlanTime(sdf.parse(planTime));
			bean.setUserId(userId);
			bean.setCreateTime(new java.util.Date());
			bean.setDescription(desc);
			result=mgr.add(bean);
			if(result){
				resultStr="新增工作计划成功";
			}else{
				resultStr="新增工作计划失败";
			}
		}
		if("update".equals(action)){
			String oid = request.getParameter("oid");
			String title = request.getParameter("title");
			String type = request.getParameter("type");
			String planTime = request.getParameter("planTime");
			String isFinish = request.getParameter("isFinish");
			String desc = request.getParameter("desc");
			WorkPlan bean = mgr.getBeanById(oid);
			if(title!=null)
				bean.setTitle(title);
			if(type!=null)
				bean.setType(type);
			if(planTime!=null)
				bean.setPlanTime(sdf.parse(planTime));
			if(null!=isFinish&&"1".equals(isFinish)){
				bean.setFinishTime(new java.util.Date());
			}
			if(desc!=null)
				bean.setDescription(desc);
			result=mgr.update(bean);
			if(result){
				resultStr="编辑工作计划成功";
			}else{
				resultStr="编辑工作计划失败";
			}
		}
		if("delete".equals(action)){
			String oid = request.getParameter("oid");
			result = mgr.delete(oid);
			if(result)
				resultStr="文档删除成功";
			else
				resultStr="文档删除失败";
		}
	}catch(Exception e){
		resultStr=e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	out.print(obj.toString());
%>
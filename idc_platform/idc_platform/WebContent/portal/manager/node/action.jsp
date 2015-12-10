<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.Date"%>
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
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ViewNodeMgr mgr = (ViewNodeMgr)ContextUtil.getBean("viewNodeMgr");
	try{
		if("add".equals(action)){
			//增加操作处理
			String name = request.getParameter("name");//名称
			String backColor = request.getParameter("backColor");//背景色
			String foreColor = request.getParameter("foreColor");//前景色
			String width = request.getParameter("width");//默认宽度
			String height = request.getParameter("height");//默认高度
			String advProp = request.getParameter("advProp");//附加属性XML
			String role = request.getParameter("role");//权限信息
			String path = request.getParameter("path");//模版路径
			String type = request.getParameter("type");//节点类型（1、普通文字类，2、表格类，3、图表类）
			
			ViewNode bean = new ViewNode();
			bean.setName(name);
			bean.setBackColor(backColor);
			bean.setForeColor(foreColor);
			bean.setWidth(width);
			bean.setHeight(height);
			bean.setAdvProp(advProp);
			bean.setCreateTime(new Date());
			bean.setCreator(userId);
			bean.setPath(path);
			bean.setType(type);
			if(role!=null){
				bean.setRole(role);				
			}
			result=mgr.add(bean);
			if(result){
				resultStr="新增节点成功";
			}else{
				resultStr="新增节点失败";
			}
		}
		if("update".equals(action)){
			//修改操作处理
			String oid = request.getParameter("oid");//oid
			String name = request.getParameter("name");//名称
			String backColor = request.getParameter("backColor");//背景色
			String foreColor = request.getParameter("foreColor");//前景色
			String width = request.getParameter("width");//默认宽度
			String height = request.getParameter("height");//默认高度
			String advProp = request.getParameter("advProp");//附加属性XML
			String role = request.getParameter("role");//权限信息
			String path = request.getParameter("path");//模版路径
			String type = request.getParameter("type");//节点类型（1、普通文字类，2、表格类，3、图表类）
			
			ViewNode bean = mgr.getBeanById(oid);
			bean.setName(name);
			bean.setBackColor(backColor);
			bean.setForeColor(foreColor);
			bean.setWidth(width);
			bean.setHeight(height);
			bean.setAdvProp(advProp);
			bean.setCreateTime(new Date());
			bean.setCreator(userId);
			bean.setPath(path);
			bean.setType(type);
			if(role!=null&&!"".equals(role)){
				bean.setRole(role);
			}
			result=mgr.update(bean);
			if(result){
				resultStr="编辑节点成功";
			}else{
				resultStr="编辑节点失败";
			}
		}
		if("delete".equals(action)){
			//删除操作处理
			String oid = request.getParameter("oid");
			result = mgr.delete(oid);
			if(result)
				resultStr="节点删除成功";
			else
				resultStr="节点删除失败";
		}
	}catch(Exception e){
		resultStr=e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	out.print(obj.toString());
%>
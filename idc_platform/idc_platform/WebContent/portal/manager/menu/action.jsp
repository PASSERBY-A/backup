<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.Date"%>
<%@page import="com.hp.idc.portal.mgr.*"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.bean.*"%>
<%@page import="com.hp.idc.json.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.File"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
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
		MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
		if("add".equals(action)){
			
			String title = request.getParameter("title");
			String type = request.getParameter("type");
			String url = request.getParameter("url");
			String alt = request.getParameter("alt");
			String role = request.getParameter("role");
			String _orderno = request.getParameter("orderno");
			int orderno = 0;
			if(_orderno != null){
				orderno = Integer.valueOf(_orderno);
			}
			boolean valid=mgr.checkOrder(Integer.parseInt(type),orderno,0);
			if(!valid){
				resultStr="新增菜单失败，排序号重复，请重新设置排序号。";
			}
			else{
				alt=alt==null?"":alt;
				role=role==null?"":role;
				
				Menu bean = new Menu();
				bean.setTitle(title);
				bean.setType(type);
				bean.setUrl(url);
				bean.setAlt(alt);
				bean.setRole(role);
				bean.setCreateTime(new Date());
				bean.setCreator(userId);
				bean.setOrderNo(orderno);
				result=mgr.add(bean,userId);
				if(result){
					resultStr="新增菜单成功";
				}else{
					resultStr="新增菜单失败";
				}
			}
		}
		if("update".equals(action)){
			String oid = request.getParameter("oid");
			String title = request.getParameter("title");

			String type = request.getParameter("type");
			String url = request.getParameter("url");
			String alt = request.getParameter("alt");
			String role = request.getParameter("role");
			String _orderno = request.getParameter("orderno");
			int orderno = 0;
			try{
				if(_orderno != null){
					orderno = Integer.valueOf(_orderno);
				}
			
			boolean valid=mgr.checkOrder(Integer.parseInt(type),orderno,Integer.parseInt(oid));
			if(!valid){
				resultStr="编辑菜单失败，排序号重复，请重新设置排序号。";
			}else{
				alt=alt==null?"":alt;
				
				Menu bean = mgr.getBeanById(oid);
				bean.setTitle(title);
				bean.setType(type);
				bean.setUrl(url);
				bean.setAlt(alt);
				
				//if(role!=null&&!"".equals(role))
					bean.setRole(role);
				bean.setOrderNo(orderno);
				result=mgr.update(bean,userId);
				if(result){
					resultStr="编辑菜单成功";
				}else{
					resultStr="编辑菜单失败";
				}
			
			}
			}catch(Exception e){
				e.printStackTrace();			
			}
		}
		if("delete".equals(action)){
			String oid = request.getParameter("oid");
			result = mgr.delete(oid,userId);
			if(result)
				resultStr="菜单删除成功";
			else
				resultStr="菜单删除失败";
		}
		if("paramEdit".equals(action)){
			//菜单参数设定
			String menuIdStr = request.getParameter("menuId");
			String params = request.getParameter("params");
			int menuId = Integer.parseInt(menuIdStr);
			MenuParams mp = MenuParamsMgr.getBeanById(userId,menuId);
			if(mp == null){
				mp=new MenuParams();
				mp.setUserId(userId);
				mp.setMenuId(menuId);
				mp.setParams(params);
				result = MenuParamsMgr.add(mp);
			}else{
				mp.setUserId(userId);
				mp.setMenuId(menuId);
				mp.setParams(params);
				result = MenuParamsMgr.update(mp);
			}
			if(result)
				resultStr="菜单参数设定成功";
			else
				resultStr="菜单参数设定失败";
		}
	}catch(Exception e){
		resultStr = e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	System.out.println(obj.toString());
	out.print(obj.toString());
%>
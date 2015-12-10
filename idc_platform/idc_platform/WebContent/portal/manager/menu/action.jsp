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
	String resultStr = "δ֪����";
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
				resultStr="�����˵�ʧ�ܣ�������ظ�����������������š�";
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
					resultStr="�����˵��ɹ�";
				}else{
					resultStr="�����˵�ʧ��";
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
				resultStr="�༭�˵�ʧ�ܣ�������ظ�����������������š�";
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
					resultStr="�༭�˵��ɹ�";
				}else{
					resultStr="�༭�˵�ʧ��";
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
				resultStr="�˵�ɾ���ɹ�";
			else
				resultStr="�˵�ɾ��ʧ��";
		}
		if("paramEdit".equals(action)){
			//�˵������趨
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
				resultStr="�˵������趨�ɹ�";
			else
				resultStr="�˵������趨ʧ��";
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
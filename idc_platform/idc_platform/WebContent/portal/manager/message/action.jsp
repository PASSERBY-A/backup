<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.json.JSONObject"%>
<%@page import="com.hp.idc.portal.mgr.MessageMgr"%>
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
		if("delete".equals(action)){
			//ɾ����������
			String oids = request.getParameter("oids");
			List<String> list = Arrays.asList(oids.split(","));
			int ret = MessageMgr.deleteMessage(list);
			if(ret>0)
				result = true;
			if(result)
				resultStr="��Ϣɾ���ɹ�";
			else
				resultStr="��Ϣɾ��ʧ��";
		}
	}catch(Exception e){
		resultStr=e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	out.print(obj.toString());
%>
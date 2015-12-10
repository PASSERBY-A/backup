<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.*"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.bean.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="java.io.File"%>
<%@page import="com.hp.idc.json.JSONObject"%>
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
	DocumentMgr mgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
	boolean result = false;
	try{
		if("update".equals(action)){
			String oid = request.getParameter("oid");
			String role = request.getParameter("role");
			role=(role==null?"":role);
			Document d = mgr.getBeanById(oid);
			d.setRole(role);
			result=mgr.update(d);
			if(result){
				resultStr="�ĵ�����ɹ�";
			}else{
				resultStr="�ĵ�����ʧ��";
			}
		}
		if("delete".equals(action)){
			String oid = request.getParameter("oid");
			Document d = mgr.getBeanById(oid);
			if(3401 == d.getCreator()){
				File file = new File(d.getFilepath()+d.getFilename());
				if(file.exists()){
					result = file.delete();
				}else{
					result = true;
				}
				if(result){
					result = mgr.delete(oid);
					if(result)
						resultStr="�ĵ�ɾ���ɹ�";
					else
						resultStr="�ĵ�ɾ��ʧ��";
				}
			}else{
				result = false;
				resultStr = "�������ĵ������ߣ���Ȩɾ�����ĵ�";
			}
		}
	}catch(Exception e){
		resultStr = e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	out.print(obj.toString());
%>
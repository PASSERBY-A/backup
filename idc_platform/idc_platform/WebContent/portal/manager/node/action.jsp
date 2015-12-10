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
	String resultStr = "δ֪����";
	boolean result = false;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ViewNodeMgr mgr = (ViewNodeMgr)ContextUtil.getBean("viewNodeMgr");
	try{
		if("add".equals(action)){
			//���Ӳ�������
			String name = request.getParameter("name");//����
			String backColor = request.getParameter("backColor");//����ɫ
			String foreColor = request.getParameter("foreColor");//ǰ��ɫ
			String width = request.getParameter("width");//Ĭ�Ͽ��
			String height = request.getParameter("height");//Ĭ�ϸ߶�
			String advProp = request.getParameter("advProp");//��������XML
			String role = request.getParameter("role");//Ȩ����Ϣ
			String path = request.getParameter("path");//ģ��·��
			String type = request.getParameter("type");//�ڵ����ͣ�1����ͨ�����࣬2������࣬3��ͼ���ࣩ
			
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
				resultStr="�����ڵ�ɹ�";
			}else{
				resultStr="�����ڵ�ʧ��";
			}
		}
		if("update".equals(action)){
			//�޸Ĳ�������
			String oid = request.getParameter("oid");//oid
			String name = request.getParameter("name");//����
			String backColor = request.getParameter("backColor");//����ɫ
			String foreColor = request.getParameter("foreColor");//ǰ��ɫ
			String width = request.getParameter("width");//Ĭ�Ͽ��
			String height = request.getParameter("height");//Ĭ�ϸ߶�
			String advProp = request.getParameter("advProp");//��������XML
			String role = request.getParameter("role");//Ȩ����Ϣ
			String path = request.getParameter("path");//ģ��·��
			String type = request.getParameter("type");//�ڵ����ͣ�1����ͨ�����࣬2������࣬3��ͼ���ࣩ
			
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
				resultStr="�༭�ڵ�ɹ�";
			}else{
				resultStr="�༭�ڵ�ʧ��";
			}
		}
		if("delete".equals(action)){
			//ɾ����������
			String oid = request.getParameter("oid");
			result = mgr.delete(oid);
			if(result)
				resultStr="�ڵ�ɾ���ɹ�";
			else
				resultStr="�ڵ�ɾ��ʧ��";
		}
	}catch(Exception e){
		resultStr=e.getMessage();
	}
	JSONObject obj = new JSONObject();
	obj.put("success",result);
	obj.put("msg",resultStr);
	out.print(obj.toString());
%>
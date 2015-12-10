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
	List<Document> list = null; 
	DocumentMgr mgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
	if("all".equals(mode))
		list = mgr.getBeanByUserId(userId);
	else if("filter".equals(mode)){
		String type = request.getParameter("type");
		String keyWords = request.getParameter("keyWords");
		list = mgr.getBeanByFilter(userId,type,keyWords);
	}
	Document document = null;
	
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
	for (int i = start; i < start + limit && i < list.size(); i++) {
		document = list.get(i);
		temp = new JSONObject();
		
		temp.put("oid", document.getOid());
		temp.put("name", document.getName());
		DecimalFormat df=new DecimalFormat("0.00"); //保留两位小数
		double size = Double.parseDouble(document.getFilesize());
		String sizeVal = "";
		if(size>1000&&size<1000*1024){
			sizeVal = df.format(size/1000)+"KB";
		}else if(size>1000*1024){
			sizeVal = df.format(size/1024/1024)+"MB";
		}else{
			sizeVal = df.format(size)+"B";
		}
		temp.put("filesize", sizeVal);
		temp.put("filetype", document.getFiletype());
		temp.put("filepath", document.getFilepath());
		temp.put("filename", document.getFilename());
		temp.put("creator", document.getCreator());
		temp.put("uploadTime", sdf.format(document.getUpdateTime()));
		temp.put("createTime", sdf.format(document.getCreateTime()));
		arr.put(temp);
	}
	obj.put("items", arr);
	out.println(obj.toString());
%>
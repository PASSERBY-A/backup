<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.List"%>
<%@page import="com.hp.idc.portal.mgr.LayoutMgr"%>
<%@page import="com.hp.idc.portal.bean.Layout"%>
<%@page import="com.hp.idc.json.*"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
	LayoutMgr mgr = (LayoutMgr)ContextUtil.getBean("layoutMgr");
	List<Layout> list = mgr.getBeans();
	JSONObject tmp = null;
	JSONArray arr = new JSONArray();
	Layout bean = null;
	for(int i=0;i<list.size();i++){
		tmp = new JSONObject();
		bean = list.get(i);
		tmp.put("oid",bean.getOid());
		tmp.put("name",bean.getName());
		tmp.put("url","images/thumbs/"+bean.getName()+".png");
		arr.put(tmp);
	}
	JSONObject o = new JSONObject();
	o.put("images",arr);
	out.print(o.toString());
%>

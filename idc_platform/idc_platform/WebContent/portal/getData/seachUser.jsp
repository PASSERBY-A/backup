<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.mgr.TopData"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Expires","0");
	response.setContentType("text/html;charset=UTF-8");
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
%>
<%
	String keyWord = request.getParameter("keyWord");
	TopData topData = (TopData)ContextUtil.getBean("topData");
	List<Map<String, String>> list = topData.seachUserByKeyWord(keyWord);
	if(null==list||list.size()<1){
		out.print("<div><p>没有查到相关人员信息</p><p>请重新查询！！</p></div>");
	}else{
		for(int i=0;i<3&&i<list.size();i++){
			Map<String, String> map = list.get(i);
			out.print("<img src='style/img/icon13.jpg'/>");
			out.print("<div>");
			out.print("<p>"+map.get("name")+"("+map.get("id")+"),电话："+StringUtil.removeNull(map.get("mobile"))+"</p>");
			out.print("<p>邮箱:"+StringUtil.removeNull(map.get("email"))+"</p>");
			out.print("</div>");
		}
	}
%>

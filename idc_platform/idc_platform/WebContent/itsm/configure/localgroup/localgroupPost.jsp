<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.authorization.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=gbk");
response.setCharacterEncoding("UTF-8");
request.setCharacterEncoding("UTF-8");
%>
<%@ include file="../../getUser.jsp"%>

<%
	Exception e = null;
	try {
		String operType = request.getParameter("type");
		if (operType == null)
			operType = "";
			
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String wfOid = request.getParameter("lg_wf");
		String status = request.getParameter("status");
		String lgUser = request.getParameter("fld_lgUser");


		if (operType.equals("add")) {
			LocalgroupInfo info = new LocalgroupInfo();
			info.setId(id);
			info.setName(name);
			info.setWfOid(wfOid);
			info.setStatus(Integer.parseInt(status));
			LocalgroupManager.addLocalgroup(info, userId);
		} else if (operType.equals("edit")) {
			LocalgroupInfo info = new LocalgroupInfo();
			info.setId(id);
			info.setName(name);
			info.setWfOid(wfOid);
			info.setStatus(Integer.parseInt(status));
			LocalgroupManager.updateLocalgroup(info, userId);
		} else if (operType.equals("addRelations")) {
			String[] users = lgUser.split(",");
			for (int i = 0; i < users.length; i++) {
				if (users[i]!=null && !users[i].equals(""))
					LocalgroupManager.addRelation(id,users[i], userId);
			}
		} else if (operType.equals("deleteRelations")) {
			LocalgroupManager.deleteRelation(id,lgUser, userId);
		}
	}catch(Exception ex) {
		e = ex;
		ex.printStackTrace();
	}
%>

<response success='<%=(e==null?"true":"false")%>'><%=(e==null?"您的请求已处理":StringUtil.escapeXml(e.toString()))%></response>
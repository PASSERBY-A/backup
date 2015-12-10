<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.authorization.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=UTF-8");
response.setCharacterEncoding("UTF-8");
request.setCharacterEncoding("UTF-8");
%>
<%@ include file="../../getUser.jsp"%>

	<%
	Exception e = null;
	try {
		String operType = request.getParameter("operType");
		if (operType == null)
			operType = "";
			
			

		String menuId = request.getParameter("menu_id");
		String menu_name = request.getParameter("menu_name");
		String menu_rule = request.getParameter("menu_rule");
		String menu_url = request.getParameter("menu_url");
		String menu_script = request.getParameter("menu_script");
		String menu_parent = request.getParameter("menu_parent");
		String menu_leaf = request.getParameter("menu_leaf");

		int menu_index = -1;
		if (request.getParameter("menu_index")!=null && !request.getParameter("menu_index").equals(""))
			menu_index = Integer.parseInt(request.getParameter("menu_index"));

		

			MenuInfo info = new MenuInfo();
			info.setId(menuId);
			info.setName(menu_name);
			info.setParentId(menu_parent);
			info.setDisplayIndex(menu_index);
			info.setRuleStr(menu_rule);
			if (menu_leaf!=null &&	!menu_leaf.equals(""))
				info.setLeaf(true);
			else
				info.setLeaf(false);
			info.setHref(menu_url);
			info.setScript(menu_script);

			if (operType.equals("add")) {
				MenuManager.addMenu(info, userId);
			} else if (operType.equals("edit")) {
				MenuManager.updateMenu(info, userId);
			}
		}catch(Exception ex) {
			e = ex;
			ex.printStackTrace();
		}
	%>

<response success='<%=(e==null?"true":"false")%>'><%=(e==null?"您的请求已经处理":StringUtil.escapeXml(e.toString()))%></response>
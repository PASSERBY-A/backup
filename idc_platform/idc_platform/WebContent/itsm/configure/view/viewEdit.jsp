<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=gbk");
response.setCharacterEncoding("gbk");
request.setCharacterEncoding("UTF-8");
%>
<%@ include file="../../getUser.jsp"%>

	<%

		String operType = request.getParameter("operType");
		if (operType == null)
			operType = "";
		String refreshObj = request.getParameter("refreshObj");

		int viewOid = -1;
		if (request.getParameter("viewOid")!=null && !request.getParameter("viewOid").equals(""))
			viewOid = Integer.parseInt(request.getParameter("viewOid"));

		String name = request.getParameter("view_name");
		String[] applyTo = request.getParameterValues("view_applyto");
		String applyToS = "";
		if (applyTo!=null) {
			for (int i = 0; i < applyTo.length; i++) {
				if (applyTo[i]!=null && !applyTo[i].equals(""))
					applyToS += applyTo[i]+",";
			}
		}
		if (applyTo == null || applyTo.length == 0)
			applyToS = "all";
		if (applyToS.startsWith(","))
			applyToS = applyToS.substring(1,applyToS.length());
		if (applyToS.endsWith(","))
			applyToS = applyToS.substring(0,applyToS.length()-1);
		String vfield_xml = request.getParameter("vfield_xml");


		Exception e = null;
		try {

			ViewInfo info = new ViewInfo();
			info.setName(name);
			info.setApplyTo(applyToS);
			info.setConfigure(vfield_xml);
			if (operType.equals("add")) {
				ViewManager.addView(info, userId);
			} else if (operType.equals("edit")) {
				info.setOid(viewOid);
				ViewManager.updateView(info, userId);
			} else if (operType.equals("delete")) {
				info.setOid(viewOid);
				if (ViewManager.isModifiable(viewOid))
					ViewManager.deleteView(viewOid, userId);
			}
		}catch(Exception ex) {
			e = ex;
			ex.printStackTrace();
		}

	%>

<response success='<%=(e==null?"true":"false")%>'><%=(e==null?"您的请求已经处理":StringUtil.escapeXml(e.toString()))%></response>
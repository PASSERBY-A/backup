<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.model.*"%>
<%@ page import="com.hp.idc.resm.expression.*"%>
<%@ page import="com.hp.idc.resm.resource.*"%>
<%@ page import="com.hp.idc.resm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

String id = request.getParameter("id");
//System.out.println(id);
StringBuilder sb = new StringBuilder();
if (id != null) {
	List<Code> list = ((CachedAttributeService)ServiceManager.getAttributeService()).getCodeCache().getCodeList(id);
	for (int i = 0; i < list.size(); i++) {
		Code c = list.get(i);
		if (i > 0)
			sb.append(",");
		renderCode(sb, c);
	}
}
%>

<%!
	public void renderCode(StringBuilder sb, Code c) throws Exception {
		sb.append("{");
		if (c.getChilds() == null)
			sb.append("leaf:true");
		else
			sb.append("leaf:false");
		sb.append(",_click: true");
		if (c.getRemark() == null)
			sb.append(",remark:''");
		else
			sb.append(",remark:'" + StringUtil.escapeJavaScript(c.getRemark()) + "'");
		int pos = c.getName().lastIndexOf("/");
		String name = c.getName();
		if (pos != -1)
			name = name.substring(pos + 1);
		sb.append(",text:'" + StringUtil.escapeJavaScript(name) + "'");
		sb.append(",id:'" + StringUtil.escapeJavaScript(c.getName()) + "'");
		sb.append(",oid:'" + c.getOid() + "'");
		sb.append(",parentOid:'" + c.getParentOid() + "'");
		List<Code> list = c.getChilds();
		if (list != null && list.size() > 0) {
			sb.append(",children:[");
			for (int i = 0; i < list.size(); i++) {
				if (i > 0)
					sb.append(",");
				renderCode(sb, list.get(i));
			}
			sb.append("]");
		}
		sb.append("}");
	}
%>
[ <%=sb.toString()%> ]

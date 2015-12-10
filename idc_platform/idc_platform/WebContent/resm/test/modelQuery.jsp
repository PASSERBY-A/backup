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

List<Model> list = ServiceManager.getModelService().getAllModels(1);
%>

<%!
	public void renderCode(StringBuilder sb, Model c) throws Exception {
		sb.append("{");
		List<Model> list = c.getChilds(1);
		sb.append("uiProvider:'col', name:'" + StringUtil.escapeJavaScript(c.getName()) + "'");
		sb.append(",modelId:'" + c.getId() + "'");
		sb.append(",parentId:'" + c.getParentId() + "'");
		
		if (list != null && list.size() > 0) {
			sb.append(",leaf:false");
			sb.append(",children:[");
			for (int i = 0; i < list.size(); i++) {
				if (i > 0)
					sb.append(",");
				renderCode(sb, list.get(i));
			}
			sb.append("]");
		} else
			sb.append(",leaf:true");
		sb.append("}");
	}
%>
[
<%
StringBuilder sb = new StringBuilder();
int count = 0;
for (int i = 0; i < list.size(); i++) {
	Model c = list.get(i);
	
	if (!c.hasParent()) {
	if (count > 0)
		sb.append(",");
		renderCode(sb, c);
		count++;
	}
}
out.println(sb.toString());
%>
]
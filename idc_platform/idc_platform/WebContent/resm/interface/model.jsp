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
// TODO 获取用户
int userId = 1;

List<Model> list = ServiceManager.getModelService().getAllModels(userId);
StringBuilder sb = new StringBuilder();
int count = 0;
for (int i = 0; i < list.size(); i++) {
	Model c = list.get(i);
	if (!c.hasParent() && c.isEnabled()) {
		if (count > 0)
			sb.append(",");
		renderCode(sb, c, userId);
		count++;
	}
}
%>

<%!
	public void renderCode(StringBuilder sb, Model c, int userId) throws Exception {
		List<Model> list = c.getChilds(userId);
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).isEnabled()) {
				// System.out.println("过滤掉子节点: " + list.get(i).getName());
				list.remove(i);
				i--;
			}
		}
		sb.append("{");
		sb.append("_click: ").append(!c.isDirectoryOnly());
		sb.append(",text:'" + StringUtil.escapeJavaScript(c.getName()) + "'");
		sb.append(",id:'" + c.getId() + "'");
		sb.append(",parentId:'" + c.getParentId() + "'");
		
		if (list != null && list.size() > 0) {
			sb.append(",leaf:false");
			sb.append(",children:[");
			int count = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).isEnabled()) {
					if (count > 0)
						sb.append(",");
					renderCode(sb, list.get(i), userId);
					count++;
				}
			}
			sb.append("]");
		} else
			sb.append(",leaf:true");
		sb.append("}");
	}
%>
[
<%
out.println(sb.toString());
%>
]
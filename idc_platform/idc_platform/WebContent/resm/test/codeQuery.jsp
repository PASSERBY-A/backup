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

List<Code> list = ((CachedAttributeService)ServiceManager.getAttributeService()).getCodeCache().getCacheStore().values();
%>

<%!
	public void renderCode(StringBuilder sb, Code c) throws Exception {
		sb.append("{");
		if (c.getChilds() == null)
			sb.append("leaf:true");
		else
			sb.append("leaf:false");
		sb.append(",uiProvider:'col', name:'" + StringUtil.escapeJavaScript(c.getName()) + "'");
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
[
<%
StringBuilder sb = new StringBuilder();
Map<String, String> map = new HashMap<String, String>();
for (int i = 0; i < list.size(); i++) {
	Code c = list.get(i);
	if (map.get(c.getId()) != null)
		continue;
	map.put(c.getId(), "1");
	
	if (i > 0)
		sb.append(",");

	sb.append("{");
	sb.append("cls:'master-task',leaf:false");
	sb.append(",uiProvider:'col', name:'" + StringUtil.escapeJavaScript(c.getId()) + "'");
	sb.append(",children:[");

	List<Code> list2 = ((CachedAttributeService)ServiceManager.getAttributeService()).getCodeCache().getCodeList(c.getId());
	for (int j = 0; j < list2.size(); j++) {
		if (j > 0)
			sb.append(",");
		renderCode(sb, list2.get(j));
	}

	sb.append("]");
	sb.append("}");
}
out.println(sb.toString());
%>
]
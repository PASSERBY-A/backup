<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.itsm.dsm.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="java.util.*"%>

<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
<table class='embed2' border=0 cellspacing=1 width=100%>
	<tr>
		<td align="center" style="background:#EEEEEE">Ӧ�õ�ַ</td>
		<td align="center" style="background:#EEEEEE">���������</td>
	</tr>
<%
	Map<String, List<String>> dsMap = DSMCenter.getDsMap();
	Set<String> keySet = dsMap.keySet();
	for (Iterator<String> ite = keySet.iterator(); ite.hasNext();) {
		String key = ite.next();
		List l = dsMap.get(key);
		out.print("<tr>");
		out.print("<td>"+key+"</td>");
		out.print("<td>"+l+"</td>");
		out.print("</tr>");
	}
%>
</table>
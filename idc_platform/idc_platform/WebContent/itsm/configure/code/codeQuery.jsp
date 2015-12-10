<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<%
int type = Integer.parseInt(request.getParameter("type"));
List codes = CIManager.getCodesByTypeOid(type);
//System.out.println(codes.size());
%>

[
<%!
public void renderNode(StringBuffer sb, CodeInfo info) {
	sb.append("{");
	sb.append("codeOId:'" + info.getOid() + "',");
	sb.append("codeId:'" + info.getCodeId() + "',");
	sb.append("clickable:'" + info.isClickable() + "',");
	sb.append("enabled:'" + info.isEnabled() + "',");
	sb.append("codeName:'" + StringUtil.escapeJavaScript(info.getName()) + "',");
	sb.append("codeDesc:'" + StringUtil.escapeJavaScript(info.getDesc()) + "',");
	sb.append("codeOrder:'" + info.getOrder() + "',");
	sb.append("uiProvider:'col',");
	List l = info.getSubItems();
	if (l.size() == 0)
		sb.append("leaf: true");
	else {
		sb.append("children:[");
		for (int i = 0; i < l.size(); i++) 
		{
			CodeInfo info2 = (CodeInfo)l.get(i);
			if (i > 0)
				sb.append(",");
			renderNode(sb, info2);
		}
		sb.append("]");
	}
	sb.append("}");
}
%>
	<%
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < codes.size(); i++) 
	{
		CodeInfo info = (CodeInfo)codes.get(i);
		if (i > 0)
			sb.append(",");
		renderNode(sb, info);
	}
	%>
	<%=sb.toString()%>
	]

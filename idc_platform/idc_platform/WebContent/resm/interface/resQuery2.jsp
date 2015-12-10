<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.model.*"%>
<%@ page import="com.hp.idc.resm.resource.*"%>
<%@ page import="com.hp.idc.resm.util.*"%>
<%@ page import="com.hp.idc.resm.ui.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
/*
	Object c[] = request.getParameterMap().keySet().toArray();
	System.out.println("-");
	for (int i = 0; i < c.length; i++)
		System.out.println("" + c[i].toString() + ":" + request.getParameter(c[i].toString()).toString());
*/
String modelId = request.getParameter("modelId");

String[] ids = request.getParameter("id").toString().split(",");

List<ResourceObject> list = new ArrayList<ResourceObject>();

for (int i = 0; i < ids.length; i++)
{
	String id = ids[i];
	if (id == null || id.length() == 0)
		continue;
	int resId = Integer.parseInt(id);
	ResourceObject res = ServiceManager.getResourceService().getResourceById(resId);
	if (res == null || !res.getModelId().equals(modelId))
		continue;
	list.add(res);
}
%>
{
	"totalCount" : <%=list.size()%>,
	"items" : [
<%
	for (int i = 0; i < list.size(); i++)
	{
		ResourceObject res = list.get(i);
		List<AttributeBase> attrs = res.getHeader();
		if (i > 0)
			out.print(",");
		out.print("{");
		for (int j = 0; j < attrs.size(); j++)
		{
			if (j > 0)
				out.print(",");
			AttributeBase ab = attrs.get(j);
			out.println(ab.getAttribute().getAttrId() + ":'" + StringUtil.escapeJavaScript(ab.getText()) + "'");
		}
		out.print("}");
	}
%>	
]}

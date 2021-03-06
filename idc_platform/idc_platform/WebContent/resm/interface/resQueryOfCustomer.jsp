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

// TODO 获取用户
int userId = 1;

/*
	Object c[] = request.getParameterMap().keySet().toArray();
	System.out.println("-");
	for (int i = 0; i < c.length; i++)
		System.out.println("" + c[i].toString() + ":" + request.getParameter(c[i].toString()).toString());
*/
String customerId = request.getParameter("customerId");
String orderNo = request.getParameter("orderNo");

/* 计算排序 */
String sortBy = request.getParameter("sort");
if ("DESC".equals(request.getParameter("dir")))
	sortBy = "-" + sortBy;
String filter = request.getParameter("filter");

String limit = request.getParameter("limit");
if (limit == null)
	limit = "20";
int pageCount = Integer.parseInt(limit);
int startPage = 1;

/* ext 传过来的是开始记录号，从0开始，需要重新计算开始页数 */
if (request.getParameter("start") != null) {
	startPage = Integer.parseInt(request.getParameter("start")) + 1;
	startPage = (startPage - 1) / pageCount + 1;
}

List<ResourceObject> l = ServiceManager.getResourceService()
.findResource(
		"$res.getAttributeValue(\"customer_id\").equals(\""+ customerId + "\") && "+
		"$res.getAttributeValue(\"order_id\").equals(\""+ orderNo + "\")", 1);

%>
{
	"totalCount" : <%=l.size() %>,
	"items" : [
<%
	for (int i = 0; i < l.size(); i++)
	{
		ResourceObject res = l.get(i);
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

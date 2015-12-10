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
String modelId = request.getParameter("modelId");
String oper = request.getParameter("oper");
/* 计算排序 */
String sortBy = request.getParameter("sort");
if ("DESC".equals(request.getParameter("dir")))
	sortBy = "-" + sortBy;
String filter = request.getParameter("filter");
System.out.println(filter);

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

PageQueryInfo query = new PageQueryInfo();
query.setSortBy(sortBy);
query.setStartPage(startPage);
query.setPageCount(pageCount);
PageInfo<ResourceObject> info = new PageInfo<ResourceObject>();
if("0".equals(oper)){
	info = ServiceManager.getResourceService().listResource(modelId, true, filter, query, userId);
}
else{
	String id=request.getParameter("id");
	String name=request.getParameter("name");
	String customer_id=request.getParameter("customer_id");
	String order_id=request.getParameter("order_id");
	String status=request.getParameter("status");
	Map<String,Object> paramMap=new HashMap<String,Object>();
	if(id!=null)paramMap.put("id",id);
	if(name!=null)paramMap.put("name",name);
	if(customer_id!=null)paramMap.put("customer_id",customer_id);
	if(order_id!=null)paramMap.put("order_id",order_id);
	if(status!=null)paramMap.put("status",status);
	System.out.println(status);
	info= ServiceManager.getResourceService().listResource(modelId, true, paramMap, query, userId);
}
%>
{
	"totalCount" : <%=info.getTotalCount()%>,
	"items" : [
<%
	for (int i = 0; i < info.getData().size(); i++)
	{
		ResourceObject res = info.getData().get(i);
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

<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.util.StringUtil"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
String wogId = request.getParameter("wogId");
wogId = wogId == null?"":wogId;

List<PersonInfo> lpList = PersonManager.getPersonsByWorkgoupId("ITSM",wogId,false);


String filter = request.getParameter("filter");
filter = (filter==null?"":filter);

if (!filter.equals("")){
	for (int i = 0; i < lpList.size(); i++) {
		PersonInfo pInfo = (PersonInfo)lpList.get(i);
		if (pInfo.getId().indexOf(filter)!=-1)
			continue;
		if (pInfo.getName().indexOf(filter)!=-1)
			continue;
		if (pInfo.getMobile().indexOf(filter)!=-1)
			continue;
		if (pInfo.getEmail().indexOf(filter)!=-1)
			continue;
		lpList.remove(i);
		i--;
	}
}
%>
{	"totalCount" : <%=lpList.size()%>,
	"success" : true,
	"lgs" : [
<%
	if (lpList!=null)
	for (int i = 0; i < lpList.size(); i++) {
		if (i > 0)
			out.print(",");
		out.print("{");
		PersonInfo lgInfo = lpList.get(i);
		out.print("'id':'"+lgInfo.getId()+"',");
		out.print("'name':'"+StringUtil.escapeJavaScript(lgInfo.getName())+"',");
		out.print("'status':'"+lgInfo.getStatus()+"'");
		out.print("}");
	}
%>

]}
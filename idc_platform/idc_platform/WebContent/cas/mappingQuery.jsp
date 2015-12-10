<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.*"%>

<%@ include file="getPurview.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("UTF-8");

String personUserId = request.getParameter("userId");
List<AUCMappingInfo> returnList = AUCMapping.getThirdUserId(personUserId);
%>

{"records" : [
<%
for (int i = 0; i < returnList.size(); i++){
	AUCMappingInfo mInfo = returnList.get(i);
	if (i>0)
		out.println(",");
	out.print("{");
	out.print("'thirdSystem':'"+mInfo.getThirdSystem()+"' ,");
	out.print("'thirdUserId':'"+mInfo.getThirdUserId()+"'");
	out.print("}");
}
%>
],
"totalCount" : <%=returnList.size()%>
}
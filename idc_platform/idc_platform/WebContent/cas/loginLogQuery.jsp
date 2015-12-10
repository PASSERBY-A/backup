<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.hp.idc.cas.log.*"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("UTF-8");

try{
String begin = request.getParameter("begin");
String end = request.getParameter("end");

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

if (begin == null || begin.equals("")) {
	begin = sdf.format(new Date());
} else
	begin += " 00:00:00";
if (end == null || end.equals(""))
	end = sdf.format(new Date());
else
	end += " 23:59:59";


List<LoginLogInfo> logList = LoginLogManager.getLoginLog(begin,end);
int limit = 0;
int start = 0;
String limit_s = request.getParameter("limit");
if (limit_s!=null && !limit_s.equals(""))
	limit = Integer.parseInt(limit_s);
else
	limit = logList.size();
String start_s = request.getParameter("start");
if (start_s != null)
	start = Integer.parseInt(start_s);


%>
{
"totalCount" : <%=logList.size()%>,
"records" : [
<%
int count = 0;
for (int i = start; i < logList.size() && i < (start+limit); i++){
	LoginLogInfo llInfo = logList.get(i);
	if (count>0)
		out.println(",");
	out.print("{");
	out.print("'userId':'"+llInfo.getUser_id()+"',");
	out.print("'loginTime':'"+llInfo.getLogin_time()+"',");
	out.print("'loginIp':'"+llInfo.getLogin_ip()+"',");
	out.print("'loginHost':'"+llInfo.getLogin_host()+"'");
	out.print("}");
	count++;
}
%>

]}

<%
} catch(Exception e){
	e.printStackTrace();
}
%>
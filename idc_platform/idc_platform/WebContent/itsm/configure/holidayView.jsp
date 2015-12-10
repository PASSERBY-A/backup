<%@page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<%@ include file="../getUser.jsp"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
</head>
<style>
.td0 {background-color: #EEEEEE; }
.td1 {background-color: #FFFFFF; }
body, td, th { font-size: 10.5pt }
</style>
<body>
<script>
function change(date)
{
	fm2.date.value = date;
	fm2.submit();
}
</script>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	Calendar curCal = new GregorianCalendar();
	int year = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : curCal.get(Calendar.YEAR);
	int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : curCal.get(Calendar.MONTH);
%>
<iframe name=ref1 id=ref1 style='display:none'></iframe>
<form action="holidayPost.jsp" method="post" name="fm2" id=fm2 target=ref1>
	<input type=hidden name="userid"         value="<%=userId%>">
	<input type=hidden name="date"         value="">
</form>
<div align=right>
<form action="holidayView.jsp" method="post" name="fm" id=fm>
	<select name=year onchange="this.form.submit()">
	<% for (int i = 2007; i < 2010; i++) {
		out.print("<option value='" + i + "'" + (i == year ? " selected" : "") + ">" + i + "</option>");
	}%>
	</select> 年
	<select name=month onchange="this.form.submit()">
	<% for (int i = 0; i < 12; i++) {
		out.print("<option value='" + i + "'" + (i == month ? " selected" : "") + ">" + (i + 1) + "</option>");
	}%>
	</select> 月&nbsp; &nbsp;
</form>
</div>
<table cellpadding=0 cellspacing=0 border=5 width=96% align=center >
 <tr><td>
   <table cellpadding=4 cellspacing=1 border=0 width=100% bgcolor="#CCCCCC">
	  <tr class="td0">
		<th align=center height="50" width="16%"><font color="red">星期日</font></th>
		<th align=center width="14%">星期一</th>
		<th align=center width="14%">星期二</th>
		<th align=center width="14%">星期三</th>
		<th align=center width="14%">星期四</th>
		<th align=center width="14%">星期五</th>
		<th align=center width="14%"><font color="red">星期六</font></th>
	  </tr>
<%
	// calc first day and last day
	Calendar cal = new GregorianCalendar();
	cal.set(Calendar.DATE, 1);
	cal.set(Calendar.MONTH, month);
	cal.set(Calendar.YEAR, year);
	out.println("<tr class=td1>");
	for (int i = 0; ; i++)
	{
		if (i > 0 && i % 7 == 0)
		{
			if (cal.get(Calendar.MONTH) != month)
				break;
			out.println("</tr><tr class=td1>");
		}
		boolean isInMonth = false;
		if (i % 7 == cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY && cal.get(Calendar.MONTH) == month)
			isInMonth = true;
		int calDate = cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DATE);
		int curDate = curCal.get(Calendar.YEAR) * 10000 + (curCal.get(Calendar.MONTH) + 1) * 100 + curCal.get(Calendar.DATE);
		if (HolidayManager.isHoliday(cal.getTime()) && isInMonth) // holiday
			out.print("<td height=50 bgcolor='#ffc1ff' align=center>");
		else
			out.print("<td height=50 align=center>");
		if (isInMonth)
		{
			int day = cal.get(Calendar.DATE);
			out.print("<span style='cursor:hand' onclick='change(\"" + sdf.format(cal.getTime()) + "\")'>" + day + "</span>");
			cal.add(Calendar.DATE, 1);
		}
		out.print("</td>");
	}
	out.println("</tr>");
%>
	</table>
 </td></tr>
</table>
<p>说明：点击日期进行工作日/休息日的切换，白色表示工作日，紫色表示休息日。</p>
</body>
</html>


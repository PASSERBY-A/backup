<%@ page language="java" import="java.text.*" pageEncoding="gbk"%>

<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="java.util.*"%>

<html>
<%	
	String userid = request.getParameter("userid");
	String date = request.getParameter("date");
	try {
		HolidayManager.change(date, userid);
	}
	catch (Exception e) {
	}
%>
</head>
<body>
<script>
	parent.fm.submit();
</script>
</body>
</html>
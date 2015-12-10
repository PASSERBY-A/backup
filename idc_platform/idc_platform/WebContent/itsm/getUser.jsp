<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>

<%
String userId = (String)session.getAttribute(Constant.SESSION_LOGIN);
String userName = "";
PersonInfo _pi = PersonManager.getPersonById(userId);
if(_pi != null)
	userName = _pi.getName();

%>

  <%
  	if (Cache.initStatus == Cache.INIT_INPROGRESS) {
  %>
  
<%@page import="com.hp.idc.common.Constant"%><html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
 	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>

	</head>
<body>
		<script>
  		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"\u6B63\u5728\u66F4\u65B0\u7F13\u5B58\u6570\u636E\uFF0C\u8BF7\u7A0D\u5019\u8BBF\u95EE...."});
			//myMask.show();
	</script>
</body>
</html>

  <%
  		return;
  	}
  %>
  

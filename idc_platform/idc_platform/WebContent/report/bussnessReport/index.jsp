<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    String ctx = request.getContextPath();
%>
<%@ page language="java" contentType="text/html; charset=gbk"%>
<html> 
<head>
<title>œµÕ≥…Ë÷√</title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="../style/css/index.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=ctx%>/common/scripts/util.js"></script>

<script type="text/javascript" src="BusinessQueryFn1.js"></script>
<script type="text/javascript" src="BusinessQueryFn2.js"></script>
<script type="text/javascript" src="BusinessQueryFn3.js"></script>
<script type="text/javascript" src="BusinessQueryFn4.js"></script>

<script type="text/javascript" src="index.js"></script>
<script type="text/javascript" src="menu.js"></script>
<script type="text/javascript" src="main.js"></script>
</head>

<body>
</body> 
</html>

<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ include file="../getUser.jsp"%>


<html>
<head>
  <title>历史记录</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>
</head>
<body>
<%
	int taskOid = -1;
	if (request.getParameter("taskOid")!=null && !request.getParameter("taskOid").equals(""))
		taskOid = Integer.parseInt(request.getParameter("taskOid"));
	int wfOid = -1;
	if (request.getParameter("wfOid")!=null && !request.getParameter("wfOid").equals(""))
		wfOid = Integer.parseInt(request.getParameter("wfOid"));
	int wfVer = -1;
	if (request.getParameter("wfVer")!=null && !request.getParameter("wfVer").equals(""))
		wfVer = Integer.parseInt(request.getParameter("wfVer"));
	String origin = request.getParameter("origin");
	
	TaskInfo taskInfo = TaskManager.getTaskInfoByOid(origin,taskOid,wfOid,wfVer);

	WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
	if (wfInfo!=null && wfInfo.getSnapMode().equals("local")) {
		String formOid = wfInfo.getSnapLocalFormOid();
		String formTemplate = wfInfo.getSnapLocalFormTemplate();
		if (formTemplate == null || formTemplate.equals(""))
			formTemplate = "/default/snapView.html";
		if (formOid != null && !formOid.equals("")) {
				Map context = new HashMap();
				FormInfo fi = FormManager.getFormByOid(Integer.parseInt(formOid));
				List fieldList = fi.getFields();
				context.put("fieldList", fieldList);
				context.put("taskInfo", taskInfo);
				out.println(TemplateUtil.getHTMLStrFromTemplate(formTemplate,context));
		}
	}
%>
</body>
</html>
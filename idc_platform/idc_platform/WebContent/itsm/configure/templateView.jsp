<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.inter.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="org.apache.velocity.*"%>
<%@ page import="org.apache.velocity.app.*"%>
<%@ include file="../getUser.jsp"%>


<html>
<head>
  <title>模板预览页面</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/json2.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
</head>
<body style="margin-left:20; margin-top:20">
<%

int formOid = -1;
String template = request.getParameter("template");
if (template == null || template.equals(""))
	template = "/default/nodeAction.html";
if (request.getParameter("formOid")!=null && !request.getParameter("formOid").equals(""))
	formOid = Integer.parseInt(request.getParameter("formOid"));

TaskInfo taskInfo = new TaskInfo();
taskInfo.setXmlData("<workflow/>");
TaskData taskData = new TaskData(taskInfo,null);
Object c[] = request.getParameterMap().keySet().toArray();
for (int i = 0; i < c.length; i++)
{
	String value = request.getParameter(c[i].toString());
	taskData.setValueVirtual(c[i].toString(),value);

}

List fieldList = new ArrayList();
FormInfo formInfo = FormManager.getFormByOid(formOid);
if (formInfo!=null) {
	fieldList = formInfo.getFields();
}

%>
<script>
var simple = new Ext.form.FormPanel({
	method: 'POST',
	autoScroll: true,
	frame:true,
	borderStyle:'border 0 none',
	defaultType: 'textfield',
	region:'center',
	minSize: 175,
	maxSize: 400,
	margins:'1 1 1 1',
	border: false,
	errorReader: new Ext.form.XmlErrorReader(),
	labelWidth: 75, // label settings here cascade unless overridden
	url:'',
	itemCls:'x-form-label-nowrap',
	titleTipEnable:'true'
});
<%

Map context = new HashMap();
context.put("fieldList", fieldList);
context.put("FLD_EXECUTE_USER", Consts.FLD_EXECUTE_USER);
context.put("Consts", new Consts());
context.put("wfOid", "");
context.put("taskOid", "");
context.put("taskData", taskData);
context.put("formId", formOid);
context.put("ItsmUtil", new ItsmUtil());
context.put("request", request);
context.put("formFields", new ArrayList());
context.put("TemplateManager", new TemplateManager());
out.println(TemplateUtil.getHTMLStrFromTemplate(template,context));

%>
Ext.onReady(function() {
	simple.buttonAlign = 'right';
	simple.addButton(
		{text: '取消'},
		function() {
			parent.window.close();
		}
	);
	var viewport = new Ext.Viewport({
		layout:'border',
    items:[simple]
	});


var _defaults = {
	currentUserId : "root",
	currentUserName : "系统管理员"
};
var _wf_status = {
	id : "-1",
	name : "-1"
};

try {
<%
		if (formInfo != null)
			out.println(formInfo.getScript());
		else {
%>
	var emptyDIV = document.createElement("div");
	emptyDIV.innerText = "找不到表单信息,请与管理员联系.";
<%
		}
%>
} catch (e) { alert('脚本出错:' + e.name + "\n\n" + e.message); }
	
});

</script>
</body>
</html>
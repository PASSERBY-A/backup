<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.inter.*"%>
<%@ page import="com.hp.idc.itsm.impl.*"%>
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
<title>申请变更</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>

<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Expires", "0");
%>
</head>
<body style="margin-left: 20; margin-top: 20">
<%
try {
	int masterTaskOid = -1;
	int masterWfOid = -1;
	int masterWfVer = -1;
	int wfOid = -1;
	wfOid = Integer.parseInt(request.getParameter("wfOid"));
	if (request.getParameter("masterTaskOid") != null)
		masterTaskOid = Integer.parseInt(request.getParameter("masterTaskOid"));
	if (request.getParameter("masterWfOid") != null)
		masterWfOid = Integer.parseInt(request.getParameter("masterWfOid"));
	if (request.getParameter("masterWfVer") != null)
		masterWfVer = Integer.parseInt(request.getParameter("masterWfVer"));
	
	ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
	
	TaskInfo masterTaskInfo = itmi.getTaskInfoByOid(masterTaskOid,masterWfOid);
	if (masterTaskInfo.getStatus() != TaskInfo.STATUS_OPEN) {
		out.println("<script defer>Ext.MessageBox.alert('错误', '原工单已经关闭，不能申请变更');</script>");
	}
	
	Map<String,String> masterFieldValue = masterTaskInfo.getValues();

	TaskInfo taskInfo = null;
	TaskData taskData = null;
	WorkflowInfo wfInfo = null;
	WorkflowData wfData = null;
	NodeInfo fromNode = null;
	NodeInfo toNode = null;
	ActionInfo actionInfo = null;

	//是否是转发动作
	boolean isTransmit = false;
	String formId = "-1";

	wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
	wfData = wfInfo.getCurrentVersion();
	fromNode = wfData.getRootNode();
	if (fromNode == null) {
		out.println("<script defer>Ext.MessageBox.alert('错误', '此流程没有设置根结点,请与管理员联系.');</script>");
		return;
	}
	actionInfo = (ActionInfo) fromNode.getActions().get(0);
	toNode = actionInfo.getToNode();
	formId = actionInfo.getFormId();

	taskInfo = new TaskInfo(wfData, userId);
	taskData = new TaskData(taskInfo, null,fromNode, actionInfo.getActionId(), "", userId);


	taskData.setAssignTo("");

	try {
		TaskUpdateInfo tuInfo = new TaskUpdateInfo();
		tuInfo.setTaskData(taskData);
		tuInfo.setTaskInfo(taskInfo);
		tuInfo.setOperName(userId);
		toNode.onEnter(tuInfo);
	} catch (Exception ex) {
		out.println("<script defer>Ext.MessageBox.alert('错误', '"
				+ StringUtil.escapeJavaScript(ex.toString()) + "');</script>");
		ex.printStackTrace();
	}

	List fieldList = new ArrayList();
	FormInfo formInfo = null;
	if (wfData.getForms().containsKey(formId)) {
		formInfo = (FormInfo) wfData.getForms().get(formId);
	} else {
		int formOid = 0;
		try {
			formOid = Integer.parseInt(formId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		formInfo = FormManager.getFormByOid(formOid);
	}

	if (formInfo != null) {
		fieldList = formInfo.getFields();
	}

%>
<script defer>
var simple = new Ext.form.FormPanel({
	method: 'POST',
	autoScroll: true,
	frame:true,
	borderStyle:'border 0 none',
	defaultType: 'textfield',
	baseParams: {
		taskOid: "-1",
		origin: "ITSM",
		taskDataId: "-1",
		wfOid: "<%=wfOid%>",
		operType: "edit",
		closeBranchNode: "0",
		actionId: "<%=actionInfo.getActionId()%>",
		formOid:"<%=formId%>",
		fromNodeId: "<%=fromNode.getId()%>",
		toNodeId: "<%=toNode.getId()%>",
		sys_linkTaskStr:""
	},
	region:'center',
	minSize: 175,
	maxSize: 400,
	margins:'1 1 1 1',
	border: false,
	errorReader: new Ext.form.XmlErrorReader(),
	labelWidth: 75, // label settings here cascade unless overridden
	url:'<%=Consts.ITSM_HOME%>/task/taskPost.jsp',
	itemCls:'x-form-label-nowrap',
	titleTipEnable:'true'
});
<%
Map context = new HashMap();
context.put("fieldList", fieldList);
context.put("FLD_EXECUTE_USER", Consts.FLD_EXECUTE_USER);
context.put("Consts", new Consts());
context.put("TYPE_BRANCH_BEGIN", NodeInfo.TYPE_BRANCH_BEGIN + "");
context.put("fromNode", fromNode);
context.put("toNode", toNode);
context.put("toNodePath", toNode.getId()+"");
context.put("actionInfo", actionInfo);
context.put("wfOid", wfOid + "");
context.put("taskOid", "-1");
context.put("taskData", taskData);
context.put("taskDataId", "-1");
context.put("formId", formId);
context.put("ItsmUtil", new ItsmUtil());
context.put("request", request);
context.put("origin", "ITSM");
context.put("formFields", new ArrayList());
context.put("attachments", taskData);
context.put("TemplateManager", new TemplateManager());
String templateFile = actionInfo == null ? "/default/nodeAction.html" : actionInfo
		.getTemplate();
out.println(TemplateUtil.getHTMLStrFromTemplate(templateFile, context));

%>
function submitTaskEditForm(closeBranchNode)
{
	var form = simple.form;
	form.baseParams.masterWfOid = <%=masterWfOid%>;
	form.baseParams.masterTaskOid = <%=masterTaskOid%>;
	form.baseParams.masterWfVer = <%=masterWfVer%>;
	
	form.baseParams.closeBranchNode = closeBranchNode;

	form.url = "<%=Consts.ITSM_HOME%>/task/changeApplyPost.jsp";
	//如果是保存草稿，不验证必填限制
	if (closeBranchNode == <%=TaskUpdateInfo.TYPE_SAVE_FOR_EDIT%>) {
		form.items.each(function(f){
		   f.allowBlank = true;
		});
	}
	
	if (!form.isValid()) {
		Ext.MessageBox.alert("提示", "请填写完整的内容");
		return;
	}
	
	var count = 0;
	if (parent.linkds)
		count = parent.linkds.getCount();
	var linkStr = "";
	for (var i = 0; i < count; i++) {
		var origin = parent.linkds.getAt(i).get("origin");
		var wfOid = parent.linkds.getAt(i).get("wfOid");
		var oid = parent.linkds.getAt(i).get("oid");
		if (i>0)
			linkStr += ",";
		linkStr += origin+"_"+wfOid+"_"+oid;
	}
	if (linkStr!="")
		form.baseParams.sys_linkTaskStr = linkStr;
	<%for (int i = 0; i < fieldList.size(); i++) {
					FieldInfo fieldInfo = (FieldInfo) fieldList.get(i);
					if (fieldInfo.getType().equals("com.hp.idc.itsm.configure.fields.GridFieldInfo")) {
						out.println("form.baseParams.fld_" + fieldInfo.getId() + " = fld_"
								+ fieldInfo.getId() + ".getValue();");
					}
				}%>
	form.items.each(function(f){
	   f.old_status_disabled = f.disabled;
	   f.enable();
	});
	form.submit({
		waitMsg: '<%=Consts.MSG_WAIT%>',
		success: function(form, action)
		{
			form.items.each(function(f){
			   if (f.old_status_disabled)
				   f.disable();
			});
			Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text,callBackFun);

		},
		failure: function(form, action)
		{
			Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_FAILED%>", form.errorReader.xmlData.documentElement.text);
		}
	});
}

function callBackFun(btn){
	try {
		var wp = window.parent.opener;
		if (wp!=null && typeof(wp) != "undefined " && !wp.closed && wp.viewds) {
			wp.viewds.reload();
		}
		if(window && window.dialogArguments){
			var mw = window.dialogArguments;
			var mwo = mw.opener;
			if (mwo!=null && typeof(mwo) != "undefined " && !mwo.closed && mwo.viewds) {
				mwo.viewds.reload();
			}

			mw.location.reload();
		}
	}catch(exc) {
	
	}
	parent.window.close();

}
Ext.onReady(function() {


simple.buttonAlign = 'right';

	<%//如果是转发，则不提供此按钮
	if(taskData.getStatus() == TaskData.STATUS_WAITING) {
		TaskWaitingInfo twInfo = taskData.getWaitInfo();
	%>
	simple.addButton(
		{text: '正在等待工单(<%=twInfo.getWaiting_task_oid()%>)处理'},
		function() {
			submitTaskEditForm("<%=TaskUpdateInfo.TYPE_SAVE_FOR_EDIT%>");
		}
	);
	<%
	} else {
		if (!isTransmit) {
	%>
		simple.addButton(
			{text: '保存为草稿'},
			function() {
				submitTaskEditForm("<%=TaskUpdateInfo.TYPE_SAVE_FOR_EDIT%>");
			}
		);
		<%}%>
		simple.addButton(
			{text: '提交'},
			function() {
				submitTaskEditForm("0");
			}
		);

	<%
	}
	%>
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

<%
for (int i = 0; i < fieldList.size(); i++) {
	FieldInfo fieldInfo = (FieldInfo) fieldList.get(i);
	String value = "";
	if (fieldInfo.getId().equals(Consts.FLD_EXECUTE_USER)) {
		
	} else {
		value = masterFieldValue.get(fieldInfo.getId());
		if (fieldInfo.getId().equals("title"))
			value = "[变更申请]"+value;
		if(fieldInfo.getId().equals("master_task_oid")) {
			value = ""+masterTaskOid;
		}
	}
	if (value!=null)
		fieldInfo.setValue(value);
	out.println(fieldInfo.getFormValue());
	//out.println("simple.getForm().findField('fld_"+fieldInfo.getId()+"').setValue('"+value+"');");
}
%>


var _defaults = {
	currentUserId : "<%=PersonManager.getRemoteId("ITSM", userId)%>",
	currentUserName : "<%=PersonManager.getPersonNameById("ITSM", PersonManager.getRemoteId("ITSM", userId))%>"
};
var _wf_status = {
	id : "<%if (taskData != null)
	out.print(taskData.getNodeId());%>",
	name : "<%if (taskData != null)
	out.print(taskData.getNodeDesc());%>"
};

try {
<%if (!isTransmit) {
	if (formInfo != null)
		out.println(formInfo.getScript());
	else {%>
	var emptyDIV = document.createElement("div");
	emptyDIV.innerText = "找不到表单信息,请与管理员联系.";
<%}
}%>
} catch (e) { alert('脚本出错:' + e.name + "\n\n" + e.message); }
});



</script>
<%
	} catch (Exception ex) {
		ex.printStackTrace();
	}
%>

</body>
</html>
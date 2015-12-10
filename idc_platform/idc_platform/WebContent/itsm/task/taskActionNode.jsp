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
<title>处理动作表单</title>
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
<body style="margin-left: 20; margin-top: 20" >

<div id="loader_container">
     <div align="center"><b><h2>页面正在加载中</h2></b><img src="../images/progress.gif"></img></div>
</div>

<%
try {
	int taskOid = -1;
	int taskDataId = -1;
	int wfOid = -1;
	int wfVer = -1;
	wfOid = Integer.parseInt(request.getParameter("wfOid"));
	if (request.getParameter("taskOid") != null)
		taskOid = Integer.parseInt(request.getParameter("taskOid"));
	if (request.getParameter("wfVer") != null)
		wfVer = Integer.parseInt(request.getParameter("wfVer"));
	String origin = request.getParameter("origin");

	if (origin == null || origin.equals("")) {
		out.println("<script defer>alert('工单来源参数为空，请核实');</script>");
		return;
	}

	/*
	 * operType=="copyCreate",是复制现有记录创建的
	 * operType!="copyCreate"&&taskOid==-1,新建的
	 * taskOid!=-1,正常的流程流转
	 */
	String operType = request.getParameter("operType");

	TaskInfo taskInfo = null;
	TaskData taskData = null;
	WorkflowInfo wfInfo = null;
	WorkflowData wfData = null;
	NodeInfo fromNode = null;
	NodeInfo toNode = null;
	ActionInfo actionInfo = null;
	String toNodePath = request.getParameter("toNodePath");

	//是否是转发动作
	boolean isTransmit = false;
	String formId = "-1";
	String template = "";
	
	boolean canDealThisNode = false;

	if (taskOid != -1) {

		taskDataId = Integer.parseInt(request.getParameter("taskDataId"));
		taskInfo = TaskManager.getTaskInfoByOid(origin, taskOid, wfOid, wfVer);
		taskData = taskInfo.getTaskData(taskDataId);
		wfOid = taskInfo.getWfOid();
		wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
		wfData = wfInfo.getVersion(taskInfo.getWfVer());
		fromNode = WorkflowData.getNodeByPath(wfData, taskData.getNodePath());
		toNode = WorkflowData.getNodeByPath(wfData, toNodePath);
		
		//add by zhonganjing about analyis
		if (!taskData.canDealThisNode(userId) && !fromNode.isShareDeal()) {
			out.println("<script defer>alert('您无权限操作此工单，请联系管理员');</script>");
			return;
		}
		
		String toNodeId = "";
		if (toNodePath.lastIndexOf("/") != -1)
			toNodeId = toNodePath.substring(toNodePath.lastIndexOf("/") + 1);
		else
			toNodeId = toNodePath;

		//如果目标节点是子流程节点，重置目标节点PATH
		if (toNode.getType() == NodeInfo.TYPE_SUB_WF) {
			toNodePath += "/" + toNode.getSubNode().getId();
		}
		if (fromNode.getOwner().getWorkflowOid() != toNode.getOwner().getWorkflowOid())
			fromNode = wfData.getNode(taskData.getNodeId());
		if (fromNode != toNode) {
			actionInfo = fromNode.getAction(toNodeId);
			if (actionInfo != null) {
				formId = actionInfo.getFormId();
				template = actionInfo.getTemplate();
			}
		} else {
			isTransmit = true;
			//转发逻辑更改：改为对当前工单不做任何处理，增加一个转发意见
			//处理多次转发的情况
			TaskData p_ = taskData;
			while (p_ != null && p_.getNodeId().equals(toNodeId)) {
				if (p_.getParent() == null)
					break;
				p_ = p_.getParent();
			}
			NodeInfo NItemp_ = wfData.getNode(p_.getNodeId());
			//如果在根节点转发
			if (NItemp_ == wfData.getRootNode() && toNode == wfData.getRootNode())
				actionInfo = (ActionInfo) toNode.getActions().get(0);
			else
				actionInfo = NItemp_.getAction(toNodeId);

			if (actionInfo != null) {
				//例如：A----线1---->B，如果当前停留在节点B，要转发的话
				//转发表单的读取过程：先取当前节点（B）上的转发表单配置，如果为-1（没有指定）
				//则取动作节点（线1）的转发表单配置，如果为-1，则取动作节点（线1）的关联表单
				//展示模板统一用节点(B)上的转发模板
				formId = fromNode.getTransmitFormId();
				if (formId.equals("-1") || formId.equals("0")) {
					formId = actionInfo.getTransmitFormId();
				}
				if (formId.equals("-1") || formId.equals("0")) {
					formId = actionInfo.getFormId();
				}
			} else {
				formId = fromNode.getTransmitFormId();
			}
			template = fromNode.getTransmitTemplate();

		}
		canDealThisNode = taskData.canDealThisNode(userId);

	} else {
		wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
		wfData = wfInfo.getCurrentVersion();
		fromNode = wfData.getRootNode();
		toNode = wfData.getRootNode();
		if (toNode == null) {
			out.println("<script defer>Ext.MessageBox.alert('错误', '此流程没有设置根结点,请与管理员联系.');</script>");
			return;
		}
		actionInfo = (ActionInfo) toNode.getActions().get(0);
		toNode = actionInfo.getToNode();
		formId = actionInfo.getFormId();
		template = actionInfo.getTemplate();
		
		taskInfo = new TaskInfo(wfData, userId);
		taskData = new TaskData(taskInfo, null, fromNode,actionInfo.getActionId(),"", userId);
		if ("copyCreate".equals(operType)) {
			TaskData taskDataTemp_ = (TaskData) session.getAttribute("copyCreateTaskData");
			taskData.setValue(taskDataTemp_.getValues());
		}
		canDealThisNode = true;
	}
	taskData.setAssignTo("");

	//取编辑保存的信息
	TaskData editData = taskData.getChildEditing(toNode.getId());
	if (editData != null) {
		Map editDataValues = editData.getValues();
		if (editDataValues != null && editDataValues.size() > 0) {
			Object[] keys = editDataValues.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				taskData.setValue((String) keys[i], (String) editDataValues.get(keys[i]));
			}
			taskData.setAssignTo(taskData.getValue(Consts.FLD_EXECUTE_USER));
		}
	}

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

	//如果是合流节点，把下一处理人，默认为分流开始节点的分配人
	//if (toNode.getType() == NodeInfo.TYPE_BRANCH_END) {
		// 取开始分支节点
		//TaskData b = TaskManager.getBranchBegin(taskData, wfData);
		//taskData.setAssignTo(b.getAssignTo());
	//}
	
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
	//转发表单
	/*if (isTransmit) {
	 FieldInfo p_ = FieldManager.getFieldById(Consts.FLD_EXECUTE_USER);
	 fieldList.add(p_);
	 FieldInfo r_ = FieldManager.getFieldById(Consts.FLD_MESSAGE);
	 fieldList.add(r_);
	 } else {*/
	if (formInfo != null) {
		fieldList = formInfo.getFields();
	}
	//}
	
%>
<script defer>
var _defaults = {
	currentUserId : "<%=PersonManager.getRemoteId(origin, userId)%>",
	currentUserName : "<%=PersonManager.getPersonNameById(origin, PersonManager.getRemoteId(origin, userId))%>"
};
var _wf_status = {
	id : "<%if (taskData != null)
	out.print(taskData.getNodeId());%>",
	name : "<%if (taskData != null)
	out.print(taskData.getNodeDesc());%>"
};

var simple = new Ext.form.FormPanel({
	method: 'POST',
	autoScroll: true,
	frame:true,
	borderStyle:'border 0 none',
	buttonAlign: 'right',
	defaultType: 'textfield',
	baseParams: {
		taskOid: "<%=taskOid%>",
		origin: "<%=origin%>",
		taskDataId: "<%=taskDataId%>",
		wfOid: "<%=wfOid%>",
		operType: "edit",
		closeBranchNode: "0",
		actionId: "<%=actionInfo.getActionId()%>",
		formOid:"<%=formId%>",
		fromNodeId: "<%=fromNode.getId()%>",
		toNodeId: "<%=toNodePath%>",
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
	context.put("toNodePath", toNodePath);
	context.put("actionInfo", actionInfo);
	context.put("wfOid", wfOid + "");
	context.put("taskOid", taskOid + "");
	context.put("taskData", taskData);
	context.put("taskDataId", taskDataId + "");
	context.put("formId", formId);
	context.put("ItsmUtil", new ItsmUtil());
	context.put("request", request);
	context.put("origin", origin);
	context.put("formFields", new ArrayList());
	context.put("attachments", taskData);
	context.put("TemplateManager", new TemplateManager());
	String templateFile = (template == null||template.equals("")) ? "/default/nodeAction.html" : template;
	out.println(TemplateUtil.getHTMLStrFromTemplate(templateFile, context));
	
	
%>

function submitTaskEditForm(closeBranchNode)
{
	var form = simple.form;


		<%if (isTransmit && fromNode != null && fromNode.getType() == NodeInfo.TYPE_BRANCH_BEGIN
						&& taskOid != -1) {
						%>
		closeBranchNode = 1;
		<%}%>

	form.baseParams.closeBranchNode = closeBranchNode;
	//如果是保存草稿，不验证必填限制
	if (closeBranchNode == <%=TaskUpdateInfo.TYPE_SAVE_FOR_EDIT%>) {
		form.items.each(function(f){
		   f.allowBlank = true;
		});
	}
	
	if (!form.isValid()) {

		form.items.each(function(f){
			   if(!f.allowBlank && f.getValue() == "")
				   alert("请填写完整的内容: [" + f.fieldLabel + "]");
		});
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
		var wfVer = parent.linkds.getAt(i).get("wfVer");
		if (i>0)
			linkStr += ",";
		linkStr += origin+"_"+wfOid+"_"+oid+"_"+wfVer;
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

function closeTaskData() {
	var form = simple.form;
	form.baseParams.operType="closeBranch";
	form.submit({
		waitMsg: '正在处理，请稍候...',
		success: function(form, action)
		{
			Ext.MessageBox.alert("信息", form.errorReader.xmlData.documentElement.text,callBackFun);

		},
		failure: function(form, action)
		{
			Ext.MessageBox.alert("失败", form.errorReader.xmlData.documentElement.text);
		}
	});
}	

function callBackFun(btn){
	try {
		var wp = window.parent.opener;
		if (wp!=null && typeof(wp) != "undefined " && !wp.closed ) {
			if(wp.viewds)			
				wp.viewds.reload();
			<%
				if(request.getAttribute("returnURL") != null)
					out.println("wp.location.href='"+request.getAttribute("returnURL")+"';");
			%>
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

function chanageInputBorder(tagname) {
	  var txts = document.getElementsByTagName(tagname);
	  for(var i = 0; i < txts.length; i++) {
	  	if(txts[i].readOnly && txts[i].className.indexOf('text-disable')>-1) {
	  		continue;
	  	}
	    txts[i].onmouseover = function() {      
	      this.style.borderColor = 'green';
	      this.style.borderWidth = '2px';
	    }
	    txts[i].onmouseout = function() {
	      this.style.borderColor = '#99BBE8';
	      this.style.borderWidth = '1px';
	    }
	  }
};


Ext.onReady(function() {
	
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
		<%}
		//add by zhonganjing
		if (canDealThisNode) {
		%>
		simple.addButton(
			{text: '提交'},
			function() {
				submitTaskEditForm("0");
			}
		);
		<%if (!isTransmit && fromNode != null && fromNode.getType() == NodeInfo.TYPE_BRANCH_BEGIN && taskOid != -1) {%>
		simple.addButton(
			//{text: '结束派单'},
			//function() {
				//closeTaskData();
			//}
			{text: '提交并结束派单'},
			function() {
				submitTaskEditForm("1");
			}
		);
	<%	}
		}
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
	simple.form.isValid();

<%
for (int i = 0; i < fieldList.size(); i++) {
	FieldInfo fieldInfo = (FieldInfo) fieldList.get(i);
	//out.println(fieldInfo.getType());
	if (fieldInfo.getType().equals("com.hp.idc.itsm.configure.fields.RadioFieldInfo"))
		continue;
	String value = "";
	if (taskData != null) {
		if (fieldInfo.getId().equals(Consts.FLD_EXECUTE_USER)) {
			if (editData != null) {
				value = editData.getValue(Consts.FLD_EXECUTE_USER);
			} else {
				value = taskData.getAssignTo();
			}
		} else {
			if (editData != null) {
				value = editData.getValue(fieldInfo.getId());
			} else {
				if (fieldInfo.isInjectData()) {//如果字段允许继承填充数据
					value = taskData.getAttribute(fieldInfo.getId());
				}
			}
		}
		if (value == null || value.equals(""))
			continue;
		fieldInfo.setValue(value);
	}

	out.println(fieldInfo.getFormValue());
	//out.println("simple.getForm().findField('fld_"+fieldInfo.getId()+"').setValue('"+value+"');");
}
%>

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

//Ext.DomHelper.insertBefore(grid.getEl(), {id: 'my-ul', tag: 'label', cls: 'x-form-item-label', html: 'male'});
chanageInputBorder('input');
chanageInputBorder('textarea');

});

</script>
<%
	} catch (Exception ex) {
		ex.printStackTrace();
	}
%>

</body>
</html>
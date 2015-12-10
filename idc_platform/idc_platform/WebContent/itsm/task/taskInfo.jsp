<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.inter.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ include file="../getUser.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/FusionCharts.js"></script>

<%
try {
int taskOid = -1;
if (request.getParameter("taskOid")!=null && !request.getParameter("taskOid").equals(""))
	taskOid = Integer.parseInt(request.getParameter("taskOid"));
int taskDataId = -1;
if (request.getParameter("taskDataId")!=null && !request.getParameter("taskDataId").equals(""))
	taskDataId = Integer.parseInt(request.getParameter("taskDataId"));

int wfOid = -1;
if (request.getParameter("wfOid")!=null && !request.getParameter("wfOid").equals(""))
	wfOid = Integer.parseInt(request.getParameter("wfOid"));
int wfVer = -1;
if (request.getParameter("wfVer")!=null && !request.getParameter("wfVer").equals(""))
	wfVer = Integer.parseInt(request.getParameter("wfVer"));

String origin = request.getParameter("origin");

/*
 * operType=="copyCreate",是复制现有记录创建的
 * operType!="copyCreate"&&taskOid==-1,新建的
 * taskOid!=-1,正常的流程流转
 */
String operType = request.getParameter("operType");

TaskInfo taskInfo = null;
TaskData taskData = null;
WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
if (wfInfo == null) {
	out.println("<script defer>Ext.MessageBox.alert('错误', '找不到流程信息(wfOid=" + wfOid + "),<br>请与管理员联系.');</script>");
	return;
}
WorkflowData wfData = null;
NodeInfo nodeInfo = null;

List btnList = new ArrayList();
List singleList = new ArrayList();
List actionList = new ArrayList();

//分支开始节点是否已经开始处理，如果已经派单了一个分支，则不能转发
boolean branchBeginIsDealing = false;

if (taskOid!=-1) {
	taskInfo = TaskManager.getTaskInfoByOid(origin,taskOid,wfOid,wfVer);
	taskData = taskInfo.getTaskData(taskDataId);
	wfData = wfInfo.getVersion(taskInfo.getWfVer());
	nodeInfo = wfData.getNode(taskData.getNodeId());
	
	/**
	List lll = new ArrayList();
	//taskData.getBranchEnd(lll,wfData,0);
	taskData.getLastData(lll);
	System.out.println(lll.size());
	
	List llb = new ArrayList();
	taskData.getBranchEnd(lll,wfData,0);
	//taskData.getLastData(lll);
	System.out.println(llb.size());
	System.out.println(llb.containsAll(lll));
	
	System.out.println(((TaskData)(lll.get(2))).getNodeDesc());
	System.out.println(taskInfo.getLatestTaskDataId());
	List<ActionInfo> al = wfData.getNode(((TaskData)(lll.get(1))).getNodeId()).getActions();
	System.out.println(al.get(0).getToNodeId());
	***/

	//btnList.add("{text: '历史记录', handler: taskHistoryView, origin:'" + origin + "', taskOid:" +
	//	taskOid + ", dataId: " + taskDataId + ",wfOid:"+wfOid+"}");

	if (origin.equals("ITSM")) {
		btnList.add("{text: '发表意见',icon:'../images/go-to-post.gif',iconCls:'x-btn-text-icon', handler: taskAddMessage, origin:'" + origin + "', taskOid:" +
			taskOid + ", dataId: " + taskDataId + ",wfOid:" + wfOid + "}");
		btnList.add("{text: '工单催办',icon:'../images/user_suit.gif',iconCls:'x-btn-text-icon', handler: sendSms, sendTo:'" + taskData.getAssignTo() + "',wfOid:" +
			wfOid + ",taskTitle:'"+StringUtil.escapeJavaScript(taskData.getAttribute("req_title"))+"',taskOid:'"+taskInfo.getOid()+"',taskStatus:'"+taskData.getNodeDesc()+"'}");
	}
	if (userId.equals(taskInfo.getCreatedBy()) && taskInfo.getStatus() == TaskInfo.STATUS_OPEN) {
		btnList.add("{text: '强行关闭',icon:'../images/cross.gif',iconCls:'x-btn-text-icon', handler: taskForceClose, origin:'" + origin + "', taskOid:" +
			taskOid + ", dataId: " + taskDataId + ",wfOid:" + wfOid + "}");
	}
	if (nodeInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN){
		if (taskData.getActivateChilds().size()>0)
			branchBeginIsDealing = true;
	}
	//System.out.println(taskData.getAssignType()+":"+TaskData.ASSIGN_PERSON+"/"+taskData.canDealThisNode(userId));
	if (taskData.getAssignType() == TaskData.ASSIGN_PERSON && (taskData.canDealThisNode(userId)&& taskData.getStatus() == TaskData.STATUS_OPEN || nodeInfo.isShareDeal()))
	{
		//如果有协助人员（就是协助处理的人员，相当于一条工单多人都可以处理）
		if (taskData.getAssistant()!=null && !taskData.getAssistant().equals("")) {
			btnList.add("{text:'接手处理',icon:'../images/plugin.gif',iconCls:'x-btn-text-icon', handler: taskAccept, origin:'" + origin + "',wfOid: " + wfOid + ",taskOid:" + taskOid + ", dataId: " +
					taskDataId + " }");
		} else {
			boolean ret = false;
	/*		if (nodeInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN)
					ret = taskData.getNextAction(actionList, wfData);
				else if (nodeInfo.getType() == NodeInfo.TYPE_SUB_WF){*/
				ret = taskData.getSubflowActions(wfData,taskData.getNodePath(),new Stack(),actionList);
	
	/*			if (actionList.size() == 0)
						actionList = nodeInfo.getActions();
				} else
					actionList = nodeInfo.getActions();*/
			if (actionList.size()>0)
			{
				if (ret) {
					btnList.add("{text:'结束分配',icon:'../images/icon-all.gif',iconCls:'x-btn-text-icon', handler: taskCloseBranch, origin:'" + origin + "', taskOid:" +
						taskOid + ", dataId: " + taskDataId + ",wfOid:" + wfOid + "}");
				}
			}
			if (taskData.isRollbackable()) {
				btnList.add("{text: '回退',icon:'../images/arrow-left.gif',iconCls:'x-btn-text-icon', handler: taskRollback,wfOid:" + wfOid + ", origin:'" + origin + "', taskOid:" +
					taskOid + ", dataId: " + taskDataId + ", wfVer: "+wfVer+"}");
			}
		}
	} else {
		PersonInfoInterface person = PersonManager.getPersonById(origin,userId);
		if (taskData.getAssignType() == TaskData.ASSIGN_WORKGROUP) {
			//WorkgroupInfoInterface workgroup = WorkgroupManager.getWorkgroupById(origin,taskData.getAssignTo());
			//登录人是所分配的工作组人员，或者有超级处理工单权限
			if ((person != null && person.isInWorkgroup(taskData.getAssignTo(), true))||taskData.canDealThisNode(userId)) {
				btnList.add("{text:'接手处理',icon:'../images/plugin.gif',iconCls:'x-btn-text-icon', handler: taskAccept, origin:'" + origin + "',wfOid: " + wfOid + ",taskOid:" + taskOid + ", dataId: " +
					taskDataId + " }");
			}
		}		
	}
	//btnList.add("{text:'关闭窗口',icon:'../images/application_go.png',iconCls:'x-btn-text-icon', handler: function(){window.close();}}");
} else {//新建时
	wfData = wfInfo.getCurrentVersion();
	if (wfData == null) {
		out.println("<script defer>Ext.MessageBox.alert('错误', '找不到流程版本信息(wfOid=" + wfOid + ";ver=CurrentVersion),<br>请与管理员联系.');</script>");
		return;
	}
	nodeInfo = wfData.getRootNode();
	if (nodeInfo == null) {
		out.println("<script defer>Ext.MessageBox.alert('错误', '此流程没有设置根结点,请与管理员联系.');</script>");
		return;
	}

	actionList = nodeInfo.getActions();
	ActionInfo actionInfo_  = (ActionInfo)actionList.get(0);
	NodeInfo ni_ = actionInfo_.getToNode();
	if (ni_.getType() == NodeInfo.TYPE_SUB_WF)
		actionInfo_.setToNodePath(actionInfo_.getToNodeId()+"/"+actionInfo_.getSubToNodeId());
	else
		actionInfo_.setToNodePath(actionInfo_.getToNodeId());
}
%>
  <title>工单信息-[<%=wfInfo.getName()%>-<%if (taskInfo!=null)out.print(taskInfo.getOid());else out.print("新建工单");%>]</title>


</head>
<body>
<%

//是否显示动作panel
boolean showActionPanel = false;

if(taskOid==-1 || (actionList.size()>0 && taskData.canDealThisNode(userId)) || nodeInfo.isShareDeal()) {
	showActionPanel = true;
}

String defaultSrc = Consts.ITSM_HOME+"/task/taskViewNode.jsp";
if (taskOid == -1)
	defaultSrc = Consts.ITSM_HOME+"/task/taskActionNode.jsp";
defaultSrc+="?origin="+origin+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&wfOid="+wfOid+"&wfVer="+wfVer;

/*String formId = nodeInfo.getFormId();
FormInfo formInfo = FormManager.getFormByOid(formId);
List fieldList = null;
if (formInfo != null)
	fieldList = formInfo.getFields();
else {
	fieldList = new ArrayList();
	out.println("<script defer>alert('找不到表单信息,请与管理员联系.');</script>");
}*/

%>

<iframe id="excelOut" style="display:none"></iframe>
<div id='task_info_north'>

</div>
<script defer>

function finishSms(){
	var fld_notice_to = new Ext.form.TextField({
		fieldLabel:"手机号",
		name :'fld_notice_to',
		width:200, 
		allowBlank:false, 
		validator:function(v){var c=/^1\d{10}$/;if (c.test(v)) return true; return '请输入一个正确的手机号';}
	});
	var fld_notice_message = new Ext.form.TextArea({fieldLabel:"通知内容",name:'fld_notice_message',height:80,width:200, allowBlank:false});
	fld_notice_message.setValue(this.content);
	var finishSmsForm = new Ext.form.FormPanel({
	    baseCls: 'x-plain',
	    border: false,
	    labelWidth: 55,
	    defaultType: 'textfield',
	    items: [fld_notice_to,fld_notice_message],
	    errorReader: new Ext.form.XmlErrorReader(),
	    url:'<%=Consts.ITSM_HOME%>/task/taskPost.jsp',
	    baseParams: {
				operType: "sendSms",wfOid:"<%=wfOid%>"
			}
		});

	var finishWindow = null;
	if (finishWindow) {
		finishWindow.close();
	}
	finishWindow = new Ext.Window({
	      title: '短信提醒',
	      width: 300,
	      height:200,
	      minWidth: 300,
	      minHeight: 200,
	      plain:true,
	      iconCls :'edit',
	      buttonAlign:'center',
	      modal:true,
	      items: [finishSmsForm],
	      buttons: [{
	          text: '发送',
	          handler: function() {
	          	var smsForm = finishSmsForm.form;
	          	if (!smsForm.isValid()) {
					Ext.MessageBox.alert("提示", "请填写完整的内容");
					return;
				}
				smsForm.submit({
					waitMsg: '<%=Consts.MSG_WAIT%>',
					success: function(form, action)
					{
						Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text);
						finishWindow.close();
					},
					failure: function(form, action)
					{
						form.items.each(function(f){
						   if (f.old_status_disabled)
							   f.disable();
						});
						Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_FAILED%>", form.errorReader.xmlData.documentElement.text);
			    	}
				});
	          }
	      },{
	          text: '关闭',
	          handler: function() {
	    	  	finishWindow.close();
	          }
	      }]
  	});
	finishWindow.show('finishsmstask');
}

var window_viewTask = new Ext.Window({});
//重置flash的高宽比
function reSize(objId,w,h){
	if (objId && objId!="") {
		getFlashMovie(objId).width=w+10;
		getFlashMovie(objId).height=h+10;
	}
}
<%
//	生成流程图的xml对象
	Document docGraph = XmlUtil.parseString("<workflow/>");
	Element rootEl = docGraph.getRootElement();
	List<NodeInfo> nodeList = wfData.getNodes();
	NodeInfo rootNodeInfo = wfData.getRootNode();
	for (int i = 0; i < nodeList.size(); i++) {
		NodeInfo node = nodeList.get(i);
		boolean isRoot = false;
		if (rootNodeInfo.getId().equals(node.getId()))
			isRoot = true;
		Element nodeEl_ = (Element)wfData.getXmlDoc().selectSingleNode("./node[@id='"+node.getId()+"']");
		Element nodeEl = rootEl.addElement("node");
		nodeEl.addAttribute("id", node.getId());
		nodeEl.addAttribute("name", (isRoot?"(*)":"")+node.getCaption());
		nodeEl.addAttribute("width", 76+"");
		nodeEl.addAttribute("height", 30+"");
		nodeEl.addAttribute("locked", "true");
		
		//在flash里是以中心点为0点的，为了配合vml的画图布局，px,py都加了一个偏移量
		int px = Integer.parseInt(nodeEl_.attributeValue("posX"));
		int py = Integer.parseInt(nodeEl_.attributeValue("posY"))+20;
		if (nodeInfo.getId().equals(node.getId()))
			nodeEl.addAttribute("selected", "true");
		if (node.getType() == NodeInfo.TYPE_BRANCH_BEGIN) {
			nodeEl.addAttribute("type", "branchBegin");
			px += 5;
		} else if (node.getType() == NodeInfo.TYPE_BRANCH_END) {
			nodeEl.addAttribute("type", "branchEnd");
			px += 5;
		} else {
			nodeEl.addAttribute("type", "normal");
			px += 30;
		}
		nodeEl.addAttribute("px", px+"");
		nodeEl.addAttribute("py", py+"");
		
		List<ActionInfo> actionList_ = node.getActions();
		for (int j = 0; j < actionList_.size(); j++){
			ActionInfo actionInfo_ = actionList_.get(j);
			Element actionEl_ = (Element)nodeEl_.selectSingleNode("./actions/action[@to='"+actionInfo_.getToNodeId()+"']");
			Element lineEl = rootEl.addElement("line");
			lineEl.addAttribute("id", node.getId()+"_line_"+j);
			//lineEl.addAttribute("name", actionInfo_.getActionName());
			lineEl.addAttribute("type", "polyline");

			String fp = actionEl_.attributeValue("fromPos");
			String fpp = "";
			if (fp.equals("1"))
				fpp = "T";
			else if (fp.equals("2"))
				fpp = "L";
			else if (fp.equals("3"))
				fpp = "R";
			else if (fp.equals("4"))
				fpp = "B";
				
			String tp = actionEl_.attributeValue("toPos");
			String tpp = "";
			if (tp.equals("1"))
				tpp = "T";
			else if (tp.equals("2"))
				tpp = "L";
			else if (tp.equals("3"))
				tpp = "R";
			else if (tp.equals("4"))
				tpp = "B";
				
			lineEl.addAttribute("bNodePoint", fpp);
			lineEl.addAttribute("eNodePoint", tpp);
			lineEl.addAttribute("fromNodeId", node.getId());
			lineEl.addAttribute("toNodeId", actionInfo_.getToNodeId());
			
		}

	}
%>
var chart0 = new FusionCharts("TaskHistoryViewer.swf", "", "100%", "100%", "0", "1");
chart0.setTransparent(true);
chart0.setDataXML("<%=rootEl.asXML().replaceAll("\"","\\\\\"")%>");

//历史流程图节点点击动作
function displayNode(params,taskOid,taskDataId,origin,wfOid,viewMode,xsl) {
	var p = params;
	if (!p.taskOid) {
		params = {};
		params.taskOid = p[0];
		params.taskDataId = p[1];
		params.origin = p[2];
		params.wfOid = p[3];
		params.wfVer = <%=wfVer%>;
	}
	var url = "<%=Consts.ITSM_HOME%>/task/taskHistoryDetail.jsp";
	task_view_detail.load({
	    url: url,
	    params:params,
	    discardUrl: false,
	    text: "Loading...",
	    scripts: false
	});
	task_view_detail.body.highlight('#c3daf9', {block:true});
}

<%
	Map context = new HashMap();
	context.put("Consts", new Consts());
	context.put("taskInfo", taskInfo);
	out.println(TemplateUtil.getHTMLStrFromTemplate("/default/taskLinkPanel.html",context));
%>
//加载查看节点信息
function loadTaskInfoDetail(){
	task_view_detail.load({
    url: "<%=defaultSrc%>",
    discardUrl: false,
    text: "Loading...",
    scripts: false
	});
	task_view_detail.body.highlight('#c3daf9', {block:true});
}
var task_view_detail = new Ext.Panel({
	region:'center',
	border:false,
	style :'border-top:1px solid',
	id:'task_view_detail',
	autoScroll:true,
	tbar:['->',{
			text:"查看所有",
			icon :'../images/view_list.gif',
			iconCls :'x-btn-text-icon',
			handler:function(){
				var params = {};
				params.taskOid = <%=taskOid%>;
				params.taskDataId = 0;
				params.origin = "<%=origin%>";
				params.wfOid = <%=wfOid%>;
				params.wfVer = <%=wfVer%>;
				params.viewMode = "viewAllNode";
				displayNode(params);
			}
		}, '-' ,{
			text:"导出到EXCEL",
			icon :'../images/xls.gif',
			iconCls :'x-btn-text-icon',
			handler:function(){
					excelOut.location.href="<%=Consts.ITSM_HOME%>/task/taskHistoryDetail.jsp?taskOid=<%=taskOid%>&viewMode=viewAllNode&xls=1&origin=<%=origin%>&wfOid=<%=wfOid%>&wfVer=<%=wfVer%>";
			}
		}]
});

<%if (taskOid != -1) {%>
var chart1 = new FusionCharts("TaskHistoryViewer.swf", "TaskHistoryViewer", "40", "30", "0", "1");
<%
Document doc = XmlUtil.parseString("<workflow/>");
Element el = doc.getRootElement();
TaskManager.printXML(el,taskInfo.getRootData(),wfData,new HashMap(),40,20,true);
%>
chart1.setDataXML("<%=el.asXML().replaceAll("\"","\\\\\"")%>");

var task_info_history = new Ext.Panel({
	title: '历史节点信息',
	id:'task_info_history',
	layout: 'border',
	items:[{
		region:'north',
		id:'history-graphics-panel',
		split: true,
		height: 60,
		minSize: 50,
		maxSize: 300,
		autoScroll:true,
		border:false,
		bodyStyle :'border-bottom:1px solid',
		html:chart1.getSWFHTML()//.replace(/\"/g,"\\\"")
	},task_view_detail]
});

var task_flow_log = new Ext.Panel({title: '工单流转日志', 
	autoLoad : {url:"taskFlowLog.jsp?origin=ITSM&taskOid=<%=taskOid%>&wfOid=<%=wfOid%>&wfVer=<%=wfVer%>"}
});

<%}%>

	//chart1.render("task_info_north");
	
var task_info_center = new Ext.TabPanel({
	region:'center',
	id:'center-panel',
	activeTab: 0,
	border:false,
	items:[
	<%
	boolean hasAct = false;
	if(showActionPanel) {

		/*仅仅为了新建工单的时候界面好看些*/
		if(taskOid ==-1) {
			ActionInfo actionInfo  = (ActionInfo)actionList.get(0);
		   	String url = "";
			if (actionInfo.getFormSelectMode().equals("remote"))
				url = actionInfo.getRemoteDealPage()+"?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+actionInfo.getToNodePath();
			else
	   			url = Consts.ITSM_HOME+"/task/taskActionNode.jsp?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+actionInfo.getToNodePath()+"&operType="+operType;

%>
			new Ext.ux.IFrameComponent({title: '<%=actionInfo.getActionName()%>',
		    	id:'act_<%=nodeInfo.getId()%>_<%=actionInfo.getToNodeId()%>',
		    	iconCls: 'views',
		    	url:'<%=url%>',
		    	autoScroll:true,
		    	region:'center',
		    	closable:false
			})

<%
			hasAct = true;
		} else {
    	int displayAheadIndex = -1;
    	for (int i = 0; i < actionList.size(); i++) {
    		ActionInfo actionInfo  = (ActionInfo)actionList.get(i);
    		if (actionInfo.isDisplayAhead()){
    			String url = "";
    			if (actionInfo.getFormSelectMode().equals("remote"))
    				url = actionInfo.getRemoteDealPage()+"?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+actionInfo.getToNodePath();
    			else
	      		url = Consts.ITSM_HOME+"/task/taskActionNode.jsp?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+actionInfo.getToNodePath();
%>
    	 new Ext.ux.IFrameComponent({title: '<%=actionInfo.getActionName()%>',
	      	id:'act_<%=nodeInfo.getId()%>_<%=actionInfo.getToNodeId()%>',
	      	iconCls: 'views',
	      	url:'<%=url%>',
	      	autoScroll:true,
	      	closable:false
	     })
    <%
    			hasAct = true;
    			displayAheadIndex = i;
    			break;
    		}
    	}
    	for (int i = 0; i < actionList.size(); i++) {
    		ActionInfo actionInfo  = (ActionInfo)actionList.get(i);
    		if (i==displayAheadIndex)
    			continue;
  			String url = "";
  			if (actionInfo.getFormSelectMode().equals("remote"))
  				url = actionInfo.getRemoteDealPage()+"?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+actionInfo.getToNodePath();
  			else
      		url = Consts.ITSM_HOME+"/task/taskActionNode.jsp?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+actionInfo.getToNodePath();
    		if(hasAct)
    			out.println(",");
    		hasAct = true;
    %>
    	 new Ext.ux.IFrameComponent({title: '<%=actionInfo.getActionName()%>',
	      	id:'act_<%=actionInfo.getToNodePath()%>',
	      	iconCls: 'views',
	      	url:'<%=url%>',
	      	autoScroll:true,
	      	closable:false
	     })
	  <%}
    	// not show the forward page; add by zhonganjing
	   if (false && taskData.isForwardable() && !branchBeginIsDealing){
	   	if (hasAct)out.println(",");
	   	String url = "";
	   	TaskData td_ = taskData.getParent();
	   	NodeInfo fromNode = wfData.getNode(td_.getNodeId());
	   	ActionInfo ai_ = fromNode.getAction(nodeInfo.getId());
	   	if (ai_!=null && ai_.getFormSelectMode().equals("remote"))
	   		url = ai_.getRemoteDealPage()+"?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+taskData.getNodePath();
	   	else
	      url = Consts.ITSM_HOME+"/task/taskActionNode.jsp?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+taskData.getNodePath();
	  %>
	  	new Ext.ux.IFrameComponent({title: '转发',
	      	id:'act_<%=nodeInfo.getId()%>_<%=taskData.getNodeId()%>',
	      	iconCls: 'views',
	      	url:'<%=url%>',
	      	autoScroll:true,
	      	closable:false
	     })
<%
			}
	}
	if(hasAct)
		out.println(",");
	out.println("linkTaskGrid");
}
	
%>	

<%
if(taskOid !=-1){
	if(hasAct)
		out.println(",");
	out.print("task_info_history, task_flow_log");
	hasAct = true;
}
if(hasAct)
	out.println(",");
%>
		{
	    id:'east-panel',
	    title: '流程图',
	    autoScroll:true,
	    minSize: 175,
	    maxSize: 400,
	    margins:'1 1 1 1',
	    html:chart0.getSWFHTML()
	    //contentEl: task_info_east
		}
	]
});

task_info_center.on("tabchange",function(tabPanel,tab){
	if(tab.getId()=='task_info_history'){
		loadTaskInfoDetail();
	}
});

var infoAll = new Ext.Panel({
	region:'center',
	id:'infoAll-panel',
	layout:'border',
	items:task_info_center,
	tbar:[
	<%
	if (btnList.size()>0){
		for(int i = 0; i < btnList.size(); i++) {
			if (i>0)
				out.print(",'-',");
			//if (i == btnList.size()-1)
				//out.print(",'->',");
			out.println(btnList.get(i).toString());
		}
		out.println(",");
	}
	%>
	'->', {
		//add by zhonganjing for showing user name
		text: <% out.print("'<i>当前操作人: <b>"+userName + "</b></i>'"); %>
		}, '-', {text:'关闭窗口',icon:'../images/application_go.png',iconCls:'x-btn-text-icon', handler: function(){window.close();}} 
	]
});

Ext.onReady(function() {
  var viewport = new Ext.Viewport({
	  layout:'border',
      items:[infoAll]
	});
});

function taskPostCallBack(ret){
	/*if (!ret)
		document.location.reload();
	else {
		var p = parent;
		while(!p.document.frames["displayForm"]){
			p = p.parent;
		}
		p.addTab('view_<%=session.getAttribute("viewOid")%>','<%=session.getAttribute("viewName")%>','<%=Consts.ITSM_HOME%>/configure/viewTemplate.jsp?viewOid=<%=session.getAttribute("viewOid")%>','views');
	}*/
	if (top.opener && top.opener.viewds)
		top.opener.viewds.reload();
	top.window.close();
}

function showDetail(dataId){
    var win = new Ext.Window({
        layout      : 'fit',
        width  : 600,
        height      : 400,
        autoScroll : true,
        autoLoad : {url:"taskHistoryDetail.jsp?origin=<%=origin%>&taskDataId="+dataId+"&taskOid=<%=taskOid%>&wfOid=<%=wfOid%>&wfVer=<%=wfVer%>"},          
        buttons: [{
            text     : '关闭',
            handler  : function(){
                win.close();
            }
        }]
    });
	win.show('node-detail-'+dataId);
}

</script>
<%} catch (Exception ex) { ex.printStackTrace(); } %>

</body>
</html>
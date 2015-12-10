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
if (taskOid == -1) {
	//新建时
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

} else {
	
	taskInfo = TaskManager.getTaskInfoByOid(origin,taskOid,wfOid,wfVer);
	taskData = taskInfo.getTaskData(taskDataId);
	wfData = wfInfo.getVersion(taskInfo.getWfVer());
	nodeInfo = wfData.getNode(taskData.getNodeId());

	if (origin.equals("ITSM")) {
		btnList.add("{text: '发表意见',icon:'../images/go-to-post.gif',iconCls:'x-btn-text-icon', handler: taskAddMessage, origin:'" + origin + "', taskOid:" +
			taskOid + ", dataId: " + taskDataId + ",wfOid:" + wfOid + "}");
		btnList.add("{text: '工单催办',icon:'../images/user_suit.gif',iconCls:'x-btn-text-icon', handler: sendSms, sendTo:'" + taskData.getAssignTo() + "',wfOid:" +
			wfOid + ",taskTitle:'"+StringUtil.escapeJavaScript(taskData.getTitle())+"',taskOid:'"+taskInfo.getOid()+"',taskStatus:'"+taskData.getNodeDesc()+"'}");
	}
	if (userId.equals(taskInfo.getCreatedBy()) && taskInfo.getStatus() == TaskInfo.STATUS_OPEN) {
		btnList.add("{text: '强行关闭',icon:'../images/cross.gif',iconCls:'x-btn-text-icon', handler: taskForceClose, origin:'" + origin + "', taskOid:" +
			taskOid + ", dataId: " + taskDataId + ",wfOid:" + wfOid + "}");
		
		if (wfInfo.getChangeWfOid()!=-1){
			btnList.add("{text: '申请变更',icon:'../images/cog_edit.gif',iconCls:'x-btn-text-icon', handler: function(){"+
				"openURL('"+Consts.ITSM_HOME+"/task/changeApply.jsp?wfOid=" + wfInfo.getChangeWfOid() + "&masterTaskOid="+taskInfo.getOid()+"&masterWfOid="+wfOid+"&masterWfVer="+wfVer+"');}}");
			
		}
	}
	if (nodeInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN){
		if (taskData.getActivateChilds().size()>0)
			branchBeginIsDealing = true;
	}
	if (taskData.getAssignType() == TaskData.ASSIGN_PERSON && taskData.canDealThisNode(userId)&& taskData.getStatus() == TaskData.STATUS_OPEN || nodeInfo.isShareDeal())
	{
		//如果有协助人员（就是协助处理的人员，相当于一条工单多人都可以处理）
		if (taskData.getAssistant()!=null && !taskData.getAssistant().equals("")) {
			btnList.add("{text:'接手处理',icon:'../images/plugin.gif',iconCls:'x-btn-text-icon', handler: taskAccept, origin:'" + origin + "',wfOid: " + wfOid + ",taskOid:" + taskOid + ", dataId: " +
					taskDataId + " }");
		} else {
			boolean ret = taskData.getSubflowActions(wfData,taskData.getNodePath(),new Stack(),actionList);
			if (actionList.size()>0)
			{
				if (ret) {
					btnList.add("{text:'结束分配',icon:'../images/icon-all.gif',iconCls:'x-btn-text-icon', handler: taskCloseBranch, origin:'" + origin + "', taskOid:" +
						taskOid + ", dataId: " + taskDataId + ",wfOid:" + wfOid + "}");
				}
			}
			if (taskData.isRollbackable()) {
				btnList.add("{text: '回退',icon:'../images/arrow-left.gif',iconCls:'x-btn-text-icon', handler: taskRollback,wfOid:" + wfOid + ", origin:'" + origin + "', taskOid:" +
					taskOid + ", dataId: " + taskDataId + "}");
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
		if (taskData.getAssignType() == TaskData.ASSIGN_ORGANIZATION) {
			//登录人是所分配的工作组人员，或者有超级处理工单权限
			if ((person != null && person.isInOrganization(taskData.getAssignTo(), true))||taskData.canDealThisNode(userId)) {
				btnList.add("{text:'接手处理',icon:'../images/plugin.gif',iconCls:'x-btn-text-icon', handler: taskAccept, origin:'" + origin + "',wfOid: " + wfOid + ",taskOid:" + taskOid + ", dataId: " +
					taskDataId + " }");
			}
		}
	}
}
%>
  <title>工单信息-[<%=wfInfo.getName()%>-<%if (taskInfo!=null)out.print(taskInfo.getOid());else out.print("新建工单");%>]</title>


</head>
<body>
<%
//是否显示动作panel
boolean showActionPanel = false;

if(taskOid==-1 || (actionList.size()>0 && taskData.canDealThisNode(userId)&& taskData.getStatus() == TaskData.STATUS_OPEN) || nodeInfo.isShareDeal()) {
	showActionPanel = true;
}


String defaultSrc = Consts.ITSM_HOME+"/task/taskViewNode.jsp";
if (taskOid == -1)
	defaultSrc = Consts.ITSM_HOME+"/task/taskActionNode.jsp";
defaultSrc+="?origin="+origin+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&wfOid="+wfOid+"&wfVer="+wfVer;

%>

<iframe id="excelOut" style="display:none"></iframe>
<div id='task_info_north'>

</div>
<script defer>
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
chart0.setDataXML("<%=rootEl.asXML().replaceAll("\"","\\\\\"")%>");

var http_request;
if (window.XMLHttpRequest) { // Mozilla, Safari, ...
    http_request = new XMLHttpRequest();
} else if (window.ActiveXObject) { // IE
    http_request = new ActiveXObject("Microsoft.XMLHTTP");
}

var currentRect = <%=taskDataId%>;



//历史流程图节点点击动作
function displayNode(params) {
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


//加载查看节点信息
function loadTaskInfoDetail(){
	task_info_deal_center.load({
	    url: "<%=Consts.ITSM_HOME%>/task/taskViewNode.jsp",
	    params:{"origin":"<%=origin%>","taskOid":"<%=taskOid%>","taskDataId":"<%=taskDataId%>","wfOid":<%=wfOid%>,"wfVer":"<%=wfVer%>"},
	    discardUrl: false,
	    text: "Loading...",
	    scripts: false
	});
	task_info_deal_center.body.highlight('#c3daf9', {block:true});
}

<%
Map context = new HashMap();
context.put("Consts", new Consts());
context.put("taskInfo", taskInfo);
out.println(TemplateUtil.getHTMLStrFromTemplate("/default/taskLinkPanel.html",context));
%>

//=============================工单处理区域布局=========================
	
var task_info_deal_center = new Ext.Panel({
	id:'task_info_deal_center',
	region:'center',
	autoScroll:true
});
<%
	boolean showBranchView = false;
	if (taskData.isBranchBegin() && taskData.getChilds().size()>0){
		showBranchView = true;
	}
	
	if (showBranchView) {
%>
var task_info_deal_branchView = new Ext.Panel({
	id:'task_info_deal_branchView',
	region:'south',
	autoLoad:"getBranchList.jsp?origin=<%=wfInfo.getOrigin()%>&wfOid=<%=wfInfo.getOid()%>&wfVer=<%=wfVer%>&taskOid=<%=taskOid%>&taskDataId=<%=taskDataId%>",
	height: 150,
	minSize: 50,	
	maxSize: 300,
	split:true,
	autoScroll:true
});
<%
	}
%>
var task_info_deal = new Ext.Panel({
	title:'<%=nodeInfo.getCaption()%>',
	id:'task_info_deal',
	region:'center',
	layout:'border',
	buttonAlign:'center',
	items:[task_info_deal_center<%if (showBranchView) {out.println(",task_info_deal_branchView");}%>],
	buttons:[
<%

boolean hasAct = false;
if(showActionPanel) {
	for (int i = 0; i < actionList.size(); i++) {
		if (i >0)
			out.println(",");
		ActionInfo actionInfo  = (ActionInfo)actionList.get(i);
		String url = "";
		//如果处理表单是远程页面
		if (actionInfo.getFormSelectMode().equals("remote"))
			url = actionInfo.getRemoteDealPage()+"?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+actionInfo.getToNodePath();
		else//如果是本地表单
	    	url = Consts.ITSM_HOME+"/task/taskActionNode.jsp?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+actionInfo.getToNodePath();
		out.print("{");
		out.print("text:'"+actionInfo.getActionName()+"',");
		out.print("handler:function(){openURL2('"+url+"');}");
		out.print("}");
		hasAct = true;
	}
	
	if (taskData.isForwardable() && !branchBeginIsDealing){
		String url = "";
		TaskData td_ = taskData.getParent();
		if (td_!=null) {
			if (hasAct)
				out.println(",");
			NodeInfo fromNode = wfData.getNode(td_.getNodeId());
			ActionInfo ai_ = fromNode.getAction(nodeInfo.getId());
			if (ai_!=null && ai_.getFormSelectMode().equals("remote"))
				url = ai_.getRemoteDealPage()+"?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+taskData.getNodePath();
			else
				url = Consts.ITSM_HOME+"/task/taskActionNode.jsp?origin="+wfInfo.getOrigin()+"&wfOid="+wfInfo.getOid()+"&wfVer="+wfVer+"&taskOid="+taskOid+"&taskDataId="+taskDataId+"&toNodePath="+taskData.getNodePath();
		   	out.print("{");
			out.print("text:'转派',");
			out.print("handler:function(){openURL2('"+url+"');}");
			out.print("}");
			hasAct = true;
		}
	}
	//if (hasAct)out.println(",");
}

if(taskData.canReadThisNode(userId)){
	if (hasAct)out.println(",");
	out.print("{");
	out.print("text:'已阅',");
	out.print("handler: readApply, origin:'" + origin + "', taskOid:" +
			taskOid + ", dataId: " + taskDataId + ",wfOid:" + wfOid);
	out.print("}");
	hasAct = true;
}
%>
/*	{
		text: '',
		handler:function() {
			parent.window.close();
		}
	}
*/
]
});
//=============================工单处理区域布局=====结束=================

	
//=============================历史工单节点流程展示======================
<%if(taskOid != -1){%>
var chart1 = new FusionCharts("TaskHistoryViewer.swf", "TaskHistoryViewer", "40", "30", "0", "1");
<%
Document doc = XmlUtil.parseString("<workflow/>");
Element el = doc.getRootElement();
TaskManager.printXML(el,taskInfo.getRootData(),wfData,new HashMap(),40,20,true);
%>
chart1.setDataXML("<%=el.asXML().replaceAll("\"","\\\\\"")%>");


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
			var params = {'taskOid':<%=taskOid%>,'taskDataId':0,'origin':'<%=origin%>','wfOid':<%=wfOid%>,'wfVer':'<%=wfVer%>','viewMode':'viewAllNode'};
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
<%}%>
//=============================历史工单节点流程展示=====结束=================

var task_info_center = new Ext.TabPanel({
	region:'center',
	id:'center-panel',
	activeTab: 0,
	border:false,
	items:[
<%
//boolean hasAct = false;
//if(showActionPanel) {
	//新建时，直接显示填写表单
	if(taskOid == -1){
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
	} else {
		out.print("task_info_deal,linkTaskGrid,task_info_history");
	}
//}
%>
	,{
	    id:'workflow-graphics-panel',
	    title: '流程图',
	    autoScroll:true,
	    minSize: 175,
	    maxSize: 400,
	    margins:'1 1 1 1',
	    html:chart0.getSWFHTML()
	}]
});
task_info_center.on("tabchange",function(tabPanel,tab){
	if(tab.getId()=='task_info_history'){
		//loadTaskInfoDetail();
		displayNode([<%=taskOid%>,0,"ITSM",<%=wfOid%>]);
	}
});

var infoAll = new Ext.Panel({
	region:'center',
	id:'infoAll-panel',
	layout:'border',
	items:task_info_center
	<%if (taskOid !=-1) {%>
	,tbar:[
	<%
		boolean hasR = false;
		if (btnList.size()>0){
			for(int i = 0; i < btnList.size(); i++) {
				if (i>0)
					out.print(",'-',");
				out.println(btnList.get(i).toString());
			}
			hasR=true;
		}
	%>
		,'->',
		{
			text:"关闭",
			icon :'../images/application_go.png',
			iconCls :'x-btn-text-icon',
			handler:function(){
				window.close();
			}
		}
	]
	<%}%>
});

Ext.onReady(function() {
	var viewport = new Ext.Viewport({
		layout:'border',
		items:[infoAll]
	});

	loadTaskInfoDetail();
});

function taskPostCallBack(ret){
	if (top.opener && top.opener.viewds)
		top.opener.viewds.reload();
	top.window.close();
}
</script>
<%} catch (Exception ex) { ex.printStackTrace(); } %>

</body>
</html>
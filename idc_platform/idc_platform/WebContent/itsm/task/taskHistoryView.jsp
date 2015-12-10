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
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>
	<xml:namespace ns="urn:schemas-microsoft-com:vml" prefix="v"/>
	<STYLE>
		v\:* {BEHAVIOR: url(#default#VML)}
	</style>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
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

WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);

String snapURL = "";
if (wfInfo.getSnapMode().equals("remote")){
	snapURL = wfInfo.getSnapRemoteViewPage()+"?wfOid="+wfOid+"&wfVer="+wfVer+"&taskOid="+taskOid+"&origin="+origin;
} else {
	snapURL = "/itsm/task/localSnapViewPage.jsp?wfOid="+wfOid+"&wfVer="+wfVer+"&taskOid="+taskOid+"&origin="+origin;
}

try {
		TaskInfo taskInfo = TaskManager.getTaskInfoByOid(origin,taskOid,wfOid,wfVer);

	%>
<iframe id="excelOut" style="display:none"></iframe>
<div id='task_history_graphics'>
</div>
<script defer>
<%
	Map context = new HashMap();
	context.put("Consts", new Consts());
	context.put("taskInfo", taskInfo);
	out.println(TemplateUtil.getHTMLStrFromTemplate("/default/taskLinkPanel.html",context));
%>
var currentRect = 0;
function loadGraphics(){
	var gurl = "<%=Consts.ITSM_HOME%>/task/taskHistoryGraphics.jsp?origin=<%=origin%>&taskOid=<%=taskOid%>&dataId=0&canChangeNode=true&wfOid=<%=wfOid%>";
	var http_request;
	if (window.XMLHttpRequest) { // Mozilla, Safari, ...
	    http_request = new XMLHttpRequest();
	} else if (window.ActiveXObject) { // IE
	    http_request = new ActiveXObject("Microsoft.XMLHTTP");
	}
	http_request.open("GET",gurl,false);
	http_request.send();
	if (http_request.readyState == 4) {
		task_history_graphics.innerHTML = http_request.responseText.trim();
	}
}

function displayNode(taskOid,taskDataId,origin,wfOid,viewMode,xsl) {
		var url = "";
		<%if (taskInfo.isShowHisGraphics()){%>
			var imgNode = document.all['node_'+currentRect+'_e'];
		if (imgNode.status=="1")
			imgNode.src='<%=Consts.ITSM_HOME%>/images/g_close.gif';
		else if (imgNode.status=="0")
			imgNode.src='<%=Consts.ITSM_HOME%>/images/g_open.gif';
		else
			imgNode.src='<%=Consts.ITSM_HOME%>/images/g_back.gif';
		currentRect = taskDataId;
		document.all['node_'+currentRect+'_e'].src='<%=Consts.ITSM_HOME%>/images/g_active.gif';
	<%}%>
		url = "<%=Consts.ITSM_HOME%>/task/taskHistoryDetail.jsp?taskOid="+taskOid+"&viewMode="+viewMode+"&xsl="+xsl+"&taskDataId="+taskDataId+"&origin="+origin+"&wfOid="+wfOid;

	task_history_panel.load({
    url: url,
    discardUrl: false,
    text: "Loading...",
    scripts: false
	});
	task_history_panel.body.highlight('#c3daf9', {block:true});
}



var task_history_panel = new Ext.Panel({
  title:'表单信息',
  autoScroll:true
});

var snap_panel = new Ext.ux.IFrameComponent({
		title: '数据快照',
  	iconCls: 'views',
  	url:'<%=snapURL%>',
  	autoScroll:true,
  	region:'center',
  	closable:false
})
Ext.onReady(function() {

	var task_detail_panel = new Ext.TabPanel({
		region:'center',
    split:true,
    margins:'1 1 1 1',
    border: true,
    activeTab:0,
    //remove snap_panel by zhonganjing
    //items:[task_history_panel,linkTaskGrid,snap_panel],
    items:[task_history_panel,linkTaskGrid],
		buttons:[			 				 				 		
		{
			text:"查看所有",
			handler:function(){
				displayNode(<%=taskOid%>,0,'<%=origin%>',<%=wfOid%>,'viewAllNode');
			}
		},{
			text:"导出到EXCEL",
			handler:function(){
					excelOut.location.href="<%=Consts.ITSM_HOME%>/task/taskHistoryDetail.jsp?taskOid=<%=taskOid%>&viewMode=viewAllNode&xls=1&origin=<%=origin%>&wfOid=<%=wfOid%>";
			}
		},{
			text:"关闭",
			handler:function(){
				window.close();
			}
		}]
	});

  var viewport = new Ext.Viewport({
			layout:'border',
      items:[{
          region:'north',
          id:'north-panel',
          collapsible: true,
          split:true,
          autoScroll :true,
          height: 150,
          minSize: 50,
          maxSize: 200,
          margins:'1 1 1 1',
          contentEl: task_history_graphics
			} , task_detail_panel]
	});
	loadGraphics();
	displayNode(<%=taskOid%>,0,'<%=origin%>',<%=wfOid%>);
});

</script>
<% } catch (Exception ex) { ex.printStackTrace(); } %>
</body>
</html>
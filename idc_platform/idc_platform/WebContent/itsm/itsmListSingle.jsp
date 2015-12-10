<%@page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.security.rule.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.authorization.*"%>
<%@ page import="com.hp.idc.itsm.inter.*"%>
<%@ include file="getUser.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

%>


<html>
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
	<iframe id="itsm_default_iframe" name="itsm_default_iframe" style="display:none"></iframe>


<script type="text/javascript">

	var mainPanel = new Ext.Panel({
		title: '待办事宜',
		id:'view_1',
		region:'center',
		margins: '1 0 0 0',
		iconCls: 'views',
		contentEl:'iframe_content'
	});

	var newAddTaskButton = new Ext.Toolbar.MenuButton({
		text: '新增工单',
		iconCls:'add',
		menu : {items: [
		<%
			List<WorkflowInfo> l = WorkflowManager.getWorkflows(userId,false);
			Collections.sort(l, new Comparator<WorkflowInfo>() {
				public int compare(WorkflowInfo o1, WorkflowInfo o2) {
					if(o1.getOid() > o2.getOid())
						return 1;
					else if (o1.getOid() < o2.getOid())
						return -1;
					else 
						return 0;
				}
			});
			int j =0;
			for(WorkflowInfo info : l){
				if(j>0)
					out.print(",");
				out.print("{text: '" + info.getName() + (info.getOrigin().equals("ITSM")?"":"("+info.getOrigin()+")")+"', handler: function(){");
				out.print("openURL('"+Consts.ITSM_HOME+"/task/taskInfo.jsp?wfOid=" + info.getOid() + "&taskOid=-1&origin="+info.getOrigin()+"');");
				out.print("}}");
				j++;
			}
			//we don't need the DSM Center, so i have to remove these code;
			//Map retMap = WorkflowManager.getWorkflowsTree(userId,false);
			//boolean hasRe = false;
			//Set mKey = retMap.keySet();
			//for (Iterator ite = mKey.iterator(); ite.hasNext();){
			//	String pName = (String)ite.next();
			//	Object o = retMap.get(pName);
			//	if (hasRe)
			//			out.print(",");
			//	if (o instanceof WorkflowInfo){
			//		WorkflowInfo info = (WorkflowInfo)o;
			//		out.print("{text: '" + info.getName() + (info.getOrigin().equals("ITSM")?"":"("+info.getOrigin()+")")+"', handler: function(){");
			//		out.print("openURL('"+Consts.ITSM_HOME+"/task/taskInfo.jsp?wfOid=" + info.getOid() + "&taskOid=-1&origin="+info.getOrigin()+"');");
			//		out.print("}}");
			//	} else {
			//		out.print("{text: '" + pName +"', hideOnClick:false, menu:{items: [");
			//		List subwf = (List)o;
			//		for (int i = 0; i < subwf.size(); i++) {
			//			if (i>0)
			//				out.print(",");
			//			WorkflowInfo info = (WorkflowInfo)subwf.get(i);
			//			String subName = info.getName().substring(info.getName().indexOf("/")+1);
			//			out.print("{text: '" + subName + (info.getOrigin().equals("ITSM")?"":"("+info.getOrigin()+")")+"', handler: function(){");
			//			out.print("openURL('"+Consts.ITSM_HOME+"/task/taskInfo.jsp?wfOid=" + info.getOid() + "&taskOid=-1&origin="+info.getOrigin()+"');");
			//			out.print("}}");
			//		}
			//		out.print("]}}");

			//	}
			//	hasRe = true;
			//}
		%>
		]}
	});
	newAddTaskButton.on("click",function(){newAddTaskButton.showMenu()});

	var westPanel = new Ext.tree.TreePanel({
		id:'navigation',
		region:'west',
		split:true,
		width: 200,
		minSize: 175,
		maxSize: 400,
		margins: '1 0 0 0',
		collapsible: true,
		rootVisible:false,
		lines:false,
		autoScroll:true,
		collapseFirst:false,
		tbar: [newAddTaskButton
			//,'->',new Ext.Toolbar.Button({
				//text:'设置代理人',
			 	//handler:function(){
			 		//Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/factorEdit.jsp?userId=<%=userId%>");
			 	//}
			//})
		]
	});
	var root = new Ext.tree.TreeNode('Naviga');
	this.westPanel.setRootNode(root);

var treeData = [
<%
	String nodeId = request.getParameter("node");
	if (nodeId == null)
		nodeId = "";
	List menus = MenuManager.getMenusOfUser(userId);
	for (int i = 0; i < menus.size(); i++) {
			MenuInfo mi = (MenuInfo)menus.get(i);
			if (i>0 && (!mi.isLeaf()&&mi.getSubMenus().size()>0))
				out.println(",");
			out.println(printNodeStr(mi,request));
	}
%>
];
loadTree(root,treeData);


	function loadTree(node, data)
	{
		for (var i = 0; i < data.length; i++)
		{
			var attr = data[i];
			var newNode = new Ext.tree.TreeNode(attr);
			node.appendChild(newNode);
			if (attr && attr.children)
				loadTree(newNode, attr.children);
		}
	};

  	function addTab(tabId,name,href,icon) {

	    	mainPanel.setTitle(name);
	    	mainPanel.id = tabId;
	    	mainPanel.iconCls = icon;
	    	if(href.indexOf('?') > -1){
		    	href = href + '&home_page=/portal/web/index.jsp?mid=1267'
		    } else {
		    	href = href + '?home_page=/portal/web/index.jsp?mid=1267'
			}
	    	displayForm.location.href = href;
   	};

  	function menuHide() {
		if(newAddTaskButton.hasVisibleMenu()) {
	  		newAddTaskButton.hideMenu();
		}
  	};
     
    Ext.onReady(function(){

       var viewport = new Ext.Viewport({
            layout:'border',
            items:[westPanel,mainPanel]
        });
        displayForm.location.href="<%=Consts.ITSM_HOME%>/configure/view/viewTemplate.jsp?viewOid=1";
       	document.getElementById("displayForm").height = mainPanel.body.dom.clientHeight;
        //westPanel.selectPath(westPanel.getNodeById("view_1").getPath());
        westPanel.on('click', function(node, e){
	       if(node.isLeaf()){
	          e.stopEvent();
	          var defaultT = '';
	          if (node.attributes.diaplayText)
	          	defaultT = node.attributes.diaplayText;
	          if (node.attributes.href_ && node.attributes.href_!="")
	          	addTab(node.id,node.text+defaultT,node.attributes.href_,node.attributes.iconCls);
	          else
				node.attributes.handler();
	       }
    	});
		westPanel.getRootNode().item(0).expand();
    });

	</script>
	<div id="iframe_content">
		<iframe id="displayForm" name="displayForm" src="" width="100%" height="100%"  frameborder=0 onmouseover="menuHide()"></iframe>
	</div>
 </body>
</html>



<%!
	public String printNodeStr(MenuInfo mi,HttpServletRequest req){
		StringBuffer sb = new StringBuffer();
		if (mi.isLeaf()){
			sb.append("{id:'"+mi.getId()+"',text:'"+mi.getName()+"',diaplayText:'"+mi.getDisplayText()+"',cls:'feed',iconCls: 'views',leaf:true,href_:\""+MacroManager.replaceMacro(mi.getHref(),req)+"\",handler:function(){"+MacroManager.replaceMacro(mi.getScript(),req)+"}}");
		} else{
			String style = "cls:'feed',iconCls: 'folder'";
			if (mi.getParentId().equals(""))
				style = "cls:'feeds-node'";
			List submenus = mi.getSubMenus();
			if (submenus.size()>0){
				sb.append("{id:'"+mi.getId()+"',text:'"+mi.getName()+"',"+style+",singleClickExpand:true,children:[");
				for (int i = 0; i < submenus.size(); i++) {
					MenuInfo mi_ = (MenuInfo)submenus.get(i);
					if (i > 0)
						sb.append(",");
					sb.append(printNodeStr(mi_,req));
				}
				sb.append("]}");
			}

		}
		return sb.toString();
	}
%>
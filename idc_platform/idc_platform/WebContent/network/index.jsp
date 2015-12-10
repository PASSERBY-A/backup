<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ include file="getUserId.jsp"%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link rel="stylesheet" type="text/css"
	href="<%=EXTJS_HOME%>/resources/css/ext-all.css" />
<script type="text/javascript"
	src="<%=EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-all.js"></script>
<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-ext.js"></script>

<link rel="stylesheet" type="text/css" href="layout-browser.css">

</head>
<body>
<div id="iframe_content"><iframe id="displayForm"
	name="displayForm" src="" width="100%" height="100%"
	frameborder=0></iframe></div>
<script>
   NNMURL = "http://<%=NNMBaseURL%>/nnm/launch?cmd=showMain&j_username=<%=userName%>&j_password=<%=password%>";
   var submitStore = new Ext.data.Store({
		        baseParams:{ajax:true},
		        proxy: new Ext.data.HttpProxy({
		        	url:NNMURL
		        }),
		        reader: new Ext.data.JsonReader({
			            root: 'result'
			        })
		    });
   Ext.onReady(function() {

		// This is an inner body element within the Details panel created to provide a "slide in" effect
			// on the panel body without affecting the body's box itself.  This element is created on
			// initial use and cached in this var for subsequent access.
			var detailEl = null;
			var contentPanel = new Ext.Panel( {
				region : 'center',
				title : '告警查询',
				bodyBorder : false,
				margins : '1 0 0 0',
				layout : 'fit',
				contentEl : 'iframe_content'
			});

			// Go ahead and create the TreePanel now so that we can use it below
			var treePanel = new Ext.tree.TreePanel( {
				id : 'tree-panel',
				title : '导航',
				region : 'west',
				split : true,
				width:200,
				minSize: 175,
				maxSize: 400,
				autoScroll : true,
				collapsible: true,
				rootVisible : false,
				lines : false,
				singleExpand : true,
				expanded : true,
				useArrows : true
			});

			var root = new Ext.tree.TreeNode('网络管理');
			treePanel.setRootNode(root);
			
            var child1 = new Ext.tree.TreeNode( {
				id : 'alarm',
				text : '告警查询',
				singleClickExpand : true
			});
			treePanel.getRootNode().appendChild(child1);
			
			var child9 = new Ext.tree.TreeNode( {
				id : 'alarm_query',
				text : '告警查询',
				leaf : true,
				singleClickExpand : true,
				href : "<%= request.getContextPath() %>/network/IntNetworkAlertShow.action"
			});
			child1.appendChild(child9);
			
			var child10 = new Ext.tree.TreeNode( {
				id : 'alarm_close_query',
				text : '确认告警查询',
				leaf : true,
				singleClickExpand : true,
				href : "<%= request.getContextPath() %>/network/IntNetworkCloseAlertShow.action"
			});
			child1.appendChild(child10);
		
			child2 = new Ext.tree.TreeNode( {
				id : 'network',
				text : '网络设备监控',
				singleClickExpand : true
			});
			treePanel.getRootNode().appendChild(child2);
			
			child7 = new Ext.tree.TreeNode( {
				id : 'switch-network',
				text : '网络设备监控',
				leaf : true,
                nwindows:true,
                RUrl:'http://<%=NNMBaseURL%>/nnm/launch?cmd=showMain&j_username=<%=userName%>&j_password=<%=password%>',
				href : ''
			});
			child2.appendChild(child7);
			
			
			child3 = new Ext.tree.TreeNode( {
				id : 'server-network',
				text : '服务器设备监控',
				singleClickExpand : true
				});
			treePanel.getRootNode().appendChild(child3);
			
			child8 = new Ext.tree.TreeNode( {
				id : 'sitescope',
				text : '服务器设备监控',
				leaf : true,
				nwindows:true,
                sitescope:true,
                RUrl:'http://<%=SitescopeBaseURL%>/SiteScope?sis_silent_login_type=encrypted&login=<%=userCode%>&password=<%=passwordCode%>',
                href : ""
			});
			child3.appendChild(child8);
			
			child4 = new Ext.tree.TreeNode( {
				id : 'topo-network',
				text : '网络拓扑展示',
				leaf : true,
				singleClickExpand : true,
				nwindows:true,
                                RUrl:"http://<%=NNMBaseURL%>/nnm/launch?cmd=showNetworkOverview&j_username=<%=userName%>&j_password=<%=password%>",
				href : "http://<%=NNMBaseURL%>/nnm/launch?cmd=showNetworkOverview"
			});
			child2.appendChild(child4);
			
			child5 = new Ext.tree.TreeNode( {
				id : 'switch-search',
				text : '网络故障查询',
				leaf : true,
				singleClickExpand : true,
				nwindows:true,
                                RUrl: 'http://<%=NNMBaseURL%>/nnm/launch?cmd=showView&objtype=Incident&j_username=<%=userName%>&j_password=<%=password%>',
				href : 'http://<%=NNMBaseURL%>/nnm/launch?cmd=showView&objtype=Incident'
			});
			child2.appendChild(child5);
			
			child6 = new Ext.tree.TreeNode( {
				id : 'device-ping',
				text : '网络状态报表',
				leaf : true,
				singleClickExpand : true,
				nwindows:true,
                                RUrl:'http://<%=NNMBaseURL%>:9300/PerfSpi/PerfSpi?Interface%20UUID=${uuid}&package=Interface_Health&CAMNamespace=ErsTrustedSignonProvider',
                href :'http://<%=NNMBaseURL%>:9300/PerfSpi/PerfSpi?Interface%20UUID=${uuid}&package=Interface_Health&CAMNamespace=ErsTrustedSignonProvider'
			});
			child2.appendChild(child6);

			// Assign the changeLayout function to be called on tree node click.
			treePanel.on('click', function(n, e) {
				if (n.leaf) {
					if(e)
						e.stopEvent();
					contentPanel.setTitle(n.text);
                                        if (!n.attributes.nwindows){
						displayForm.location.href = n.attributes.href;
						}else{ 
                          
						  window.open(n.attributes.RUrl);
						 
                    }	
				}
			});

			// Finally, build the main layout once all the pieces are ready.  This is also a good
			// example of putting together a full-screen BorderLayout within a Viewport.
			new Ext.Viewport( {
				layout : 'border',
				title : 'Ext Layout Browser',
				items : [ treePanel, contentPanel ],
				renderTo : Ext.getBody()
			});
			//function login() {
				//	contentPanel.setTitle(child9.text);
                  //  displayForm.location.href = child9.attributes.href;
	        //}   
			//login();
			
			//click the child9 when open the page
			child1.expand(true,true,function(node){
				treePanel.fireEvent('click',node.firstChild);
			});
			
		});
 
</script>
</body>
</html>
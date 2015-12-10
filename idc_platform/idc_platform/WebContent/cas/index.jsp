<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getPurview.jsp"%>
<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=EXTJS_HOME%>/resources/css/ext-all.css" />
	<script type="text/javascript" src="<%=EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-ext.js"></script>

	<link rel="stylesheet" type="text/css" href="layout-browser.css">

</head>
<body>
<div style="display:none;">
	<div id="user-details">
     <h2>项不能删除，只能禁用</h2>
     <p>人员有两个状态：</p>
     <pre><code>
status:
    0:记录正常
    1:记录已删除
p_status:
    0:正常在班
    1:出差
    2:其他（缺席、离职）
    </code></pre>
    <p>重置后密码：用户ID+123
  </div>
  <div id="organization-details">
     <h2>项不能删除，只能禁用</h2>
     <pre><code>
状态(status):
    0:记录正常
    1:记录已删除
     </code></pre>
     <p>人员-组织，属于多对一的关系。</p>
  </div>
  <div id="workgroup-details">
     <h2>项不能删除，只能禁用</h2>
     <pre><code>
状态(status):
    0:记录正常
    1:记录已删除
     </code></pre>
     <p>人员-工作组，属于多对多关系。</p>
  </div>
  <div id="pc_mapping-details">
     <h2>其他系统用户与本系统的用户的映射表</h2>
  </div>
  <div id="config-details">
     <h2>帐号密码规则配置</h2>
  </div>
  <div id="log-details">
     <h2>登录日志查询</h2>
  </div>
</div>
	<div id="iframe_content">
		<iframe id="displayForm" name="displayForm" src="user.jsp" width="100%" height="100%"  frameborder=0></iframe>
	</div>
	<script>
	Ext.onReady(function(){

	// This is an inner body element within the Details panel created to provide a "slide in" effect
	// on the panel body without affecting the body's box itself.  This element is created on
	// initial use and cached in this var for subsequent access.
	var detailEl = null;
	var contentPanel = new Ext.Panel({
	    region:'center',
	    title:'人员列表',
	    bodyBorder:false,
	    margins: '1 0 0 0',
	    layout: 'fit',
			contentEl:'iframe_content'
		});

	// Go ahead and create the TreePanel now so that we can use it below
    var treePanel = new Ext.tree.TreePanel({
    	id: 'tree-panel',
    	title: '导航',
        region:'north',
        split: true,
        height: 300,
        minSize: 150,
        autoScroll: true,
        // tree-specific configs:
        rootVisible: false,
        lines: false,
        singleExpand: true,
        useArrows: true
    });

    var root = new Ext.tree.TreeNode('Naviga');
		treePanel.setRootNode(root);

    var child = new Ext.tree.TreeNode({id:'user',text:'人员',leaf:true,singleClickExpand:true,href:"user.jsp"});
    treePanel.getRootNode().appendChild(child);
    child = new Ext.tree.TreeNode({id:'organization',text:'组织',leaf:true,singleClickExpand:true,href:"organization.jsp"});
    treePanel.getRootNode().appendChild(child);
    child = new Ext.tree.TreeNode({id:'workgroup',text:'工作组',leaf:true,singleClickExpand:true,href:"workgroup.jsp"});
    treePanel.getRootNode().appendChild(child);
/*
    child = new Ext.tree.TreeNode({id:'pc_mapping',text:'portal用户映射',leaf:true,singleClickExpand:true,href:"portalAUCSync.jsp"});
    treePanel.getRootNode().appendChild(child);
*/    
    child = new Ext.tree.TreeNode({id:'config',text:'配置',leaf:true,singleClickExpand:true,href:"config.jsp"});
    treePanel.getRootNode().appendChild(child);
    child = new Ext.tree.TreeNode({id:'log',text:'登录日志',leaf:true,singleClickExpand:true,href:"loginLogList.jsp"});
    treePanel.getRootNode().appendChild(child);
	// Assign the changeLayout function to be called on tree node click.
    treePanel.on('click', function(n,e){

  		 if(n.leaf){
  		 	e.stopEvent();
  		 	contentPanel.setTitle(n.text);
          displayForm.location.href = n.attributes.href;
       }
  		if(detailEl == null){
  			var bd = Ext.getCmp('details-panel').body;
  			bd.update('').setStyle('background','#fff');
  			detailEl = bd.createChild(); //create default empty div
  		}
  		if(Ext.getDom(n.id+'-details'))
  			detailEl.hide().update(Ext.getDom(n.id+'-details').innerHTML).slideIn('l', {stopFx:true,duration:.2});
    	//}
    });
    
	// This is the Details panel that contains the description for each example layout.
	var detailsPanel = {
			id: 'details-panel',
      title: '描述',
      region: 'center',
      height: 200,
      bodyStyle: 'padding-bottom:15px;background:#eee;',
			autoScroll: true,
			html: '<p class="details-info">说明</p>'
    };

	// Finally, build the main layout once all the pieces are ready.  This is also a good
	// example of putting together a full-screen BorderLayout within a Viewport.
    new Ext.Viewport({
		layout: 'border',
		title: 'Ext Layout Browser',
		items: [{
			layout: 'border',
    	id: 'layout-browser',
      region:'west',
      border: false,
      split:true,
			margins: '1 0 0 0',
      width: 160,
      minSize: 100,
      maxSize: 500,
			items: [treePanel, detailsPanel]
		},
			contentPanel
		],
        renderTo: Ext.getBody()
    });
});

	</script>
</body>
</html>
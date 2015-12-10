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
     <h2>���ɾ����ֻ�ܽ���</h2>
     <p>��Ա������״̬��</p>
     <pre><code>
status:
    0:��¼����
    1:��¼��ɾ��
p_status:
    0:�����ڰ�
    1:����
    2:������ȱϯ����ְ��
    </code></pre>
    <p>���ú����룺�û�ID+123
  </div>
  <div id="organization-details">
     <h2>���ɾ����ֻ�ܽ���</h2>
     <pre><code>
״̬(status):
    0:��¼����
    1:��¼��ɾ��
     </code></pre>
     <p>��Ա-��֯�����ڶ��һ�Ĺ�ϵ��</p>
  </div>
  <div id="workgroup-details">
     <h2>���ɾ����ֻ�ܽ���</h2>
     <pre><code>
״̬(status):
    0:��¼����
    1:��¼��ɾ��
     </code></pre>
     <p>��Ա-�����飬���ڶ�Զ��ϵ��</p>
  </div>
  <div id="pc_mapping-details">
     <h2>����ϵͳ�û��뱾ϵͳ���û���ӳ���</h2>
  </div>
  <div id="config-details">
     <h2>�ʺ������������</h2>
  </div>
  <div id="log-details">
     <h2>��¼��־��ѯ</h2>
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
	    title:'��Ա�б�',
	    bodyBorder:false,
	    margins: '1 0 0 0',
	    layout: 'fit',
			contentEl:'iframe_content'
		});

	// Go ahead and create the TreePanel now so that we can use it below
    var treePanel = new Ext.tree.TreePanel({
    	id: 'tree-panel',
    	title: '����',
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

    var child = new Ext.tree.TreeNode({id:'user',text:'��Ա',leaf:true,singleClickExpand:true,href:"user.jsp"});
    treePanel.getRootNode().appendChild(child);
    child = new Ext.tree.TreeNode({id:'organization',text:'��֯',leaf:true,singleClickExpand:true,href:"organization.jsp"});
    treePanel.getRootNode().appendChild(child);
    child = new Ext.tree.TreeNode({id:'workgroup',text:'������',leaf:true,singleClickExpand:true,href:"workgroup.jsp"});
    treePanel.getRootNode().appendChild(child);
/*
    child = new Ext.tree.TreeNode({id:'pc_mapping',text:'portal�û�ӳ��',leaf:true,singleClickExpand:true,href:"portalAUCSync.jsp"});
    treePanel.getRootNode().appendChild(child);
*/    
    child = new Ext.tree.TreeNode({id:'config',text:'����',leaf:true,singleClickExpand:true,href:"config.jsp"});
    treePanel.getRootNode().appendChild(child);
    child = new Ext.tree.TreeNode({id:'log',text:'��¼��־',leaf:true,singleClickExpand:true,href:"loginLogList.jsp"});
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
      title: '����',
      region: 'center',
      height: 200,
      bodyStyle: 'padding-bottom:15px;background:#eee;',
			autoScroll: true,
			html: '<p class="details-info">˵��</p>'
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
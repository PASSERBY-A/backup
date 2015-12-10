<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>

	<link rel="stylesheet" type="text/css" href="layout-browser.css">
	<script type="text/javascript" src="cas_auc.js"></script>

</head>
<body>
	<%
	String singleMode = request.getParameter("singleMode");
	if (singleMode == null || singleMode.equals(""))
		singleMode = "true";
	String pathMode = request.getParameter("pathMode");
	if (pathMode == null || pathMode.equals(""))
		pathMode = "false";
	String personStr = request.getParameter("s_value");
	if (personStr == null)
		personStr = "";
	String filter = request.getParameter("filter");
	if (filter == null)
		filter = "";
	String groupType = request.getParameter("groupType");
	if (groupType == null)
		groupType = "1";
	String selectGroup = request.getParameter("selectGroup");
	if (selectGroup == null)
		selectGroup = "0";
		
	String groupName = "未知";
	if (groupType.equals("1"))
		groupName = "工作组";
	else if (groupType.equals("3"))
		groupName = "组织";
	else if (groupType.equals("5"))
		groupName = "本地组";
	%>
<script>
var userDS = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        <%if (groupType.equals("1")) {%>
        	url : 'workgroupUserQuery.jsp'
        <%} else if (groupType.equals("3")) {%>
    		url : 'organizationUserQuery.jsp'
        <%} else if (groupType.equals("5")) {%>
			url: 'configure/localgroup/localgroupUserQuery.jsp'
		<%}%>
    }),
    baseParams:{'lgId':''},
    reader: new Ext.data.JsonReader({
        root: 'lgs',
        totalProperty: 'totalCount'
    }, [
				{name: 'id',mapping: 'id'},
    		{name: 'name',mapping: 'name'},
    		{name: 'mobile',mapping: 'mobile'},
    		{name: 'role',mapping: 'role'},
    		{name: 'status',mapping: 'status'}
    ])
});
var currentSelectRecord;
var orgUserGrid = new Ext.grid.GridPanel({
	title: '关联的人员',
	region:'center',
	layout: 'fit',
  split: true,
  margins: '1 1 1 0',
  ds: userDS,
  cm: new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer(),
    {header: "ID", width: 70, sortable: true, dataIndex: 'id'},
    {header: "名称", width: 75, sortable: true, dataIndex: 'name'},
    {header: "手机号", width: 75, sortable: true, dataIndex: 'mobile'},
    {header: "职务", width: 75, sortable: true, dataIndex: 'role'},
		{header: "状态", width: 85, sortable: true, dataIndex: 'status'}
	]),
  stripeRows: true,
  loadMask:true,
  viewConfig: {
		forceFit:true
	},
	tbar:['检索','-',
	new Ext.form.SearchField({
		paramName:'filter',
		emptyText:'按用户的ID/名称/手机号/Email过滤',
		store: userDS,
		width:320
	})
	],
	bbar: new Ext.PagingToolbar({
		store: userDS,
		displayInfo: true,
		pageSize: 25,
		displayMsg: "当前记录第 {0} - {1}条，一共{2}条",
		emptyMsg: "没有记录",
		items:['-']
	}),
	listeners: {
		rowdblclick:function(g,rowIndex,e){
  		var record = this.store.getAt(rowIndex);
  		addRecord(record);
  		if ("<%=singleMode%>" == "true"){
				submitBtFun();
			}
  	},
  	rowclick:function(g,rowIndex,e){
  		var record = this.store.getAt(rowIndex);
  		addRecord(record);
  		//currentSelectRecord = this.store.getAt(rowIndex);
  	}
	}
});
function checkId(rid){
	if ("<%=singleMode%>" != "true"){
		for (var i = 0 ; i < selectStore.getCount(); i++){
			var id = selectStore.getAt(i).get("id");
			if (id == rid)
				return false;
		}
	}
	return true;
};
function addRecord(record){
	if ("<%=singleMode%>" == "true"){
		selectStore.removeAll();
	}
	if (checkId(record.get("id"))) {
		selectStore.add(record);
	}


};

var selecData = [
	<%
		if (personStr!=null && !personStr.equals("")) {
			String[] objs = personStr.split(",");
			int index = 0;
			for (int i = 0; i < objs.length; i++) {
				CIInfo ci = PersonManager.getPersonById(objs[i]);
				if (index>0)
					out.print(",");
				if (ci == null)
					ci = OrganizationManager.getOrganizationById(objs[i]);
				if (ci == null)
					ci = WorkgroupManager.getWorkgroupById(objs[i]);
				if (ci!=null) {
					out.print("[");
					out.print("'"+ci.getId()+"',");
					out.print("'"+ci.getName()+"'");
					out.print("]");
					
					index++;
				}
			}
		}
	%>
	];
var selectStore = new Ext.data.SimpleStore({
	fields:[
						{name: 'id', type: 'string'},
						{name: 'name', type: 'string'},
		    ],
	data:[]
});

var selectUserGrid = new Ext.grid.GridPanel({
	region:'south',
	layout: 'fit',
	height: 200,
  minSize: 100,
  maxSize: 300,
  split: true,
  margins: '1 1 1 0',
  ds: selectStore,
  cm: new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(),
	    {header: "ID", width: 70, sortable: true, dataIndex: 'id'},
	    {header: "名称", width: 75, sortable: true, dataIndex: 'name'}
	]),
	loadMask:true,
  stripeRows: true,
  viewConfig: {
		forceFit:true
	},

	listeners: {
		rowdblclick:function(g,rowIndex,e){
  		var record = this.store.getAt(rowIndex);
  		selectStore.remove(record);
  	}
	}
});
selectStore.loadData(selecData);



Ext.tree.TreeLoaderExtend = function(config){
	Ext.tree.TreeLoaderExtend.superclass.constructor.call(this, config);
}

Ext.extend(Ext.tree.TreeLoaderExtend, Ext.tree.TreeLoader, {
    createNode : function(attr){
			var ret = Ext.tree.FilterTreeLoader.superclass.createNode.call(this, attr);
			ret._click = attr._click;
			return ret;
    }
});

var treePanel = new Ext.tree.TreePanel({
  region:'west',
  stripeRows: true,
  title: '<%=groupName%>',
  split: true,
  width: 200,
  minSize: 150,
  autoScroll: true,
  // tree-specific configs:
  margins: '1 0 1 1',
  rootVisible: false,
  lines: true,
  singleExpand: true,
  useArrows: false,
  loadMask:true,
  root:new Ext.tree.TreeNode({id:'-1'}),
  loader:new Ext.tree.TreeLoaderExtend({
  	baseParams:{regexId:'<%=filter%>',includeAll:false
  	<%if (singleMode == "false" && selectGroup.equals("1")){%>
  		,selectGroup:'1'
  	<%}%>
  	},
  	<%if (groupType.equals("3")) {%>
  	dataUrl:'organizationTreeQuery.jsp'
  	<%} else if (groupType.equals("1")){%>
  		dataUrl:'workgroupTreeQuery.jsp'
  	<%} else if (groupType.equals("5")){%>
  		dataUrl:'configure/localgroup/localgroupTreeQuery.jsp'
  	<%}%>
  })
});
treePanel.loader.on('beforeload', function(ol, node, cb){
	ol.baseParams.nodePath = node.getPath();
});
treePanel.on("click",function (node,e){
	if (!node._click)
		return;
	userDS.baseParams.includeAll='false';
	<%if (groupType.equals("3")) {%>
		userDS.baseParams.type="org";
		userDS.baseParams.orgId=node.id;
	<%} else if (groupType.equals("1")){%>
		userDS.baseParams.type="wog";
		userDS.baseParams.wogId=node.id;
	<%} else if (groupType.equals("5")){%>
		userDS.baseParams.type="log";
		userDS.baseParams.logId=node.id;
	<%}%>
	userDS.reload();
})

<%if (selectGroup.equals("1")){%>
treePanel.on("dblclick",function (node,e){
	addRecord(new Ext.data.Record({id:node.id,name:node.text}));
})
<%}%>
//treePanel.loader.load(treePanel.root);
//expand the filter
treePanel.loader.load(treePanel.root, function(){
<%if(!filter.equals("")){%>
treePanel.root.expandChildNodes(true);
treePanel.selectPath("/-1<%=filter%>", 'id', function(b, selN) {
if(b){
selN.fireEvent('click', selN);
}
});
<%}%>
});



function submitBtFun() {
	var retv = "";

	for (var i = 0 ; i < selectStore.getCount(); i++){
		var id = selectStore.getAt(i).get("id");
		var name = selectStore.getAt(i).get("name");
		if (i>0)
			retv += ",";
		retv += id+"="+name;
	}

	window.returnValue = retv;
	window.close();
}

Ext.onReady(function(){
	var eastPanel = {
    region:'center',
    layout:'border',
		bodyBorder: false,
		border:false,
		split: true,
		items:[
		<%if (singleMode.equals("false")){%>
			selectUserGrid,
		<%}%>
		orgUserGrid],
		buttonAlign:'right',
		buttons:[{
			text:'确定',
			handler :submitBtFun
		},{
			text:'取消',
			handler :function(){
				window.close();
			}
		}]
};

	new Ext.Viewport({
		layout: 'border',
		items: [treePanel,
			eastPanel,
			new Ext.BoxComponent({ // raw
          region:'north',
          el: 'north',
          height:32
      })
		]
  });
  
});
</script>
  <div id="north">
    选择参数：<br>
    <b>允许多选：</b><%=singleMode.equals("true")?"false":"true"%>&nbsp;&nbsp;
    <b>分组方式：</b><%=groupName%>&nbsp;&nbsp;
    <b>允许选择组：</b><%=selectGroup.equals("1")?"true":"false"%>&nbsp;&nbsp;
    <b>过滤条件：</b><%=filter%>&nbsp;&nbsp;
  </div>
</body>
</html>
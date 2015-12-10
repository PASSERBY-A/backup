<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<html>
<head>
  <title>TEST</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
</head>

<body>

<script defer>
var grid_fieldList = null;
var window_fieldView = null;
var currentCatalogOid = 6;


function newField()
{
	viewField({"id": -1,"origin":'ITSM'});
	//viewField({cfgXML:"<attribute/>"});
}

function viewField(paramList)
{
	paramList.module = currentCatalogOid;
	Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/field/fieldEdit.jsp",
		paramList);
}

// 生成数据源
var ds = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: '<%=Consts.ITSM_HOME%>/configure/field/fieldQuery.jsp'
    }),

		baseParams: { "module": currentCatalogOid },
    // create reader that reads the Topic records
    reader: new Ext.data.JsonReader({
        root: 'items',
        totalProperty: 'totalCount',
        id: 'id'
    }, [
        {name: 'id', mapping: 'id'},
        {name: 'name', mapping: 'name'},
        {name: 'type', mapping: 'type'},
        {name: 'origin', mapping: 'origin'},
        {name: 'category', mapping: 'category'},
        {name: 'origin_category', mapping: 'origin_category'}
    ]),
sortInfo:{field: 'id', direction: 'ASC'},

    // turn on remote sorting
    remoteSort: true
});


var catalogTree = new Ext.tree.TreePanel({
	title:'模块类别',
	region:'west',
	split:true,
  width: 150,
  minSize: 75,
  maxSize: 175,
  animate:false,
	lines:false,
	margins:'1 0 1 1',
  autoScroll:true,
  collapsible: true,
  rootVisible:false
});

var root = new Ext.tree.TreeNode({
	text: '模块列表',
	draggable:false
});
	<%
	List modules = CIManager.getCodesByTypeOid(Consts.CODETYPE_MODULE);
	for (int i = 0; i < modules.size(); i++) {
	CodeInfo cc = (CodeInfo)modules.get(i);
	cc.output("root_" + i, out, "javascript:loadCodeType(/oid/)");
	out.println("root.appendChild(root_" + i + ");");
	}
	%>
catalogTree.setRootNode(root);

Ext.onReady(function() {
	
    // 生成表格头信息
    var cm = new Ext.grid.ColumnModel([{
			header: "ID",
			dataIndex: 'id',
			width: 160,
			sortable: true,
			css: 'white-space:normal;'
			},{
			header: "名称",
			sortable: true,
			dataIndex: 'name',
			width: 150
			},{
			header: "类型",
			dataIndex: 'type',
			sortable: true,
			width: 100
			},{
			header: "归属",
			sortable: true,
			dataIndex: 'origin_category',
			width: 100
        }]);

	// 生成表格
    grid_fieldList = new Ext.grid.GridPanel({
        ds: ds,
        cm: cm,
		border:false,
		viewConfig: {
			autoFill: true
		},
        selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
        enableColLock:false,
        loadMask: true,
		tbar: [
			'<b></b>', '-', '按名称过滤: ', ' ',
			new Ext.form.SearchField({
				paramName:'filter',
				store: ds,
				width:320
			})
		],
		bbar: new Ext.PagingToolbar({
				store: ds,
				pageSize: <%=Consts.ITEMS_PER_PAGE%>,
				displayInfo: true,
				displayMsg: "<%=Consts.MSG_PAGE_DISPLAY%>",
				emptyMsg: "<%=Consts.MSG_PAGE_EMPTY%>",
				items: ['-', new Ext.Toolbar.Button({
						text: '新增',
						handler: newField
					})
						]
			})
    });

	var viewport = new Ext.Viewport({
			layout:'border',
      items:[catalogTree,{
            region:'center',
            split:true,
            width: 225,
            minSize: 175,
            maxSize: 400,
            layout:'fit',
            margins:'1 1 1 0',
            items: grid_fieldList
			}]
	});

	grid_fieldList.on("rowdblclick", function(grid, rowIndex, e)
		{
			var id = grid.getStore().getAt(rowIndex).get("id");
			var origin = grid.getStore().getAt(rowIndex).get("origin");
			viewField({"id": id,"origin":origin});
		});
    // trigger the data store load
    ds.load({params:{start:0, limit:<%=Consts.ITEMS_PER_PAGE%>}});

});

function loadCodeType(catalogOid)
{
	currentCatalogOid = catalogOid;
	ds.baseParams.module=currentCatalogOid;
	ds.reload();
}

</script>

</body>

</html>

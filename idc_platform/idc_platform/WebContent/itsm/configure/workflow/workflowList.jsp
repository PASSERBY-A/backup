<%@page contentType="text/html; charset=gbk" language="java"%>
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
  <title>流程列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk"/>
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>
</head>

<body>

<script defer>
var grid_workflowList = null;
var window_workflowView = null;
var window_workflowGraphics = null;
var currentCatalogOid = 6;

function getWorkflowXml()
{
	var fld = window_workflowView._form.form.findField("fld_xml");
	return fld.getValue();
}

function setWorkflowXml(xml)
{
	var fld = window_workflowView._form.form.findField("fld_xml");
	fld.setValue(xml);
}

function newWorkflow()
{
	viewWorkflow({"id": -1});
}

function viewWorkflow(paramList)
{
	paramList.module = currentCatalogOid;
	var p = Ext.urlEncode(paramList);
	openURL("<%=Consts.ITSM_HOME%>/configure/workflow/workflowEdit.jsp?"+p);
}
// create the Data Store
var ds = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: '<%=Consts.ITSM_HOME%>/configure/workflow/workflowQuery.jsp'
    }),

	baseParams: { "module": currentCatalogOid },
    // create reader that reads the Topic records
    reader: new Ext.data.JsonReader({
        root: 'items',
        totalProperty: 'totalCount',
        id: 'id'
    }, [
        {name: 'id', mapping: 'id'},
        {name: 'status', mapping: 'status'},
        {name: 'name', mapping: 'name'},
        {name: 'desc', mapping: 'desc'},
        {name: 'rule', mapping: 'rule'},
        {name: 'category', mapping: 'category'}
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
    

    var expander = new Ext.grid.RowExpander({
        tpl : new Ext.Template(
        		'<p><b>流程权限:</b><br>',
            "<font color='gray'>{rule}</font>"
        )
    });

    // the column model has information about grid columns
    // dataIndex maps the column to the specific data field in
    // the data store
    var cm = new Ext.grid.ColumnModel([expander,{
           header: "ID",
           dataIndex: 'id',
           width: 40,
			sortable: true,
           css: 'white-space:normal;'
        },{
           header: "名称",
			sortable: true,
           dataIndex: 'name',
           width: 150
        },{
           header: "描述",
			sortable: true,
           dataIndex: 'desc',
           width: 250
        },{
           header: "归属",
			sortable: true,
           dataIndex: 'category',
           width: 100
        },{
           header: "状态",
					sortable: true,
           dataIndex: 'status',
           width: 100
        }]);

	// 生成表格
    grid_workflowList = new Ext.grid.GridPanel({
        ds: ds,
        cm: cm,
				border:false,
				viewConfig: {
					autoFill: true
				},

        selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
        enableColLock:false,
        loadMask: true,
				plugins: expander,
				bbar: new Ext.PagingToolbar({
					store: ds,
					pageSize: <%=Consts.ITEMS_PER_PAGE%>,
					displayInfo: true,
					displayMsg: "<%=Consts.MSG_PAGE_DISPLAY%>",
					emptyMsg: "<%=Consts.MSG_PAGE_EMPTY%>",
					items: ['-', new Ext.Toolbar.Button({
							text: '新增',
							handler: newWorkflow
					})]
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
            items: grid_workflowList
			}]
	});
	grid_workflowList.on("rowdblclick", function(grid, rowIndex, e)
		{
			var id = grid.getStore().getAt(rowIndex).get("id");
			viewWorkflow({"id": id});
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

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
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
</head>

<body>
<script defer>
var grid_formList = null;
var currentCatalogOid = 6;


function newForm()
{
	viewForm({"id": -1});
	//viewForm({cfgXML:"<attribute/>"});
}

function viewForm(paramList)
{
	paramList.module = currentCatalogOid;
	Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/form/formEdit.jsp",
		paramList);
}

// 生成数据源
var formListDS = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: '<%=Consts.ITSM_HOME%>/configure/form/formQuery.jsp'
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
    
    // the column model has information about grid columns
    // dataIndex maps the column to the specific data field in
    // the data store
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
           header: "归属",
			sortable: true,
           dataIndex: 'category',
           width: 100
        }]);

	// 生成表格
    grid_formList = new Ext.grid.GridPanel({
        ds: formListDS,
        cm: cm,
		border:false,
		viewConfig: {
			autoFill: true
		},

        selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
        enableColLock:false,
        loadMask: true,

		bbar: new Ext.PagingToolbar({
				store: formListDS,
				pageSize: <%=Consts.ITEMS_PER_PAGE%>,
				displayInfo: true,
				displayMsg: "<%=Consts.MSG_PAGE_DISPLAY%>",
				emptyMsg: "<%=Consts.MSG_PAGE_EMPTY%>",
				items: ['-', new Ext.Toolbar.Button({
						text: '新增',
						handler: newForm
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
            items: grid_formList
			}]
	});
	grid_formList.on("rowdblclick", function(grid, rowIndex, e)
		{
			var id = grid.getStore().getAt(rowIndex).get("id");
			viewForm({"id": id});
		});
    // trigger the data store load
    formListDS.load({params:{start:0, limit:<%=Consts.ITEMS_PER_PAGE%>}});
});

function loadCodeType(catalogOid)
{
	currentCatalogOid = catalogOid;
	formListDS.baseParams.module=currentCatalogOid;
	formListDS.load({params:{start:0, limit:<%=Consts.ITEMS_PER_PAGE%>}});
}

</script>

</body>

</html>

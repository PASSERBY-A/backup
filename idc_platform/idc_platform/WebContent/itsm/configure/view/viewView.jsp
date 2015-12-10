<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>


<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

%>
<html>
<head>
  <title>视图列表</title>
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

	<div id="view-dialog">
		<div id="content">
		<div id="center"></div>
		<div id="south"></div>
		</div>
	</div>
	<script>

	function addView(type) {
		viewDetail({"viewOid": -1, "action": 'add',"type":type});
	}
  function viewDetail(paramList){
		Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/view/viewAdd.jsp",
		paramList);
  }
 var viewgrid = null;
Ext.onReady(function(){

    // create the Data Store
    var viewds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: '<%=Consts.ITSM_HOME%>/configure/view/viewQuery.jsp'
        }),

        // create reader that reads the Topic records
        reader: new Ext.data.JsonReader({
            root: 'views',
            totalProperty: 'totalCount',
            id: 'oid'
        }, [
						{name: 'oid',mapping: 'oid',type:'float'},
        		{name: 'name',mapping: 'name'},
        		{name: 'type',mapping: 'type'},
        		{name: 'rule',mapping: 'rule'},
        		{name: 'applyto',mapping: 'applyto'}
        ]),
				sortInfo:{field: 'oid', direction: 'ASC'}
    });

    var expander = new Ext.grid.RowExpander({
        tpl : new Ext.Template(
        		'<p><b>视图权限:</b><br>',
            "<font color='gray'>{rule}</font>"
        )
    });

    var viewcm = new Ext.grid.ColumnModel([expander,{
    		header: "OID",
    		dataIndex: 'oid'
    	},{
    		header: "名称",
    		dataIndex: 'name'
    	},{
    		header: "类型",
    		dataIndex: 'type'
    	},{
    		header: "属于",
    		dataIndex: 'applyto'
    	}]);


    // create the editor grid
   var viewMenu = new Ext.Toolbar.MenuButton({
			text: '新增视图',
			iconCls:'add',
			menu : {
				items: [{
	        text: '标准视图',
	        handler: function(){
	        	addView(<%=ViewInfo.TYPE_STANDARD%>);
	        }
	  		},{
	        text: '链接视图',
	        handler: function(){
	        	addView(<%=ViewInfo.TYPE_LINKED%>);
	        }
	  		}]
	  	}
	  });

   viewgrid = new Ext.grid.GridPanel({
      store: viewds,
      cm: viewcm,
      sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
      loadMask: true,
      border:false,
      plugins: expander,
      viewConfig: {
				autoFill: true
			},

      bbar: new Ext.PagingToolbar({
          pageSize: <%=Consts.ITEMS_PER_PAGE%>,
          store: viewds,
          displayInfo: true,
          displayMsg: '<%=Consts.MSG_PAGE_DISPLAY%>',
          emptyMsg: "<%=Consts.MSG_PAGE_EMPTY%>",
          items:[
            '-',viewMenu
            /*,'-',{
            	text:'删除',
	            handler:function(){
	            	var cell = viewgrid.getSelectionModel().getSelectedCell();
								if (cell == null)
									return;
								var n = cell[0];
								var r = viewds.getAt(n);
								r.get("oid");
	            }
            }*/
          ]
      })
    });
    viewMenu.on("click",function(){viewMenu.showMenu()});

    var viewport = new Ext.Viewport({
			layout:'border',
      items:[{
            region:'center',
            split:true,
            width: 225,
            minSize: 175,
            maxSize: 400,
            layout:'fit',
            margins:'1 1 1 1',
            items: viewgrid
			}]
	});

    viewgrid.on("rowdblclick", function(viewgrid, rowIndex, e)
		{
			var id = viewgrid.getStore().getAt(rowIndex).get("oid");
			viewDetail({"viewOid": id, "action": 'edit'});
		});


    // trigger the data store load
    viewds.load({params:{start:0, limit:<%=Consts.ITEMS_PER_PAGE%>}});


});
</script>
</body>
</html>
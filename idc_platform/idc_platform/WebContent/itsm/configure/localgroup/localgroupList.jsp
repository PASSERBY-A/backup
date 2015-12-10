<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>


<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Expires", "0");
%>
<html>
<head>
<title>本地工作组</title>
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
<script>
  function lgDetail(paramList){
		Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/localgroup/localgroupEdit.jsp",
		paramList);
  }

    // create the Data Store
    var lgds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: '<%=Consts.ITSM_HOME%>/configure/localgroup/localgroupQuery.jsp'
        }),
        // create reader that reads the Topic records
        reader: new Ext.data.JsonReader({
            root: 'lgs',
            totalProperty: 'totalCount'
        }, [
						{name: 'id',mapping: 'id'},
        		{name: 'name',mapping: 'name'},
        		{name: 'wfName',mapping: 'wfName'},
        		{name: 'status',mapping: 'status'}
        ])
    });

    var lgcm = new Ext.grid.ColumnModel([
    	{header: "ID",dataIndex: 'id'},
    	{header: "名称",dataIndex: 'name'},
    	{header: "所属流程",dataIndex: 'wfName'},
    	{header: "状态",dataIndex: 'status'}
    ]);

   var lggrid = new Ext.grid.GridPanel({

      ds: lgds,
      cm: lgcm,
      loadMask: true,
      viewConfig: {
		autoFill: true
	},
	tbar:[{
			text:"增加",
			handler:function(){
				lgDetail({"lgId": "", "action": 'add'});
			}
		}
		,'-',
		{
			text:"编辑",
			handler:function(){
				var selectRecord = lggrid.getSelectionModel().getSelected();
				if (!selectRecord){
					Ext.MessageBox.alert("信息","选择一条要编辑的记录");
    			return;
				}
				lgDetail({"lgId": selectRecord.get("id"), "action": 'edit'});
			}
		}
	],
      bbar: new Ext.PagingToolbar({
          pageSize: <%=Consts.ITEMS_PER_PAGE%>,
          store: lgds,
          displayInfo: true,
          displayMsg: '<%=Consts.MSG_PAGE_DISPLAY%>',
          emptyMsg: "<%=Consts.MSG_PAGE_EMPTY%>"
      })
    });
    
    var userds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: '<%=Consts.ITSM_HOME%>/configure/localgroup/localgroupUserQuery.jsp'
        }),
        baseParams:{'lgId':''},
        reader: new Ext.data.JsonReader({
            root: 'lgs',
            totalProperty: 'totalCount'
        }, [
						{name: 'id',mapping: 'id'},
        		{name: 'name',mapping: 'name'},
        		{name: 'status',mapping: 'status'}
        ])
    });
    var usergrid = new Ext.grid.GridPanel({
      ds: userds,
      cm: new Ext.grid.ColumnModel([
	    	{header: "ID",dataIndex: 'id'},
	    	{header: "名称",dataIndex: 'name'},
	    	{header: "状态",dataIndex: 'status'}
	    ]),
      loadMask: true,
      viewConfig: {
			autoFill: true
		},
		tbar:[{
				text:"增加",
				handler:function(){
					var selectRecord = lggrid.getSelectionModel().getSelected();
					if (!selectRecord){
						Ext.MessageBox.alert("信息","选择一个工作组");
	    			return;
					}
					Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/localgroup/localgroupAddUser.jsp",
						{'lgId':selectRecord.get("id")});
				}
			}
			,'-',
			{
				text:"删除",
				handler:function(){
					var lgRecord = lggrid.getSelectionModel().getSelected();
					if (!lgRecord){
						Ext.MessageBox.alert("信息","选择一个工作组");
	    			return;
					}
					
					var selectRecord = usergrid.getSelectionModel().getSelected();
					if (!selectRecord){
						Ext.MessageBox.alert("信息","选择一条人员记录");
	    			return;
					}
					if (confirm("确定要删除关联关系吗？")){
						var formElement = document.getElementById("local_post_form_div");
						if (!formElement) {
							formElement = document.createElement("div");
							formElement.style.visibility = 'hidden';
							formElement.style.position = 'absolute';
							formElement.id = "local_post_form_div";
							document.body.appendChild(formElement);
						}
						var simple = new Ext.form.FormPanel({
							defaultType: 'textfield',
							method: 'POST',
							baseParams: {type:'deleteRelations',id:lgRecord.get("id"),'fld_lgUser':selectRecord.get("id")},
							errorReader: new Ext.form.XmlErrorReader(),
							labelWidth: 75, // label settings here cascade unless overridden
							items: [{
									xtype: 'hidden',
					                name: '_temp_hidden_field'
					            }],
							url:"localgroupPost.jsp"
						});
						simple.render(formElement);
						simple.form.submit({
							waitMsg: '<%=Consts.MSG_WAIT%>',
							success: function(form, action)
							{
								Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text);
								userds.load();
							},
							failure: function(form, action)
							{
								Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_FAILED%>", form.errorReader.xmlData.documentElement.text);
							}
						});
					}
				}
			}
		]
    });

Ext.onReady(function(){

	var viewport = new Ext.Viewport({
		layout:'border',
	  	items:[{
			region:'center',
			margins:'1 1 1 1',
			layout:'fit',
			border:false,
			items:lggrid
		},{
			region:'east',
			split:true,
			width: 225,
			minSize: 175,
			layout:'fit',
			border:false,
			maxSize: 400,
			margins:'1 1 1 1',
			items:usergrid
		}]
	});

  lggrid.on("rowdblclick", function(g, rowIndex, e){
		var id = g.getStore().getAt(rowIndex).get("id");
		lgDetail({"lgId": id, "action": 'edit'});
	});
	
	lggrid.on("rowclick",function(g,rowIndex,e){
		var id = g.getStore().getAt(rowIndex).get("id");
		userds.baseParams.lgId = id;
		userds.load();

	});

	lgds.load({params:{start:0, limit:<%=Consts.ITEMS_PER_PAGE%>}});
});
</script>
</body>
</html>
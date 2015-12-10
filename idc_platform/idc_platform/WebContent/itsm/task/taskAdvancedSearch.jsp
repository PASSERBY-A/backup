<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.security.rule.*"%>
<%@ include file="../getUser.jsp"%>

<html>
<head>
  <title>工单高级查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

</head>
<body>

<script>
	var wfCombox = new Ext.form.ComboBox({
	    fieldLabel: '流程',
	    hiddenName:'task_wf',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : [
	        	<%
		        	List wfList = WorkflowManager.getWorkflows(true);
		        	int wfCount = wfList.size();
		        	for (int i = 0; i < wfCount; i++) {
		        		WorkflowInfo wfInfo  = (WorkflowInfo)wfList.get(i);
		        		if (i>0)
		        			out.print(",");
		        		out.print("['"+wfInfo.getOid()+"','"+wfInfo.getName()+"']");

		        	}
		        %>
	        ]
	    }),
	    width:100,
	    mode: 'local',
	    triggerAction: 'all',
	    editable:false,
	    displayField:'name',
	    valueField: 'value',
	    allowBlank: false
	});
	wfCombox.on("change",function(f,nv,ov){
		wfStatusDs.baseParams.wfOid=nv;
		wfStatusDs.reload();

	});

	var wfStatusDs = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: '<%=Consts.ITSM_HOME%>/configure/workflowStatusQuery.jsp'
    }),
		baseParams:{'wfOid':'-1'},
    reader: new Ext.data.JsonReader({
        root: 'items',
        totalProperty: 'totalCount'
    }, [
    		{name: 'id', mapping: 'id'},
    		{name: 'caption', mapping: 'caption'}
    ])
  });
	var statusCombox = new Ext.form.ComboBox({
	    fieldLabel: '状态',
	    hiddenName:'task_status',
	    store: wfStatusDs,
	    width:100,
	    triggerAction: 'all',
	    editable:false,
	    displayField:'caption',
	    allowBlank:false,
	    valueField: 'id'
		});

	var searchForm = new Ext.form.FormPanel({
		region:'north',
    frame: true,
    height: 100,
    border: false,
    layout: 'column',
    labelWidth: 60,
    labelAlign: 'right',
    items: [{
  		columnWidth: 0.4,
  		layout: 'form',
  		defaultType: 'textfield',
      items: [wfCombox,
      {
        fieldLabel: '标题',
        width:100,
        name: 'task_title'
    	},{
        fieldLabel: '创建人',
        width:100,
        name: 'task_create_by'
      }]
    },{
    	columnWidth: 0.4,
    	layout: 'form',
    	defaultType: 'textfield',
      items: [{
        xtype: 'datefield',
        fieldLabel: '创建时间',
        width:100,
        name: 'task_create_time_b'
      },{
        xtype: 'datefield',
        fieldLabel: '至',
        width:100,
        name: 'task_create_time_e'
      },statusCombox
      	]
    },{
    	columnWidth: 0.2,
    	layout: 'form',
    	items:[new Ext.Button({
    		text:"检索",
    		handler: function() {
    			var form = searchForm.form;
    			if (!form.isValid()) {
    				return;
    			}

    			linkAddds.baseParams.task_title = form.findField("task_title").getValue();
    			linkAddds.baseParams.task_wf = form.findField("task_wf").getValue();
    			linkAddds.baseParams.task_create_by = form.findField("task_create_by").getValue();
    			linkAddds.baseParams.task_create_time_b = form.findField("task_create_time_b").getValue();
    			linkAddds.baseParams.task_create_time_e = form.findField("task_create_time_e").getValue();
    			linkAddds.baseParams.task_status = form.findField("task_status").getValue();
    			linkAddds.load({params:{start:0,limit:15}});
        }
    	})]
    }]
	});


	var linkAddds = new Ext.data.GroupingStore({
      proxy: new Ext.data.HttpProxy({
          url: '<%=Consts.ITSM_HOME%>/task/taskQuery.jsp'
      }),
      baseParams:{},
      reader: new Ext.data.JsonReader({
          root: 'items',
          totalProperty: 'totalCount'
      }, [
      		{name: 'oid', mapping: 'oid'},
      		{name: 'origin', mapping: 'origin'},
      		{name: 'title', mapping: 'title'},
      		{name: 'status', mapping: 'status'},
      		{name: 'wfName', mapping: 'wfName'},
      		{name: 'wfOid', mapping: 'wfOid'},
      		{name: 'wfVer', mapping: 'wfVer'},
      		{name: 'create_time', mapping: 'create_time'},
      		{name: 'create_by', mapping: 'create_by'}
      	]
      )
  });
  var linkcm = new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(),
			new Ext.grid.CheckboxSelectionModel(),
			{header:'OID',dataIndex:'oid',width:40},
			{header:'来源',dataIndex:'origin',width:40},
			{header:'标题',dataIndex:'title',width:40},
			{header:'创建人',dataIndex:'create_by',width:40},
			{header:'创建时间',dataIndex:'create_time',width:40},
			{header:'流程',dataIndex:'wfName',width:40},
			{header:'状态',dataIndex:'status',width:40}
	]);
  var linkTaskAddGrid = new Ext.grid.GridPanel({
  	title:'检索结果',
    store: linkAddds,
    cm: linkcm,
    sm:new Ext.grid.CheckboxSelectionModel(),
    region:'center',
		border:false,
		loadMask: true,
		enableColLock:false,
		viewConfig: {
			forceFit:true
		},
		bbar: new Ext.PagingToolbar({
      pageSize: 15,
      store: linkAddds,
      displayInfo: true,
      displayMsg: '<%=Consts.MSG_PAGE_DISPLAY%>',
      emptyMsg: "<%=Consts.MSG_PAGE_EMPTY%>"
    }),
    buttons:[{
    	text:'确定',
    	handler:searchSubmit
    },{
    	text:'取消',
    	handler:function(){
    		window.close();
    	}
    }],
    listeners:{
    	rowdblclick:function(g,rowIndex,e){
	  		var record = this.store.getAt(rowIndex);
	  		window.returnValue = record.get("oid");
				window.close();
	  	}
    }
	});
function searchSubmit() {
	var selectionModel = linkTaskAddGrid.getSelectionModel();
	var selectRecords = selectionModel.getSelections();
	var countRecords = selectRecords.length;
	var retStr = "";
	for (var i = 0; i < countRecords; i++)
	{
		var selectRecord = selectRecords[i];
		if (i > 0)
			retStr += ",";
		retStr += selectRecord.get("oid");
	}
	
	window.returnValue = retStr;
	window.close();
}
Ext.onReady(function(){
  var viewport = new Ext.Viewport({
		layout:'border',
    items:[searchForm,linkTaskAddGrid]
	});
});


function taskPostCallBack(ret){
	viewds.reload();
}

	</script>
</body>
</html>
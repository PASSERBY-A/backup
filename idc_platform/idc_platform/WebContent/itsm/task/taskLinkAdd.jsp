<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>

<%@ page import="java.util.*"%>


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
        url: '<%=Consts.ITSM_HOME%>/configure/workflow/workflowStatusQuery.jsp'
    }),
		baseParams:{'wfOid':'-1','includeAll':'true'},
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
    autoHeight: true,
    border: false,
    layout: 'column',
    labelWidth: 60,
    labelAlign: 'right',
    items: [{
  		columnWidth: 0.4,
  		layout: 'form',
  		defaultType: 'textfield',
      items: [{
        fieldLabel: '标题',
        width:100,
        name: 'task_title'
    	},
    	wfCombox,
    	{
        fieldLabel: '创建人',
        width:100,
        name: 'task_create_by'
      }
			]
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
    			linkAddds.reload();
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
  	title:'关联工单',
    store: linkAddds,
    cm: linkcm,
    sm:new Ext.grid.CheckboxSelectionModel(),
    region:'center',
		border:false,
		loadMask: true,
		enableColLock:false,
		viewConfig: {
			forceFit:true
		}
	});

	var formWindow = null;
	if (formWindow) {
		formWindow.close();
	}

	formWindow = new Ext.Window({
      title: '添加关联工单',
      width: 510,
      height:400,
      minWidth: 300,
      minHeight: 200,
      layout: 'border',
      plain:true,
      iconCls :'edit',
      buttonAlign:'center',
      modal:true,
      items: [searchForm,linkTaskAddGrid],

      buttons: [{
          text: '添加',
          handler: function() {
          	var linkPlant = Ext.data.Record.create([
						   {name: 'oid', type: 'string'},
		      		{name: 'origin', type: 'string'},
		      		{name: 'title', type: 'string'},
		      		{name: 'status', type: 'string'},
		      		{name: 'wfName', type: 'string'},
		      		{name: 'wfOid', type: 'string'},
		      		{name: 'wfVer', type: 'string'},
		      		{name: 'create_time', type: 'string'},
		      		{name: 'create_by', type: 'string'}
					  ]);
          	var ds = formWindow.linkAddds;
          	var linkgrid = formWindow.linkTaskAddGrid;
          	var selectionModel = linkgrid.getSelectionModel();

          	var selectRecords = selectionModel.getSelections();
						var countRecords = selectRecords.length;

						for (var i = 0; i < countRecords; i++)
						{

							var selectRecord = selectRecords[i];
							var ap = new linkPlant({
								oid: selectRecord.get("oid"),
								origin: selectRecord.get("origin"),
								title: selectRecord.get("title"),
								status: selectRecord.get("status"),
								wfName: selectRecord.get("wfName"),
								wfOid: selectRecord.get("wfOid"),
								wfVer: selectRecord.get("wfVer"),
								create_time: selectRecord.get("create_time"),
								create_by: selectRecord.get("create_by")
							});

							//验证列表里面存在的记录，是否有重复的
							var count = linkds.getCount();
							var hasExist = false;
							for (var j = 0; j < count; j++) {
								var oid = linkds.getAt(j).get("oid");
								var wfName = linkds.getAt(j).get("wfName");
								if (oid == selectRecord.get("oid") && wfName==selectRecord.get("wfName")) {
									hasExist = true;
									break;
								}
							}

							if (!hasExist) {
								linkds.add(ap);
							}
						}
          }
      },{
          text: '关闭',
          handler: function() {
            formWindow.close();
          }
      }]
  });

	formWindow.linkAddds = linkAddds;
	formWindow.linkTaskAddGrid = linkTaskAddGrid;

  formWindow.on("close",function(){
		//document.location.reload();
	});

  formWindow.show();
</script>
<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<html>
<head>
  <title>流程编辑</title>
	<xml:namespace ns="urn:schemas-microsoft-com:vml" prefix="v"/>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk"/>
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>
	<script src="<%=Consts.ITSM_HOME%>/js/Ext.workflow.js"></script>
	<script src="<%=Consts.ITSM_HOME%>/js/Ext.workflow.Node.js"></script>
	<script src="<%=Consts.ITSM_HOME%>/js/Ext.workflow.Action.js"></script>
	<style>
		v\:* {BEHAVIOR: url(#default#VML)}
	</style>
</head>

<body>
<%
try {
int oid = Integer.parseInt(request.getParameter("id"));
int module = Integer.parseInt(request.getParameter("module"));
//String type = request.getParameter("type");
WorkflowInfo workflow = WorkflowManager.getWorkflowByOid(oid);
int width = 175;

int currentVersion = -1;
if (workflow!=null)
	currentVersion = workflow.getCurrentVersionId();
%>

<div width="100%" id="dlg_workflow_view"></div>

<script defer>

var workflowPanel = new Ext.workflow.GraphicsPanel({
	height : 300,
	border:false,
	wfOid:<%=oid%>
	});
workflowPanel.moduleOid = <%=module%>;
<%
		PersonFieldInfo pfInfo = new PersonFieldInfo();
		pfInfo.setId("editRule");
		pfInfo.setName("超级处理");
		pfInfo.setSingleMode(false);
		pfInfo.setAllowSelectGroup(true);
		pfInfo.parse();
		out.print(pfInfo.getFormCode(150));
%>
fld_editRule.anchor = '-5';
var wf_id = new Ext.form.TextField({
	fieldLabel: 'ID',
	name: 'fld_oid',
	anchor:'-5',
	value: '-1'
});

var wf_name = new Ext.form.TextField({
	fieldLabel: '名称',
	name: 'fld_name',
	anchor:'-5',
	allowBlank:false
});

var wf_origin = new Ext.form.ComboBox({
  fieldLabel: '工单系统',
  hiddenName:'fld_origin',
  store: new Ext.data.SimpleStore({
      fields: ['value', 'name'],
      data : [
      	['ITSM','ITSM'],['OVSD','OVSD']
      ]
  }),
  displayField:'name',
  valueField: 'value',
  typeAhead: true,
  mode: 'local',
  triggerAction: 'all',
  emptyText:'请选择......',
  selectOnFocus:true,
  anchor:'-5',
  forceSelection: true,
  editable:false,
	allowBlank:false
});

var wf_deal_page = new Ext.form.TextField({
	fieldLabel: '处理页面',
	name: 'fld_deal_page',
	anchor:'-5',
	value:'$ITSM_HOME/task/taskInfo.jsp',
	allowBlank:false
});
var wf_rule= new Ext.form.SelectDialogField({
	fieldLabel: '新建权限',
	name: 'fld_rule',
	anchor:'-5',
	allowBlank:true,
	selectUrl:"<%=Consts.ITSM_HOME%>/configure/view/viewRuleSelect.jsp"
});

var wf_allowLocalGroup = new Ext.form.Checkbox({
	name: 'fld_allowLocalGroup',
	fieldLabel: '本组处理',
	allowBlank: true
});
var wf_enableSMS = new Ext.form.Checkbox({
	name: 'fld_enableSMS',
	fieldLabel: '启用短信',
	checked:true,
	allowBlank: true
});
var wf_loadHis = new Ext.form.Checkbox({
	name: 'fld_loadHisToCache',
	fieldLabel: '加载历史',
	checked:false,
	allowBlank: true
});

var wf_status = new Ext.form.ComboBox({
	fieldLabel: '流程状态',
	hiddenName:'fld_status',
	store: new Ext.data.SimpleStore({
	    fields: ['value', 'name'],
	    data : [
	    	['1','启用'],['0','已停用']
	    ]
	}),
	displayField:'name',
	valueField: 'value',
	typeAhead: true,
	value :'1',
	mode: 'local',
	triggerAction: 'all',
	emptyText:'请选择......',
	selectOnFocus:true,
	anchor:'-5',
	forceSelection: true,
	editable:false,
	allowBlank:false
});
var wf_dataTable = new Ext.form.TextField({
	fieldLabel: '数据存储表',
	name: 'fld_data_table',
	anchor:'-5',
	allowBlank:true
});

var snap_remote_viewPage = new Ext.form.TextField({
	name: 'snap_remote_viewPage',
	anchor:'-5',
	fieldLabel: '处理页面',
	allowBlank: true
});

var formds = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '<%=Consts.ITSM_HOME%>/configure/form/formQuery.jsp'
	}),

	baseParams: { "module":<%=module%>, "start": 0, "limit" : 999999 },
	// create reader that reads the Topic records
	reader: new Ext.data.JsonReader({
		root: 'items',
		totalProperty: 'totalCount',
		id: 'id'
	}, [
		{name: 'id', mapping: 'id'},
		{name: 'name', mapping: 'name'}
	]),
	remoteSort: true
});

var snap_local_form = new Ext.form.ComboBox({
	store: formds,
	anchor:'-5',
	hiddenName:'snap_local_form',
	displayField:'name',
	fieldLabel: '关联表单',
	valueField:'id',
	typeAhead: false,
	mode: 'remote',
	maxHeight: 150,
	triggerAction: 'all',
	emptyText:'请选择表单',
	forceSelection:true,
	selectOnFocus:true,
	allowBlank: true,
	listeners:{
		change:function(f,nv,ov) {
			snap_local_viewTemplate.params.formOid = nv;
		}
	}
});

var snap_local_viewTemplate = new Ext.form.SelectDialogField({
   fieldLabel: '展示模板',
   anchor:'-5',
   name: 'snap_local_viewTemplate',
   selectUrl : '',
   valueParam:'template',
   dialogWidth:800,
   dialogHeight:600,
   editable:true,
   allowBlank:true,
   value:'/default/nodeAction.html'
});

var fld_snap_mode = new Ext.ux.RadioGroup({
	name:'fld_snap_mode',
	fieldLabel: '历史快照',
	horizontal:true,
	radios:[{
		boxLabel :'本地',
		value:'local',
		listeners:{
			check:function(obj,citem){
				if(citem) {
					//snap_local_form.allowBlank = false;
					//snap_local_viewTemplate.allowBlank = false;
					
					//snap_remote_viewPage.allowBlank = true;
				} else {
					//snap_local_form.allowBlank = true;
					//snap_local_viewTemplate.allowBlank = true;
					
					//snap_remote_viewPage.allowBlank = false;
				}
			}
		}
	},{
		boxLabel :'远程',
		value:'remote'
	}]
});

var basicFieldSet = new Ext.form.FieldSet({
	collapsible: false,
	title: '常规信息',
	layout : 'form',
	labelWidth: 70,
	collapsed: false,
	anchor:'-20',
	items:[{
		layout:'column',
		border: false,
		items:[{
				columnWidth: .5,
				layout: 'form',
				border:false,
				items:[wf_id,wf_name,wf_origin,wf_status]
			},{
				columnWidth: .5,
				layout: 'form',
				border:false,
				items:[wf_deal_page,wf_dataTable,wf_enableSMS,wf_loadHis]
			}]
	}]
});

var changeDS = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '<%=Consts.ITSM_HOME%>/configure/workflow/workflowQuery.jsp'
	}),
	baseParams: { "module": "-1", "start": 0, "limit" : 999999 },
	reader: new Ext.data.JsonReader({
		root: 'items',
		totalProperty: 'totalCount',
		id: 'id'
	}, [
		{name: 'id', mapping: 'id'},
		{name: 'name', mapping: 'name'}
	]),
	remoteSort: true
});

var changeWf = new Ext.form.ComboBox({
	store: changeDS,
	hiddenName:'fld_changeWf',
	displayField:'name',
	valueField:'id',
	fieldLabel: '变更申请流程',
	mode: 'remote',
	maxHeight: 150,
	width:150,
	triggerAction: 'all',
	editable:false,
	forceSelection:true,
	selectOnFocus:true,
	allowBlank: true
});

var ruleFieldSet =  new Ext.form.FieldSet({
	collapsible: false,
	title: '权限配置',
	layout : 'form',
	labelWidth: 70,
	collapsed: false,
	anchor:'-20',
	items:[{
		layout:'column',
		border: false,
		items:[{
				columnWidth: .5,
				layout: 'form',
				border:false,
				items:[wf_allowLocalGroup,changeWf]
			},{
				columnWidth: .5,
				layout: 'form',
				border:false,
				items:[wf_rule,fld_editRule]
			}]
	}]
});

var local_form = new Ext.form.FieldSet({
	title: '本地表单',
	layout: 'form',
	autoHeight:true,
	items:[snap_local_form,snap_local_viewTemplate]
});

var remote_form = new Ext.form.FieldSet({
	title: '远程页面',
	layout: 'form',
	autoHeight:true,
	items:[snap_remote_viewPage]
});

var snapFieldSet = new Ext.Panel({
	layout: 'column',
	border:false,
	anchor:'-20',
	items:[{
		columnWidth: .5,
		style : 'padding-right: 5px;',
		layout:'form',
		items:[fld_snap_mode,remote_form]
	},{
		columnWidth: .5,
		items:[local_form]
	}]
});

var sms_new = new Ext.form.TextArea({
	fieldLabel:"新任务到达短信",
	name:'sms_new',
	anchor:'-20'
});
var sms_dealed = new Ext.form.TextArea({
	fieldLabel:"工单被代处理短信",
	name:'sms_dealed',
	anchor:'-20'
});
var sms_rollback = new Ext.form.TextArea({
	fieldLabel:"工单回退短信",
	name:'sms_rollback',
	anchor:'-20'
});
var sms_overtime = new Ext.form.TextArea({
	fieldLabel:"工单超时后循环提醒短信",
	name:'sms_overtime',
	anchor:'-20'
});
var sms_overdue = new Ext.form.TextArea({
	fieldLabel:"工单超时短信",
	name:'sms_overdue',
	anchor:'-20'
});
var sms_remind = new Ext.form.TextArea({
	fieldLabel:"工单超催办短信",
	name:'sms_remind',
	anchor:'-20'
});

var sms_template = new Ext.form.FieldSet({
	title: '短信模板',
	layout: 'form',
	labelAlign:'top',
	autoHeight:true,
	items:[sms_new,sms_dealed,sms_rollback,sms_overtime,sms_overdue,sms_remind]
});

var simple = new Ext.form.FormPanel({
	method: 'POST',
	region:'center',
  defaultType: 'textfield',
	baseParams: {"fld_applyto": <%=module%>,"forceModify":false},
	errorReader: new Ext.form.XmlErrorReader(),
	labelWidth: 60, // label settings here cascade unless overridden
	labelAlign:'right',
	layout: 'fit',
	items: {
		xtype:'tabpanel',
		activeTab: 0,
		border:false,
		items:[{
			title:'基本信息',
			layout:'form',
			border:false,
			frame:true,
			margins : '1 1 1 1',
			defaultType: 'textfield',
			autoScroll: true,
			listeners: {
				activate: function(tab) {
					if (workflowPanel.doc) {
						var form = simple.form;
						form.findField('fld_xml').setValue(workflowPanel.doc.xml);
					}
				}
			},
			items: [
				basicFieldSet,ruleFieldSet,snapFieldSet,
				new Ext.form.TextArea({
					fieldLabel: '描述',
					name: 'fld_desc',
					anchor:'-20'
				}),sms_template,
				new Ext.form.Hidden({
					fieldLabel: '流程',
					name: 'fld_xml',
					width:<%=width%>,
					height:200,
					readOnly: true,
					allowBlank:true
				})
				]
		}, {
			title:'流程图',
			layout:'fit',
			baseCls: 'x-plain',
			defaultType: 'textfield',
			border:'false',
			listeners: {
				activate: function(tab) {
					var form = simple.form;
					if (!workflowPanel.doc)
						workflowPanel.loadWorkflow(form.findField('fld_xml').getValue());
					
					workflowPanel.origin = form.findField('fld_origin').getValue();
				}
			},
			items: workflowPanel
		}
		]
	},

	url:'<%=Consts.ITSM_HOME%>/configure/workflow/workflowPost.jsp'
});


var formFieldds = new Ext.data.GroupingStore({
    proxy: new Ext.data.HttpProxy({
        url: '<%=Consts.ITSM_HOME%>/configure/form/formFieldQuery.jsp'
    }),

		baseParams: { "formList": '' },
    reader: new Ext.data.JsonReader({
        root: 'items',
        totalProperty: 'totalCount'
    }, [
        {name: 'id', mapping: 'id'},
        {name: 'name', mapping: 'name'},
        {name: 'formName', mapping: 'formName'}
    ]),
    sortInfo:{field: 'formName', direction: 'DESC'},
    groupField:'formName'
});

var formFieldList = new Ext.grid.GridPanel({
	title:'用到的表单',
	region:'east',
	split:'true',
	collapsible :true,
	collapseMode:'mini',
	collapsed :false,
	margins : '0 0 0 1',
	width:200,
	columns:[
		{header:'id',dataIndex:'id'},
		{header:'name',dataIndex:'name'},
		{header:'formName',dataIndex:'formName',hidden :true}
	],
	ds : formFieldds,
	view: new Ext.grid.GroupingView({
			showGroupName:false,
      groupTextTpl: '{text} ({[values.rs.length]} {["条"]})'
	}),
	tbar:[{text:'刷新',handler:loadFormField}]
});

var verData = [
<%
List<WorkflowData> wdList = new ArrayList<WorkflowData>();
if (workflow != null) {
	wdList = workflow.getWorkflows();
}
for (int i = 0; i < wdList.size(); i++) {
	WorkflowData wfData = wdList.get(i);
	if (i > 0)
		out.println(",");
	int count = WorkflowManager.getTaskInfoOfWfVersion(oid,wfData.getVersionId());
	out.println("{num:"+wfData.getVersionId()+",count:"+count+"}");
}
%>
]
var versionStore = new Ext.data.JsonStore({
	fields : ['num','count']
});

var versionGrid = new Ext.grid.GridPanel({
	title:'版本',
	region:'west',
	split:'true',
	collapsible :true,
	collapsed :true,
	margins : '0 0 0 1',
	width:200,
	cm:new Ext.grid.ColumnModel([
		{header:'版本号',dataIndex:'num'},
		{header:'工单个数',dataIndex:'count'}
	]),
	ds : versionStore,
	tbar:[{
		text:'删除',
		handler:function (){
			var record = versionGrid.getSelectionModel().getSelected();
			if (record != null) {
				if(record.get("num") == <%=currentVersion%>) {
					Ext.Msg.alert("删除失败", "当前处理版本不能删除");
					return;
				}
				Ext.Ajax.request({
				    url: "<%=Consts.ITSM_HOME%>/configure/workflow/workflowPost.jsp",
				    params: {'type':'removeVersion','wfOid':<%=oid%>,'wfVer':record.get("num")},
				    success: function(res) {
				    	var doc_ = new ActiveXObject("Microsoft.XMLDOM");
				    	doc_.loadXML(res.responseText);
				        alert(doc_.documentElement.text);
				        versionStore.remove(record);
				    },
				    failure: function() {
				    	Ext.Msg.alert("获取信息失败", "页面连接失败");
				    }
				});
			}
		}
	}],
	listeners:{
		rowdblclick :function(gp,ri,e){
			var p = grid.getStore().getAt(rowIndex);
			p.get(num);
		}
	}
});

versionStore.loadData(verData);

function loadFormField(vnum){
	var doc_ = new ActiveXObject("Microsoft.XMLDOM");
	var xml;
	if (workflowPanel.doc)
		xml = workflowPanel.doc.xml;
	if (xml == null || xml == "")
		xml = simple.form.findField('fld_xml').getValue();
	doc_.loadXML(xml);
	if (!(doc_ && doc_.documentElement))
		return;
	var el = doc_.documentElement.selectNodes("./node/actions/action");
	var formStr = '';
	for (var i = 0; i < el.length; i++){
		if (i > 0)
			formStr += ",";
		formStr += el[i].getAttribute("formId");
	}
	formFieldList.getStore().baseParams.formList = formStr;
	formFieldList.getStore().reload();
};

function submitForm(){
	var form = simple.form;
	var doc = workflowPanel.doc;
	if (doc == null) {
		doc = new ActiveXObject("Microsoft.XMLDOM");
		doc.loadXML(form.findField('fld_xml').getValue());
	}
	var el = doc.documentElement.selectSingleNode("./desc");
	if (el === null)
	{
		el = doc.createElement("desc");
		doc.documentElement.appendChild(el);
	}
	el.text = form.findField('fld_desc').getValue();
	doc.documentElement.setAttribute("dataTable",wf_dataTable.getValue());
	
	form.findField('fld_xml').setValue(doc.xml);
	if (!form.isValid()) {
		Ext.MessageBox.alert("提示", "请填写完整的内容");
		return;
	}
	form.items.each(function(f){
		f.old_status_disabled = f.disabled;
		f.enable();
	});	
	form.submit({
		waitMsg: '<%=Consts.MSG_WAIT%>',
		success: function(form, action) {
			form.items.each(function(f){
				if (f.old_status_disabled)
					f.disable();
			});
			Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text);
			//window_workflowView.close();
			opener.ds.reload();
		},
		failure: function(form, action) {
			form.items.each(function(f){
			   if (f.old_status_disabled)
				   f.disable();
			});
			Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_FAILED%>", form.errorReader.xmlData.documentElement.text);
		}
	});
};
Ext.onReady(function(){
	 var viewport = new Ext.Viewport({
         layout:'border',
         items:[{
             layout:'border',
             region:'center',
             items:[versionGrid,simple, formFieldList],
             buttons: [{
                 text:'强制覆盖',
                 handler:function(){
	            	var form = simple.form;
	            	form.baseParams.forceModify = true;
	            	submitForm();
             	}
             },{
         		text: '确定',
         		handler: function() {
            		submitForm();
         		}
         	},{
         		text: '取消',
         		handler: function() {
         			window.close();
         		}
         	}]
         }]
             
     });
	 var fld = simple.form.findField("fld_oid");
	 fld.disable();
	 <%
	 	if (workflow != null) {
	 		WorkflowData data = workflow.getCurrentVersion();
	 %>
	 	fld.setValue("<%=oid%>");
	 	fld = simple.form.findField("fld_name");
	 	fld.setValue("<%=StringUtil.escapeJavaScript(workflow.getName())%>");
	 	fld = simple.form.findField("fld_origin");
	 	fld.setValue("<%=workflow.getOrigin()%>");
	 	fld = simple.form.findField("fld_rule");
	 	fld.setValue("<%=workflow.getRule().replaceAll("=",":")%>");
	 	fld = simple.form.findField("fld_status");
	 	fld.setValue("<%=workflow.getStatus()%>");
	 	fld = simple.form.findField("fld_editRule");
	 	fld.setValue("<%=workflow.getEditRule()%>");
	 	fld = simple.form.findField("fld_enableSMS");
	 	fld.setValue("<%=workflow.isEnableSMS()%>");
	 	fld = simple.form.findField("fld_allowLocalGroup");
	 	fld.setValue("<%=workflow.isAllowLocalGroup()%>");
	 	fld = simple.form.findField("fld_loadHisToCache");
	 	fld.setValue("<%=workflow.isLoadHisToCache()%>");
	 	fld = simple.form.findField("fld_deal_page");
	 	fld.setValue("<%=workflow.getDealPage()%>");
	 	fld = simple.form.findField("fld_snap_mode");
	 	fld.setValue("<%=workflow.getSnapMode()%>");
	 	fld = simple.form.findField("snap_local_form");
	 	fld.setValue("<%=workflow.getSnapLocalFormOid()%>");
	 	fld = simple.form.findField("snap_local_viewTemplate");
	 	fld.setValue("<%=workflow.getSnapLocalFormTemplate()%>");
	 	fld = simple.form.findField("snap_remote_viewPage");
	 	fld.setValue("<%=workflow.getSnapRemoteViewPage()%>");
	 	fld = simple.form.findField("fld_changeWf");
	 	fld.setValue("<%=workflow.getChangeWfOid()%>");
	 <%if (data != null) {%>
	 	fld = simple.form.findField("fld_xml");
	 	fld.setValue("<%=StringUtil.escapeJavaScript(data.getXmlData()).replaceAll("</script>", "<\" + \"/script>")%>");
	 	fld = simple.form.findField("fld_desc");
	 	fld.setValue("<%=StringUtil.escapeJavaScript(data.getDesc())%>");
	 	fld = simple.form.findField("fld_data_table");
	 	fld.setValue("<%=data.getDataTable()%>");
	 <% }
	 	SMSTemplate smsTemp = workflow.getSmsTemplate();
	 	if (smsTemp!=null) {
	 %>
	 	sms_new.setValue("<%=StringUtil.escapeJavaScript(smsTemp.getSmsNew())%>");
	 	sms_rollback.setValue("<%=StringUtil.escapeJavaScript(smsTemp.getSmsRollback())%>");
	 	sms_dealed.setValue("<%=StringUtil.escapeJavaScript(smsTemp.getSmsDealed())%>");
	 	sms_overtime.setValue("<%=StringUtil.escapeJavaScript(smsTemp.getSmsOvertime())%>");
	 	sms_overdue.setValue("<%=StringUtil.escapeJavaScript(smsTemp.getSmsOverdue())%>");
	 	sms_remind.setValue("<%=StringUtil.escapeJavaScript(smsTemp.getSmsRemind())%>");
	 <%
	 	}
	 } %>

	 loadFormField();
});
/*if (window_workflowView)
	window_workflowView.close();
window_workflowView = new Ext.Window({
	title: '流程信息',
	width: 750,
	height:460,
	minWidth: 320,
	minHeight: 360,
	layout: 'border',
	bodyBorder:false,
	border:false,
	bodyStyle:'padding:5px;',
	items: [versionGrid,simple, formFieldList],

	
});
  window_workflowView.on("close",function(){
		//document.location.reload();
	});
window_workflowView._form = simple;
window_workflowView._workflow = workflowPanel;
window_workflowView.show();
*/

</script>
<%} catch (Exception e) {
	e.printStackTrace();
}
%>

</body>

</html>
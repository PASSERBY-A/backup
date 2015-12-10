<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.workflow.rule.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=GBK");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

String referToWFOid = request.getParameter("referToWFOid");
String referToUserField = request.getParameter("referToUserField");
String referFromTaskOidField = request.getParameter("referFromTaskOidField");
String runPosition = request.getParameter("runPosition");

%>
(function() {
	var ruleWindow = null;
	if (ruleWindow)
		ruleWindow.close();
	// create the editor grid

	var store = new Ext.data.Store({
		proxy: new Ext.data.HttpProxy({
			url: ITSM_HOME+'/configure/workflow/workflowQuery.jsp'
		}),

		baseParams: { "module": "-1", "start": 0, "limit" : 999999 },
		// create reader that reads the Topic records
		reader: new Ext.data.JsonReader({
			root: 'items',
			totalProperty: 'totalCount',
			id: 'id'
		}, [
			{name: 'id', mapping: 'id'},
			{name: 'name', mapping: 'name'}
		]),

		// turn on remote sorting
		remoteSort: true
	});
	
	var fld_referToWF = new Ext.form.ComboBox({
		store: store,
		name:'fld_referToWF',
		value:'<%=(referToWFOid==null?"":referToWFOid)%>',
		displayField:'name',
		fieldLabel: '查阅流程',
		valueField:'id',
		typeAhead: false,
		mode: 'remote',
		maxHeight: 150,
		width:150,
		triggerAction: 'all',
		forceSelection:true,
		selectOnFocus:true,
		allowBlank: false
	});

	var fld_referToUser = new Ext.form.TextField({
		name: 'fld_referToUser',
		fieldLabel: '查阅人字段',
		value:'<%=(referToUserField==null?"":referToUserField)%>',
		width:150,
		allowBlank: false
	});

	var fld_referFromTaskOidField = new Ext.form.TextField({
		name: 'fld_referFromTaskOidField',
		fieldLabel: '存原工单OID字段',
		value:'<%=(referFromTaskOidField==null?"":referFromTaskOidField)%>',
		width:150,
		allowBlank: false
	});

	var fld_runPosition = new Ext.form.ComboBox({
		store: new Ext.data.SimpleStore({
			fields: ['value', 'text'],
			data : [
				['<%=AbstractRule.RULE_RUN_POSITION_ENTER%>','页面打开时(onEnter)'],
				['<%=AbstractRule.RULE_RUN_POSITION_UPDATE%>','工单提交时(onUpdate)'],
				['<%=AbstractRule.RULE_RUN_POSITION_UPDATE_END%>','提交完毕时(onUpdateEnd)'],
				['<%=AbstractRule.RULE_RUN_POSITION_OVERTIME%>','工单超时时(onOvertime)'],
				['<%=AbstractRule.RULE_RUN_POSITION_ROLLBACK%>','工单回退时(onRollback)']
			]
		}),
		value:'<%=(runPosition==null?"":runPosition)%>',
		width:150,
		name:'fld_runPosition',
		displayField:'text',
		fieldLabel: '运行时刻',
		valueField:'value',
		typeAhead: false,
		mode: 'local',
		triggerAction: 'all',
		emptyText:'请选择...',
		forceSelection:true,
		selectOnFocus:true,
		allowBlank: false
	});
	
	//type和runPosition是必须的，其他的根据不同的规则不同
	var Plant = Ext.data.Record.create([
	   {name: 'type', type: 'string'},
	   {name: 'runPosition', type: 'string'},
	   {name: 'referToWFOid', type: 'string'},
	   {name: 'referToUserField', type: 'string'},
	   {name: 'referFromTaskOidField', type: 'string'}
	]);
	
	ruleWindow = new Ext.Window({
      title: '规则配置-短信定义',
      width: 300,
      autoHeight: true,
      minWidth: 300,
      layout: 'form',
      plain:true,
      buttonAlign:'center',
      bodyStyle:'padding:5px;',
      modal:true,
      items: [fld_referToWF,fld_referToUser,fld_referFromTaskOidField,fld_runPosition],
      buttons: [{
          text: '确定',
          handler: function() {
			var p = new Plant({
				type:'<%=NewTaskRule.class.getName()%>',
				referToWFOid: fld_referToWF.getValue(),
				referToUserField: fld_referToUser.getValue(),
				referFromTaskOidField: fld_referFromTaskOidField.getValue(),
				runPosition: fld_runPosition.getValue()
			});
			addRuleRecord(p);
			ruleWindow.close();
          }
      },{
          text: '取消',
          handler: function() {
          	ruleWindow.close();
          }
      }]
  });
  return ruleWindow;
})();
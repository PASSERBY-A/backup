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
		fieldLabel: '��������',
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
		fieldLabel: '�������ֶ�',
		value:'<%=(referToUserField==null?"":referToUserField)%>',
		width:150,
		allowBlank: false
	});

	var fld_referFromTaskOidField = new Ext.form.TextField({
		name: 'fld_referFromTaskOidField',
		fieldLabel: '��ԭ����OID�ֶ�',
		value:'<%=(referFromTaskOidField==null?"":referFromTaskOidField)%>',
		width:150,
		allowBlank: false
	});

	var fld_runPosition = new Ext.form.ComboBox({
		store: new Ext.data.SimpleStore({
			fields: ['value', 'text'],
			data : [
				['<%=AbstractRule.RULE_RUN_POSITION_ENTER%>','ҳ���ʱ(onEnter)'],
				['<%=AbstractRule.RULE_RUN_POSITION_UPDATE%>','�����ύʱ(onUpdate)'],
				['<%=AbstractRule.RULE_RUN_POSITION_UPDATE_END%>','�ύ���ʱ(onUpdateEnd)'],
				['<%=AbstractRule.RULE_RUN_POSITION_OVERTIME%>','������ʱʱ(onOvertime)'],
				['<%=AbstractRule.RULE_RUN_POSITION_ROLLBACK%>','��������ʱ(onRollback)']
			]
		}),
		value:'<%=(runPosition==null?"":runPosition)%>',
		width:150,
		name:'fld_runPosition',
		displayField:'text',
		fieldLabel: '����ʱ��',
		valueField:'value',
		typeAhead: false,
		mode: 'local',
		triggerAction: 'all',
		emptyText:'��ѡ��...',
		forceSelection:true,
		selectOnFocus:true,
		allowBlank: false
	});
	
	//type��runPosition�Ǳ���ģ������ĸ��ݲ�ͬ�Ĺ���ͬ
	var Plant = Ext.data.Record.create([
	   {name: 'type', type: 'string'},
	   {name: 'runPosition', type: 'string'},
	   {name: 'referToWFOid', type: 'string'},
	   {name: 'referToUserField', type: 'string'},
	   {name: 'referFromTaskOidField', type: 'string'}
	]);
	
	ruleWindow = new Ext.Window({
      title: '��������-���Ŷ���',
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
          text: 'ȷ��',
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
          text: 'ȡ��',
          handler: function() {
          	ruleWindow.close();
          }
      }]
  });
  return ruleWindow;
})();
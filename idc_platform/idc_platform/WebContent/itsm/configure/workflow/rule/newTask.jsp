<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.workflow.rule.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=GBK");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

String referToWFOid = request.getParameter("wfOid");
String referToUser = request.getParameter("exeUser");
String referFromTaskOidField = request.getParameter("taskOidField");
String referFromFields = request.getParameter("fields");
String conditionExp = request.getParameter("conditionExp");
String doWait = request.getParameter("wait");
String waitStatus = request.getParameter("waitStatus");
String runPosition = request.getParameter("runPosition");

%>
(function() {
	var ruleWindow = null;
	if (ruleWindow)
		ruleWindow.close();
	// create the editor grid

	var store = new Ext.data.Store({
		proxy: new Ext.data.HttpProxy({
			url: '<%=Consts.ITSM_HOME%>/configure/workflow/workflowQuery.jsp'
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
		fieldLabel: '����Oid',
		valueField:'id',
		mode: 'remote',
		maxHeight: 150,
		width:150,
		triggerAction: 'all',
		editable:false,
		forceSelection:true,
		selectOnFocus:true,
		allowBlank: false,
		listeners:{
			change : function(f,nv,ov){
				wfStatusDs.baseParams.wfOid=nv;
				wfStatusDs.reload();
			}
		}
	});

	var fld_referToUser = new Ext.form.TextField({
		name: 'fld_referToUser',
		fieldLabel: '������',
		value:'<%=(referToUser==null?"":referToUser)%>',
		width:150,
		allowBlank: false
	});

	var fld_referFromTaskOidField = new Ext.form.TextField({
		name: 'fld_referFromTaskOidField',
		fieldLabel: '��ԭ����OID�ֶ�',
		value:'<%=(referFromTaskOidField==null?"master_task_oid":referFromTaskOidField)%>',
		width:150,
		allowBlank: false
	});
	
	var fld_referFromFields = new Ext.form.TextField({
		name: 'fld_referFromFields',
		fieldLabel: '���ݵ��ֶ�',
		value:'<%=(referFromFields==null?"":referFromFields)%>',
		width:150,
		allowBlank: true
	});

	var fld_conditionExp = new Ext.form.TextField({
		name: 'fld_conditionExp',
		fieldLabel: '�������ʽ',
		value:'<%=(conditionExp==null?"":conditionExp)%>',
		width:150,
		allowBlank: true
	});
	
	var fld_doWait = new Ext.form.Checkbox({
		name: 'fld_doWait',
		fieldLabel : '�Ƿ�ȴ�',
		checked:<%=doWait%>
	});
	
	var wfStatusDs = new Ext.data.Store({
	    proxy: new Ext.data.HttpProxy({
			url: '<%=Consts.ITSM_HOME%>/configure/workflow/workflowStatusQuery.jsp'
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
	var fld_waitStatus = new Ext.form.ComboBox({
		fieldLabel: '�ȴ���״̬',
		hiddenName:'fld_waitStatus',
		store: wfStatusDs,
		value:'<%=(waitStatus==null?"":waitStatus)%>',
		width:150,
		triggerAction: 'all',
		editable:false,
		valueField: 'caption',
		displayField:'caption',
		allowBlank:true
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
		mode: 'local',
		editable:false,
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
	   {name: 'wfOid', type: 'string'},
	   {name: 'exeUser', type: 'string'},
	   {name: 'taskOidField', type: 'string'},
	   {name: 'fields', type: 'string'},
	   {name: 'conditionExp', type: 'string'},
	   {name: 'wait', type: 'string'},
	   {name: 'waitStatus', type: 'string'}
	]);
	var descText = '<b>˵����</b>(��ϸ�μ������ֲ�)<br>'+
		'���������ʽ���˹�����жϱ��ʽ��true�½��������½�(�μ������ֲ�ı��ʽ���ò���)<br>'+
		'�������ˡ��¹����Ĵ����ˣ����ŷָ������֧��getFV("aaa")��ʽ�ĺ�;<br>'+
		'����ԭ����OID�ֶΡ��ǰѴ������Ĺ�����ԭ������OIDЯ�������Ĺ������ֶ�ID<br>'+
		'�����ݵ��ֶΡ�ԭ������Ҫ�����¹������ֶΣ����ŷָ����<br>'+
		'���Ƿ�ȴ���ԭ�����Ƿ�ȴ��¹���������ż���ִ��<br>'+
		'���ȴ���״̬���ȴ�ʱ���ж��¹��������ı�־'
	var desc = new Ext.form.Label({
		html:descText
	});
	
	ruleWindow = new Ext.Window({
      title: '��������-�����¹���',
      width: 600,
      autoHeight: true,
      minWidth: 300,
      layout: 'form',
      plain:true,
      buttonAlign:'center',
      bodyStyle:'padding:5px;',
      modal:true,
      items: [{
		layout:'column',
		bodyStyle:'background-color:transparent;',
		border: false,
		items:[{
			columnWidth: .5,
			layout: 'form',
			bodyStyle:'background-color:transparent;',
			border:false,
			items:[fld_conditionExp,fld_referToWF,fld_referToUser,fld_referFromTaskOidField]
		},{
			columnWidth: .5,
			layout: 'form',
			bodyStyle:'background-color:transparent;',
			border:false,
			items:[fld_referFromFields,fld_doWait,fld_waitStatus,fld_runPosition]
		}]
	},desc],
      buttons: [{
          text: 'ȷ��',
          handler: function() {
			var p = new Plant({
				type:'<%=NewTaskRule.class.getName()%>',
				wfOid: fld_referToWF.getValue(),
				exeUser: fld_referToUser.getValue(),
				taskOidField: fld_referFromTaskOidField.getValue(),
				fields: fld_referFromFields.getValue(),
				conditionExp: fld_conditionExp.getValue(),
				wait: fld_doWait.getValue(),
				waitStatus: fld_waitStatus.getValue(),
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
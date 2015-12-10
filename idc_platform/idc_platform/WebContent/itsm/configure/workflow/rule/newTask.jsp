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
		fieldLabel: '流程Oid',
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
		fieldLabel: '处理人',
		value:'<%=(referToUser==null?"":referToUser)%>',
		width:150,
		allowBlank: false
	});

	var fld_referFromTaskOidField = new Ext.form.TextField({
		name: 'fld_referFromTaskOidField',
		fieldLabel: '存原工单OID字段',
		value:'<%=(referFromTaskOidField==null?"master_task_oid":referFromTaskOidField)%>',
		width:150,
		allowBlank: false
	});
	
	var fld_referFromFields = new Ext.form.TextField({
		name: 'fld_referFromFields',
		fieldLabel: '传递的字段',
		value:'<%=(referFromFields==null?"":referFromFields)%>',
		width:150,
		allowBlank: true
	});

	var fld_conditionExp = new Ext.form.TextField({
		name: 'fld_conditionExp',
		fieldLabel: '条件表达式',
		value:'<%=(conditionExp==null?"":conditionExp)%>',
		width:150,
		allowBlank: true
	});
	
	var fld_doWait = new Ext.form.Checkbox({
		name: 'fld_doWait',
		fieldLabel : '是否等待',
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
		fieldLabel: '等待的状态',
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
		mode: 'local',
		editable:false,
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
	   {name: 'wfOid', type: 'string'},
	   {name: 'exeUser', type: 'string'},
	   {name: 'taskOidField', type: 'string'},
	   {name: 'fields', type: 'string'},
	   {name: 'conditionExp', type: 'string'},
	   {name: 'wait', type: 'string'},
	   {name: 'waitStatus', type: 'string'}
	]);
	var descText = '<b>说明：</b>(详细参见配置手册)<br>'+
		'【条件表达式】此规则的判断表达式，true新建，否则不新建(参见配置手册的表达式配置部分)<br>'+
		'【处理人】新工单的处理人，逗号分隔多个，支持getFV("aaa")格式的宏;<br>'+
		'【存原工单OID字段】是把触发查阅工单的原工单的OID携带到查阅工单的字段ID<br>'+
		'【传递的字段】原工单需要传到新工单的字段，逗号分隔多个<br>'+
		'【是否等待】原工单是否等待新工单结束后才继续执行<br>'+
		'【等待的状态】等待时，判断新工单结束的标志'
	var desc = new Ext.form.Label({
		html:descText
	});
	
	ruleWindow = new Ext.Window({
      title: '规则配置-触发新工单',
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
          text: '确定',
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
          text: '取消',
          handler: function() {
          	ruleWindow.close();
          }
      }]
  });
  return ruleWindow;
})();
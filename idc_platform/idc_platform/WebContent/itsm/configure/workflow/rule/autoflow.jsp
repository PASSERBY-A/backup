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

String autoflowToNode = request.getParameter("autoflowToNode");
String autoflowToUser = request.getParameter("autoflowToUser");
String autoflowType = request.getParameter("autoflowType");
String conditionExp = request.getParameter("conditionExp");
String runPosition = request.getParameter("runPosition");

%>
(function() {
	var ruleWindow = null;
	if (ruleWindow)
		ruleWindow.close();

	var store0 = new Ext.data.SimpleStore({
		fields: ['value', 'text'],
		data : nodeArray
	});
	var fld_autoflowToNode = new Ext.form.ComboBox({
		store: store0,
		name:'fld_autoflowToNode',
		width:150,
		displayField:'text',
		fieldLabel: 'Ŀ��ڵ�',
		valueField:'value',
		value:'<%=(autoflowToNode==null?"":autoflowToNode)%>',
		typeAhead: false,
		mode: 'local',
		triggerAction: 'all',
		emptyText:'��ѡ��...',
		forceSelection:true,
		selectOnFocus:true,
		allowBlank: false
	});

	var fld_autoflowToUser  = new Ext.form.TextField({
        fieldLabel: 'ִ����',
        name: 'fld_autoflowToUser',
		width:150,
		value:'<%=(autoflowToNode==null?"":autoflowToNode)%>',
 		allowBlank: false
    });
    
    var fld_autoflowType = new Ext.form.ComboBox({
		store: new Ext.data.SimpleStore({
			fields: ['value', 'text'],
			data : [
				['<%=AutoflowRule.AUTOFLOW_TYPE_UNCONDITIONAL %>','��������ת'],
				['<%=AutoflowRule.AUTOFLOW_TYPE_CONDITIONAL %>','��������']
			]
		}),
		name:'fld_autoflowType',
		width:150,
		displayField:'text',
		fieldLabel: '��ת����',
		valueField:'value',
		value:'<%=(autoflowType==null?"":autoflowType)%>',
		typeAhead: false,
		mode: 'local',
		triggerAction: 'all',
		emptyText:'��ѡ��...',
		forceSelection:true,
		selectOnFocus:true,
		allowBlank: false,
		listeners:{
			select: function(combo,record,index){
				if (record.get("value") == <%=AutoflowRule.AUTOFLOW_TYPE_UNCONDITIONAL %>)
					fld_conditionExp.allowBlank = true;
				if (record.get("value") == <%=AutoflowRule.AUTOFLOW_TYPE_CONDITIONAL %>)
					fld_conditionExp.allowBlank = false;
			}
		}
	});

	var fld_conditionExp  = new Ext.form.TextField({
        fieldLabel: '��ת����',
        name: 'fld_conditionExp',
		width:150,
 		value:'<%=(conditionExp==null?"":conditionExp)%>'
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
	   {name: 'autoflowToNode', type: 'string'},
	   {name: 'autoflowToUser', type: 'string'},
	   {name: 'autoflowType', type: 'string'},
	   {name: 'conditionExp', type: 'string'},
	   {name: 'runPosition', type: 'string'}
	]);
	
	var desc = new Ext.form.Label({
		html:'<b>˵����</b>��ִ���ˡ�֧��ֱ����д��ԱID��Ҳ֧�ֶ�̬��ȡ��ʷ�ֶ�ֵ��getFV("��Ա�ֶ�ID")�������ŷָ����;����ת������Ϊ�򵥵ıȽϱ��ʽ������д���μ������ֲᣩ'
	});
	
	ruleWindow = new Ext.Window({
      title: '��������-�Զ���ת',
      width: 300,
      autoHeight: true,
      minWidth: 300,
      layout: 'form',
      plain:true,
      buttonAlign:'center',
      bodyStyle:'padding:5px;',
      modal:true,
      items: [fld_autoflowToNode,fld_autoflowToUser,fld_autoflowType,fld_conditionExp,fld_runPosition,desc],
      buttons: [{
          text: 'ȷ��',
          handler: function() {
			var p = new Plant({
				type:'<%=AutoflowRule.class.getName()%>',
				autoflowToNode: fld_autoflowToNode.getValue(),
				autoflowToUser: fld_autoflowToUser.getValue(),
				autoflowType: fld_autoflowType.getValue(),
				conditionExp: fld_conditionExp.getValue(),
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
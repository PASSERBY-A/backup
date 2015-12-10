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
		fieldLabel: '目标节点',
		valueField:'value',
		value:'<%=(autoflowToNode==null?"":autoflowToNode)%>',
		typeAhead: false,
		mode: 'local',
		triggerAction: 'all',
		emptyText:'请选择...',
		forceSelection:true,
		selectOnFocus:true,
		allowBlank: false
	});

	var fld_autoflowToUser  = new Ext.form.TextField({
        fieldLabel: '执行人',
        name: 'fld_autoflowToUser',
		width:150,
		value:'<%=(autoflowToNode==null?"":autoflowToNode)%>',
 		allowBlank: false
    });
    
    var fld_autoflowType = new Ext.form.ComboBox({
		store: new Ext.data.SimpleStore({
			fields: ['value', 'text'],
			data : [
				['<%=AutoflowRule.AUTOFLOW_TYPE_UNCONDITIONAL %>','无条件流转'],
				['<%=AutoflowRule.AUTOFLOW_TYPE_CONDITIONAL %>','有条件的']
			]
		}),
		name:'fld_autoflowType',
		width:150,
		displayField:'text',
		fieldLabel: '流转类型',
		valueField:'value',
		value:'<%=(autoflowType==null?"":autoflowType)%>',
		typeAhead: false,
		mode: 'local',
		triggerAction: 'all',
		emptyText:'请选择...',
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
        fieldLabel: '流转条件',
        name: 'fld_conditionExp',
		width:150,
 		value:'<%=(conditionExp==null?"":conditionExp)%>'
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
	   {name: 'autoflowToNode', type: 'string'},
	   {name: 'autoflowToUser', type: 'string'},
	   {name: 'autoflowType', type: 'string'},
	   {name: 'conditionExp', type: 'string'},
	   {name: 'runPosition', type: 'string'}
	]);
	
	var desc = new Ext.form.Label({
		html:'<b>说明：</b>【执行人】支持直接填写人员ID，也支持动态获取历史字段值“getFV("人员字段ID")”，逗号分隔多个;【流转条件】为简单的比较表达式（具体写法参见配置手册）'
	});
	
	ruleWindow = new Ext.Window({
      title: '规则配置-自动流转',
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
          text: '确定',
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
          text: '取消',
          handler: function() {
          	ruleWindow.close();
          }
      }]
  });
  return ruleWindow;
})();
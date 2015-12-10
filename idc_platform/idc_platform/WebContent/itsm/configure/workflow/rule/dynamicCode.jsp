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

String runEnter = request.getParameter("runEnter");
String runUpdate = request.getParameter("runUpdate");
String runUpdateEnd = request.getParameter("runUpdateEnd");
String runRollback = request.getParameter("runRollback");
String runOvertime = request.getParameter("runOvertime");
String runRealtime = request.getParameter("runRealtime");

String runPosition = request.getParameter("runPosition");

%>
(function() {
	var ruleWindow = null;
	if (ruleWindow)
		ruleWindow.close();

	var fld_onEnter = new Ext.form.Checkbox({
		name: 'fld_onEnter',
		fieldLabel : 'onEnter',
		checked:<%=runEnter%>
	});
	var fld_onUpdate = new Ext.form.Checkbox({
		name: 'fld_onUpdate',
		fieldLabel : 'onUpdate',
		checked:<%=runUpdate%>
	});
	var fld_onUpdateEnd = new Ext.form.Checkbox({
		name: 'fld_onUpdateEnd',
		fieldLabel : 'onUpdateEnd',
		checked:<%=runUpdateEnd%>
	});
	var fld_onRollback = new Ext.form.Checkbox({
		name: 'fld_onRollback',
		fieldLabel : 'onRollback',
		checked:<%=runRollback%>
	});
	var fld_onOvertime = new Ext.form.Checkbox({
		name: 'fld_onOvertime',
		fieldLabel : 'onOvertime',
		checked:<%=runOvertime%>
	});
	var fld_onRealtime = new Ext.form.Checkbox({
		name: 'fld_onRealtime',
		fieldLabel : 'onRealtime',
		checked:<%=runRealtime%>
	});

	//type和runPosition是必须的，其他的根据不同的规则不同
	var Plant = Ext.data.Record.create([
	   {name: 'type', type: 'string'},
	   {name: 'runEnter', type: 'string'},
	   {name: 'runUpdate', type: 'string'},
	   {name: 'runUpdateEnd', type: 'string'},
	   {name: 'runRollback', type: 'string'},
	   {name: 'runOvertime', type: 'string'},
	   {name: 'runRealtime', type: 'string'}
	]);
	
	var desc = new Ext.form.Label({
		html:'<b>说明：</b>选中代表流程流转过程中会执行此方法'
	});
	
	ruleWindow = new Ext.Window({
      title: '规则配置-动态代码',
      width: 300,
      autoHeight: true,
      minWidth: 300,
      layout: 'form',
      plain:true,
      buttonAlign:'center',
      bodyStyle:'padding:5px;',
      labelAlign:'right',
      modal:true,
      items: [fld_onEnter,fld_onUpdate,fld_onUpdateEnd,fld_onRollback,fld_onOvertime,fld_onRealtime,desc],
      buttons: [{
          text: '确定',
          handler: function() {
			var p = new Plant({
				type:'<%=DynamicCodeRule.class.getName()%>',
				runEnter: fld_onEnter.getValue(),
				runUpdate: fld_onUpdate.getValue(),
				runUpdateEnd: fld_onUpdateEnd.getValue(),
				runRollback: fld_onRollback.getValue(),
				runOvertime: fld_onOvertime.getValue(),
				runRealtime: fld_onRealtime.getValue()
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
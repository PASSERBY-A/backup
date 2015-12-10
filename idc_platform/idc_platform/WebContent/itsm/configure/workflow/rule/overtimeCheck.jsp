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

String overtimeCheckFields = request.getParameter("overtimeCheckFields");
String smsRemindDuration = request.getParameter("smsRemindDuration");
String runPosition = request.getParameter("runPosition");

%>
(function() {
	var ruleWindow = null;
	if (ruleWindow)
		ruleWindow.close();

	var fld_overtimeCheckFields = new Ext.form.TextField({
		name: 'fld_overtimeCheckFields',
		fieldLabel: '超时检测字段',
		value:'<%=(overtimeCheckFields==null?"":overtimeCheckFields)%>',
		width:150,
		allowBlank: true
	});

	var fld_smsRemindDuration = new Ext.form.TextField({
		name: 'fld_smsRemindDuration',
		fieldLabel: '短信提醒间隔',
		value:'<%=(smsRemindDuration==null?"0":smsRemindDuration)%>',
		width:150,
		allowBlank: false
	});

	//type和runPosition是必须的，其他的根据不同的规则不同
	var Plant = Ext.data.Record.create([
	   {name: 'type', type: 'string'},
	   {name: 'overtimeCheckFields', type: 'string'},
	   {name: 'smsRemindDuration', type: 'string'}
	]);
	
	var desc = new Ext.form.Label({
		html:'<b>说明：</b>【超时检测字段】是本节点前提交表单里的时间字段ID，逗号分隔多个;【短信提醒间隔】是工单的循环提醒间隔，0或负数不提醒，单位分钟'
	});
	
	ruleWindow = new Ext.Window({
      title: '规则配置-超时检测',
      width: 300,
      autoHeight: true,
      minWidth: 300,
      layout: 'form',
      plain:true,
      buttonAlign:'center',
      bodyStyle:'padding:5px;',
      modal:true,
      items: [fld_overtimeCheckFields,fld_smsRemindDuration,desc],
      buttons: [{
          text: '确定',
          handler: function() {
			var p = new Plant({
				type:'<%=OvertimeCheckRule.class.getName()%>',
				overtimeCheckFields: fld_overtimeCheckFields.getValue(),
				smsRemindDuration: fld_smsRemindDuration.getValue()
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
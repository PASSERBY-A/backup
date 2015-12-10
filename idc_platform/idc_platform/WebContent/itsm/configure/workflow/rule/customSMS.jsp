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

String smsContent = request.getParameter("smsContent");
String smsReceiver = request.getParameter("smsReceiver");
String runPosition = request.getParameter("runPosition");

%>
(function() {
	var ruleWindow = null;
	if (ruleWindow)
		ruleWindow.close();

	var fld_smsReceiver = new Ext.form.TextField({
		name: 'fld_smsReceiver',
		fieldLabel: '接受人',
		value:'<%=(smsReceiver==null?"":smsReceiver)%>',
		width:150,
		allowBlank: true
	});

	var fld_smsContent = new Ext.form.TextArea({
		name: 'fld_smsContent',
		fieldLabel: '短信内容',
		value:'<%=(smsContent==null?"":smsContent)%>',
		width:150,
		allowBlank: false
	});

	//type和runPosition是必须的，其他的根据不同的规则不同
	var Plant = Ext.data.Record.create([
	   {name: 'type', type: 'string'},
	   {name: 'runPosition', type: 'string'},
	   {name: 'smsReceiver', type: 'string'},
	   {name: 'smsContent', type: 'string'}
	]);
	
	var desc = new Ext.form.Label({
		html:'<b>说明：</b>【短信内容】支持getFV("字段ID");<br>【接受人】支持getFV("字段ID"),逗号“,”分隔多个'
	});
	
	ruleWindow = new Ext.Window({
      title: '规则配置-自定义短信',
      width: 300,
      autoHeight: true,
      minWidth: 300,
      layout: 'form',
      plain:true,
      buttonAlign:'center',
      bodyStyle:'padding:5px;',
      modal:true,
      items: [fld_smsReceiver,fld_smsContent,desc],
      buttons: [{
          text: '确定',
          handler: function() {
			var p = new Plant({
				type:'<%=CustomSMSRule.class.getName()%>',
				runPosition:'<%=AbstractRule.RULE_RUN_POSITION_UPDATE_END %>',
				smsReceiver: fld_smsReceiver.getValue(),
				smsContent: fld_smsContent.getValue()
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
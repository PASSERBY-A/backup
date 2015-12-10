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
		fieldLabel: '������',
		value:'<%=(smsReceiver==null?"":smsReceiver)%>',
		width:150,
		allowBlank: true
	});

	var fld_smsContent = new Ext.form.TextArea({
		name: 'fld_smsContent',
		fieldLabel: '��������',
		value:'<%=(smsContent==null?"":smsContent)%>',
		width:150,
		allowBlank: false
	});

	//type��runPosition�Ǳ���ģ������ĸ��ݲ�ͬ�Ĺ���ͬ
	var Plant = Ext.data.Record.create([
	   {name: 'type', type: 'string'},
	   {name: 'runPosition', type: 'string'},
	   {name: 'smsReceiver', type: 'string'},
	   {name: 'smsContent', type: 'string'}
	]);
	
	var desc = new Ext.form.Label({
		html:'<b>˵����</b>���������ݡ�֧��getFV("�ֶ�ID");<br>�������ˡ�֧��getFV("�ֶ�ID"),���š�,���ָ����'
	});
	
	ruleWindow = new Ext.Window({
      title: '��������-�Զ������',
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
          text: 'ȷ��',
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
          text: 'ȡ��',
          handler: function() {
          	ruleWindow.close();
          }
      }]
  });
  return ruleWindow;
})();
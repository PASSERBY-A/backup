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
		fieldLabel: '��ʱ����ֶ�',
		value:'<%=(overtimeCheckFields==null?"":overtimeCheckFields)%>',
		width:150,
		allowBlank: true
	});

	var fld_smsRemindDuration = new Ext.form.TextField({
		name: 'fld_smsRemindDuration',
		fieldLabel: '�������Ѽ��',
		value:'<%=(smsRemindDuration==null?"0":smsRemindDuration)%>',
		width:150,
		allowBlank: false
	});

	//type��runPosition�Ǳ���ģ������ĸ��ݲ�ͬ�Ĺ���ͬ
	var Plant = Ext.data.Record.create([
	   {name: 'type', type: 'string'},
	   {name: 'overtimeCheckFields', type: 'string'},
	   {name: 'smsRemindDuration', type: 'string'}
	]);
	
	var desc = new Ext.form.Label({
		html:'<b>˵����</b>����ʱ����ֶΡ��Ǳ��ڵ�ǰ�ύ�����ʱ���ֶ�ID�����ŷָ����;���������Ѽ�����ǹ�����ѭ�����Ѽ����0���������ѣ���λ����'
	});
	
	ruleWindow = new Ext.Window({
      title: '��������-��ʱ���',
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
          text: 'ȷ��',
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
          text: 'ȡ��',
          handler: function() {
          	ruleWindow.close();
          }
      }]
  });
  return ruleWindow;
})();
<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.message.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ include file="../getUser.jsp"%>

<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
%>

<script>
	<%
		String sendTO = request.getParameter("sendTO");
		String wfOid = request.getParameter("wfOid");
		String taskOid = request.getParameter("taskOid");
		String taskTitle = request.getParameter("taskTitle");
		String taskStatus = request.getParameter("taskStatus");

	%>
	
	var fld_notice_to = new Ext.form.SelectDialogField({fieldLabel:"通知人",hiddenName :'fld_notice_to',selectUrl:'../configure/userSelectDialog.jsp',forceSelection:true,params: {groupType:1,origin:'ITSM',wfOid:'-1'},emptyText:'请选择...',desc:'',width:150});
	var fld_notice_message = new Ext.form.TextArea({fieldLabel:"通知内容",name:'fld_notice_message',height:80,desc:'',width:150});

	fld_notice_to.anchor='-20';
	fld_notice_message.anchor = '-20';
	var smsSendForm = new Ext.form.FormPanel({
    region:'center',
    baseCls: 'x-plain',
    border: false,
    labelWidth: 55,
    defaultType: 'textfield',
    items: [fld_notice_to,fld_notice_message],
    errorReader: new Ext.form.XmlErrorReader(),
    url:'<%=Consts.ITSM_HOME%>/task/taskPost.jsp',
    baseParams: {
			operType: "sendSms",wfOid:"<%=wfOid%>"
		}
	});


	var formWindow = null;
	if (formWindow) {
		formWindow.close();
	}

	formWindow = new Ext.Window({
      title: '短信提醒',
      width: 300,
      height:200,
      minWidth: 300,
      minHeight: 200,
      layout: 'border',
      plain:true,
      iconCls :'edit',
      buttonAlign:'center',
      modal:true,
      items: [smsSendForm],
      buttons: [{
          text: '发送',
          handler: function() {
          	var smsForm = formWindow.smsForm.form;
          	if (!smsForm.isValid()) {
							Ext.MessageBox.alert("提示", "请填写完整的内容");
							return;
						}

						smsForm.submit({
							waitMsg: '<%=Consts.MSG_WAIT%>',
							success: function(form, action)
							{
								Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text);
								formWindow.hide();
							},
							failure: function(form, action)
							{
								form.items.each(function(f){
								   if (f.old_status_disabled)
									   f.disable();
								});
								Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_FAILED%>", form.errorReader.xmlData.documentElement.text);
					    }
					});
          }
      },{
          text: '关闭',
          handler: function() {
            formWindow.hide();
          }
      }]
  });

	formWindow.smsForm = smsSendForm;
 	formWindow.show();
  <%
  	if (sendTO!=null && !sendTO.equals("")) {
  			PersonFieldInfo pfInfo = new PersonFieldInfo();
  			pfInfo.setId("notice_to");
  			pfInfo.setValue(sendTO);
			out.print(pfInfo.getFormValue());
		}

		String operName_zh = userId;
		PersonInfo person = (PersonInfo)PersonManager.getPersonById(userId);
		if (person!=null)
			operName_zh = person.getName();
		String messageContent = MessageManager.getInjectedMessage(
				Consts.SMS_TEMPLATE.getSmsRemind(), new String[] { operName_zh,
								taskOid,taskTitle, taskStatus },null);
  %>
  fld_notice_message.setValue("<%=messageContent%>");
</script>
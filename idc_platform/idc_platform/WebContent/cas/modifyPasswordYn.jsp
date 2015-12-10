<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getUserId.jsp"%>

<html>
<head>
  <title>�޸ĸ�������</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<link rel="stylesheet" type="text/css" href="<%=EXTJS_HOME%>/resources/css/ext-all.css" />
	<script type="text/javascript" src="<%=EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-ext.js"></script>

	<script type="text/javascript" src="cas_auc.js"></script>
</head>
<body>
<%
String personId = request.getParameter("id");
	personId = personId==null?"":personId;
 	PersonInfo pInfo = PersonManager.getPersonById(personId);
 	
%>
<script>
var modifyPassword = new Ext.form.FormPanel({
		title:'��������',
		id: 'modifypassword',
		method: 'POST',
		autoScroll: true,
		margins:'20 0 0 70',
    defaultType: 'textfield',
    baseParams:{'postType':'user','subType':'changePassword'},
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:'casAucPost.jsp',
		items:[{
        fieldLabel: 'ID',
        name: 'id',
        readOnly:true,
        allowBlank:false
    },{
        fieldLabel: 'ԭ����',
        name: 'oldPassword',
        inputType:'password',
        allowBlank:false
    },{
        fieldLabel: '������',
        name: 'newPassword',
        inputType:'password',
        allowBlank:false
    },{
        fieldLabel: '�ظ�������',
        inputType:'password',
        name: 'newPassword_confirm',
        allowBlank:false
    }
    ],
     buttons: [{
      text: 'ȷ��',
      handler: function() {
				var form = modifyPassword.form;
				if (!form.isValid()) {
					Ext.MessageBox.alert("��ʾ", "��Ϣ����д����");
					return;
				}
				if (form.findField("newPassword").getValue()!=form.findField("newPassword_confirm").getValue()){
					Ext.MessageBox.alert("��ʾ", "ȷ�����벻ͬ");
					return;
				}
				form.submit({
					waitMsg: '���ڴ���....',
					success: function(form, action)
					{
						Ext.MessageBox.alert("����ɹ�", form.errorReader.xmlData.documentElement.text);
					},
					failure: function(form, action)
					{
						Ext.MessageBox.alert("����ʧ��", form.errorReader.xmlData.documentElement.text);
					}
				});
      }
  },{
      text: 'ȡ��',
      handler: function() {
      	window.close();
      }
  }]
    
});

var flag = false;

<% if(!PersonManager.isSystemManager(userId)){ %>
	flag = true;
<% } %>

var modifyPersonInfo = new Ext.form.FormPanel({
		title:'�޸Ļ�������',
		id: 'modifypersoninfo',
		method: 'POST',
		autoScroll: true,
		margins:'20 0 0 70',
    defaultType: 'textfield',
    baseParams:{'postType':'user','id':'<%=personId%>','subType':'modify','p_status':'<%=pInfo.getPersonStatus()%>','status':'<%=pInfo.getStatus()%>'},
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:'casAucPost.jsp',
		items:[{
        fieldLabel: 'ID',
        name: 'id',
        readOnly:true,
        value:'<%=personId%>',
        disabled: flag,
        allowBlank:false
    },{
        fieldLabel: '����',
        name: 'name',
        value:'<%=pInfo.getName()%>',
        disabled: flag,
        allowBlank:false
    },{
        fieldLabel: '�ֻ���',
        name: 'mobile',
        value:'<%=pInfo.getMobile()%>',
        disabled: flag,
        allowBlank:true
    },{
        fieldLabel: 'E-Mail',
        name: 'email',
        value:'<%=pInfo.getEmail()%>',
        disabled: flag,
        allowBlank:true
    }
    ],

 <% if(!PersonManager.isSystemManager(userId)){ %>
	html: '<br><b><font color=red>��Ҫ�޸�����, ����ϵͳ����Ա��ϵ!</font></b>',
<% } %>
    
     buttons: [{
      text: 'ȷ��',
      disabled: flag,
      handler: function() {
				var form = modifyPersonInfo.form;
				if (!form.isValid()) {
					Ext.MessageBox.alert("��ʾ", "��Ϣ����д����");
					return;
				}
				form.submit({
					waitMsg: '���ڴ���....',
					success: function(form, action)
					{
						Ext.MessageBox.alert("����ɹ�", form.errorReader.xmlData.documentElement.text);
					},
					failure: function(form, action)
					{
						Ext.MessageBox.alert("����ʧ��", form.errorReader.xmlData.documentElement.text);
					}
				});
      }
  },{
      text: 'ȡ��',
      handler: function() {
      	window.close();
      }
  }]
});


var tabPanel = new Ext.TabPanel({
	region:'center',
	id:'center-panel',
  	activeTab: 0,
  	border:false,
	items:[modifyPassword,modifyPersonInfo]
});
Ext.onReady(function(){
	new Ext.Viewport({
		layout: 'border',
		items: [tabPanel]
  });

modifyPassword.form.findField("id").setValue("<%=personId%>");


});

</script>

</body>
</html>
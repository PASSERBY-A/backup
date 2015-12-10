<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getPurview.jsp"%>

<html>
<head>
  <title>�޸ĸ�������</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
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
Ext.apply(Ext.form.VTypes,{
    password:function(val,field){//valָ������ı���ֵ��fieldָ����ı�����������Ҫ���������˼
       if(field.confirmTo){//confirmTo�������Զ�������ò�����һ��������������������idֵ
           var pwd=Ext.get(field.confirmTo);//ȡ��confirmTo���Ǹ�id��ֵ
           return (val==pwd.getValue());
       }
       return true;
    }
});

var modifyPassword = new Ext.form.FormPanel({
		title:'�޸ĸ�������',
		method: 'POST',
		autoScroll: true,
		margins:'20 0 0 70',
		frame:true,
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
        id:'newPassword',
        name: 'newPassword',
        inputType:'password',
        allowBlank:false
    },{
        fieldLabel: '�ظ�������',
        inputType:'password',
        name: 'newPassword_confirm',
        vtype:'password',
        vtypeText:"�������벻һ�£�",
        confirmTo:'newPassword',
        allowBlank:false
    },{
		xtype:'panel',
		id:'errorDetail'
    }
    ],
     buttons: [{
      text: 'ȷ��',
      handler: function() {
				var form = modifyPassword.form;
				if (!form.isValid()) {
					Ext.MessageBox.alert("��ʾ", "��Ϣ����д������ȷ");
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
						//Ext.MessageBox.alert("����ʧ��", form.errorReader.xmlData.documentElement.text);
						Ext.DomHelper.overwrite(document.getElementById('errorDetail'),'<div id="happenedError" style="color: red;font-size: 15;width=200">'+form.errorReader.xmlData.documentElement.text+'</div>');
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


var modifyPersonInfo = new Ext.form.FormPanel({
		title:'�޸Ļ�������',
		method: 'POST',
		autoScroll: true,
		margins:'20 0 0 70',
		frame:true,
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
        allowBlank:false
    },{
        fieldLabel: '����',
        name: 'name',
        value:'<%=pInfo.getName()%>',
        allowBlank:false
    },{
        fieldLabel: '�ֻ���',
        name: 'mobile',
        value:'<%=pInfo.getMobile()%>',
        allowBlank:true
    },{
        fieldLabel: 'E-Mail',
        name: 'email',
        value:'<%=pInfo.getEmail()%>',
        allowBlank:true
    }
    ],
     buttons: [{
      text: 'ȷ��',
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
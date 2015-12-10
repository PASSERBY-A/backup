<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getUserId.jsp"%>
<html>
<head>
  <title>�޸�����</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=EXTJS_HOME%>/resources/css/ext-all.css" />
	<script type="text/javascript" src="<%=EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-ext.js"></script>

	<script type="text/javascript" src="cas_auc.js"></script>
</head>
<body>
<script>
	Ext.apply(Ext.form.VTypes, {
  password: function(val, field) {
    if (field.initialPassField) {
      var pwd = Ext.getCmp(field.initialPassField);
      return (val == pwd.getValue());
    }
    return true;
  },

  passwordText: '���벻һ��'
});
	var userForm = new Ext.form.FormPanel({
		method: 'POST',
		region:'center',
		autoScroll: true,
		frame :true,
    defaultType: 'textfield',
    baseParams:{
    	'postType':'user',
    	'subType':'changePassword',
    	'id':'<%=userId%>'
    },
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:'casAucPost.jsp',
		defaults: {
      inputType: 'password'
    },
		items:[{
        fieldLabel: '�û�',
        name: 'id',
        inputType:'text',
        value:'<%=userId%>',
        disabled:true
    },{
        fieldLabel: 'ԭʼ����',
        name: 'oldPassword',
        allowBlank:false
    },{
        fieldLabel: '������',
        name: 'newPassword',
        id: 'pass'
    },{
	      fieldLabel: 'ȷ��������',
	      name: 'pass-cfrm',
	      vtype: 'password',
	      initialPassField: 'pass' // id of the initial password field
    }],
    buttons:[{
    	text: 'ȷ��',
    	handler: function(){
    		var form = userForm.form;
      	if (!form.isValid()) {
					Ext.MessageBox.alert("��ʾ", "����д����������");
					return;
				}

				form.submit({
						waitMsg: '���ڴ������Ժ�...',
						success: function(form, action)
						{
							Ext.MessageBox.alert("��Ϣ", form.errorReader.xmlData.documentElement.text,callBack);

						},
						failure: function(form, action)
						{
							form.items.each(function(f){
								   if (f.old_status_disabled)
									   f.disable();
								});
							Ext.MessageBox.alert("ʧ��", form.errorReader.xmlData.documentElement.text);
				    }
				});
    	}
    },{
    	text: 'ȡ��',
  	 	handler: function(){
  	 		top.window.close();
  	 	}
    }]
});

function callBack(){
	top.window.close();
}

Ext.onReady(function(){
	new Ext.Viewport({
		layout: 'border',
		items: [userForm]
	});
});
</script>
</body>
</html>
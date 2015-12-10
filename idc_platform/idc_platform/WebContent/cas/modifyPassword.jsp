<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getPurview.jsp"%>

<html>
<head>
  <title>修改个人资料</title>
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
    password:function(val,field){//val指这里的文本框值，field指这个文本框组件，大家要明白这个意思
       if(field.confirmTo){//confirmTo是我们自定义的配置参数，一般用来保存另外的组件的id值
           var pwd=Ext.get(field.confirmTo);//取得confirmTo的那个id的值
           return (val==pwd.getValue());
       }
       return true;
    }
});

var modifyPassword = new Ext.form.FormPanel({
		title:'修改个人密码',
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
        fieldLabel: '原密码',
        name: 'oldPassword',
        inputType:'password',
        allowBlank:false
    },{
        fieldLabel: '新密码',
        id:'newPassword',
        name: 'newPassword',
        inputType:'password',
        allowBlank:false
    },{
        fieldLabel: '重复新密码',
        inputType:'password',
        name: 'newPassword_confirm',
        vtype:'password',
        vtypeText:"两次密码不一致！",
        confirmTo:'newPassword',
        allowBlank:false
    },{
		xtype:'panel',
		id:'errorDetail'
    }
    ],
     buttons: [{
      text: '确定',
      handler: function() {
				var form = modifyPassword.form;
				if (!form.isValid()) {
					Ext.MessageBox.alert("提示", "信息请填写完整正确");
					return;
				}
				if (form.findField("newPassword").getValue()!=form.findField("newPassword_confirm").getValue()){
					Ext.MessageBox.alert("提示", "确认密码不同");
					return;
				}
				form.submit({
					waitMsg: '正在处理....',
					success: function(form, action)
					{
						Ext.MessageBox.alert("处理成功", form.errorReader.xmlData.documentElement.text);
					},
					failure: function(form, action)
					{
						//Ext.MessageBox.alert("处理失败", form.errorReader.xmlData.documentElement.text);
						Ext.DomHelper.overwrite(document.getElementById('errorDetail'),'<div id="happenedError" style="color: red;font-size: 15;width=200">'+form.errorReader.xmlData.documentElement.text+'</div>');
					}
				});
      }
  },{
      text: '取消',
      handler: function() {
      	window.close();
      }
  }]
    
});


var modifyPersonInfo = new Ext.form.FormPanel({
		title:'修改基本资料',
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
        fieldLabel: '名称',
        name: 'name',
        value:'<%=pInfo.getName()%>',
        allowBlank:false
    },{
        fieldLabel: '手机号',
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
      text: '确定',
      handler: function() {
				var form = modifyPersonInfo.form;
				if (!form.isValid()) {
					Ext.MessageBox.alert("提示", "信息请填写完整");
					return;
				}
				form.submit({
					waitMsg: '正在处理....',
					success: function(form, action)
					{
						Ext.MessageBox.alert("处理成功", form.errorReader.xmlData.documentElement.text);
					},
					failure: function(form, action)
					{
						Ext.MessageBox.alert("处理失败", form.errorReader.xmlData.documentElement.text);
					}
				});
      }
  },{
      text: '取消',
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
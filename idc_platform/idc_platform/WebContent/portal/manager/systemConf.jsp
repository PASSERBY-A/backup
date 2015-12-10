<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@page import="com.hp.idc.portal.mgr.SystemConfig"%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
	<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/extjs/ext-ext.js"></script>
	<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>

</head>
<body>
<div id="systemConfDiv"></div>

<script>
Ext.onReady(function(){
	var systemConf = new Ext.FormPanel({
        frame:true,
        bodyStyle:'padding:5px 5px 5px 5px ',
        width: 900,
        errorReader: new Ext.form.XmlErrorReader(),
    	url:'systemConfPost.jsp',
        items:[{
			layout:'column',
            border:false,
            items:[{
				columnWidth:1,
            	layout: 'form',
            	defaultType: 'textfield',
            	defaults: {width: 450},
            	labelWidth: 150,
            	items:[new Ext.form.SelectDialogField({
						fieldLabel:"ϵͳ����Ա",
						hiddenName :'admin',
						params: {groupType:3,singleMode:false},
						selectUrl:'/itsm/configure/security/userSelectDialog.jsp',
						editable :false,
						emptyText:'��ѡ��...'
					})
                ]
			}]
        }],
		buttons:[{
			text:'����',
			handler:function(){
				var form = systemConf.form;
				if (!form.isValid()) {
					Ext.MessageBox.alert("��ʾ", "����д����������");
					return;
				}
				form.submit({
					waitMsg: '���ڴ������Ժ�...',
					success: function(form, action) {
						Ext.MessageBox.alert("��Ϣ", form.errorReader.xmlData.documentElement.text);					
					},
					failure: function(form, action){
						Ext.MessageBox.alert("ʧ��", form.errorReader.xmlData.documentElement.text);
					}
				});
			}
		}]
    });

	systemConf.render('systemConfDiv');
	
	fld = systemConf.form.findField("admin");
	fld.setValue("<%=SystemConfig.getAttributeValue("admin").substring(1,SystemConfig.getAttributeValue("admin").length()-1)%>");
});
</script>
</body>
</html>
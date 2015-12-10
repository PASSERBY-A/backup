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
						fieldLabel:"系统管理员",
						hiddenName :'admin',
						params: {groupType:3,singleMode:false},
						selectUrl:'/itsm/configure/security/userSelectDialog.jsp',
						editable :false,
						emptyText:'请选择...'
					})
                ]
			}]
        }],
		buttons:[{
			text:'保存',
			handler:function(){
				var form = systemConf.form;
				if (!form.isValid()) {
					Ext.MessageBox.alert("提示", "请填写完整的内容");
					return;
				}
				form.submit({
					waitMsg: '正在处理，请稍候...',
					success: function(form, action) {
						Ext.MessageBox.alert("信息", form.errorReader.xmlData.documentElement.text);					
					},
					failure: function(form, action){
						Ext.MessageBox.alert("失败", form.errorReader.xmlData.documentElement.text);
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
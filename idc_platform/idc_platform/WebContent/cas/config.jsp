<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>

<%@ include file="getPurview.jsp"%>
<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=EXTJS_HOME%>/resources/css/ext-ext.css" />
	<script type="text/javascript" src="<%=EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-ext.js"></script>
</head>
<STYLE type=text/css>
BODY {
	FONT-SIZE: 12px;
	margin: 10px;
}
</style>
<body>
<%
	Map<String,String> sysConfig = SystemManager.getConfig();
%>
<div id="configForm"></div>

<script>
/**
1������10�δ��������˺�����root��������
2���������ø�λʱ�䣨��λ��Сʱ��
3�����������˺�����ʱ���Ͷ�������
4�������˺��������˺ű���ǿ���޸�������޸Ĳ��ܽ���ϵͳ
5�������˺�����ٴε�¼ ��ʹ�ɹ�Ҳ���ܽ���ϵͳ���������������
6���ṩ�������˺ŵĹ������ͬʱ�ṩ������λ����
7����ǿ��������̽������� 8λ���ֺ���ĸ����ʹ�� ���û�����㲻��ʹ��
**/
var login_repeat = new Ext.form.TextField({
    fieldLabel: '��¼���Դ���',
    name: 'login_repeat'
});
var login_locktime = new Ext.form.TextField({
    fieldLabel: '����ʱ��(Сʱ)',
    name: 'login_locktime'
});

var pass_period = new Ext.form.TextField({
    fieldLabel: '������Ч��(��)',
    name: 'pass_period'
});
var pass_regulation = new Ext.form.TextField({
    fieldLabel: '������ɹ���',
    name: 'pass_regulation'
});

var pass_regulation_desc = new Ext.form.TextArea({
    fieldLabel: '��������',
    name: 'pass_regulation_desc'
});
var pass_repeatTime = new Ext.form.TextField({
    fieldLabel: '�����ظ�����',
    name: 'pass_repeatTime',
    allowBlank:true
});
var pass_first = new Ext.form.Checkbox({
    fieldLabel: '�޸�Ĭ������',
    name: 'pass_first'
});

var systemManager = new Ext.form.SelectDialogField({
	fieldLabel:"ά����Ա",
	hiddenName :'systemManager',
	selectUrl:'userSelectDialog.jsp',
	forceSelection:true,
	params: {groupType:3,singleMode:false}
});


var loginForm = new Ext.FormPanel({
		method: 'POST',
		autoScroll: true,
    defaultType: 'textfield',
    frame:true,
    baseParams:{'operType':'update'},
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 95,
		defaults: {width: 210},
		renderTo:"configForm",
		url:'configPost.jsp',
		width: 350,
		items:[login_repeat,login_locktime,pass_period,pass_regulation,pass_regulation_desc,
			pass_repeatTime
			,pass_first
			,systemManager
		],
    buttons:[{text:'�ύ',handler:function(){
      	if (!loginForm.form.isValid()) {
					Ext.MessageBox.alert("��ʾ", "����д����������");
					return;
				}

				loginForm.form.submit({
						waitMsg: '���ڴ������Ժ�...',
						success: function(form, action)
						{
							Ext.MessageBox.alert("��Ϣ", form.errorReader.xmlData.documentElement.text);
						},
						failure: function(form, action)
						{
							Ext.MessageBox.alert("ʧ��", form.errorReader.xmlData.documentElement.text);
				    }
				});
    	}
    }]
});

<%
if (sysConfig!=null) {
%>
	loginForm.form.findField("pass_period").setValue("<%=sysConfig.get("pass_period")==null?"":sysConfig.get("pass_period")%>");
	loginForm.form.findField("pass_regulation").setValue("<%=StringUtil.escapeJavaScript(sysConfig.get("pass_regulation")==null?"":sysConfig.get("pass_regulation"))%>");
	loginForm.form.findField("pass_repeatTime").setValue("<%=sysConfig.get("pass_repeatTime")==null?"":sysConfig.get("pass_repeatTime")%>");
	loginForm.form.findField("pass_first").setValue("<%=(sysConfig.get("pass_first")==null||sysConfig.get("pass_first").equals(""))?false:true%>");
	loginForm.form.findField("login_repeat").setValue("<%=sysConfig.get("login_repeat")==null?"":sysConfig.get("login_repeat")%>");
	loginForm.form.findField("login_locktime").setValue("<%=sysConfig.get("login_locktime")==null?"":sysConfig.get("login_locktime")%>");
	loginForm.form.findField("systemManager").setValue("<%=sysConfig.get("systemManager")==null?"":sysConfig.get("systemManager")%>");
	loginForm.form.findField("pass_regulation_desc").setValue("<%=sysConfig.get("pass_regulation_desc")==null?"":sysConfig.get("pass_regulation_desc")%>");
<%
}
%>
</script>
<b>��¼���Դ���:</b>ָ���Ե�¼��������������������0��ջ��������޶�<br>
<b>����ʱ��:</b>�˻��������󣬱�������ʱ�䣬��СʱΪ��λ��0��ջ��������޶�<br>
<b>������Ч��:</b>ָ�����ñ������һ�Σ���Ϊ��λ��0��ջ��������޶�<br>
<b>������ɹ���:</b>������ʽ����:8λ������ɣ�/^(0-9){8}$/���գ����޶�<br>
<b>�����ظ�����:</b>ָ�����޸�ʱ�����ٴ�֮�����벻���ظ�ʹ�ã�0��ջ��������޶�<br>
<b>�޸�Ĭ������:</b>����һ�ε�½���߱�����Ա���ú��Ƿ�����޸����롣<br>

</body>
</html>

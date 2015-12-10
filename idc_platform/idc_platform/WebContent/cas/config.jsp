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
1、连续10次错误密码账号锁定root不能锁定
2、可以配置复位时间（单位：小时）
3、被锁定的账号锁定时发送短信提醒
4、所有账号三个月账号必须强制修改如果不修改不能进入系统
5、被锁账号如果再次登录 即使成功也不能进入系统，界面就是锁界面
6、提供管理锁账号的管理界面同时提供解锁复位功能
7、增强防御密码探测的能力 8位数字和字母交替使用 如果没有满足不能使用
**/
var login_repeat = new Ext.form.TextField({
    fieldLabel: '登录尝试次数',
    name: 'login_repeat'
});
var login_locktime = new Ext.form.TextField({
    fieldLabel: '锁定时间(小时)',
    name: 'login_locktime'
});

var pass_period = new Ext.form.TextField({
    fieldLabel: '密码有效期(天)',
    name: 'pass_period'
});
var pass_regulation = new Ext.form.TextField({
    fieldLabel: '密码组成规则',
    name: 'pass_regulation'
});

var pass_regulation_desc = new Ext.form.TextArea({
    fieldLabel: '规则描述',
    name: 'pass_regulation_desc'
});
var pass_repeatTime = new Ext.form.TextField({
    fieldLabel: '密码重复次数',
    name: 'pass_repeatTime',
    allowBlank:true
});
var pass_first = new Ext.form.Checkbox({
    fieldLabel: '修改默认密码',
    name: 'pass_first'
});

var systemManager = new Ext.form.SelectDialogField({
	fieldLabel:"维护人员",
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
    buttons:[{text:'提交',handler:function(){
      	if (!loginForm.form.isValid()) {
					Ext.MessageBox.alert("提示", "请填写完整的内容");
					return;
				}

				loginForm.form.submit({
						waitMsg: '正在处理，请稍候...',
						success: function(form, action)
						{
							Ext.MessageBox.alert("信息", form.errorReader.xmlData.documentElement.text);
						},
						failure: function(form, action)
						{
							Ext.MessageBox.alert("失败", form.errorReader.xmlData.documentElement.text);
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
<b>登录尝试次数:</b>指尝试登录的最大次数，而后锁定；0或空或负数，不限定<br>
<b>锁定时间:</b>账户被锁定后，被锁定的时间，以小时为单位；0或空或负数，不限定<br>
<b>密码有效期:</b>指密码多久必须更改一次，天为单位；0或空或负数，不限定<br>
<b>密码组成规则:</b>规则表达式，如:8位数字组成，/^(0-9){8}$/；空，不限定<br>
<b>密码重复次数:</b>指密码修改时，多少次之内密码不能重复使用；0或空或负数，不限定<br>
<b>修改默认密码:</b>当第一次登陆或者被管理员重置后，是否必须修改密码。<br>

</body>
</html>

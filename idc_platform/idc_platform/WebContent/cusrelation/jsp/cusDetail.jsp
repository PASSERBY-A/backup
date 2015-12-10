<%@ page language="java" pageEncoding="GBK"%>
<html>
<head>
<title>IDC业务管理-客户信息明细</title>
</head>

<%@ include file="/common/inc/header.jsp"%>
<script type="text/javascript"
	src="../cusrelation/scripts/CusQuery_2.js"></script>
<script type="text/javascript"
	src="../cusrelation/scripts/CusQuery_3.js"></script>

<script type="text/javascript">

pageConst={
		queryContactUrl:'<%=ctx%>/cusrelation/queryContactRelation.action',
		queryServantUrl: '<%=ctx%>/cusrelation/queryServantRelation.action'
		
	}
	var tabPanel;
	Ext
			.onReady(function() {
				Ext.QuickTips.init();

				tabPanel = new Ext.TabPanel({
					activeTab : 1,//默认激活第一个tab页
					animScroll : true,//使用动画滚动效果
					enableTabScroll : true,//tab标签超宽时自动出现滚动按钮
					region : 'center',
					margins : '3 3 3 0',
					items : [ {

					} ]
				});

				var row1 = {
					layout : 'column',
					items : [
							{
								columnWidth : .2, //该列有整行中所占百分比   
								layout : 'form', //从上往下布局   
								items : [ {
									xtype : 'textfield',
									id : 'bossid',
									labelStyle : 'color: #445163;font-weight:bold',
									fieldLabel : 'BOSS客户编号',
									width : 150,
									readOnly : true,
									value : '<s:property value="cusInfo.id" escape="false"/>'
								} ]
							},
							{
								columnWidth : .3, //该列有整行中所占百分比   
								layout : 'form', //从上往下布局   
								items : [ {
									xtype : 'textfield',
									id : 'name',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '客户名称',
									readOnly : true,
									value : '<s:property value="cusInfo.name" escape="false"/>'
								} ]
							},
							{
								columnWidth : .2, //该列有整行中所占百分比   
								layout : 'form', //从上往下布局   
								items : [ {
									xtype : 'textfield',
									id : 'abbrName',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '客户名缩写',
									readOnly : true,
									value : '<s:property value="cusInfo.abbrName" escape="false"/>'
								} ]
							},
							{
								columnWidth : .2, //该列有整行中所占百分比   
								layout : 'form', //从上往下布局   
								items : [ {
									xtype : 'textfield',
									id : 'majorContact',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '主要联系人',
									readOnly : true,
									value : '<s:property value="cusInfo.majorContact" escape="false"/>'
								} ]
							} ]
				};

				var row3 = {
					layout : 'column',
					items : [
							
							{
								columnWidth : .2, //该列有整行中所占百分比   
								layout : 'form', //从上往下布局   
								items : [ {
									xtype : 'textfield',
									id : 'address',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '客户地址',
									readOnly : true,
									value : '<s:property value="cusInfo.address" escape="false"/>'
								} ]
							} ]
				};
				var row2 = {
					layout : 'column',
					items : [
							{
								columnWidth : .2,
								layout : 'form',
								items : [ {
									xtype : 'textfield',
									id : 'phoneNo',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '电话',
									readOnly : true,
									value : '<s:property value="cusInfo.phoneNo" escape="false"/>'
								} ]
							},
							{
								columnWidth : .3, //该列有整行中所占百分比   
								layout : 'form', //从上往下布局   
								items : [ {
									xtype : 'textfield',
									id : 'fax',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '传真',
									readOnly : true,
									value : '<s:property value="cusInfo.fax" escape="false"/>'
								} ]
							},
							{
								columnWidth : .2, //该列有整行中所占百分比   
								layout : 'form', //从上往下布局   
								items : [ {
									xtype : 'textfield',
									id : 'email',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : 'Email',
									readOnly : true,
									value : '<s:property value="cusInfo.email" escape="false"/>'
								} ]
							},
							{
								columnWidth : .2, //该列有整行中所占百分比   
								layout : 'form', //从上往下布局   
								items : [ {
									xtype : 'textfield',
									id : 'zipCode',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '邮编',
									readOnly : true,
									value : '<s:property value="cusInfo.zipCode" escape="false"/>'
								} ]
							} ]
				};

				//客户基本信息显示panel
				var form = new Ext.form.FormPanel({
					id : 'form',
					height : 140,
					width : 600,
					region : 'north',
					layout : 'form',
					frame : true,
					labelAlign : 'right',
					buttonAlign : 'right',
					items : [ {
						columnWidth : .9,
						xtype : 'fieldset',
						height : 115,
						layout : 'form',
						title : '客户基本信息',
						items : [ row1, row2, row3 ]
					} ]
				});
				var window = new Ext.Window({
					layout : 'border',
					title : '客户信息明细',
					height : 400,
					closeAction : 'hide',
					draggable : false,
					resizeable : false,
					plain : true,
					modal : true,
					items : [ form, tabPanel ],
					listeners : {
						hide : function() {
							history.back(-1);
						}
					}
				});

				var viewport = new Ext.Viewport({
					layout : "fit",
					items : [ window ]
				});

				window.show();
				//gridReload();

				//客户基本信息列表
				//customerQuery_1();
				//联系人信息列表
				cusQuery_2();
				//客户订购信息列表
				//cusQuery_3();
				//客户账务费用明细列表
				//customerQuery_4();
				//客户消费记录列表
				//customerQuery_5();
				//boss IDC业务信息列表
				//customerQuery_6();
			});
</script>

<body>
</body>
</html>

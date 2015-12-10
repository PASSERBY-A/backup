<%@ page language="java" pageEncoding="GBK"%>
<html>
<head>
<title>IDCҵ�����-�ͻ���Ϣ��ϸ</title>
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
					activeTab : 1,//Ĭ�ϼ����һ��tabҳ
					animScroll : true,//ʹ�ö�������Ч��
					enableTabScroll : true,//tab��ǩ����ʱ�Զ����ֹ�����ť
					region : 'center',
					margins : '3 3 3 0',
					items : [ {

					} ]
				});

				var row1 = {
					layout : 'column',
					items : [
							{
								columnWidth : .2, //��������������ռ�ٷֱ�   
								layout : 'form', //�������²���   
								items : [ {
									xtype : 'textfield',
									id : 'bossid',
									labelStyle : 'color: #445163;font-weight:bold',
									fieldLabel : 'BOSS�ͻ����',
									width : 150,
									readOnly : true,
									value : '<s:property value="cusInfo.id" escape="false"/>'
								} ]
							},
							{
								columnWidth : .3, //��������������ռ�ٷֱ�   
								layout : 'form', //�������²���   
								items : [ {
									xtype : 'textfield',
									id : 'name',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '�ͻ�����',
									readOnly : true,
									value : '<s:property value="cusInfo.name" escape="false"/>'
								} ]
							},
							{
								columnWidth : .2, //��������������ռ�ٷֱ�   
								layout : 'form', //�������²���   
								items : [ {
									xtype : 'textfield',
									id : 'abbrName',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '�ͻ�����д',
									readOnly : true,
									value : '<s:property value="cusInfo.abbrName" escape="false"/>'
								} ]
							},
							{
								columnWidth : .2, //��������������ռ�ٷֱ�   
								layout : 'form', //�������²���   
								items : [ {
									xtype : 'textfield',
									id : 'majorContact',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '��Ҫ��ϵ��',
									readOnly : true,
									value : '<s:property value="cusInfo.majorContact" escape="false"/>'
								} ]
							} ]
				};

				var row3 = {
					layout : 'column',
					items : [
							
							{
								columnWidth : .2, //��������������ռ�ٷֱ�   
								layout : 'form', //�������²���   
								items : [ {
									xtype : 'textfield',
									id : 'address',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '�ͻ���ַ',
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
									fieldLabel : '�绰',
									readOnly : true,
									value : '<s:property value="cusInfo.phoneNo" escape="false"/>'
								} ]
							},
							{
								columnWidth : .3, //��������������ռ�ٷֱ�   
								layout : 'form', //�������²���   
								items : [ {
									xtype : 'textfield',
									id : 'fax',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '����',
									readOnly : true,
									value : '<s:property value="cusInfo.fax" escape="false"/>'
								} ]
							},
							{
								columnWidth : .2, //��������������ռ�ٷֱ�   
								layout : 'form', //�������²���   
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
								columnWidth : .2, //��������������ռ�ٷֱ�   
								layout : 'form', //�������²���   
								items : [ {
									xtype : 'textfield',
									id : 'zipCode',
									labelStyle : 'color: #445163;font-weight:bold',
									width : 150,
									fieldLabel : '�ʱ�',
									readOnly : true,
									value : '<s:property value="cusInfo.zipCode" escape="false"/>'
								} ]
							} ]
				};

				//�ͻ�������Ϣ��ʾpanel
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
						title : '�ͻ�������Ϣ',
						items : [ row1, row2, row3 ]
					} ]
				});
				var window = new Ext.Window({
					layout : 'border',
					title : '�ͻ���Ϣ��ϸ',
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

				//�ͻ�������Ϣ�б�
				//customerQuery_1();
				//��ϵ����Ϣ�б�
				cusQuery_2();
				//�ͻ�������Ϣ�б�
				//cusQuery_3();
				//�ͻ����������ϸ�б�
				//customerQuery_4();
				//�ͻ����Ѽ�¼�б�
				//customerQuery_5();
				//boss IDCҵ����Ϣ�б�
				//customerQuery_6();
			});
</script>

<body>
</body>
</html>

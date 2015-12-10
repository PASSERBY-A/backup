<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDCҵ�����-��ƷĿ¼</title>
</head>

<%@ include file="/common/inc/header.jsp"%>

<script type="text/javascript">
	var desc='<s:property value="service.description" escape="false"/>';
	desc=desc.replace(new RegExp("<br>","gm"),"\r\n");
	
	Ext.onReady(function(){
		Ext.QuickTips.init();
		
		pageConst={
			queryServiceResourceUrl:'<%=ctx%>/business/queryServiceResource.action',
			saveServiceResourceUrl:'<%=ctx%>/business/saveServiceResource.action',
			removeServiceResourceUrl:'<%=ctx%>/business/removeServiceResource.action',
			queryResourceUrl:'<%=ctx%>/business/queryResource.action'
		}
		
		var store = new Ext.data.Store({
			baseParams : {
				id : <s:property value="service.id"/>,
				ajax : true
			},
			proxy : new Ext.data.HttpProxy({
				url : pageConst.queryServiceResourceUrl
			}),
			reader : new Ext.data.JsonReader({
				root : 'result'
			},
			[
				{
					name : 'serviceId',
					mapping : 'serviceId',
					type : 'int'
				}, {
					name : 'resModelId',
					mapping : 'resModelId',
					type : 'string'
				},{
					name : 'amount',
					mapping : 'amount',
					type : 'int'
				}, {
					name : 'resName',
					mapping : 'resName',
					type : 'string'
				},{
					name : 'resType',
					mapping : 'resType',
					type : 'int'
				},{
					name : 'remark',
					mapping : 'remark',
					type : 'string'
				}
			])
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
		sm.on('rowselect', function(sm_, rowIndex, record) {
			btnDelShow();
		}, this);
		sm.on('rowdeselect', function(sm_, rowIndex, record) {
			btnDelShow();
		}, this);
		store.on('load', function() {
			btnDelShow();
		});
		var grid=new Ext.grid.GridPanel({
			id : 'grid',
			region : 'center',
			title : '��������Դ',
			store : store,
			loadMask : true,
			viewConfig : {
				forceFit : true
			},
			tbar:[
				{
					id : 'addBtn',
					text : '���',
					iconCls : 'add',
					handler : function() {
						showServiceResourceEditWin();
					}
				}, '-', {
					id : 'delBtn',
					text : '�Ƴ�',
					iconCls : 'remove',
					handler : function() {
						removeServiceResource();
					}
				}
			],
			sm : sm,
			cm : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(),
				sm,
				{header : "���",sortable : true, dataIndex : 'resModelId'},
				{header : "����",sortable : true, dataIndex : 'resName'},
				{header : "����",sortable : true, dataIndex : 'resType'},
				{header : "����",sortable : true, dataIndex : 'amount'}
			]),
			animCollapse : false,
			bbar:new Ext.PagingToolbar({
				displayMsg : '��{0} �� {1} ������ ��{2}��',
				emptyMsg : "û������",
				pageSize : 20,
				store : store,
				displayInfo : true
			})
		});
		
		var form = new Ext.form.FormPanel({
			id:'form',
			region:'north',
			height:170,
			layout:'column',
			frame:true,
			labelAlign:'right',
			buttonAlign:'right',
			items:[{
					xtype:'fieldset',
					height:143,
					layout:'form',
					title:'����������Ϣ',
					items:[
						{
							items:[
								{
									columnWidth:1,
									layout:"column",
									autoHeight:true,
									items:[
										
										{layout:"form", labelWidth:60, width:350, items:[getServiceIdNI()]},
										{layout:"form", labelWidth:60, width:350, items:[getServiceTypeTI()]}
										
									]
								},
								{
									columnWidth:1,
									layout:"column",
									autoHeight:true,
									items:[
										{layout:"form", labelWidth:60, width:700, items:[getServiceNameTI()]},
										{layout:"form", labelWidth:60, width:700, items:[getServiceDescTA()]}
									]
								}
							]
						}
					]
				}]
		});
		
		var window = new Ext.Window({
			layout: 'border',
			title:'����������ϸ',
			closeAction : 'hide',
			draggable:false,
			resizeable:false,
			plain:true,
			modal:true,
			items: [form,grid],
			listeners :{
				hide:function(){
					history.back(-1);
				}
			}
		})
		
		var viewport = new Ext.Viewport({
			layout: 'fit',
			items: [window]
		});
		window.show();
		gridReload();
	});
	function btnDelShow() {
		var btnDel = Ext.getCmp("delBtn");
		var grid = Ext.getCmp("grid");
		var selCount = grid.getSelectionModel().getCount();
		btnDel.setDisabled(selCount <= 0);
	}
	function gridReload() {
		Ext.getCmp('grid').store.reload({
			params:{
				start:0,
				limit:20
			}
		});
	}
	
	var serviceResourceEditWin;
	var serviceResourceForm;
	var serviceResourceGrid;
	function showServiceResourceEditWin(){
		if(!serviceResourceEditWin){
			serviceResourceEditWin = new Ext.Window({
				id : 'serviceResourceEditWin',
				title : '�����Դ',
				layout : 'fit',
				closable : true,
				closeAction : 'hide',
				constrain:true,
				plain : true,
				modal : true,
				width : 500,
				height : 250,
				resizable : false,
				items : [
				    getServiceResourceForm()
				],
				buttonAlign : 'center',
				buttons : [ {
					text : '�ύ',
					handler : function() {
						var form=Ext.getCmp('serviceResourceForm');
						if (checkServiceResourceValid()) {
							Ext.MessageBox.wait("�ύ����...", "��ȴ�");
							form.form.doAction('submit', {
								url : pageConst.saveServiceResourceUrl,
								method : 'POST',
								success : function(form, action) {
									Ext.MessageBox.hide();
									serviceResourceEditWin.hide();
									Ext.Msg.alert('�ɹ�', action.result.message);
									gridReload();
								},
								failure : function(form, action) {
									Ext.MessageBox.hide();
									Ext.Msg.alert('ʧ��', '����ʱ�����쳣��');
								}
							});
				
						} 
					}
				}, {
					text : 'ȡ��',
					handler : function() {
						serviceResourceEditWin.hide();
						Ext.getCmp('serviceResourceForm').form.reset();
					}
				} ]
			});
		}
		serviceResourceEditWin.show();
	}
	
	
	
	function getServiceResourceForm(){
		if(!serviceResourceForm){
		
			var resourceStore = new Ext.data.JsonStore({
				url : pageConst.queryResourceUrl,
				fields: ['id','name','type'],
				root:'result'
				
			}); 
			serviceResourceForm=new Ext.form.FormPanel({
				id:'serviceResourceForm',
				layout:'form',
				frame:true,
				labelAlign : 'right',
				labelWidth : 70,
				items:[
					
					{
						xtype:'combo',
						fieldLabel:'��Դģ��',
						id:'seviceResourceResModelId',
						hiddenName:'serviceResource.id.resModelId',
						width:380,
						valueField:'id',
						displayField:'name',
						editable:false,
						selectOnFocus:true,
						mode:'remote',
						triggerAction:'all',
						emptyText: '��ѡ��',
						store:resourceStore
					},
					{
						xtype:'numberfield',
						id:'serviceResouceAmount',
						name:'serviceResource.amount',
						fieldLabel:'����',
						width:380,
						minValue:1,
						value:1
					},
					{
						xtype:'textarea',
						id:'serviceResourceRemark',
						name:'serviceResource.remark',
						fieldLabel:'��ע',
						width:377,
						height:100
					},
					{
						xtype:'hidden',
						id:'serviceResourceServiceId',
						name:'serviceResource.id.service.id',
						value:<s:property value="service.id"/>
					}
				]
			});
		}
		return serviceResourceForm;
	}
	
	
	
	function checkServiceResourceValid(){
			
		return true;
	}
	
	function removeServiceResource(){
		
		var rows = Ext.getCmp("grid").getSelectionModel().getSelections();
		var resModelId = "";
		for(var i=0 ;i<rows.length;i++){
			if(resModelId!="")
				resModelId+=","
			resModelId+=rows[i].get("resModelId");
		}
		Ext.Msg.show({
			title : "ɾ����Դ",
			msg : "�Ƿ�ȷ���Ƴ�ѡ�е���Դ��",
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(b) {
				if (b == 'yes') {
					Ext.MessageBox.show({
						title : '�����Ƴ���Դ',
						width : 280,
						wait : true,
						icon : Ext.MessageBox.INFO,
						cls : 'custom',
						closable : false
					});
					Ext.Ajax.request({
						url : pageConst.removeServiceResourceUrl,
						method : 'POST',
						params : {
							serviceId : <s:property value="service.id"/>,
							ids : resModelId
						},
						success : function(response) {
							Ext.MessageBox.hide();
							var responseArray = Ext.util.JSON
									.decode(response.responseText);
							if (true == responseArray.success) {
								Ext.Msg.show({
									title : "���",
									msg : responseArray.message,
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.INFO
								});
								gridReload();
							} else {
								Ext.Msg.show({
									title : "ʧ��",
									msg : responseArray.message,
									buttons : Ext.MessageBox.CANCEL,
									icon : Ext.MessageBox.ERROR
								});
								gridReload();
							}
						},
						failure : function() {
							Ext.MessageBox.hide();
							Ext.MessageBox.hide();
							Ext.Msg.show({
								title : "ʧ��",
								msg : "����ʧ�ܣ��������磬���������쳣����ϵͳ����Ա��ϵ",
								buttons : Ext.MessageBox.CANCEL,
								icon : Ext.MessageBox.ERROR
							});
							gridReload();
						}
					});
				}
			}
		});
		
	}
	
	var serviceIdNI;
	function getServiceIdNI(){
		if(!serviceIdNI){
			serviceIdNI=new Ext.form.TextField({
				 id:'id',
				 width:260,
				 fieldLabel:'���',
				 readOnly:true,
				 value: '<s:property value="service.id"/>'
			});				 
		}
		return serviceIdNI;
	}
	
	var serviceTypeTI;
	function getServiceTypeTI(){
		if(!serviceTypeTI){
			serviceTypeTI=new Ext.form.TextField({			 
				 id:'type',
				 fieldLabel:'����',
				 width:260,
				 readOnly:true,
				 value: <s:if test="service.type==1">'��������'</s:if><s:else>'��ֵ����'</s:else>
			});
		}
		return serviceTypeTI;
	}
	var serviceNameTI;
	function getServiceNameTI(){
		if(!serviceNameTI){
			serviceNameTI=new Ext.form.TextField({
						 xtype:'textfield',
						 id:'name',
						 width:610,
						 fieldLabel:'����',
						 readOnly:true,
						 value:  '<s:property value="service.name" escape="false"/>'
			});
		}
		return serviceNameTI;
	}
	var serviceDescTA;
	function getServiceDescTA(){
		if(!serviceDescTA){
			serviceDescTA=new Ext.form.TextArea({
						 id:'insertTimeAfter',
						 fieldLabel:'����',
						 width:607,
						 height:60,
						 readOnly:true,
						 value: desc
			});
		}
		return serviceDescTA;
	}
</script>

<body>
</body>
</html>
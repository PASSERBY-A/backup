<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDCҵ�����-��ƷĿ¼</title>
</head>

<%@ include file="/common/inc/header.jsp"%>

<script type="text/javascript" src="../scripts/catalogDtlEdit.js"></script>

<script type="text/javascript">

	var desc='<s:property value="catslog.description" escape="false"/>';
	desc=desc.replace(new RegExp("<br>","gm"),"\r\n");
	
	pageConst={
		queryProductCatalogDtlUrl:'<%=ctx%>/business/queryProductCatalogDtl.action',
		saveProductCatalogDtlUrl:'<%=ctx%>/business/saveProductCatalogDtl.action',
		removeProductCatalogDtlUrl:'<%=ctx%>/business/removeProductCatalogDtl.action',
		queryProductUrl:'<%=ctx%>/business/queryProduct.action'
	}
	Ext.onReady(function(){
		Ext.QuickTips.init();
		
		var store = new Ext.data.Store({
			baseParams : {
				"id" : -1,
				ajax : true
			},
			proxy : new Ext.data.HttpProxy({
				url : pageConst.queryProductCatalogDtlUrl
			}),
			reader : new Ext.data.JsonReader({
				totalProperty : 'totalCount',
				root : 'result'
			},
			[
				{
					name : 'id',
					mapping : 'id',
					type : 'int'
				},
				{
					name : 'productId',
					mapping : 'productId',
					type : 'int'
				}, {
					name : 'productName',
					mapping : 'productName',
					type : 'string'
				},{
					name : 'status',
					mapping : 'status',
					type : 'int'
				}, {
					name : 'creator',
					mapping : 'creator',
					type : 'string'
				},{
					name : 'createDate',
					mapping : 'createDate',
					type : 'string',
					renderer : Ext.util.Format.dateRenderer('Y��m��d��')
				},{
					name : 'defaultQuanity',
					mapping : 'defaultQuanity',
					type : 'int'
				}
			])
		});
		store.on('beforeload',function(s,options){
 			Ext.apply(s.baseParams,{
 				id : Ext.getCmp("number").getValue(),
				name : Ext.getCmp("p_name").getValue(),
				catalogId: <s:property value="productCatalog.id"/>
 			});
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
			title : '�����Ļ�����Ʒ',
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
						showCatalogDtlEditWin();
					}
				}, '-', {
					id : 'delBtn',
					text : '�Ƴ�',
					iconCls : 'remove',
					handler : function() {
						removeCatalogDtl();
					}
				}, '-','��ţ�',
				{
					xtype : 'textfield',
					id : 'number',
					name : 'number',
					width : 150
				},'���ƣ�',
				{
					xtype : 'textfield',
					id : 'p_name',
					name : 'p_name',
					width : 150
				}, {
					tooltip : '�������',
					text : '����',
					iconCls : 'search',
					handler : gridReload
				} 
			],
			sm : sm,
			cm : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(),
				sm,
				{header : "���",sortable : true, dataIndex : 'productId'},
				{header : "����",sortable : true, dataIndex : 'productName'},
				{header : "״̬",sortable : true, dataIndex : 'status',
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return value==0?'��Ч':'ʧЧ'
					}
				},
				{header : "Ĭ������",sortable : true, dataIndex : 'defaultQuanity'}
			]),
			animCollapse : false,
			bbar:new Ext.PagingToolbar({
				displayMsg : '��{0} �� {1} ������ ��{2}��',
				emptyMsg : "û������",
				pageSize : 200,
				store : store,
				displayInfo : true
			})
		});
	
		
		var form = new Ext.form.FormPanel({
			id:'form',
			region:'north',
			height:170,
			layout:'form',
			frame:true,
			labelAlign:'right',
			buttonAlign:'right',
			items:[
				{
					xtype:'fieldset',
					height:143,
					layout:'form',
					title:'��ƷĿ¼��Ϣ',
					items:[
						{
							items:[
								{
									columnWidth:1,
									layout:"column",
									autoHeight:true,
									items:[
										
										{layout:"form", labelWidth:60, width:350, items:[getProductCatalogIdTI()]},
										{layout:"form", labelWidth:60, width:350, items:[getProductStatusTI()]}
										
									]
								},
								{
									columnWidth:1,
									layout:"column",
									autoHeight:true,
									items:[
										{layout:"form", labelWidth:60, width:700, items:[getProductCatalogNameTI()]},
										{layout:"form", labelWidth:60, width:700, items:[getProductCatalogDescTA()]}
									]
								}
							]
						}
					]
				}
			]
		});
		var window = new Ext.Window({
			layout: 'border',
			title:'��ƷĿ¼��ϸ',
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
			params : {
				start : 0,
				limit : 10
			}
		});
	}
	var catalogDtlEditWin;
	var catalogDtlForm;
	var catalogDtlGrid;
	
	function showCatalogDtlEditWin(){
		if(!catalogDtlEditWin){
			catalogDtlEditWin = new Ext.Window({
				id : 'catalogDtlEditWin',
				title : '��Ӳ�Ʒ',
				layout : 'border',
				closable : true,
				closeAction : 'hide',
				constrain:true,
				plain : true,
				modal : true,
				width : 600,
				height : 410,
				resizable : false,
				items : [
				    getCatalogDtlForm(),
				    getCatalogDtlGrid()
				],
				buttonAlign : 'center',
				buttons : [ {
					text : '�ύ',
					handler : function() {
						var form = Ext.getCmp('catalogDtlForm');
						if (checkCatalogDtlValid()) {
							Ext.MessageBox.wait("�ύ����...", "��ȴ�");
							form.form.doAction('submit', {
								url : pageConst.saveProductCatalogDtlUrl,
								method : 'POST',
								success : function(form, action) {
									Ext.MessageBox.hide();
									catalogDtlEditWin.hide();
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
						catalogDtlEditWin.hide();
					}
				} ],
				listeners : {
					hide : function() {
						Ext.getCmp('catalogDtlForm').getForm().reset();
					}
				}
			});
		}
		catalogDtlEditWin.show();
		catalogDtlGridReload();
	}

	function getCatalogDtlForm(catalogId){
		if(!catalogDtlForm){
			catalogDtlForm= new Ext.form.FormPanel({
				id:'catalogDtlForm',
				region:'north',
				height:50,
				layout : 'form',
				frame : true,
				labelAlign : 'right',
				labelWidth : 70,
				items : [
					{
						xtype : 'numberfield',
						id : 'catalogDtlDefaultQuanity',
						name : 'productCatalogDtl.defaultQuanity',
						fieldLabel : 'Ĭ������',
						width:390,
						minValue : 1,
						blankText : '����Ϊ�գ�����д',
						value: 1
					},
					{
						xtype : 'hidden',
						id : 'catalogDtlCatalog',
						name : 'productCatalogDtl.id.catalog.id',
						value : <s:property value="productCatalog.id"/>
					},
					{
						xtype : 'hidden',
						id : 'catalogDtlProduct',
						name : 'productCatalogDtl.id.product.id'
					}
				]
			});
		}
		return catalogDtlForm;
	}
	
	function getCatalogDtlGrid(catalogId){
		if(!catalogDtlGrid){
			var catalogDtlStore = new Ext.data.Store({
				baseParams : {
					"id" : -1,
					ajax : true
				},
				proxy : new Ext.data.HttpProxy({
					url : pageConst.queryProductUrl
				}),
				reader : new Ext.data.JsonReader({
					totalProperty : 'totalCount',
					root : 'result'
				},
				[
					{
						name : 'id',
						mapping : 'id',
						type : 'int'
					},{
						name : 'name',
						mapping : 'name',
						type : 'string'
					},{
						name : 'subParam',
						mapping : 'subParam',
						type : 'string'
					},{
						name : 'status',
						mapping : 'status',
						type : 'int'
					},{
						name : 'doneCode',
						mapping : 'doneCode',
						type : 'int'
					}, {
						name : 'creator',
						mapping : 'creator',
						type : 'string'
					},{
						name : 'createDate',
						mapping : 'createDate',
						type : 'string',
						renderer : Ext.util.Format.dateRenderer('Y��m��d��')
					}, {
						name : 'description',
						mapping : 'description',
						type : 'string'
					}
				])
			});
			catalogDtlStore.on('beforeload',function(s,options){
	 			Ext.apply(s.baseParams,{
	 				id : Ext.getCmp("productNumber").getValue(),
					name : Ext.getCmp("productName").getValue()
	 			});
	 		});
			var sm1 = new Ext.grid.RowSelectionModel({singleSelect:true});
			sm1.on('rowselect', function(sm_, rowIndex, record) {
				Ext.getCmp('catalogDtlProduct').setValue(record.get('id'));
			}, this);
			sm1.on('rowdeselect', function(sm_, rowIndex, record) {
				Ext.getCmp('catalogDtlProduct').setValue('');
			}, this);
			
			var catalogDtlGrid=new Ext.grid.GridPanel({
				id : 'catalogDtlGrid',
				region:'center',
				title : 'ѡ��Ҫ��ӵĻ�����Ʒ',
				store : catalogDtlStore,
				loadMask : true,
				viewConfig : {
					forceFit : true
				},
				tbar:[
					'��ţ�',
					{
						xtype : 'textfield',
						id : 'productNumber',
						name : 'productNumber',
						width : 150
					},'���ƣ�',
					{
						xtype : 'textfield',
						id : 'productName',
						name : 'productName',
						width : 150
					}, {
						tooltip : '�������',
						text : '����',
						iconCls : 'search',
						handler : catalogDtlGridReload
					} 
				],
				sm : sm1,
				cm : new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(),
					{header : "���",sortable : true, dataIndex : 'id'},
					{header : "����",sortable : true, dataIndex : 'name'},
					{header : "��Ʒ����",sortable : true, dataIndex : 'subParam'},
					{header : "״̬",sortable : true, dataIndex : 'status',
						renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return value==0?'��Ч':'ʧЧ'
					}
					},
					{header : "������",sortable : true, dataIndex : 'creator'},
					{header : "��������",sortable : true, dataIndex : 'createDate'}
				]),
				animCollapse : false,
				bbar:new Ext.PagingToolbar({
					displayMsg : '��{0} �� {1} ������ ��{2}��',
					emptyMsg : "û������",
					pageSize : 20,
					store : catalogDtlStore,
					displayInfo : true
				})
			});
		}
		return catalogDtlGrid;
	}
	
	function catalogDtlGridReload(){
		Ext.getCmp('catalogDtlGrid').store.reload({
			params : {
				start : 0,
				limit : 20
			}
		})
	}
	
	function checkCatalogDtlValid() {
		var name = Ext.getCmp('catalogDtlDefaultQuanity').getValue();
		if (number == ''||name=='') {
			Ext.MessageBox.alert('���ݲ�����', '����дĬ��������');
			return false;
		}
		var product = Ext.getCmp('catalogDtlProduct').getValue();
		if (product == ''||product=='') {
			Ext.MessageBox.alert('���ݲ�����', '��ѡ��һ����Ʒ��');
			return false;
		}
		return true;
	}
	
	function removeCatalogDtl(){
	
		var rows = Ext.getCmp("grid").getSelections();
		var productId = "";
		for(var i=0;i<rows.length;i++){
			if(productId!="")
				productId+=","
			productId+=rows[i].get('productId');
		}
		
		Ext.Msg.show({
			title : "ɾ��֪ʶ��",
			msg : "�Ƿ�ȷ���Ƴ�ѡ�еĲ�Ʒ��",
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(b) {
				if (b == 'yes') {
					
	
					Ext.MessageBox.show({
						title : '�����Ƴ���Ʒ',
						width : 280,
						wait : true,
						icon : Ext.MessageBox.INFO,
						cls : 'custom',
						closable : false
					});
					Ext.Ajax.request({
						url : pageConst.removeProductCatalogDtlUrl,
						method : 'POST',
						params : {
							ids : productId,
							catalogId : <s:property value="productCatalog.id"/>
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
	var productCatalogIdNI;
	function getProductCatalogIdTI(){
		if(!productCatalogIdNI){
			productCatalogIdNI=new Ext.form.TextField({
				 id:'id',
				 width:260,
				 fieldLabel:'���',
				 readOnly:true,
				 value: '<s:property value="productCatalog.id"/>'
			});				 
		}
		return productCatalogIdNI;
	}
	
	var productStatusTI;
	function getProductStatusTI(){
		if(!productStatusTI){
			productStatusTI=new Ext.form.TextField({			 
				 id:'status',
				 fieldLabel:'״̬',
				 width:260,
				 readOnly:true,
				 value: <s:if test="productCatalog.status==0">'����'</s:if><s:else>'����'</s:else>
			});
		}
		return productStatusTI;
	}
	var productCatalogNameTI;
	function getProductCatalogNameTI(){
		if(!productCatalogNameTI){
			productCatalogNameTI=new Ext.form.TextField({
						 xtype:'textfield',
						 id:'name',
						 width:610,
						 fieldLabel:'����',
						 readOnly:true,
						 value:  '<s:property value="productCatalog.name" escape="false"/>'
			});
		}
		return productCatalogNameTI;
	}
	var productCatalogDescTA;
	function getProductCatalogDescTA(){
		if(!productCatalogDescTA){
			productCatalogDescTA=new Ext.form.TextArea({
						 id:'insertTimeAfter',
						 fieldLabel:'����',
						 width:607,
						 height:60,
						 readOnly:true,
						 value: desc
			});
		}
		return productCatalogDescTA;
	}
</script>

<body>
</body>
</html>
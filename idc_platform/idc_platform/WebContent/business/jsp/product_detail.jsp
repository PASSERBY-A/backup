<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDCҵ�����-��ƷĿ¼</title>
</head>

<%@ include file="/common/inc/header.jsp"%>

<script type="text/javascript">

	var desc='<s:property value="product.description" escape="false"/>';
	desc=desc.replace(new RegExp("<br>","gm"),"\r\n");
	Ext.onReady(function(){
		Ext.QuickTips.init();
		
		pageConst={
			queryProductServiceUrl:'<%=ctx%>/business/queryProductService.action',
			queryServiceUrl:'<%=ctx%>/business/queryService.action',
			addProductServiceUrl:'<%=ctx%>/business/addProductService.action',
			removeProductServiceUrl:'<%=ctx%>/business/removeProductService.action'
		}
		
		var store = new Ext.data.Store({
			baseParams : {
				id : <s:property value="product.id"/>,
				ajax : true
			},
			proxy : new Ext.data.HttpProxy({
				url : pageConst.queryProductServiceUrl
			}),
			reader : new Ext.data.JsonReader({
				root : 'result'
			},
			[
				{
					name : 'id',
					mapping : 'id',
					type : 'int'
				}, {
					name : 'name',
					mapping : 'name',
					type : 'string'
				},{
					name : 'type',
					mapping : 'type',
					type : 'int'
				}, {
					name : 'creator',
					mapping : 'creator',
					type : 'string'
				},{
					name : 'createDate',
					mapping : 'createDate',
					type : 'string',
					renderer : Ext.util.Format.dateRenderer('Y-m-d')
				},{
					name : 'serviceValue',
					mapping : 'serviceValue',
					type : 'string'
				},{
					name : 'description',
					mapping : 'description',
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
			title : '�����ķ���',
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
						showProductServiceEditWin();
					}
				}, '-', {
					id : 'delBtn',
					text : '�Ƴ�',
					iconCls : 'remove',
					handler : function() {
						removeProductService();
					}
				}
			],
			sm : sm,
			cm : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(),
				sm,
				{header : "���",sortable : true, dataIndex : 'id'},
				{header : "����",sortable : true, dataIndex : 'name'},
				{header : "����",sortable : true, dataIndex : 'type',
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return value==1?'��������':'��ֵ����'
					}
				},
				{header : "����",sortable : true, dataIndex : 'serviceValue'},
				{header : "��������",sortable : true, dataIndex : 'createDate'}
			]),
			animCollapse : false
		});
		
		var form = new Ext.form.FormPanel({
			id:'form',
			region:'north',
			height:200,
			layout:'column',
			frame:true,
			labelAlign:'right',
			buttonAlign:'right',
			items:[{
					xtype:'fieldset',
					height:170,
					layout:'form',
					title:'������Ʒ��Ϣ',
					items:[
						{
							items:[
								{
									columnWidth:1,
									layout:"column",
									autoHeight:true,
									items:[
										{layout:"form", labelWidth:80, width:350, items:[getProductIdNI()]},
										{layout:"form", labelWidth:80, width:350, items:[getProductStatusTI()]}
									]
								},
								{
									columnWidth:1,
									layout:"column",
									autoHeight:true,
									items:[
										{layout:"form", labelWidth:80, width:700, items:[getProductNameTI()]},
										{layout:"form", labelWidth:80, width:700, items:[getProductSubParamTI()]},
										{layout:"form", labelWidth:80, width:700, items:[getProductDescTA()]}
									]
								}
							]
						}
						
					]
				}]
		});
		
		var window = new Ext.Window({
			layout: 'border',
			title:'������Ʒ��ϸ',
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
		});
	}
	
	var productServiceEditWin;
	var productServiceGrid;
	
	function showProductServiceEditWin(){
		if(!productServiceEditWin){
			productServiceEditWin = new Ext.Window({
				id : 'productServiceEditWin',
				title : '��ӷ���',
				layout : 'fit',
				closable : true,
				closeAction : 'hide',
				constrain:true,
				plain : true,
				modal : true,
				width : 600,
				height : 410,
				resizable : false,
				items : [
				    getProductServiceGrid()
				],
				buttonAlign : 'center',
				buttons : [ {
					text : '�ύ',
					handler : function() {
						if (checkProductServiceValid()) {
							var records = Ext.getCmp('productServiceGrid').getSelectionModel().getSelections();
							var record=records[0];
							var serviceid=record.get('id');
							Ext.MessageBox.wait("�ύ����...", "��ȴ�");
							Ext.Ajax.request({
								url : pageConst.addProductServiceUrl,
								method : 'POST',
								params : {
									productId : <s:property value="product.id"/>,
									id : serviceid
								},
								success : function(response) {
									Ext.MessageBox.hide();
									productServiceEditWin.hide();
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
				}, {
					text : 'ȡ��',
					handler : function() {
						productServiceEditWin.hide();
					}
				} ]
			});
		}
		productServiceEditWin.show();
		productServiceGridReload();
	}
	
	function getProductServiceGrid(catalogId){
		if(!productServiceGrid){
			var productServiceStore = new Ext.data.Store({
				baseParams : {
					"id" : -1,
					ajax : true
				},
				proxy : new Ext.data.HttpProxy({
					url : pageConst.queryServiceUrl
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
					}, {
						name : 'name',
						mapping : 'name',
						type : 'string'
					},{
						name : 'type',
						mapping : 'type',
						type : 'int'
					}, {
						name : 'creator',
						mapping : 'creator',
						type : 'string'
					},{
						name : 'createDate',
						mapping : 'createDate',
						type : 'string',
						renderer : Ext.util.Format.dateRenderer('Y-m-d')
					},{
						name : 'serviceValue',
						mapping : 'serviceValue',
						type : 'string'
					},{
						name : 'description',
						mapping : 'description',
						type : 'string'
					}
				])
			});
			productServiceStore.on('beforeload',function(s,options){
	 			Ext.apply(s.baseParams,{
	 				id : Ext.getCmp("serviceNumber").getValue(),
					name : Ext.getCmp("serviceName").getValue(),
					productId: <s:property value="product.id"/>
	 			});
	 		});
			var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
			
			
			var productServiceGrid=new Ext.grid.GridPanel({
				id : 'productServiceGrid',
				region:'center',
				title : 'ѡ��Ҫ��ӵķ���',
				store : productServiceStore,
				loadMask : true,
				viewConfig : {
					forceFit : true
				},
				tbar:[
					'��ţ�',
					{
						xtype : 'textfield',
						id : 'serviceNumber',
						name : 'serviceNumber',
						width : 150
					},'���ƣ�',
					{
						xtype : 'textfield',
						id : 'serviceName',
						name : 'serviceName',
						width : 150
					}, {
						tooltip : '�������',
						text : '����',
						iconCls : 'search',
						handler : productServiceGridReload
					} 
				],
				sm : sm,
				cm : new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(),
					{header : "���",sortable : true, dataIndex : 'id'},
					{header : "����",sortable : true, dataIndex : 'name'},
					{header : "����",sortable : true, dataIndex : 'type',
						renderer:function(value, metaData, record, rowIndex, colIndex, store){
							return value==1?'��������':'��ֵ����'
						}
					},
					{header : "����",sortable : true, dataIndex : 'serviceValue'},
					{header : "��������",sortable : true, dataIndex : 'createDate'}
				]),
				animCollapse : false,
				bbar:new Ext.PagingToolbar({
					displayMsg : '��{0} �� {1} ������ ��{2}��',
					emptyMsg : "û������",
					pageSize : 20,
					store : productServiceStore,
					displayInfo : true
				})
			});
		}
		return productServiceGrid;
	}
	
	function productServiceGridReload(){
		Ext.getCmp('productServiceGrid').store.reload({
			params : {
				start : 0,
				limit : 20
			}
		})
	}
	
	function checkProductServiceValid(){
		var selCount=Ext.getCmp('productServiceGrid').getSelectionModel().getCount();
		if(selCount <1){
			Ext.MessageBox.alert("��ѡ��","��ѡ��Ҫ��ӵķ���");
			return false;
		}
		return true;
	}
	
	function removeProductService(){
		
		var rows = Ext.getCmp("grid").getSelections();
		var ids = "";
		Ext.Msg.show({
			title : "ɾ������",
			msg : "�Ƿ�ȷ���Ƴ�ѡ�еķ���",
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(b) {
				if (b == 'yes') {
					for ( var i = 0; i < rows.length; i++) {
						if(ids!="")
							ids+=",";
						ids += rows[i].json.id;
					
					Ext.MessageBox.show({
						title : '�����Ƴ�����',
						width : 280,
						wait : true,
						icon : Ext.MessageBox.INFO,
						cls : 'custom',
						closable : false
					});
					Ext.Ajax.request({
						url : pageConst.removeProductServiceUrl,
						method : 'POST',
						params : {
							productId : <s:property value="product.id"/>,
							ids : ids
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
				}
		});
		
	}
	
	var productIdNI;
	function getProductIdNI(){
		if(!productIdNI){
			productIdNI=new Ext.form.TextField({
			 	id:'id',
			 	width:260,
			 	fieldLabel:'���',
			 	readOnly:true,
			 	value: '<s:property value="product.id"/>'
			});
		}
		return productIdNI;
	}
	var productStatusTI;
	function getProductStatusTI(){
		if(!productStatusTI){
			productStatusTI=new Ext.form.TextField({	
				 id:'status',
				 fieldLabel:'״̬',
				 width:260,
				 readOnly:true,
				 value: <s:if test="product.status==0">'��Ч'</s:if><s:else>'ʧЧ'</s:else>
			});
		}
		return productStatusTI;
	}
	var productSubParamTI;
	function getProductSubParamTI(){
		if(!productSubParamTI){
			productSubParamTI=new Ext.form.TextField({
				 xtype:'textfield',
				 id:'subParam',
				 fieldLabel:'��Ʒ����',
				 width:610,
				 readOnly:true,
				 value:'<s:property value="product.subParam"/>'
			});
		}
		return productSubParamTI;
	}
					
	var productNameTI;
	function getProductNameTI(){
		if(!productNameTI){
			productNameTI=new Ext.form.TextField({
				 id:'name',
				 width:610,
				 fieldLabel:'����',
				 readOnly:true,
				 value:'<s:property value="product.name" escape="false"/>'
			});
		}
		return productNameTI;
	}
	var productDescTA;
	function getProductDescTA(){
		if(!productDescTA){
			productDescTA=new Ext.form.TextArea({
				 id:'insertTimeAfter',
				 fieldLabel:'����',
				 width:607,
				 readOnly:true,
				 value:desc
			});
		}
		return productDescTA;
	}
</script>

<body>
</body>
</html>
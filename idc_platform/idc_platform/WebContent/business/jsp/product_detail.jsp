<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDC业务管理-产品目录</title>
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
			title : '包含的服务',
			store : store,
			loadMask : true,
			viewConfig : {
				forceFit : true
			},
			tbar:[
				{
					id : 'addBtn',
					text : '添加',
					iconCls : 'add',
					handler : function() {
						showProductServiceEditWin();
					}
				}, '-', {
					id : 'delBtn',
					text : '移除',
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
				{header : "编号",sortable : true, dataIndex : 'id'},
				{header : "名称",sortable : true, dataIndex : 'name'},
				{header : "类型",sortable : true, dataIndex : 'type',
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return value==1?'基础服务':'增值服务'
					}
				},
				{header : "属性",sortable : true, dataIndex : 'serviceValue'},
				{header : "创建日期",sortable : true, dataIndex : 'createDate'}
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
					title:'基础产品信息',
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
			title:'基础产品明细',
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
				title : '添加服务',
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
					text : '提交',
					handler : function() {
						if (checkProductServiceValid()) {
							var records = Ext.getCmp('productServiceGrid').getSelectionModel().getSelections();
							var record=records[0];
							var serviceid=record.get('id');
							Ext.MessageBox.wait("提交数据...", "请等待");
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
											title : "完成",
											msg : responseArray.message,
											buttons : Ext.MessageBox.OK,
											icon : Ext.MessageBox.INFO
										});
										gridReload();
									} else {
										Ext.Msg.show({
											title : "失败",
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
										title : "失败",
										msg : "连接失败，请检查网络，如网络无异常请与系统管理员联系",
										buttons : Ext.MessageBox.CANCEL,
										icon : Ext.MessageBox.ERROR
									});
									gridReload();
								}
							});
						} 
					}
				}, {
					text : '取消',
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
				title : '选择要添加的服务',
				store : productServiceStore,
				loadMask : true,
				viewConfig : {
					forceFit : true
				},
				tbar:[
					'编号：',
					{
						xtype : 'textfield',
						id : 'serviceNumber',
						name : 'serviceNumber',
						width : 150
					},'名称：',
					{
						xtype : 'textfield',
						id : 'serviceName',
						name : 'serviceName',
						width : 150
					}, {
						tooltip : '点击搜索',
						text : '搜索',
						iconCls : 'search',
						handler : productServiceGridReload
					} 
				],
				sm : sm,
				cm : new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(),
					{header : "编号",sortable : true, dataIndex : 'id'},
					{header : "名称",sortable : true, dataIndex : 'name'},
					{header : "类型",sortable : true, dataIndex : 'type',
						renderer:function(value, metaData, record, rowIndex, colIndex, store){
							return value==1?'基础服务':'增值服务'
						}
					},
					{header : "属性",sortable : true, dataIndex : 'serviceValue'},
					{header : "创建日期",sortable : true, dataIndex : 'createDate'}
				]),
				animCollapse : false,
				bbar:new Ext.PagingToolbar({
					displayMsg : '第{0} 到 {1} 条数据 共{2}条',
					emptyMsg : "没有数据",
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
			Ext.MessageBox.alert("请选择","请选择要添加的服务");
			return false;
		}
		return true;
	}
	
	function removeProductService(){
		
		var rows = Ext.getCmp("grid").getSelections();
		var ids = "";
		Ext.Msg.show({
			title : "删除服务",
			msg : "是否确认移除选中的服务？",
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(b) {
				if (b == 'yes') {
					for ( var i = 0; i < rows.length; i++) {
						if(ids!="")
							ids+=",";
						ids += rows[i].json.id;
					
					Ext.MessageBox.show({
						title : '正在移除服务',
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
									title : "完成",
									msg : responseArray.message,
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.INFO
								});
								gridReload();
							} else {
								Ext.Msg.show({
									title : "失败",
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
								title : "失败",
								msg : "连接失败，请检查网络，如网络无异常请与系统管理员联系",
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
			 	fieldLabel:'编号',
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
				 fieldLabel:'状态',
				 width:260,
				 readOnly:true,
				 value: <s:if test="product.status==0">'有效'</s:if><s:else>'失效'</s:else>
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
				 fieldLabel:'产品参数',
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
				 fieldLabel:'名称',
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
				 fieldLabel:'描述',
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
<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDC业务管理——基础产品</title>
</head>

<%@ include file="/common/inc/header.jsp"%>
<script type="text/javascript" src="/common/scripts/fieldLength.js"></script>
<script type="text/javascript" src="../scripts/productEdit.js"></script>
<script type="text/javascript">

	pageConst={
		queryProductUrl:'<%=ctx%>/business/queryProduct.action',
		addProductUrl:'<%=ctx%>/business/addProduct.action',
		updateProductUrl:'<%=ctx%>/business/updateProduct.action',
		removeProductUrl:'<%=ctx%>/business/removeProduct.action',
		detailProductUrl: '<%=ctx%>/business/detailProduct.action'
	}
	Ext.onReady(function(){
		Ext.QuickTips.init();
		
		var store = new Ext.data.Store({
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
					type : 'string'
				}, {
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
					renderer : Ext.util.Format.dateRenderer('Y-m-d')
				},{
					name : 'effectDate',
					mapping : 'effectDate',
					type : 'string',
					renderer : Ext.util.Format.dateRenderer('Y-m-d')
				},{
					name : 'expireDate',
					mapping : 'expireDate',
					type : 'string',
					renderer : Ext.util.Format.dateRenderer('Y-m-d')
				}, {
					name : 'description',
					mapping : 'description',
					type : 'string'
				}
			])
		});
		store.on('beforeload',function(s,options){
 			Ext.apply(s.baseParams,{
 				id : Ext.getCmp("number").getValue(),
				name : Ext.getCmp("name").getValue()
 			});
 		});
		var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
		sm.on('rowselect', function(sm_, rowIndex, record) {
			btnEditShow();
			btnDelShow();
		}, this);
		sm.on('rowdeselect', function(sm_, rowIndex, record) {
			btnEditShow();
			btnDelShow();
		}, this);
		store.on('load', function() {
			btnEditShow();
			btnDelShow();
		});
		var grid=new Ext.grid.GridPanel({
			id : 'grid',
			title : parent.nodeText,
			store : store,
			loadMask : true,
			viewConfig : {
				forceFit : true
			},
			tbar:[
				{
					id : 'addBtn',
					text : '新增',
					iconCls : 'add',
					handler : function() {
						showProductEditWin(null);
					}
				}, '-', {
					id : 'delBtn',
					text : '删除',
					iconCls : 'remove',
					handler : function() {
						removeProduct();
					}
				}, '-', {
					id : 'editBtn',
					text : '修改',
					iconCls : 'edit',
					handler : function() {
						editProduct();
					}
				}, '-',{
					id : 'dtlBtn',
					text : '明细',
					iconCls : 'views',
					handler : function() {
						var records=grid.getSelectionModel().getSelections();
						var record=records[0];
						window.location.href= pageConst.detailProductUrl+"?id="+record.get('id');
					}
				}, '-','编号：',
				{
					xtype : 'textfield',
					id : 'number',
					name : 'number',
					width : 150
				},'名称：',
				{
					xtype : 'textfield',
					id : 'name',
					name : 'name',
					width : 150
				}, {
					tooltip : '点击搜索',
					text : '搜索',
					iconCls : 'search',
					handler : gridReload
				} 
			],
			sm : sm,
			cm : new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(),
				sm,
				{header : "编号",sortable : true, dataIndex : 'id'},
				{header : "名称",sortable : true, dataIndex : 'name'},
				{header : "产品参数",sortable : true, dataIndex : 'subParam',hidden:true},
				{header : "状态",sortable : true, dataIndex : 'status',
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return value==0?'有效':'失效'
					}
				},
				{header : "受理编号",sortable : true, dataIndex : 'doneCode',hidden:true},
				{header : "创建人",sortable : true, dataIndex : 'creator'},
				{header : "创建日期",sortable : true, dataIndex : 'createDate'}
			]),
			animCollapse : false,
			bbar:new Ext.PagingToolbar({
				displayMsg : '第{0} 到 {1} 条数据 共{2}条',
				emptyMsg : "没有数据",
				pageSize : 20,
				store : store,
				displayInfo : true
			})
		});
		var viewport = new Ext.Viewport({
			layout: 'fit',
			items: [grid]
		});
		
		gridReload();
	});
	
	
	
	function gridReload() {
		Ext.getCmp('grid').store.reload({
			params : {
				start : 0,
				limit : 20
			}
		});
	}
	
	//知识点列表操作按钮显示控制
	function btnEditShow() {
		var btnEdit = Ext.getCmp("editBtn");
		var btnDtl = Ext.getCmp("dtlBtn");
		var grid = Ext.getCmp("grid");
		var selCount = grid.getSelectionModel().getCount();
		btnEdit.setDisabled(selCount != 1);
		btnDtl.setDisabled(selCount != 1);
	}
	
	function btnDelShow() {
		var btnDel = Ext.getCmp("delBtn");
		var grid = Ext.getCmp("grid");
		var selCount = grid.getSelectionModel().getCount();
		btnDel.setDisabled(selCount <= 0);
	}
</script>

<body>
</body>
</html>
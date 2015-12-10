<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDC业务管理-产品目录</title>
</head>

<%@ include file="/common/inc/header.jsp"%>
<script type="text/javascript" src="../scripts/catalogEdit.js"></script>
<script type="text/javascript" src="/common/scripts/fieldLength.js"></script>
<script type="text/javascript" src="/common/scripts/fieldBlank.js"></script>
<script type="text/javascript" src="/common/scripts/dateCompare.js"></script>
<script type="text/javascript">

	pageConst={
		queryCatalogUrl:'<%=ctx%>/business/queryProductCatalog.action',
		addCatalogUrl:'<%=ctx%>/business/addProductCatalog.action',
		updateCatalogUrl:'<%=ctx%>/business/updateProductCatalog.action',
		removeCatalogUrl:'<%=ctx%>/business/removeProductCatalog.action',
		detailCatalogUrl: '<%=ctx%>/business/detailProductCatalog.action'
	}
	
	Ext.onReady(function(){
		Ext.QuickTips.init();
		
		var store = new Ext.data.Store({
			baseParams : {
				"id" : -1,
				ajax : true
			},
			proxy : new Ext.data.HttpProxy({
				url : pageConst.queryCatalogUrl
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
					name : 'status',
					mapping : 'status',
					type : 'int'
				}, {
					name : 'creator',
					mapping : 'creator',
					type : 'string'
				}, {
					name : 'description',
					mapping : 'description',
					type : 'string'
				},{
					name : 'createDate',
					mapping : 'createDate',
					type : 'string',
					renderer : Ext.util.Format.dateRenderer('Y年m月d日')
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
						showCatalogEditWin(null);
					}
				}, '-', {
					id : 'delBtn',
					text : '删除',
					iconCls : 'remove',
					handler : function() {
						removeCatalog();
					}
				}, '-', {
					id : 'editBtn',
					text : '修改',
					iconCls : 'edit',
					handler : function() {
						editProductCatalog();
					}
				}, '-',{
					id : 'dtlBtn',
					text : '明细',
					iconCls : 'views',
					handler : function() {
						var records=grid.getSelectionModel().getSelections();
						var record=records[0];
						window.location.href= pageConst.detailCatalogUrl+"?id="+record.get('id');
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
				{header : "状态",sortable : true, dataIndex : 'status',
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return value==0?'启用':'禁用'
					}
				},
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
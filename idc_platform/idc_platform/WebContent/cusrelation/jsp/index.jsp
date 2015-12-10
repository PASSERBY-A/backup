<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDC业务管理-客户管理</title>
</head>

<%@ include file="/common/inc/header.jsp"%>

<script type="text/javascript">

	pageConst={
		queryCustomerInfo:'<%=ctx%>/cusrelation/queryCustomerRelationInfo.action',
		detailCustomer:'<%=ctx%>/cusrelation/detailCustomerRelation.action'
	};

	var store;
	Ext.onReady(function() {
		Ext.QuickTips.init();

		//form.render();

		//客户基本信息数据组装
		store = new Ext.data.Store({
			baseParams : {
				"id" : -1,
				ajax : true
			},
			proxy : new Ext.data.HttpProxy({
				url : pageConst.queryCustomerInfo
			}),
			reader : new Ext.data.JsonReader({
				totalProperty : 'totalCount',
				root : 'result'
			}, [ {
				name : 'id',
				mapping : 'id',
				type : 'int'
			}, {
				name : 'name',
				mapping : 'name',
				type : 'string'
			},{
				name : 'abbrName',
				mapping : 'abbrName',
				type : 'string'
			}, {
				name : 'vocation',
				mapping : 'vocation',
				type : 'int'
			},  {
				name : 'majorContact',
				mapping : 'majorContact',
				type : 'string'
			}, {
				name : 'address',
				mapping : 'address',
				type : 'string'
			}, {
				name : 'openTime',
				mapping : 'openTime',
				type : 'string'
			//renderer : Ext.util.Format.dateRenderer('Y年m月d日')
			}, {
				name : 'cancelTime',
				mapping : 'cancelTime',
				type : 'string'
			///renderer : Ext.util.Format.dateRenderer('Y年m月d日')
			}, {
				name : 'status',
				mapping : 'status',
				type : 'int'
			} ])
		});
		 store.on('beforeload', function(s, options) {
			Ext.apply(s.baseParams);
		}); 

		//store.load({params:{start:0, limit:20}});
		//客户基本信息列表
		var gridPanel = new Ext.grid.GridPanel({
			id : 'grid',
			title : '客户列表',
			region : 'center',
			store : store,
			loadMask : true,
			viewConfig : {
				forceFit : true
			},
			cm : new Ext.grid.ColumnModel([
					{
						header : "BOSS客户编号",
						sortable : true,
						dataIndex : 'id',
						width : 70
					},

					{
						header : "客户名称",
						sortable : true,
						dataIndex : 'name'
					},
					{
						header : "客户名缩写",
						sortable : true,
						dataIndex : 'abbrName'
					},
					
					{
						header : "行业类别",
						sortable : true,
						dataIndex : 'vocation',
						width : 60
					},
					
					{
						header : "主要联系人",
						sortable : true,
						dataIndex : 'majorContact',
						width : 60
					},
					{
						header : "基本地址",
						sortable : true,
						dataIndex : 'address'
					},
					{
						header : "开户时间",
						sortable : true,
						dataIndex : 'openTime'
					},
					{
						header : "销户时间",
						sortable : true,
						dataIndex : 'cancelTime'
					},
					{
						header : "状态",
						sortable : true,
						dataIndex : 'status',
						renderer : function(data, metadata, record, rowIndex,
								columnIndex, store) {
							var value = record.get('status');
							if (value == 0)
								return "历史";
							if (value == 1)
								return "正常";
							if (value == 2)
								return "潜在";
							if (value == 3)
								return "注销";

						}
					} ]),
			animCollapse : false,
			//tbar : tbar,
			tbar : ['-',{
				id : 'searchBtn',
				text : '查询',
				iconCls:'search',
				handler : function() {
					showSearchWin();
				}
			} ,'-', {
				id : 'detailBtn',
				text : '明细',
				iconCls:'views',
				handler : viewDetail
			} ],
			bbar : new Ext.PagingToolbar({
				displayMsg : '第{0} 到 {1} 条数据 共{2}条',
				emptyMsg : "没有数据",
				pageSize : 20,
				store : store,
				displayInfo : true
				
			}),
			listeners : {
				//监听，双击弹出窗口
				rowdblclick : viewDetail
			}
		});

		var viewport = new Ext.Viewport({
			layout : 'fit',
			items : [ gridPanel ]
		});

		gridReload();
		
		//明细查看方法
		function viewDetail() {
					//查看明细
					var records = gridPanel.getSelectionModel().getSelections();
					var record = records[0];
					if (record != null) {
						window.location.href = pageConst.detailCustomer
										+ "?id=" + record.get('id');
					} else {
						Ext.Msg.alert("错误", "请选择一条记录");
					}
				}
	});
	
	/*****************Ext.onReady End*/

	//列表载入function
	function gridReload() {
		Ext.getCmp('grid').store.reload({
			params : {
				start : 0,
				limit : 20
			}
		});
	};
	//查询条件输入校验function
	function checkSearch() {

	}
	/***判断是否为数字*******/
	function isNumber(str) {
		if ("" == str) {
			return false;
		}
		var reg = /\D/;
		return str.match(reg) == null;
	}

	/***********检索条件弹出框********************/
	function showSearchWin() {
		//查询条件面板
		var form = new Ext.form.FormPanel({
			labelWidth : 100,
			layout : 'form',
			autoHeight:true,
			frame : true,
			labelAlign : 'right',
			buttonAlign : 'center',

			items : [ {
				xtype : 'numberfield',
				fieldLabel : 'BOSS客户编号',
				id : 'id',
				name : 'id'
			},{
				xtype : 'textfield',
				fieldLabel : '客户名称',
				id : 'name',
				name : 'name'
			}, {
				xtype : 'textfield',
				fieldLabel : '大客户经理',
				id : 'managerName',
				name : 'managerName'
			},{
				xtype : 'textfield',
				fieldLabel : '电话',
				id : 'phoneNo',
				name : 'phoneNo'
			}, {
				xtype : 'textfield',
				fieldLabel : 'Email',
				id : 'email',
				name : 'email'
			}]
		});
	
		var SearchWin = new Ext.Window({
			title : '检索对话框',
			width : 280,
			height : 220,
			plain : true,
			iconCls : 'search',
			modal : true,
			items : [ form ],
			buttons : [ {
				text : '检索',
				handler : function() {
					search();
					SearchWin.close();
				}
			}, {
				text : '取消',
				handler : function() {
					SearchWin.close();
				}
			},{
				text : '重置',
				handler : function() {
					form.getForm().reset();
				}
			} ]
		});
		SearchWin.show();

		
		//传递查询条件funcion
		function search() {

			store.baseParams.search_id = form.getForm().findField("id").getValue();
			store.baseParams.search_name = form.getForm().findField("name")
					.getValue();
			store.baseParams.search_managerName = form.getForm().findField(
					"managerName").getValue();
			store.baseParams.search_phoneNo = form.getForm().findField("phoneNo")
					.getValue();
			store.baseParams.search_email = form.getForm().findField("email")
					.getValue();

			store.reload({
				params : {
					start : 0,
					limit : 20
				}
			});
		}
	}
</script>

<body>
</body>
</html>
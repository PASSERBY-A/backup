//BOSS IDC业务信息列表
var gridPanel_6;
function customerQuery_6() {

	var store = new Ext.data.Store({
		baseParams : {
			"id" : -1,
			ajax : true
		},
		proxy : new Ext.data.HttpProxy({
			url : pageConst.queryBussinessUrl
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'result'
		}, [ {
			name : 'id.orderId',
			mapping : 'id.orderId',
			type : 'int'
		}, {
			name : 'id.orderDetailId',
			mapping : 'id.orderDetailId',
			type : 'int'
		}, {
			name : 'idcNum',
			mapping : 'idcNum',
			type : 'int'
		}, {
			name : 'ipNum',
			mapping : 'ipNum',
			type : 'int'
		}, {
			name : 'deviceNum',
			mapping : 'deviceNum',
			type : 'int'
		}, {
			name : 'domainName',
			mapping : 'domainName',
			type : 'string'
		}, {
			name : 'vpnNum',
			mapping : 'vpnNum',
			type : 'int'
		}, {
			name : 'powerNum',
			mapping : 'powerNum',
			type : 'int'
		}, {
			name : 'validDate',
			mapping : 'validDate',
			type : 'string',
			renderer : Ext.util.Format.dateRenderer('Y年m月d日')
		}, {
			name : 'expireDate',
			mapping : 'expireDate',
			type : 'string',
			renderer : Ext.util.Format.dateRenderer('Y年m月d日')
		}, {
			name : 'id.doneCode',
			mapping : 'id.doneCode',
			type : 'int'
		}, {
			name : 'doneDate',
			mapping : 'doneDate',
			type : 'string',
			renderer : Ext.util.Format.dateRenderer('Y年m月d日')
		} ])
	});
	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			id : Ext.getCmp("bossid").getValue()
		});
	});

	gridPanel_6 = new Ext.grid.GridPanel({
		id : 'grid6',
		title : 'BOSS_IDC业务信息',
		store : store,
		loadMask : true,
		frame:true,
        border:true,
		viewConfig : {
			forceFit : true
		},
		cm : new Ext.grid.ColumnModel([ {
			header : "订单号",
			sortable : true,
			dataIndex : 'id.orderId'
		}, {
			header : "订单明细号",
			sortable : true,
			dataIndex : 'id.orderDetailId'
		}, {
			header : "IDC数量",
			sortable : true,
			dataIndex : 'idcNum'
		}, {
			header : "IP地址数",
			sortable : true,
			dataIndex : 'ipNum'
		}, {
			header : "托管设备数量",
			sortable : true,
			dataIndex : 'deviceNum'
		}, {
			header : "域名",
			sortable : true,
			dataIndex : 'domainName'
		}, {
			header : "VPN数量",
			sortable : true,
			dataIndex : 'vpnNum'
		}, {
			header : "电源数量",
			sortable : true,
			dataIndex : 'powerNum'
		}, {
			header : "生效日期",
			sortable : true,
			dataIndex : 'validDate'

		}, {
			header : "失效日期",
			sortable : true,
			dataIndex : 'expireDate'

		}, {
			header : "业务流水号",
			sortable : true,
			dataIndex : 'id.doneCode'

		}, {
			header : "业务日期",
			sortable : true,
			dataIndex : 'doneDate'

		} ]),
		animCollapse : false,
		bbar : new Ext.PagingToolbar({
			displayMsg : '第{0} 到 {1} 条数据 共{2}条',
			emptyMsg : "没有数据",
			pageSize : 20,
			store : store,
			displayInfo : true
		})
	});

	// viewport1 = new Ext.Viewport({ layout : 'fit', items : [ gridPanel_1 ]
	// });
	tabPanel.add({
		title : "BOSS_IDC业务信息",
		height : 568,
		// closable:true,
		layout : "fit",
		items : [ gridPanel_6 ]
	});
	Ext.getCmp('grid6').store.reload({
		params : {
			start : 0,
			limit : 20
		}
	});
}

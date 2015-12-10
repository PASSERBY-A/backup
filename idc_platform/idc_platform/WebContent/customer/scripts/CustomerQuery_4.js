//客户账务费用列表
var gridPanel_4;
function customerQuery_4() {

	var store = new Ext.data.Store({
		baseParams : {
			"id" : -1,
			ajax : true
		},
		proxy : new Ext.data.HttpProxy({
			url : pageConst.queryAccountUrl
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'result'
		}, [ {
			name : 'id.billMonth',
			mapping : 'id.billMonth',
			type : 'int'
		}, {
			name : 'id.customerId',
			mapping : 'id.customerId',
			type : 'int'
		},{
			name : 'id.serviceId',
			mapping : 'id.serviceId',
			type : 'int'
		},{
			name : 'payFlag',
			mapping : 'payFlag',
			type : 'int'
		},{
			name : 'totalFee',
			mapping : 'totalFee'}, {
			name : 'remarks',
			mapping : 'remarks',
			type : 'string'
		} ])
	});
	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			id : Ext.getCmp("bossid").getValue()
		});
	});

	gridPanel_4 = new Ext.grid.GridPanel({
		id : 'grid4',
		title : '账务费用列表',
		store : store,
		loadMask : true,
		frame:true,
        border:true,
		viewConfig : {
			forceFit : true
		},
		cm : new Ext.grid.ColumnModel([ {
			header : "账单月",
			sortable : true,
			dataIndex : 'id.billMonth'
		}, {
			header : "集团编号",
			sortable : true,
			dataIndex : 'id.customerId'
		},{
			header : "服务编号",
			sortable : true,
			dataIndex : 'id.serviceId'
		}, {
			header : "代付标识",
			sortable : true,
			dataIndex : 'payFlag'

		}, {
			header : "费用",
			sortable : true,
			dataIndex : 'totalFee'

		}, {
			header : "说明",
			dataIndex : 'remarks'

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
		title : "客户账务费用明细",
		height : 568,
		// closable:true,
		layout : "fit",
		items : [ gridPanel_4 ]
	});
	Ext.getCmp('grid4').store.reload({
		params : {
			start : 0,
			limit : 20
		}
	});
}

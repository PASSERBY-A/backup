//客户消费记录列表
var gridPanel_5;
function customerQuery_5() {

	var store = new Ext.data.Store({
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
		}, [  {
			name : 'id.billMonth',
			mapping : 'id.billMonth',
			type : 'int'
		},{
			name : 'id.customerId',
			mapping : 'id.customerId',
			type : 'int'
		},{
			name : 'id.accId',
			mapping : 'id.accId',
			type : 'int'
		},{
			name : 'id.serviceId',
			mapping : 'id.serviceId',
			type : 'int'
		},{
			name : 'id.subId',
			mapping : 'id.subId',
			type : 'int'
		},{
			name : 'payFlag',
			mapping : 'payFlag',
			type : 'int'
		},{
			name : 'totalFee',
			mapping : 'totalFee'} ])
	});
	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			id : Ext.getCmp("bossid").getValue()
		});
	});
	
	gridPanel_5 = new Ext.grid.GridPanel({
		id : 'grid5',
		title : '客户消费记录列表',
		store : store,
		loadMask : true,
		frame:true,
        border:true,
		viewConfig : {
			forceFit : true
		},
		cm : new Ext.grid.ColumnModel([
				
				{
					header : "账单月",
					sortable : true,
					dataIndex : 'id.billMonth'
				},{
					header : "客户编号",
					sortable : true,
					dataIndex : 'id.customerId'
				},
				{
					header : "账户号码",
					sortable : true,
					dataIndex : 'id.accId'
				},{
					header : "服务编号",
					sortable : true,
					dataIndex : 'id.serviceId'
				},{
					header : "订购编号",
					sortable : true,
					dataIndex : 'id.subId'
				},{
					header : "代付标识",
					sortable : true,
					dataIndex : 'payFlag'
				},
				{
					header : "费用",
					sortable : true,
					dataIndex : 'totalFee'
				}]),
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
		title : "客户消费记录",
		height : 568,
		// closable:true,
		layout : "fit",
		items : [ gridPanel_5 ]
	});
	Ext.getCmp('grid5').store.reload({
		params : {
			start : 0,
			limit : 20
		}
	});
}

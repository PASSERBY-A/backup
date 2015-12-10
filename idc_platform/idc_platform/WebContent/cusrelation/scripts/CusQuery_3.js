//客户订购信息列表
var gridPanel_3;
function cusQuery_3() {

	var store = new Ext.data.Store({
		baseParams : {
			"id" : -1,
			ajax : true
		},
		proxy : new Ext.data.HttpProxy({
			url : pageConst.queryServantUrl
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'result'
		}, [ {
			name : 'id.serviceId',
			mapping : 'id.serviceId',
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
		} ])
	});
	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			id : Ext.getCmp("bossid").getValue()
		});
	});

	gridPanel_3 = new Ext.grid.GridPanel({
		id : 'grid3',
		title : '客户订购信息',
		store : store,
		loadMask : true,
		frame:true,
        border:true,
		viewConfig : {
			forceFit : true
		},
		cm : new Ext.grid.ColumnModel([ {
			header : "服务标识",
			sortable : true,
			dataIndex : 'id.serviceId'
		},  {
			header : "生效日期",
			sortable : true,
			dataIndex : 'validDate'
		}, {
			header : "失效日期",
			sortable : true,
			dataIndex : 'expireDate'
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
		title : "客户订购信息",
		height : 568,
		// closable:true,
		layout : "fit",
		items : [ gridPanel_3 ]
	});

	Ext.getCmp('grid3').store.reload({
		params : {
			start : 0,
			limit : 20
		}
	});
}

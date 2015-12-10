//�ͻ���������б�
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
		title : '��������б�',
		store : store,
		loadMask : true,
		frame:true,
        border:true,
		viewConfig : {
			forceFit : true
		},
		cm : new Ext.grid.ColumnModel([ {
			header : "�˵���",
			sortable : true,
			dataIndex : 'id.billMonth'
		}, {
			header : "���ű��",
			sortable : true,
			dataIndex : 'id.customerId'
		},{
			header : "������",
			sortable : true,
			dataIndex : 'id.serviceId'
		}, {
			header : "������ʶ",
			sortable : true,
			dataIndex : 'payFlag'

		}, {
			header : "����",
			sortable : true,
			dataIndex : 'totalFee'

		}, {
			header : "˵��",
			dataIndex : 'remarks'

		} ]),
		animCollapse : false,
		bbar : new Ext.PagingToolbar({
			displayMsg : '��{0} �� {1} ������ ��{2}��',
			emptyMsg : "û������",
			pageSize : 20,
			store : store,
			displayInfo : true
		})
	});

	// viewport1 = new Ext.Viewport({ layout : 'fit', items : [ gridPanel_1 ]
	// });
	tabPanel.add({
		title : "�ͻ����������ϸ",
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

//BOSS IDCҵ����Ϣ�б�
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
			renderer : Ext.util.Format.dateRenderer('Y��m��d��')
		}, {
			name : 'expireDate',
			mapping : 'expireDate',
			type : 'string',
			renderer : Ext.util.Format.dateRenderer('Y��m��d��')
		}, {
			name : 'id.doneCode',
			mapping : 'id.doneCode',
			type : 'int'
		}, {
			name : 'doneDate',
			mapping : 'doneDate',
			type : 'string',
			renderer : Ext.util.Format.dateRenderer('Y��m��d��')
		} ])
	});
	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			id : Ext.getCmp("bossid").getValue()
		});
	});

	gridPanel_6 = new Ext.grid.GridPanel({
		id : 'grid6',
		title : 'BOSS_IDCҵ����Ϣ',
		store : store,
		loadMask : true,
		frame:true,
        border:true,
		viewConfig : {
			forceFit : true
		},
		cm : new Ext.grid.ColumnModel([ {
			header : "������",
			sortable : true,
			dataIndex : 'id.orderId'
		}, {
			header : "������ϸ��",
			sortable : true,
			dataIndex : 'id.orderDetailId'
		}, {
			header : "IDC����",
			sortable : true,
			dataIndex : 'idcNum'
		}, {
			header : "IP��ַ��",
			sortable : true,
			dataIndex : 'ipNum'
		}, {
			header : "�й��豸����",
			sortable : true,
			dataIndex : 'deviceNum'
		}, {
			header : "����",
			sortable : true,
			dataIndex : 'domainName'
		}, {
			header : "VPN����",
			sortable : true,
			dataIndex : 'vpnNum'
		}, {
			header : "��Դ����",
			sortable : true,
			dataIndex : 'powerNum'
		}, {
			header : "��Ч����",
			sortable : true,
			dataIndex : 'validDate'

		}, {
			header : "ʧЧ����",
			sortable : true,
			dataIndex : 'expireDate'

		}, {
			header : "ҵ����ˮ��",
			sortable : true,
			dataIndex : 'id.doneCode'

		}, {
			header : "ҵ������",
			sortable : true,
			dataIndex : 'doneDate'

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
		title : "BOSS_IDCҵ����Ϣ",
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

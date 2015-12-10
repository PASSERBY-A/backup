//�ͻ�������Ϣ�б�
var gridPanel_3;
function customerQuery_3() {

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
			name : 'id.subId',
			mapping : 'id.subId',
			type : 'int'
		}, {
			name : 'id.serviceId',
			mapping : 'id.serviceId',
			type : 'int'
		}, {
			name : 'productId',
			mapping : 'productId',
			type : 'int'
		}, {
			name : 'serviceValue',
			mapping : 'serviceValue',
			type : 'string'
		}, {
			name : 'createDate',
			mapping : 'createDate',
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
			name : 'notes',
			mapping : 'notes',
			type : 'string'
		} ])
	});
	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			id : Ext.getCmp("bossid").getValue()
		});
	});

	gridPanel_3 = new Ext.grid.GridPanel({
		id : 'grid3',
		title : '�ͻ�������Ϣ',
		store : store,
		loadMask : true,
		frame:true,
        border:true,
		viewConfig : {
			forceFit : true
		},
		cm : new Ext.grid.ColumnModel([ {
			header : "���������",
			sortable : true,
			dataIndex : 'id.subId'
		}, {
			header : "�����ʶ",
			sortable : true,
			dataIndex : 'id.serviceId'
		}, {
			header : "��Ʒ��ʶ",
			sortable : true,
			dataIndex : 'productId'
		}, {
			header : "��������ֵ",
			sortable : true,
			dataIndex : 'serviceValue'
		}, {
			header : "��������",
			sortable : true,
			dataIndex : 'createDate'
		}, {
			header : "������",
			sortable : true,
			dataIndex : 'id.doneCode'
		}, {
			header : "��������",
			sortable : true,
			dataIndex : 'doneDate'
		}, {
			header : "��Ч����",
			sortable : true,
			dataIndex : 'validDate'
		}, {
			header : "ʧЧ����",
			sortable : true,
			dataIndex : 'expireDate'
		}, {
			header : "��ע",
			sortable : true,
			dataIndex : 'notes'
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
		title : "�ͻ�������Ϣ",
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

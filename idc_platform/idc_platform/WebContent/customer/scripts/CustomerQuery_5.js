//�ͻ����Ѽ�¼�б�
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
		title : '�ͻ����Ѽ�¼�б�',
		store : store,
		loadMask : true,
		frame:true,
        border:true,
		viewConfig : {
			forceFit : true
		},
		cm : new Ext.grid.ColumnModel([
				
				{
					header : "�˵���",
					sortable : true,
					dataIndex : 'id.billMonth'
				},{
					header : "�ͻ����",
					sortable : true,
					dataIndex : 'id.customerId'
				},
				{
					header : "�˻�����",
					sortable : true,
					dataIndex : 'id.accId'
				},{
					header : "������",
					sortable : true,
					dataIndex : 'id.serviceId'
				},{
					header : "�������",
					sortable : true,
					dataIndex : 'id.subId'
				},{
					header : "������ʶ",
					sortable : true,
					dataIndex : 'payFlag'
				},
				{
					header : "����",
					sortable : true,
					dataIndex : 'totalFee'
				}]),
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
		title : "�ͻ����Ѽ�¼",
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
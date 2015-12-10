//��ϵ���б�
var gridPanel_2;
function cusQuery_2() {
	var store = new Ext.data.Store({
		baseParams : {
			"id" : -1,
			ajax : true
		},
		proxy : new Ext.data.HttpProxy({
			url : pageConst.queryContactUrl
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'result'
		}, [ {
			name : 'id',
			mapping : 'id',
			type : 'int'
		}, {
			name : 'contactName',
			mapping : 'contactName',
			type : 'string'
		}, {
			name : 'phone',
			mapping : 'phone',
			type : 'string'
		}, {
			name : 'mobile',
			mapping : 'mobile',
			type : 'string'
		}, {
			name : 'position',
			mapping : 'position',
			type : 'string'
		},{
			name : 'email',
			mapping : 'email',
			type : 'string'
		},{
			name : 'address',
			mapping : 'address',
			type : 'string'
		} ])
	});
	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			id : Ext.getCmp("bossid").getValue()
		});
	});

	gridPanel_2 = new Ext.grid.GridPanel({
		id : 'grid2',
		title : '�ͻ���ϵ���б�',
		store : store,
		layout:'fit',
		loadMask : true,
		frame:true,
        border:true,
        viewConfig : {
			forceFit : true
		},
		cm : new Ext.grid.ColumnModel([
				{
					header : "��ϵ�˱��",
					sortable : true,
					dataIndex : 'id'
				},
				{
					header : "����",
					sortable : true,
					dataIndex : 'contactName'
				},
				
				{
					header : "�绰",
					sortable : true,
					dataIndex : 'phone'
				},
				{
					header : "�ֻ�",
					sortable : true,
					dataIndex : 'mobile'
				},
				
				{
					header : "ְλ",
					sortable : true,
					dataIndex : 'position'
				},
				{
					header : "Email",
					sortable : true,
					dataIndex : 'email'
				},
				{
					header : "��ַ",
					sortable : true,
					dataIndex : 'address'
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
	
	var tabPage = tabPanel.add({
		title : "�ͻ���ϵ����Ϣ",
		// closable:true
		//frame:true,
		layout : "fit",
		items : [ gridPanel_2 ]
	});
	tabPanel.setActiveTab(tabPage);
	Ext.getCmp('grid2').store.reload({
		params : {
			start : 0,
			limit : 20
		}
	});
}

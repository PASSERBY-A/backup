//��ϵ���б�
var gridPanel_2;
function customerQuery_2() {
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
			type : 'long'
		}, {
			name : 'contactName',
			mapping : 'contactName',
			type : 'string'
		},{
			name : 'cardType',
			mapping : 'cardType',
			type : 'string'
		}, {
			name : 'cardId',
			mapping : 'cardId',
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
			name : 'homePhone',
			mapping : 'homePhone',
			type : 'string'
		}, {
			name : 'officePhone',
			mapping : 'officePhone',
			type : 'string'
		},{
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
		},{
			name : 'postcard',
			mapping : 'postcard',
			type : 'long'
		} ])
	});
	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			//id : Ext.getCmp("name").getValue()
		});
	});

	gridPanel_2 = new Ext.grid.GridPanel({
		id : 'grid',
		title : '�ͻ���ϵ���б�',
		store : store,
		loadMask : true,
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
					header : "֤������",
					sortable : true,
					dataIndex : 'cardType',
					renderer : function(data, metadata, record, rowIndex,
							columnIndex, store) {
						var value = record.get('cardType');
						if (value == 0)
							return "Ӫҵִ��";
						if (value == 1)
							return "���֤";
						if (value == 2)
							return "����";
						if (value == 3)
							return "����֤";
						if (value == 4)
							return "�籣��";
						if (value == 5)
							return "��ҵ����֤";
						if (value == 6)
							return "���̵Ǽ�֤";
						if (value == 7)
							return "ͼ��֤";
						if (value == 8)
							return "��ʻ֤";
						if (value == 9)
							return "����֤��";

					}
				}, {
					header : "֤������",
					sortable : true,
					dataIndex : 'cardId'
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
					header : "��ͥ�绰",
					sortable : true,
					dataIndex : 'homePhone'
				},
				{
					header : "�칫�绰",
					sortable : true,
					dataIndex : 'officePhone'
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
				},
				{
					header : "�ʱ�",
					sortable : true,
					dataIndex : 'postcard'
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
		title : "�ͻ���ϵ����Ϣ",
		height : 568,
		// closable:true,
		layout : "fit",
		items : [ gridPanel_2 ]
	});

}

//联系人列表
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
		title : '客户联系人列表',
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
					header : "联系人编号",
					sortable : true,
					dataIndex : 'id'
				},
				{
					header : "姓名",
					sortable : true,
					dataIndex : 'contactName'
				},
				
				{
					header : "电话",
					sortable : true,
					dataIndex : 'phone'
				},
				{
					header : "手机",
					sortable : true,
					dataIndex : 'mobile'
				},
				
				{
					header : "职位",
					sortable : true,
					dataIndex : 'position'
				},
				{
					header : "Email",
					sortable : true,
					dataIndex : 'email'
				},
				{
					header : "地址",
					sortable : true,
					dataIndex : 'address'
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
	
	var tabPage = tabPanel.add({
		title : "客户联系人信息",
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

function userQueryFn2() {
	// 记录类型
	var user = Ext.data.Record.create([ {
		name : "tid",
		mapping : "tid"
	}, {
		name : "updateTime",
		mapping : "updateTime"
	}, {
		name : "taskUser",
		mapping : "taskUser"
	}, {
		name : "serviceType",
		mapping : "serviceType"
	}, {
		name : "processresult",
		mapping : "processresult"
	}, {
		name : "excuteUser",
		mapping : "excuteUser"
	} ]);

	// 存储器
	var store = new Ext.data.Store({
		url : "../getCustomerQingdanCountReport.action",
		reader : new Ext.data.JsonReader({
			root : "results",
			totalProperty : "recordSize"
		}, user)
	});

	// 开始时间
	var startTime1 = new Ext.form.DateField({
		fieldLabel : '开始时间',
		ReadOnly : true,// 禁止手工修改
		// Editable:false,
		emptyText: new Date().dateFormat('Y-m-d'),
		id : 'b_date1',
		name : 'bDate1',
		// value: new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});

	// 结束时间
	var endTime1 = new Ext.form.DateField({
		fieldLabel : '结束时间',
		editable : false,// 禁止手工修改
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'e_date1',
		name : 'eDate1',
		//value : new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});

	startTime1.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	endTime1.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	// 客户服务清单报告
	var gridPanel = new Ext.grid.GridPanel(
			{
				width : 300,
				height : 200,
				frame : true,
				store : store,
				columns : [ {
					header : "序号",
					width : 60,
					dataIndex : "tid",
					sortable : true,
					align : 'center'
				}, {
					header : "处理时间",
					width : 100,
					dataIndex : "updateTime",
					sortable : true,
					align : 'center'
				}, {
					header : "客户名称",
					width : 260,
					dataIndex : "taskUser",
					sortable : true,
					align : 'center'
				}, {
					header : "处理内容",
					width : 320,
					dataIndex : "serviceType",
					sortable : true,
					align : 'center'
				}, {
					header : "处理结果",
					width : 160,
					dataIndex : "processresult",
					sortable : true,
					align : 'center'
				}, {
					header : "处理人",
					width : 160,
					dataIndex : "excuteUser",
					sortable : true,
					align : 'center'
				} ],
				autoExpandColumn : 1,
				// 分页控制条
				bbar : new Ext.PagingToolbar({
					displayMsg : '第{0} 到 {1} 条数据 共{2}条',
					emptyMsg : "没有数据",
					pageSize : 10,
					store : store,
					displayInfo : true
				}),
				selModel : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
				tbar : [
						"开始时间  :",
						startTime1,
						"结束时间  :",
						endTime1,
						"  ",
						{
							id : 'search',
							tooltip : '点击搜索',
							iconCls : 'icon-search',
							handler : function() {
								var startTime = Ext.get("b_date1").dom.value;
								var endTime = Ext.get("e_date1").dom.value;
								if (startTime == "开始时间" || endTime == "结束时间") {
									alert("开始时间或者结束时间不能为空！");
									return false;
								} else if (startTime > endTime) {
									alert("开始时间不能大于结束时间！");
								} else {
									store.load({
										params : {
											start : 0,
											limit : 10,
											startTime : startTime,
											endTime : endTime
										}
									});
								}
							}
						},
						'-',
						{
							xtype : 'tbbutton',
							text : '导出统计结果',
							iconCls : 'icon-add',
							handler : function() {
								var bd = Ext.get("b_date1").dom.value;
								var ed = Ext.get("e_date1").dom.value;
								// var ed = new
								// Date(Ext.getCmp('e_date1').getValue());
								var form = Ext.DomHelper
										.append(
												Ext.getBody(),
												'<form action="../exportCustomerQingdanCountReport.action"><input id="beginDate" name="beginDate" type="hidden" value="'
														+ bd
														+ '"/><input id="endDate" name="endDate" type="hidden" value="'
														+ ed + '"/></form>',
												true);
								form.dom.submit();
								form.remove();
								form = null;
							}
						} ]
			});
	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			startTime : (Ext.get("b_date1") == null ? null
					: Ext.get("b_date1").dom.value),
			endTime : (Ext.get("e_date1") == null ? null
					: Ext.get("e_date1").dom.value)
		});
	});
	store.load({
		params : {
			start : 0,
			limit : 10
		}
	});
	if (!userQueryPageIsOpen2) {
		var tabPage = tabPanel.add({
			id:'tab_2',
			title : "客户服务清单报告",
			height : 527,
			closable : true,
			layout : "fit",
			items : [ gridPanel ],
			listeners : {
				beforedestroy : function() {
					userQueryPageIsOpen2 = false;
				}
			}
		});
		tabPanel.setActiveTab(tabPage);
		userQueryPageIsOpen2 = true;
	}else{
		var n_tab = tabPanel.getComponent('tab_2');
		tabPanel.setActiveTab(n_tab);
		userQueryPageIsOpen2 = true;
	}
}
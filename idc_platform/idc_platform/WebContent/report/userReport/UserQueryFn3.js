function userQueryFn3() {
	// 记录类型
	var user = Ext.data.Record.create([ {
		name : "cid",
		mapping : "cid"
	}, {
		name : "cname",
		mapping : "cname"
	}, {
		name : "status",
		mapping : "status"
	} ]);

	// 存储器
	var store = new Ext.data.Store({
		url : "../getCustomerBiandongmingxiCountReport.action",
		reader : new Ext.data.JsonReader({
			root : "results",
			totalProperty : "recordSize"
		}, user)
	});

	// 开始时间
	var startTime2 = new Ext.form.DateField({
		fieldLabel : '开始时间',
		ReadOnly : true,// 禁止手工修改
		// Editable:false,
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'b_date2',
		name : 'bDate2',
		// value: new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});

	// 结束时间
	var endTime2 = new Ext.form.DateField({
		fieldLabel : '结束时间',
		editable : false,// 禁止手工修改
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'e_date2',
		name : 'eDate2',
		// value: new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});

	startTime2.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	endTime2.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	// 客户变动明细报告
	// dataIndex要，否则记录都显示不出来。
	var gridPanel = new Ext.grid.GridPanel(
			{
				width : 300,
				height : 200,
				frame : true,
				store : store,
				columns : [ {
					header : "客户编号",
					width : 160,
					dataIndex : "cid",
					sortable : true,
					align : 'center'
				}, {
					header : "客户名称",
					width : 160,
					dataIndex : "cname",
					sortable : true,
					align : 'center'
				}, {
					header : "客户类别",
					width : 160,
					dataIndex : "status",
					sortable : true,
					align : 'center'
				}, {
					header : "",
					width : 700,
					dataIndex : "no"
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
					singleSelect : false
				}),
				tbar : [

						"开始时间  :",
						startTime2,
						"结束时间  :",
						endTime2,
						"  ",
						{
							id : 'search',
							tooltip : '点击搜索',
							iconCls : 'icon-search',
							handler : function() {
								var startTime = Ext.get("b_date2").dom.value;
								var endTime = Ext.get("e_date2").dom.value;
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
								var bd = Ext.get("b_date2").dom.value;
								var ed = Ext.get("e_date2").dom.value;
								// if(bd=='NaN-NaN-NaN'||ed=='NaN-NaN-NaN'){
								// alert("请输入正确的日期");
								// return false;
								// }
								var form = Ext.DomHelper
										.append(
												Ext.getBody(),
												'<form action="../exportCustomerBiandongmingxiCountReport.action"><input id="beginDate" name="beginDate" type="hidden" value="'
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
			startTime : (Ext.get("b_date2") == null ? null
					: Ext.get("b_date2").dom.value),
			endTime : (Ext.get("e_date2") == null ? null
					: Ext.get("e_date2").dom.value)
		});
	});
	store.load({
		params : {
			start : 0,
			limit : 10
		}
	});

	if (!userQueryPageIsOpen3) {
		var tabPage = tabPanel.add({
			id : 'tab_3',
			title : "客户变动明细报告",
			height : 527,
			closable : true,
			layout : "fit",
			items : [ gridPanel ],
			listeners : {
				beforedestroy : function() {
					userQueryPageIsOpen3 = false;
				}
			}
		});
		tabPanel.setActiveTab(tabPage);
		userQueryPageIsOpen3 = true;
	} else {
		var n_tab = tabPanel.getComponent('tab_3');
		tabPanel.setActiveTab(n_tab);
		userQueryPageIsOpen3 = true;
	}
}
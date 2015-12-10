function businessQueryFn3() {
	// 基本业务信息统计报告

	// 记录类型
	var record = Ext.data.Record.create([ {
		name : "category",
		mapping : "category"
	}, {
		name : "total_num",
		mapping : "total_num"
	} ]);

	// 存储器
	var store = new Ext.data.Store({
		url : "../getBasicBussnessInformationReport.action",
		reader : new Ext.data.JsonReader({
			root : "results",
			totalProperty : "recordSize"

		}, record)
	});

	// 开始时间
	var startTime3 = new Ext.form.DateField({
		fieldLabel : '开始时间',
		editable : false,
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'b_date3',
		name : 'bDate3',
		// value: new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});

	// 结束时间
	var endTime3 = new Ext.form.DateField({
		fieldLabel : '结束时间',
		editable : false,
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'e_date3',
		name : 'eDate3',
		// value: new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});
	// 渲染的时候设置为不能编辑
	startTime3.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	endTime3.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	// 基本业务信息统计报告
	var gridPanel = new Ext.grid.GridPanel(
			{
				width : 500,
				height : 500,
				frame : true,
				region : 'west',
				store : store,
				columns : [ {
					header : "业务类别",
					width : 100,
					dataIndex : "category",
					sortable : true,
					align : 'center'
				}, {
					header : "数量",
					width : 100,
					dataIndex : "total_num",
					sortable : true,
					align : 'center'
				}],
				//autoExpandColumn : 1,
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
						startTime3,
						"结束时间  :",
						endTime3,
						{
							xtype : 'textfield',
							id : 'path',
							name : 'path',
							mapping : 'path',
							hidden : true,
							hideLabel : true
						},
						"  ",
						{
							id : 'search',
							tooltip : '点击搜索',
							iconCls : 'icon-search',
							handler : function() {
								var startTime = Ext.get("b_date3").dom.value;
								var endTime = Ext.get("e_date3").dom.value;
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
									chartPanel3.load({
										params : {
											start : 0,
											limit : 10,
											startTime : startTime,
											endTime : endTime
										},
										url : '../generateB3Chart.action',
										scripts : true,
										text : "Loading...",
										timeout : 30
									});
									// Ext.get("myphoto3").dom.src =
									// '../style/temp1.jpeg?'
									// + (new Date()).getTime();

								}
							}
						},
						
						'-',
						{
							xtype : 'tbbutton',
							text : '导出统计结果',
							iconCls : 'icon-add',
							handler : function() {
								var bd = Ext.get("b_date3").dom.value;
								var ed = Ext.get("e_date3").dom.value;
								// if(bd=='NaN-NaN'||ed=='NaN-NaN'){
								// alert("请输入正确的日期");
								// return false;
								// }
								var form = Ext.DomHelper
										.append(
												Ext.getBody(),
												'<form action="../exportBasicBussnessInformationReport.action"><input id="beginDate" name="beginDate" type="hidden" value="'
														+ bd
														+ '"/><input id="endDate" name="endDate" type="hidden" value="'
														+ ed + '"/></form>',
												true);
								form.dom.submit();
								form.remove();
								form = null;
								// Ext.get("myphoto2").dom.src =
								// '../style/temp1.jpeg' ;
							}
						} ]
			});

	store.on('beforeload', function(s, options) {
		Ext.apply(s.baseParams, {
			startTime : (Ext.get("b_date3") == null ? null
					: Ext.get("b_date3").dom.value),
			endTime : (Ext.get("e_date3") == null ? null
					: Ext.get("e_date3").dom.value)
		});
	});

	store.load({
		params : {
			start : 0,
			limit : 10
		}
	});

	// var picPanel = new Ext.Panel({
	// //id : 'myphoto2',
	// title : "hello",
	// layout : 'fit',
	// items:[]
	//		
	// });

	var chartPanel3 = new Ext.Panel({
		id : 'chartPanel3',
		// height : 430,
		region : 'center',
		border : false,
		autoLoad : {
			url : '../test.jsp'
		}
	});
	if (!businessQueryPageIsOpen3) {
		var tabPage = tabPanel.add({
			id : 'btab_3',
			title : "基本业务信息统计报告",
			height : 527,
			closable : true,
			layout : "border",
			items : [ gridPanel, chartPanel3
			// {
			// //title:'Hell',
			// xtype : 'box', // 或者xtype: 'component',
			// region:'center',
			// id : 'myphoto3',
			// name : 'myphoto3',
			// width : 80, // 图片宽度
			// height : 200, // 图片高度
			// autoEl : {
			// tag : 'img', // 指定为img标签
			// src : '../style/index.png'
			// }
			// }
			],
			listeners : {
				beforedestroy : function() {
					businessQueryPageIsOpen3 = false;
				}
			}
		});
		tabPanel.setActiveTab(tabPage);
		businessQueryPageIsOpen3 = true;
	} else {
		var n_tab = tabPanel.getComponent('btab_3');
		tabPanel.setActiveTab(n_tab);
		businessQueryPageIsOpen3 = true;
	}
}
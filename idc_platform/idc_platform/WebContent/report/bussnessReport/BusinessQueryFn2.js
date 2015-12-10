function businessQueryFn2() {
	// 记录类型
	var record = Ext.data.Record.create([ {
		name : "create_month",
		mapping : "create_month"
	}, {
		name : "total_num",
		mapping : "total_num"
	}, {
		name : "add_num",
		mapping : "add_num"
	}, {
		name : "update_num",
		mapping : "update_num"
	}, {
		name : "end_num",
		mapping : "end_num"
	} ]);

	// 存储器
	var store = new Ext.data.Store({
		url : "../getBussnessChangeCountReport.action",
		reader : new Ext.data.JsonReader({
			root : "results",
			totalProperty : "recordSize"
		}, record)
	});

	// 开始时间
	var startTime2 = new Ext.ux.MonthField({
		fieldLabel : '开始时间',
		id : 'b_date2',
		name : 'bDate2',
		emptyText : new Date().dateFormat('Y'),
		// value: new Date().dateFormat('Y-m'),
		format : 'Y',
		width : 90
	});

	// 结束时间
	var endTime2 = new Ext.ux.MonthField({
		fieldLabel : '结束时间',
		// editable:false,
		// readonly:true,
		id : 'e_date2',
		name : 'eDate2',
		emptyText : new Date().dateFormat('Y'),
		// value: new Date().dateFormat('Y-m'),
		format : 'Y',
		width : 90
	});

	// 渲染的时候设置为不能编辑
	startTime2.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y'));
		df.setEditable(false);
	});

	// endTime2.on('render', function(df){
	// df.setValue(new Date().dateFormat('Y'));
	// df.setEditable(false);
	// });

	// 业务变动统计报告
	var gridPanel = new Ext.grid.GridPanel(
			{
				width : 500,
				height : 500,
				frame : true,
				store : store,
				loadMask : true,
				split : true,
				region : 'west',
				columns : [ {
					header : "时间",
					width : 50,
					dataIndex : "create_month",
					sortable : true,
					align : 'center'
				}, {
					header : "业务总量",
					width : 80,
					dataIndex : "total_num",
					sortable : true,
					align : 'center'
				}, {
					header : "新增业务",
					width : 80,
					dataIndex : "add_num",
					sortable : true,
					align : 'center'
				}, {
					header : "变更业务",
					width : 80,
					dataIndex : "update_num",
					sortable : true,
					align : 'center'
				}, {
					header : "终止业务",
					width : 80,
					dataIndex : "end_num",
					sortable : true,
					align : 'center'
				}

				],
				// autoExpandColumn:1,
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

						"选择年份  :",
						startTime2,
						"  ",
						{
							id : 'search',
							tooltip : '点击搜索',
							iconCls : 'icon-search',
							handler : function() {

								var startTime = Ext.get("b_date2").dom.value;
								// var endTime = Ext.get("e_date2").dom.value;
								// if (startTime != null && endTime != null) {
								// if (startTime > endTime) {
								// alert("开始时间不能大于结束时间！");
								// //return;
								// }
								// var time1 = new Date(startTime);
								// var time2 = new Date(endTime);
								// if ((time2 - time1) > 1000 * 60 * 60 * 24
								// * 366) {
								// alert("开始时间与结束时间间隔不能超过一年！");
								// }
								// else {
								// store.load({
								// params : {
								// start : 0,
								// limit : 10,
								// startTime : startTime,
								// endTime : endTime
								// }
								// });
								// }
								//
								// }
								store.load({
									params : {
										start : 0,
										limit : 10,
										startTime : startTime
									}
								});
								chartPanel2.load({
									params : {
										start : 0,
										limit : 10,
										startTime : startTime

									},
									url : '../generateB2Chart.action',
									scripts : true,
									text : "Loading...",
									timeout : 30
								});
								// Ext.get("myphoto2").dom.src =
								// '../style/temp1.jpeg?'
								// + (new Date()).getTime();

							}
						},
						'-',
						{
							xtype : 'tbbutton',
							text : '导出统计结果',
							iconCls : 'icon-add',
							handler : function() {
								var bd = Ext.get("b_date2").dom.value;
								var ed = null;
								var form = Ext.DomHelper
										.append(
												Ext.getBody(),
												'<form action="../exportBussnessChangeCountReport.action"><input id="beginDate" name="beginDate" type="hidden" value="'
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
			startTime : Ext.get("b_date2") == null ? null
					: Ext.get("b_date2").dom.value
		});
	});
	store.load({
		params : {
			start : 0,
			limit : 10
		}
	});

	var chartPanel2 = new Ext.Panel({
		id : 'chartPanel2',
		// height : 430,
		region : 'center',
		border : false,
		autoLoad : {
			url : '../test.jsp'
		}
	});
	if (!businessQueryPageIsOpen2) {
		var tabPage = tabPanel.add({
			id : 'btab_2',
			title : "业务变动统计报告",
			height : 527,
			closable : true,
			layout : "border",
			items : [ gridPanel, chartPanel2
			// {xtype:'panel',
			// region:"center",
			// html:'<img src="../style/temp1.jpeg">'
			// }

			// {
			// xtype: 'box', //或者xtype: 'component',
			// id:'myphoto2',
			// name:'myphoto2',
			// region:'center',
			// width: 80, //图片宽度
			// height: 200, //图片高度
			// autoEl: {
			// tag: 'img', //指定为img标签
			// src: '../style/index.png' //指定url路径
			// }
			// }

			],
			listeners : {
				beforedestroy : function() {
					businessQueryPageIsOpen2 = false;
				}
			}
		});
		tabPanel.setActiveTab(tabPage);
		businessQueryPageIsOpen2 = true;
	} else {
		var n_tab = tabPanel.getComponent('btab_2');
		tabPanel.setActiveTab(n_tab);
		businessQueryPageIsOpen2 = true;
	}
}
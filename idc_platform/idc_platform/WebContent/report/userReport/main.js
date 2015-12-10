Ext.BLANK_IMAGE_URL = "/Ext/resources/images/default/s.gif";
var tabPanel = new Ext.TabPanel({
	activeTab : 0,// 默认激活第一个tab页
	animScroll : true,// 使用动画滚动效果
	enableTabScroll : true,// tab标签超宽时自动出现滚动按钮
	region : 'center',
	listeners : {
		"contextmenu" : function(tabPanel, myitem, e) {
			var menu = new Ext.menu.Menu([ {
				text : "关闭当前选项页",
				icon : "../style/images/cancel.gif",
				handler : function() {
					if (myitem != tabPanel.getItem(0)) {
						tabPanel.remove(myitem)
					}
				}
			}, {
				text : "关闭其他所有选项页",
				icon : "../style/images/cancel.gif",
				handler : function() {
					tabPanel.items.each(function(item) {
						if (item != myitem && item != tabPanel.getItem(0)) {
							tabPanel.remove(item);
						}
					});
				}
			} ]);
			menu.showAt(e.getPoint());
		}
	}
});

// 记录类型
var user = Ext.data.Record.create([ {
	name : "serviceType",
	mapping : "serviceType"
}, {
	name : "shoulicount",
	mapping : "shoulicount"
}, {
	name : "wanchengcount",
	mapping : "wanchengcount"
} ]);

// 开始时间
var startTime = new Ext.form.DateField({
	fieldLabel : '开始时间',
	emptyText : new Date().dateFormat('Y-m-d'),
	// value: new Date().dateFormat('Y-m-d'),
	id : 'b_date',
	name : 'bDate',
	format : 'Y-m-d',
	width : 90
});

// 结束时间
var endTime = new Ext.form.DateField({
	fieldLabel : '结束时间',
	emptyText : new Date().dateFormat('Y-m-d'),
	id : 'e_date',
	name : 'eDate',
	// value: new Date().dateFormat('Y-m-d'),
	format : 'Y-m-d',
	width : 90
});
// 渲染的时候设置为不能编辑
startTime.on('render', function(df) {
	df.setValue(new Date().dateFormat('Y-m-d'));
	df.setEditable(false);
});

endTime.on('render', function(df) {
	df.setValue(new Date().dateFormat('Y-m-d'));
	df.setEditable(false);
});

// 存储器
var store = new Ext.data.Store({
	baseParams : {

	},
	url : "../getCustomerFuwuCountReport.action",
	reader : new Ext.data.JsonReader({
		root : "results",
		totalProperty : "recordSize"
	}, user)
});

// 客户服务统计报告
var gridPanel = new Ext.grid.GridPanel(
		{
			width : 500,
			height : 500,
			frame : true,
			region : 'west',
			store : store,
			columns : [ {
				header : "服务类别",
				width : 180,
				dataIndex : "serviceType",
				sortable : true,
				align : 'center'
			}, {
				header : "受理数量",
				width : 80,
				dataIndex : "shoulicount",
				sortable : true,
				align : 'center'
			}, {
				header : "完成数量",
				width : 80,
				dataIndex : "wanchengcount",
				sortable : true,
				align : 'center'
			}],
			//autoExpandColumn : 1,

			selModel : new Ext.grid.RowSelectionModel({
				singleSelect : false
			}),
			tbar : [
					"开始时间  :",
					startTime,
					"结束时间  :",
					endTime,
					"  ",
					{
						id : 'search',
						tooltip : '点击搜索',
						iconCls : 'icon-search',
						handler : function() {
							var startTime = Ext.get("b_date").dom.value;
							var endTime = Ext.get("e_date").dom.value;

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
								chartPanel.load({
									params : {
										start : 0,
										limit : 10,
										startTime : startTime,
										endTime : endTime
									},
									url : '../generateU1Chart.action',
									scripts : true,
									text : "Loading...",
									timeout : 30
								});
								// Ext.get("myphoto1").dom.src =
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
							var bd = Ext.get("b_date").dom.value;
							var ed = Ext.get("e_date").dom.value;
							var form = Ext.DomHelper
									.append(
											Ext.getBody(),
											'<form action="../exportCustomerFuwuCountReport.action"><input id="beginDate" name="beginDate" type="hidden" value="'
													+ bd
													+ '"/><input id="endDate" name="endDate" type="hidden" value="'
													+ ed + '"/></form>', true);
							form.dom.submit();
							form.remove();
							form = null;
						}
					} ],
			// 分页控制条
			bbar : new Ext.PagingToolbar({
				displayMsg : '第{0} 到 {1} 条数据 共{2}条',
				emptyMsg : "没有数据",
				pageSize : 10,
				store : store,
				displayInfo : true
			})
		});

store.on('beforeload', function(s, options) {
	Ext.apply(s.baseParams, {
		startTime : (Ext.get("b_date") == null ? null
				: Ext.get("b_date").dom.value),
		endTime : (Ext.get("e_date") == null ? null
				: Ext.get("e_date").dom.value)
	});

});

store.load({
	params : {
		start : 0,
		limit : 10,
		startTime : Ext.getCmp("b_date").getValue(),
		endTime : Ext.getCmp("e_date").getValue()
	}
});

var chartPanel = new Ext.Panel({
	id : 'chartPanel',
	// height : 430,
	region : 'center',
	border : false,
	autoLoad : {
		url : '../test.jsp'
	}
});

if (!userQueryPageIsOpen1) {
	var tabPage = tabPanel.add({
		id : 'tab_1',
		title : "客户服务统计报告",
		height : 527,
		closable : false,
		layout : "border",
		items : [ gridPanel, chartPanel
		// {
		// //title:'Hell',
		// xtype : 'box', // 或者xtype: 'component',
		// region:'center',
		// id : 'myphoto1',
		// name : 'myphoto1',
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
				userQueryPageIsOpen1 = false;
			}
		}
	});
	tabPanel.setActiveTab(tabPage);
	userQueryPageIsOpen1 = true;
} else {
	var n_tab = tabPanel.getComponent('tab_1');
	tabPanel.setActiveTab(n_tab);
	userQueryPageIsOpen1 = true;
}

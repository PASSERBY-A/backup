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

// 开始时间
var startTime = new Ext.form.DateField({
	fieldLabel : '开始时间',
	emptyText : new Date().dateFormat('Y-m-d'),
	// value:new Date().dateFormat('Y-m-d'),
	id : 'b_date',
	name : 'b_date',
	format : 'Y-m-d',
	width : 90
});

// 结束时间
var endTime = new Ext.form.DateField({
	fieldLabel : '结束时间',
	emptyText : new Date().dateFormat('Y-m-d'),
	// value: new Date().dateFormat('Y-m-d'),
	id : 'e_date',
	name : 'e_date',
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

// 记录类型
var order = Ext.data.Record.create([ {
	name : "category",
	mapping : "category"
}, {
	name : "total_num",
	mapping : "total_num"
}, {
	name : "finish_num",
	mapping : "finish_num"
}, {
	name : "over_time_num",
	mapping : "over_time_num"
} ]);

// 存储器
var store = new Ext.data.Store({
	baseParams : {
		ajax : true
	},
	proxy : new Ext.data.HttpProxy({
		url : "../getBussnessOrderCountReport.action"
	}),

	reader : new Ext.data.JsonReader({
		root : "results",
		totalProperty : "recordSize"
	}, order)
});

store.on('beforeload', function(s, options) {
	Ext.apply(s.baseParams, {
		startTime : (Ext.get("b_date") == null ? null
				: Ext.get("b_date").dom.value),
		endTime : (Ext.get("e_date") == null ? null
				: Ext.get("e_date").dom.value)
	});
});

// 业务工单统计报告
var gridPanel = new Ext.grid.GridPanel(
		{
			width : 500,
			height : 300,
			frame : true,
			region : 'west',
			store : store,
			loadMask : true,
			split : true,
			columns : [ {
				header : "业务类别",
				width : 120,
				dataIndex : "category",
				sortable : true,
				align : 'center'
			}, {
				header : "受理数量",
				width : 80,
				dataIndex : "total_num",
				sortable : true,
				align : 'center'
			}, {
				header : "完成数量",
				width : 80,
				dataIndex : "finish_num",
				sortable : true,
				align : 'center'
			}, {
				header : "超时",
				width : 80,
				dataIndex : "over_time_num",
				sortable : true,
				align : 'center'
			} ],
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
									url: '../generateB1Chart.action',
									scripts: true,
									text: "Loading...",
							    		timeout: 30
								});
								//document.picFrame.location.href="../generateB1Chart.action";
								//chartPanel.reload();
								//Ext.get("myphoto1").dom.src = '../generateB1Chart.action';

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
											'<form action="../exportBussnessOrderCountReport.action"><input id="beginDate" name="beginDate" type="hidden" value="'
													+ bd
													+ '"/><input id="endDate" name="endDate" type="hidden" value="'
													+ ed + '"/></form>', true);
							form.dom.submit();
							form.remove();
							form = null;
						}
					} ]
		});

store.load({
	params : {
		start : 0,
		limit : 10
	}
});
var picPanel = new Ext.Panel({
	region:'center',
	id:'picPanel',
	floating:true,
	html :  '<iframe name="picFrame" src="../generateB1Chart.action" width="100%" height="400" frameborder="0" scrolling="auto"></iframe>'

});

var chartPanel = new Ext.Panel({
	   id : 'chartPanel',
	//   height : 430,
	   region:'center',
	   border : false,
	   autoLoad : {
	    url : '../test.jsp'
	     }
	    }); 
if (!businessQueryPageIsOpen1) {
	var tabPage = tabPanel.add({
		id : 'btab_1',
		title : "业务工单统计报告",
		// height:400,
		closable : false,
		layout : "border",
		items : [ gridPanel, chartPanel ],
		listeners : {
			beforedestroy : function() {
				businessQueryPageIsOpen1 = false;
			}
		}
	});
//	picPanel.load({   
//		       url: '../chart.jsp',   
//		        scripts: true,   
//		        text: "Loading...",   
//		            timeout: 30  
//		    });  

	tabPanel.setActiveTab(tabPage);
	businessQueryPageIsOpen1 = true;
} else {
	var n_tab = tabPanel.getComponent('btab_1');
	tabPanel.setActiveTab(n_tab);
	businessQueryPageIsOpen1 = true;
}

Ext.BLANK_IMAGE_URL = "/Ext/resources/images/default/s.gif";
var tabPanel = new Ext.TabPanel({
	activeTab : 0,// Ĭ�ϼ����һ��tabҳ
	animScroll : true,// ʹ�ö�������Ч��
	enableTabScroll : true,// tab��ǩ����ʱ�Զ����ֹ�����ť
	region : 'center',
	listeners : {
		"contextmenu" : function(tabPanel, myitem, e) {
			var menu = new Ext.menu.Menu([ {
				text : "�رյ�ǰѡ��ҳ",
				icon : "../style/images/cancel.gif",
				handler : function() {
					if (myitem != tabPanel.getItem(0)) {
						tabPanel.remove(myitem)
					}
				}
			}, {
				text : "�ر���������ѡ��ҳ",
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

// ��¼����
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

// ��ʼʱ��
var startTime = new Ext.form.DateField({
	fieldLabel : '��ʼʱ��',
	emptyText : new Date().dateFormat('Y-m-d'),
	// value: new Date().dateFormat('Y-m-d'),
	id : 'b_date',
	name : 'bDate',
	format : 'Y-m-d',
	width : 90
});

// ����ʱ��
var endTime = new Ext.form.DateField({
	fieldLabel : '����ʱ��',
	emptyText : new Date().dateFormat('Y-m-d'),
	id : 'e_date',
	name : 'eDate',
	// value: new Date().dateFormat('Y-m-d'),
	format : 'Y-m-d',
	width : 90
});
// ��Ⱦ��ʱ������Ϊ���ܱ༭
startTime.on('render', function(df) {
	df.setValue(new Date().dateFormat('Y-m-d'));
	df.setEditable(false);
});

endTime.on('render', function(df) {
	df.setValue(new Date().dateFormat('Y-m-d'));
	df.setEditable(false);
});

// �洢��
var store = new Ext.data.Store({
	baseParams : {

	},
	url : "../getCustomerFuwuCountReport.action",
	reader : new Ext.data.JsonReader({
		root : "results",
		totalProperty : "recordSize"
	}, user)
});

// �ͻ�����ͳ�Ʊ���
var gridPanel = new Ext.grid.GridPanel(
		{
			width : 500,
			height : 500,
			frame : true,
			region : 'west',
			store : store,
			columns : [ {
				header : "�������",
				width : 180,
				dataIndex : "serviceType",
				sortable : true,
				align : 'center'
			}, {
				header : "��������",
				width : 80,
				dataIndex : "shoulicount",
				sortable : true,
				align : 'center'
			}, {
				header : "�������",
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
					"��ʼʱ��  :",
					startTime,
					"����ʱ��  :",
					endTime,
					"  ",
					{
						id : 'search',
						tooltip : '�������',
						iconCls : 'icon-search',
						handler : function() {
							var startTime = Ext.get("b_date").dom.value;
							var endTime = Ext.get("e_date").dom.value;

							if (startTime == "��ʼʱ��" || endTime == "����ʱ��") {
								alert("��ʼʱ����߽���ʱ�䲻��Ϊ�գ�");
								return false;
							} else if (startTime > endTime) {
								alert("��ʼʱ�䲻�ܴ��ڽ���ʱ�䣡");
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
						text : '����ͳ�ƽ��',
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
			// ��ҳ������
			bbar : new Ext.PagingToolbar({
				displayMsg : '��{0} �� {1} ������ ��{2}��',
				emptyMsg : "û������",
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
		title : "�ͻ�����ͳ�Ʊ���",
		height : 527,
		closable : false,
		layout : "border",
		items : [ gridPanel, chartPanel
		// {
		// //title:'Hell',
		// xtype : 'box', // ����xtype: 'component',
		// region:'center',
		// id : 'myphoto1',
		// name : 'myphoto1',
		// width : 80, // ͼƬ���
		// height : 200, // ͼƬ�߶�
		// autoEl : {
		// tag : 'img', // ָ��Ϊimg��ǩ
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

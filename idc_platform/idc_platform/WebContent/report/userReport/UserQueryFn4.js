function userQueryFn4() {
	// ��¼����
	var user = Ext.data.Record.create([ {
		name : "openTime",
		mapping : "openTime"
	}, {
		name : "customer_count",
		mapping : "customer_count"
	}, {
		name : "qianzai_count",
		mapping : "qianzai_count"
	}, {
		name : "zhuxiao_count",
		mapping : "zhuxiao_count"
	} ]);

	// �洢��
	var store = new Ext.data.Store({
		url : "../getCustomerBiandongtongjiCountReport.action",
		reader : new Ext.data.JsonReader({
			root : "results",
			totalProperty : "recordSize"
		}, user)
	});

	// ��ʼʱ��(ֻ��ʾ���¿ؼ�)
	var startTime3 = new Ext.ux.MonthField({
		fieldLabel : '��ʼʱ��',
		// ReadOnly:true,//��ֹ�ֹ��޸�
		// Editable:false,
		emptyText : new Date().dateFormat('Y'),
		id : 'b_date3',
		name : 'bDate3',
		// value: new Date().dateFormat('Y-m'),
		format : 'Y',
		width : 90
	});

	// // ����ʱ��
	// var endTime3 = new Ext.ux.MonthField({
	// fieldLabel : '����ʱ��',
	// // editable : false,// ��ֹ�ֹ��޸�
	// emptyText : new Date().dateFormat('Y-m'),
	// id : 'e_date3',
	// name : 'eDate3',
	// // value: new Date().dateFormat('Y-m'),
	// format : 'Y-m',
	// width : 90
	// });

	startTime3.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y'));
		df.setEditable(false);
	});

	// endTime3.on('render', function(df) {
	// df.setValue(new Date().dateFormat('Y-m'));
	// df.setEditable(false);
	// });

	// �ͻ��䶯ͳ�Ʊ���

	var gridPanel = new Ext.grid.EditorGridPanel(
			{
				width : 500,
				height : 500,
				frame : true,
				region : 'west',
				store : store,
				columns : [ {
					header : "ʱ��",
					width : 100,
					dataIndex : "openTime",
					sortable : true,
					align : 'center'
				}, {
					header : "�ͻ�����",
					width : 80,
					dataIndex : "customer_count",
					sortable : true,
					align : 'center'
				}, {
					header : "�����û�",
					width : 80,
					dataIndex : "qianzai_count",
					sortable : true,
					align : 'center'
				}, {
					header : "�����û�",
					width : 80,
					dataIndex : "zhuxiao_count",
					sortable : true,
					align : 'center'
				} ],
				//autoExpandColumn : 1,
				// ��ҳ������
				bbar : new Ext.PagingToolbar({
					displayMsg : '��{0} �� {1} ������ ��{2}��',
					emptyMsg : "û������",
					pageSize : 10,
					store : store,
					displayInfo : true
				}),
				selModel : new Ext.grid.RowSelectionModel({
					singleSelect : false
				}),
				tbar : [
						"��ʼ����  :",
						startTime3,

						{
							id : 'search',
							tooltip : '�������',
							iconCls : 'icon-search',
							handler : function() {

								var startTime = Ext.get("b_date3").dom.value;
								store.load({
									params : {
										start : 0,
										limit : 10,
										startTime : startTime,
										endTime : endTime
									}
								});
								chartPanel4.load({
									params : {
										start : 0,
										limit : 10,
										startTime : startTime,
										endTime : endTime
									},
									url : '../generateU4Chart.action',
									scripts : true,
									text : "Loading...",
									timeout : 30
								});
								// Ext.get("myphoto4").dom.src =
								// '../style/temp1.jpeg?'+ (new
								// Date()).getTime();
							}

						},
						'-',
						{
							xtype : 'tbbutton',
							text : '����ͳ�ƽ��',
							iconCls : 'icon-add',
							handler : function() {
								// var bd = new
								// Date(Ext.getCmp('b_date3').getValue());
								var bd = Ext.get("b_date3").dom.value;
								var ed = null;
								var form = Ext.DomHelper
										.append(
												Ext.getBody(),
												'<form action="../exportCustomerBiandongtongjiCountReport.action"><input id="beginDate" name="beginDate" type="hidden" value="'
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

	var chartPanel4 = new Ext.Panel({
		id : 'chartPanel4',
		// height : 430,
		region : 'center',
		border : false,
		autoLoad : {
			url : '../test.jsp'
		}
	});

	if (!userQueryPageIsOpen4) {
		var tabPage = tabPanel.add({
			id : 'tab_4',
			title : "�ͻ��䶯ͳ�Ʊ���",
			height : 522,
			closable : true,
			layout : "border",
			items : [ gridPanel, chartPanel4
			// {
			// //title:'Hell',
			// xtype : 'box', // ����xtype: 'component',
			// region:'center',
			// id : 'myphoto4',
			// name : 'myphoto4',
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
					userQueryPageIsOpen4 = false;
				}
			}
		});
		tabPanel.setActiveTab(tabPage);
		userQueryPageIsOpen4 = true;
	} else {
		var n_tab = tabPanel.getComponent('tab_4');
		tabPanel.setActiveTab(n_tab);
		userQueryPageIsOpen4 = true;
	}
}
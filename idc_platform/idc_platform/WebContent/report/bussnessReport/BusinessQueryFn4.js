function businessQueryFn4() {
	// ��¼����
	var record = Ext.data.Record.create([ {
		name : "category",
		mapping : "category"
	}, {
		name : "total_num",
		mapping : "total_num"
	} ]);

	// �洢��
	var store = new Ext.data.Store({
		url : "../getPriceBussnessInformationReport.action",
		reader : new Ext.data.JsonReader({
			root : "results",
			totalProperty : "recordSize"
		}, record)
	});

	// ��ʼʱ��
	var startTime4 = new Ext.form.DateField({
		fieldLabel : '��ʼʱ��',
		editable : false,
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'b_date4',
		name : 'bDate4',
		// value : new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});

	// ����ʱ��
	var endTime4 = new Ext.form.DateField({
		fieldLabel : '����ʱ��',
		editable : false,
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'e_date4',
		name : 'eDate4',
		// value : new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});
	// ��Ⱦ��ʱ������Ϊ���ܱ༭
	startTime4.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	endTime4.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	// ��ֵҵ����Ϣͳ�Ʊ���
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
					header : "ҵ�����",
					width : 150,
					dataIndex : "category",
					sortable : true,
					align : 'center'
				}, {
					header : "����",
					width : 100,
					dataIndex : "total_num",
					sortable : true,
					align : 'center'
				} ],
				//autoExpandColumn : 1,
				// ��ҳ������
				bbar : new Ext.PagingToolbar({
					displayMsg : '��{0} �� {1} ������ ��{2}��',
					emptyMsg : "û������",
					pageSize : 20,
					store : store,
					displayInfo : true
				}),
				selModel : new Ext.grid.RowSelectionModel({
					singleSelect : false
				}),
				tbar : [
						"��ʼʱ��  :",
						startTime4,
						"����ʱ��  :",
						endTime4,
						"  ",
						{
							id : 'search',
							tooltip : '�������',
							iconCls : 'icon-search',
							handler : function() {
								var startTime = Ext.get("b_date4").dom.value;
								var endTime = Ext.get("e_date4").dom.value;
								if (startTime == "��ʼʱ��" || endTime == "����ʱ��") {
									alert("��ʼʱ����߽���ʱ�䲻��Ϊ�գ�");
									return false;
								} else if (startTime > endTime) {
									alert("��ʼʱ�䲻�ܴ��ڽ���ʱ�䣡");
								} else {
									store.load({
										params : {
											start : 0,
											limit : 20,
											startTime : startTime,
											endTime : endTime
										}
									});
									chartPanel4.load({
										params : {
											start : 0,
											limit : 20,
											startTime : startTime,
											endTime : endTime
										},
										url : '../generateB4Chart.action',
										scripts : true,
										text : "Loading...",
										timeout : 30
									});
									// Ext.get("myphoto4").dom.src =
									// '../style/temp1.jpeg?'+ (new
									// Date()).getTime();
								}
							}
						},
						'-',
						{
							xtype : 'tbbutton',
							text : '����ͳ�ƽ��',
							iconCls : 'icon-add',
							handler : function() {
								var bd = Ext.get("b_date4").dom.value;
								var ed = Ext.get("e_date4").dom.value;
								var form = Ext.DomHelper
										.append(
												Ext.getBody(),
												'<form action="../exportPriceBussnessInformationReport.action"><input id="beginDate" name="beginDate" type="hidden" value="'
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
			startTime : (Ext.get("b_date4") == null ? null
					: Ext.get("b_date4").dom.value),
			endTime : (Ext.get("e_date4") == null ? null
					: Ext.get("e_date4").dom.value)
		});
	});
	store.load({
		params : {
			start : 0,
			limit : 20
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
	if (!businessQueryPageIsOpen4) {
		var tabPage = tabPanel.add({
			id : 'btab_4',
			title : "��ֵҵ����Ϣͳ�Ʊ���",
			height : 527,
			closable : true,
			layout : "border",
			items : [ gridPanel, chartPanel4
			// {
			// xtype : 'box', // ����xtype: 'component',
			// region : 'center',
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
					businessQueryPageIsOpen4 = false;
				}
			}
		});
		tabPanel.setActiveTab(tabPage);
		businessQueryPageIsOpen4 = true;
	} else {
		var n_tab = tabPanel.getComponent('btab_4');
		tabPanel.setActiveTab(n_tab);
		businessQueryPageIsOpen4 = true;
	}
}
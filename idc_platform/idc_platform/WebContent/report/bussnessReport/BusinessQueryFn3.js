function businessQueryFn3() {
	// ����ҵ����Ϣͳ�Ʊ���

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
		url : "../getBasicBussnessInformationReport.action",
		reader : new Ext.data.JsonReader({
			root : "results",
			totalProperty : "recordSize"

		}, record)
	});

	// ��ʼʱ��
	var startTime3 = new Ext.form.DateField({
		fieldLabel : '��ʼʱ��',
		editable : false,
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'b_date3',
		name : 'bDate3',
		// value: new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});

	// ����ʱ��
	var endTime3 = new Ext.form.DateField({
		fieldLabel : '����ʱ��',
		editable : false,
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'e_date3',
		name : 'eDate3',
		// value: new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});
	// ��Ⱦ��ʱ������Ϊ���ܱ༭
	startTime3.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	endTime3.on('render', function(df) {
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});

	// ����ҵ����Ϣͳ�Ʊ���
	var gridPanel = new Ext.grid.GridPanel(
			{
				width : 500,
				height : 500,
				frame : true,
				region : 'west',
				store : store,
				columns : [ {
					header : "ҵ�����",
					width : 100,
					dataIndex : "category",
					sortable : true,
					align : 'center'
				}, {
					header : "����",
					width : 100,
					dataIndex : "total_num",
					sortable : true,
					align : 'center'
				}],
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
						"��ʼʱ��  :",
						startTime3,
						"����ʱ��  :",
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
							tooltip : '�������',
							iconCls : 'icon-search',
							handler : function() {
								var startTime = Ext.get("b_date3").dom.value;
								var endTime = Ext.get("e_date3").dom.value;
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
							text : '����ͳ�ƽ��',
							iconCls : 'icon-add',
							handler : function() {
								var bd = Ext.get("b_date3").dom.value;
								var ed = Ext.get("e_date3").dom.value;
								// if(bd=='NaN-NaN'||ed=='NaN-NaN'){
								// alert("��������ȷ������");
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
			title : "����ҵ����Ϣͳ�Ʊ���",
			height : 527,
			closable : true,
			layout : "border",
			items : [ gridPanel, chartPanel3
			// {
			// //title:'Hell',
			// xtype : 'box', // ����xtype: 'component',
			// region:'center',
			// id : 'myphoto3',
			// name : 'myphoto3',
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
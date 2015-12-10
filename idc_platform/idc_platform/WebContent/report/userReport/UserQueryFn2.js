function userQueryFn2() {
	// ��¼����
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

	// �洢��
	var store = new Ext.data.Store({
		url : "../getCustomerQingdanCountReport.action",
		reader : new Ext.data.JsonReader({
			root : "results",
			totalProperty : "recordSize"
		}, user)
	});

	// ��ʼʱ��
	var startTime1 = new Ext.form.DateField({
		fieldLabel : '��ʼʱ��',
		ReadOnly : true,// ��ֹ�ֹ��޸�
		// Editable:false,
		emptyText: new Date().dateFormat('Y-m-d'),
		id : 'b_date1',
		name : 'bDate1',
		// value: new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});

	// ����ʱ��
	var endTime1 = new Ext.form.DateField({
		fieldLabel : '����ʱ��',
		editable : false,// ��ֹ�ֹ��޸�
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

	// �ͻ������嵥����
	var gridPanel = new Ext.grid.GridPanel(
			{
				width : 300,
				height : 200,
				frame : true,
				store : store,
				columns : [ {
					header : "���",
					width : 60,
					dataIndex : "tid",
					sortable : true,
					align : 'center'
				}, {
					header : "����ʱ��",
					width : 100,
					dataIndex : "updateTime",
					sortable : true,
					align : 'center'
				}, {
					header : "�ͻ�����",
					width : 260,
					dataIndex : "taskUser",
					sortable : true,
					align : 'center'
				}, {
					header : "��������",
					width : 320,
					dataIndex : "serviceType",
					sortable : true,
					align : 'center'
				}, {
					header : "������",
					width : 160,
					dataIndex : "processresult",
					sortable : true,
					align : 'center'
				}, {
					header : "������",
					width : 160,
					dataIndex : "excuteUser",
					sortable : true,
					align : 'center'
				} ],
				autoExpandColumn : 1,
				// ��ҳ������
				bbar : new Ext.PagingToolbar({
					displayMsg : '��{0} �� {1} ������ ��{2}��',
					emptyMsg : "û������",
					pageSize : 10,
					store : store,
					displayInfo : true
				}),
				selModel : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
				tbar : [
						"��ʼʱ��  :",
						startTime1,
						"����ʱ��  :",
						endTime1,
						"  ",
						{
							id : 'search',
							tooltip : '�������',
							iconCls : 'icon-search',
							handler : function() {
								var startTime = Ext.get("b_date1").dom.value;
								var endTime = Ext.get("e_date1").dom.value;
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
								}
							}
						},
						'-',
						{
							xtype : 'tbbutton',
							text : '����ͳ�ƽ��',
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
			title : "�ͻ������嵥����",
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
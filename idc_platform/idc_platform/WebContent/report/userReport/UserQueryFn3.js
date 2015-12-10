function userQueryFn3() {
	// ��¼����
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

	// �洢��
	var store = new Ext.data.Store({
		url : "../getCustomerBiandongmingxiCountReport.action",
		reader : new Ext.data.JsonReader({
			root : "results",
			totalProperty : "recordSize"
		}, user)
	});

	// ��ʼʱ��
	var startTime2 = new Ext.form.DateField({
		fieldLabel : '��ʼʱ��',
		ReadOnly : true,// ��ֹ�ֹ��޸�
		// Editable:false,
		emptyText : new Date().dateFormat('Y-m-d'),
		id : 'b_date2',
		name : 'bDate2',
		// value: new Date().dateFormat('Y-m-d'),
		format : 'Y-m-d',
		width : 90
	});

	// ����ʱ��
	var endTime2 = new Ext.form.DateField({
		fieldLabel : '����ʱ��',
		editable : false,// ��ֹ�ֹ��޸�
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

	// �ͻ��䶯��ϸ����
	// dataIndexҪ�������¼����ʾ��������
	var gridPanel = new Ext.grid.GridPanel(
			{
				width : 300,
				height : 200,
				frame : true,
				store : store,
				columns : [ {
					header : "�ͻ����",
					width : 160,
					dataIndex : "cid",
					sortable : true,
					align : 'center'
				}, {
					header : "�ͻ�����",
					width : 160,
					dataIndex : "cname",
					sortable : true,
					align : 'center'
				}, {
					header : "�ͻ����",
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
						startTime2,
						"����ʱ��  :",
						endTime2,
						"  ",
						{
							id : 'search',
							tooltip : '�������',
							iconCls : 'icon-search',
							handler : function() {
								var startTime = Ext.get("b_date2").dom.value;
								var endTime = Ext.get("e_date2").dom.value;
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
								var bd = Ext.get("b_date2").dom.value;
								var ed = Ext.get("e_date2").dom.value;
								// if(bd=='NaN-NaN-NaN'||ed=='NaN-NaN-NaN'){
								// alert("��������ȷ������");
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
			title : "�ͻ��䶯��ϸ����",
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
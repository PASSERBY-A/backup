var eventEditWin;
function showEventEditWin(id, op) {
	// Ext.Msg.alert(id);
	if (!eventEditWin) {
		eventEditWin = new Ext.Window({
			id : 'eventEditWin',
			title : op == 'add' ? "����" : "�༭" + "����֪ʶ��",
			layout : 'fit',
			closable : true,
			closeAction : 'hide',
			plain : true,
			modal : true,
			width : 500,
			height : 300,
			resizable : false,
			items : [ new Ext.form.FormPanel({
				id : 'eventForm',
				layout : 'form',
				frame : true,
				labelAlign : 'right',
				// autoLoad : true,
				items : [ {
					xtype : 'textfield',
					id : 'eventTitle',
					name : 'kbknowledge.title',
					allowBlank : false,
					fieldLabel : '����',
					maxLength : 150,
					blankText : '����Ϊ�գ�����д',
					regex : /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/
				}, {
					xtype : 'textfield',
					id : 'eventKeyWords',
					name : 'kbknowledge.keywords',
					fieldLabel : '�ؼ���',
					maxLength : 150,
					regex : /^[\u4e00-\u9fa5a-zA-Z0-9]+$/
				}, {
					xtype : 'textarea',
					id : 'eventDesc',
					name : 'kbknowledge.description',
					width : 350,
					height : 60,
					fieldLabel : '��������',
					maxLength : 900
				}, {
					xtype : 'textarea',
					id : 'eventSolution',
					name : 'kbknowledge.solution',
					width : 350,
					height : 60,
					fieldLabel : '�������',
					maxLength : 900
				}, new Ext.form.ComboBox({
					id : 'eventIsRetired',
					hiddenName : 'kbknowledge.isRetired',
					labelWidth : 80,
					width : 140,
					fieldLabel : '�Ƿ����',
					mode : 'local',
					// readOnly : true,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "value", "text" ],
						data : [ [ 0, "��" ], [ 1, "��" ] ]
					}),
					defaultValue : 0,
					valueField : "value",
					displayField : "text"
					
				}), {
					xtype : 'hidden',
					id : 'eventCatalog',
					name : 'kbknowledge.categoryId.id'
				}, {
					xtype : 'hidden',
					id : 'eventCreator',
					name : 'kbknowledge.creator'
				}, {
					xtype : 'hidden',
					id : 'eventCreateDate',
					name : 'createdate'
				}, {
					xtype : 'hidden',
					id : 'eventId',
					name : 'kbknowledge.id'
				} ]

			}) ],

			buttonAlign : 'center',
			buttons : [ {
				text : '�ύ',
				handler : function() {
					var form = Ext.getCmp('eventForm');
					Ext.MessageBox.wait("�ύ����...", "��ȴ�");
					if (checkValid()) {
						form.form.doAction('submit', {
							url : pageConst.saveKnowledgeUrl,
							params : {
								baseType : 1
							},
							method : 'POST',
							success : function(form, action) {
								Ext.MessageBox.hide();
								eventEditWin.hide();
								Ext.Msg.alert('�ɹ�', action.result.message);
								gridReload();

							},
							failure : function(form, action) {
								Ext.MessageBox.hide();
								Ext.Msg.alert('ʧ��', '����ʱ�����쳣��');
							}
						});
					}
					;
				}
			}, {
				text : 'ȡ��',
				handler : function() {
					Ext.getCmp('eventForm').getForm().reset();
					eventEditWin.hide();

				}
			} ],
			// �ر�ʱ�������
			listeners : {
				hide : function() {
					Ext.getCmp('eventForm').getForm().reset();
				}
			}
		});
	}
	eventEditWin.show();
	if (op == 'edit') {
		// �༭
		// var record = Ext.getCmp('grid').getSelectionModel().getSelected();
		// eventForm.getForm().loadRecord(record);
		var rows = Ext.getCmp("grid").getSelections();
		eventEditWin.setTitle("�༭����֪ʶ��");
		Ext.getCmp('eventTitle').setValue(rows[0].json.title);
		Ext.getCmp('eventKeyWords').setValue(rows[0].json.keywords);
		Ext.getCmp('eventDesc').setValue(rows[0].json.description);
		Ext.getCmp('eventSolution').setValue(rows[0].json.solution);

		Ext.getCmp('eventCatalog').setValue(rows[0].json.cateId);
		Ext.getCmp('eventCreator').setValue(rows[0].json.creatorId);
		Ext.getCmp('eventCreateDate').setValue(rows[0].json.createDate + '');
		Ext.getCmp('eventId').setValue(rows[0].json.id);
		Ext.getCmp('eventIsRetired').setValue(rows[0].json.isRetired);
	} else if (op == 'add') {
		eventEditWin.setTitle("��������֪ʶ��");
		// ����ʱ������idע��֪ʶ�����ֶ�
		var cataid = pageObj.selectNode.attributes.id;
		if (cataid != -1) {
			Ext.getCmp('eventIsRetired').setValue(0);
			Ext.getCmp('eventCatalog').setValue(cataid);
		}
	}

}

/** form������У��** */
function checkValid() {
	var title = Ext.getCmp('eventTitle').getValue();
	var keywords = Ext.getCmp('eventKeyWords').getValue();
	var eventDesc = Ext.getCmp('eventDesc').getValue();
	var eventSolution = Ext.getCmp('eventSolution').getValue();
	if (trim(title) == '') {
		Ext.Msg.alert('���ݲ�����', '��ȷ����д���������ݣ�');
		return false;
	} else if (!Ext.getCmp('eventTitle').isValid()) {
		Ext.Msg.alert('���ݲ���ȷ', '��ȷ����д��ȷ������!');
		return false;
	} else if (!Ext.getCmp('eventKeyWords').isValid()) {
		Ext.Msg.alert('���ݲ���ȷ', '�ؼ�������������������!');
		return false;
	}else {
		return true;
	}
}
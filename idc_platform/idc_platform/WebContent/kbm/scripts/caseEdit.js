var caseEditWin;
// ����֪ʶ��༭����
function showCaseEditWin(id,op) {
	if (!caseEditWin) {
		caseEditWin = new Ext.Window({
			id : 'caseEditWin',
			title : id != null ? '�༭' : '����' + '����֪ʶ��',
			layout : 'fit',
			closable : true,
			closeAction : 'hide',
			plain : true,
			modal : true,
			width : 500,
			height : 410,
			resizable : false,
			items : [ new Ext.form.FormPanel({
				id : 'caseForm',
				layout : 'form',
				frame : true,
				labelAlign : 'right',
				items : [ {
					xtype : 'textfield',
					id : 'caseTitle',
					name : 'kbknowledge.title',
					allowBlank : false,
					fieldLabel : '����',
					maxLength : 150,
					blankText : '����Ϊ�գ�����д',
					regex : /^[\u4e00-\u9fa5a-zA-Z0-9]+$/
				}, {
					xtype : 'textfield',
					id : 'caseKeyWords',
					name : 'kbknowledge.keywords',
					fieldLabel : '�ؼ���',
					maxLength : 150,
					regex : /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/
				}, {
					xtype : 'textfield',
					id : 'caseProfessionType',
					name : 'kbknowledge.professionType',
					fieldLabel : 'רҵ���',
					maxLength : 100
				}, {
					xtype : 'textfield',
					id : 'caseDeviceName',
					name : 'kbknowledge.deviceName',
					fieldLabel : '�豸����',
					maxLength : 100
				}, {
					xtype : 'textarea',
					id : 'caseDesc',
					name : 'kbknowledge.description',
					width : 300,
					height : 60,
					fieldLabel : '��������',
					maxLength : 900
				}, {
					xtype : 'textarea',
					id : 'caseSolution',
					name : 'kbknowledge.solution',
					width : 300,
					height : 60,
					fieldLabel : '���ۺͽ���',
					maxLength : 900
				}, {
					xtype : 'textarea',
					id : 'caseSolutionContent',
					name : 'kbknowledge.solutionContent',
					width : 300,
					height : 60,
					fieldLabel : '�ɽ���ľ���',
					maxLength : 900
				}, new Ext.form.ComboBox({
					id : 'caseIsRetired',
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
					valueField : "value",
					displayField : "text",
					defaultValue:0
						}), {
					xtype : 'hidden',
					id : 'caseCatalog',
					name : 'kbknowledge.categoryId.id'
				}, {
					xtype : 'hidden',
					id : 'caseCreator',
					name : 'kbknowledge.creator'
				}, {
					xtype : 'hidden',
					id : 'caseCreateDate',
					name : 'createdate'
				}, {
					xtype : 'hidden',
					id : 'caseId',
					name : 'kbknowledge.id'
				} ]
			}) ],
			buttonAlign : 'center',
			buttons : [ {
				text : '�ύ',
				handler : function() {
					var form = Ext.getCmp('caseForm');
					if (checkCaseValid()) {
						Ext.MessageBox.wait("�ύ����...", "��ȴ�");
						form.form.doAction('submit', {
							url : pageConst.saveKnowledgeUrl,
							params : {
								baseType : 2
							},
							method : 'POST',
							success : function(form, action) {
								Ext.MessageBox.hide();
								caseEditWin.hide();
								Ext.Msg.alert('�ɹ�', action.result.message);
								gridReload();
							},
							failure : function(form, action) {
								Ext.MessageBox.hide();
								Ext.Msg.alert('ʧ��', '����ʱ�����쳣��');
							}
						});
					} 
				}
			}, {
				text : 'ȡ��',
				handler : function() {
					Ext.getCmp('caseForm').getForm().reset();
					caseEditWin.hide();
				}
			} ],
			// �ر�ʱ�������
			listeners : {
				hide : function() {
					Ext.getCmp('caseForm').getForm().reset();
				}
			}
		});
	}
	caseEditWin.show();
	if (op == 'edit') {
		// �༭
		var rows = Ext.getCmp("grid").getSelections();
		caseEditWin.setTitle("�༭����֪ʶ��");
		Ext.getCmp('caseTitle').setValue(rows[0].json.title);
		Ext.getCmp('caseKeyWords').setValue(rows[0].json.keywords);
		Ext.getCmp('caseDesc').setValue(rows[0].json.description);
		Ext.getCmp('caseSolution').setValue(rows[0].json.solution);
		Ext.getCmp('caseProfessionType').setValue(rows[0].json.professionType);
		Ext.getCmp('caseDeviceName').setValue(rows[0].json.deviceName);
		Ext.getCmp('caseSolutionContent')
				.setValue(rows[0].json.solutionContent);
		Ext.getCmp('caseIsRetired').setValue(rows[0].json.isRetired);

		Ext.getCmp('caseCatalog').setValue(rows[0].json.cateId);
		Ext.getCmp('caseCreator').setValue(rows[0].json.creatorId);
		Ext.getCmp('caseCreateDate').setValue(rows[0].json.createDate+'');
		Ext.getCmp('caseId').setValue(rows[0].json.id);
		
	} else if(op == 'add'){
		caseEditWin.setTitle("��������֪ʶ��");
		var cataid = pageObj.selectNode.attributes.id;
		if (cataid != -2) {
			Ext.getCmp('caseCatalog').setValue(cataid);
			Ext.getCmp('caseIsRetired').setValue(0);
		}
	}

	function checkCaseValid() {
		var title = Ext.getCmp('caseTitle').getValue();
		var keywords = Ext.getCmp('caseKeyWords').getValue();
		if (trim(title) == '') {
			Ext.Msg.alert('���ݲ�����', '��ȷ����д���������ݣ�');
			return false;
		}else if(!Ext.getCmp('caseTitle').isValid()){
			Ext.Msg.alert('���ݲ���ȷ', '��ȷ����д��ȷ������!');
			return false;
		}else if(!Ext.getCmp('caseKeyWords').isValid()){
			Ext.Msg.alert('���ݲ���ȷ', '�ؼ�������������������!');
			return false;
		}
		return true;
	}
}
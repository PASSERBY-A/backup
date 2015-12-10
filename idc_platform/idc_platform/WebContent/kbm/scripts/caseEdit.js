var caseEditWin;
// 案例知识点编辑窗口
function showCaseEditWin(id,op) {
	if (!caseEditWin) {
		caseEditWin = new Ext.Window({
			id : 'caseEditWin',
			title : id != null ? '编辑' : '新增' + '案例知识点',
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
					fieldLabel : '标题',
					maxLength : 150,
					blankText : '不能为空，请填写',
					regex : /^[\u4e00-\u9fa5a-zA-Z0-9]+$/
				}, {
					xtype : 'textfield',
					id : 'caseKeyWords',
					name : 'kbknowledge.keywords',
					fieldLabel : '关键字',
					maxLength : 150,
					regex : /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/
				}, {
					xtype : 'textfield',
					id : 'caseProfessionType',
					name : 'kbknowledge.professionType',
					fieldLabel : '专业类别',
					maxLength : 100
				}, {
					xtype : 'textfield',
					id : 'caseDeviceName',
					name : 'kbknowledge.deviceName',
					fieldLabel : '设备名称',
					maxLength : 100
				}, {
					xtype : 'textarea',
					id : 'caseDesc',
					name : 'kbknowledge.description',
					width : 300,
					height : 60,
					fieldLabel : '案例简述',
					maxLength : 900
				}, {
					xtype : 'textarea',
					id : 'caseSolution',
					name : 'kbknowledge.solution',
					width : 300,
					height : 60,
					fieldLabel : '结论和建议',
					maxLength : 900
				}, {
					xtype : 'textarea',
					id : 'caseSolutionContent',
					name : 'kbknowledge.solutionContent',
					width : 300,
					height : 60,
					fieldLabel : '可借鉴的经验',
					maxLength : 900
				}, new Ext.form.ComboBox({
					id : 'caseIsRetired',
					hiddenName : 'kbknowledge.isRetired',
					labelWidth : 80,
					width : 140,
					fieldLabel : '是否废弃',
					mode : 'local',
					// readOnly : true,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "value", "text" ],
						data : [ [ 0, "否" ], [ 1, "是" ] ]
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
				text : '提交',
				handler : function() {
					var form = Ext.getCmp('caseForm');
					if (checkCaseValid()) {
						Ext.MessageBox.wait("提交数据...", "请等待");
						form.form.doAction('submit', {
							url : pageConst.saveKnowledgeUrl,
							params : {
								baseType : 2
							},
							method : 'POST',
							success : function(form, action) {
								Ext.MessageBox.hide();
								caseEditWin.hide();
								Ext.Msg.alert('成功', action.result.message);
								gridReload();
							},
							failure : function(form, action) {
								Ext.MessageBox.hide();
								Ext.Msg.alert('失败', '操作时发生异常！');
							}
						});
					} 
				}
			}, {
				text : '取消',
				handler : function() {
					Ext.getCmp('caseForm').getForm().reset();
					caseEditWin.hide();
				}
			} ],
			// 关闭时清空数据
			listeners : {
				hide : function() {
					Ext.getCmp('caseForm').getForm().reset();
				}
			}
		});
	}
	caseEditWin.show();
	if (op == 'edit') {
		// 编辑
		var rows = Ext.getCmp("grid").getSelections();
		caseEditWin.setTitle("编辑案例知识点");
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
		caseEditWin.setTitle("新增案例知识点");
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
			Ext.Msg.alert('数据不完整', '请确保填写完整的数据！');
			return false;
		}else if(!Ext.getCmp('caseTitle').isValid()){
			Ext.Msg.alert('数据不正确', '请确保填写正确的数据!');
			return false;
		}else if(!Ext.getCmp('caseKeyWords').isValid()){
			Ext.Msg.alert('数据不正确', '关键字中请勿包含特殊符号!');
			return false;
		}
		return true;
	}
}
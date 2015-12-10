var eventEditWin;
function showEventEditWin(id, op) {
	// Ext.Msg.alert(id);
	if (!eventEditWin) {
		eventEditWin = new Ext.Window({
			id : 'eventEditWin',
			title : op == 'add' ? "新增" : "编辑" + "故障知识点",
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
					fieldLabel : '标题',
					maxLength : 150,
					blankText : '不能为空，请填写',
					regex : /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/
				}, {
					xtype : 'textfield',
					id : 'eventKeyWords',
					name : 'kbknowledge.keywords',
					fieldLabel : '关键字',
					maxLength : 150,
					regex : /^[\u4e00-\u9fa5a-zA-Z0-9]+$/
				}, {
					xtype : 'textarea',
					id : 'eventDesc',
					name : 'kbknowledge.description',
					width : 350,
					height : 60,
					fieldLabel : '故障内容',
					maxLength : 900
				}, {
					xtype : 'textarea',
					id : 'eventSolution',
					name : 'kbknowledge.solution',
					width : 350,
					height : 60,
					fieldLabel : '解决内容',
					maxLength : 900
				}, new Ext.form.ComboBox({
					id : 'eventIsRetired',
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
				text : '提交',
				handler : function() {
					var form = Ext.getCmp('eventForm');
					Ext.MessageBox.wait("提交数据...", "请等待");
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
								Ext.Msg.alert('成功', action.result.message);
								gridReload();

							},
							failure : function(form, action) {
								Ext.MessageBox.hide();
								Ext.Msg.alert('失败', '操作时发生异常！');
							}
						});
					}
					;
				}
			}, {
				text : '取消',
				handler : function() {
					Ext.getCmp('eventForm').getForm().reset();
					eventEditWin.hide();

				}
			} ],
			// 关闭时清空数据
			listeners : {
				hide : function() {
					Ext.getCmp('eventForm').getForm().reset();
				}
			}
		});
	}
	eventEditWin.show();
	if (op == 'edit') {
		// 编辑
		// var record = Ext.getCmp('grid').getSelectionModel().getSelected();
		// eventForm.getForm().loadRecord(record);
		var rows = Ext.getCmp("grid").getSelections();
		eventEditWin.setTitle("编辑故障知识点");
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
		eventEditWin.setTitle("新增故障知识点");
		// 新增时，传入id注入知识分类字段
		var cataid = pageObj.selectNode.attributes.id;
		if (cataid != -1) {
			Ext.getCmp('eventIsRetired').setValue(0);
			Ext.getCmp('eventCatalog').setValue(cataid);
		}
	}

}

/** form表单输入校验** */
function checkValid() {
	var title = Ext.getCmp('eventTitle').getValue();
	var keywords = Ext.getCmp('eventKeyWords').getValue();
	var eventDesc = Ext.getCmp('eventDesc').getValue();
	var eventSolution = Ext.getCmp('eventSolution').getValue();
	if (trim(title) == '') {
		Ext.Msg.alert('数据不完整', '请确保填写完整的数据！');
		return false;
	} else if (!Ext.getCmp('eventTitle').isValid()) {
		Ext.Msg.alert('数据不正确', '请确保填写正确的数据!');
		return false;
	} else if (!Ext.getCmp('eventKeyWords').isValid()) {
		Ext.Msg.alert('数据不正确', '关键字中请勿包含特殊符号!');
		return false;
	}else {
		return true;
	}
}
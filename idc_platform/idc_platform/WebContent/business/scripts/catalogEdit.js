Ext.apply(Ext.form.TextField.prototype,{ 
    validator:function(text){
        if(this.allowBlank==false &&Ext.util.Format.trim(text).length==0)
        	return false;
        else
        	return true;
    }
});

var catalogEditWin;
function showCatalogEditWin(id){
	if(!catalogEditWin){
		catalogEditWin = new Ext.Window({
			id : 'catalogEditWin',
			title :'产品目录',
			layout : 'fit',
			closable : true,
			closeAction : 'hide',
			constrain:true,
			plain : true,
			modal : true,
			width : 500,
			height : 240,
			resizable : false,
			items : [ new Ext.form.FormPanel({
				id : 'catalogForm',
				layout : 'form',
				frame : true,
				labelAlign : 'right',
				labelWidth :50,
				items : [
				{
					xtype : 'numberfield',
					id : 'catalogId',
					name : 'productCatalog.id',
					fieldLabel : '编号',
					labelWidth: 130,
					width : 390,
					minValue:1,
					maxValue:99999999,
					maxText:"编号过大",
					maxLength:8,
					allowDecimals:false,
					allowNegative:false,
					allowBlank:false,
					blankText : '不能为空，请填写'
				},
				{
					xtype : 'textfield',
					id : 'catalogName',
					name : 'productCatalog.name',
					fieldLabel : '名称',
					width : 390,
					allowBlank:false,
					invalidText : '输入内容不符合要求',
					blankText : '不能为空，请填写'
				}, new Ext.form.ComboBox({
					id : 'catalogStatus',
					hiddenName : 'productCatalog.status',
					labelWidth : 80,
					width : 390,
					fieldLabel : '状态',
					mode : 'local',
					// readOnly : true,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "value", "text" ],
						data : [ [ 0, "启用" ], [ 1, "禁用" ] ]
					}),
					valueField : "value",
					displayField : "text",
					readOnly:true,
					editable: false,
					forceSelection: true,
					value : 0
				}), 
				{
					xtype : 'textarea',
					id : 'catalogDescription',
					name : 'productCatalog.description',
					width : 387,
					height : 60,
					fieldLabel : '描述',
					maxLength : 900
				},
				{
					xtype : 'hidden',
					id : 'catalogUrl'
				}]
			}) ],
			buttonAlign : 'center',
			buttons : [ {
				text : '提交',
				handler : function() {
					var form = Ext.getCmp('catalogForm');
					var url= Ext.getCmp('catalogUrl').getValue();
					if (form.getForm().isValid()) {
						Ext.MessageBox.wait("提交数据...", "请等待");
						form.form.doAction('submit', {
							url : url,
							method : 'POST',
							success : function(form, action) {
								Ext.MessageBox.hide();
								catalogEditWin.hide();
								Ext.Msg.alert('成功', '保存产品目录成功！');
								gridReload();
							},
							failure : function(form, action) {
								Ext.MessageBox.hide();
								Ext.Msg.alert('失败', '保存产品目录失败！');
							}
						});
					} else {
						Ext.MessageBox.alert('校验错误', '请确保填写完整且正确的数据！');
					}
				}
			}, {
				text : '取消',
				handler : function() {
					catalogEditWin.hide();
				}
			} ],
			// 关闭时清空数据
			listeners : {
				hide : function() {
					Ext.getCmp('catalogForm').getForm().reset();
				}
			}
		});
	}
	catalogEditWin.show();
	if(id!=null){
		catalogEditWin.setTitle("编辑产品目录");
		Ext.getCmp('catalogId').getEl().dom.readOnly=true;
		Ext.getCmp('catalogUrl').setValue(pageConst.updateCatalogUrl);
		var records = Ext.getCmp('grid').getSelectionModel().getSelections();
		var record=records[0];
		Ext.getCmp('catalogId').setValue(record.get('id'));
		Ext.getCmp('catalogName').setValue(record.get('name'));
		Ext.getCmp('catalogStatus').setValue(record.get('status'));
		Ext.getCmp('catalogDescription').setValue(record.get('description'));
	}
	else{
		catalogEditWin.setTitle("新增产品目录");
		Ext.getCmp('catalogId').getEl().dom.readOnly=false;
		Ext.getCmp('catalogUrl').setValue(pageConst.addCatalogUrl);
	}
}

function editProductCatalog(){
	var records = Ext.getCmp('grid').getSelectionModel().getSelections();
	var record=records[0];
	showCatalogEditWin(record.get('id'));
}

function removeCatalog(){
	
	var rows = Ext.getCmp("grid").getSelections();
	var ids = "";
	Ext.Msg.show({
		title : "删除知识点",
		msg : "是否确认删除选中的产品目录？",
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(b) {
			if (b == 'yes') {
				for ( var i = 0; i < rows.length; i++) {
					if(ids!="")
						ids+=",";
					ids += rows[i].json.id;
				}

				Ext.MessageBox.show({
					title : '正在删除产品目录',
					width : 280,
					wait : true,
					icon : Ext.MessageBox.INFO,
					cls : 'custom',
					closable : false
				});
				Ext.Ajax.request({
					url : pageConst.removeCatalogUrl,
					method : 'POST',
					params : {
						ids : ids
					},
					success : function(response) {
						Ext.MessageBox.hide();
						var responseArray = Ext.util.JSON
								.decode(response.responseText);
						if (true == responseArray.success) {
							Ext.Msg.show({
								title : "完成",
								msg : responseArray.message,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
							gridReload();
						} else {
							Ext.Msg.show({
								title : "失败",
								msg : responseArray.message,
								buttons : Ext.MessageBox.CANCEL,
								icon : Ext.MessageBox.ERROR
							});
							gridReload();
						}
					},
					failure : function() {
						Ext.MessageBox.hide();
						Ext.MessageBox.hide();
						Ext.Msg.show({
							title : "失败",
							msg : "连接失败，请检查网络，如网络无异常请与系统管理员联系",
							buttons : Ext.MessageBox.CANCEL,
							icon : Ext.MessageBox.ERROR
						});
						gridReload();
					}
				});
			}
		}
	});
	
}


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
			title :'��ƷĿ¼',
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
					fieldLabel : '���',
					labelWidth: 130,
					width : 390,
					minValue:1,
					maxValue:99999999,
					maxText:"��Ź���",
					maxLength:8,
					allowDecimals:false,
					allowNegative:false,
					allowBlank:false,
					blankText : '����Ϊ�գ�����д'
				},
				{
					xtype : 'textfield',
					id : 'catalogName',
					name : 'productCatalog.name',
					fieldLabel : '����',
					width : 390,
					allowBlank:false,
					invalidText : '�������ݲ�����Ҫ��',
					blankText : '����Ϊ�գ�����д'
				}, new Ext.form.ComboBox({
					id : 'catalogStatus',
					hiddenName : 'productCatalog.status',
					labelWidth : 80,
					width : 390,
					fieldLabel : '״̬',
					mode : 'local',
					// readOnly : true,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "value", "text" ],
						data : [ [ 0, "����" ], [ 1, "����" ] ]
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
					fieldLabel : '����',
					maxLength : 900
				},
				{
					xtype : 'hidden',
					id : 'catalogUrl'
				}]
			}) ],
			buttonAlign : 'center',
			buttons : [ {
				text : '�ύ',
				handler : function() {
					var form = Ext.getCmp('catalogForm');
					var url= Ext.getCmp('catalogUrl').getValue();
					if (form.getForm().isValid()) {
						Ext.MessageBox.wait("�ύ����...", "��ȴ�");
						form.form.doAction('submit', {
							url : url,
							method : 'POST',
							success : function(form, action) {
								Ext.MessageBox.hide();
								catalogEditWin.hide();
								Ext.Msg.alert('�ɹ�', '�����ƷĿ¼�ɹ���');
								gridReload();
							},
							failure : function(form, action) {
								Ext.MessageBox.hide();
								Ext.Msg.alert('ʧ��', '�����ƷĿ¼ʧ�ܣ�');
							}
						});
					} else {
						Ext.MessageBox.alert('У�����', '��ȷ����д��������ȷ�����ݣ�');
					}
				}
			}, {
				text : 'ȡ��',
				handler : function() {
					catalogEditWin.hide();
				}
			} ],
			// �ر�ʱ�������
			listeners : {
				hide : function() {
					Ext.getCmp('catalogForm').getForm().reset();
				}
			}
		});
	}
	catalogEditWin.show();
	if(id!=null){
		catalogEditWin.setTitle("�༭��ƷĿ¼");
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
		catalogEditWin.setTitle("������ƷĿ¼");
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
		title : "ɾ��֪ʶ��",
		msg : "�Ƿ�ȷ��ɾ��ѡ�еĲ�ƷĿ¼��",
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
					title : '����ɾ����ƷĿ¼',
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
								title : "���",
								msg : responseArray.message,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
							gridReload();
						} else {
							Ext.Msg.show({
								title : "ʧ��",
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
							title : "ʧ��",
							msg : "����ʧ�ܣ��������磬���������쳣����ϵͳ����Ա��ϵ",
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


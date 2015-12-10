Ext.apply(Ext.form.TextField.prototype,{ 
    validator:function(text){
        if(this.allowBlank==false &&Ext.util.Format.trim(text).length==0)
        	return false;
        else
        	return true;
    }
});
var now=new Date();
var today=new Date(now.getFullYear(),now.getMonth(),now.getDate());
var productEditWin;
function showProductEditWin(id){

	if(!productEditWin){
		productEditWin = new Ext.Window({
			id : 'productEditWin',
			title : '������Ʒ',
			layout : 'fit',
			closable : true,
			closeAction : 'hide',
			constrain:true,
			plain : true,
			modal : true,
			width : 500,
			height : 320,
			resizable : false,
			items : [ new Ext.form.FormPanel({
				id : 'productForm',
				layout : 'form',
				frame : true,
				labelAlign : 'right',
				labelWidth : 65,
				items : [
				{
					xtype : 'numberfield',
					id : 'productId',
					name : 'product.id',
					fieldLabel : '���',
					width:380,
					readOnly: true,
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
					id : 'productName',
					name : 'product.name',
					fieldLabel : '����',
					width:380,
					maxLength : 150,
					allowBlank:false,
				    invalidText:'�������ݲ�����Ҫ��',
					blankText : '����Ϊ�գ�����д'
				},
				{
					xtype : 'textfield',
					id : 'productSubParam',
					name : 'product.subParam',
					fieldLabel : '��Ʒ����',
					width:380,
					maxLength : 150
				}, {
					xtype : 'datefield',
					id : 'productEffectDate',
					name : 'effectDate',
					width : 380,
					height : 60,
					fieldLabel : '��Чʱ��',
					format:'Y-m-d',
					altFormats: 'Y-m-d',
					value : today,
					menuListeners : {
						beforeshow : function() {
							var myDate=new Date();
							this.menu.picker.setValue(this.getValue()||myDate);
						},
						select : function(m, d) {
							this.setValue(this.menu.picker.getValue());
						}
					},
					listeners:{
						valid:function(field){
							if(Ext.getCmp("productExpireDate").isValid())
								Ext.getCmp("productExpireDate").clearInvalid();
							dateValid(Ext.getCmp("productExpireDate"));
						}
					}
				},{
					xtype : 'datefield',
					id : 'productExpireDate',
					name : 'expireDate',
					width : 380,
					height : 60,
					fieldLabel : 'ʧЧʱ��',
					format:'Y-m-d',
					altFormats: 'Y-m-d',
					value : new Date(2099,11,31),
					menuListeners : {
						beforeshow : function() {
							var myDate=new Date();
				            myDate.setFullYear(2099,11,31);
							this.menu.picker.setValue(this.getValue()||myDate);
						},
						select : function(m, d) {
							this.setValue(this.menu.picker.getValue());
						}
					},
					listeners:{
						valid:function(field){
							dateValid(field);
						}
					}
				},
				new Ext.form.ComboBox({
					id : 'productStatus',
					name : 'product.status',
					labelWidth : 80,
					width : 380,
					fieldLabel : '״̬',
					mode : 'local',
					// readOnly : true,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "value", "text" ],
						data : [ [ 0, "��Ч" ], [ 1, "ʧЧ" ] ]
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
					id : 'productDescription',
					name : 'product.description',
					width : 377,
					height : 60,
					fieldLabel : '����',
					maxLength : 900
				},
				{
					xtype : 'hidden',
					id : 'productUrl'
				}
				]
			}) ],
			buttonAlign : 'center',
			buttons : [ {
				text : '�ύ',
				handler : function() {
					var form = Ext.getCmp('productForm');
					var url= Ext.getCmp('productUrl').getValue();
					if (form.getForm().isValid()&&dateValid(Ext.getCmp('productExpireDate'))) {
						Ext.MessageBox.wait("�ύ����...", "��ȴ�");
						var p="";
						form.items.each(function(f){ 
							if(f.getId()!="productUrl"){
								if(""!=p)
									p+="&"
								if(f.isXType("datefield")&&f.getValue()!=null&&f.getValue()!=''){
									p += ((f.name +'='+f.getValue().format('Y-m-d'))); 
								}
								else{
									p += ((f.name +'='+f.getValue())); 
								}
							}
						}); 
						Ext.Ajax.request({
							url : url+"?action=do",
							method : 'POST',
							params : p,
							success : function(response) {
								Ext.MessageBox.hide();
								var responseArray = Ext.util.JSON
										.decode(response.responseText);
								if (true == responseArray.success) {
									Ext.Msg.show({
										title : "���",
										msg : responseArray.message,
										buttons : Ext.MessageBox.OK,
										icon : Ext.MessageBox.INFO,
										fn: function(){
											productEditWin.hide();
										}
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
								Ext.Msg.show({
									title : "ʧ��",
									msg : "����ʧ�ܣ��������磬���������쳣����ϵͳ����Ա��ϵ",
									buttons : Ext.MessageBox.CANCEL,
									icon : Ext.MessageBox.ERROR
								});
								gridReload();
							}
						
						});
					} else {
						Ext.MessageBox.alert('У�����', '��ȷ����д�����Ҹ�ʽ��ȷ�����ݣ�');
					}
				}
			}, {
				text : 'ȡ��',
				handler : function() {
					productEditWin.hide();
				}
			} ],
			// �ر�ʱ�������
			listeners : {
				hide : function() {
					Ext.getCmp('productForm').getForm().reset();
				}
			}
		});
	}
	productEditWin.show();
	if(id!=null){
		productEditWin.setTitle("�༭������Ʒ");
		Ext.getCmp("productId").getEl().dom.readOnly = true;
		Ext.getCmp('productUrl').setValue(pageConst.updateProductUrl);
		var records = Ext.getCmp('grid').getSelectionModel().getSelections();
		var record=records[0];
		Ext.getCmp('productId').setValue(record.get('id'));
		Ext.getCmp('productName').setValue(record.get('name'));
		Ext.getCmp('productSubParam').setValue(record.get('subParam'));
		Ext.getCmp('productStatus').setValue(record.get('status'));
		Ext.getCmp('productDescription').setValue(record.get('description'));
		Ext.getCmp('productEffectDate').setValue(record.get('effectDate'));
		Ext.getCmp('productExpireDate').setValue(record.get('expireDate'));
		Ext.getCmp('productEffectDate').setMinValue(record.get('createDate'));
		Ext.getCmp('productExpireDate').setMinValue(record.get('createDate'));
	}
	else{
		productEditWin.setTitle("����������Ʒ");
		Ext.getCmp("productId").getEl().dom.readOnly = false;
		Ext.getCmp('productUrl').setValue(pageConst.addProductUrl);
		Ext.getCmp('productEffectDate').setMinValue(today);
		Ext.getCmp('productExpireDate').setMinValue(today);
	}
}

function dateValid(field){
  if(Ext.getCmp("productEffectDate")!=null&&field.isValid()){
       var dateBefore=Ext.getCmp("productEffectDate");
       if(dateBefore.getValue()!=null&&dateBefore.getValue()!=""
      	 &&field.getValue()!=""&&field.getValue()!=null){
      	  if(field.getValue()<dateBefore.getValue()){
      		  field.markInvalid("ʧЧ���ʱ��Ӧ������Чʱ��");
      		  return false;
      	  }
  	  }
  }
  return field.isValid();
}

function editProduct(){
	var records = Ext.getCmp('grid').getSelectionModel().getSelections();
	var record=records[0];
	showProductEditWin(record.get('id'));
}

function removeProduct(){
	
	var rows = Ext.getCmp("grid").getSelections();
	var ids = "";
	Ext.Msg.show({
		title : "ɾ��������Ʒ",
		msg : "�Ƿ�ȷ��ɾ��ѡ�еĻ�����Ʒ��",
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
					title : '����ɾ��������Ʒ',
					width : 280,
					wait : true,
					icon : Ext.MessageBox.INFO,
					cls : 'custom',
					closable : false
				});
				Ext.Ajax.request({
					url : pageConst.removeProductUrl,
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
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
var serviceEditWin;
function showServiceEditWin(id){
	if(!serviceEditWin){
		serviceEditWin = new Ext.Window({
			id : 'serviceEditWin',
			title : '服务信息',
			layout : 'fit',
			closable : true,
			closeAction : 'hide',
			constrain:true,
			plain : true,
			modal : true,
			width : 500,
			height : 380,
			resizable : false,
			items : [ new Ext.form.FormPanel({
				id : 'serviceForm',
				layout : 'form',
				frame : true,
				labelAlign : 'right',
				labelWidth : 65,
				items : [ 
				{
					xtype : 'numberfield',
					id : 'serviceId',
					name : 'service.id',
					fieldLabel : '编号',
					width:380,
					readOnly: true,
					minValue:1,
					maxValue:99999,
					maxText:"编号过大",
					maxLength:5,
					allowDecimals:false,
					allowNegative:false,
					allowBlank:false,
					blankText : '不能为空，请填写'
				},
				{
					xtype : 'textfield',
					id : 'serviceName',
					name : 'service.name',
					fieldLabel : '名称',
					width:380,
					maxLength : 150,
					allowBlank:false,
					invalidText:'输入内容不符合要求',
					blankText : '不能为空，请填写'
				}, new Ext.form.ComboBox({
					id : 'serviceType',
					name : 'service.type',
					width : 380,
					fieldLabel : '类型',
					mode : 'local',
					// readOnly : true,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "value", "text" ],
						data : [ [ 1, "基础服务" ], [ 2, "增值服务" ] ]
					}),
					valueField : "value",
					displayField : "text",
					readOnly:true,
					editable: false,
					forceSelection: true,
					value : 1
				}), 
				new Ext.form.ComboBox({
					id : 'serviceStatus',
					name : 'service.status',
					width : 380,
					fieldLabel : '状态',
					mode : 'local',
					// readOnly : true,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "value", "text" ],
						data : [ [ 0, "无效" ], [ 1, "有效" ] ]
					}),
					valueField : "value",
					displayField : "text",
					readOnly:true,
					editable: false,
					forceSelection: true,
					value : 1
				}),
				{
					xtype : 'textfield',
					id : 'serviceValue',
					name : 'service.serviceValue',
					fieldLabel : '服务属性',
					width:380,
					maxLength : 150,
					invalidText:'输入内容不符合要求',
					blankText : '不能为空，请填写'
				},{
					xtype : 'datefield',
					id : 'serviceEffectDate',
					name : 'effectDate',
					width : 380,
					fieldLabel : '生效时间',
					format:'Y-m-d',
					altFormats: 'Y-m-d',
					value : new Date(),
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
							if(Ext.getCmp("serviceExpireDate").isValid())
								Ext.getCmp("serviceExpireDate").clearInvalid();
							dateValid(Ext.getCmp("serviceExpireDate"));
						}
					}
				},{
					xtype : 'datefield',
					id : 'serviceExpireDate',
					name : 'expireDate',
					width : 380,
					fieldLabel : '失效时间',
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
				{
					xtype : 'textarea',
					id : 'serviceDescription',
					name : 'service.description',
					width : 377,
					height : 100,
					fieldLabel : '描述'
				},
				{
					xtype : 'hidden',
					id : 'serviceUrl'
				}]
			}) ],
			buttonAlign : 'center',
			buttons : [ {
				text : '提交',
				handler : function() {
					var form = Ext.getCmp('serviceForm');
					var url = Ext.getCmp('serviceUrl').getValue();
					if (form.getForm().isValid()&&dateValid(Ext.getCmp("serviceExpireDate"))) {
						Ext.MessageBox.wait("提交数据...", "请等待");
						var p="";
						form.items.each(function(f){ 
							if(f.getId()!="serviceUrl"){
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
										title : "完成",
										msg : responseArray.message,
										buttons : Ext.MessageBox.OK,
										icon : Ext.MessageBox.INFO,
										fn: function(){
											serviceEditWin.hide();
										}
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
								Ext.Msg.show({
									title : "失败",
									msg : "连接失败，请检查网络，如网络无异常请与系统管理员联系",
									buttons : Ext.MessageBox.CANCEL,
									icon : Ext.MessageBox.ERROR
								});
								gridReload();
							}
						
						});
					} else {
						Ext.MessageBox.alert('校验错误', '请确保填写完整且正确的数据！');
					}
				}
			}, {
				text : '取消',
				handler : function() {
					serviceEditWin.hide();
				}
			} ],
			// 关闭时清空数据
			listeners : {
				hide : function() {
					Ext.getCmp('serviceForm').getForm().reset();
				}
			}
		});
	}
	serviceEditWin.show();
	if(id!=null){
		serviceEditWin.setTitle("编辑服务");
		Ext.getCmp("serviceId").getEl().dom.readOnly = true;
		Ext.getCmp('serviceUrl').setValue(pageConst.updateServiceUrl);
		var records = Ext.getCmp('grid').getSelectionModel().getSelections();
		var record=records[0];
		Ext.getCmp('serviceId').setValue(record.get('id'));
		Ext.getCmp('serviceName').setValue(record.get('name'));
		Ext.getCmp('serviceType').setValue(record.get('type'));
		Ext.getCmp('serviceStatus').setValue(record.get('status'));
		Ext.getCmp('serviceValue').setValue(record.get('serviceValue'));
		Ext.getCmp('serviceDescription').setValue(record.get('description'));
		Ext.getCmp('serviceEffectDate').setValue(record.get('effectDate'));
		Ext.getCmp('serviceExpireDate').setValue(record.get('expireDate'));
		Ext.getCmp('serviceEffectDate').setMinValue(record.get('createDate'));
		Ext.getCmp('serviceExpireDate').setMinValue(record.get('createDate'));
	}else{
		serviceEditWin.setTitle("新增服务");
		Ext.getCmp("serviceId").getEl().dom.readOnly = false;
		Ext.getCmp('serviceUrl').setValue(pageConst.addServiceUrl);
		Ext.getCmp('serviceEffectDate').setMinValue(today);
		Ext.getCmp('serviceExpireDate').setMinValue(today);
	}
}
function dateValid(field){
	  if(Ext.getCmp("serviceEffectDate")!=null&&field.isValid()){
	       var dateBefore=Ext.getCmp("serviceEffectDate");
	       if(dateBefore.getValue()!=null&&dateBefore.getValue()!=""
	      	 &&field.getValue()!=""&&field.getValue()!=null){
	      	  if(field.getValue()<dateBefore.getValue()){
	      		  field.markInvalid("失效完成时间应晚于生效时间");
	      		  return false;
	      	  }
	  	  }
	  }
	  return field.isValid();
}

function editService(){
	var records = Ext.getCmp('grid').getSelectionModel().getSelections();
	var record=records[0];
	showServiceEditWin(record.get('id'));
}

function removeService(){
	
	var rows = Ext.getCmp("grid").getSelections();
	var ids = "";
	Ext.Msg.show({
		title : "删除服务",
		msg : "是否确认删除选中的服务？",
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
					title : '正在删除服务',
					width : 280,
					wait : true,
					icon : Ext.MessageBox.INFO,
					cls : 'custom',
					closable : false
				});
				Ext.Ajax.request({
					url : pageConst.removeServiceUrl,
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
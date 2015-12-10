Ext.onReady(function(){
	Ext.BLANK_IMAGE_URL = "/Ext/resources/images/default/s.gif";
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget="qtip";
	
	Ext.apply(Ext.form.VTypes,{
		//field表示该宿主组件，val是该宿主的值
		repetition:function(val,field) {
			//是否配置了相比较的目标组件
			if(field.repetition){
				//获得该目标组件
				var cmp = Ext.getCmp(field.repetition.targetCmpId);
				if(Ext.isEmpty(cmp)){
					Ext.MessageBox.show({
						title: '错误',
                     	msg: '发生异常错误，指定的组件未找到',
                      	icon: Ext.Msg.ERROR,
                      	buttons: Ext.Msg.OK
					});
				}
				//比较宿主和目标对象的值
				if(val == cmp.getValue()) {
					return true;
				} else {
					return false;
				}
			}
		},
		repetitionText:"两个组件的值不相同"
	});
	
	var viewPort = new Ext.Viewport({
		renderTo : Ext.getBody(),
		layout:"border",
		items:[ {
			title:"导航栏",
			region:"west",
			width:200,
			items:menu,
			collapsible:true,
			split:true
		}, tabPanel ]
	});
});
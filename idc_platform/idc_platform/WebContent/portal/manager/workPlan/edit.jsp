<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.WorkPlanMgr"%>
<%@page import="com.hp.idc.portal.bean.WorkPlan"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>
<%
	String oid = request.getParameter("oid");
	WorkPlanMgr mgr = (WorkPlanMgr)ContextUtil.getBean("workPlanMgr");
	WorkPlan bean = mgr.getBeanById(oid);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat xdf = new SimpleDateFormat("yyyy/MM/dd");
%>
<script type="text/javascript">
Ext.apply(Ext.form.TextField.prototype,{ 
    validator:function(text){
        if(this.allowBlank==false &&Ext.util.Format.trim(text).length==0)
        	return false;
        else
        	return true;
    }
});

var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [[1,'普通'],[2,'重要'],[3,'里程碑']]
})

var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	baseParams: {action:'update',oid:'<%=oid%>'}, 
	method : 'POST',
	autoscroll : true,
	labelWidth : 80,
	items : [{
				xtype : 'textfield',
				fieldLabel : '标题',
				name : 'title',
				allowBlank : false,
				value:'<%=bean.getTitle()%>',
				maxLength: 30,
				invalidText : '输入内容不符合要求',
				blankText : '不能为空，请填写',
				anchor:'-20'
			},{
	            xtype: 'combo',
	            hiddenName: 'type',
	            fieldLabel : '计划类型',
	            anchor:'-20',
	            valueField : 'id',
				displayField : 'value',
				typeAhead : true,
				mode : 'local',
				maxHeight : 150,
				triggerAction : 'all',
				emptyText : '请选择...',
				selectOnFocus : true,
				editable : false,
				forceSelection : true,
				allowBlank : false,
				value:'<%=bean.getType()%>',
				store : typeData
	        },{
				xtype : 'datefield',
				fieldLabel: '计划完成时间',
                format:'Y-m-d H:i:s',
                altFormats: 'Y-m-d H:i:s',
                id: 'planTime',
                name: 'planTime',
                precision:'t',
                allowBlank : false,
                value:'<%=sdf.format(bean.getPlanTime())%>',
				anchor:'-20',
				listeners:{
					valid:function(field){
						plantimeValid(field)
					}
				},
				menuListeners:{
					select:function(m,d){
						var p=this.menu.picker.getValue();
						var now=new Date();
						p.setHours(now.getHours());
						p.setMinutes(now.getMinutes());
						p.setSeconds(now.getSeconds());
						this.setValue(p);
					}
				}
			},{
				xtype : 'textarea',
				fieldLabel : '计划描述',
				name : 'desc',
				value:'<%=StringUtil.escapeJavaScript(bean.getDescription())%>',
				maxLength: 500,
				maxLengthText:'最多输入500个字符',
				anchor:'-20'
			}]
});
function plantimeValid(field){
	if(field.isValid()){
		var date=new Date('<%=xdf.format(bean.getCreateTime())%>');
		date.setHours(<%=bean.getCreateTime().getHours()%>);
		date.setMinutes(<%=bean.getCreateTime().getMinutes()%>);
		date.setSeconds(<%=bean.getCreateTime().getSeconds()%>);
		if(field.getValue()<date){
			field.markInvalid("计划完成时间应晚于创建时间'<%=sdf.format(bean.getCreateTime())%>'");
			return false;
		}
	}
	return field.isValid();
}
if (win)
	win.close();
var win = new Ext.Window({
			title : '编辑工作计划',
			border : false,
			modal : true,
			width : 360,
			height : 250,
			minWidth : 350,
			minHeight : 250,
			closable : true,
			layout : 'fit', // window的默认布局
			plain : true, // 颜色透明
			items : form,
			buttons : [{
				text : '保存',
				iconCls :'icon-save',
				handler : function() {
					var form = win._form.form;
					if (!(form.isValid())||!plantimeValid(Ext.getCmp("planTime"))) {
						Ext.Msg.alert("提示", "请填写正确的内容！");
						return;
					}
					form.submit({
						waitMsg : '正在更新，请稍候...', // form提交时等待的提示信息
						// 提交成功后的处理
						success : function(form, action) {   
				            var o = Ext.util.JSON.decode(action.response.responseText); 
				            alert(o.msg);
				            win.close();
				            store.load();
				        },
						// 提交失败后的处理
						failure : function(form, action) {
							var o = Ext.util.JSON.decode(action.response.responseText); 
				            alert(o.msg);
				            win.close();
				            store.load()
						}
					});
				}
				}, {
					text : '取消',
					iconCls :'icon-cancel',
					handler : function() {
						win.close();
					}
				}]
		});

win._form = form;
win.show();
</script>

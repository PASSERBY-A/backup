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
	data : [[1,'��ͨ'],[2,'��Ҫ'],[3,'��̱�']]
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
				fieldLabel : '����',
				name : 'title',
				allowBlank : false,
				value:'<%=bean.getTitle()%>',
				maxLength: 30,
				invalidText : '�������ݲ�����Ҫ��',
				blankText : '����Ϊ�գ�����д',
				anchor:'-20'
			},{
	            xtype: 'combo',
	            hiddenName: 'type',
	            fieldLabel : '�ƻ�����',
	            anchor:'-20',
	            valueField : 'id',
				displayField : 'value',
				typeAhead : true,
				mode : 'local',
				maxHeight : 150,
				triggerAction : 'all',
				emptyText : '��ѡ��...',
				selectOnFocus : true,
				editable : false,
				forceSelection : true,
				allowBlank : false,
				value:'<%=bean.getType()%>',
				store : typeData
	        },{
				xtype : 'datefield',
				fieldLabel: '�ƻ����ʱ��',
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
				fieldLabel : '�ƻ�����',
				name : 'desc',
				value:'<%=StringUtil.escapeJavaScript(bean.getDescription())%>',
				maxLength: 500,
				maxLengthText:'�������500���ַ�',
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
			field.markInvalid("�ƻ����ʱ��Ӧ���ڴ���ʱ��'<%=sdf.format(bean.getCreateTime())%>'");
			return false;
		}
	}
	return field.isValid();
}
if (win)
	win.close();
var win = new Ext.Window({
			title : '�༭�����ƻ�',
			border : false,
			modal : true,
			width : 360,
			height : 250,
			minWidth : 350,
			minHeight : 250,
			closable : true,
			layout : 'fit', // window��Ĭ�ϲ���
			plain : true, // ��ɫ͸��
			items : form,
			buttons : [{
				text : '����',
				iconCls :'icon-save',
				handler : function() {
					var form = win._form.form;
					if (!(form.isValid())||!plantimeValid(Ext.getCmp("planTime"))) {
						Ext.Msg.alert("��ʾ", "����д��ȷ�����ݣ�");
						return;
					}
					form.submit({
						waitMsg : '���ڸ��£����Ժ�...', // form�ύʱ�ȴ�����ʾ��Ϣ
						// �ύ�ɹ���Ĵ���
						success : function(form, action) {   
				            var o = Ext.util.JSON.decode(action.response.responseText); 
				            alert(o.msg);
				            win.close();
				            store.load();
				        },
						// �ύʧ�ܺ�Ĵ���
						failure : function(form, action) {
							var o = Ext.util.JSON.decode(action.response.responseText); 
				            alert(o.msg);
				            win.close();
				            store.load()
						}
					});
				}
				}, {
					text : 'ȡ��',
					iconCls :'icon-cancel',
					handler : function() {
						win.close();
					}
				}]
		});

win._form = form;
win.show();
</script>

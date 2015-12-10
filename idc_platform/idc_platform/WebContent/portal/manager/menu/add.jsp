<%@ page language="java" contentType="text/html; charset=gbk"%>
<script type="text/javascript">
var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [[1,'ҵ����Ӫ'],[2,'��Դ����'],[3,'������'],[4,'�ͻ�����'],[5,'ͳ�Ʊ���'],[6,'�ճ�����'],[7,'ϵͳ����']]
})

var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	baseParams: {action:'add'}, 
	method : 'POST',
	autoscroll : true,
	labelWidth : 80,
	items : [{
				xtype : 'textfield',
				fieldLabel : 'ģ������',
				name : 'title',
				allowBlank : false,
				anchor:'-20'
			},{
	            xtype: 'combo',
	            hiddenName: 'type',
	            fieldLabel : '��������',
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
				value:'1',
				store : typeData
	        },{
				xtype : 'textfield',
				fieldLabel : 'ģ��URL',
				name : 'url',
				allowBlank : false,
				anchor:'-20'
			},{
				xtype : 'textfield',
				fieldLabel : '��ʾ��Ϣ',
				name : 'alt',
				anchor:'-20'
			},{
				xtype : 'selDlgfield',
				fieldLabel : '�˵���Ȩ',
				selectUrl : '../common/viewRuleSelect.jsp',
				name : 'role',
				anchor:'-10'
			},{
				xtype : 'numberfield',
				fieldLabel : '�����',
				name : 'orderno',
				anchor:'-20',
				allowBlank: false,
				value: 0
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
			title : '����ģ��',
			border : false,
			modal : true,
			width : 400,
			height : 300,
			minWidth : 400,
			minHeight : 300,
			closable : true,
			layout : 'fit', // window��Ĭ�ϲ���
			plain : true, // ��ɫ͸��
			items : form,
			buttons : [{
				text : '����',
				iconCls :'icon-save',
				handler : function() {
					var form = win._form.form;
					if (!(form.isValid())) {
						Ext.Msg.alert("��ʾ", "����д��ȷ�����ݣ�");
						return;
					}
					form.submit({
						waitMsg : '���ڸ��£����Ժ�...', // form�ύʱ�ȴ�����ʾ��Ϣ
						// �ύ�ɹ���Ĵ���
						success : function(form, action) {   
				            var o = Ext.util.JSON.decode(action.response.responseText); 
				            alert(o.msg);
				            win.hide();
				            store.load();
				        },
						// �ύʧ�ܺ�Ĵ���
						failure : function(form, action) {
							var o = Ext.util.JSON.decode(action.response.responseText); 
				            alert(o.msg);
				            win.hide();
				            store.load()
						}
					});
				}
				}, {
					text : 'ȡ��',
					iconCls :'icon-cancel',
					handler : function() {
						win.hide();
					}
				}]
		});

win._form = form;
win.show();
</script>

<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>

<div id='editActionPanel'>
</div>
<script type="text/javascript">
	var actionWindow = null;
	if (actionWindow)
		actionWindow.close();
		//----form2 begin-----
	
	var filterRelation = new Ext.data.SimpleStore({
		fields: ['id', 'value'],
		data : [
			<%
				List operationList = OperationCode.getOperations();
				for (int i = 0; i< operationList.size(); i++) {
					String name = (String)operationList.get(i);
					if (i>0)
						out.print(",");
					out.print("['"+name+"','"+name+"']");
				}
			%>
		]
	});
	
	var macroData = [
		['${year}','今年'],
		['${month}','本月'],
		['${date}','今天'],
		['${datetime}','当前时间'],
		['${user}','登录者']
	]
	var filterMacro = new Ext.data.SimpleStore({
		fields: ['id', 'value'],
		data : macroData
	});
	
	var filter_field_ori = new Ext.form.ComboBox({
    fieldLabel: '字段域',
    hiddenName:'field',
    store:fieldDs,
    displayField:'value',
    valueField: 'id',
    typeAhead: true,
    mode: 'local',
    triggerAction: 'all',
    emptyText:'请选择......',
    selectOnFocus:true,
    width:100,
    editable:false,
		allowBlank:false
	});

	/*filter_field_ori.on("beforeselect",function(filter_field_ori,record,index) {
		var type=record.get("type");
		if (type == "<%=DateTimeFieldInfo.class%>") {
			type = '<%=DateFieldInfo.class%>';
		}
		filter_relation_ori.store.filter('type',type,false);
	});
*/
	
	var filter_relation_ori = new Ext.form.ComboBox({
		fieldLabel: '运算符',
    hiddenName:'relation',
		store: filterRelation,
		valueField: 'id',
		displayField:'value',
		typeAhead: true,
		mode: 'local',
		triggerAction: 'all',
		emptyText:'请选择...',
		selectOnFocus:true,
		editable:false,
		forceSelection: true,
		width:100,
		allowBlank:false
	});

	
	var filterForm = new Ext.FormPanel({
		border:false,
		labelAlign:'right',
		labelWidth:75,
		items:[
			filter_field_ori,
			filter_relation_ori,
			new Ext.form.ComboBox({
				fieldLabel: '值',
		    hiddenName:'value',
				store: filterMacro,
				valueField: 'id',
				displayField:'value',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'请选择...',
				selectOnFocus:true,
				editable:true,
				width:150,
				allowBlank:false
			})]
	});
	actionWindow = new Ext.Window({
      title: '增加过滤器',
      width: 300,
      height:200,
      minWidth: 300,
      minHeight: 200,
      layout: 'fit',
      plain:true,
      iconCls :'edit',
      buttonAlign:'center',
      modal:true,
      items: filterForm,
      buttons: [{
          text: '确定',
          handler: function() {
          	if (!filterForm.form.isValid()) {
							Ext.MessageBox.alert("提示", "请填写完整的内容");
							return;
						}
						var filterPlant = Ext.data.Record.create([
						   {name: 'field', type: 'string'},
						   {name: 'relation', type: 'string'},
						   {name: 'origin', type: 'string'},
						   {name: 'value', type: 'string'}
					  ]);
						var value_ = filterForm.form.findField("value").getValue();
						if (value_==null || value_ == "" ) {
							value_ = filterForm.form.findField("value").getEl().dom.value;
						}
						var ori;
						var field = filterForm.form.findField("field").getValue();
						for (var i = 0; i < fieldData.length; i++) {
							if (fieldData[i][0] ==  field) {
								ori = fieldData[i][3];
								break;
							}
						}
						var p = new filterPlant({
							'field': field,
							'relation': filterForm.form.findField("relation").getValue(),
							'origin':ori,
							'value': value_
						});
						filter_ds.add(p);
						actionWindow.close();
          }
      },{
      	text: '取消',
          handler: function() {
          	actionWindow.close();
          }
      }]
  });
  actionWindow.show();
</script>
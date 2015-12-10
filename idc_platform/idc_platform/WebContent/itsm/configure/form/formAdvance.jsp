<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=GBK");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
try{
	String fieldId = request.getParameter("fieldId");

	//规则串
	String fieldRule = request.getParameter("fieldRule");
	fieldRule = StringUtil.escapeJavaScript(fieldRule);
	//System.out.println(fieldRule);
%>

<script type="text/javascript">
	var ruleWindow = null;
	if (ruleWindow)
		ruleWindow.close();
	// create the editor grid

	var view_rule_ds = new Ext.data.SimpleStore({
	fields: ['code', 'value'],
	data: [
		<%
		if (fieldRule!=null && !fieldRule.equals("")){
			String[] rules = fieldRule.split("&__&");
			for (int i = 0; i < rules.length; i++) {
				String code = rules[i].substring(0,rules[i].indexOf("="));
				String value = rules[i].substring(rules[i].indexOf("=")+1);
				if (i>0)
					out.print(",");
				out.print("['"+code+"','"+value+"']");
			}
		}
		%>
	]});

	var actionData = [
		['change','OnChange'],
		['blur','OnBlur']
	];
	var actionList = new Ext.data.SimpleStore({
		fields: ['id','value'],
		data : actionData
	});

	function formatal(value,p,record){
		for (var i = 0; i < actionData.length; i++)
			if (actionData[i][0] == value){
				return actionData[i][1];
			}
		return value;
	};

	var view_rule_cm = new Ext.grid.ColumnModel([{
			header: "动作名",
			dataIndex: 'code',
			align:'right',
			renderer: formatal,
			width: 80,
			editor: new Ext.grid.GridEditor(new Ext.form.ComboBox({
				store: actionList,
				valueField: 'id',
				displayField:'value',
				typeAhead: true,
				mode: 'local',
				maxHeight: 50,
				triggerAction: 'all',
				emptyText:'请选择...',
				selectOnFocus:true,
				editable:true,
				forceSelection: true,
				allowBlank:false
	   	}))
	},{
	   header: "内容",
	   dataIndex: 'value',
	   width: 400,
	   editor: new Ext.grid.GridEditor(new Ext.form.TextArea({
	   	 grow:true,
		   height:200,
		   growMax:200
	   }))
	}]);

var ruleGridRecord = Ext.data.Record.create([
	   {name: 'code',type:'string'},
	   {name: 'value',type:'string'}
  ]);
var rulePanel_grid = new Ext.grid.EditorGridPanel({
	//renderTo:'dlg_view_field',
	ds: view_rule_ds,
	cm: view_rule_cm,
	selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
	enableColLock:false,
	tbar:[{
		text: '新增',
		iconCls:'add',
		handler: function()
		{
			var p = new ruleGridRecord({
				code: '',
				value: ''
			});
			rulePanel_grid.stopEditing();
			view_rule_ds.add(p);
			var row = view_rule_ds.getTotalCount();
			rulePanel_grid.getSelectionModel().selectLastRow();
		}
	} , '-', {
    text:'删除',
    tooltip:'删除选择行',
    iconCls:'remove',
    handler: function()
		{
			var row = rulePanel_grid.getSelectionModel().getSelected();
			if (row != null)
				view_rule_ds.remove(row);
		}
  }]
});

	ruleWindow = new Ext.Window({
      title: '高级配置-(<%=fieldId%>)',
      width: 500,
      height:300,
      minWidth: 300,
      minHeight: 200,
      layout: 'fit',
      plain:true,
      iconCls :'edit',
      buttonAlign:'center',
      modal:true,
      items: [rulePanel_grid],
      buttons: [{
          text: '确定',
          handler: function() {
          	var rule = "";

					  var count = view_rule_ds.getCount();
						var hasExist = false;
						for (var i = 0; i < count; i++) {
							var code = view_rule_ds.getAt(i).get("code");
							var value = view_rule_ds.getAt(i).get("value");
							if (code == null || code.length==0 || value==null || value.length==0)
								continue;
							if (hasExist)
								rule +="&__&";

							rule += code+"="+value;
							hasExist = true;
						}

						grid_formFieldList.getSelectionModel().getSelected().set("advance",rule);
					  ruleWindow.close();
          }
      },{
          text: '取消',
          handler: function() {
          	ruleWindow.close();
          }
      }]
  });
  ruleWindow.show();


</script>

<%}catch(Exception e){e.printStackTrace();}%>
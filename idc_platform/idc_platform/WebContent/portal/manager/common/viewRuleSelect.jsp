<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@page import="com.hp.idc.portal.security.RuleManager"%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
	<link rel="stylesheet" type="text/css" href="../style/css/index.css" />
	<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/extjs/ext-ext.js"></script>
	<script type="text/javascript" src="itsm.js"></script>
	<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
</head>
<body>
<%
String ruleStr = request.getParameter("s_value");
Map ruleMap = RuleManager.parseRuleStrByCategoryToMap(ruleStr);
List ruleList = RuleManager.parseRuleStrByCategoryToList(ruleStr);

%>

<script type="text/javascript">

	var view_rule_ds = new Ext.data.SimpleStore({
	fields: ['code', 'value'],
	data: [
	<%
		if (ruleList==null)
			ruleList = new ArrayList();
		for(int i = 0; i < ruleList.size(); i++) {
			String[] rule = (String[])ruleList.get(i);
			if (i>0)
				out.print(",");
			out.print("['"+rule[0]+"','"+rule[1]+"']");
		}
	%>
	]});

	function formatRuleCode(value,p, record){
		if (value=='ap')
			return "允许个人";
		else if (value=='rp')
			return "拒绝个人";
		else if (value=='ao')
			return "允许组织";
		else if (value=='ro')
			return "拒绝组织";
		else if (value=='ag')
			return "允许工作组";
		else if (value=='rg')
			return "拒绝工作组";
		else if (value=='allow')
			return "允许所有";
		else if (value=='reject')
			return "拒绝所有";
		return value;
	};

	var view_rule_cm = new Ext.grid.ColumnModel([{
			dataIndex: 'code',
			renderer:formatRuleCode,
			align:'right',
			width: 70
	},{
	   header: "值",
	   dataIndex: 'value',
	   width: 320
	}]);

var ruleGridRecord = Ext.data.Record.create([
	   {name: 'code',type:'string'},
	   {name: 'value',type:'string'}
  ]);
var rulePanel_grid = new Ext.grid.EditorGridPanel({
	//renderTo:'dlg_view_field',
	region:'west',
	width: 400,
	split:true,
	ds: view_rule_ds,
	cm: view_rule_cm,
	enableColLock:false,
	tbar:[{
    text:'上移',
    tooltip:'目标行上移',
    iconCls:'up',
    handler: function()
		{
			var cell = rulePanel_grid.getSelectionModel().getSelectedCell();
			if (cell == null || cell[0] == 0)
				return;
			var n = cell[0];
			var r = view_rule_ds.getAt(n);
			view_rule_ds.remove(r);
			view_rule_ds.insert(n - 1, r);
			rulePanel_grid.getSelectionModel().select(n - 1, cell[1]);
		}
  }, '-', {
    text:'下移',
    tooltip:'目标行下移',
    iconCls:'down',
    handler: function()
		{
			var cell = rulePanel_grid.getSelectionModel().getSelectedCell();
			if (cell == null || cell[0] == view_rule_ds.getCount() - 1)
				return;
			var n = cell[0];
			var r = view_rule_ds.getAt(n);
			view_rule_ds.remove(r);
			view_rule_ds.insert(n + 1, r);
			rulePanel_grid.getSelectionModel().select(n + 1, cell[1]);
		}
  }, '-', {
    text:'删除',
    tooltip:'删除选择行',
    iconCls:'remove',
    handler: function()
		{
			var cell = rulePanel_grid.getSelectionModel().getSelectedCell();
			if (cell == null)
				return;
			var n = cell[0];
			var r = view_rule_ds.getAt(n);
			view_rule_ds.remove(r);
		}
  }, '-', {
      text: '说明',
      tooltip:'放在前面的记录优先',
      iconCls:'help',
      disabled:true
  }]
});


var fld_ap = new Ext.form.TreeBox({fieldLabel:"允许人员",singleMode: false,hiddenName:'fld_ap',viewLoader: new Ext.tree.FilterTreeLoader( {baseParams: {child:1, type:3,origin:'',showPerson:true},dataUrl:'ciPersonTree.jsp'}),emptyText:'请选择...',desc:'',width:150});
var fld_rp = new Ext.form.TreeBox({fieldLabel:"拒绝人员",singleMode: false,hiddenName:'fld_rp',viewLoader: new Ext.tree.FilterTreeLoader( {baseParams: {child:1, type:3,origin:'',showPerson:true},dataUrl:'ciPersonTree.jsp'}),emptyText:'请选择...',desc:'',width:150});

var fld_ao = new Ext.form.TreeBox({fieldLabel:"允许组织",singleMode: false,hiddenName:'fld_ao',viewLoader: new Ext.tree.FilterTreeLoader( {baseParams: {child:1, type:3,origin:''},dataUrl:'ciPersonTree.jsp'}),emptyText:'请选择...',desc:'',width:150});
var fld_ro = new Ext.form.TreeBox({fieldLabel:"拒绝组织",singleMode: false,hiddenName:'fld_ro',viewLoader: new Ext.tree.FilterTreeLoader( {baseParams: {child:1, type:3,origin:''},dataUrl:'ciPersonTree.jsp'}),emptyText:'请选择...',desc:'',width:150});
var fld_ag = new Ext.form.TreeBox({fieldLabel:"允许工作组",singleMode: false,hiddenName:'fld_ag',viewLoader: new Ext.tree.FilterTreeLoader( {baseParams: {child:1, type:1,origin:''},dataUrl:'ciPersonTree.jsp'}),emptyText:'请选择...',desc:'',width:150});
var fld_rg = new Ext.form.TreeBox({fieldLabel:"拒绝工作组",singleMode: false,hiddenName:'fld_rg',viewLoader: new Ext.tree.FilterTreeLoader( {baseParams: {child:1, type:1,origin:''},dataUrl:'ciPersonTree.jsp'}),emptyText:'请选择...',desc:'',width:150});

fld_ao.viewLoader.baseParams.showPerson = "false";
fld_ro.viewLoader.baseParams.showPerson = "false";
fld_ag.viewLoader.baseParams.showPerson = "false";
fld_rg.viewLoader.baseParams.showPerson = "false";

var rulePanel_form =  new Ext.form.FormPanel({
		layout:'form',
		labelAlign:'right',
		region:'center',
    collapseFirst:false,
    enableTabScroll:true,
    margins:'0 0 0 0',
		labelWidth:70,
		items:[
			fld_ap,fld_rp,fld_ao,fld_ro,fld_ag,fld_rg,
			new Ext.form.Checkbox({id:"allow",fieldLabel :"允许所有"}),
			new Ext.form.Checkbox({id:"reject",fieldLabel :"拒绝所有"})

		],
		tbar:[{text:'更新至表格',iconCls:'left',
			handler:function() {
				var ap = fld_ap.getValue();
				var rp = fld_rp.getValue();
				var ao = fld_ao.getValue();
				var ro = fld_ro.getValue();
				var ag = fld_ag.getValue();
				var rg = fld_rg.getValue();
				var allow = rulePanel_form.items.get("allow").getValue();
				var reject = rulePanel_form.items.get("reject").getValue();
					if (ap.length!=0) {
						var newRecord = new ruleGridRecord({
							code:'ap',
							value:ap
						});
						view_rule_ds.add(newRecord);
					}
					if (rp.length!=0) {
						var newRecord = new ruleGridRecord({
							code:'rp',
							value:rp
						});
						view_rule_ds.add(newRecord);
					}
					if (ao.length!=0) {
						var newRecord = new ruleGridRecord({
							code:'ao',
							value:ao
						});
						view_rule_ds.add(newRecord);
					}
					if (ro.length!=0) {
						var newRecord = new ruleGridRecord({
							code:'ro',
							value:ro
						});
						view_rule_ds.add(newRecord);
					}
					if (ag.length!=0) {
						var newRecord = new ruleGridRecord({
							code:'ag',
							value:ag
						});
						view_rule_ds.add(newRecord);
					}
					if (rg.length!=0) {
						var newRecord = new ruleGridRecord({
							code:'rg',
							value:rg
						});
						view_rule_ds.add(newRecord);
					}
					if (allow) {
						var newRecord = new ruleGridRecord({
							code:'allow',
							value:allow
						});
						view_rule_ds.add(newRecord);
					}
					if (reject) {
						var newRecord = new ruleGridRecord({
							code:'reject',
							value:reject
						});
						view_rule_ds.add(newRecord);
					}
					resetForm();
			}
		},'-',{
			text:'重置',
			handler: resetForm
		}]
	});
	function resetForm(){
		fld_ap.setValue("");
		fld_ap.view.root.reload();
		fld_rp.setValue("");
		fld_rp.view.root.reload();
		fld_ao.setValue("");
		fld_ao.view.root.reload();
		fld_ro.setValue("");
		fld_ro.view.root.reload();
		fld_ag.setValue("");
		fld_ag.view.root.reload();
		fld_rg.setValue("");
		fld_rg.view.root.reload();
		rulePanel_form.items.get("allow").setValue("");
		rulePanel_form.items.get("reject").setValue("");
	};
  Ext.onReady(function(){

     var viewport = new Ext.Viewport({
          layout:'border',
          items:[new Ext.Panel({
          	layout:'border',
          	border:false,
          	region:'center',
          	items:[rulePanel_form,rulePanel_grid],
          	buttons:[{
		          text: '确定',
		          handler: function() {
		          	var rule = "";
							  var ap = fld_ap.getValue();
							  var rp = fld_rp.getValue();
		
							  var count = view_rule_ds.getCount();
								var hasExist = false;
								for (var i = 0; i < count; i++) {
									var code = view_rule_ds.getAt(i).get("code");
									var value = view_rule_ds.getAt(i).get("value");
									if (value==null || value.length==0)
										continue;
									if (hasExist)
										rule +=";";
									if (code == "allow" || code == "reject")
										rule += code;
									else
										rule += code+":"+value;
									hasExist = true;
								}

							  window.returnValue = rule;
							  window.close();
		          }
			      },{
			          text: '取消',
			          handler: function() {
			          	window.close();
			          }
			      }]
          	
          })]
      });
  });

</script>

 </body>
</html>
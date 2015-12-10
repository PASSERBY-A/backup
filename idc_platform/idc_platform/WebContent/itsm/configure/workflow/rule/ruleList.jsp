<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.workflow.rule.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=GBK");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

String referToWFOid = request.getParameter("referToWFOid");
String referToUserField = request.getParameter("referToUserField");
String referFromTaskOidField = request.getParameter("referFromTaskOidField");
String runPosition = request.getParameter("runPosition");

%>
(function() {

	var ruleDS = new Ext.data.JsonStore({
		fields : ['type','runPosition']
   	});
   	
	function createObjectFormServer(ruleURL,ruleParams,callback){
		Ext.Ajax.request({
		    url: ruleURL,
		    params: ruleParams,
		    success: function(res) {
		        var newComponent = eval(res.responseText);
		        newComponent.show();
		    },
		    failure: function() {
		    	Ext.Msg.alert("获取信息失败", "页面连接失败");
		    }
		});
	};
	
	function menuItemFun(){
   		var ri = getRuleRecordIndex(this.type);
   		var p = {};
   		if (ri > -1)
   			p = ruleDS.getAt(ri).data;
   		createObjectFormServer(this.url,p);
   	};
	
	var ruleArray = [
		{
 			text:'触发新工单',
 			type:'<%=NewTaskRule.class.getName()%>',
 			url:'<%=Consts.ITSM_HOME %>/configure/workflow/rule/newTask.jsp',
		   	handler: menuItemFun
 		},{
 			text:'超时检测',
 			type:'<%=OvertimeCheckRule.class.getName() %>',
 			url : '<%=Consts.ITSM_HOME %>/configure/workflow/rule/overtimeCheck.jsp',
		   	handler: menuItemFun
 		},{
 			text:'自定义短信',
 			type:'<%=CustomSMSRule.class.getName() %>',
 			url : '<%=Consts.ITSM_HOME %>/configure/workflow/rule/customSMS.jsp',
		   	handler: menuItemFun
 		},{
 			text : '自动流转',
 			type : '<%=AutoflowRule.class.getName() %>',
 			url : '<%=Consts.ITSM_HOME %>/configure/workflow/rule/autoflow.jsp',
		   	handler: menuItemFun
 		},{
 			text:'动态代码',
 			type:'<%=DynamicCodeRule.class.getName() %>',
 			url : '<%=Consts.ITSM_HOME %>/configure/workflow/rule/dynamicCode.jsp',
		   	handler: menuItemFun
 		}
	];
	
	var toolbar = new Ext.Toolbar({
		items:[{
        	text:'增加规则',
        	iconCls:'add',
			menu : new Ext.menu.Menu({
        		items: ruleArray
        	})
       	},
		new Ext.Toolbar.Button({
			text: '删除',
			handler: function()
			{
				var cell = ruleGrid.getSelectionModel().getSelected();
				if (cell != null)
					ruleDS.remove(cell);
			}
		}),
		'-',
		new Ext.Toolbar.Button({
			text: '上移',
			handler: function()
			{
				var cell = ruleGrid.getSelectionModel().getSelected();
				if (cell == null)
					return;
				var n = ds.indexOf(cell);
				if (n == 0)
					return;
				var r = ds.getAt(n);
				ruleDS.remove(r);
				ruleDS.insert(n - 1, r);
				ruleGrid.getSelectionModel().selectRow(n - 1, cell);
			}
		}),
		'-',
		new Ext.Toolbar.Button({
			text: '下移',
			handler: function()
			{
				var cell = ruleGrid.getSelectionModel().getSelected();
				if (cell == null)
					return;
				var n = ds.indexOf(cell);
				if (n == ds.getCount() - 1)
					return;
				var r = ds.getAt(n);
				ruleDS.remove(r);
				ruleDS.insert(n + 1, r);
				ruleGridgetSelectionModel().selectRow(n + 1, cell);
			}
		})]
	});
	
	function renderType(value,p,record){
		for (i in ruleArray) {
			if (ruleArray[i].type == value)
				return ruleArray[i].text;
		}
		return value;
	};
	
	function getRuleConf(type){
		for (i in ruleArray) {
			if (ruleArray[i].type == type)
				return ruleArray[i];
		}
		return {};
	};
	
	function getRuleRecordIndex(type){
		var recordCount = ruleDS.getCount();
		for (var i = 0; i < recordCount; i++) {
			if (ruleDS.getAt(i).get("type") == type) {
				return i;
			}
		}
		return -1;
	};
	
	function addRuleRecord(r){
		var recordCount = ruleDS.getCount();
		for (var i = 0; i < recordCount; i++) {
			var r_ = ruleDS.getAt(i);
			if (r_.get("type") == r.get("type")) {
				ruleDS.remove(r_);
				ruleDS.insert(i,r);
				return;
			}
		}
		ruleDS.add(r);
	};
	
	var ruleGrid = new Ext.grid.GridPanel({
		id:'ruleGrid',
		title :'规则配置',
		cm:new Ext.grid.ColumnModel([{
    		header: "规则类型",
    		dataIndex: 'type',
    		renderer: renderType
    	},{
    		header: "运行位置",
    		dataIndex: 'runPosition'
    	}]),
    	ds:ruleDS,
    	border:false,
		viewConfig: {
			autoFill: true
		},
		selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
        enableColLock:false,
        loadMask: true,
        tbar:toolbar,
        listeners:{
			rowdblclick : function(grid, rowIndex, e){
				var p = ruleDS.getAt(rowIndex).data;
				var rr = getRuleConf(p.type);
				createObjectFormServer(rr.url,p);
			}
		}
	});
	return ruleGrid;
})();
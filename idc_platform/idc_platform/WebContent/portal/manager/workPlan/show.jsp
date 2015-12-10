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
%>
<html> 
<head>
<title>工作计划</title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="../style/css/index.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript">
var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [[1,'普通'],[2,'重要'],[3,'里程碑']]
});

Ext.onReady(function(){
	
	Ext.QuickTips.init();
	var form = new Ext.FormPanel({
		title :'工作计划',
		frame : false,
		region: 'center',
		labelWidth : 80,
		items : [{
					xtype : 'textfield',
					fieldLabel : '标题',
					name : 'title',
					value:'<%=bean.getTitle()%>',
					readOnly:true,
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
					readOnly:true,
					value:'<%=bean.getType()%>',
					store : typeData
		        },{
					xtype : 'datefield',
					fieldLabel: '计划完成时间',
	                format:'Y-m-d H:i:s',
	                name: 'planTime',
	                precision:'t',
	                readOnly:true,
	                value:'<%=sdf.format(bean.getPlanTime())%>',
					anchor:'-20'
				},{
					xtype : 'textarea',
					fieldLabel : '计划描述',
					name : 'desc',
					readOnly:true,
					value:'<%=StringUtil.escapeJavaScript(bean.getDescription())%>',
					anchor:'-20'
				}],
		buttons : [{
				text : '完成计划',
				iconCls :'icon-save',
				handler : function() {
					Ext.Ajax.request( {
					        url : 'action.jsp',
					        method : 'post',
					        params : {
					            action : 'update',   
					            oid : '<%=oid%>',
					            isFinish:'1'
					        },
					        success : function(response, options) {   
					            var o = Ext.util.JSON.decode(response.responseText); 
					            alert(o.msg);
					            window.close();
					        },
					        failure : function() { 
					        }
					    });
				}
			},{
				text : '关闭',
				iconCls :'icon-cancel',
				handler : function() {
					window.close();
				}
			}]
	});
	var viewport = new Ext.Viewport({
		layout: "border",
		items: [form]
	}); 
});
</script>
</head>
<body>
</body>
</html>
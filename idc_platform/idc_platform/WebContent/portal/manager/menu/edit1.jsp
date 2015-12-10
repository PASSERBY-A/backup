<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.bean.Menu"%>
<%@page import="com.hp.idc.portal.mgr.MenuMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>
<%@include file="../../getUser.jsp" %>

<%
	String title = request.getParameter("title");

	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
	Menu bean = mgr.getBeanByTitle(title);
	
%>
<script type="text/javascript">

var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [
	    	<%
	    		List<Menu> list = new ArrayList<Menu>();
	    	list = mgr.getBeanByFilter(userId,"-1");
	    	int i = 0;
	    	for(Menu m : list){
	    		if(i>0)
	    			out.write(",");
	    		out.write("["+m.getOid()+",'"+m.getTitle()+"']");
	    		i++;
	    	}
	    	%>
	    	]
})

var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	baseParams: {action:'update',oid:'<%=bean.getOid() %>'}, 
	method : 'POST',
	autoscroll : true,
	labelWidth : 80,
	items : [{
				xtype : 'textfield',
				fieldLabel : '模块名称',
				name : 'title',
				allowBlank : false,
				anchor:'-20',
				value:'<%=bean.getTitle()%>'
			},{
	            xtype: 'hidden',
	            name: 'type',
	            value: '-1'
	        },{
				xtype : 'hidden',
				fieldLabel : '模块URL',
				name : 'url',
				value:'null'
			},{
				xtype : 'selDlgfield',
				fieldLabel : '菜单授权',
				selectUrl : '../common/viewRuleSelect.jsp',
				name : 'role',
				value:'<%=StringUtil.removeNull(bean.getRole())%>',
				anchor:'-10'
			},{
				xtype : 'textfield',
				fieldLabel : '排序号',
				name : 'orderno',
				value:'<%=bean.getOrderNo() %>',
				anchor:'-20'
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
			title : '编辑模块',
			border : false,
			modal : true,
			width : 400,
			height : 300,
			minWidth : 400,
			minHeight : 300,
			closable : true,
			layout : 'fit', // window的默认布局
			plain : true, // 颜色透明
			items : form,
			buttons : [{
				text : '保存',
				iconCls :'icon-save',
				handler : function() {
					var form = win._form.form;
					if (!(form.isValid())) {
						Ext.Msg.alert("提示", "请填写正确的内容！");
						return;
					}
					form.submit({
						waitMsg : '正在更新，请稍候...', // form提交时等待的提示信息
						// 提交成功后的处理
						success : function(form, action) {   
				            var o = Ext.util.JSON.decode(action.response.responseText); 
				            alert(o.msg);
				            win.hide();
				            store.load();
				        },
						// 提交失败后的处理
						failure : function(form, action) {
							var o = Ext.util.JSON.decode(action.response.responseText); 
				            alert(o.msg);
				            win.hide();
				            store.load()
						}
					});
				}
				}, {
					text : '取消',
					iconCls :'icon-cancel',
					handler : function() {
						win.hide();
					}
				}]
		});

win._form = form;
win.show();
</script>

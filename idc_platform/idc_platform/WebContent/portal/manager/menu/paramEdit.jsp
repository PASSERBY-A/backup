<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.regex.*"%>
<%@page import="com.hp.idc.portal.bean.Menu"%>
<%@page import="com.hp.idc.portal.security.Cache"%>
<%@page import="com.hp.idc.portal.bean.MenuParams"%>
<%@page import="com.hp.idc.portal.mgr.MenuParamsMgr"%>
<%@page import="java.util.*"%>
<%@include file="../../getUser.jsp" %>
<%
	String menuIdStr = request.getParameter("menuId");
	int menuId = Integer.parseInt(menuIdStr);
	Menu menu = Cache.MenuMap.get(menuIdStr);
	Pattern p = Pattern.compile("#\\d+");
	Matcher m = p.matcher(menu.getUrl());
	StringBuffer dsData = new StringBuffer();
	
	MenuParams mp = MenuParamsMgr.getBeanById(userId,menuId);
	Map<String,String> params = new HashMap<String,String>();
	if(mp!=null){
		//处理参数记录，放入map中
		String[] paramArr = mp.getParams().split(",");
		for(String param:paramArr){
			String[] temp=param.split("=");
			params.put(temp[0],temp[1]);
		}
	}
	
	if(m.find()){
		String key = m.group();
		String val = params.get(key)==null?"":params.get(key);
		dsData.append("['"+key+"','"+val+"']");
	}else{
		out.println("<script type='text/javascript'>alert('该菜单链接不带参数')</script>");
		return;
	}
	while(m.find()){
		String key = m.group();
		String val = params.get(key)==null?"":params.get(key);
		dsData.append(",['"+key+"','"+val+"']");
	}
%>
<script type="text/javascript">
var cm = new Ext.grid.ColumnModel([{
			header : "参数名",
			dataIndex : 'id',
			width : 100
		}, {
			header : "参数值",
			dataIndex : 'value',
			editor : new Ext.form.TextField(),
			width : 180
		}]);

var dsData = [<%=dsData.toString()%>];
var ds = new Ext.data.SimpleStore({
	fields : ['id', 'value'],
	data : []
});
var Plant = Ext.data.Record.create([
	{name : 'id',type : 'string'}, 
	{name : 'value',type : 'string'}]);

var grid_formFieldList = new Ext.grid.EditorGridPanel({
	ds : ds,
	cm : cm,
	selModel : new Ext.grid.RowSelectionModel({
				singleSelect : true
			}),
	enableColLock : false,
	loadMask : true,
	anchor : '100% 100%'
});

if (win)
	win.hide();
var win = new Ext.Window({
		title : '编辑菜单参数',
		border : false,
		modal : true,
		width : 300,
		height : 200,
		minWidth : 300,
		minHeight : 200,
		closable : true,
		layout : 'form', // window的默认布局
		labelWidth : 30,
		plain : true, 	// 颜色透明
		items : [{
				xtype:'label',
	            text : '<%=menu.getUrl()%>',
	            anchor:'-10'
			},
			grid_formFieldList],
		buttons : [{
						text : '保存',
						iconCls :'icon-save',
						handler : function() {
							var ds = grid_formFieldList.getStore();
							var count = ds.getCount();
							var params = "";
							for (var i = 0; i < count; i++) {
								if(i>0)
									params +=",";
								params +=ds.getAt(i).get("id")+"="+ds.getAt(i).get("value");
							}
							Ext.Ajax.request({
						        url : 'action.jsp',
						        method : 'post',
						        params : {
						            action : 'paramEdit',
						            menuId : '<%=menuId%>',
						            params : params
						        },
						        success : function(response, options) {   
						            var o = Ext.util.JSON.decode(response.responseText); 
						            alert(o.msg);
						            store.load();
						        },
						        failure : function() { 
						        }
						    });
							win.hide();
						}
					}, {
						text : '取消',
						iconCls :'icon-cancel',
						handler : function() {
							win.hide();
					}
				}]
	});
ds.loadData(dsData);
win.show();
</script>

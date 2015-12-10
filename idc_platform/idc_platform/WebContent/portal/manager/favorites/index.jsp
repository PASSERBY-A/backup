<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.*"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.bean.*"%>
<%@page import="com.hp.idc.json.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.security.Cache"%>
<%@include file="../../getUser.jsp" %>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<%
	FavoritesMgr mgr = (FavoritesMgr)ContextUtil.getBean("favoritesMgr");
	Favorites favorites = mgr.getBeanByUserId(userId);
	
	String menuIds = favorites.getMenuIds();
	
	JSONArray arr = new JSONArray();
	JSONObject temp = null;
	Iterator<Map.Entry<String, Menu>> iter = Cache.MenuMap.entrySet().iterator(); 
	while (iter.hasNext()) {
		Map.Entry<String, Menu> entry =  iter.next(); 
		temp = new JSONObject();
	//	Object key = entry.getKey(); 
	    Menu menu = entry.getValue(); 
		
	    /*Ȩ���ж�*/
		temp.put("boxLabel", menu.getTitle());
		temp.put("name", menu.getOid());
		String menuIdStr = ","+menu.getOid()+",";
		if(menuIds.contains(menuIdStr))
			temp.put("checked", true);
		else
			temp.put("checked", false);
		
		arr.put(temp);
	}
%>
<html> 
<head>
<title>�����ƻ�</title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="../style/css/index.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript">
var comData=<%=arr.toString()%>; //ȡ����

Ext.onReady(function(){
	Ext.QuickTips.init();
    var formPanel = new Ext.FormPanel({
		frame: true,
        title:'�ղؼй���',
        region: 'center',
        baseParams: {action:'commit'}, 
        url : 'action.jsp',
		method : 'POST',
        items: [{
                xtype: 'checkboxgroup',
                columns: 6,
                name:'menuIds',
                fieldLabel: '��ѡ�ղ�',
                items: comData
            }],
		buttons: [{
			text: '�ύ',
			handler: function(){
				var form = formPanel.form;
				formPanel.baseParams.menuIds=formPanel.getForm().getValues(true)
				form.submit({
					waitMsg : '���ڸ��£����Ժ�...', // form�ύʱ�ȴ�����ʾ��Ϣ
					// �ύ�ɹ���Ĵ���
					success : function(form, action) {   
			            var o = Ext.util.JSON.decode(action.response.responseText); 
			            alert(o.msg);
			        },
					// �ύʧ�ܺ�Ĵ���
					failure : function(form, action) {
						var o = Ext.util.JSON.decode(action.response.responseText); 
			            alert(o.msg);
					}
				});
	       }
        },{
            text: '����',
            handler: function(){
                window.location.reload();
            }
        }]
    });
    var viewport = new Ext.Viewport({
		layout: "border",
		items: [formPanel]
	}); 
});
</script>
</head>
<body>
</body>
</html>
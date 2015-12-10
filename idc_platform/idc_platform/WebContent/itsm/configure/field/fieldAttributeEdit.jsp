<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import=" org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>
</head>
<body>
<%
try {
String id = request.getParameter("fieldid");
String origin = request.getParameter("origin");
String type  ="";
FieldInfo field = FieldManager.getFieldById(origin,id);
int oid = -1;
if (field != null)
	oid = field.getOid();
int width=85;

%>

<script defer>

var form = new Ext.form.FormPanel({
    baseCls: 'x-plain',
	method: 'POST',
	autoScroll: true,
	region:'center',
    defaultType: 'textfield',
	errorReader: new Ext.form.XmlErrorReader(),
	labelWidth: 35, // label settings here cascade unless overridden
	url:'<%=Consts.ITSM_HOME%>/configure/fieldPost.jsp',
	tbar:[{text:'保存',iconCls:'left'}]
});

form.add(
	new Ext.form.TextField({
		fieldLabel: 'ID',
		name: 'fld_id',
		width: <%=width%>,
		allowBlank:false,
		value:"<%=id%>",
		readOnly:true
	}),

	new Ext.form.TextField({
		fieldLabel: '名称',
		name: 'fld_name',
		width: <%=width%>,
		value:'<%=field.getName()%>'
	}),
	
	new Ext.form.Checkbox({
	  fieldLabel: '只读',
	  name: 'readOnly',
	  allowBlank:true
  }),
	
	new Ext.form.Checkbox({
	  fieldLabel: '必填',
	  name: 'notNull',
	  allowBlank:true
  }),
	
	new Ext.form.Checkbox({
	  fieldLabel: '填充',
	  name: 'injectData',
	  allowBlank:true
  }),

	new Ext.form.TextField({
		fieldLabel: '条件',
		name: 'filter',
		width: <%=width%>
		
	}),
	
	new Ext.form.TextField({
		fieldLabel: '行数',
		name: 'rowNum'
		<%if (!(field instanceof StringFieldInfo)) {%>
		,readOnly:true
		<%}%>
		,width: <%=width%>
	})
);


Ext.onReady(function(){
	 var viewport = new Ext.Viewport({
      layout:'border',
      items:[form]
   });
});

<%

}catch(Exception e){
	e.printStackTrace();
}%>


</script>

 </body>
</html>
<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="java.util.*"%>
<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
</head>

<script type="text/javascript">
Ext.onReady(function(){
	var viewport = new Ext.Viewport({
        layout:'border',
		items:[ {
				region:'west',
				contentEl: 'west',
				split:false,
				width: 200,
				minSize: 175,
				maxSize: 400,
				collapsible: false,
				autoScroll: true,
				margins:'0 0 0 0',
				layoutConfig:{
					animate:false
				}
			}, {
				margins:'0 0 0 0',
				region:'center',
				contentEl: 'main',
				autoScroll: true
			}]
	});
});

Ext.onReady(function(){
    var tree = new Ext.tree.TreePanel({
		el:'west',
        animate:false,
        enableDD:true,
		border: false,
		autoHeight : true,
        containerScroll: true
    });

	var root = new Ext.tree.TreeNode({
		text: '代码类型',
		draggable:false
	});
<%
List codeTypes = CIManager.getCodeTypes();
ItsmUtil.sort(codeTypes, "name", false);
for (int i = 0; i < codeTypes.size(); i++) {
	CodeTypeInfo cc = (CodeTypeInfo)codeTypes.get(i);
	out.println("root.appendChild(new Ext.tree.TreeNode({\n"
		+ "text: '" + cc.getName() + "',\n");
     out.println("href: 'javascript:loadCode(" + cc.getOid() + ")',\n");
 out.println("draggable:false" + "}));\n");
}
%>
   tree.setRootNode(root);

    // render the tree
    tree.render();
	root.expand();

loadCode(<%=Consts.CODETYPE_CICATEGORY%>);

});

function loadCode(oid)
{
	Ext.get('mainFrame').dom.src = "codeList.jsp?oid=" + oid;
}

</script>

<body>
	<div id="west"></div>
	<div id="main">
		<iframe id="mainFrame" frameborder="no" width="100%" height="100%"></iframe>
	</div>
</body>

</html>

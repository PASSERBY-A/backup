<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.model.*"%>
<%@ page import="com.hp.idc.resm.expression.*"%>
<%@ page import="com.hp.idc.resm.resource.*"%>
<%@ page import="com.hp.idc.resm.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>测试</title>
	<link rel="stylesheet" type="text/css" href="/resm/extjs3/resources/css/ext-all.css" />
    <!-- ENDLIBS -->
 	<script type="text/javascript" src="/resm/extjs3/adapter/ext/ext-base.js"></script>
  <script type="text/javascript" src="/resm/extjs3/ext-all.js"></script>
    <link rel="stylesheet" type="text/css" href="/resm/extjs3/examples/ux/css/LockingGridView.css" />
    <script type="text/javascript" src="/resm/extjs3/examples/ux/LockingGridView.js"></script>
</head>
<body>
属性数据测试：对照下表，对比数据库中的resm_attribute表数据。
<script>
Ext.onReady(function(){
    // sample static data for the store
    var myData = [
<%
	List<AttributeDefine> list = ServiceManager.getAttributeService().getAllAttributes();
	for (int i = 0; i < list.size(); i++) {
		AttributeDefine a = list.get(i);
		if (i > 0)
			out.println(",");
		out.print("['" + StringUtil.escapeJavaScript(a.getId()) + "'");
		out.print(",'" + StringUtil.escapeJavaScript(a.getName()) + "'");
		out.print(",'" + StringUtil.escapeJavaScript(a.getRemark()) + "']");
	}
%>
    ];

    // create the data store
    var store = new Ext.data.ArrayStore({
        fields: [
           {name: 'id'},
           {name: 'name'},
           {name: 'remark'}
        ],
        data: myData
    });

    // create the Grid
    // To use locking functionality we must explicitly specify the LockingColumnModel and the LockingGridView
    var grid = new Ext.grid.GridPanel({
        store: store,
        colModel: new Ext.ux.grid.LockingColumnModel([
            {header: 'id',      width: 160, sortable: true, dataIndex: 'id', locked: true, id:'id'},
            {header: 'name',        width: 120,  sortable: true, dataIndex: 'name'},
            {header: 'remark',       width: 400,  sortable: true, dataIndex: 'remark'}
        ]),
        stripeRows: true,
        height: 400,
        width: '100%',
        title: '属性列表',
        view: new Ext.ux.grid.LockingGridView()
    });
    
    // render the grid to the specified div in the page
    grid.render(Ext.getBody());
});
</script>
</body>
</html>

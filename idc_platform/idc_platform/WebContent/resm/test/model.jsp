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
<title>����</title>
	<link rel="stylesheet" type="text/css" href="/resm/extjs3/resources/css/ext-all.css" />
    <!-- ENDLIBS -->
 	<script type="text/javascript" src="/resm/extjs3/adapter/ext/ext-base.js"></script>
  <script type="text/javascript" src="/resm/extjs3/ext-all.js"></script>
   <script type="text/javascript" src="/resm/extjs3/examples/ux/ColumnNodeUI.js"></script>
    <link rel="stylesheet" type="text/css" href="/resm/extjs3/examples/ux/css/ColumnNodeUI.css" />
</head>
<body>
ģ�����ݲ��ԣ������±��Ա����ݿ��е�resm_model�����ݡ�
<script>
Ext.onReady(function(){
    var tree = new Ext.ux.tree.ColumnTree({
        width: "100%",
        height: "100%",
        rootVisible:false,
        autoScroll:false,
        border:false,
        title: '��Դģ���б�',
        renderTo: Ext.getBody(),
        
        columns:[{
            header:'id',
            width:300,
            dataIndex:'modelId'
        },{
            header:'ģ������',
            width:100,
            dataIndex:'name'
        },{
            header:'parentId',
            width:100,
            dataIndex:'parentId'
        }],

        loader: new Ext.tree.TreeLoader({
            dataUrl:'modelQuery.jsp',
            uiProviders:{
                'col': Ext.tree.ColumnNodeUI
            }
        }),

        root: new Ext.tree.AsyncTreeNode({
            text:'Tasks'
        })
    });
});
</script>
</body>
</html>

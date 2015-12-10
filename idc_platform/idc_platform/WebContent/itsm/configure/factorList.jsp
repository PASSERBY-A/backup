<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

%>
<html>
<head>
  <title>视图列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
</head>

<body>

	<div id="view-dialog">
		<div id="content">
		<div id="center"></div>
		<div id="south"></div>
		</div>
	</div>
	<script>

	function addView() {
		viewDetail({"userId": ''});
	}
  function viewDetail(paramList){
		Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/factorEdit.jsp",
		paramList);
  }
 var viewgrid = null;
Ext.onReady(function(){

    // create the Data Store
    var factords = new Ext.data.SimpleStore({
        fields: ['operName', 'factor'],
				data : [
					<%
						Object[] keys = Cache.Factors.keySet().toArray();
						for (int i = 0; i < keys.length; i++){
							if (i > 0)
								out.println(",");
							PersonFieldInfo pfInfo = new PersonFieldInfo();
							FactorInfo fi = Cache.Factors.get(keys[i]);
							out.println("['"+keys[i]+"','"+pfInfo.getPathValue(fi.getFactors())+"']");
						}
					%>
				]
    });

    var factorcm = new Ext.grid.ColumnModel([{
    		header: "人员",
    		dataIndex: 'operName'
    	},{
    		header: "代理人",
    		dataIndex: 'factor'
    	}]);


    // create the editor grid
  factorgrid = new Ext.grid.GridPanel({
      store: factords,
      cm: factorcm,
      sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
      loadMask: true,
      border:false,
      viewConfig: {
				autoFill: true
			}
    });

    var viewport = new Ext.Viewport({
			layout:'border',
      items:[{
          region:'center',
          split:true,
          width: 225,
          minSize: 175,
          maxSize: 400,
          layout:'fit',
          margins:'1 1 1 1',
          items: factorgrid
			}]
	});

    factorgrid.on("rowdblclick", function(factorgrid, rowIndex, e)
		{
			var id = factorgrid.getStore().getAt(rowIndex).get("operName");
			viewDetail({"userId": id});
		});

});
</script>
</body>
</html>
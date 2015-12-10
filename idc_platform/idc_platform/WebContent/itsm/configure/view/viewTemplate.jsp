<%@page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.security.rule.*"%>
<html>
<head>
  <title>视图列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>
<%@ include file="../../getUser.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

int viewOid = -1;
if (request.getParameter("viewOid")!=null && !request.getParameter("viewOid").equals(""))
	viewOid = Integer.parseInt(request.getParameter("viewOid"));
ViewInfo vInfo = ViewManager.getViewByOid(viewOid);
List columns = new ArrayList();
List searchList = new ArrayList();
Map actions = new HashMap();
List buttons = new ArrayList();

int limit = 1000;
String beginTime = "";
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
if (vInfo!=null) {
	String recordCount = vInfo.getAttribute("recordCountPerPage");
	if (recordCount != null && !recordCount.equals(""))
		limit = Integer.parseInt(recordCount);
	if (limit <= 0)
	 limit = 1000;
	 
	String rule = vInfo.getRule();
	if (!RuleManager.valid(userId, rule, true)) {
		out.print("<b>你没有权限查看此视图</b><br>");
		out.print("<font color='gray'>"+RuleManager.ruleDesc(rule)+"</font>");
		return;
	}
	columns = vInfo.getColumns();
	actions = vInfo.getAction();
	searchList = vInfo.getSearchCol();
	buttons = vInfo.getButton();

	String beforeDay = vInfo.getAttribute("beforeDayNo");

	if (beforeDay!=null && !beforeDay.equals("")) {
		long before = (long)24000.0*3600*Integer.parseInt(beforeDay);
		beginTime = sdf.format(new java.util.Date(System.currentTimeMillis()-before));
	}



} else {
	out.print("找不到此视图("+viewOid+")");
	return;
}

String defaultEnd = sdf.format(new java.util.Date(System.currentTimeMillis()));
String defaultDisplay = "";
if (!beginTime.equals("") && beginTime!=null)
	defaultDisplay = "(默认:"+beginTime+"~"+defaultEnd+")";
else
	beginTime = "2007-01-01";
session.setAttribute("viewOid",viewOid+"");
session.setAttribute("viewName",vInfo.getName()+defaultDisplay);

String groupByField = "";
if (vInfo!=null && vInfo.getGroupField()!=null && !vInfo.getGroupField().equals("")) {
	groupByField = vInfo.getGroupField();
}
String sortField = "";
if (vInfo!=null && vInfo.getDefaultSortField()!=null && !vInfo.getDefaultSortField().equals("")) {
	sortField = vInfo.getDefaultSortField();
}
%>
</head>
<body>
	<iframe id="itsm_default_iframe" name="itsm_default_iframe" style="display:none"></iframe>

	<div id="view-grid"></div>
	<script>


	function setOvertime(value, p, record){
		if (record.get('overtime') == 'true') 
			return String.format('<font color=red>{0}</font>', value); 
		return value;
	}
		
	var grid_taskList = null;
		// create the Data Store
    var viewds = new Ext.data.GroupingStore({
        proxy: new Ext.data.HttpProxy({
            url: '<%=Consts.ITSM_HOME%>/configure/view/viewTemplateQuery.jsp'
        }),
 				baseParams:{'viewOid':<%=viewOid%>,'fld_system_create_time_b':'<%=beginTime%>'},
        reader: new Ext.data.JsonReader({
            root: 'items',
            totalProperty: 'totalCount'
        }, [
	   		{name: 'oid', mapping: 'oid'},
	   		{name: 'wait', mapping: 'wait'},
	   		{name: 'dataId', mapping: 'dataId'},
	   		{name: 'origin', mapping: 'origin'},
	   		{name: 'overtime', mapping: 'overtime'},
	   		{name: 'wfOid', mapping: 'wfOid'},
	   		{name: 'wfVer', mapping: 'wfVer'},
	   		{name: 'dealPage', mapping: 'dealPage'}
			<%
			for (int i = 0; i < columns.size(); i++) {
				ViewColumnInfo column = (ViewColumnInfo)columns.get(i);

				out.print(",");
				out.print("{");
				out.print("name:'"+column.getNameEN()+"',");
				out.print("mapping:'"+column.getNameEN()+"'");
				out.println("}");
			}
	        %>
        ])
        <%if (!sortField.equals("")) {%>
      		,sortInfo:{field: '<%=sortField%>', direction: 'DESC'}
      	<%} else {%>
	        ,sortInfo:{field: 'wait', direction: 'ASC'}
      	<%}  
 		if (!groupByField.equals("")) {%>
        	,groupField:'<%=groupByField%>'
      	<%}%>
    });

Ext.onReady(function(){
<%
	if ("true".equals(vInfo.getAttribute("mutiSelect"))){
%>
    var sm = new Ext.grid.CheckboxSelectionModel();
<%}%>
    var viewcm = new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer(),
		<%if ("true".equals(vInfo.getAttribute("mutiSelect"))){%>
		sm,
		<%}%>
		{header:'OID',dataIndex:'oid',width:40,sortable: true},
		/*{header:'来源',dataIndex:'origin',width:40,sortable: true},*/
		<%
		int columnCount = 0;
		for (int i = 0; i < columns.size(); i++) {
			ViewColumnInfo column = (ViewColumnInfo)columns.get(i);
			if (columnCount>0)
				out.print(",");
			out.print("{");
			out.print("header:'"+column.getNameZH()+"',");
			if (column.isSort())
				out.print("sortable: true,");
			out.print("dataIndex:'"+column.getNameEN()+"'");
			out.print("}");
			columnCount ++;
		}
		if(viewOid != 3){
		%>
		,{header:'处理状态', dataIndex:'wait', width: 60, sortable: true}
		<%}%>
	]);

    // create the editor grid
    var viewgrid = new Ext.grid.GridPanel({
        ds: viewds,
        cm: viewcm,
        <%if ("true".equals(vInfo.getAttribute("mutiSelect"))){%>
			sm: sm,
  		<%}%>
		border:false,
		loadMask: true,
		enableColLock:false,
		viewConfig: {
			forceFit:true
		},
		bbar: new Ext.PagingToolbar({
			pageSize: <%=limit%>,
			store: viewds,
			displayInfo: true,
			displayMsg: '<%=Consts.MSG_PAGE_DISPLAY%>',
			emptyMsg: "<%=Consts.MSG_PAGE_EMPTY%>",
			items: ['-' ,{
	            text:'查询',
	            tooltip:'检索记录',
	            iconCls:'search',
	            handler: function() {
	            	Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/view/viewSearch1.jsp?viewOid=<%=viewOid%>");
	            }
        	},'-' ,{
                text:'导出到excel',
                iconCls:'excel',
                handler: downloadFile
            }
        	<%
			for (int i = 0; i < buttons.size(); i++) {
				String[] key = (String[])buttons.get(i);
				if (key[0]==null || key[0].equals(""))
					key[0] = "新按钮";
					if (!key[1].startsWith("function(){"))
						key[1] = "function(){"+key[1];
					if (!key[1].endsWith("}"))
						key[1] += "}";
			%>
			,'-',{
	            text:'<%=key[0]%>',
	            handler: <%=key[1]%>
	        }
			<%}%>
			]

		  })
	      <%if (vInfo!=null && vInfo.getGroupField()!=null && !vInfo.getGroupField().equals("")) {%>
	      ,view: new Ext.grid.GroupingView({
            forceFit:true,
            groupTextTpl: '{text} ({[values.rs.length]} {["条"]})'
        })
      	<%}%>
        ,
        collapsible: true,
        animCollapse: false
    });

    viewgrid.getView().getRowClass = function(record, index){
		if(record.data.overtime=='true')
			return "x-overtime";
    	  
	}; 
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
            items: viewgrid
			}]
	});

	// 给表格动作加上事件
	<%
		Set aset = actions.keySet();
		for (Iterator ite = aset.iterator();ite.hasNext();) {
			String key = (String)ite.next();
			String value = (String)actions.get(key);
			out.print("viewgrid.on('"+key+"', "+value+");");
		}
	%>
    // trigger the data store load\
    viewds.load({params:{start:0}});

	grid_taskList = viewgrid;
});


function taskPostCallBack(ret){
	viewds.reload();
}

function downloadFile() {

	Ext.getBody().mask("正在下载，请稍候", 'x-mask-loading');
	var http = null;
	var activeX = ['MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP'];
	try {
		http = new XMLHttpRequest();
	} catch(e) {
		for (var i = 0; i < activeX.length; ++i) {
			try {
				http = new ActiveXObject(activeX[i]);
				break;
			}
			catch(e) {
			}
		}
	}
	if (http == null) {
		Ext.getBody().unmask();
		alert("下载失败");
		return;
	}
	http.open("GET", "<%=Consts.ITSM_HOME%>/configure/view/viewTemplateExport.jsp?"+Ext.urlEncode(viewds.baseParams), false);
	http.send();
	document.getElementById("exportForm").JSONStr.value = http.responseText;
	var fireOnThis = document.getElementById("exportForm").b1;
	if (document.createEvent)
	{
		var evObj = document.createEvent('MouseEvents');
		evObj.initEvent( 'click', true, false );
		fireOnThis.dispatchEvent(evObj);
	}
	else if (document.createEventObject)
	{
		fireOnThis.fireEvent('onclick');
	}
	Ext.getBody().unmask();
}
</script>
	
<div style='display:none'>
<form id="exportForm" name="exportForm" action="<%=request.getContextPath() %>/saveAsExcel" method="post">
	<input type="text" name="JSONStr" value="" /><br>
	<input type="submit" name=b1 value="submit" onclick="this.form.submit();" />
</form>
</div>
</body>
</html>
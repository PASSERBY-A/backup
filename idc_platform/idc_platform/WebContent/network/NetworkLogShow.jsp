<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ include file="getUserId.jsp"%>
<html>
<head>
<title>Network Management</title>
</head>
<%@ taglib prefix="s" uri="/struts-tags" %>
<jsp:include page="../common/inc/header.jsp"/>
<script language="javascript">
Ext.onReady(function() {
	var columnSub01 = new Ext.Panel({
		layout : 'form',
		border : false,
		defaults: {width: 150},
		labelWidth:80,
		bodyStyle : 'padding: 0 30 0 0',
		defaultType: 'textfield',
		items : [
			{	
				fieldLabel:'时间',
                id:'datex',
                name:'datex'	}
		] 
	});
	var columnSub02 = new Ext.Panel({
		layout : 'form',
		border : false,
		defaults: {width: 150},
		labelWidth:80,
		bodyStyle : 'padding: 0 0 0 30',
		defaultType: 'textfield',
		items : [
			{	
				fieldLabel:'监控设备',
                id:'monitorname',
                name:'monitorname'
			}
		]
	});
	var formSubCard = new Ext.Panel({
		layout : 'column',
		frame : false,
		border : false,
		anchor : '100%',
                width : '100%',
		items : [columnSub02]
	});
	var queryForm=new Ext.form.FormPanel({
		region:'north',
		frame:true,
                height:200,
		title:'监控设备状态查询',
		items:[formSubCard],
		buttons:[
			{
				text:'搜索',
				handler:function(){
					search();
				}
			},
			{
				text:'清除',
				handler:function(){
					queryForm.form.reset();
				}
			}
		]
	});


         var store = new Ext.data.Store(
    {
        baseParams:{ajax:true},
        proxy: new Ext.data.HttpProxy({
        	url:'NetworkLogShowALL.do'
        }),
        reader: new Ext.data.JsonReader({
	            totalProperty: 'totalCount',
	            root: 'result'
	        }, [  
	          	{name: 'monitorname'},
                        {name: 'status'},
                        {name: 'datex'}
	    	]
	    )
       
    });

     store.on('beforeload',function(s,options){	
 		Ext.apply(s.baseParams,{monitorname : Ext.getCmp("monitorname").getValue()
 		});
 	});
    
     var grid = new Ext.grid.GridPanel({
    	id:'grid',
        region:'center',
        title:'查询结果',
        store: store ,
        disableSelection:true,
        loadMask: true,
        viewConfig: {
            forceFit: true
        },
        //autoExpandColumn:'descn', 
        columns: [
        { header: '日期', dataIndex: 'datex',width:30},
        { header: '监控对象名称', dataIndex: 'monitorname',width:30},
        { header: '状态', dataIndex: 'status'}
        ],
        animCollapse: false,
        bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    store: store,
                    displayInfo: true,
                    displayMsg: '第{0} 到 {1} 条数据 共{2}条',
                    emptyMsg: "没有数据"
        })
        
    });
    store.load({params:{start:0, limit:20,monitorname: Ext.getCmp("monitorname").getValue()}});
    var viewport= new Ext.Viewport({
		renderTo:Ext.getBody(), 
		layout:'border',
		items:[queryForm,grid]
	});

    


}); 
function searchEmp(){
	Ext.getCmp('grid').store.reload({params:{start:0,limit:20,monitorname: Ext.getCmp("monitorname").getValue()}});
}

function search(){
	Ext.getCmp('grid').store.reload({params:{start:0,limit:20,monitorname: Ext.getCmp("monitorname").getValue()}});
}

</script>
<body background="<%=request.getContextPath()%>/images/background.gif">
</body>
</html>
<%@ page language="java" contentType="text/html; charset=gbk"%>
<html> 
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/portal/manager/style/css/index.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript">

 var store = new Ext.data.Store(
    {
        baseParams:{ajax:true},
        proxy: new Ext.data.HttpProxy({
        	url:'NetworkAlertShowALL.action'
        }),
        reader: new Ext.data.JsonReader({
	            totalProperty: 'totalCount',
	            root: 'result'
	        }, [  
	          	{name: 'timed'},
                        {name: 'state'},
                        {name: 'monitorName'},
                        {name: 'severity'},
                        {name:'eventConfirmer'}
	    	]
	    )
       
    });

  

function getType(value, p, record){
	if(value=='2'){
		return '错误';
	}else if(value=='3'){
		return '告警';
	}else{
		return '严重';
	}
}

function getType(value){
	if(value=='2'){
		return 'Error';
	}else if(value=='3'){
		return 'Warning';
	}else if(value=='1'){
		return 'Critical';
	}else {
        return '';
        }
}

//fileTypeData		
var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [[-1,'全部'],[1,'严重'],[2,'错误'],[3,'告警']]
})

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : ['查询 ','告警级别: ',{
            xtype: 'combo',
            id:'type',
            value: '全部',
            width : 70,
            valueField : 'id',
			displayField : 'value',
			typeAhead : true,
			mode : 'local',
			maxHeight : 150,
			triggerAction : 'all',
			emptyText : '请选择...',
			selectOnFocus : true,
			editable : false,
			forceSelection : true,
			allowBlank : false,
			value:'-1',
			store : typeData
        },'监控设备: ',new Ext.form.TextField({
			id:'key',
			width : 150
		}),'事件确认人： ',new Ext.form.TextField({
			id:'eventConfirmer',
			width : 150
		}),
                      {
			id : 'search',
			tooltip : '点击搜索',
			iconCls : 'icon-search',
			// enableToggle : true,
			handler : function() {
                                store.baseParams.mode = 'filter';
				store.baseParams.type = Ext.getCmp('type').getValue();
				store.baseParams.keyWords = Ext.getCmp('key').getValue();
    			search();
			}
		}
		]
});


Ext.onReady(function(){
	
	var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : false
		});
	 var grid = new Ext.grid.GridPanel({
    	id:'grid',
        region:'center',
        store: store ,
        stripeRows: true,
        loadMask: true,
        viewConfig: {
            forceFit: true
        },
        //autoExpandColumn:'descn', 
        sm:sm,
        cm : new Ext.grid.ColumnModel([
        { header: '告警发生日期', dataIndex: 'timed',sortable : true,width:30},
        { header: '监控对象名称', dataIndex: 'monitorName',sortable : true,width:30},
        { header: '告警级别', dataIndex: 'severity',sortable : true,width:10},
        { header: '状态', dataIndex: 'state',sortable : true},
        { header: '告警确认人',dataIndex: 'eventConfirmer', sortable : true,fixed:true}
        ]),
        animCollapse: false,
        tbar:topToolbar,
        bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    store: store,
                    displayInfo: true,
                    displayMsg: '第{0} 到 {1} 条数据 共{2}条',
                    emptyMsg: "没有数据"
        })
        
    });
    store.load({params:{start:0, limit:20,eventStatus:'Close'}});
    var viewport= new Ext.Viewport({
		renderTo:Ext.getBody(), 
		layout:'border',
		items:[grid]
	});
store.on('beforeload',function(s,options){	
 		Ext.apply(s.baseParams,{monitor_name: Ext.getCmp("key").getValue(),
                                        severity: getType(Ext.getCmp("type").getValue()),
                                        eventConfirmer:Ext.getCmp("eventConfirmer").getValue(),
                                        eventStatus:'Close'
 		});
 	});
 
});

function search(){
	Ext.getCmp('grid').store.reload({params:{start:0,limit:20,monitor_name: Ext.getCmp("key").getValue(),
		                                   severity:getType(Ext.getCmp("type").getValue()),
		                                   eventConfirmer:Ext.getCmp("eventConfirmer").getValue(),
		                                   eventStatus:'Close'
		                                   }});
}
</script>
</head>
<body>
</body>
</html>
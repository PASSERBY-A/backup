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
		return '����';
	}else if(value=='3'){
		return '�澯';
	}else{
		return '����';
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
	data : [[-1,'ȫ��'],[1,'����'],[2,'����'],[3,'�澯']]
})

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : ['��ѯ ','�澯����: ',{
            xtype: 'combo',
            id:'type',
            value: 'ȫ��',
            width : 70,
            valueField : 'id',
			displayField : 'value',
			typeAhead : true,
			mode : 'local',
			maxHeight : 150,
			triggerAction : 'all',
			emptyText : '��ѡ��...',
			selectOnFocus : true,
			editable : false,
			forceSelection : true,
			allowBlank : false,
			value:'-1',
			store : typeData
        },'����豸: ',new Ext.form.TextField({
			id:'key',
			width : 150
		}),'�¼�ȷ���ˣ� ',new Ext.form.TextField({
			id:'eventConfirmer',
			width : 150
		}),
                      {
			id : 'search',
			tooltip : '�������',
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
        { header: '�澯��������', dataIndex: 'timed',sortable : true,width:30},
        { header: '��ض�������', dataIndex: 'monitorName',sortable : true,width:30},
        { header: '�澯����', dataIndex: 'severity',sortable : true,width:10},
        { header: '״̬', dataIndex: 'state',sortable : true},
        { header: '�澯ȷ����',dataIndex: 'eventConfirmer', sortable : true,fixed:true}
        ]),
        animCollapse: false,
        tbar:topToolbar,
        bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    store: store,
                    displayInfo: true,
                    displayMsg: '��{0} �� {1} ������ ��{2}��',
                    emptyMsg: "û������"
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
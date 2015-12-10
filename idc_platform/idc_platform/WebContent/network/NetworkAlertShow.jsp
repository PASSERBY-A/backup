<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ include file="getUserId.jsp"%>
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
 var eventConfirmer = '<%=userName%>';
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
                        {name: 'groupName'},
                        {name: 'severity'}
	    	]
	    )
       
    });

var submitStore = new Ext.data.Store(
    {
        baseParams:{ajax:true},
        proxy: new Ext.data.HttpProxy({
        	url:'updateAlert.action'
        }),
        reader: new Ext.data.JsonReader({
	            root: 'result'
	        })
       
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

var simpleForm = new Ext.FormPanel({
	id:'simpleForm',
	hidden : true
});



function confim(){
          if (eventConfirmer == null ||eventConfirmer == ''){
              Ext.Msg.alert("����", "���ȵ�¼��");
              return;
            }
           
           var record = Ext.getCmp('grid').getSelectionModel().getSelected();
                         if(record == null){
                           Ext.Msg.alert("����", "��ѡ����Ҫȷ�ϵĸ澯");
                           return;
                          } 

          Ext.Msg.confirm("��ʾ��Ϣ","�Ƿ�ȷ��ѡ�еĸ澯��",function callback(id){
          if(id=="yes"){
            submitStore.load({
             params:{
                     timed:record.get('timed'),
                     eventStatus: 'Close',
                     group_name:record.get('groupName'),
                     monitor_name:record.get('monitorName'),
                     eventConfirmer: eventConfirmer},
             callback: function(records, options, success){
                 Ext.Msg.alert('ȷ��', 'ȷ�ϲ����ɹ���'+record.get('monitorName')+' �� '+record.get('timed')+'��'+record.get('severity')+'�澯��ȷ�ϡ�ȷ����:'+eventConfirmer);  
                search(); }   
                     });		
        }else{
            return;
        }
    });
            
            	 
  
}

function getBtn(value){
  
  return "<img src='<%=request.getContextPath()%>/portal/manager/style/images/finish.gif' onclick='confim()'/>";
}

Ext.onReady(function(){
	
	var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true,
                        hidden : true
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
         sm,
        { header: '����', dataIndex: 'timed',sortable : true,width:30},
        { header: '��ض�������', dataIndex: 'monitorName',sortable : true,width:30},
        { header: '�澯����', dataIndex: 'severity',sortable : true,width:10},
        { header: '״̬', sortable : true,dataIndex: 'state'},
        { header: 'ȷ��', dataIndex: 'severity',sortable : false,width:7,renderer:getBtn},
        { header: '״̬', dataIndex: 'groupName', hidden : true}
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
    store.load({params:{start:0, limit:20,eventStatus:'Open'}});
    var viewport= new Ext.Viewport({
		renderTo:Ext.getBody(), 
		layout:'border',
		items:[grid]
	});
store.on('beforeload',function(s,options){	
 		Ext.apply(s.baseParams,{monitor_name: Ext.getCmp("key").getValue(),
                                        severity: getType(Ext.getCmp("type").getValue()),
                                        eventStatus :'Open'
 		});
 	});
 	
    
 
});

function search(){
	Ext.getCmp('grid').store.reload({params:{
		start:0,limit:20,monitor_name: Ext.getCmp("key").getValue(),
		eventStatus:'Open',severity:getType(Ext.getCmp("type").getValue())}});
}
</script>
</head>
<body>
</body>
<div id='submitFrom' name='submitFrom' style='display:none'></div>
</html>
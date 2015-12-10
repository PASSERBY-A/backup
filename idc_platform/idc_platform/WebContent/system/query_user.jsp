<%@ page language="java" pageEncoding="gbk"%>
<html>
<head>
<title>User Management</title>
</head>
<%@ include file="../common/inc/header.jsp" %>


<script language="javascript">

Ext.onReady(function() {
	var columnSub01 = new Ext.Panel({
		columnWidth : .5,
		layout : 'form',
		border : false,
		defaults: {width: 150},
		labelWidth:80,
		bodyStyle : 'padding: 0 30 0 0',
		defaultType: 'textfield',
		items : [
			{	
				fieldLabel:'��¼ID',
                id:'loginName',
                name:'loginName'
			}
		] 
	});
	var columnSub02 = new Ext.Panel({
		columnWidth : .5,
		layout : 'form',
		border : false,
		defaults: {width: 150},
		labelWidth:80,
		bodyStyle : 'padding: 0 0 0 30',
		defaultType: 'textfield',
		items : [
			{	
				fieldLabel:'��ʵ����',
                id:'actualName',
                name:'actualName'
			}
		]
	});
	var formSubCard = new Ext.Panel({
		layout : 'column',
		frame : false,
		border : false,
		anchor : '100%',
		items : [columnSub01, columnSub02]
	});
	var queryForm=new Ext.form.FormPanel({
		region:'north',
		frame:true,
		title:'��ѯϵͳ�û�',
		height:120,
		items:[
			formSubCard
		],
		buttons:[
			{
				text:'����',
				handler:function(){
					searchEmp();
				}
			},
			{
				text:'���',
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
        	url:'querySysUser.do'
        }),
        reader: new Ext.data.JsonReader({
	            totalProperty: 'totalCount',
	            root: 'result'
	        }, [  
	          	{name: 'id', type: 'int'},
	          	{name: 'loginName', type: 'string'},
	          	{name: 'actualName', type: 'string'},
	          	{name: 'status', type: 'String'},
	          	{name : 'createdDate',type: 'String'}
	    	]
	    )
       
    });
    
    store.on('beforeload',function(s,options){	
 		Ext.apply(s.baseParams,{loginName : Ext.getCmp("loginName").getValue(),
 			actualName : Ext.getCmp("actualName").getValue()
 		});
 	});
    
    var grid = new Ext.grid.GridPanel({
    	id:'grid',
        region:'center',
        height:300,
        title:'��ѯ���',
        store: store,
        disableSelection:true,
        loadMask: true,
        viewConfig: {
            forceFit: true
        },
        tbar:[
        	{
        		id:'add_user',
        		text:'�����û�',
        		handler:function(){}
        	}
        ],
        //autoExpandColumn:'descn', 
        columns: [
        { header: "��¼ID", sortable: true, dataIndex: 'loginName' },
        { header: '��ʵ����', sortable: true, dataIndex: 'actualName', width: 80 },
        { header: "״̬", sortable: true, dataIndex: 'status' },
        { header: '��������', dataIndex: 'createdDate', width: 80 }],
        animCollapse: false,
        bbar: new Ext.PagingToolbar({
                    pageSize: 10,
                    store: store,
                    displayInfo: true,
                    displayMsg: '��{0} �� {1} ������ ��{2}��',
                    emptyMsg: "û������"
        })
        
    });
	
	store.load({params:{start:0, limit:10}});
    var viewport= new Ext.Viewport({
		renderTo:Ext.getBody(), 
		layout:"border",
		items:[queryForm,grid]
	});
}); 
function deleteUser(value){
	Ext.Msg.show({
        title:"ɾ���û�",
		msg:"�Ƿ�ȷ��ɾ���û���"+value+"���ò��������棡",
		buttons:Ext.MessageBox.YESNO,
		icon:Ext.MessageBox.QUESTION,
		fn:function(b){
			if(b=='yes'){
				Ext.Ajax.request({
           			url:'',
           			params:{loginName:value},
           			success:function(response){
           				var responseArray = Ext.util.JSON.decode(response.responseText);
           				if(true==responseArray.success){
           					Ext.Msg.show({
	              				title:"���",
								msg:"ɾ����¼IDΪ '"+value+"' ���û��ɹ�",
							 	buttons:Ext.MessageBox.OK,
							 	icon:Ext.MessageBox.INFO,
							 	fn:function(e){
							 		Ext.getCmp('grid').store.reload();
							 	}
						 	})
           				}
           				else if('admin'==response.responseText.trim()){
           					Ext.Msg.show({
	              				title:"�Ƿ�����",
								msg:"����ɾ��ϵͳ����Ա",
							 	buttons:Ext.MessageBox.OK,
							 	icon:Ext.MessageBox.INFO
						 	})
           				}
           				else{
           					Ext.Msg.show({
	              			 title:"ʧ��",
							 msg:"ɾ���û�ʧ�ܣ���ˢ��ҳ�������ݣ����������쳣����ϵͳ����Ա��ϵ��",
							 buttons:Ext.MessageBox.CANCEL,
							 icon:Ext.MessageBox.ERROR,
							 fn:function(e){
							 	Ext.getCmp('grid').store.reload();
							 }
              				})
           				}
           			},
           			failure:function(){
           				Ext.Msg.show({
	              			 title:"ʧ��",
							 msg:"����ʧ�ܣ��������磬���������쳣����ϵͳ����Ա��ϵ��",
							 buttons:Ext.MessageBox.CANCEL,
							 icon:Ext.MessageBox.ERROR
             			})
           			}
             	});
			}
		}
	})	
}
function searchEmp(){
	Ext.getCmp('grid').store.reload({params:{start:0,limit:10}});
}

</script>
<body background="<%=request.getContextPath()%>/images/background.gif">
</body>
</html>
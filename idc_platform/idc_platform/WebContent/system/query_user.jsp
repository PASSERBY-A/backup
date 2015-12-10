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
				fieldLabel:'登录ID',
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
				fieldLabel:'真实姓名',
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
		title:'查询系统用户',
		height:120,
		items:[
			formSubCard
		],
		buttons:[
			{
				text:'搜索',
				handler:function(){
					searchEmp();
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
        title:'查询结果',
        store: store,
        disableSelection:true,
        loadMask: true,
        viewConfig: {
            forceFit: true
        },
        tbar:[
        	{
        		id:'add_user',
        		text:'新增用户',
        		handler:function(){}
        	}
        ],
        //autoExpandColumn:'descn', 
        columns: [
        { header: "登录ID", sortable: true, dataIndex: 'loginName' },
        { header: '真实姓名', sortable: true, dataIndex: 'actualName', width: 80 },
        { header: "状态", sortable: true, dataIndex: 'status' },
        { header: '创建日期', dataIndex: 'createdDate', width: 80 }],
        animCollapse: false,
        bbar: new Ext.PagingToolbar({
                    pageSize: 10,
                    store: store,
                    displayInfo: true,
                    displayMsg: '第{0} 到 {1} 条数据 共{2}条',
                    emptyMsg: "没有数据"
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
        title:"删除用户",
		msg:"是否确认删除用户："+value+"？该操作不可逆！",
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
	              				title:"完成",
								msg:"删除登录ID为 '"+value+"' 的用户成功",
							 	buttons:Ext.MessageBox.OK,
							 	icon:Ext.MessageBox.INFO,
							 	fn:function(e){
							 		Ext.getCmp('grid').store.reload();
							 	}
						 	})
           				}
           				else if('admin'==response.responseText.trim()){
           					Ext.Msg.show({
	              				title:"非法操作",
								msg:"不能删除系统管理员",
							 	buttons:Ext.MessageBox.OK,
							 	icon:Ext.MessageBox.INFO
						 	})
           				}
           				else{
           					Ext.Msg.show({
	              			 title:"失败",
							 msg:"删除用户失败，请刷新页面检查数据，如数据无异常请与系统管理员联系！",
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
	              			 title:"失败",
							 msg:"连接失败，请检查网络，如网络无异常请与系统管理员联系！",
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
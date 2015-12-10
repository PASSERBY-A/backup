function userQueryFn6(){
	//记录类型
	var user = Ext.data.Record.create([
		{name:"id",mapping:"id"},
		{name:"username",mapping:"username"},
		{name:"password",mapping:"password1"},
		{name:"password",mapping:"password2"},
		{name:"password",mapping:"password"}
	]);
	
	//存储器
	var store = new Ext.data.Store({
		url:"../getCustomerWupinjinruCountReport.action",
		reader:new Ext.data.JsonReader({
			root:"allUser",
			id:"id",
			totalProperty:"recordSize"
		},user)
	});
	
			//开始时间
	var startTime5 = new Ext.form.DateField(
		{ 			  
	        fieldLabel: '开始时间',
				  ReadOnly:true,//禁止手工修改 
				  //Editable:false,
	        emptyText: new Date().dateFormat('Y-m-d'),
	        id: 'b_date5',
	        name: 'bDate5',
	        //value: new Date().dateFormat('Y-m-d'),
	        format: 'Y-m-d',
	        width: 90
	  }
	);
	
	
	//结束时间
	var endTime5 = new Ext.form.DateField(
		{
        fieldLabel: '结束时间',
			  editable:false,//禁止手工修改 
        emptyText:  new Date().dateFormat('Y-m-d'),
       	id: 'e_date5',
       	name: 'eDate5',
       	//value: new Date().dateFormat('Y-m-d'),
        format: 'Y-m-d',
        width: 90
    }
	);
	
	startTime5.on('render', function(df){
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});
	
	endTime5.on('render', function(df){
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});
	
	
	//客户物品进入统计报告

	var gridPanel = new Ext.grid.GridPanel({
		width:300,
		height:200,
		frame:true,
		store:store,
		loadMask: true,
		store:store,
		split: true,
		columns:[
			{header:"日期与时间",width: 160, dataIndex:"id",sortable:true,align:'center'},
			{header:"客户名称",width: 160, dataIndex:"username",sortable:true,align:'center'},
			{header:"设备编号",width: 100, dataIndex:"password1",sortable:true,align:'center'},
			{header:"设备型号",width: 100, dataIndex:"password2",sortable:true,align:'center'},
			{header:"位置",width: 360, dataIndex:"password",sortable:true,align:'center'},
			{header:"",width: 300}
		],
		autoExpandColumn:1,
		//分页控制条
		bbar:new Ext.PagingToolbar({
			displayMsg : '第{0} 到 {1} 条数据 共{2}条',
			emptyMsg : "没有数据",
			pageSize : 10,
			store : store,
			displayInfo : true
		}),
		selModel:new Ext.grid.RowSelectionModel({singleSelect:false}),
		tbar:[
			"开始时间  :",
			startTime5,
			"结束时间  :",
			endTime5,
			"  ",{
		  	  id : 'search',
			  tooltip : '点击搜索',
			  iconCls : 'icon-search',
			  handler:function(){
        			var startTime = Ext.get("b_date5").dom.value;
            		var endTime = Ext.get("e_date5").dom.value;
            		if(startTime=="开始时间"||endTime =="结束时间"){
            			alert("开始时间或者结束时间不能为空！");
            			return false;
            		}else if(startTime>endTime){
            			alert("开始时间不能大于结束时间！");	
            		}else{
            			store.load({
        					params:
        						{
        							start:0, limit:10,
        							startTime:startTime,
        							endTime:endTime
        						}
            			}
        				);
            		}
			}},'-',
			{
			  xtype : 'tbbutton',
			  text:'导出统计结果',
			  iconCls : 'icon-add',
			  handler:function(){
		 			var bd = new Date(Ext.getCmp('b_date5').getValue());
	        		var ed = new Date(Ext.getCmp('e_date5').getValue());
	        		var form = Ext.DomHelper.append(Ext.getBody(), '<form action="../exportCustomerWupinjinruCountReport.action"><input id="beginDate" name="beginDate" type="hidden" value="' + bd + '"/><input id="endDate" name="endDate" type="hidden" value="' + ed + '"/></form>', true);
	        		form.dom.submit();
	        		form.remove();
	        		form = null; 
			  }
			}
		]
	});
	
	store.load({params:{start:0, limit:10}});
	
	if(!userQueryPageIsOpen6){
		var tabPage = tabPanel.add({
						id:'tab_6',
						title:"客户物品进入统计报告",
						height:527,
						closable:true,
						layout:"fit",
						items:[
							gridPanel
						],
						listeners:{
							beforedestroy:function(){
								userQueryPageIsOpen6 = false;
							}
						}
					});
		tabPanel.setActiveTab(tabPage);
		userQueryPageIsOpen6= true;
	}else {
		var n_tab = tabPanel.getComponent('tab_6');
		tabPanel.setActiveTab(n_tab);
		userQueryPageIsOpen6 = true;
	}
}
function userQueryFn5(){
	//记录类型
	var user = Ext.data.Record.create([
		{name:"id",mapping:"id"},
		{name:"username",mapping:"username"},
		{name:"username",mapping:"username1"},
		{name:"password",mapping:"password"}
	]);
	
	//存储器
	var store = new Ext.data.Store({
		url:"../getCustomerBussnessTypeCountReport.action",
		reader:new Ext.data.JsonReader({
			root:"allUser",
			id:"id",
			totalProperty:"recordSize"
		},user)
	});
	
	
		//开始时间
	var startTime4 = new Ext.form.DateField(
		{ 			  
	        fieldLabel: '开始时间',
				  ReadOnly:true,//禁止手工修改 
				  //Editable:false,
	        emptyText: new Date().dateFormat('Y-m-d'),
	        id: 'b_date4',
	        name: 'bDate4',
	        //value: new Date().dateFormat('Y-m-d'),
	        format: 'Y-m-d',
	        width: 90
	  }
	);
	
	
	//结束时间
	var endTime4 = new Ext.form.DateField(
		{
        fieldLabel: '结束时间',
			  editable:false,//禁止手工修改 
        emptyText:  new Date().dateFormat('Y-m-d'),
       	id: 'e_date4',
       	name: 'eDate4',
       	//value: new Date().dateFormat('Y-m-d'),
        format: 'Y-m-d',
        width: 90
    }
	);
	
	startTime4.on('render', function(df){
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});
	
	endTime4.on('render', function(df){
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});
	
	//客户业务类别统计报告
	
	var gridPanel = new Ext.grid.GridPanel({
		width:300,
		height:200,
		frame:true,
		store:store,
		columns:[
			{header:"类别",width: 160, dataIndex:"id",sortable:true,align:'center'},
			{header:"已入驻客户数量",width: 160, dataIndex:"username",sortable:true,align:'center'},
			{header:"合计",width: 160, dataIndex:"username1",sortable:true,align:'center'},
			{header:"签约客户数量",width: 160, dataIndex:"password",sortable:true,align:'center'},
			{header:"",width: 500}
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
			startTime4,
			"结束时间  :",
			endTime4,
			"  ",{
		  	  id : 'search',
			  tooltip : '点击搜索',
			  iconCls : 'icon-search',
			  handler:function(){
        			var startTime = Ext.get("b_date4").dom.value;
            		var endTime = Ext.get("e_date4").dom.value;
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
	 			var bd = new Date(Ext.getCmp('b_date4').getValue());
        		var ed = new Date(Ext.getCmp('e_date4').getValue());
        		var form = Ext.DomHelper.append(Ext.getBody(), '<form action="../exportCustomerBussnessTypeCountReport.action"><input id="beginDate" name="beginDate" type="hidden" value="' + bd + '"/><input id="endDate" name="endDate" type="hidden" value="' + ed + '"/></form>', true);
        		form.dom.submit();
        		form.remove();
        		form = null; 
			  }
			}
		]
	});
	
	store.load({params:{start:0, limit:10}});
	if(!userQueryPageIsOpen5){
		var tabPage = tabPanel.add({
						id:'tab_5',
						title:"客户业务类别统计报告",
						height:527,
						closable:true,
						layout:"fit",
						items:[
							gridPanel
						],
						listeners:{
							beforedestroy:function(){
								userQueryPageIsOpen5 = false;
							}
						}
					});
		tabPanel.setActiveTab(tabPage);
		userQueryPageIsOpen5= true;
	}else {
		var n_tab = tabPanel.getComponent('tab_5');
		tabPanel.setActiveTab(n_tab);
		userQueryPageIsOpen5 = true;
	}
}
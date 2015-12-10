//使datefield不能被编辑
Ext.override(Ext.form.DateField,{
	setEditable : function(value){     
        if(!value){
            this.el.dom.setAttribute('readOnly', true);
            //this.el.on('mousedown', this.onTriggerClick,  this);
            this.el.addClass('x-combo-noedit');
        }else{
            this.el.dom.removeAttribute('readOnly');
            //this.el.un('mousedown', this.onTriggerClick,  this);
            this.el.removeClass('x-combo-noedit');
        }
    }
	
});


function userQueryFn1(){
	//记录类型
	var user = Ext.data.Record.create([
		{name:"serviceType",mapping:"serviceType"},
		{name:"shoulicount",mapping:"shoulicount"},
		{name:"wanchengcount",mapping:"wanchengcount"}
	]);
	
	//开始时间
	var startTime = new Ext.form.DateField(
		{ 			  
	        fieldLabel: '开始时间',
	        emptyText: new Date().dateFormat('Y-m-d'),
	        id: 'b_date',
	        name: 'bDate',
	        //value: new Date().dateFormat('Y-m-d'),
	        format: 'Y-m-d',
	        width: 90
	  }
	);
	
	
	//结束时间
	var endTime = new Ext.form.DateField(
		{
        fieldLabel: '结束时间',
        emptyText:  new Date().dateFormat('Y-m-d'),
       	id: 'e_date',
       	name: 'eDate',
       	//value: new Date().dateFormat('Y-m-d'),
        format: 'Y-m-d',
        width: 90
    }
	);
	//渲染的时候设置为不能编辑
	startTime.on('render', function(df){
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});
	
	endTime.on('render', function(df){
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});
	
	//存储器
	var store = new Ext.data.Store({
		url:"../getCustomerFuwuCountReport.action",
		reader:new Ext.data.JsonReader({
			root:"results",
			totalProperty:"recordSize"
		},user)
	});
	
	//客户服务统计报告
	var gridPanel = new Ext.grid.GridPanel({
		width : 500,
		height : 500,
		frame:true,
		store:store,
		columns:[
			{header:"服务类别",width: 100,dataIndex:"serviceType",sortable:true,align:'center'},
			{header:"受理数量",width: 100,dataIndex:"shoulicount",sortable:true,align:'center'},
			{header:"完成数量",width: 100,dataIndex:"wanchengcount",sortable:true,align:'center'},
			{header:"",width: 770,dataIndex:"no"}
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
			startTime,
			"结束时间  :",
			endTime,"  ",
			{
		  	  id : 'search',
			  tooltip : '点击搜索',
			  iconCls : 'icon-search',
			  handler:function(){
        		var startTime = Ext.get("b_date").dom.value;
        		var endTime = Ext.get("e_date").dom.value;
        		if(startTime=="开始时间"||endTime =="结束时间"){
        			alert("开始时间或者结束时间不能为空！");
        			return false;
        		}else if(startTime>endTime){
        			alert("开始时间不能大于结束时间！");	
        		}else{
        			store.load({
    					params:
    						{
    							start:0, limit:1,
    							startTime:startTime,
    							endTime:endTime
    						}}
    						);
        		}	
			}},'-',
			{
			  xtype : 'tbbutton',
			  text:'导出统计结果',
			  iconCls : 'icon-add',
			  handler:function(){
        		//var bd = new Date(Ext.getCmp('b_date').getValue());
        		//var ed = new Date(Ext.getCmp('e_date').getValue());
        		var bd = Ext.get("b_date").dom.value;
        		var ed = Ext.get("e_date").dom.value;
        		var form = Ext.DomHelper.append(Ext.getBody(), '<form action="../exportCustomerFuwuCountReport.action"><input id="beginDate" name="beginDate" type="hidden" value="' + bd + '"/><input id="endDate" name="endDate" type="hidden" value="' + ed + '"/></form>', true);
        		form.dom.submit();
        		form.remove();
        		form = null;
			  }
			}	
		]
	});
	
	store.load({params:{start:0, limit:1}});
	
	if(!userQueryPageIsOpen1){
		var tabPage = tabPanel.add({
			id:'tab_1',
						title:"客户服务统计报告",
						height:527,
						closable:true,
						layout:"fit",
						items:[
							gridPanel
						],
						listeners:{
							beforedestroy:function(){
								userQueryPageIsOpen1 = false;
							}
						}
					});
		tabPanel.setActiveTab(tabPage);
		userQueryPageIsOpen1= true;
	}else {
		var n_tab = tabPanel.getComponent('tab_1');
		tabPanel.setActiveTab(n_tab);
		userQueryPageIsOpen1 = true;
	}
}
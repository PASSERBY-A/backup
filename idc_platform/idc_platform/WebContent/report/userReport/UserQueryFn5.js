function userQueryFn5(){
	//��¼����
	var user = Ext.data.Record.create([
		{name:"id",mapping:"id"},
		{name:"username",mapping:"username"},
		{name:"username",mapping:"username1"},
		{name:"password",mapping:"password"}
	]);
	
	//�洢��
	var store = new Ext.data.Store({
		url:"../getCustomerBussnessTypeCountReport.action",
		reader:new Ext.data.JsonReader({
			root:"allUser",
			id:"id",
			totalProperty:"recordSize"
		},user)
	});
	
	
		//��ʼʱ��
	var startTime4 = new Ext.form.DateField(
		{ 			  
	        fieldLabel: '��ʼʱ��',
				  ReadOnly:true,//��ֹ�ֹ��޸� 
				  //Editable:false,
	        emptyText: new Date().dateFormat('Y-m-d'),
	        id: 'b_date4',
	        name: 'bDate4',
	        //value: new Date().dateFormat('Y-m-d'),
	        format: 'Y-m-d',
	        width: 90
	  }
	);
	
	
	//����ʱ��
	var endTime4 = new Ext.form.DateField(
		{
        fieldLabel: '����ʱ��',
			  editable:false,//��ֹ�ֹ��޸� 
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
	
	//�ͻ�ҵ�����ͳ�Ʊ���
	
	var gridPanel = new Ext.grid.GridPanel({
		width:300,
		height:200,
		frame:true,
		store:store,
		columns:[
			{header:"���",width: 160, dataIndex:"id",sortable:true,align:'center'},
			{header:"����פ�ͻ�����",width: 160, dataIndex:"username",sortable:true,align:'center'},
			{header:"�ϼ�",width: 160, dataIndex:"username1",sortable:true,align:'center'},
			{header:"ǩԼ�ͻ�����",width: 160, dataIndex:"password",sortable:true,align:'center'},
			{header:"",width: 500}
		],
		autoExpandColumn:1,
		//��ҳ������
		bbar:new Ext.PagingToolbar({
			displayMsg : '��{0} �� {1} ������ ��{2}��',
			emptyMsg : "û������",
			pageSize : 10,
			store : store,
			displayInfo : true
		}),
		selModel:new Ext.grid.RowSelectionModel({singleSelect:false}),
		tbar:[
			"��ʼʱ��  :",
			startTime4,
			"����ʱ��  :",
			endTime4,
			"  ",{
		  	  id : 'search',
			  tooltip : '�������',
			  iconCls : 'icon-search',
			  handler:function(){
        			var startTime = Ext.get("b_date4").dom.value;
            		var endTime = Ext.get("e_date4").dom.value;
            		if(startTime=="��ʼʱ��"||endTime =="����ʱ��"){
            			alert("��ʼʱ����߽���ʱ�䲻��Ϊ�գ�");
            			return false;
            		}else if(startTime>endTime){
            			alert("��ʼʱ�䲻�ܴ��ڽ���ʱ�䣡");	
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
			  text:'����ͳ�ƽ��',
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
						title:"�ͻ�ҵ�����ͳ�Ʊ���",
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
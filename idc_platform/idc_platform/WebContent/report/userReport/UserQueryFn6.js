function userQueryFn6(){
	//��¼����
	var user = Ext.data.Record.create([
		{name:"id",mapping:"id"},
		{name:"username",mapping:"username"},
		{name:"password",mapping:"password1"},
		{name:"password",mapping:"password2"},
		{name:"password",mapping:"password"}
	]);
	
	//�洢��
	var store = new Ext.data.Store({
		url:"../getCustomerWupinjinruCountReport.action",
		reader:new Ext.data.JsonReader({
			root:"allUser",
			id:"id",
			totalProperty:"recordSize"
		},user)
	});
	
			//��ʼʱ��
	var startTime5 = new Ext.form.DateField(
		{ 			  
	        fieldLabel: '��ʼʱ��',
				  ReadOnly:true,//��ֹ�ֹ��޸� 
				  //Editable:false,
	        emptyText: new Date().dateFormat('Y-m-d'),
	        id: 'b_date5',
	        name: 'bDate5',
	        //value: new Date().dateFormat('Y-m-d'),
	        format: 'Y-m-d',
	        width: 90
	  }
	);
	
	
	//����ʱ��
	var endTime5 = new Ext.form.DateField(
		{
        fieldLabel: '����ʱ��',
			  editable:false,//��ֹ�ֹ��޸� 
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
	
	
	//�ͻ���Ʒ����ͳ�Ʊ���

	var gridPanel = new Ext.grid.GridPanel({
		width:300,
		height:200,
		frame:true,
		store:store,
		loadMask: true,
		store:store,
		split: true,
		columns:[
			{header:"������ʱ��",width: 160, dataIndex:"id",sortable:true,align:'center'},
			{header:"�ͻ�����",width: 160, dataIndex:"username",sortable:true,align:'center'},
			{header:"�豸���",width: 100, dataIndex:"password1",sortable:true,align:'center'},
			{header:"�豸�ͺ�",width: 100, dataIndex:"password2",sortable:true,align:'center'},
			{header:"λ��",width: 360, dataIndex:"password",sortable:true,align:'center'},
			{header:"",width: 300}
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
			startTime5,
			"����ʱ��  :",
			endTime5,
			"  ",{
		  	  id : 'search',
			  tooltip : '�������',
			  iconCls : 'icon-search',
			  handler:function(){
        			var startTime = Ext.get("b_date5").dom.value;
            		var endTime = Ext.get("e_date5").dom.value;
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
						title:"�ͻ���Ʒ����ͳ�Ʊ���",
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
//ʹdatefield���ܱ��༭
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
	//��¼����
	var user = Ext.data.Record.create([
		{name:"serviceType",mapping:"serviceType"},
		{name:"shoulicount",mapping:"shoulicount"},
		{name:"wanchengcount",mapping:"wanchengcount"}
	]);
	
	//��ʼʱ��
	var startTime = new Ext.form.DateField(
		{ 			  
	        fieldLabel: '��ʼʱ��',
	        emptyText: new Date().dateFormat('Y-m-d'),
	        id: 'b_date',
	        name: 'bDate',
	        //value: new Date().dateFormat('Y-m-d'),
	        format: 'Y-m-d',
	        width: 90
	  }
	);
	
	
	//����ʱ��
	var endTime = new Ext.form.DateField(
		{
        fieldLabel: '����ʱ��',
        emptyText:  new Date().dateFormat('Y-m-d'),
       	id: 'e_date',
       	name: 'eDate',
       	//value: new Date().dateFormat('Y-m-d'),
        format: 'Y-m-d',
        width: 90
    }
	);
	//��Ⱦ��ʱ������Ϊ���ܱ༭
	startTime.on('render', function(df){
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});
	
	endTime.on('render', function(df){
		df.setValue(new Date().dateFormat('Y-m-d'));
		df.setEditable(false);
	});
	
	//�洢��
	var store = new Ext.data.Store({
		url:"../getCustomerFuwuCountReport.action",
		reader:new Ext.data.JsonReader({
			root:"results",
			totalProperty:"recordSize"
		},user)
	});
	
	//�ͻ�����ͳ�Ʊ���
	var gridPanel = new Ext.grid.GridPanel({
		width : 500,
		height : 500,
		frame:true,
		store:store,
		columns:[
			{header:"�������",width: 100,dataIndex:"serviceType",sortable:true,align:'center'},
			{header:"��������",width: 100,dataIndex:"shoulicount",sortable:true,align:'center'},
			{header:"�������",width: 100,dataIndex:"wanchengcount",sortable:true,align:'center'},
			{header:"",width: 770,dataIndex:"no"}
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
			startTime,
			"����ʱ��  :",
			endTime,"  ",
			{
		  	  id : 'search',
			  tooltip : '�������',
			  iconCls : 'icon-search',
			  handler:function(){
        		var startTime = Ext.get("b_date").dom.value;
        		var endTime = Ext.get("e_date").dom.value;
        		if(startTime=="��ʼʱ��"||endTime =="����ʱ��"){
        			alert("��ʼʱ����߽���ʱ�䲻��Ϊ�գ�");
        			return false;
        		}else if(startTime>endTime){
        			alert("��ʼʱ�䲻�ܴ��ڽ���ʱ�䣡");	
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
			  text:'����ͳ�ƽ��',
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
						title:"�ͻ�����ͳ�Ʊ���",
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
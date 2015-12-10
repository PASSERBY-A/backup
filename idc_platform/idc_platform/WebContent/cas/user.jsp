<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getPurview.jsp"%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=EXTJS_HOME%>/resources/css/ext-all.css" />
	<script type="text/javascript" src="<%=EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-ext.js"></script>

	<script type="text/javascript" src="cas_auc.js"></script>
</head>
<style>
	.x-tree-selected {
    border:1px dotted #a3bae9;
    background:#DFE8F6;
}
.x-tree-node .x-tree-selected a span{
	background:transparent;
	color:#15428b;
    font-weight:bold;
}
</style>
<body>
<script>

Ext.override(Ext.form.TwinTriggerField, {
	initComponent : function(){
        Ext.form.TwinTriggerField.superclass.initComponent.call(this);

        this.triggerConfig = {
            tag:'span', cls:'x-form-twin-triggers', cn:[
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger1Class},
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger2Class}
        ]};
    }
});

Ext.form.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
        Ext.form.SearchField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },

    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
    width:180,
    hasSearch : false,
    paramName : 'query',

    onTrigger1Click : function(){
        if(this.hasSearch){
            this.el.dom.value = '';
            var o = {start: 0};
            this.store.baseParams = this.store.baseParams || {};
            this.store.baseParams[this.paramName] = '';
            this.store.reload({params:o});
            this.triggers[0].hide();
            this.hasSearch = false;
        }
    },

    onTrigger2Click : function(){
        var v = this.getRawValue();
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
        var o = {start: 0};
        this.store.baseParams = this.store.baseParams || {};
        this.store.baseParams[this.paramName] = v;
        this.store.baseParams['filterAttribute'] = Ext.getCmp('search-type').getValue();
        this.store.reload({params:o});
        this.hasSearch = true;
        this.triggers[0].show();
    }
});

var userMappingDS = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: 'mappingQuery.jsp'
    }),
		baseParams:{type:'all'},
    reader: new Ext.data.JsonReader({
        root: 'records',
        totalProperty: 'totalCount'
    }, [
				{name: 'thirdSystem', type: 'string'},
				{name: 'thirdUserId', type: 'string'}
    ])
});

var userGrid = new Ext.grid.GridPanel({
  ds: userDS,
  cm: userCM,
  loadMask: true,
  viewConfig: {
		forceFit:true
	},
	tbar:[{
		text:"����",
		handler:function(){
			Ext.loadRemoteScript("userEdit.jsp?subType=add");
		}
	},'-',
	{
		text:"��������",
		handler:function(){
			var row = userGrid.getSelectionModel().getSelected();
			if (!row){
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
				return;
			}
			if (confirm("ȷ��Ҫ����"+row.get("id")+"�����룿")){
				postForm({'postType':'user','id':row.get("id"),'subType':'resetPassword'},"casAucPost.jsp",userCallback);
			}
		}
	},'-',
	//{
		//text:"�޸�����",
		//handler:function(){
			//var row = userGrid.getSelectionModel().getSelected();
			//if (!row){
				//Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
				//return;
			//}
			//var xposition = (screen.width - 350) / 2;
  		//var yposition = (screen.height - 200) / 2;
			//window.open("modifyPassword.jsp?id="+row.get("id"), '_blank','left='+xposition+',top='+yposition+',height=200,width=350,toolbar=no, menubar=no,resizable=no, scrollbars=no,location=no, status=no');

			//Ext.loadRemoteScript("modifyPassword.jsp?id="+row.get("id"));
		//}
	//},'-',
	{
		text:"����",
		handler:function(){
			var row = userGrid.getSelectionModel().getSelected();
			if (!row){
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
				return;
			}
			if (confirm("ȷ��Ҫ��"+row.get("id")+"������")){
				postForm({'postType':'user','id':row.get("id"),'subType':'unlock'},"casAucPost.jsp",userCallback);
			}
		}
	},'-', '����: ',
	new Ext.form.ComboBox({
        width:120,
        value:'all',
        id:'search-type',
        mode:'local',
        store: new Ext.data.SimpleStore({
            fields: ['id', 'value'],
            data : [['all','���κη�ʽ����'],['id','���û�ID����'], ['name','���û����ƹ���'],['mobile','���û��ֻ��Ź���'],['email','���û�Email����']]
        }),
        displayField: 'value',
        valueField:'id',
        triggerAction: 'all',
		selectOnFocus:true
        
    }),' ',
	new Ext.form.SearchField({
		id:'searchField',
		paramName:'filter',
		store: userDS,
		width:250
	})],
	bbar: new Ext.PagingToolbar({
		store: userDS,
		displayInfo: true,
		pageSize: 25,
		displayMsg: "��ǰ��¼�� {0} - {1}����һ��{2}��",
		emptyMsg: "û�м�¼",
		items:['-']
	}),
  listeners: {
  	rowclick:function(g,rowIndex,e){
  		var userId = this.store.getAt(rowIndex).get("id");
  		orgDS.baseParams.type="user";
  		orgDS.baseParams.userId=userId;
			orgDS.reload();

			wgDS.baseParams.type="user";
  		wgDS.baseParams.userId=userId;
			wgDS.reload();
			
			userMappingDS.baseParams.userId = userId;
			userMappingDS.reload();
  	},
  	rowdblclick:function(g,rowIndex,e){
  		var userId = this.store.getAt(rowIndex).get("id");
  		Ext.loadRemoteScript("userEdit.jsp?subType=modify&userId="+userId);
  	}
  }
});

function userCallback(){
}

var userOrgGrid = new Ext.grid.GridPanel({
	title: '��֯',
	region:'north',
	height: 180,
  minSize: 100,
  maxSize: 250,
  margins: '1 1 1 0',
  ds: orgDS,
  columns:  [
  	{header: "����", width: 75, sortable: true, dataIndex: 'name'},
		{header: "״̬", width: 85, sortable: true, dataIndex: 'status',renderer:formatStatus}
  ],
  stripeRows: true,
  viewConfig: {
		forceFit:true
	},tbar:[{
		text:"������֯",
		handler:function(){
			var selectRecord = userGrid.getSelectionModel().getSelected();
			if (!selectRecord){
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
  			return;
			}
			Ext.loadRemoteScript("organizationSelect.jsp?selectUserId="+selectRecord.get("id"));
		}
	}, '-' ,{
		text:"ɾ��",
		handler:function(){
			//alert(Ext.getCmp('searchField').getTrigger(0));
			
			//return;
		
			var selectRecord = userGrid.getSelectionModel().getSelected();
			if (!selectRecord){
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
  			return;
			}
			var orgSelRecord =userOrgGrid.getSelectionModel().getSelected();
			if (!orgSelRecord){
				Ext.MessageBox.alert("��Ϣ","ѡ��Ҫɾ������֯");
  			return;
			}
			if (confirm("ȷ��Ҫ��"+orgSelRecord.get("id")+"��("+selectRecord.get("id")+")�Ĺ�����ɾ����")){
				postForm({'postType':'deleteRelations','toId':orgSelRecord.get("id"),'rType':'userOrganization','user':selectRecord.get("id")},"casAucPost.jsp",userOrgCallback);
			}
		}
	}]
});
function userOrgCallback(){
	orgDS.reload();
}
var userWgGrid = new Ext.grid.GridPanel({
	title: '������',
	region:'center',
  	margins: '1 1 1 0',
  	store: wgDS,
  	columns: [
  		{header: "����", width: 75, sortable: true, dataIndex: 'name'},
		{header: "״̬", width: 85, sortable: true, dataIndex: 'status',renderer:formatStatus}
  	],
  	stripeRows: true,
  	viewConfig: {
		forceFit:true
	},tbar:[{
		text:"��ӹ�����",
		handler:function(){
			var selectRecord = userGrid.getSelectionModel().getSelected();
			if (!selectRecord){
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
  			return;
			}
			Ext.loadRemoteScript("workgroupSelect.jsp?selectUserId="+selectRecord.get("id"));
		}
	}, '-' ,{
		text:"ɾ��",
		handler:function(){
			var selectRecord = userGrid.getSelectionModel().getSelected();
			if (!selectRecord){
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
  			return;
			}
			var wgSelRecord =userWgGrid.getSelectionModel().getSelected();
			if (!wgSelRecord){
				Ext.MessageBox.alert("��Ϣ","ѡ��Ҫɾ���Ĺ�����");
  			return;
			}
			if (confirm("ȷ��Ҫ��"+wgSelRecord.get("id")+"��("+selectRecord.get("id")+")�Ĺ�����ɾ����")){
				postForm({'postType':'deleteRelations','toId':wgSelRecord.get("id"),'rType':'userWorkgroup','user':selectRecord.get("id")},"casAucPost.jsp",userWgCallback);
			}
		}
	}]
});
function userWgCallback(){
	wgDS.reload();
}

var userMappingGrid = new Ext.grid.GridPanel({
	title: '��������Ӧ',
	region:'south',
	height:300,
  margins: '1 1 1 0',
  store: userMappingDS,
  columns: [
  	{header: "ϵͳ", width: 75, sortable: true, dataIndex: 'thirdSystem'},
		{header: "��Ӧ���û�", width: 85, sortable: true, dataIndex: 'thirdUserId',renderer:formatStatus}
  ],
  stripeRows: true,
  viewConfig: {
		forceFit:true
	},tbar:[{
		text:"���",
		handler:function(){
			var selectRecord = userGrid.getSelectionModel().getSelected();
			if (!selectRecord){
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
  			return;
			}
			Ext.loadRemoteScript("mappingEdit.jsp?selectUserId="+selectRecord.get("id"));
		}
	}, '-' ,{
		text:"ɾ��",
		handler:function(){
			var selectRecord = userGrid.getSelectionModel().getSelected();
			if (!selectRecord){
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
  			return;
			}
			var mappingRecord =userMappingGrid.getSelectionModel().getSelected();
			if (!mappingRecord){
				Ext.MessageBox.alert("��Ϣ","ѡ��Ҫɾ���ļ�¼");
  			return;
			}
			if (confirm("ȷ��Ҫɾ����Ӧ��ϵ��")){
				postForm({'postType':'deleteMapping','thirdSystem':mappingRecord.get("thirdSystem"),'thirdUserId':mappingRecord.get("thirdUserId"),'user':selectRecord.get("id")},"casAucPost.jsp",userMappingCallback);
			}
		}
	}]
});
function userMappingCallback(){
	userMappingDS.reload();
}

var eastPanel = {
    region:'east',
    layout:'border',
		bodyBorder: false,
		border:false,
		split: true,
		defaults: {
	    split: true,
			animFloat: false,
			autoHide: false,
			useSplitTips: true
		},
    width: 175,
    minSize: 100,
    maxSize: 250,
	//items:[userOrgGrid,userWgGrid,userMappingGrid]
	items:[userOrgGrid,userWgGrid]
};

Ext.onReady(function(){
	new Ext.Viewport({
		layout: 'border',
		items: [{
      region:'center',
      split:true,
      width: 225,
      minSize: 175,
      bodyBorder: false,
			border:false,
      maxSize: 400,
      layout:'fit',
      margins:'1 1 1 1',
      items: userGrid
		},eastPanel]
  });
  userDS.load({params:{start:0, limit:25}});
});

</script>
</body>
</html>
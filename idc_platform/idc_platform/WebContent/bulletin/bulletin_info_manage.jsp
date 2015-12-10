<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
<title>bulletin_query</title>
<%@ include file="/common/inc/header.jsp" %>
<script language="javascript">

Ext.apply(Ext.form.TextField.prototype,{ 
    validator:function(text){
        if(this.allowBlank==false &&Ext.util.Format.trim(text).length==0)
        	return false;
        else
        	return true;
    }
});

pageConst={
	queryBulletinUrl : '<%=ctx%>/bulletin/queryBulletinInfo.action',
	addBulletinUrl : '<%=ctx%>/bulletin/addBulletinInfo.action',
	deleteBulletinUrl: '<%=ctx%>/bulletin/deleteBulletinInfo.action',
	updateBulletinUrl: '<%=ctx%>/bulletin/updateBulletinInfo.action'
}

Ext.onReady(function() {
  
    var store = new Ext.data.JsonStore(
    {
    	baseParams:{ajax:true},
        url: pageConst.queryBulletinUrl,
        root: "result",
        totalProperty: 'totalCount',
        groupField:'id',
        fields:['id','title','content','creator','creatorName','begin_time','end_time','created_time']
     
    });
    
 	store.on('beforeload',function(s,options){	
 			Ext.apply(s.baseParams,{title:Ext.getCmp("title").getValue()
 		});
 	});
 	var sm=new Ext.grid.RowSelectionModel({ singleSelect : true});
    sm.on('rowselect', function(sm_, rowIndex, record) {
		btnShow();
	}, this);
	sm.on('rowdeselect', function(sm_, rowIndex, record) {
		btnShow();
	}, this);
	store.on('load', function() {
		btnShow();
	});
    var grid = new Ext.grid.GridPanel({
    	id:'grid',
        title:'�����б�',
        store: store,
        loadMask: true,
        viewConfig: {
            forceFit: true
        },
        tbar:[
        	{
				id : 'addBtn',
				text : '����',
				iconCls : 'add',
				handler : function() {
					showBulletinEditWin(null);
				}
			}, '-', {
				id : 'delBtn',
				text : 'ɾ��',
				iconCls : 'remove',
				handler : function() {
					var records=grid.getSelectionModel().getSelections();
					var record=records[0];
					var id=record.get('id');
					var title=record.get('title');
					deleteBulletin(id,title);
				}
			}, '-', {
				id : 'editBtn',
				text : '�޸�',
				iconCls : 'edit',
				handler : function() {
					editBulletin();
				}
			},'-',{
				id : 'dtlBtn',
				text : '�鿴',
				iconCls : 'views',
				handler : function() {
					var records=grid.getSelectionModel().getSelections();
					var record=records[0];
					var id=record.get('id');
					showBulletin(id);
				}
			},'-','������⣺',
        	{
        		xtype:'textfield',
        		id:'title',
        		width:200
        	},
        	{	
        		text:'��ѯ',
        		iconCls:'search',
        		handler:function(){
        			gridReload();
        		}
        	}
        ],
        sm: sm,
        cm: new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
	        { header: "�������", sortable: true, dataIndex: 'title' },
	        { header: '������', sortable:true ,dataIndex: 'creatorName' },
	        { header: "��Чʱ��", sortable: true, dataIndex: 'begin_time' },
	        { header: "ʧЧʱ��", sortable: true, dataIndex: 'end_time' },
	        { header: "����ʱ��", sortable: true, dataIndex: 'created_time' }
        ]), 
        animCollapse: false,
     
        
        bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    store: store,
                    displayInfo: true,
                    displayMsg: '��{0} �� {1} ������ ��{2}��',
                    emptyMsg: "û������"
        })
        
    });
    
    
    var viewport = new Ext.Viewport({ 
    	renderTo:Ext.getBody(),
    	layout:'fit',
    	items:[grid]
    });
	store.load({params:{start:0, limit:20}});
}); 

function gridReload(){
	Ext.getCmp('grid').store.reload({params:{start:0, limit:20}});
}
function btnShow() {
	var btnDel = Ext.getCmp("delBtn");
	var btnEdit = Ext.getCmp("editBtn");
	var btnDtl = Ext.getCmp("dtlBtn");
	var grid = Ext.getCmp("grid");
	var selCount = grid.getSelectionModel().getCount();
	btnDel.setDisabled(selCount != 1);
	btnEdit.setDisabled(selCount != 1);
	btnDtl.setDisabled(selCount != 1);
}

var bulletinEditWin;
function showBulletinEditWin(id){
	if(!bulletinEditWin){
		var startDataPick= new Ext.form.DateField({
			id:'beginTime',
			name:'beginTime',
			format:'Y-m-d',
			altFormats: 'Y-m-d',
			fieldLabel:'��Ч����',
			width:500,
			allowBlank:false,
			listeners:{
				valid:function(field){
					if(Ext.getCmp("endTime").isValid())
						Ext.getCmp("endTime").clearInvalid();
					endtimeValid(Ext.getCmp("endTime"));
				}
			}
		});
		var endDataPick= new Ext.form.DateField({
			id:'endTime',
			name:'endTime',
			format:'Y-m-d',
			altFormats: 'Y-m-d',
			fieldLabel:'ʧЧ����',
			width:500,
			allowBlank:false,
			listeners:{
				valid:function(field){
					endtimeValid(field);
				}
			}
		});
		bulletinEditWin=new Ext.Window({
			id : 'bulletinEditWin',
			title :'����',
			layout : 'fit',
			closable : true,
			closeAction : 'hide',
			plain : true,
			modal : true,
			width : 700,
			height : 400,
			resizable : false,
			items : [ new Ext.form.FormPanel({
				id : 'bulletinForm',
				layout : 'form',
				frame : true,
				labelAlign : 'right',
				labelWidth :80,
				items : [
					{
						xtype:'textfield',
						id:'bulletinTitle',
						name:'bulletinInfo.title',
						fieldLabel:'�������',
						width:500,
						allowBlank:false,  
						maxLength:50,
						maxLengthText:'�������50���ַ�',
						blankText:"���ⲻ����Ϊ��!",
						invalidText:'�������ݲ�����Ҫ��'
					},
					{
						xtype:'htmleditor',
						id:'bulletinContent',
						name:'bulletinInfo.content',
						fieldLabel:'��������',
						allowBlank:false,  
						blankText:"���ݲ�����Ϊ��!",
						invalidText:'�������ݲ�����Ҫ��',
			            height:200,
			            anchor:'98%'
					},
					startDataPick,endDataPick,
					{
						xtype:'hidden',
						id:'bulletinId',
						name:'bulletinInfo.id'
					},
					{
						xtype:'hidden',
						id:'bulletinCreator',
						name:'bulletinInfo.creator'
					},
					{
						xtype:'hidden',
						id:'bulletinCreateDate',
						name:'createDate'
					},
					{
						xtype:'hidden',
						id:'bulletinUrl'
					}
				]
				})
			],
			buttonAlign : 'center',
			buttons : [ {
				text : '�ύ',
				handler : function() {
					var form = Ext.getCmp('bulletinForm');
					var url= Ext.getCmp('bulletinUrl').getValue();
					var et=Ext.getCmp('endTime');
					if(!htmlValid()){
						Ext.Msg.alert('��Ϣ', '�������ݹ�����');
						return
					}
					if (form.getForm().isValid()&&endtimeValid(et)) {
						Ext.MessageBox.wait("�ύ����...", "��ȴ�");
						form.form.doAction('submit', {
							url : url,
							method : 'POST',
							success : function(form, action) {
								Ext.MessageBox.hide();
								bulletinEditWin.hide();
								Ext.Msg.alert('�ɹ�', '���湫��ɹ���');
								gridReload();
							},
							failure : function(form, action) {
								Ext.MessageBox.hide();
								Ext.Msg.alert('ʧ��', '���湫��ʧ�ܣ�');
							}
						});
					} else {
						Ext.MessageBox.alert('У�����', '��ȷ����д��������ȷ�����ݣ�');
					}
				}
			}, {
				text : 'ȡ��',
				handler : function() {
					bulletinEditWin.hide();
				}
			} ],
			// �ر�ʱ�������
			listeners : {
				hide : function() {
					Ext.getCmp('bulletinForm').getForm().reset();
				}
			}
		
		});
	}
	bulletinEditWin.show();
	if(id!=null){
		bulletinEditWin.setTitle("�༭����");
		Ext.getCmp('bulletinUrl').setValue(pageConst.updateBulletinUrl);
		var records = Ext.getCmp('grid').getSelectionModel().getSelections();
		var record=records[0];
		Ext.getCmp('bulletinId').setValue(record.get('id'));
		Ext.getCmp('bulletinTitle').setValue(record.get('title'));
		Ext.getCmp('bulletinContent').setValue(record.get('content'));
		Ext.getCmp('beginTime').setValue(record.get('begin_time'));
		Ext.getCmp('endTime').setValue(record.get('end_time'));
		Ext.getCmp('bulletinCreateDate').setValue(record.get('created_time'));
		Ext.getCmp('bulletinCreator').setValue(record.get('creator'));
		if(record.get('created_time')!=null&&record.get('created_time')!=""){
			Ext.getCmp('beginTime').setMinValue(record.get('created_time'));
			Ext.getCmp('endTime').setMinValue(record.get('created_time'));
		}
		Ext.getCmp('endTime').validate();
	}
	else{
		bulletinEditWin.setTitle("��������");
		Ext.getCmp('bulletinUrl').setValue(pageConst.addBulletinUrl);
		Ext.getCmp('beginTime').setMinValue(new Date());
		Ext.getCmp('endTime').setMinValue(new Date());
	}
	
}
function htmlValid(){
	return Ext.getCmp("bulletinContent").getValue().length<500;
}
function endtimeValid(field){
	if(Ext.getCmp("beginTime")!=null){
          var dateBefore=Ext.getCmp("beginTime");
         if(dateBefore.getValue()!=null&&dateBefore.getValue()!=""
        	 &&field.getValue()!=""&&field.getValue()!=null){
        	  if(field.getValue()<=dateBefore.getValue()){
        		  field.markInvalid("ʧЧ�������Ӧ������Ч����");
        		  return false;
        	  }
    	  }
    }
	return field.isValid();
}

function editBulletin(){
	var records = Ext.getCmp('grid').getSelectionModel().getSelections();
	var record=records[0];
	showBulletinEditWin(record.get('id'));
}

function deleteBulletin(value,title){
	Ext.Msg.show({
        title:"ɾ������",
		msg:"�Ƿ�ȷ��ɾ������Ϊ��"+title+" �Ĺ��棿",
		buttons:Ext.MessageBox.YESNO,
		icon:Ext.MessageBox.QUESTION,
		fn:function(b){
			if(b=='yes'){
				Ext.Ajax.request({
           			url:pageConst.deleteBulletinUrl,
           			params:{bulletinId:value},
           			success:function(response){
           				if('success'==response.responseText.trim()){
           					Ext.Msg.show({
	              				title:"���",
								msg:"ɾ������ɹ�",
							 	buttons:Ext.MessageBox.OK,
							 	icon:Ext.MessageBox.INFO
						 	})
           					Ext.getCmp('grid').store.reload();
           				}
           				else{
           					Ext.Msg.show({
	              			 title:"ʧ��",
							 msg:"ɾ������ʧ�ܣ���ˢ��ҳ�������ݣ����������쳣����ϵͳ����Ա��ϵ��",
							 buttons:Ext.MessageBox.CANCEL,
							 icon:Ext.MessageBox.ERROR
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
var win;
function showBulletin(value){
   if(!win){
	 win=new Ext.Window({
	   id:'win',
	   layout:'fit',
       width:300,
       height:200,
       plain:true, 
       maximized:true,
       closeAction:'hide',
       title:'������Ϣ',
       html:'<iframe id="detailFrame" height="100%" width="100%"></ifram>',
       buttons:[{text:"����",handler:function(){win.hide();}}]
   	});
   }
   win.show();
   document.getElementById("detailFrame").src='getBulletinInfo.action?bulletinId='+value;
}

</script>
</head>

<body bgcolor="#d0d0d0">

</body>
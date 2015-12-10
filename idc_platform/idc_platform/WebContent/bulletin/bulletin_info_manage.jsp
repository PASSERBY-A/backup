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
        title:'公告列表',
        store: store,
        loadMask: true,
        viewConfig: {
            forceFit: true
        },
        tbar:[
        	{
				id : 'addBtn',
				text : '新增',
				iconCls : 'add',
				handler : function() {
					showBulletinEditWin(null);
				}
			}, '-', {
				id : 'delBtn',
				text : '删除',
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
				text : '修改',
				iconCls : 'edit',
				handler : function() {
					editBulletin();
				}
			},'-',{
				id : 'dtlBtn',
				text : '查看',
				iconCls : 'views',
				handler : function() {
					var records=grid.getSelectionModel().getSelections();
					var record=records[0];
					var id=record.get('id');
					showBulletin(id);
				}
			},'-','公告标题：',
        	{
        		xtype:'textfield',
        		id:'title',
        		width:200
        	},
        	{	
        		text:'查询',
        		iconCls:'search',
        		handler:function(){
        			gridReload();
        		}
        	}
        ],
        sm: sm,
        cm: new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
	        { header: "公告标题", sortable: true, dataIndex: 'title' },
	        { header: '发布人', sortable:true ,dataIndex: 'creatorName' },
	        { header: "生效时间", sortable: true, dataIndex: 'begin_time' },
	        { header: "失效时间", sortable: true, dataIndex: 'end_time' },
	        { header: "创建时间", sortable: true, dataIndex: 'created_time' }
        ]), 
        animCollapse: false,
     
        
        bbar: new Ext.PagingToolbar({
                    pageSize: 20,
                    store: store,
                    displayInfo: true,
                    displayMsg: '第{0} 到 {1} 条数据 共{2}条',
                    emptyMsg: "没有数据"
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
			fieldLabel:'生效日期',
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
			fieldLabel:'失效日期',
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
			title :'公告',
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
						fieldLabel:'公告标题',
						width:500,
						allowBlank:false,  
						maxLength:50,
						maxLengthText:'最多输入50个字符',
						blankText:"标题不可以为空!",
						invalidText:'输入内容不符合要求'
					},
					{
						xtype:'htmleditor',
						id:'bulletinContent',
						name:'bulletinInfo.content',
						fieldLabel:'公告内容',
						allowBlank:false,  
						blankText:"内容不可以为空!",
						invalidText:'输入内容不符合要求',
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
				text : '提交',
				handler : function() {
					var form = Ext.getCmp('bulletinForm');
					var url= Ext.getCmp('bulletinUrl').getValue();
					var et=Ext.getCmp('endTime');
					if(!htmlValid()){
						Ext.Msg.alert('信息', '公告内容过长！');
						return
					}
					if (form.getForm().isValid()&&endtimeValid(et)) {
						Ext.MessageBox.wait("提交数据...", "请等待");
						form.form.doAction('submit', {
							url : url,
							method : 'POST',
							success : function(form, action) {
								Ext.MessageBox.hide();
								bulletinEditWin.hide();
								Ext.Msg.alert('成功', '保存公告成功！');
								gridReload();
							},
							failure : function(form, action) {
								Ext.MessageBox.hide();
								Ext.Msg.alert('失败', '保存公告失败！');
							}
						});
					} else {
						Ext.MessageBox.alert('校验错误', '请确保填写完整且正确的数据！');
					}
				}
			}, {
				text : '取消',
				handler : function() {
					bulletinEditWin.hide();
				}
			} ],
			// 关闭时清空数据
			listeners : {
				hide : function() {
					Ext.getCmp('bulletinForm').getForm().reset();
				}
			}
		
		});
	}
	bulletinEditWin.show();
	if(id!=null){
		bulletinEditWin.setTitle("编辑公告");
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
		bulletinEditWin.setTitle("新增公告");
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
        		  field.markInvalid("失效完成日期应晚于生效日期");
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
        title:"删除公告",
		msg:"是否确认删除标题为："+title+" 的公告？",
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
	              				title:"完成",
								msg:"删除公告成功",
							 	buttons:Ext.MessageBox.OK,
							 	icon:Ext.MessageBox.INFO
						 	})
           					Ext.getCmp('grid').store.reload();
           				}
           				else{
           					Ext.Msg.show({
	              			 title:"失败",
							 msg:"删除公告失败，请刷新页面检查数据，如数据无异常请与系统管理员联系！",
							 buttons:Ext.MessageBox.CANCEL,
							 icon:Ext.MessageBox.ERROR
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
       title:'公告信息',
       html:'<iframe id="detailFrame" height="100%" width="100%"></ifram>',
       buttons:[{text:"返回",handler:function(){win.hide();}}]
   	});
   }
   win.show();
   document.getElementById("detailFrame").src='getBulletinInfo.action?bulletinId='+value;
}

</script>
</head>

<body bgcolor="#d0d0d0">

</body>
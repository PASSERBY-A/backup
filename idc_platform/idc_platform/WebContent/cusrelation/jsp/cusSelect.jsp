<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDC业务管理-客户管理</title>
</head>

<%@ include file="/common/inc/header.jsp"%>

<script type="text/javascript">
	function getWindowHeight(){
		var windowHeight;
		if (self.innerHeight) { // all except Explorer
			windowHeight = self.innerHeight;
		} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
			windowHeight = document.documentElement.clientHeight;
		} else if (document.body) { // other Explorers
			windowHeight = document.body.clientHeight;
		}
		return windowHeight
	}
	pageConst={
		queryCustomerInfo:'<%=ctx%>/cusrelation/queryCustomerRelationInfo.action'
	};

	Ext.onReady(function() {
		Ext.QuickTips.init();

		//查询条件面板
		var form = new Ext.form.FormPanel({
			labelWidth : 100,
			title : '查询条件',
			bodyStyle : 'padding:5px 5px 0',
			renderTo:'formdiv',
			autoHeight:true,
			layout : 'form',
			frame : true,
			labelAlign : 'right',
			buttonAlign : 'center',
			buttons : [
				{
					text : '搜索',
					//iconCls : 'search',
					handler : gridReload
				},{
					text : '重置',
					handler : function() {
						form.getForm().reset();
				}
			} ],
			items : [ 
				{
					items:[
						{
							columnWidth:1,
							layout:"column",
							autoHeight:true,
							items:[
								
								{layout:"form", labelWidth:90, width:300, items:[getBossCustomerIdNF()]},
								{layout:"form", labelWidth:90, width:300, items:[getCustomerTypeCB()]}
								
							]
						},
						{
							columnWidth:1,
							layout:"column",
							autoHeight:true,
							items:[
								{layout:"form", labelWidth:90, width:300, items:[getCustomerNameTI()]},
								{layout:"form", labelWidth:90, width:300, items:[getManagerNameTI()]}
							]
						},
						{
							columnWidth:1,
							layout:"column",
							autoHeight:true,
							items:[
								{layout:"form", labelWidth:90, width:300, items:[getPhoneNoTI()]},
								{layout:"form", labelWidth:90, width:300, items:[getEmailTI()]}
							]
						}
					]
				}
			]
		});
		//form.render();

		//客户基本信息数据组装
		var store = new Ext.data.Store({
			baseParams : {
				"id" : -1,
				ajax : true
			},
			proxy : new Ext.data.HttpProxy({
				url : pageConst.queryCustomerInfo
			}),
			reader : new Ext.data.JsonReader({
				totalProperty : 'totalCount',
				root : 'result'
			}, [ {
				name : 'id',
				mapping : 'id',
				type : 'int'
			}, {
				name : 'name',
				mapping : 'name',
				type : 'string'
			}, {
				name : 'typeId',
				mapping : 'typeId',
				type : 'int'

			}, {
				name : 'vocation',
				mapping : 'vocation',
				type : 'int'
			}, {
				name : 'icpCert',
				mapping : 'icpCert',
				type : 'int'
			}, {
				name : 'majorContact',
				mapping : 'majorContact',
				type : 'string'
			}, {
				name : 'address',
				mapping : 'address',
				type : 'string'
			}, {
				name : 'openTime',
				mapping : 'openTime',
				type : 'string'
			//renderer : Ext.util.Format.dateRenderer('Y年m月d日')
			}, {
				name : 'cancelTime',
				mapping : 'cancelTime',
				type : 'string'
			///renderer : Ext.util.Format.dateRenderer('Y年m月d日')
			}, {
				name : 'status',
				mapping : 'status',
				type : 'int'
			}, {
				name : 'email',
				mapping : 'email',
				type : 'string'
			}, {
				name : 'fax',
				mapping : 'fax',
				type : 'string'
			}, {
				name : 'phoneNo',
				mapping : 'phoneNo',
				type : 'string'
			}, {
				name : 'longinName',
				mapping : 'longinName',
				type : 'string'
			}, {
				name : 'abbrName',
				mapping : 'abbrName',
				type : 'string'
			}
			])
		});
		store.on('beforeload', function(s, options) {
			Ext.apply(s.baseParams, {
				search_id : Ext.getCmp("id").getValue(),
				search_typeId : Ext.getCmp("typeId").getValue(),
				search_name : Ext.getCmp("name").getValue(),
				search_managerName : Ext.getCmp("managerName").getValue(),
				search_phoneNo : Ext.getCmp("phoneNo").getValue(),
				search_email : Ext.getCmp("email").getValue()
			});
			
			Ext.getCmp('grid').setHeight(getWindowHeight() - Ext.get("griddiv").getY()-2);
		});

		//store.load({params:{start:0, limit:20}});
		//客户基本信息列表
		var gridPanel = new Ext.grid.GridPanel({
			id : 'grid',
			title : '客户列表',
			renderTo:'griddiv',
			store : store,
			loadMask : true,
			width: Ext.get("content").getWidth(), 
			height: Ext.get("content").getHeight(), 
			viewConfig : {
				forceFit : true
			},
			cm : new Ext.grid.ColumnModel([
					{
						header : "BOSS客户编号",
						sortable : true,
						dataIndex : 'id',
						width : 70
					},

					{
						header : "客户名称",
						sortable : true,
						dataIndex : 'name'
					},
					{
						header : "客户类别",
						sortable : true,
						dataIndex : 'typeId',
						renderer : function(data, metadata, record, rowIndex,
								columnIndex, store) {
							if (record.get('typeId') == 101) {
								return 'SP-WAP';
							}
							if (record.get('typeId') == 102) {
								return 'SP-短信';
							}

						}
					},
					{
						header : "行业类别",
						sortable : true,
						dataIndex : 'vocation',
						width : 60
					},
					{
						header : "ICP许可证",
						sortable : true,
						dataIndex : 'icpCert',
						renderer : function(data, metadata, record, rowIndex,
								columnIndex, store) {
							if (record.get('icpCert') == 1) {
								return '是';
							}
							if (record.get('icpCert') == 0) {
								return '否';
							}

						}
					},
					{
						header : "主要联系人",
						sortable : true,
						dataIndex : 'majorContact',
						width : 60
					},
					{
						header : "基本地址",
						sortable : true,
						dataIndex : 'address'
					},
					{
						header : "状态",
						sortable : true,
						dataIndex : 'status',
						renderer : function(data, metadata, record, rowIndex,
								columnIndex, store) {
							var value = record.get('status');
							if (value == 0)
								return "历史";
							if (value == 1)
								return "正常";
							if (value == 2)
								return "潜在";
							if (value == 3)
								return "注销";

						}
					} ]),
			animCollapse : false,
			//tbar : tbar,
			bbar : new Ext.PagingToolbar({
				displayMsg : '第{0} 到 {1} 条数据 共{2}条',
				emptyMsg : "没有数据",
				pageSize : 20,
				store : store,
				displayInfo : true
			}),
			listeners: {
				rowdblclick:function(grid,rowIndex,e){
			  		var record = this.store.getAt(rowIndex);
					var retv = "";
					for(var i in record.data){
						if(retv!="")
							retv+=",";
						retv = retv + i+ ":'" + record.data[i]+ "'";
					}
					window.returnValue = retv;
					window.close();
				}
		  	}
		});


		gridReload();
	});

	//列表载入
	function gridReload() {
		Ext.getCmp('grid').store.reload({
			params : {
				start : 0,
				limit : 20
			}
		});
	};
	//查询条件输入校验
	function checkSearch() {

	}
	/***判断是否为数字*******/
	function isNumber(str) {
		if ("" == str) {
			return false;
		}
		var reg = /\D/;
		return str.match(reg) == null;
	}
	
	var bossCustomerIdNF;
	var customerTypeCB;
	var customerNameTI;
	var managerNameTI;
	var phoneNoTI;
	var emailTI;
	
	function getBossCustomerIdNF(){
		if(!bossCustomerIdNF){
			bossCustomerIdNF=new Ext.form.NumberField({
				fieldLabel : 'BOSS客户编号',
				width:180,
				id : 'id',
				name : 'id'
			});
		}
		return bossCustomerIdNF;
	}
	
	function getCustomerTypeCB(){
		if(!customerTypeCB){
			customerTypeCB=new Ext.form.ComboBox({
				fieldLabel : '客户类别',
				width:180,
				id : 'typeId',
				name : 'typeId',
				mode : 'local',
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				blankText : '请选择客户类别',
				emptyText : '选择客户类别',
				store : new Ext.data.SimpleStore({
					fields : [ "value", "text" ],
					data : [ [ 101, "SP-WAP" ], [ 102, "SP-短信" ] ]
				}),
				valueField : "value",
				displayField : "text"
			});
		}
		return customerTypeCB;
	}
	
	 function getCustomerNameTI(){
	 	if(!customerNameTI){
			customerNameTI=new Ext.form.TextField({
				fieldLabel : '客户名称',
				width:180,
				id : 'name',
				name : 'name'
			});
		}
		return customerNameTI;
	}
					
	function getManagerNameTI(){
		if(!managerNameTI){
			managerNameTI=new Ext.form.TextField({
				fieldLabel : '大客户经理',
				width:180,
				id : 'managerName',
				name : 'managerName'
			});
		}
		return managerNameTI;
	}
	
	function getPhoneNoTI(){
		if(!phoneNoTI){
			phoneNoTI=new Ext.form.TextField({
				fieldLabel : '电话',
				width:180,
				id : 'phoneNo',
				name : 'phoneNo'
			});
		}
		return phoneNoTI;
	}
	
	function getEmailTI(){
		if(!emailTI){
			emailTI=new Ext.form.TextField({
				fieldLabel : 'Email',
				width:180,
				id : 'email',
				name : 'email'
			});
		}
		return emailTI;
	}
</script>

<body>
<div id="tab">
	<div id="formdiv"></div>
	<div id="content" style="width:100%;height:100%;">
	<div id="griddiv" ></div>
	</div>
</div>
</body>
</html>
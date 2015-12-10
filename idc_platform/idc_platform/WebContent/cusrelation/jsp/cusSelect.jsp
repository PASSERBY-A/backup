<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDCҵ�����-�ͻ�����</title>
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

		//��ѯ�������
		var form = new Ext.form.FormPanel({
			labelWidth : 100,
			title : '��ѯ����',
			bodyStyle : 'padding:5px 5px 0',
			renderTo:'formdiv',
			autoHeight:true,
			layout : 'form',
			frame : true,
			labelAlign : 'right',
			buttonAlign : 'center',
			buttons : [
				{
					text : '����',
					//iconCls : 'search',
					handler : gridReload
				},{
					text : '����',
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

		//�ͻ�������Ϣ������װ
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
			//renderer : Ext.util.Format.dateRenderer('Y��m��d��')
			}, {
				name : 'cancelTime',
				mapping : 'cancelTime',
				type : 'string'
			///renderer : Ext.util.Format.dateRenderer('Y��m��d��')
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
		//�ͻ�������Ϣ�б�
		var gridPanel = new Ext.grid.GridPanel({
			id : 'grid',
			title : '�ͻ��б�',
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
						header : "BOSS�ͻ����",
						sortable : true,
						dataIndex : 'id',
						width : 70
					},

					{
						header : "�ͻ�����",
						sortable : true,
						dataIndex : 'name'
					},
					{
						header : "�ͻ����",
						sortable : true,
						dataIndex : 'typeId',
						renderer : function(data, metadata, record, rowIndex,
								columnIndex, store) {
							if (record.get('typeId') == 101) {
								return 'SP-WAP';
							}
							if (record.get('typeId') == 102) {
								return 'SP-����';
							}

						}
					},
					{
						header : "��ҵ���",
						sortable : true,
						dataIndex : 'vocation',
						width : 60
					},
					{
						header : "ICP���֤",
						sortable : true,
						dataIndex : 'icpCert',
						renderer : function(data, metadata, record, rowIndex,
								columnIndex, store) {
							if (record.get('icpCert') == 1) {
								return '��';
							}
							if (record.get('icpCert') == 0) {
								return '��';
							}

						}
					},
					{
						header : "��Ҫ��ϵ��",
						sortable : true,
						dataIndex : 'majorContact',
						width : 60
					},
					{
						header : "������ַ",
						sortable : true,
						dataIndex : 'address'
					},
					{
						header : "״̬",
						sortable : true,
						dataIndex : 'status',
						renderer : function(data, metadata, record, rowIndex,
								columnIndex, store) {
							var value = record.get('status');
							if (value == 0)
								return "��ʷ";
							if (value == 1)
								return "����";
							if (value == 2)
								return "Ǳ��";
							if (value == 3)
								return "ע��";

						}
					} ]),
			animCollapse : false,
			//tbar : tbar,
			bbar : new Ext.PagingToolbar({
				displayMsg : '��{0} �� {1} ������ ��{2}��',
				emptyMsg : "û������",
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

	//�б�����
	function gridReload() {
		Ext.getCmp('grid').store.reload({
			params : {
				start : 0,
				limit : 20
			}
		});
	};
	//��ѯ��������У��
	function checkSearch() {

	}
	/***�ж��Ƿ�Ϊ����*******/
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
				fieldLabel : 'BOSS�ͻ����',
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
				fieldLabel : '�ͻ����',
				width:180,
				id : 'typeId',
				name : 'typeId',
				mode : 'local',
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				blankText : '��ѡ��ͻ����',
				emptyText : 'ѡ��ͻ����',
				store : new Ext.data.SimpleStore({
					fields : [ "value", "text" ],
					data : [ [ 101, "SP-WAP" ], [ 102, "SP-����" ] ]
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
				fieldLabel : '�ͻ�����',
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
				fieldLabel : '��ͻ�����',
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
				fieldLabel : '�绰',
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
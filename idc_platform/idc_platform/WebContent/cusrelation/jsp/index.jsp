<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDCҵ�����-�ͻ�����</title>
</head>

<%@ include file="/common/inc/header.jsp"%>

<script type="text/javascript">

	pageConst={
		queryCustomerInfo:'<%=ctx%>/cusrelation/queryCustomerRelationInfo.action',
		detailCustomer:'<%=ctx%>/cusrelation/detailCustomerRelation.action'
	};

	var store;
	Ext.onReady(function() {
		Ext.QuickTips.init();

		//form.render();

		//�ͻ�������Ϣ������װ
		store = new Ext.data.Store({
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
			},{
				name : 'abbrName',
				mapping : 'abbrName',
				type : 'string'
			}, {
				name : 'vocation',
				mapping : 'vocation',
				type : 'int'
			},  {
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
			} ])
		});
		 store.on('beforeload', function(s, options) {
			Ext.apply(s.baseParams);
		}); 

		//store.load({params:{start:0, limit:20}});
		//�ͻ�������Ϣ�б�
		var gridPanel = new Ext.grid.GridPanel({
			id : 'grid',
			title : '�ͻ��б�',
			region : 'center',
			store : store,
			loadMask : true,
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
						header : "�ͻ�����д",
						sortable : true,
						dataIndex : 'abbrName'
					},
					
					{
						header : "��ҵ���",
						sortable : true,
						dataIndex : 'vocation',
						width : 60
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
						header : "����ʱ��",
						sortable : true,
						dataIndex : 'openTime'
					},
					{
						header : "����ʱ��",
						sortable : true,
						dataIndex : 'cancelTime'
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
			tbar : ['-',{
				id : 'searchBtn',
				text : '��ѯ',
				iconCls:'search',
				handler : function() {
					showSearchWin();
				}
			} ,'-', {
				id : 'detailBtn',
				text : '��ϸ',
				iconCls:'views',
				handler : viewDetail
			} ],
			bbar : new Ext.PagingToolbar({
				displayMsg : '��{0} �� {1} ������ ��{2}��',
				emptyMsg : "û������",
				pageSize : 20,
				store : store,
				displayInfo : true
				
			}),
			listeners : {
				//������˫����������
				rowdblclick : viewDetail
			}
		});

		var viewport = new Ext.Viewport({
			layout : 'fit',
			items : [ gridPanel ]
		});

		gridReload();
		
		//��ϸ�鿴����
		function viewDetail() {
					//�鿴��ϸ
					var records = gridPanel.getSelectionModel().getSelections();
					var record = records[0];
					if (record != null) {
						window.location.href = pageConst.detailCustomer
										+ "?id=" + record.get('id');
					} else {
						Ext.Msg.alert("����", "��ѡ��һ����¼");
					}
				}
	});
	
	/*****************Ext.onReady End*/

	//�б�����function
	function gridReload() {
		Ext.getCmp('grid').store.reload({
			params : {
				start : 0,
				limit : 20
			}
		});
	};
	//��ѯ��������У��function
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

	/***********��������������********************/
	function showSearchWin() {
		//��ѯ�������
		var form = new Ext.form.FormPanel({
			labelWidth : 100,
			layout : 'form',
			autoHeight:true,
			frame : true,
			labelAlign : 'right',
			buttonAlign : 'center',

			items : [ {
				xtype : 'numberfield',
				fieldLabel : 'BOSS�ͻ����',
				id : 'id',
				name : 'id'
			},{
				xtype : 'textfield',
				fieldLabel : '�ͻ�����',
				id : 'name',
				name : 'name'
			}, {
				xtype : 'textfield',
				fieldLabel : '��ͻ�����',
				id : 'managerName',
				name : 'managerName'
			},{
				xtype : 'textfield',
				fieldLabel : '�绰',
				id : 'phoneNo',
				name : 'phoneNo'
			}, {
				xtype : 'textfield',
				fieldLabel : 'Email',
				id : 'email',
				name : 'email'
			}]
		});
	
		var SearchWin = new Ext.Window({
			title : '�����Ի���',
			width : 280,
			height : 220,
			plain : true,
			iconCls : 'search',
			modal : true,
			items : [ form ],
			buttons : [ {
				text : '����',
				handler : function() {
					search();
					SearchWin.close();
				}
			}, {
				text : 'ȡ��',
				handler : function() {
					SearchWin.close();
				}
			},{
				text : '����',
				handler : function() {
					form.getForm().reset();
				}
			} ]
		});
		SearchWin.show();

		
		//���ݲ�ѯ����funcion
		function search() {

			store.baseParams.search_id = form.getForm().findField("id").getValue();
			store.baseParams.search_name = form.getForm().findField("name")
					.getValue();
			store.baseParams.search_managerName = form.getForm().findField(
					"managerName").getValue();
			store.baseParams.search_phoneNo = form.getForm().findField("phoneNo")
					.getValue();
			store.baseParams.search_email = form.getForm().findField("email")
					.getValue();

			store.reload({
				params : {
					start : 0,
					limit : 20
				}
			});
		}
	}
</script>

<body>
</body>
</html>
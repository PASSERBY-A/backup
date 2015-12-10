<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>

<%@page import="com.hp.idc.common.Constant"%><html>
<head>
<title>IDC֪ʶ�����</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="Content-Type" content="text/html" ; charset="gbk">
</head>
<%@ include file="/common/inc/header.jsp"%>
<script type="text/javascript" src="<%=ctx%>/common/scripts/util.js"></script>
<script type="text/javascript" src="scripts/eventEdit.js"></script>
<script type="text/javascript" src="scripts/caseEdit.js"></script>
<script type="text/javascript" src="scripts/catalogEdit.js"></script>
<script type="text/javascript" src="scripts/deleteMethod.js"></script>
<script type="text/javascript">
	var pageObj = {
		selectNode : null,
		type : [ 'event', 'case' ],
		eventCatalog : [],
		caseCatalog : [],
		isRetired : 0,
		currentNodePath: null
	};
	var pageConst = {
		deleteKnowledgeUrl : 'deleteKnowledge.action',
		saveKnowledgeUrl : 'saveKnowledge.action',
		queryCatalogUrl : 'queryCatalog.action',
		queryKnowledgeUrl : 'queryKnowledge.action',
		loadKnowledgeUrl : 'loadKnowledge.action',
		loadEventUrl : 'loadEvent.action',
		loadCaseUrl : 'loadCase.action',
		saveEventUrl : 'saveEvent.action',
		saveCaseUrl : 'saveCase.action',
		saveCategoryUrl : 'saveCategory.action',
		deleteCategoryUrl : 'deleteCategory.action',
		loadTreeUrl : 'loadCateTree.action'
	}
	var record;
	var rootTree;
	var treeLoader;
	var leftTree;
	Ext.form.TextField.override({   
		initComponent: Ext.form.TextField.prototype.initComponent.createInterceptor(function(){   
	    if (this.allowBlank === false && this.fieldLabel) {   
			this.fieldLabel += '<font color=red>*</font>';   
			}   
		})   
	});
	Ext.onReady(function() {

		/***********���֪ʶ�����������***********/
		treeLoader = new Ext.tree.TreeLoader({
			
			baseParams : {
				'includeAll' : true,
				'id' : -1
			},
			dataUrl : pageConst.loadTreeUrl
			
		});
		rootTree = new Ext.tree.TreeNode({
			id : 0
	,
			draggable : false,
			leaf : false,
			text : "root"
		});
		leftTree = new Ext.tree.TreePanel({
			//region : 'west',
			id : 'leftTree',
			title : "֪ʶ�����",
			width : 200,
			margins : '0 0 5 5',
			cmargins : '0 5 5 5',
			minSize : 175,
			frame : false,
			animate : true,
			enableDD : false,
			autoScroll : true,
			collapsible : true,
			split : true,
			rootVisible : false,
			root : rootTree,
			loader : treeLoader,
			
			tbar : [ {
				id : 'addNode',
				text : '����',
				iconCls : '',
				handler : function() {
					//alert(leftTree.getNodeById(pageObj.selectNode.id).id);
					catalogOper('add');
				}
			}, '-', {
				id : 'delNode',
				text : 'ɾ��',
				iconCls : '',
				handler : function() {

					deleteCatalog();
				}
			}, '-', {
				id : 'editNode',
				text : '�޸�',
				iconCls : '',
				handler : function() {
					catalogOper('edit');
				}
			} ]
		});
		//leftTree.setRootNode(rootTree);
		/* var eventNode = new Ext.tree.AsyncTreeNode({
			id : 'event',
			draggable : false,
			leaf : false,
			text : "����֪ʶ��"
		});
		var caseNode = new Ext.tree.AsyncTreeNode({
			id : 'case',
			draggable : false,
			leaf : false,
			text : "����֪ʶ��"
		});
		rootTree.appendChild(eventNode);
		rootTree.appendChild(caseNode); */

		leftTree.loader.load(leftTree.root);
		//����ѡ�У�����ѡ�нڵ��id,��ˢ���Ҳ��б�
		leftTree.on('click', function(node, event) {
			pageObj.currentNodePath = node.getPath();
			pageObj.selectNode = node;
			loadSubCate(node.id);
		});

		//��װtreeLoader�ڵ�ѡ���¼�
		leftTree.loader.on('load' , function(node){   
			 if(!pageObj.currentNodePath){  
	  
	            if(leftTree.getRootNode()==node){  
	  
	               //��һ�μ��أ�Ĭ��ѡ�и��ڵ�   
	  				leftTree.fireEvent('click',node);  
	  
	            }  
	  
	         } else{  
	  					leftTree.selectPath(pageObj.currentNodePath,null,function(bSuccess,bNode){     
	  					if(bNode != null){
	  						bNode.fireEvent("click",bNode);  
	  					}else{
	  						pageObj.selectNode = bNode;
	  					}
	                     
	  
	               });  
	  
	            
	  
	         }  
	  
	});

		function treeReload() {
			treeLoader.reload();
		}
		function loadSubCate(cateOid) {
			treeLoader.baseParams.id = cateOid;
			store.baseParams.id = cateOid;
			store.load({
				params : {
					start : 0,
					limit : 20
				}
			});
		}
		/******************֪ʶ���б������*************************/
		var store = new Ext.data.Store({
			baseParams : {
				"id" : -1,
				ajax : true
			},
			proxy : new Ext.data.HttpProxy({
				url : pageConst.queryKnowledgeUrl
			}),
			reader : new Ext.data.JsonReader({
				totalProperty : 'totalCount',
				root : 'result'
			}, [ {
				name : 'id',
				mapping : 'id',
				type : 'string'
			}, {
				name : 'title',
				mapping : 'title',
				type : 'string'
			}, {
				name : 'keywords',
				mapping : 'keywords',
				type : 'string'
			}, {
				name : 'categoryId',
				type : 'string'
			}, {
				name : 'createDate',
				mapping : 'createDate',
				type : 'string'}, {
				name : 'creator',
				type : 'string'
			}, {
				name : 'isRetired',
				type : 'int'
			} ])
		});
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : false
		});
		//������ť��ЧʧЧ
		sm.on('rowselect', function(sm_, rowIndex, record) {
			btnEditShow();
			btnDelShow();
		}, this);
		sm.on('rowdeselect', function(sm_, rowIndex, record) {
			btnEditShow();
			btnDelShow();
		}, this);
		store.on('load', function() {
			btnEditShow();
			btnDelShow();
		});

		store.load({params:{start:0, limit:<%=Constant.ITEMS_PER_PAGE%>}});
		/********************֪ʶ���б�Panel********************/
		var delButton = new Ext.Button({
			id : 'delRBtn',
			text : '��ʾ��ɾ�ڵ�',
			handler : function() {
				if (pageObj.isRetired == 1) {
					pageObj.isRetired = <%=Const.KBM_ISRETIRED_NO%>;
					Ext.getCmp("delRBtn").setText('��ʾ��ɾ�ڵ�');
				} else {
					pageObj.isRetired = <%=Const.KBM_ISRETIRED_YES%>;
					Ext.getCmp("delRBtn").setText('��ʾ��Ч�ڵ�');
				}
				gridReload();
			}
		});
		
		var grid = new Ext.grid.GridPanel({
			id : 'grid',
			region : 'center',
			title : '��ѯ���',
			margins : '0 5 5 0',
			store : store,
			loadMask : true,
			viewConfig : {
				forceFit : true,
				autoFill: true
			},
			//��ť������
			tbar : [ {
				id : 'addBtn',
				text : '����',
				iconCls : '',
				handler : function() {
					showEditWin('add');
				}
			}, '-', {
				id : 'delBtn',
				text : 'ɾ��',
				iconCls : '',
				handler : function() {
					deleteKnowledge();
				}
			}, '-', {
				id : 'editBtn',
				text : '�޸�',
				iconCls : '',
				handler : function() {
					showEditWin('edit');
				}
			}, '-', '�ؼ��֣�',{
				xtype : 'textfield',
				id : 'search',
				name : 'search',
				width : 150,
				emptyText : '����ؼ���',
			    regex: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
			    regexText:'��ѯ�����в��ܰ����������'


			}, {
				tooltip : '�������',
				text : '����',
				iconCls : 'search',
				handler : function(){
					Ext.getCmp('search').setValue(trim(Ext.getCmp('search').getValue()));
					if(Ext.getCmp('search').isValid())
					{ 
						gridReload();
					}
					else
					{
						Ext.Msg.alert("��ʾ", "��ѯ�����в��ܰ����������");
					    return;
					}
				}
			},{
				text:'�߼���ѯ',
				iconCls:'search',
				handler:function(){
					showSearch();
				}
			}],
			//autoExpandColumn:'descn', 
			sm : sm,
			cm : new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(),
					sm,
					{
						header : "֪ʶ���",
						sortable : true,
						dataIndex : 'id',
						renderer:redKeywords
					},
					{
						header : '����',
						sortable : true,
						dataIndex : 'title',
						width : 80,
						renderer:redKeywords
					},
					{
						header : '�ؼ���',
						sortable : true,
						dataIndex : 'keywords',
						width : 80,
						renderer:redKeywords
					},
					{
						header : '���',
						sortable : true,
						dataIndex : 'categoryId',
						width : 80
					},

					{
						header : '��������',
						sortable : true,
						dataIndex : 'createDate',
						width : 80
					},
					{
						header : '������',
						dataIndex : 'creator',
						width : 80
					},
					{
						header : '�Ƿ����',
						dataIndex : 'isRetired',
						width : 80,
						renderer : function(data, metadata, record, rowIndex,
								columnIndex, store) {
							return record.get('isRetired') ? '��' : '��';
						}
					} ]),
			animCollapse : false,
			bbar : new Ext.PagingToolbar({
				displayMsg : '<%=Constant.MSG_PAGE_DISPLAY%>'
	,emptyMsg : '<%=Constant.MSG_PAGE_EMPTY%>',
	pageSize : 20,
				store : store,
				displayInfo : true,
				items : [ '-', delButton ]
			}),
			listeners : {
				//������˫����������
				rowdblclick : function() {
					//�༭��¼
					showEditWin('edit');
				}
			}
		});

		store.on('beforeload', function(s, options) {
			Ext.apply(s.baseParams, {
				search : Ext.getCmp("search").getValue(),
				isRetired : pageObj.isRetired
			});
		}

		);
		/****************�˵����********************/

		/******************������*****************/
		var viewport = new Ext.Viewport({
			renderTo : Ext.getBody(),
			layout : "border",
			items : [ {
				xtype : 'panel',
				title : '',
				width : 200,

				layout : 'accordion',
				region : 'west',
				split : true,
				collapseMode : 'mini',
				animCollapse : false,
				enableTabScroll : true,
				//border:false,
				//activeItem: 0,
				//tbar:mainBar,
				items : [ leftTree ]
			}, grid ]
		});

	});

	//�б����
	function gridReload() {
		Ext.getCmp('grid').store.reload({
			params : {
				keywords : Ext.getCmp("search").getValue(),
				start : 0,
				limit : 20
			}
		});
	}

	//֪ʶ���б������ť��ʾ����
	function btnEditShow() {
		var btnEdit = Ext.getCmp("editBtn");
		var grid = Ext.getCmp("grid");
		var selCount = grid.getSelectionModel().getCount();
		btnEdit.setDisabled(selCount != 1);
	}
	function btnDelShow() {
		var btnDel = Ext.getCmp("delBtn");
		var grid = Ext.getCmp("grid");
		var selCount = grid.getSelectionModel().getCount();
		btnDel.setDisabled(selCount <= 0);
	}

	/*****************�༭��֪ʶ��Ϣ����*********************/
	//�ж�Ӧ�ñ༭�������͵�֪ʶ�⡣
	function showEditWin(oper) {
		//��ȡ�б���ѡ�еļ�¼
		record = Ext.getCmp('grid').getSelectionModel().getSelected();
		//Ext.Msg.alert(record.get('id')+''+oper);
		//��ȡ�������ѡ�еĽڵ�
		var node = pageObj.selectNode;
		oper = oper + '';
		if (node == null) {
			Ext.Msg.alert("����", "�����������ѡ��һ�ַ���");
		} else if (node.attributes.id == -1 || node.attributes.id == -2) {
			Ext.Msg.alert("����", "�÷��಻Ϊ��Ч����,������ӱ༭֪ʶ��");
		} else {
			if (oper == 'edit' && record != null) {
				//�޸Ĳ���
				var type = node.attributes.baseType;//ѡ�нڵ�BaseType
				var id = record.get('id');
				if (type == 2) {
					//����
					showCaseEditWin(id, oper);
				} else {
					//����֪ʶ
					showEventEditWin(id, oper);
				}
			} else if (oper == 'edit') {
				//������޸ģ�����û��ѡ��֪ʶ�㣨Ӧ�ò��ᷢ����
				Ext.Msg.alert("����", "��ѡ����Ҫ�޸ĵ�֪ʶ��");
			} else if (oper == 'add' && node != null) {
				id = null;
				//��������
				if (node.attributes.baseType == 2) {
					//����
					showCaseEditWin(id, oper);
				} else {
					//����֪ʶ
					showEventEditWin(id, oper);
				}
			} else {//û��ѡ��֪ʶ�⣬Ҳû��ѡ��֪ʶ���¼���޷�ȷ�����������Ǳ༭����ʾ����
				Ext.Msg.alert("����", "�����������ѡ��һ��֪ʶ������");
			}
		}
	}
var searchWin;
function showSearch(){
	if(!searchWin){
		searchWin=new Ext.Window({
			id : 'searchWin',
			title : '�߼���ѯ',
			layout : 'fit',
			closable : true,
			closeAction : 'hide',
			constrain:true,
			plain : true,
			modal : true,
			width : 500,
			height : 240,
			resizable : false,
			items : [ new Ext.form.FormPanel({
					id : 'productForm',
					layout : 'form',
					frame : true,
					labelAlign : 'right',
					labelWidth : 65,
					items:[
						{
							xtype:'numberfield',
							id:'knowledgeId',
							width:380,
							fieldLabel:'֪ʶ���'
						},
						{
							xtype:'textfield',
							id:'title',
							width:380,
							fieldLabel:'����'
						},
						{
							xtype:'textfield',
							id:'keyword',
							width:380,
							fieldLabel:'�ؼ���'
						}
					]
				})
			],
			buttonAlign : 'center',
			buttons : [ {
					text : '��ѯ',
					handler : function() {
						Ext.getCmp('search').setValue("");
						Ext.getCmp('grid').store.reload({
							params : {
								knowledgeId : Ext.getCmp("knowledgeId").getValue(),
								title : Ext.getCmp("title").getValue(),
								keywords : Ext.getCmp("keyword").getValue(),
								start : 0,
								limit : 20
							}
						});
						searchWin.hide();
					}
				}
			]
		});
	}
	searchWin.show();
}

function redKeywords(val){
	var s=Ext.getCmp("search").getValue();
	
	if(val!=null&&s!=null){
		if(val.indexOf(s)>-1){
			var text="";
			text+=val.substring(0,val.indexOf(s));
			text+="<font color='red'>"+val.substr(val.indexOf(s),s.length)+"</font>";
			text+=val.substring(val.indexOf(s)+s.length,val.length);
			val=text;
		}
	}
	return val;
}

</script>

<body>
</body>
</html>

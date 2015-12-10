<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>

<%@page import="com.hp.idc.common.Constant"%><html>
<head>
<title>IDC知识库管理</title>
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

		/***********左边知识点分类树定义***********/
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
			title : "知识库管理",
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
				text : '新增',
				iconCls : '',
				handler : function() {
					//alert(leftTree.getNodeById(pageObj.selectNode.id).id);
					catalogOper('add');
				}
			}, '-', {
				id : 'delNode',
				text : '删除',
				iconCls : '',
				handler : function() {

					deleteCatalog();
				}
			}, '-', {
				id : 'editNode',
				text : '修改',
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
			text : "故障知识库"
		});
		var caseNode = new Ext.tree.AsyncTreeNode({
			id : 'case',
			draggable : false,
			leaf : false,
			text : "案例知识库"
		});
		rootTree.appendChild(eventNode);
		rootTree.appendChild(caseNode); */

		leftTree.loader.load(leftTree.root);
		//单击选中，保存选中节点的id,并刷新右侧列表
		leftTree.on('click', function(node, event) {
			pageObj.currentNodePath = node.getPath();
			pageObj.selectNode = node;
			loadSubCate(node.id);
		});

		//组装treeLoader节点选中事件
		leftTree.loader.on('load' , function(node){   
			 if(!pageObj.currentNodePath){  
	  
	            if(leftTree.getRootNode()==node){  
	  
	               //第一次加载，默认选中根节点   
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
		/******************知识点列表加载器*************************/
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
		//监听按钮生效失效
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
		/********************知识点列表Panel********************/
		var delButton = new Ext.Button({
			id : 'delRBtn',
			text : '显示已删节点',
			handler : function() {
				if (pageObj.isRetired == 1) {
					pageObj.isRetired = <%=Const.KBM_ISRETIRED_NO%>;
					Ext.getCmp("delRBtn").setText('显示已删节点');
				} else {
					pageObj.isRetired = <%=Const.KBM_ISRETIRED_YES%>;
					Ext.getCmp("delRBtn").setText('显示有效节点');
				}
				gridReload();
			}
		});
		
		var grid = new Ext.grid.GridPanel({
			id : 'grid',
			region : 'center',
			title : '查询结果',
			margins : '0 5 5 0',
			store : store,
			loadMask : true,
			viewConfig : {
				forceFit : true,
				autoFill: true
			},
			//按钮工具条
			tbar : [ {
				id : 'addBtn',
				text : '新增',
				iconCls : '',
				handler : function() {
					showEditWin('add');
				}
			}, '-', {
				id : 'delBtn',
				text : '删除',
				iconCls : '',
				handler : function() {
					deleteKnowledge();
				}
			}, '-', {
				id : 'editBtn',
				text : '修改',
				iconCls : '',
				handler : function() {
					showEditWin('edit');
				}
			}, '-', '关键字：',{
				xtype : 'textfield',
				id : 'search',
				name : 'search',
				width : 150,
				emptyText : '输入关键字',
			    regex: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
			    regexText:'查询条件中不能包含特殊符号'


			}, {
				tooltip : '点击搜索',
				text : '搜索',
				iconCls : 'search',
				handler : function(){
					Ext.getCmp('search').setValue(trim(Ext.getCmp('search').getValue()));
					if(Ext.getCmp('search').isValid())
					{ 
						gridReload();
					}
					else
					{
						Ext.Msg.alert("提示", "查询条件中不能包含特殊符号");
					    return;
					}
				}
			},{
				text:'高级查询',
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
						header : "知识编号",
						sortable : true,
						dataIndex : 'id',
						renderer:redKeywords
					},
					{
						header : '标题',
						sortable : true,
						dataIndex : 'title',
						width : 80,
						renderer:redKeywords
					},
					{
						header : '关键字',
						sortable : true,
						dataIndex : 'keywords',
						width : 80,
						renderer:redKeywords
					},
					{
						header : '类别',
						sortable : true,
						dataIndex : 'categoryId',
						width : 80
					},

					{
						header : '创建日期',
						sortable : true,
						dataIndex : 'createDate',
						width : 80
					},
					{
						header : '创建人',
						dataIndex : 'creator',
						width : 80
					},
					{
						header : '是否废弃',
						dataIndex : 'isRetired',
						width : 80,
						renderer : function(data, metadata, record, rowIndex,
								columnIndex, store) {
							return record.get('isRetired') ? '是' : '否';
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
				//监听，双击弹出窗口
				rowdblclick : function() {
					//编辑记录
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
		/****************菜单面板********************/

		/******************布局器*****************/
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

	//列表加载
	function gridReload() {
		Ext.getCmp('grid').store.reload({
			params : {
				keywords : Ext.getCmp("search").getValue(),
				start : 0,
				limit : 20
			}
		});
	}

	//知识点列表操作按钮显示控制
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

	/*****************编辑信知识点息窗口*********************/
	//判断应该编辑何种类型的知识库。
	function showEditWin(oper) {
		//获取列表上选中的记录
		record = Ext.getCmp('grid').getSelectionModel().getSelected();
		//Ext.Msg.alert(record.get('id')+''+oper);
		//获取类别树上选中的节点
		var node = pageObj.selectNode;
		oper = oper + '';
		if (node == null) {
			Ext.Msg.alert("错误", "请在左边树上选择一种分类");
		} else if (node.attributes.id == -1 || node.attributes.id == -2) {
			Ext.Msg.alert("错误", "该分类不为有效分类,不可添加编辑知识点");
		} else {
			if (oper == 'edit' && record != null) {
				//修改操作
				var type = node.attributes.baseType;//选中节点BaseType
				var id = record.get('id');
				if (type == 2) {
					//案例
					showCaseEditWin(id, oper);
				} else {
					//故障知识
					showEventEditWin(id, oper);
				}
			} else if (oper == 'edit') {
				//点击了修改，但是没有选择知识点（应该不会发生）
				Ext.Msg.alert("错误", "请选择需要修改的知识点");
			} else if (oper == 'add' && node != null) {
				id = null;
				//新增操作
				if (node.attributes.baseType == 2) {
					//案例
					showCaseEditWin(id, oper);
				} else {
					//故障知识
					showEventEditWin(id, oper);
				}
			} else {//没有选中知识库，也没有选中知识点记录，无法确定是新增还是编辑，提示错误
				Ext.Msg.alert("错误", "请在左边树上选择一种知识库类型");
			}
		}
	}
var searchWin;
function showSearch(){
	if(!searchWin){
		searchWin=new Ext.Window({
			id : 'searchWin',
			title : '高级查询',
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
							fieldLabel:'知识编号'
						},
						{
							xtype:'textfield',
							id:'title',
							width:380,
							fieldLabel:'标题'
						},
						{
							xtype:'textfield',
							id:'keyword',
							width:380,
							fieldLabel:'关键字'
						}
					]
				})
			],
			buttonAlign : 'center',
			buttons : [ {
					text : '查询',
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

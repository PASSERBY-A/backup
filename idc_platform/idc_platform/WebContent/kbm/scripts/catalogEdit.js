//知识类别树节点新增编辑
var catalogWin;
var type;
var id;
function catalogOper(oper) {
	if (pageObj.selectNode != null) {
		id = pageObj.selectNode.attributes.id;
		if (id == -1 || id == -2) {
			// 类别表中最高层级有效节点父id为-1
			id = -1;
		}
		;
		type = pageObj.selectNode.attributes.baseType;
		if ((id == -1 || id == -2) && oper == 'edit') {
			Ext.Msg.alert("错误", "不能修改根类型");
		} else if (id) {
			showCatalogWin(id, oper, type);
		}
	} else {
		Ext.Msg.alert("错误", oper == 'edit' ? "请选择需要修改的知识库类型"
				: "请选择需要添加下级类型的知识库类型");
	}
}
// 展示编辑窗口的方法
function showCatalogWin(id, oper, type) {

	if (!catalogWin) {
		catalogWin = new Ext.Window({
			id : 'catalogWin',
			title : oper == 'edit' ? '编辑' : '新增' + '知识库类型',
			layout : 'fit',
			closable : true,
			closeAction : 'hide',
			plain : true,
			modal : true,
			width : 400,
			height : 200,
			resizable : false,
			items : [ new Ext.form.FormPanel({
				id : 'catalogForm',
				layout : 'form',
				frame : true,
				labelAlign : 'right',
				items : [ {
					xtype : 'textfield',
					id : 'categoryName',
					fieldLabel : '知识库类别名称',
					name : 'kbcate.categoryName',
					allowBlank : false,
					blankText : '不能为空，请填写',
					regex : /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
					regexText : '名称不能包含特殊符号'
				}, {
					xtype : 'hidden',
					id : 'parentId',
					name : 'kbcate.parentCategoryId'
				}, {
					xtype : 'hidden',
					id : 'categoryId',
					name : 'kbcate.id'
				}, {
					xtype : 'hidden',
					id : 'baseType',
					name : 'kbcate.baseType'
				} ]
			}) ],
			buttonAlign : 'center',
			buttons : [
					{
						text : '提交',
						handler : function() {
							var form = Ext.getCmp('catalogForm');
							if (catalogValid()) {
								Ext.MessageBox.wait("提交数据...", "请等待");
								form.form.doAction('submit', {
									url : 'saveCategory.action' + '?path='
											+ pageObj.selectNode.getPath(),
									method : 'POST',
									success : function(form, action) {
										Ext.MessageBox.hide();

										Ext.Msg.alert('成功',
												action.result.message,
												function() {
													catalogWin.hide();
													leftTree.loader.load(leftTree.root);
													
													//新增操作，选中节点为新添加节点;编辑操作，选中当前选中节点
													if(oper == 'add'){
														pageObj.currentNodePath = action.result.path;
													}
													
													//alert(leftTree.getNodeById(-1).id+'###');
													//treeLoader.load(rootTree);
													//alert(leftTree.getNodeById(pageObj.selectNode.id).id+'###');
													//rootTree.expandPath(action.result.path);

													// rootTree.getOwnerTree().selectPath(action.result.path,'id',function(b,selN){
													// if( b )
													// selN.fireEvent('click',selN);
													// });
													// reload(rootTree,function(node){
													// rootTree.getOwnerTree().selectPath(action.result.path,'id',function(b,selN){
													// if( b )
													// selN.fireEvent('click',selN);
													// });
													// });
													// pageObj.selectNode.appendChild(node);

													// rootTree.expandAll();
													// leftTree.expandAll();

													// rootTree.getNodeById
													// (treeLoader.baseParams.id).selected();
													// rootTree.getOwnerTree().expandPath(rootTree.getOwnerTree().getNodeById
													// (treeLoader.baseParams.id).getPath());//
												});
									},
									failure : function(form, action) {
										Ext.MessageBox.hide();
										Ext.Msg.alert('失败', '知识类别保存失败');
									}
								});
							} else {
								// Ext.MessageBox.alert('数据不完整', '请确保填写完整的数据！');
							}
						}
					}, {
						text : '取消',
						handler : function() {
							catalogWin.hide();
						}
					} ],
			// 关闭时清空数据
			listeners : {
				hide : function() {
					Ext.getCmp('catalogForm').form.reset();
				}
			}
		});
	}
	catalogWin.show();
	if (oper == 'edit') {
		// 编辑
		Ext.getCmp('categoryName').setValue(pageObj.selectNode.attributes.text);
		Ext.getCmp('parentId').setValue(pageObj.selectNode.attributes.parent);
		Ext.getCmp('categoryId').setValue(id);
		Ext.getCmp('baseType').setValue(type);
	} else {
		// 新增
		Ext.getCmp('parentId').setValue(id);
		Ext.getCmp('baseType').setValue(type);
	}
}

function reload(node, callback, scope) {
	if (!node)
		return;
	node.collapse(false, false);
	while (node.firstChild) {
		node.removeChild(node.firstChild).destroy();
	}
	node.childrenRendered = false;
	node.loaded = false;
	if (node.isHiddenRoot()) {
		node.expanded = false;
	}
	alert(node.id);
	node.getOwnerTree().loader.load(node, function(node) {
		node.expand(true, false, callback);
	});
}
// 校验
function catalogValid() {
	if (trim(Ext.getCmp('categoryName').getValue()) == '') {
		Ext.MessageBox.alert('数据不完整', '请确保填写完整的数据！');
		return false;
	} else if (!Ext.getCmp('categoryName').isValid()) {
		Ext.MessageBox.alert('数据不正确', '请确保填写正确的数据！');
		return false;
	} else
		return true;
}

//֪ʶ������ڵ������༭
var catalogWin;
var type;
var id;
function catalogOper(oper) {
	if (pageObj.selectNode != null) {
		id = pageObj.selectNode.attributes.id;
		if (id == -1 || id == -2) {
			// ��������߲㼶��Ч�ڵ㸸idΪ-1
			id = -1;
		}
		;
		type = pageObj.selectNode.attributes.baseType;
		if ((id == -1 || id == -2) && oper == 'edit') {
			Ext.Msg.alert("����", "�����޸ĸ�����");
		} else if (id) {
			showCatalogWin(id, oper, type);
		}
	} else {
		Ext.Msg.alert("����", oper == 'edit' ? "��ѡ����Ҫ�޸ĵ�֪ʶ������"
				: "��ѡ����Ҫ����¼����͵�֪ʶ������");
	}
}
// չʾ�༭���ڵķ���
function showCatalogWin(id, oper, type) {

	if (!catalogWin) {
		catalogWin = new Ext.Window({
			id : 'catalogWin',
			title : oper == 'edit' ? '�༭' : '����' + '֪ʶ������',
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
					fieldLabel : '֪ʶ���������',
					name : 'kbcate.categoryName',
					allowBlank : false,
					blankText : '����Ϊ�գ�����д',
					regex : /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
					regexText : '���Ʋ��ܰ����������'
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
						text : '�ύ',
						handler : function() {
							var form = Ext.getCmp('catalogForm');
							if (catalogValid()) {
								Ext.MessageBox.wait("�ύ����...", "��ȴ�");
								form.form.doAction('submit', {
									url : 'saveCategory.action' + '?path='
											+ pageObj.selectNode.getPath(),
									method : 'POST',
									success : function(form, action) {
										Ext.MessageBox.hide();

										Ext.Msg.alert('�ɹ�',
												action.result.message,
												function() {
													catalogWin.hide();
													leftTree.loader.load(leftTree.root);
													
													//����������ѡ�нڵ�Ϊ����ӽڵ�;�༭������ѡ�е�ǰѡ�нڵ�
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
										Ext.Msg.alert('ʧ��', '֪ʶ��𱣴�ʧ��');
									}
								});
							} else {
								// Ext.MessageBox.alert('���ݲ�����', '��ȷ����д���������ݣ�');
							}
						}
					}, {
						text : 'ȡ��',
						handler : function() {
							catalogWin.hide();
						}
					} ],
			// �ر�ʱ�������
			listeners : {
				hide : function() {
					Ext.getCmp('catalogForm').form.reset();
				}
			}
		});
	}
	catalogWin.show();
	if (oper == 'edit') {
		// �༭
		Ext.getCmp('categoryName').setValue(pageObj.selectNode.attributes.text);
		Ext.getCmp('parentId').setValue(pageObj.selectNode.attributes.parent);
		Ext.getCmp('categoryId').setValue(id);
		Ext.getCmp('baseType').setValue(type);
	} else {
		// ����
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
// У��
function catalogValid() {
	if (trim(Ext.getCmp('categoryName').getValue()) == '') {
		Ext.MessageBox.alert('���ݲ�����', '��ȷ����д���������ݣ�');
		return false;
	} else if (!Ext.getCmp('categoryName').isValid()) {
		Ext.MessageBox.alert('���ݲ���ȷ', '��ȷ����д��ȷ�����ݣ�');
		return false;
	} else
		return true;
}

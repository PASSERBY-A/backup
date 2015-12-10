//ɾ��֪ʶ����Ϣ
function deleteKnowledge() {
	// ��ȡѡ��֪ʶ��������
	var rows = Ext.getCmp("grid").getSelections();
	var ids = "";
	Ext.Msg.show({
		title : "ɾ��֪ʶ��",
		msg : "�Ƿ�ȷ��ɾ��ѡ�еĹ���֪ʶ��",
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(b) {
			if (b == 'yes') {
				for ( var i = 0; i < rows.length; i++) {
					ids += rows[i].json.id + ",";// ��ȡ����ɾ������Ҫ��ID
				}

				Ext.MessageBox.show({
					title : '����ɾ��֪ʶ��',
					width : 280,
					wait : true,
					icon : Ext.MessageBox.INFO,
					cls : 'custom',
					closable : false
				});
				Ext.Ajax.request({
					url : pageConst.deleteKnowledgeUrl,
					method : 'POST',
					params : {
						ids : ids
					},
					success : function(response) {
						Ext.MessageBox.hide();
						var responseArray = Ext.util.JSON
								.decode(response.responseText);
						if (true == responseArray.success) {
							Ext.Msg.show({
								title : "���",
								msg : responseArray.message,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
							gridReload();
						} else {
							Ext.Msg.show({
								title : "ʧ��",
								msg : responseArray.message,
								buttons : Ext.MessageBox.CANCEL,
								icon : Ext.MessageBox.ERROR
							});
							gridReload();
						}
					},
					failure : function() {
						Ext.MessageBox.hide();
						Ext.MessageBox.hide();
						Ext.Msg.show({
							title : "ʧ��",
							msg : "����ʧ�ܣ��������磬���������쳣����ϵͳ����Ա��ϵ",
							buttons : Ext.MessageBox.CANCEL,
							icon : Ext.MessageBox.ERROR
						});
						gridReload();
					}
				});
			}
		}
	});
}

// ɾ��֪ʶ�����
function deleteCatalog() {
	// �ж��Ƿ�ѡ�������ڵ�
	if (pageObj.selectNode == null) {
		Ext.MessageBox.alert("����", "��ѡ��Ҫɾ����֪ʶ������");
	}
	// �ж��Ƿ�ѡ��������֪ʶ����ڵ�
	else {
		var id = pageObj.selectNode.attributes.id;
		var isleaf = pageObj.selectNode.attributes.leaf;
		if (id == -1 || id == -2) {
			Ext.MessageBox.alert("����", "����ɾ�����ڵ�");
		} else {
			var type = pageObj.selectNode.attributes.baseType;

			Ext.MessageBox.show({
				title : '����ɾ��֪ʶ������',
				width : 280,
				wait : true,
				icon : Ext.MessageBox.INFO,
				cls : 'custom',
				closable : false
			});
			Ext.Ajax.request({
				url : pageConst.deleteCategoryUrl,
				method : 'POST',
				params : {
					id : id,
					isleaf : isleaf
				},
				success : function(response) {
					Ext.MessageBox.hide();
					var responseArray = Ext.util.JSON
							.decode(response.responseText);
					if (true == responseArray.success) {
						Ext.Msg.show({
							title : "���",
							msg : "ɾ��֪ʶ�����ͳɹ�",
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.INFO
						});
						treeLoader.load(rootTree);
					} else {
						Ext.Msg.show({
							title : "ʧ��",
							msg : "ɾ��֪ʶ������ʧ�ܣ�" + responseArray.message,
							buttons : Ext.MessageBox.CANCEL,
							icon : Ext.MessageBox.ERROR
						});
						// gridReload();
					}

				},
				failure : function() {
					Ext.MessageBox.hide();
					Ext.Msg.show({
						title : "ʧ��",
						msg : "����ʧ�ܣ��������磬���������쳣����ϵͳ����Ա��ϵ",
						buttons : Ext.MessageBox.CANCEL,
						icon : Ext.MessageBox.ERROR
					});

				}
			});
		}
	}
}
//删除知识点信息
function deleteKnowledge() {
	// 获取选中知识点行数据
	var rows = Ext.getCmp("grid").getSelections();
	var ids = "";
	Ext.Msg.show({
		title : "删除知识点",
		msg : "是否确认删除选中的故障知识点",
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(b) {
			if (b == 'yes') {
				for ( var i = 0; i < rows.length; i++) {
					ids += rows[i].json.id + ",";// 获取批量删除所需要的ID
				}

				Ext.MessageBox.show({
					title : '正在删除知识点',
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
								title : "完成",
								msg : responseArray.message,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
							gridReload();
						} else {
							Ext.Msg.show({
								title : "失败",
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
							title : "失败",
							msg : "连接失败，请检查网络，如网络无异常请与系统管理员联系",
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

// 删除知识点分类
function deleteCatalog() {
	// 判断是否选中了树节点
	if (pageObj.selectNode == null) {
		Ext.MessageBox.alert("错误", "请选中要删除的知识库类型");
	}
	// 判断是否选择了两个知识库根节点
	else {
		var id = pageObj.selectNode.attributes.id;
		var isleaf = pageObj.selectNode.attributes.leaf;
		if (id == -1 || id == -2) {
			Ext.MessageBox.alert("错误", "不能删除根节点");
		} else {
			var type = pageObj.selectNode.attributes.baseType;

			Ext.MessageBox.show({
				title : '正在删除知识库类型',
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
							title : "完成",
							msg : "删除知识库类型成功",
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.INFO
						});
						treeLoader.load(rootTree);
					} else {
						Ext.Msg.show({
							title : "失败",
							msg : "删除知识库类型失败，" + responseArray.message,
							buttons : Ext.MessageBox.CANCEL,
							icon : Ext.MessageBox.ERROR
						});
						// gridReload();
					}

				},
				failure : function() {
					Ext.MessageBox.hide();
					Ext.Msg.show({
						title : "失败",
						msg : "连接失败，请检查网络，如网络无异常请与系统管理员联系",
						buttons : Ext.MessageBox.CANCEL,
						icon : Ext.MessageBox.ERROR
					});

				}
			});
		}
	}
}

Ext.workflow.Action = function() {
	this.xmlNode = null;
	this.workflowNode = null;
	this.toNode = null;
	this.graphicsPanel = null;
}

Ext.workflow.Action.prototype = {
	destroy : function() {
		this.el.dom.outerHTML = "";
		this.xmlNode.parentNode.removeChild(this.xmlNode);
	},

	getModule : function() {
		if (this.graphicsPanel == null)
			return this.workflowNode.graphicsPanel.moduleOid;
		else
			return this.graphicsPanel.moduleOid;
	},
	editProperty : function() {
		var d = [];
		var nodes;
		if (this.graphicsPanel == null) {
			this.graphicsPanel = this.workflowNode.graphicsPanel;
		}
		nodes = this.graphicsPanel.workflowNodes;

        for(var i = 0; i < nodes.length; i++) {
             d.push([nodes[i].getNodeId(), nodes[i].getNodeName()]);
        }
		var store0 = new Ext.data.SimpleStore({
			'id': 0,
			fields: ['value', 'text'],
			data : d
		});
		var store1 = new Ext.data.SimpleStore({
			'id': 0,
			fields: ['value', 'text'],
			data : [ ['1', '上'],['2', '左'],['3', '右'],['4', '下']]
		});
		var fld_fromNode = new Ext.form.ComboBox({
			store: store0,
			anchor:'-20',
			name:'fld_fromNode',
			displayField:'text',
			fieldLabel: '所属结点',
			valueField:'value',
			typeAhead: false,
			mode: 'local',
			triggerAction: 'all',
			emptyText:'请选择...',
			editable:false,
			forceSelection:true,
			selectOnFocus:true,
			allowBlank: false
		});
		if (this.xmlNode != null) {
			fld_fromNode.setValue(this.workflowNode.getNodeId());
			fld_fromNode.disabled = true;
		}
		var fld_toNode = new Ext.form.ComboBox({
			store: store0,
			anchor:'-20',
			name:'fld_toNode',
			displayField:'text',
			fieldLabel: '目标结点',
			valueField:'value',
			typeAhead: false,
			mode: 'local',
			triggerAction: 'all',
			emptyText:'请选择...',
			editable:false,
			forceSelection:true,
			selectOnFocus:true,
			allowBlank: false
		});
		if (this.xmlNode != null) {
			fld_toNode.setValue(this.getToNodeId());
			fld_toNode.disabled = true;
		}
		var fld_frompos = new Ext.form.ComboBox({
			store: store1,
			anchor:'-20',
			name:'fld_frompos',
			displayField:'text',
			fieldLabel: '起始位置',
			valueField:'value',
			typeAhead: false,
			mode: 'local',
			triggerAction: 'all',
			editable:false,
			emptyText:'请选择...',
			forceSelection:true,
			selectOnFocus:true,
			allowBlank: false
		});
		if (this.xmlNode != null) {
			fld_frompos.setValue(this.getFromPos());
		}
		var fld_topos = new Ext.form.ComboBox({
			store: store1,
			anchor:'-20',
			name:'fld_topos',
			displayField:'text',
			fieldLabel: '结束位置',
			valueField:'value',
			typeAhead: false,
			mode: 'local',
			triggerAction: 'all',
			editable:false,
			emptyText:'请选择...',
			forceSelection:true,
			selectOnFocus:true,
			allowBlank: false
		});
		if (this.xmlNode != null) {
			fld_topos.setValue(this.getToPos());
		}
		
		var fld_id = new Ext.form.TextField({
			name: 'fld_id',
			fieldLabel: 'ID',
			readOnly: true,
			allowBlank: false
		});
		fld_id.setValue(this.getId());
		
		var fld_name = new Ext.form.TextField({
				name: 'fld_name',
				anchor:'-20',
				fieldLabel: '名称',
				allowBlank: false
			});
		if (this.xmlNode != null)
			fld_name.setValue(this.getName());
		
		var fld_groupId = new Ext.form.TextField({
			name: 'fld_groupId',
			anchor:'-20',
			fieldLabel: '分组'
		});
		if (this.xmlNode != null)
			fld_groupId.setValue(this.getGroupId());

		var fld_mutiassign = new Ext.form.Checkbox({
				name: 'fld_mutiassign',
				fieldLabel: '多次分配',
				allowBlank: false
			});

		var fld_displayAhead = new Ext.form.Checkbox({
				name: 'fld_displayAhead',
				fieldLabel: '优先显示',
				allowBlank: true
			});
			
		var fld_select_mode = new Ext.ux.RadioGroup({
			name:'fld_select_mode',
			fieldLabel: '表单类型',
			horizontal:true,
			radios:[{
				boxLabel :'本地',
				value:'local',
				listeners:{
					check:function(obj,citem){
						if(citem) {
							fld_form.allowBlank = false;
							fld_viewTemplate.allowBlank = false;
							
							fld_remote_viewPage.allowBlank = true;
							fld_remote_dealPage.allowBlank = true;
						} else {
							fld_form.allowBlank = true;
							fld_viewTemplate.allowBlank = true;
							
							fld_remote_viewPage.allowBlank = false;
							fld_remote_dealPage.allowBlank = false;
						}
					}
				}
			},{
				boxLabel :'远程',
				value:'remote'
			}]
		});

		
		var ds = new Ext.data.Store({
			proxy: new Ext.data.HttpProxy({
				url: ITSM_HOME+'/configure/form/formQuery.jsp'
			}),

			baseParams: { "module": this.getModule(), "start": 0, "limit" : 999999 },
			// create reader that reads the Topic records
			reader: new Ext.data.JsonReader({
				root: 'items',
				totalProperty: 'totalCount',
				id: 'id'
			}, [
				{name: 'id', mapping: 'id'},
				{name: 'name', mapping: 'name'}
			]),

			// turn on remote sorting
			remoteSort: true
		});



		var fld_form = new Ext.form.ComboBox({
			store: ds,
			anchor:'-5',
			name:'fld_form',
			displayField:'name',
			fieldLabel: '关联表单',
			valueField:'id',
			typeAhead: false,
			mode: 'remote',
			maxHeight: 150,
			triggerAction: 'all',
			emptyText:'请选择表单',
			forceSelection:true,
			selectOnFocus:true,
			allowBlank: false,
			listeners:{
				change:function(f,nv,ov) {
					fld_viewTemplate.params.formOid = nv;
				}
			}
		});
		if (this.xmlNode != null) {
			fld_form.setValue(this.getFormId());
		}

		var fld_transmit_form = new Ext.form.ComboBox({
			store: ds,
			anchor:'-5',
			name:'fld_transmit_form',
			displayField:'name',
			fieldLabel: '转发表单',
			valueField:'id',
			typeAhead: false,
			mode: 'remote',
			maxHeight: 150,
			triggerAction: 'all',
			emptyText:'请选择表单',
			forceSelection:true,
			selectOnFocus:true,
			allowBlank: true
		});
		if (this.xmlNode != null) {
			fld_transmit_form.setValue(this.getTransmitFormId());
			
		}

		var fld_viewTemplate = new Ext.form.SelectDialogField({
       fieldLabel: '展示模板',
       anchor:'-5',
       name: 'fld_viewTemplate',
       selectUrl : ITSM_HOME+'/configure/templateView.jsp',
       valueParam:'template',
       dialogWidth:800,
       dialogHeight:600,
       editable:true,
       allowBlank:false,
       value:'/default/nodeAction.html'
    });	

		var fld_desc = new Ext.form.TextArea({
			fieldLabel: '描述',
			anchor:'-20',
			name: 'fld_desc'
		});
		if (this.xmlNode != null)
			fld_desc.setValue(this.getDesc());
		
		var fld_remote_viewPage = new Ext.form.TextField({
				name: 'fld_remote_viewPage',
				anchor:'-5',
				fieldLabel: '转单页面',
				allowBlank: true
			});
		if (this.xmlNode != null) {
			fld_remote_viewPage.setValue(this.getRemoteViewPage());
		}
		var fld_remote_dealPage = new Ext.form.TextField({
				name: 'fld_remote_dealPage',
				anchor:'-5',
				fieldLabel: '处理页面',
				allowBlank: true
			});
		if (this.xmlNode != null) {
			fld_remote_dealPage.setValue(this.getRemoteDealPage());
		}
		/*====================表单布局开始===================*/
		var fieldBasic = new Ext.form.FieldSet({
			collapsible: false,
			title: '基本属性',
			layout : 'form',
			collapsed: false,
			anchor:'-20',
			items:[{
				layout:'column',
				border: false,
				items:[{
						columnWidth: .5,
						layout: 'form',
						border:false,
						items:[fld_id,fld_name,fld_groupId,fld_displayAhead,fld_mutiassign,fld_select_mode]
					},{
						columnWidth: .5,
						layout: 'form',
						border:false,
						items:[fld_fromNode, fld_toNode, fld_frompos, fld_topos]
					}]
			}]
		});
		var local_form = new Ext.form.FieldSet({
			title: '本地表单',
			layout: 'form',
			autoHeight:true,
			items:[fld_form,fld_transmit_form,fld_viewTemplate]
		});
		
		var remote_form = new Ext.form.FieldSet({
			title: '远程页面',
			layout: 'form',
			autoHeight:true,
			items:[//fld_remote_viewPage,
			fld_remote_dealPage,fld_remote_viewPage]
		})
		var fieldForm = {
			layout: 'column',
			border:false,
			anchor:'-20',
			items:[{
				columnWidth: .5,
				style : 'padding-right: 5px;',
				layout:'fit',
				items:[local_form]
			},{
				columnWidth: .5,
				items:[remote_form]
			}]
		};
		
		var tabs = new Ext.FormPanel({
			border:false,
			region:'center',
			autoScroll : true,
			frame : true,
			labelWidth:60,
			margins : '1 1 1 1',
			items: [fieldBasic,fieldForm,fld_desc]
		});

		if (this._win)
			this._win.close();
		var win = new Ext.Window({
			title: '动作信息',
			width: 500,
			height:480,
			minWidth: 300,
			minHeight: 200,
			layout: 'fit',
			plain:true,
			bodyStyle:'padding:5px;',
			items: tabs,

			buttons: [{
				text: '确定',
				scope: this,
				handler: function() {
					if (!this._form.form.isValid())
						return;
					if (this.xmlNode == null)
					{
						var startNode = this._form.form.findField("fld_fromNode").getValue();
						var toNode = this._form.form.findField("fld_toNode").getValue();
						if (startNode == toNode)
						{
							alert("所属结点和目标结束不能相同.");
							//return;
						}
						this.workflowNode = this.graphicsPanel.findNode(startNode);
						for (var i = 0; i < this.workflowNode.workflowActions.length; i++){
							if (this.workflowNode.workflowActions[i].getToNodeId() == toNode)
							{
								alert("已存在到目标节点的动作.");
								return;
							}
						}
						this.xmlNode = this.graphicsPanel.doc.createElement("action");
						this.toNode = this.graphicsPanel.findNode(toNode);
						this.workflowNode.workflowActions[this.workflowNode.workflowActions.length] = this;
						var n = this.workflowNode.xmlNode.selectSingleNode("./actions");
						if (n === null)
						{
							n = this.graphicsPanel.doc.createElement("actions");
							this.workflowNode.xmlNode.appendChild(n);
						}
						n.appendChild(this.xmlNode);
						this.xmlNode.setAttribute("to",  toNode);
						this.render();
						this.workflowNode.graphicsPanel.addAction(this);
					}
					this.setDesc(this._form.form.findField("fld_desc").getValue());
					this.setFromPos(this._form.form.findField("fld_frompos").getValue());
					this.setToPos(this._form.form.findField("fld_topos").getValue());
					this.setId(this._form.form.findField("fld_id").getValue());
					this.setGroupId(this._form.form.findField("fld_groupId").getValue());
					this.setName(this._form.form.findField("fld_name").getValue());
					this.setSelectMode(this._form.form.findField("fld_select_mode").getValue());
					this.setFormId(this._form.form.findField("fld_form").getValue());
					this.setTransmitFormId(this._form.form.findField("fld_transmit_form").getValue());
					this.setRemoteViewPage(this._form.form.findField("fld_remote_viewPage").getValue());
					this.setRemoteDealPage(this._form.form.findField("fld_remote_dealPage").getValue());
					this.setMutiAssign(this._form.form.findField("fld_mutiassign").getValue() ? "true" : "false");
					this.setDisplayAhead(this._form.form.findField("fld_displayAhead").getValue() ? "true" : "false");
					this.setViewTemplate(this._form.form.findField("fld_viewTemplate").getValue());
					
					this.workflowNode.graphicsPanel.createNewActId();
					this._win.hide();
					this.redrawLine();
				}
			},{
				text: '取消',
				scope: this,
				handler: function() {
					this._win.hide();
				}
			}]
		});
		this._form = tabs;
		this._win = win;
		win.show();

		if (this.xmlNode != null){
			fld_mutiassign.setValue(this.getMutiAssign());
			fld_displayAhead.setValue(this.getDisplayAhead());
			fld_viewTemplate.setValue(this.getViewTemplate());
			fld_viewTemplate.params.formOid = this.getFormId();
			fld_select_mode.setValue(this.getSelectMode());
		}
	},

	showContextMenu : function() {
		this.workflowNode.graphicsPanel.actionContextMenu.items.each(function(i) {
				i.processingAction = this;
			}, this);
		this.workflowNode.graphicsPanel.actionContextMenu.show(this.el);
	},

	render: function() {
		if (!this.el) {
			var text = "<v:polyline filled=false style='POSITION: absolute;top:0;left:0' points='-1,-1 -1,-1'></v:polyline>";
			var line = document.createElement(text);
			line.strokecolor = 'black';
			text = "<v:stroke EndArrow='Classic' dashstyle='solid'></v:stroke>";
			var stroke = document.createElement(text);
			line.appendChild(stroke);
			this.el = Ext.get(line);
			this.el.dom.oncontextmenu = function() { return false; };
			this.el.on("contextmenu", this.showContextMenu, this);
			this.el.on("dblclick", this.showContextMenu, this);
			this.workflowNode.graphicsPanel.addAction(this);
		}
		this.redrawLine();
    },

	getMutiAssign : function() {
		if (this.xmlNode.getAttribute("mutiAssign") == "true")
			return true;
		return false;
	},
	setMutiAssign : function(val) {
		this.xmlNode.setAttribute("mutiAssign", val);
	},

	getDisplayAhead : function() {
		if (this.xmlNode.getAttribute("displayAhead") == "true")
			return true;
		return false;
	},
	setDisplayAhead : function(val) {
		this.xmlNode.setAttribute("displayAhead", val);
	},

	setDesc : function(val) {
		this.xmlNode.setAttribute("desc", val);
	},
	getFromPos : function() {
		return parseInt(this.xmlNode.getAttribute("fromPos"));
	},
	getToPos : function() {
		return parseInt(this.xmlNode.getAttribute("toPos"));
	},

	getDesc : function() {
		return this.xmlNode.getAttribute("desc");
	},
	getToNodeId : function() {
		return this.xmlNode.getAttribute("to");
	},

	getFormId : function() {
		return this.xmlNode.getAttribute("formId");
	},

	setFormId : function(v) {
		this.xmlNode.setAttribute("formId", v);
	},

	getTransmitFormId : function() {
		var tfId = this.xmlNode.getAttribute("transmitFormId");
		return tfId==null?"":tfId;
	},
	setTransmitFormId : function(v) {
		if (v!=null && v!="")
			this.xmlNode.setAttribute("transmitFormId", v);
	},
	setFromPos : function(v) {
		this.xmlNode.setAttribute("fromPos", v);
	},
	setToPos : function(v) {
		this.xmlNode.setAttribute("toPos", v);
	},
	
	getId : function() {
		if (this.xmlNode!=null && this.xmlNode.getAttribute("id")!=null)
			return this.xmlNode.getAttribute("id");
		else
			return "act"+this.graphicsPanel.getNewActId();
	},
	setId : function(v) {
		this.xmlNode.setAttribute("id", v);
	},
	
	getGroupId : function() {
		if (this.xmlNode.getAttribute("groupId")!=null)
			return this.xmlNode.getAttribute("groupId");
		else
			return "";
	},
	setGroupId : function(v) {
		this.xmlNode.setAttribute("groupId", v);
	},
	
	getName : function() {
		return this.xmlNode.getAttribute("name");
	},
	setName : function(v) {
		this.xmlNode.setAttribute("name", v);
	},
	getViewTemplate : function() {
		var el = this.xmlNode.selectSingleNode("./template");
		if (el != null){
			return el.text;
		}
		return "/default/nodeAction.html";
	},
	setViewTemplate : function(val) {
		var n = this.xmlNode.selectSingleNode("./template");
		if (n === null)
		{
			var doc = new ActiveXObject("Microsoft.XMLDOM");
			n = doc.createElement("template");
			this.xmlNode.appendChild(n);
		}
		n.text = val;
	},
	getSelectMode : function () {
		var v = this.xmlNode.getAttribute("selectFormMode");
		if (v == null)
			v = "local";
		return v;
	},
	setSelectMode : function(v) {
		this.xmlNode.setAttribute("selectFormMode", v);
	},
	getRemoteViewPage :function() {
		var el = this.xmlNode.selectSingleNode("./remotePage/view");
		if (el != null){
			return el.text;
		}
	},
	setRemoteViewPage : function(val) {
		var nr = this.xmlNode.selectSingleNode("./remotePage");
		if (nr === null)
		{
			var doc = new ActiveXObject("Microsoft.XMLDOM");
			nr = doc.createElement("remotePage");
			this.xmlNode.appendChild(nr);
		}
		var n = nr.selectSingleNode("./view");
		if (n === null)
		{
			var doc = new ActiveXObject("Microsoft.XMLDOM");
			n = doc.createElement("view");
			nr.appendChild(n);
		}
		n.text = val;
	},
	getRemoteDealPage :function() {
		var el = this.xmlNode.selectSingleNode("./remotePage/deal");
		if (el != null){
			return el.text;
		}
	},
	setRemoteDealPage : function(val) {
		var nr = this.xmlNode.selectSingleNode("./remotePage");
		if (nr === null)
		{
			var doc = new ActiveXObject("Microsoft.XMLDOM");
			nr = doc.createElement("remotePage");
			this.xmlNode.appendChild(nr);
		}
		var n = nr.selectSingleNode("./deal");
		if (n === null)
		{
			var doc = new ActiveXObject("Microsoft.XMLDOM");
			n = doc.createElement("deal");
			nr.appendChild(n);
		}
		n.text = val;
	},
	
	redrawLine : function() {
		var points = this.calcPoints(this.workflowNode.getNodeX(), this.workflowNode.getNodeY(),
			this.workflowNode.width, this.workflowNode.height, this.getFromPos(),
			this.toNode.getNodeX(), this.toNode.getNodeY(),
			this.toNode.width, this.toNode.height, this.getToPos(), 0);
		this.el.dom.points.value = points;
	},

	calcPoints : function(x1, y1, w1, h1, p1, x2, y2, w2, h2, p2, delta)
	{
		if (p1 == 1) {
			x1 += w1 / 2;
		} else if (p1 == 2) {
			y1 += h1 / 2;
		} else if (p1 == 3) {
			x1 += w1;
			y1 += h1 / 2;
		} else {
			x1 += w1 / 2;
			y1 += h1;
		}
		if (p2 == 1) {
			x2 += w2 / 2;
		} else if (p2 == 2) {
			y2 += h2 / 2;
		} else if (p2 == 3) {
			x2 += w2;
			y2 += h2 / 2;
		} else {
			x2 += w2 / 2;
			y2 += h2;
		}
		// 调整位置
		if (x2 > x1)
			x2 -= delta;
		else
			x2 += delta;
		if (y2 > y1)
			y2 -= delta;
		else
			y2 += delta;

		var x0 = (x1 + x2) / 2;
		var y0 = (y1 + y2) / 2;
		var type = 1;
		if ((p1 == 4 && p2 == 1) || (p1 == 1 && p2 == 4))
			type = 0;
		else if ((p1 == 2 || p1 == 3) && (p2 == 2 || p2 == 3))
			type = 2;
		else if (p1 == 2 || p1 == 3)
			type = 3;
		else
			type = 1;
		var str;
		var dd = 10;
		if (p1 == 1 && p2 == 1)
			str = "" + x1 + "," + y1 + " " + (x1) + "," + (y1 - dd) + " " + x2 + "," + (y2 - dd) + " " + x2 + "," + y2;
		else if (p1 == 2 && p2 == 2)
			str = "" + x1 + "," + y1 + " " + (x1 - dd) + "," + (y1) + " " + (x2 - dd) + "," + (y2) + " " + x2 + "," + y2;
		else if (p1 == 3 && p2 == 3)
			str = "" + x1 + "," + y1 + " " + (x1 + dd) + "," + (y1) + " " + (x2 + dd) + "," + (y2) + " " + x2 + "," + y2;
		else if (p1 == 4 && p2 == 4)
			str = "" + x1 + "," + y1 + " " + (x1) + "," + (y1 + dd) + " " + x2 + "," + (y2 + dd) + " " + x2 + "," + y2;
		else if (type == 0)
			str = "" + x1 + "," + y1 + " " + x1 + "," + y0 + " " + x2 + "," + y0 + " " + x2 + "," + y2;
		else if (type == 1)
			str = "" + x1 + "," + y1 + " " + x1 + "," + y2 + " " + x2 + "," + y2;
		else if (type == 2)
			str = "" + x1 + "," + y1 + " " + x0 + "," + y1 + " " + x0 + "," + y2 + " " + x2 + "," + y2;
		else if (type == 3)
			str = "" + x1 + "," + y1 + " " + x2 + "," + y1 + " " + x2 + "," + y2;

		return str;
	}
};

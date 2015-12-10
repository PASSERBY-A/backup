
Ext.workflow.Node = Ext.extend(Ext.Panel, {
	initComponent : function(){
		this.xmlNode = null;
		this.graphicsPanel = null;
		this.workflowActions = [];
		this.tabs = null;
		Ext.workflow.Node.superclass.initComponent.call(this);
	},
	
	initFormLayout: function(){
		var fld_id = new Ext.form.TextField({
			name: 'fld_id',
			fieldLabel: 'ID',
			readOnly: true,
			allowBlank: false
		});
		fld_id.setValue(this.getNodeId());
		var fld_name = new Ext.form.TextField({
			name: 'fld_name',
			fieldLabel: '名称',
			allowBlank: false
		});
		fld_name.setValue(this.getNodeName());

		var fld_rollbackable = new Ext.form.Checkbox({
			name: 'fld_rollbackable',
			fieldLabel: '可以回退',
			allowBlank: false
		});

		var fld_forwardable = new Ext.form.Checkbox({
			name: 'fld_forwardable',
			fieldLabel: '可以转发',
			allowBlank: false
		});

		var fld_shareDeal = new Ext.form.Checkbox({
			name: 'fld_shareDeal',
			fieldLabel: '共享处理',
			allowBlank: false
		});
		
		var fld_enableSMS = new Ext.form.Checkbox({
			name: 'fld_enableSMS',
			fieldLabel: '启用短信',
			allowBlank: false
		});

		var fld_class = new Ext.form.TextField({
			name: 'fld_class',
			fieldLabel: '类名',
			readOnly: true
		});

		var fld_jars = new Ext.form.TextField({
			name: 'fld_jars',
			fieldLabel: '引用包'
		});
		var fld_imports = new Ext.form.TextField({
			name: 'fld_imports',
			fieldLabel: '导入类'
		});
		var fld_code = new Ext.form.TextArea({
			name: 'fld_code',
			fieldLabel: '代码',
			height: 150
		});
		fld_code.setValue(this.getCodeSource());
		fld_imports.setValue(this.getCodeImports());
		fld_jars.setValue(this.getCodeJars());
		fld_class.setValue(this.getCodeClassName());
		var ds = new Ext.data.Store({
			proxy: new Ext.data.HttpProxy({
				url: ITSM_HOME+'/configure/form/formQuery.jsp'
			}),

			baseParams: { "module": this.graphicsPanel.moduleOid, "start": 0, "limit" : 999999 },
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
			allowBlank: false
		});
		fld_form.setValue(this.getFormId());

		var fld_actionTemplate = new Ext.form.TextField({
	       fieldLabel: '展示模板',
	       name: 'fld_actionTemplate',
	       value:'/default/nodeViewForm.html'
	    });
		fld_actionTemplate.setValue(this.getActionTemplate());
		
		var fld_transmit_form = new Ext.form.ComboBox({
			store: ds,
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
		fld_transmit_form.setValue(this.getTransmitFormId());

		var fld_transmitTemplate = new Ext.form.TextField({
	       fieldLabel: '转发模板',
	       name: 'fld_transmitTemplate',
	       value:'/default/nodeAction.html'
	    });
		fld_transmitTemplate.setValue(this.getTransmitTemplate());
		
		var fld_queue = new Ext.form.TextField({
		       fieldLabel: '工单队列',
		       name: 'fld_queue',
		       allowBlank: true
		    });
		fld_queue.setValue(this.getQueue());
		
		var compile_btn = new Ext.Button({ text: '编译',
			scope: this,
			handler: function() {
				var wfOid = this._form.baseParams.wfOid;
				var wfVer = this._form.baseParams.wfVer;
				var wfNode = this._form.baseParams.wfNode;
				var code = this._form.form.findField("fld_code").getValue();
				var jars = this._form.form.findField("fld_jars").getValue();
				var imports = this._form.form.findField("fld_imports").getValue();
				var formElement = document.createElement("div");
				formElement.style.visibility = 'hidden';
				formElement.style.position = 'absolute';
				document.body.appendChild(formElement);
				var simple = new Ext.FormPanel({
					defaultType: 'textfield',
					method: 'POST',
					baseParams: { jars: jars, imports: imports, javasource: code, wfOid: wfOid,wfVer:wfVer,wfNode:wfNode},
					errorReader: new Ext.form.XmlErrorReader(),
					labelWidth: 75, // label settings here cascade unless overridden
					items: [{
							xtype: 'hidden',
							name: '_temp_hidden_field'
						}],
					url:ITSM_HOME+'/configure/workflow/workflowCompile.jsp'
				});
				simple.render(formElement);
				simple.form._obj = this;
				simple.form.submit({
					waitMsg: '正在编译，请稍候...',
					success: function(form, action)
					{
						Ext.MessageBox.alert("提示", "编译成功");
						form._obj._compiled = true;
						form._obj._code = form._obj._form.form.findField("fld_code").getValue();
						form._obj._jars = form._obj._form.form.findField("fld_jars").getValue();
						form._obj._imports = form._obj._form.form.findField("fld_imports").getValue();
						form._obj._form.form.findField("fld_class").setValue(form.errorReader.xmlData.documentElement.text);
					},
					failure: function(form, action)
					{
						Ext.MessageBox.alert("编译失败", form.errorReader.xmlData.documentElement.text);
						form._obj._compiled = false;
					}
				});
			}
		});
	
		var tabPanel = new Ext.TabPanel({
			xtype:'tabpanel',
			activeTab: 0,
			height:320,
			items:[{
				title:'基本信息',
				layout:'form',
				defaultType: 'textfield',
				bodyStyle:'padding:10px',
				defaults: {width: 240},
				autoScroll:true,
				items: [fld_id, fld_name,fld_forwardable, fld_rollbackable,fld_shareDeal,fld_enableSMS,fld_form, fld_actionTemplate,fld_transmit_form,fld_transmitTemplate, fld_queue]
			},{
				title:'相关代码',
				layout:'form',
				bodyStyle:'padding:10px',
				defaults: {width: 240},
				defaultType: 'textfield',
				items: [fld_class, fld_jars, fld_imports, fld_code, compile_btn]
			}]
		});
		
		this.tabs = new Ext.FormPanel({
			labelWidth: 80,
			border:false,
			region:'center',
			baseParams: {wfOid:''},
			layout:'fit',
			items: tabPanel
		});
		var nodeArray = this.getNodesArray();
		var ruleData = this.getRule();
		addTab();
		function callBackfun(res){
	        var newComponent = eval(res.responseText);
	        for (var i = 0; i < ruleData.length; i++){
	        	var r = new Ext.data.Record(ruleData[i]);
	        	newComponent.getStore().add(r);
	        }
			tabPanel.insert(1,newComponent);
			tabPanel.setActiveTab(0);
		}
		
		function addTab(){
			Ext.Ajax.request({
			    url: ITSM_HOME+"/configure/workflow/rule/ruleList.jsp",
			    params: {},
			    success: callBackfun,
			    failure: function() {
			    	Ext.Msg.alert("获取信息失败", "服务器连接失败");
			    }
			});
		}
		
		this.tabs.baseParams.wfOid = this.graphicsPanel.wfOid;
		this.tabs.baseParams.wfVer = this.graphicsPanel.doc.documentElement.getAttribute("version");
		this.tabs.baseParams.wfNode = this.xmlNode.getAttribute("id");
	},
	
	beforeSubmit : function() {
		this.setNodeName(this._form.form.findField("fld_name").getValue());
		this.setFormId(this._form.form.findField("fld_form").getValue());
		var dataObj = [];
		var store = this._form.findById("ruleGrid").getStore();
		for (var i = 0; i < store.getCount(); i++) {
			dataObj[i] = store.getAt(i).data;
		}
		this.setRule(dataObj);
		this.setTransmitFormId(this._form.form.findField("fld_transmit_form").getValue());
		this.setForwardable(this._form.form.findField("fld_forwardable").getValue() ? "true" : "false");
		this.setRollbackable(this._form.form.findField("fld_rollbackable").getValue() ? "true" : "false");
		this.setShareDeal(this._form.form.findField("fld_shareDeal").getValue() ? "true" : "false");
		this.setEnableSMS(this._form.form.findField("fld_enableSMS").getValue() ? "true" : "false");
		this.setActionTemplate(this._form.form.findField("fld_actionTemplate").getValue());
		this.setTransmitTemplate(this._form.form.findField("fld_transmitTemplate").getValue());
		this.setQueue(this._form.form.findField("fld_queue").getValue());
		if (this._compiled)
			this.setSourceCode(this._code, this._jars, this._imports, this._form.form.findField("fld_class").getValue());

	},

	editProperty : function() {
		if (this.tabs == null)
			this.initFormLayout();

		if (this._win)
			this._win.close();
		var win = new Ext.Window({
			title: '节点信息',
			width: 400,
			height:420,
			minWidth: 300,
			minHeight: 200,
			layout: 'fit',
			plain:true,
			bodyStyle:'padding:5px;',
			items: this.tabs,

			buttons: [{
				text: '确定',
				scope: this,
				handler: function() {
					if (!this._form.form.isValid()){
						alert("请填写完整");
						return;
					}
					this.beforeSubmit();
					this.renderNode();
					this._win.hide();
				}
			},{
				text: '取消',
				scope: this,
				handler: function() {
					this._win.hide();
				}
			}]
		});
		this._form = this.tabs;
		this._win = win;
		win.show();
		this.tabs.form.findField("fld_rollbackable").setValue(this.getRollbackable());
		this.tabs.form.findField("fld_forwardable").setValue(this.getForwardable());
		this.tabs.form.findField("fld_shareDeal").setValue(this.getShareDeal());
		this.tabs.form.findField("fld_enableSMS").setValue(this.getEnableSMS());

	},

	removeAction : function(toNode) {
		for (var i = 0; i < this.workflowActions.length; i++) {
			if (this.workflowActions[i].toNode == toNode) {
				var o = this.workflowActions[i];
				this.workflowActions.splice(i, 1);
				o.destroy(true);
				return;
			}
		}
	},
	showContextMenu : function() {
		this.graphicsPanel.nodeContextMenu.items.each(function(i) {
				i.processingNode = this;
			}, this);
		this.graphicsPanel.nodeContextMenu.show(this.el);
	},

    onRender : function(ct, position){
        Ext.workflow.Node.superclass.onRender.call(this, ct, position);
		this.bwrap.setStyle('width', this.width + 2);
		this.body.dom.outerHTML = "";
        var dd = new Ext.workflow.DD(this);
		this.renderNode();
		this.el.setLeft(this.getNodeX());
		this.el.setTop(this.getNodeY());
		this.el.dom.oncontextmenu = function() { return false; };
		this.el.on("contextmenu", this.showContextMenu, this);
		this.el.on("dblclick", this.showContextMenu, this);
    },

	redrawActions : function() {
		for (var i = 0; i < this.workflowActions.length; i++) {
			this.workflowActions[i].redrawLine();
		}
		for (var i = 0; i < this.graphicsPanel.workflowNodes.length; i++)
		{
			var o = this.graphicsPanel.workflowNodes[i];
			for (var j = 0; j < o.workflowActions.length; j++)
			{
				var k = o.workflowActions[j];
				if (k.toNode == this)
				{
					k.redrawLine();
					break;
				}
			}
		}
	},
	parseActions : function()
	{
		var xmlNodes = this.xmlNode.selectNodes("./actions/action");
		for (var i = 0; i < xmlNodes.length; i++)
		{
			this.workflowActions[i] = new Ext.workflow.Action();
			this.workflowActions[i].xmlNode = xmlNodes[i];
			this.workflowActions[i].workflowNode = this;
			this.workflowActions[i].toNode = this.graphicsPanel.findNode(this.workflowActions[i].getToNodeId());
			this.workflowActions[i].render();
		}
	},
	
	getNodesArray : function() {
		
		var nodes;
		if (this.graphicsPanel == null)
			nodes = this.workflowNode.graphicsPanel.workflowNodes;
		else
			nodes = this.graphicsPanel.workflowNodes;
		var d = [];
		for(var i = 0; i < nodes.length; i++) {
	         d.push([nodes[i].getNodeId(), nodes[i].getNodeName()]);
	    }
		return d;
	},

	isStartNode : function() {
		return this.graphicsPanel.getStartNode() == this.getNodeId();
	},
	getNodeId : function() {
		return this.xmlNode.getAttribute("id");
	},

	setNodeX : function(x) {
		this.xmlNode.setAttribute("posX", x)
	},
	setNodeY : function(y) {
		this.xmlNode.setAttribute("posY", y)
	},
	getNodeX : function() {
		return parseInt(this.xmlNode.getAttribute("posX"));
	},
	getCodeJars : function() {
		var n = this.xmlNode.selectSingleNode("./script");
		if (n != null)
			return n.getAttribute("jars");
		return "";
	},
	getCodeImports : function() {
		var n = this.xmlNode.selectSingleNode("./script");
		if (n != null)
			return n.getAttribute("imports");
		return "";
	},
	getCodeClassName : function() {
		var n = this.xmlNode.selectSingleNode("./script");
		if (n != null)
			return n.getAttribute("className");
		return "";
	},
	getCodeSource : function() {
		var n = this.xmlNode.selectSingleNode("./script");
		if (n != null)
			return n.text;
		return "";
	},
	setSourceCode : function(javaSource, javaJars, javaImports, javaClassName)
	{
		var n = this.xmlNode.selectSingleNode("./script");
		if (n === null)
		{
			n = this.graphicsPanel.doc.createElement("script");
			n.text = "";
			this.xmlNode.appendChild(n);
		}
		var srcCData;
		//nodeType == 4 表示CDATA,原来用的不是CDATA，所以再次存储的时候，转换一下
		if (n.firstChild.nodeType != 4){
			srcCData = this.graphicsPanel.doc.createCDATASection("");
			n.text = "";
			n.appendChild(srcCData);
		} else {
			srcCData = n.firstChild;
		}
		n.setAttribute("jars", javaJars);
		n.setAttribute("imports", javaImports);
		n.setAttribute("className", javaClassName);
		srcCData.text = javaSource;
	},
	getNodeY : function() {
		return parseInt(this.xmlNode.getAttribute("posY"));
	},
	getNodeName : function() {
		return this.xmlNode.getAttribute("name");
	},
	setNodeName : function(val) {
		this.xmlNode.setAttribute("name", val);
	},
	getRule : function(){
		var el = this.xmlNode.selectSingleNode("./rule");
		if (el != null) {
			return Ext.decode(el.text);
		}
		return [];
	},
	setRule :function (val) {
		var n = this.xmlNode.selectSingleNode("./rule");
		var srcCData;
		if (n === null)
		{
			n = this.graphicsPanel.doc.createElement("rule");
			this.xmlNode.appendChild(n);
			srcCData = this.graphicsPanel.doc.createCDATASection("");
			n.appendChild(srcCData);
		} else {
			srcCData = n.firstChild;
		}
		var text = Ext.encode(val);
		srcCData.text = text;
	},
	
	getFormId : function() {
		return this.xmlNode.getAttribute("formId");
	},
	setFormId : function(val) {
		this.xmlNode.setAttribute("formId", val);
	},
	getTransmitFormId : function() {
		var tfId = this.xmlNode.getAttribute("transmitFormId");
		return tfId==null?"":tfId;
	},
	setTransmitFormId : function(v) {
		if (v!=null && v!="")
		this.xmlNode.setAttribute("transmitFormId", v);
	},

	getRollbackable : function() {
		if (this.xmlNode.getAttribute("rollbackable") == "false")
			return false;
		return true;
	},
	setRollbackable : function(val) {
		this.xmlNode.setAttribute("rollbackable", val);
	},

	getForwardable : function() {
		if (this.xmlNode.getAttribute("forwardable") == "false")
			return false;
		return true;
	},
	setForwardable : function(val) {
		this.xmlNode.setAttribute("forwardable", val);
	},
	
	getShareDeal : function() {
		if (this.xmlNode.getAttribute("shareDeal") == "true")
			return true;
		return false;
	},
	setShareDeal : function(val) {
		this.xmlNode.setAttribute("shareDeal", val);
	},
	
	getEnableSMS : function() {
		if (this.xmlNode.getAttribute("enableSMS") == "false")
			return false;
		return true;
	},
	setEnableSMS : function(val) {
		this.xmlNode.setAttribute("enableSMS", val);
	},

	getActionTemplate : function() {
		var el = this.xmlNode.selectSingleNode("./template");
		if (el != null){
			return el.text;
		}
		return "/default/nodeViewForm.html";
	},
	setActionTemplate : function(val) {
		var n = this.xmlNode.selectSingleNode("./template");
		if (n === null)
		{
			n = this.graphicsPanel.doc.createElement("template");
			this.xmlNode.appendChild(n);
		}
		n.text = val;
	},
	
	getTransmitTemplate : function() {
		var el = this.xmlNode.selectSingleNode("./transmitTemplate");
		if (el != null){
			return el.text;
		}
		return "/default/nodeAction.html";
	},
	setTransmitTemplate : function(val) {
		var n = this.xmlNode.selectSingleNode("./transmitTemplate");
		if (n === null)
		{
			n = this.graphicsPanel.doc.createElement("transmitTemplate");
			this.xmlNode.appendChild(n);
		}
		n.text = val;
	},

	getQueue : function() {
		return this.xmlNode.getAttribute("queue");		
	},
	
	setQueue : function(val) {
		this.xmlNode.setAttribute("queue", val);
	},
	
	renderNode : function() {
		if (this.graphicsEl)
			this.graphicsEl.outerHTML = "";
	}
});

Ext.workflow.NormalNode = Ext.extend(Ext.workflow.Node, {
	width : 80,
	height : 30,

	renderNode : function() {
		Ext.workflow.NormalNode.superclass.renderNode.call(this);
		var text = "<v:RoundRect style=\"WIDTH:" + (this.width - 4) + "px; HEIGHT: " + this.height + "px\"></v:RoundRect>";
		var vmlRect = document.createElement(text);
		vmlRect.fillcolor= 'red';
		vmlRect.strokecolor = 'black';

		// 加渐变色
		var temp_fill = document.createElement("<v:fill type='gradient' color2='yellow' angle='0' method='linear sigma'></v:fill>");
		vmlRect.appendChild(temp_fill);

		// 加阴影
		var temp_shadow = document.createElement("<v:shadow on='t' type='perspective' color='black' opacity='19660f' matrix ='' offset='2pt,3pt'></v:shadow>");
		vmlRect.appendChild(temp_shadow);

		temp_fill.innerHTML = "<table onselectstart='return false;' border=0 width='100%' height=" + this.height + "><tr><td align=center valign=center style='font-size:12px'>" + (this.isStartNode() ? "(*)" : "") + this.getNodeName() + "</td></tr></table>";
		this.bwrap.dom.appendChild(vmlRect);
		this.graphicsEl = vmlRect;
	}
});

Ext.workflow.BranchBegin = Ext.extend(Ext.workflow.Node, {
	width : 30,
	height : 30,

	renderNode : function() {
		Ext.workflow.BranchBegin.superclass.renderNode.call(this);
		var text = "<v:oval style=\"WIDTH:" + this.width + "px; HEIGHT: " + this.height + "px\"></v:oval>";
		var vmlRect = document.createElement(text);
		vmlRect.fillcolor= '#ffffff';
		vmlRect.strokecolor = 'black';
		this.bwrap.dom.appendChild(vmlRect);
		this.graphicsEl = vmlRect;
	}
});

Ext.workflow.BranchEnd = Ext.extend(Ext.workflow.Node, {
	width : 30,
	height : 30,

	renderNode : function() {
		Ext.workflow.BranchEnd.superclass.renderNode.call(this);
		var text = "<v:oval style=\"WIDTH:" + this.width + "px; HEIGHT: " + this.height + "px\"></v:oval>";
		var vmlRect = document.createElement(text);
		vmlRect.fillcolor= '#cccccc';
		vmlRect.strokecolor = 'black';
		this.bwrap.dom.appendChild(vmlRect);
		this.graphicsEl = vmlRect;
	}
});

Ext.workflow.Subflow = Ext.extend(Ext.workflow.Node, {
	width : 70,
	height : 30,

	renderNode : function() {
		Ext.workflow.Subflow.superclass.renderNode.call(this);
		var text = "<v:oval style=\"WIDTH:" + this.width + "px; HEIGHT: " + this.height + "px\"></v:oval>";
		var vmlRect = document.createElement(text);
		vmlRect.fillcolor= '#cccccc';
		vmlRect.strokecolor = 'black';
		this.bwrap.dom.appendChild(vmlRect);
		this.graphicsEl = vmlRect;
	},
	
	initFormLayout: function(){
		Ext.workflow.Subflow.superclass.initFormLayout.call(this);
		var fld_subflow = new Ext.form.ComboBox({
			store: new Ext.data.Store({
				proxy: new Ext.data.HttpProxy({
					url: ITSM_HOME+'/configure/workflowQuery.jsp'
				}),
	
				baseParams: { "module": "-1", "start": 0, "limit" : 999999 },
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
			}),
			name:'fld_subflow',
			displayField:'name',
			fieldLabel: '子流程',
			valueField:'id',
			typeAhead: false,
			mode: 'remote',
			maxHeight: 150,
			triggerAction: 'all',
			forceSelection:true,
			selectOnFocus:true,
			allowBlank: false,
			listeners: {
				change: function(el,newvalue,oldvalue) {
					subflowVerStore.baseParams.wfOid  = newvalue;
				}
			}
		});
		fld_subflow.setValue(this.getSubflow());
		
		
		var subflowVerStore = new Ext.data.Store({
				proxy: new Ext.data.HttpProxy({
					url: ITSM_HOME+'/configure/workflowVersionQuery.jsp'
				}),
	
				baseParams: { "wfOid": "-1", "start": 0, "limit" : 999999 },
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
			subflowVerStore.baseParams.wfOid  = this.getSubflow();
		var fld_subflowver = new Ext.form.ComboBox({
			store: subflowVerStore,
			name:'fld_subflowver',
			displayField:'name',
			fieldLabel: '子流程版本',
			valueField:'id',
			typeAhead: false,
			mode: 'remote',
			maxHeight: 150,
			editable:false,
			triggerAction: 'all',
			forceSelection:true,
			selectOnFocus:true,
			allowBlank: false
		});
		fld_subflowver.setValue(this.getSubflowVer());
		
		this.basicInfo.insert(4,fld_subflowver);
		this.basicInfo.insert(4,fld_subflow);
	},
	
	beforeSubmit : function() {
		Ext.workflow.Subflow.superclass.beforeSubmit.call(this);
		this.setSubflow(this._form.form.findField("fld_subflow").getValue());
		this.setSubflowVer(this._form.form.findField("fld_subflowver").getValue());
	},
	
	getSubflow : function() {
		return this.xmlNode.getAttribute("subflow");
	},
	setSubflow : function(val) {
		this.xmlNode.setAttribute("subflow", val);
	},
	
	getSubflowVer : function() {
		return this.xmlNode.getAttribute("subflowVer");
	},
	setSubflowVer : function(val) {
		this.xmlNode.setAttribute("subflowVer", val);
	}
});

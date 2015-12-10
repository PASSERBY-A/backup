Ext.namespace('Ext.workflow');

Ext.workflow.GraphicsPanel = Ext.extend(Ext.Panel, {
	autoScroll: true,

	setStartNode : function(id)
	{
		var id1 = this.getStartNode();
		var n1 = this.findNode(id1);
		var n2 = this.findNode(id);
		this.doc.documentElement.setAttribute("root", id);
		if (n1 != null)
			n1.renderNode();
		n2.renderNode();
	},

	getStartNode : function() {
		return this.doc.documentElement.getAttribute("root");
	},

	getNewId : function()
	{
		var rootNode = this.doc.documentElement;
		var id = rootNode.getAttribute("currentId");
		if (id == null)
		{
			rootNode.setAttribute("currentId", "2");
			return "1";
		}
		rootNode.setAttribute("currentId", parseInt(id) + 1);
		return id;
	},
	
	getNewActId:function()
	{
		var rootNode = this.doc.documentElement;
		var id = rootNode.getAttribute("currentActId");
		if (id == null)
		{
			return "1";
		}
		return id;
	},
	createNewActId:function(){
		var rootNode = this.doc.documentElement;
		var id = rootNode.getAttribute("currentActId");
		if (id == null) {
			rootNode.setAttribute("currentActId", "2");
		} else {
			rootNode.setAttribute("currentActId", parseInt(id) + 1);
		}
	},

	onNewNode : function() {
		var node = this.doc.createElement("node");
		this.doc.documentElement.appendChild(node);
		var newId = this.getNewId();
		var ret = "node" + newId;
		node.setAttribute("id", ret);
		node.setAttribute("posX", "0");
		node.setAttribute("posY", "0");
		node.setAttribute("name", '新节点'+newId);
		node.setAttribute("formId", "-1");
		node.setAttribute("type", 'normal');
		var i = this.workflowNodes.length;
		this.workflowNodes[i] = new Ext.workflow.NormalNode();
		this.workflowNodes[i].xmlNode = node;
		this.workflowNodes[i].graphicsPanel = this;
		this.addNode(this.workflowNodes[i]);
	},

	onNewBranchBegin : function() {
		var node = this.doc.createElement("node");
		this.doc.documentElement.appendChild(node);
		var ret = "node" + this.getNewId();
		node.setAttribute("id", ret);
		node.setAttribute("posX", "0");
		node.setAttribute("posY", "0");
		node.setAttribute("name", '分支开始');
		node.setAttribute("formId", "-1");
		node.setAttribute("type", 'branchBegin');
		var i = this.workflowNodes.length;
		this.workflowNodes[i] = new Ext.workflow.BranchBegin();
		this.workflowNodes[i].xmlNode = node;
		this.workflowNodes[i].graphicsPanel = this;
		this.addNode(this.workflowNodes[i]);
	},
	onNewBranchEnd : function() {
		var node = this.doc.createElement("node");
		this.doc.documentElement.appendChild(node);
		var ret = "node" + this.getNewId();
		node.setAttribute("id", ret);
		node.setAttribute("posX", "0");
		node.setAttribute("posY", "0");
		node.setAttribute("name", '分支结束');
		node.setAttribute("formId", "-1");
		node.setAttribute("type", 'branchEnd');
		var i = this.workflowNodes.length;
		this.workflowNodes[i] = new Ext.workflow.BranchEnd();
		this.workflowNodes[i].xmlNode = node;
		this.workflowNodes[i].graphicsPanel = this;
		this.addNode(this.workflowNodes[i]);
	},
	onNewSubflow : function() {
		var node = this.doc.createElement("node");
		this.doc.documentElement.appendChild(node);
		var ret = "node" + this.getNewId();
		node.setAttribute("id", ret);
		node.setAttribute("posX", "0");
		node.setAttribute("posY", "0");
		node.setAttribute("name", '子流程');
		node.setAttribute("formId", "-1");
		node.setAttribute("type", 'subflow');
		var i = this.workflowNodes.length;
		this.workflowNodes[i] = new Ext.workflow.Subflow();
		this.workflowNodes[i].xmlNode = node;
		this.workflowNodes[i].graphicsPanel = this;
		this.addNode(this.workflowNodes[i]);
	},

	onNewAction : function() {
		var action = new Ext.workflow.Action();
		action.graphicsPanel = this;
		action.editProperty();
	},
	viewXML : function() {
		alert(this.doc.xml);
	},

	initComponent : function(){
		this.doc = null;
		this.workflowNodes = [];

		this.tbar = [
		    {text: '增加节点', scope: this, handler: this.onNewNode},
			'-',
			{text: '增加分支开始', scope: this, handler: this.onNewBranchBegin},
			'-',
			{text: '增加分支结束', scope: this, handler: this.onNewBranchEnd},
			'-',
			{text: '增加动作', scope: this, handler: this.onNewAction}
			,'-',
			{text: '查看xml',scope: this, handler:this.viewXML}
		];

    Ext.workflow.GraphicsPanel.superclass.initComponent.call(this);

		var menu = new Ext.menu.Menu({
			id: 'basicMenu',
			items: [{
					icon: ITSM_HOME+'/images/cog_edit.png',
					text: '编辑节点信息',
					handler : function(item) {
						var node = item.processingNode;
						node.editProperty();
					}
				},
				new Ext.menu.Item({
					icon: ITSM_HOME+'/images/cross.gif',
					text: '删除流程节点',
					handler : function(item) {
						var node = item.processingNode;
						node.graphicsPanel.removeNode(node);
					}
				}),
				new Ext.menu.Item({
					icon: ITSM_HOME+'/images/connect.gif',
					text: '设置为开始节点',
					handler : function(item) {
						var node = item.processingNode;
						node.graphicsPanel.setStartNode(node.getNodeId());
					}
				})
			]
		});
		this.nodeContextMenu = menu;

		var menu2 = new Ext.menu.Menu({
			id: 'basicMenu',
			items: [{
					icon: ITSM_HOME+'/images/cog_edit.png',
					text: '编辑动作信息',
					handler : function(item) {
						var action = item.processingAction;
						action.editProperty();
					}
				},
				new Ext.menu.Item({
					icon: ITSM_HOME+'/images/cross.gif',
					text: '删除流程动作',
					handler : function(item) {
						var action = item.processingAction;
						action.workflowNode.removeAction(action.workflowNode.graphicsPanel.findNode(action.getToNodeId()));
					}
				})
			]
		});
		this.actionContextMenu = menu2;
	},


	removeNode : function(node) {
		for (var i = 0; i < this.workflowNodes.length; i++)
		{
			if (this.workflowNodes[i] == node)
			{
				this.workflowNodes.splice(i, 1);
				this.doc.documentElement.removeChild(node.xmlNode);
				for (var i = 0; i < this.workflowNodes.length; i++)
				{
					var o = this.workflowNodes[i];
					for (var j = 0; j < o.workflowActions.length; j++)
					{
						var k = o.workflowActions[j];
						if (k.toNode == node)
						{
							o.removeAction(node);
							break;
						}
					}
				}
				for (var j = 0; j < node.workflowActions.length; j++)
					node.workflowActions[j].destroy();

				node.destroy(true);
				return;
			}
		}
	},

	findNode : function(id){
		for (var i = 0; i < this.workflowNodes.length; i++) {
			if (this.workflowNodes[i].getNodeId() == id) {
				return this.workflowNodes[i];
			}
		}
		return null;
	},

	addNode : function (obj) {
		if (!obj.rendered)
			obj.render(this.body);
		else
			this.body.appendChild(obj.el);
	},

	addAction : function (obj) {
		this.body.appendChild(obj.el);
	},

	loadWorkflow : function(xml) {
		if (xml == "")
			xml = "<workflow/>";

		this.doc = new ActiveXObject("Microsoft.XMLDOM");
		this.doc.loadXML(xml);
		var rootNode = this.doc.documentElement;

		this.startNodeId = rootNode.getAttribute("root");
		var xmlNodes = rootNode.selectNodes("./node");

		this.workflowNodes.length = 0;
		if (xmlNodes != null)
		{
			for (var i = 0; i < xmlNodes.length; i++)
			{
				if (xmlNodes[i].getAttribute("type") == "branchBegin")
					this.workflowNodes[i] = new Ext.workflow.BranchBegin();
				else if (xmlNodes[i].getAttribute("type") == "branchEnd")
					this.workflowNodes[i] = new Ext.workflow.BranchEnd();
				else if (xmlNodes[i].getAttribute("type") == "subflow")
					this.workflowNodes[i] = new Ext.workflow.Subflow();
				else
					this.workflowNodes[i] = new Ext.workflow.NormalNode();
				this.workflowNodes[i].xmlNode = xmlNodes[i];
				this.workflowNodes[i].graphicsPanel = this;
			}
			for (var i = 0; i < this.workflowNodes.length; i++)
				this.workflowNodes[i].parseActions();
			for (var i = 0; i < this.workflowNodes.length; i++)
				this.addNode(this.workflowNodes[i]);
		}
	}
});

Ext.workflow.DD = function(obj, group, config) {
	this.workflowNode = obj;
	obj.el.setStyle('cursor', 'arrow');
	obj.el.setStyle('position', 'absolute');
    Ext.workflow.DD.superclass.constructor.call(this, Ext.id(obj.el.dom), group, config);
};

Ext.extend(Ext.workflow.DD, Ext.dd.DD, {
    onDrag: function(e) {
		this.workflowNode.setNodeX(this.workflowNode.el.getLeft(true));
		this.workflowNode.setNodeY(this.workflowNode.el.getTop(true));
		this.workflowNode.redrawActions();
    },

	startDrag: function() {
		this.workflowNode.el.setStyle('cursor', 'move');
	},
	endDrag: function() {
		this.workflowNode.el.setStyle('cursor', 'arrow');
	}
});

Ext.workflow.ViewPanel = Ext.extend(Ext.TabPanel, {
	graphicsPanel : new Ext.workflow.GraphicsPanel({
			title: '流程图'
		}),

    initComponent : function(){
		this.items = [ this.graphicsPanel, {
				autoScroll : true,
				title: '源代码',
				listeners: {
					activate: function(tab) {
						tab.body.dom.innerText = tab.ownerCt.graphicsPanel.doc.xml;
					}
				}
			}
		];
		this.activeTab = 0;
        Ext.workflow.ViewPanel.superclass.initComponent.call(this);
	},
	loadWorkflow : function(xml) {
		this.graphicsPanel.loadWorkflow(xml);
	}
});

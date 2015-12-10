Ext.form.TreeBox = function(config){
	Ext.form.TreeBox.superclass.constructor.call(this, config);
	this.regexId = config.regexId;
	this.regexText = config.regexText;
}

Ext.form.TreeBox = Ext.extend(Ext.form.TwinTriggerField, {
  regexId : null,
  regexText : null,
  defaultAutoCreate : {tag: "input", type: "text", size: "24", autocomplete: "off"},
  listWidth: undefined,
  hiddenName: undefined,
  listClass: '',
  selectedClass: 'x-combo-selected',
  triggerClass : 'x-form-arrow-trigger',
  shadow:'sides',
	lastSelection: null,
	singleMode: true,
	pathMode:false,
	readOnly:false,
  listAlign: 'tl-bl?',
  maxHeight: 150,
  resizable: true,
  handleHeight : 8,
  minListWidth : 70,
	viewLoader : null,
	trigger1Class:'x-form-clear-trigger',
  trigger2Class:'x-form-trigger',
	hideTriggers:[true,true],

	initComponent : function(){
		Ext.form.ComboBox.superclass.initComponent.call(this);
		this.addEvents({
			'expand' : true,
			'collapse' : true,
			'beforeselect' : true,
			'change' : true,
			'select' : true
		});

		if(this.transform){
			this.allowDomMove = false;
			var s = Ext.getDom(this.transform);
			this.hiddenName = s.name;
			s.name = Ext.id(); // wipe out the name in case somewhere else they have a reference
			if(!this.lazyRender){
				this.target = true;
				this.el = Ext.DomHelper.insertBefore(s, this.autoCreate || this.defaultAutoCreate);
				s.parentNode.removeChild(s); // remove it
				this.render(this.el.parentNode);
			}else{
				s.parentNode.removeChild(s); // remove it
			}
		}
		this.triggerConfig = {
            tag:'span', cls:'x-form-twin-triggers', cn:[
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger1Class},
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger2Class}
        ]};
	},

	// private
    onRender : function(ct, position){
        Ext.form.TreeBox.superclass.onRender.call(this, ct, position);
        if(this.hiddenName){
            this.hiddenField = this.el.insertSibling({tag:'input', type:'hidden', name: this.hiddenName, id:  (this.hiddenId||this.hiddenName)},
                    'before', true);
            this.hiddenField.value =
                this.hiddenValue !== undefined ? this.hiddenValue :
                this.value !== undefined ? this.value : '';
            // prevent input submission
            this.el.dom.removeAttribute('name');
        }
        if(Ext.isGecko){
            this.el.dom.setAttribute('autocomplete', 'off');
        }
        var cls = 'x-combo-list';

        this.list = new Ext.Layer({
            shadow: this.shadow, cls: [cls, this.listClass].join(' '), constrain:false
        });

        var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);
        this.list.setWidth(lw);
        this.list.swallowEvent('mousewheel');
		// this.list.dom.style.overflow = 'auto';
		this.view = new Ext.tree.TreePanel({
			el:this.list,
			animate:false,
			// autoHeight : true,
			autoScroll: true,
			rootVisible: false,
			//loader: this.viewLoader,
			containerScroll: true
			//,tbar:['->','-',{text:'清空',ownerC:this,handler:function(){this.ownerC.setValue("=");}}]
		});
		var root = null;
		if (!this.treeData)
			root = new Ext.tree.AsyncTreeNode({
				text: 'Ext JS',
				id:'_',
				draggable:false
			});
		else
			root = new Ext.tree.TreeNode({
				text: 'Ext JS',
				id:'_',
				draggable:false
			});
		this.view.setRootNode(root);

		if (!this.treeData)
		{
			this.view.loader = this.viewLoader;
			this.view.loader.regexId = this.regexId;
			this.view.loader.regexText = this.regexText;
			this.view.loader.box = this;
			this.view.ownerC = this;
			this.view.loader.on('beforeload', this.beforeload, this);

		} else {
			this.loadTree(root, this.treeData);
			if (!this.singleMode) {
				this.view.on("dblclick", this.dblClick, this);
				this.view.on("checkchange", this.checkChanged, this);
				this.view.on("expand",this.afterExpand,this);
			}
		}

		this.view.getSelectionModel().on("selectionchange", this.selectionChange, this);

		this.view.on('expand', function() { this.view.root.expand(); }, this);
		this.view.on('expandnode', this.recalcSize, this);
		this.view.on('collapsenode', this.recalcSize, this);

		this.view.on('click', this.onItemSelect, this);
		this.renderTree();

		this.el.dom.setAttribute('readOnly', true);
		this.el.on('mousedown', function(){if (!this.readOnly)this.onTriggerClick();},  this);
		this.el.addClass('x-combo-noedit');

		this.resizer = new Ext.Resizable(this.list,  {
                   pinned:true, handles:'se'
                });
		
		this.resizer.on('resize', function(r, w, h){
					this.list.beginUpdate();
					this.list.setHeight(h < 150 ? 150 : h);
					this.view.setHeight(h < 150 ? 150 : h);
					this.list.setWidth(w < 150 ? 150 : w);
					this.view.setWidth((w  < 150 ? 150 : w) - this.list.getFrameWidth('lr'));
					this.list.alignTo(this.wrap, this.listAlign);
					this.list.endUpdate();
                }, this);
		this.list.setHeight(150);
		this.view.setHeight(150);
    },

	loadTree : function(node, data)
	{
		for (var i = 0; i < data.length; i++)
		{
			var attr = data[i];
			if (this.regexId && !this.regexId.test(attr.id))
				continue;
			if (this.regexText && !this.regexText.test(attr.text))
				continue;
			var param = { id: attr.id, text: attr.text };
			if (attr._click && !this.singleMode)
				param.checked = false;
			var newNode = new Ext.tree.TreeNode(param);
			newNode._click = attr._click;
			newNode.alertMsg = attr.alertMsg;
			node.appendChild(newNode);
			attr.obj = newNode;
			if (attr.children)
				this.loadTree(newNode, attr.children);
		}
	},

	selectionChange : function(mode, node) {
		this.lastSelection = node;
		return true;
	},

	renderTree : function() {
		this.view.render();
	},

	getView : function() {
		return this.view;
	},

	dblClick : function(node, e) {
		if (node.ui.checkbox)
			this.checkChanged(node, node.ui.checkbox.checked);
	},

	checkChanged : function(node, checked) {
		if (checked)
			this.addValue(node.getPath(),node.id, node.text);
		else
			this.removeValue(node.getPath(),node.id, node.text);
		
		this.fireEvent("select", this, node);
	},

	addValue : function(path,id, text) {
		var v = this.getValue();
		vals = v.split(",");
		var found = false;
		for (var i = 0; i < vals.length; i++) {
			if (this.pathMode){
				if (vals[i] == path){
					found = true;
					break;
				}
			} else {
				if (vals[i] == id) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			if (v.length > 0)
			{
				v = "";
				for (var i = 0; i < vals.length; i++)
					v += vals[i] + "=" + this._text_cache[i] + ",";
			}
			if (this.pathMode)
				v += path + "=" + text;
			else
				v += id + "=" + text;
			this.setValue(v);
		}

	},

	removeValue : function(path,id, text) {
		var vals = this.getValue();
		if ((!this.pathMode && vals == id) || (this.pathMode && vals == path)) {
			this.setValue("");
			return;
		}
		vals = vals.split(",");
		var v = "";
		for (var i = 0; i < vals.length; i++) {
			if ((!this.pathMode && vals[i] == id) || (this.pathMode && vals[i] == path))
				continue;
			if (v.length > 0)
				v += ",";
			v += vals[i] + "=" + this._text_cache[i];
		}
		this.setValue(v);
	},

	onItemSelect : function(node, e) {
		if (this.singleMode) {

			if (node._click) {
				if (this.pathMode)
					this.setValue(node.getPath() + "=" + node.text);
				else
					this.setValue(node.id + "=" + node.text);
				this.collapse();
			}
		}
		this.fireEvent("select", this, node);
	},

    // private
    initEvents : function(){
        Ext.form.TreeBox.superclass.initEvents.call(this);

        this.keyNav = new Ext.KeyNav(this.el, {
            "down" : function(e){
                if(!this.isExpanded()){
                    this.onTriggerClick();
                }
				else
					this.selectNext();
            },

            "right" : function(e){
				this.expandNode();
            },

            "left" : function(e){
				this.selectParent();
            },

			"up" : function(e){
                if(this.isExpanded()){
					this.selectPrev();
                }
            },

            "enter" : function(e){
                this.selectValue();
            },

			"esc" : function(e){
                this.collapse();
            },

            "tab" : function(e){
                this.collapse();
                return true;
            },

            scope : this,

            forceKeyDown: true
        });

    },
  afterExpand : function(pNode){
    if (!this.singleMode){
			var val = this.getValue();
			if (!val)
				return;
			val = val.split(",");
			for (var i = 0; i < val.length; i++){
				if (val[i].indexOf("/")!=-1) {
					var subVal = val[i].substr(val[i].lastIndexOf("/")+1);
					var cNode = pNode.findChild("id",subVal);
					var path;
					if (this.pathMode)
						path = cNode.getPath();
					else
						path = cNode.id();
					if (path == val[i]){
						if(cNode.ui.checkbox)
							cNode.ui.checkbox.checked = true;
					}
				} else {
					//var nid = cNode.id;
					var cNode = pNode.findChild("id",val[i]);
					//alert(nid+"==="+val[i]);
					if (cNode){
						if(cNode.ui.checkbox)
							cNode.ui.checkbox.checked = true;
					}
				}
			}
		}
  },

	expandNode : function() {
		if (this.lastSelection) {
			if (!this.lastSelection.isExpanded()) {
				this.lastSelection.expand();
				this.expandNode();
			}
			else if (this.lastSelection.childNodes.length > 0) {
				this.lastSelection = this.lastSelection.childNodes[0];
				this.lastSelection.select();
			}
		}
	},

	beforeload : function(ol, node, cb) {
		ol.baseParams.nodePath = node.getPath();
	},

	selectParent : function() {
		if (this.lastSelection) {
			if (this.lastSelection.parentNode && this.lastSelection.parentNode.getDepth() > 0) {
				this.lastSelection = this.lastSelection.parentNode;
				this.lastSelection.select();
			}
		}
	},

	selectValue : function() {
		if (this.lastSelection) {
			if (this.singleMode) {
				this.onItemSelect(this.lastSelection);
			}
			else {
				if (this.lastSelection.ui.checkbox) {
					this.lastSelection.ui.checkbox.checked = !this.lastSelection.ui.checkbox.checked;
					this.checkChanged(this.lastSelection, this.lastSelection.ui.checkbox.checked);
				}
			}
		}
	},

	selectNext : function() {
		if (this.lastSelection == null) {
			this.lastSelection = this.treeData[0]._node_object;
			this.lastSelection.select();
		}
		else {
			if (this.lastSelection.nextSibling) {
				this.lastSelection = this.lastSelection.nextSibling;
				this.lastSelection.select();
			}
			else
				this.expandNode();
		}
	},

	selectPrev : function() {
		if (this.lastSelection) {
			if (this.lastSelection.previousSibling) {
				this.lastSelection = this.lastSelection.previousSibling;
				this.lastSelection.select();
			}
			else
				this.selectParent();
		}
	},

	onDestroy : function(){
        if(this.view){
            this.view.destroy();
        }
        if(this.list){
            this.list.destroy();
        }
        Ext.form.TreeBox.superclass.onDestroy.call(this);
    },
    
    onDisable : function(){
        Ext.form.TreeBox.superclass.onDisable.call(this);
				this.triggers[0].hide();
    },
    // private
    onEnable : function(){
        Ext.form.TreeBox.superclass.onEnable.call(this);
        this.triggers[0].show();
    },


    /**
     * Returns the currently selected field value or empty string if no value is set.
     * @return {String} value The selected value
     */
    getValue : function(){
        if(this.hiddenField) {
            return this.hiddenField.value;
        }
        return typeof this.value != 'undefined' ? this.value : '';
    },

    /**
     * Clears any text/value currently set in the field
     */
    clearValue : function(){
        if(this.hiddenField){
            this.hiddenField.value = '';
        }
        this.setRawValue('');
        this.lastSelectionText = '';
        this.applyEmptyText();
    },

	findRecord : function(id) {
		var i = id.indexOf("=");
		if (i == -1)
			return id;
		return id.substr(i + 1);
	},

	findValue : function(val, data, sel)
	{
		for (var i = 0; i < data.length; i++)
		{
			var attr = data[i];
			if (attr.id == val)
			{
				if (sel) {
					this.view.getSelectionModel().select(attr.obj);
				}
				return attr.text;
			}
			if (attr.children)
			{
				var s = this.findValue(val, attr.children, sel);
				if (s != null)
					return s;
			}
		}
		return null;
	},

  setValue : function(v) {
  	v = "" + v;
    var text = v;
		if (this.singleMode)
		{
			var pos = text.indexOf("=");
			if (pos != -1) {
				v = text.substr(0, pos);
				text = text.substr(pos + 1);
			} else if (this.treeData) {
				var s = this.findValue(text, this.treeData, true);
				if (s != null)
					text = s;
			}
//			if (!this.pathMode) {
//				v = v.substr(v.lastIndexOf("/")+1);
//			}
		}
		else
		{
			var vals = v.split(",");
			text = "";
			var i;
			this._text_cache = [];
			v = "";
			for (var i = 0; i < vals.length; i++)
			{
				if (i > 0) {
					text += ",";
					v += ",";
				}
				var pos = vals[i].indexOf("=");
				if (pos == -1) {
					if (!this.pathMode){
						vals[i] = vals[i].substr(vals[i].lastIndexOf("/")+1);
					}
					this._text_cache[i] = vals[i];
					v += vals[i];
				} else {
					var vStr = vals[i].substr(0, pos);
					if (!this.pathMode){
						vStr = vStr.substr(vStr.lastIndexOf("/")+1);
					}
					v += vStr;
					this._text_cache[i] = vals[i].substr(pos + 1);
				}
				text += this._text_cache[i];
			}
		}
    if(this.hiddenField){
       this.hiddenField.value = v;
    }
		else
			this.hiddenValue = v;
    Ext.form.TreeBox.superclass.setValue.call(this, text);
    this.value = v;
		//this.fireEvent("select", this, this.value);挪至onItemSelect方法里
		if (!this.disabled) {
			if (this.value == "")
				this.triggers[0].hide();
			else if (!this.readOnly) {
				this.triggers[0].show();
			}
		}
  },

    // private
    onEmptyResults : function(){
        this.collapse();
    },

    /**
     * Returns true if the dropdown list is expanded, else false.
     */
    isExpanded : function(){
        return this.list.isVisible();
    },

    // private
    validateBlur : function(){
        return !this.list || !this.list.isVisible();
    },

    /**
     * Hides the dropdown list if it is currently expanded. Fires the 'collapse' event on completion.
     */
    collapse : function(){
        if(!this.isExpanded()){
            return;
        }
        this.list.hide();
        Ext.get(document).un('mousedown', this.collapseIf, this);
        Ext.get(document).un('mousewheel', this.collapseIf, this);
        this.fireEvent('collapse', this);
    },

    // private
    collapseIf : function(e){
        if(!e.within(this.wrap) && !e.within(this.list)){
            this.collapse();
        }
    },

	recalcSize : function(pNode) {
		//var inner = this.view.getTreeEl().dom;
		//var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);
		//this.list.setWidth(lw);
		//var h = Math.max(inner.clientHeight, inner.offsetHeight, inner.scrollHeight);
		//this.list.beginUpdate();
		//this.list.setHeight(h < this.maxHeight ? 'auto' : this.maxHeight);
		//this.list.setHeight(this.list.getHeight()+this.list.getFrameWidth('tb'));
		//this.list.alignTo(this.wrap, this.listAlign);
		//this.list.endUpdate();
	},

    /**
     * Expands the dropdown list if it is currently hidden. Fires the 'expand' event on completion.
     */
    expand : function(){
      if(this.isExpanded() || !this.hasFocus){
          return;
      }
      this.list.alignTo(this.wrap, this.listAlign);
			this.recalcSize();
      this.list.show();
      Ext.get(document).on('mousedown', this.collapseIf, this);
      Ext.get(document).on('mousewheel', this.collapseIf, this);
      this.fireEvent('expand', this);
    },

    // private
    // Implements the default empty TriggerField.onTriggerClick function
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }
        if(this.isExpanded()){
            this.collapse();
            this.el.focus();
        }else {
            this.hasFocus = true;
            this.expand();
            this.el.focus();
        }
    },
    onTrigger1Click : function(){
    	this.setValue("");
    },

    onTrigger2Click : function(){
       this.onTriggerClick();
    }
});
Ext.reg('treebox', Ext.form.TreeBox);


Ext.tree.FilterTreeLoader = function(config){
	Ext.tree.FilterTreeLoader.superclass.constructor.call(this, config);
}

Ext.extend(Ext.tree.FilterTreeLoader, Ext.tree.TreeLoader, {
    createNode : function(attr){
      if (attr._click && this.regexId && !this.regexId.test(attr.id))
          return null;
      if (attr._click && this.regexText && !this.regexText.test(attr.text))
          return null;
			if (this.box && this.box.singleMode == false) {
				if (attr._click) {
					attr.checked = false;
				}
			}
			var ret = Ext.tree.FilterTreeLoader.superclass.createNode.call(this, attr);
			ret._click = attr._click;
			ret.alertMsg = attr.alertMsg;
			if (attr && attr.otherParams && (attr.otherParams instanceof Object)) {
				for (var attrop in attr.otherParams) {
					ret[attrop] = attr.otherParams[attrop];
				}
			}
			if (this.box && this.box.singleMode == false)
			{
				ret.on("checkchange", this.box.checkChanged, this.box);
				ret.on("dblclick", this.box.dblClick, this.box);
				ret.on("expand",this.box.afterExpand,this.box);
			}
			return ret;
    }
});
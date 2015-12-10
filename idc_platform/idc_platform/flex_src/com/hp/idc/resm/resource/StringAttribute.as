package com.hp.idc.resm.resource
{
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.components.TreeItem;
	import com.hp.idc.components.TreeItemRenderer;
	import com.hp.idc.resm.model.Code;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.model.StringAttributeDefine;
	
	import mx.collections.ArrayCollection;
	import mx.core.ClassFactory;
	import mx.core.UIComponent;
	
	import spark.components.Button;
	import spark.components.DropDownList;
	import spark.components.FormItem;
	import spark.components.Group;
	import spark.components.Label;
	import spark.components.TextArea;
	import spark.components.TextInput;
	import spark.layouts.HorizontalLayout;
	import spark.skins.spark.StackedFormItemSkin;
	
	[RemoteClass(alias="com.hp.idc.resm.resource.StringAttribute")]
	[Bindable]
	public class StringAttribute implements AttributeBase
	{				
		//在这里, text和value取的还是一个值. 但是对于select和tree型结构, 需要分开取值. 
		//暂未做, 预留下来
		public var text:String;
		public var value:String;
		public var attribute:ModelAttribute;
		
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var uicomponent:UIComponent;
		[Transient]
		private var code_type:int;
		[Transient]
		private var bigTag:Boolean = false;
		
		
		public function createField():void
		{
			
			var sad:StringAttributeDefine = attribute.define as StringAttributeDefine;
			this.code_type = sad.codeType;
			
			switch(code_type){
				case StringAttributeDefine.TEXTINPUT_TYPE:
					var input:TextInput = new TextInput();
					input.percentWidth = 100;
					this.uicomponent = input;
					this.formItem = new FormItem();
					formItem.height = 22;
					formItem.layout = new HorizontalLayout();
					formItem.label = attribute.name;
					formItem.addElement(input);
					formItem.required = !(attribute.nullable);
					
					input.text = this.text;
					input.restrict = "^\~\!\@\#\$\%\^\*\(\)\+\=\:\;\,\(\)\?\/\>\&#38;\&#60;\&#92;\`\|\&#91;\&#93;\&#123;\&#125;";
					
					this.uicomponent.toolTip = sad.remark;
					if(sad.unitName!=null){
						var helpLabel:Label = new Label();
						helpLabel.verticalCenter = 0;
						helpLabel.text = sad.unitName;
						var g:Group = new Group();
						g.addElement(helpLabel);
						g.percentHeight = 100;
						formItem.addElement(g);
					}
					bigTag = false;
					break;
				case StringAttributeDefine.SELECT_TYPE:
					var combo:DropDownList = new DropDownList();
					combo.dataProvider = sad.codes;
					combo.labelField = "name";
					combo.percentWidth = 100;
					this.uicomponent = combo;
					this.formItem = new FormItem();
					formItem.height = 22;
					formItem.label = attribute.name;
					formItem.addElement(combo);
					formItem.required = !(attribute.nullable);
					
					combo.toolTip = sad.remark;
					
					if(combo.dataProvider != null) {
						for(var i:int=0;i<combo.dataProvider.length;i++){
							if(combo.dataProvider.getItemAt(i).name == this.value){
								combo.selectedIndex = i;
							}
						}
					}
					
					bigTag = false;
				break
				case StringAttributeDefine.TEXTAREA_TYPE:
					var area:TextArea = new TextArea();
					area.height = 80;
					area.percentWidth = 100;
					this.uicomponent = area;
					this.formItem = new FormItem();
					formItem.setStyle("skinClass",StackedFormItemSkin);
					formItem.label = attribute.name;
					formItem.addElement(area);
					formItem.required = !(attribute.nullable);
					
					area.toolTip = sad.remark;
					area.text = this.text;
					
					bigTag = true;
					break;
				case StringAttributeDefine.TREE_TYPE:
					var tree:DropDownList = new DropDownList();
					tree.percentWidth = 100;
					tree.itemRenderer = new ClassFactory(TreeItemRenderer);
					tree.dataProvider = TreeItem.createDataProviderFromXML(createXmlProvider(sad.codes));
					
					this.uicomponent = tree;
					this.formItem = new FormItem();
					formItem.height = 22;
					formItem.label = attribute.name;
					formItem.addElement(tree);
					formItem.required = !(attribute.nullable);
					
					tree.toolTip = sad.remark;
					if(tree.dataProvider!=null){
						var treeDP:ArrayCollection = tree.dataProvider as ArrayCollection;
						for(var j:int=0;j<treeDP.length;j++){
							if(treeDP.getItemAt(j).id==this.value){
								tree.selectedItem = treeDP.getItemAt(j);
							}
						}
					}
					
					break;
			}
		}
		
		//根据codes构造tree，依赖后台codes的结构
		private function createXmlProvider(codes:ArrayCollection):XML{
			var xml:XML = <treeRoot></treeRoot>;
			for(var i:int=0;i<codes.length;i++){
				var code:Code = codes.getItemAt(i) as Code;
				xml.appendChild(createChildNode(code));
			}
			return xml;
		}
		
		private function createChildNode(code:Code):XML{
			var node:XML = <node id={code.name} label={code.name}></node>;
			if(code.childs!=null&&code.childs.length>0){
				for(var i:int=0;i<code.childs.length;i++){
					var child:Code = code.childs.getItemAt(i) as Code;
					node.appendChild(createChildNode(child));
				}
			}
			return node;
		}
		
		
		public function setValue(o:Object):void
		{
			if(o == null){
				return;
			}
			switch(code_type){
				case StringAttributeDefine.TEXTINPUT_TYPE:
					var input:TextInput = this.uicomponent as TextInput;
					input.text = o.toString();
					break;
				case StringAttributeDefine.SELECT_TYPE:
					var select:DropDownList = this.uicomponent as DropDownList;
					if(select.dataProvider!=null){
						var selectDP:ArrayCollection = select.dataProvider as ArrayCollection;
						for(var i:int=0;i<selectDP.length;i++){
							if(selectDP.getItemAt(i).name==o){
								select.selectedItem = selectDP.getItemAt(i);
							}
						}
					}
					break;
				case StringAttributeDefine.TEXTAREA_TYPE:
					var area:TextArea = this.uicomponent as TextArea;
					area.text = o.toString();
					break;
				case StringAttributeDefine.TREE_TYPE:
					var tree:DropDownList = this.uicomponent as DropDownList;
					if(tree.dataProvider!=null){
						var treeDP:ArrayCollection = tree.dataProvider as ArrayCollection;
						for(var j:int=0;j<treeDP.length;j++){
							if(treeDP.getItemAt(j).id==o){
								tree.selectedItem = treeDP.getItemAt(j);
							}
						}
					}
					break;
			}
		}
		
		public function refreshValue():void
		{
			switch(code_type){
				case StringAttributeDefine.TEXTINPUT_TYPE:
					var input:TextInput = this.uicomponent as TextInput;
					value = input.text;
					text = input.text;
					break;
				case StringAttributeDefine.SELECT_TYPE:
					var select:DropDownList = this.uicomponent as DropDownList;
					if(select.selectedItem!=null){
						value = select.selectedItem.name as String;
						text = select.selectedItem.name as String;
					}
					break;
				case StringAttributeDefine.TEXTAREA_TYPE:
					var area:TextArea = this.uicomponent as TextArea;
					value = area.text;
					text = area.text;
					break;
				case StringAttributeDefine.TREE_TYPE:
					var tree:DropDownList = this.uicomponent as DropDownList;
					if(tree.selectedItem!=null){
						value = tree.selectedItem.id as String;
						text = tree.selectedItem.name as String;
					}
					break;
			};
		}
		
		public function getFormItem():FormItem
		{
			return this.formItem;
		}
		
		public function setAttribute(modelAttribute:ModelAttribute):void{
			this.attribute = modelAttribute;
		}
		
		public function isBigComponent():Boolean{
			return bigTag;
		}
		
		public function getValue():String{
			return value;
		}
		
		public function getAttribute():ModelAttribute{
			return attribute;
		}
		
		public function setEdit(edit:Boolean=true):void{
			if(this.uicomponent == null)
				return;
			switch(code_type){
				case StringAttributeDefine.TEXTINPUT_TYPE:
					var input:TextInput = this.uicomponent as TextInput;
					input.editable = edit;
					break;
				case StringAttributeDefine.SELECT_TYPE:
					var select:DropDownList = this.uicomponent as DropDownList;
					select.enabled = edit;
					break;
				case StringAttributeDefine.TEXTAREA_TYPE:
					var area:TextArea = this.uicomponent as TextArea;
					area.editable = edit;
					break;
				case StringAttributeDefine.TREE_TYPE:
					var tree:DropDownList = this.uicomponent as DropDownList;
					tree.enabled = edit;
					break;
			};
		}
		
		public function setDefaultValue():void{
			if(this.uicomponent == null)
				return;
			if(this.attribute == null)
				return;
			this.setValue(this.attribute.defaultValue);
		}
	}
}
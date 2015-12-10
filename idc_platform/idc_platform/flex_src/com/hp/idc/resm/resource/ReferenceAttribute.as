package com.hp.idc.resm.resource
{
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.model.ReferenceAttributeDefine;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.UIComponent;
	
	import spark.components.ComboBox;
	import spark.components.FormItem;
	import spark.components.TextInput;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.ReferenceAttribute")]
	public class ReferenceAttribute implements AttributeBase
	{
		public var text:String;
		public var value:int;
		public var attribute:ModelAttribute;
		
		[Transient]
		public var formItem:FormItem;
		[Transient]
		public var uicomponent:UIComponent;
		
		public function isBigComponent():Boolean{
			return false;
		}
		
		public function getValue():String{
			return value.toString();
		}
		
		public function getAttribute():ModelAttribute{
			return attribute;
		}
		
		public function createField():void
		{
			var sad:ReferenceAttributeDefine = attribute.define as ReferenceAttributeDefine;
			var combo:ComboBox = new ComboBox();
			combo.dataProvider = sad.res;
			combo.labelField = "name";
			combo.percentWidth = 100;
			this.uicomponent = combo;
			this.formItem = new FormItem();
			formItem.height = 22;
			formItem.label = attribute.name;
			formItem.addElement(combo);
			formItem.required = !(attribute.nullable);
			
			combo.toolTip = attribute.remark;
			
			if(combo.dataProvider != null) {
				for(var i:int=0;i<combo.dataProvider.length;i++){
					if(combo.dataProvider.getItemAt(i).id == this.value){
						combo.selectedIndex = i;
						break;
					}
				}
			}									
		}
		
		public function setValue(o:Object):void
		{
			if(o==null)
				return;
			var select:ComboBox = this.uicomponent as ComboBox;
			if(select.dataProvider!=null){
				var selectDP:ArrayCollection = select.dataProvider as ArrayCollection;
				for(var i:int=0;i<selectDP.length;i++){
					if(selectDP.getItemAt(i).id==o){
						select.selectedItem = selectDP.getItemAt(i);
						break;
					}
				}
			}			
		}
		
		public function refreshValue():void
		{			
			var select:ComboBox = this.uicomponent as ComboBox;
			if(select.selectedItem!=null){
				value = select.selectedItem.id as int;
				text = select.selectedItem.id as String;
			}
		}
		
		public function getFormItem():FormItem
		{
			return this.formItem;
		}
		
		public function setAttribute(modelAttribute:ModelAttribute):void
		{
			this.attribute = modelAttribute;
		}
		
		public function setEdit(edit:Boolean=true):void{
			if(this.uicomponent == null)
				return;
			(this.uicomponent as ComboBox).enabled = edit;
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
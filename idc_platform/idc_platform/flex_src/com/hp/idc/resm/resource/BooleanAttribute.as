package com.hp.idc.resm.resource
{
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.resm.model.BooleanAttributeDefine;
	import com.hp.idc.resm.model.ModelAttribute;
	
	import mx.core.UIComponent;
	
	import spark.components.CheckBox;
	import spark.components.FormItem;
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.BooleanAttribute")]
	public class BooleanAttribute implements AttributeBase
	{
		public var text:String;
		public var value:Boolean;
		public var attribute:ModelAttribute;
		
		
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var uicomponent:UIComponent;
		
		public function createField():void
		{
			var bad:BooleanAttributeDefine = attribute.define as BooleanAttributeDefine;
			
			var checkbox:CheckBox = new CheckBox();
			checkbox.label = attribute.name;
			this.uicomponent = checkbox;
			this.formItem = new FormItem();
			formItem.height = 22;
			formItem.addElement(checkbox);
			formItem.required = !(attribute.nullable);
			
			checkbox.toolTip = bad.remark;
			checkbox.selected = this.value;
		}
		
		public function setValue(o:Object):void
		{
			if(o==null)
				return;
			var checkbox:CheckBox = this.uicomponent as CheckBox;
			checkbox.selected = o;
		}
		
		public function refreshValue():void
		{
			var checkbox:CheckBox = this.uicomponent as CheckBox;
			this.value = checkbox.selected;
			this.text = String(this.value.toString());
		}
		
		public function getFormItem():FormItem
		{
			return this.formItem;
		}
		
		public function setAttribute(modelAttribute:ModelAttribute):void{
			this.attribute = modelAttribute;
		}
		
		public function isBigComponent():Boolean{
			return false;
		}
		
		public function getValue():String{
			return this.value.toString();
		}
		
		public function getAttribute():ModelAttribute{
			return attribute;
		}
		
		public function setEdit(edit:Boolean=true):void{
			if(this.uicomponent == null)
				return;
			(this.uicomponent as CheckBox).enabled = edit;
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
package com.hp.idc.resm.resource
{
	import com.hp.idc.resm.model.ModelAttribute;
	
	import mx.controls.Text;
	import mx.core.UIComponent;
	
	import spark.components.FormItem;
	import spark.components.TextInput;
	import spark.layouts.HorizontalLayout;
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.NumberAttribute")]
	public class NumberAttribute implements AttributeBase
	{
		public var text:String;
		public var value:Number;
		public var attribute:ModelAttribute;
		
		
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var uicomponent:UIComponent;
		
		public function createField():void
		{
			var input:TextInput = new TextInput();
			input.percentWidth = 100;
			this.uicomponent = input;
			this.formItem = new FormItem();
			this.formItem.layout = new HorizontalLayout();
			formItem.height = 22;
			formItem.label = attribute.name;
			formItem.addElement(input);
			var t:Text = new Text();
			t.text = attribute.define.unitName;
			if(t.text.length > 0)
				formItem.addElement(t);
			formItem.required = !(attribute.nullable);
			input.toolTip = attribute.remark;
			input.text = this.text;
			input.restrict = "0-9\.";
		}
		
		public function setValue(o:Object):void
		{
			if(o==null)
				return;
			var input:TextInput = this.uicomponent as TextInput;
			input.text = o.toString();
			
		}
		
		public function refreshValue():void
		{
			var input:TextInput = this.uicomponent as TextInput;
			this.value = new Number(input.text);
			this.text = input.text;
		}
		
		public function getFormItem():FormItem
		{
			return this.formItem;
		}
		
		public function setAttribute(modelAttribute:ModelAttribute):void
		{
			this.attribute = modelAttribute;
		}
		
		public function isBigComponent():Boolean{
			return false;
		}
		
		public function getValue():String{
			return value.toString();
		}
		
		public function getAttribute():ModelAttribute{
			return attribute;
		}
		
		public function setEdit(edit:Boolean=true):void{
			if(this.uicomponent == null)
				return;
			(this.uicomponent as TextInput).enabled = edit;
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
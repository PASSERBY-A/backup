package com.hp.idc.resm.resource.model
{
	import mx.containers.FormItem;
	import mx.controls.Alert;
	
	import spark.components.TextInput;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.resource.AttributeBase;
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.StringAttribute")]
	public class StringAttributeInput implements AttributeBase
	{
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var input:TextInput;
		
		public var value:String;
		public var attribute:ModelAttribute;
		
		public function createField():FormItem{
			this.input = new TextInput();
			input.width = 250;
			this.formItem = new FormItem();
			formItem.label = attribute.name;
			formItem.setStyle("labelWidth","80");
			formItem.addElement(input);
			return formItem;
		}
		
		public function setValue():void{
			this.input.text = value;
		}
		
		public function  refreshValue():void{
			this.value = input.text;
		}
		
		public function getFormItem():FormItem{
			return this.formItem;
		}
		
	}
}
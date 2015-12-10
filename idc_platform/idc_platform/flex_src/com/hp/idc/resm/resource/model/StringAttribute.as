package com.hp.idc.resm.resource.model
{
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.model.StringAttributeDefine;
	import com.hp.idc.resm.resource.AttributeBase;
	
	import spark.components.FormItem;
	import spark.components.TextInput;
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.StringAttribute")]
	public class StringAttribute implements AttributeBase
	{
		public var value:String;
		public var attribute:ModelAttribute;
		
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var uicomponent:UIComponent;
		[Transient]
		private var code_type:int;
		
		
		public function createField():FormItem
		{
			
			var sad:StringAttributeDefine = attribute.define as StringAttributeDefine;
			this.code_type = sad.codeType;
			if(this.code_type == StringAttributeDefine.CODETYPE_1){
				
				var input:TextInput = new TextInput();
				this.uicomponent = input;
				this.formItem = new FormItem();
				formItem.label = attribute.name;
				formItem.setStyle("labelWidth","80");
				formItem.addElement(input);
				return formItem;
			}
		}
		
		public function setValue():void
		{
			this.area.text = value;
		}
		
		public function refreshValue():void
		{
			value = this.area.text;
		}
		
		public function getFormItem():FormItem
		{
			return this.formItem;
		}
		
	}
}
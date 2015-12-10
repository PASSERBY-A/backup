package com.hp.idc.resm.resource.model
{
	import mx.containers.FormItem;
	
	import spark.components.TextArea;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.resource.AttributeBase;
	
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.StringAttribute")]
	public class StringAttributeArea implements AttributeBase
	{
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var area:TextArea;
		
		public var value:String;
		public var attribute:ModelAttribute;
		
		
		public function createField():FormItem
		{
			this.area = new TextArea();
			area.height = 100;
			area.width = 770;
			this.formItem = new FormItem();
			formItem.label = attribute.name;
			formItem.setStyle("labelWidth","80");
			formItem.addElement(area);
			return formItem;
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
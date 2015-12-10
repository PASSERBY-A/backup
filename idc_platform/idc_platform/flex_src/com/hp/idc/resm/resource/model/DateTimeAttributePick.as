package com.hp.idc.resm.resource.model
{
	import com.hp.idc.components.widgets.DateTimeField;
	
	import mx.containers.FormItem;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.resource.AttributeBase;
	
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.DateTimeAttribute")]
	public class DateTimeAttributePick implements AttributeBase
	{
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var datetimeField:DateTimeField;
		
		public var value:Date;
		public var attribute:ModelAttribute;
		
		public function createField():FormItem
		{
			datetimeField = new DateTimeField();
			datetimeField.fieldwidth = 240;
			this.formItem = new FormItem();
			formItem.label = attribute.name;
			formItem.setStyle("labelWidth","80");
			formItem.addElement(datetimeField);
			return formItem;
		}
		
		public function setValue():void
		{
			this.datetimeField.setDateTime(value);
		}
		
		public function refreshValue():void
		{
			value = this.datetimeField.getDateTime();
		}
		
		public function getFormItem():FormItem
		{
			return this.formItem;
		}
	}
}
package com.hp.idc.resm.resource.model
{
	import mx.containers.FormItem;
	import mx.controls.DateField;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.resource.AttributeBase;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.DateAttribute")]
	public class DateAttributePick implements AttributeBase
	{
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var dateField:DateField;
		
		public var value:Date;
		public var attribute:ModelAttribute;
		
		public function createField():FormItem{
			dateField = new DateField();
			dateField.width = 250;
			this.formItem = new FormItem();
			formItem.setStyle("labelWidth","80");
			formItem.label = attribute.name;
			formItem.addElement(dateField);
			return formItem;
		}
		
		public function setValue():void{
			this.dateField.selectedDate = value;
		}
		
		public function refreshValue():void{
			value = this.dateField.selectedDate;
		}
		
		public function getFormItem():FormItem{
			return this.formItem;
		}
	}
}
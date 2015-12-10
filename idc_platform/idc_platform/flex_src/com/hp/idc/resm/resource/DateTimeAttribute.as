package com.hp.idc.resm.resource
{
	import com.georg.DateTimeField;
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.util.DateTimeUtil;
	
	import mx.controls.DateField;
	import mx.formatters.DateFormatter;
	
	import spark.components.FormItem;
	
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.DateTimeAttribute")]
	public class DateTimeAttribute extends DateAttribute
	{
		override public function createField():void{
			var dateTimeField:DateTimeField = new DateTimeField();
			dateTimeField.formatString = DateTimeUtil.CHINESE_DATE_FORMAT;
			
			dateTimeField.percentWidth = 100;
			this.uicomponent = dateTimeField;
			
			this.formItem = new FormItem();
			formItem.height = 22;
			formItem.label = attribute.name;
			formItem.addElement(dateTimeField);
			formItem.required = !(attribute.nullable);
			
			dateTimeField.selectedDate = DateTimeUtil.stringToDateTime(this.text);
		}
		
		override public function setValue(o:Object):void{
			if(o==null)
				return;
			var dateTimeField:DateTimeField = this.uicomponent as DateTimeField;
			dateTimeField.data = DateTimeUtil.stringToDateTime(o.toString()); 
		}
		
		override public function refreshValue():void{
			var dateTimeField:DateTimeField = this.uicomponent as DateTimeField;
			this.text = DateTimeUtil.formatFullDateTime(dateTimeField.selectedDate);
		}
		
		override public function getFormItem():FormItem{
			return this.formItem;
		}
		
		override public function getValue():String{
			var format:DateFormatter = new DateFormatter();
			format.formatString = "YYYY-MM-DD JJ:NN:SS";
			var d:Date = new Date();
			d.setTime(value);
			return format.format(d);
		}
		
	}
}
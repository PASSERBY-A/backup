package com.hp.idc.resm.resource
{
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.util.DateTimeUtil;
	
	import mx.controls.Alert;
	import mx.controls.DateField;
	import mx.core.UIComponent;
	import mx.formatters.DateFormatter;
	
	import spark.components.FormItem;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.DateAttribute")]
	public class DateAttribute implements AttributeBase
	{
		[Transient]
		public var formItem:FormItem;
		[Transient]
		public var uicomponent:UIComponent;
		
		public var text:String;
		public var value:Number;
		public var attribute:ModelAttribute;
		
		public function createField():void{
			var dateField:DateField = new DateField();
			dateField.percentWidth = 100;
			dateField.formatString = "YYYY-MM-DD";
			dateField.monthNames = ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"];
			this.uicomponent = dateField;
			
			this.formItem = new FormItem();
			formItem.height = 22;
			formItem.label = attribute.name;
			formItem.addElement(dateField);
			formItem.required = !(attribute.nullable);
			
			dateField.toolTip = attribute.remark;
			dateField.selectedDate = DateTimeUtil.stringToDate(this.text);
		}
		
		public function setValue(o:Object):void{
			if(o==null)
				return;
			
			var dateField:DateField = this.uicomponent as DateField;
			dateField.selectedDate = DateField.stringToDate(o.toString(),"YYYY-MM-DD");
		}
		
		public function refreshValue():void{
			var dateField:DateField = this.uicomponent as DateField;
			var d:Date = dateField.selectedDate;
			if(d==null){
				value = -1;
			}else{
				value = d.getTime();
			}
			this.text = DateField.dateToString(d,"YYYY-MM-DD");
		}
		
		public function getFormItem():FormItem{
			return this.formItem;
		}
		
		public function setAttribute(modelAttribute:ModelAttribute):void{
			this.attribute = modelAttribute;
		}
		
		public function isBigComponent():Boolean{
			return false;
		}
		
		public function getValue():String{
			var format:DateFormatter = new DateFormatter();
			format.formatString = "YYYY-MM-DD";
			var d:Date = new Date();
			d.setTime(value);
			return format.format(d);
		}
		
		public function getAttribute():ModelAttribute{
			return attribute;
		}
		
		public function setEdit(edit:Boolean=true):void{
			if(this.uicomponent == null)
				return;
			(this.uicomponent as DateField).enabled = edit;
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
package com.hp.idc.resm.resource
{
	import com.hp.idc.components.widgets.TimeField;
	import com.hp.idc.util.DateTimeUtil;
	
	import mx.formatters.DateFormatter;
	
	import spark.components.FormItem;
	import spark.components.HGroup;
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.TimeAttribute")]
	public class TimeAttribute extends DateAttribute
	{
		
		override public function createField():void{
			var timeField:TimeField = new TimeField();
			this.uicomponent = timeField;
			this.formItem = new FormItem();
			formItem.percentWidth = 100;
			formItem.height = 22;
			formItem.label = attribute.name;
			
			var h:HGroup = new HGroup();
			h.percentWidth = 100;
			h.percentHeight = 100;
			h.addElement(timeField);
			formItem.addElement(h);
			
		}
		
		override public function setValue(o:Object):void{
			var timeField:TimeField = this.uicomponent as TimeField;
			if(o==null||o.toString()==""){
				return;
			}
			timeField.time = DateTimeUtil.stringToTime(o.toString(),"JJ:NN:SS");
		}
		
		override public function refreshValue():void{
			var timeField:TimeField = this.uicomponent as TimeField;
			value = timeField.time;
			this.text = String(this.value.toString());
		}
		
		override public function getFormItem():FormItem{
			return this.formItem;
		}
		
		override public function getValue():String{
			var format:DateFormatter = new DateFormatter();
			format.formatString = "J:NN:SS";
			var d:Date = new Date(2000,12,21,0,0,0);
			d.setTime(d.time+value);
			return format.format(d);
		}
	}
}
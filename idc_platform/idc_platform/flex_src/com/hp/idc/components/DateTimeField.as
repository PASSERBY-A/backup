package com.hp.idc.components
{
	import mx.controls.Alert;
	import mx.controls.DateField;
	
	import spark.components.HGroup;
	
	public class DateTimeField extends HGroup
	{
		private var dateField:DateField;
		private var timeField:TimeField;
		public function DateTimeField()
		{
			super();
			this.paddingBottom = 0;
			this.paddingLeft = 0;
			this.paddingRight =0;
			this.paddingTop =0;
			
			this.verticalAlign = "middle";
			
			this.dateField = new DateField;
			this.dateField.formatString = "YYYY-MM-DD";
			this.timeField = new TimeField();
			
			this.addElement(dateField);
			this.addElement(timeField);
		}
		
		public function setDateTime(date:Date):void{
			if(date==null){
				return;
			}
			dateField.data = date;
			timeField.Time = date;
		}
		
		public function getDateTime():Date{
			var d:Date = this.dateField.selectedDate
			d.setHours(timeField.hours);
			d.setMinutes(timeField.minutes);
			d.setSeconds(timeField.seconds);
			return d;
		}
		
		public function set fieldwidth(width:Number):void{
			this.dateField.width = width-70;
		}
	}
}
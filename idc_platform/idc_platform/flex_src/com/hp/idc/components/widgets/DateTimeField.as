package com.hp.idc.components.widgets
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
//			timeField.Time = date;
		}
		
		public function getDateTime():Date{
			var d:Date = this.dateField.selectedDate
			if(d == null){
				return null;
			}
			d.setHours(timeField.hours);
			d.setMinutes(timeField.minutes);
			d.setSeconds(timeField.seconds);
			return d;
		}
		
		/**
		 * 设置时间日期控件值
		 */
		public function set time(value:Number):void{
			if(value<0){
				return;
			}
			var d:Date = new Date();
			d.setTime(value);
			//年月日域值
			this.dateField.selectedDate = d;
			
			//时间域值
			var timeFieldValue:Number = d.getHours()*3600*1000+d.getMinutes()*60*1000;
			this.timeField.time = timeFieldValue;
		}
		
		public function get time():Number{
			if(this.dateField.selectedDate==null){
				return -1;
			}
			var d:Date = this.dateField.selectedDate;
			return d.getTime() + this.timeField.time;
		}
		
		public function set fieldwidth(width:Number):void{
			this.dateField.width = width-70;
		}
		
		override public function set percentWidth(value:Number):void
		{
			super.percentWidth = value;
			this.dateField.percentWidth = value;
		}
		
		
	}
}
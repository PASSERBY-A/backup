package com.hp.idc.resm.event
{
	import flash.events.Event;
	
	public class EditTableEvent extends Event
	{
		public static const ROWADD:String = "rowadd";
		
		public static const ROWUPDATE:String = "rowupdate";
		
		public var row:Object;
		//用于保存老的修改记录，如果修改成功后，需要先删除原先的记录，再添加新纪录
		public var oldRow:Object;
		
		
		public function EditTableEvent(type:String, row:Object,bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.row = row;
		}
		
		override public function clone():Event{
			var e:EditTableEvent = new EditTableEvent(ROWADD,row);
			return e;
		}
	}
}
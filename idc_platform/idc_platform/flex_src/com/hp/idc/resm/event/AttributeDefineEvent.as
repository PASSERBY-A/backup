package com.hp.idc.resm.event
{
	import flash.events.Event;
	
	public class AttributeDefineEvent extends Event
	{
		public static const ATTRIBUTEREFRESH:String = "attributerefresh";
		
		public function AttributeDefineEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type,bubbles,cancelable);
		}
	}
}
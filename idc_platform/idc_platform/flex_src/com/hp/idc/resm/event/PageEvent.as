package com.hp.idc.resm.event
{
	import com.hp.idc.resm.ui.PageInfo;
	import com.hp.idc.resm.ui.PageQueryInfo;
	
	import flash.events.Event;
	
	public class PageEvent extends Event
	{
		public static const PAGECHANGE:String = "pagechange";
		
		public static const STATECHANGE:String = "statechange";
		
		public var pageQueryInfo:PageQueryInfo;
		
		public var pageInfo:PageInfo;
		
		public function PageEvent(type:String,query:PageQueryInfo=null,info:PageInfo=null,bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.pageQueryInfo = query;
			this.pageInfo = info;
		}
	}
}
package com.hp.idc.resm.event
{
	import com.hp.idc.resm.model.ModelAttribute;
	
	import flash.events.Event;
	
	public class ModelAttributeEvent extends Event
	{
		
		public static const MODELATTRIBUTEADD:String = "modelattributeadd";
		
		public static const MODELATTRIBUTEUPDATE:String = "modelattributeupdate";
		
		public var modelAttribute:ModelAttribute;
		
		public function ModelAttributeEvent(type:String, modelAttribute:ModelAttribute,bubbles:Boolean=false, cancelable:Boolean=false){
			super(type,bubbles,cancelable);
			this.modelAttribute = modelAttribute;
		}
		
		override public function clone():Event{
			var e:ModelAttributeEvent = new ModelAttributeEvent(MODELATTRIBUTEADD,modelAttribute,true);
			return e;
		}
	}
}
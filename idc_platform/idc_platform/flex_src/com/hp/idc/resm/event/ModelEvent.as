package com.hp.idc.resm.event
{
	import com.hp.idc.resm.model.Model;
	
	import flash.events.Event;
	
	public class ModelEvent extends Event
	{
		public static const MODELSELECT:String = "modelselect";
		public var model:Model;
		public function ModelEvent(type:String, model:Model,bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.model = model;
		}
		
		override public function clone():Event{
			var e:ModelEvent = new ModelEvent(MODELSELECT,model);
			return e;
		}
	}
}
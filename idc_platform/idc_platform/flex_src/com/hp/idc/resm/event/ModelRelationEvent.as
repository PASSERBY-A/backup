package com.hp.idc.resm.event
{
	import com.hp.idc.resm.model.ModelRelation;
	
	import flash.events.Event;
	
	public class ModelRelationEvent extends Event
	{
		public static const MODELRELATIONADD:String = "modelrelationadd";
		public var modelRelation:ModelRelation;
		public function ModelRelationEvent(type:String,modelRelation:ModelRelation)
		{
			super(type);
			this.modelRelation = modelRelation;
		}
		
		
		override public function clone():Event{
			var e:ModelRelationEvent = new ModelRelationEvent(MODELRELATIONADD,modelRelation);
			return e;
		}
	}
}
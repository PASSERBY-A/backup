package com.hp.idc.resm.ui.actions
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.utils.getDefinitionByName;
	
	import mx.containers.ViewStack;
	import mx.core.IFlexDisplayObject;
	import mx.core.INavigatorContent;
	import mx.effects.Fade;
	import mx.events.EffectEvent;

	import mx.managers.PopUpManager;
	import mx.core.IFlexDisplayObject;

	public class PopupWindowAction implements IUiAction
	{
		private var container :DisplayObject = null;
		private var param :Object = null;
		
		public function PopupWindowAction()
		{
		}
		
		public function setTarget(target :Object, param: Object): void
		{
			container = target as DisplayObject;
			this.param = param;
		}
		
		public function doAction(p:Object,newTag:Boolean=false):void
		{
			if (p == null)
				p = param;
			var className :String = p as String;
			var obj :IFlexDisplayObject = null;
			if (className != null)
			{
				var c:Class = getDefinitionByName(className) as Class;
				obj = new c();
				PopUpManager.addPopUp(obj, container, true);
				PopUpManager.centerPopUp(obj);				
			}
		}
	}
}
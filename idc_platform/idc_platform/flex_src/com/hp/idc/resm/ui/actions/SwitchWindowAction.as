package com.hp.idc.resm.ui.actions
{
	import com.hp.idc.components.PetAlert;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.utils.getDefinitionByName;
	
	import mx.containers.ViewStack;
	import mx.core.INavigatorContent;
	import mx.effects.Fade;
	import mx.events.EffectEvent;

	public class SwitchWindowAction implements IUiAction
	{
		private var container :ViewStack = null;
		private var param :Object = null;
		
		public function SwitchWindowAction()
		{
		}
		
		public function setTarget(target :Object, param: Object): void
		{
			container = target as ViewStack;
			this.param = param;
		}
		
		public function doAction(p:Object,newTag:Boolean=false):void
		{
			if (p == null)
				p = param;
			var className :String = p as String;
			var obj :DisplayObject = null;
			if (className != null)
			{

				container.removeAll();

				if (obj == null)
				{
					var c:Class = getDefinitionByName(className) as Class;
					obj = new c();
					obj.name = className;
					container.alpha = 0;
					container.addChild(obj);
				}
			}
			if (obj != null)
			{
				container.alpha = 0;
				container.selectedChild = obj as INavigatorContent;
				var fd :Fade = new Fade();
				fd.alphaTo = 1;
				fd.target = container;
				fd.duration = 750;
				fd.play();
			}
				
		}
	}
}
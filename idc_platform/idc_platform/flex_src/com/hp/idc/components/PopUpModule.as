package com.hp.idc.components
{
	import com.hp.idc.ui.IShowModule;
	
	import flash.display.DisplayObject;
	
	import mx.containers.ViewStack;
	import mx.core.IVisualElementContainer;
	import mx.managers.PopUpManager;
	
	import spark.components.NavigatorContent;
	import spark.modules.Module;
	
	public class PopUpModule extends Module implements IShowModule
	{
		public function PopUpModule()
		{
			super();
		}
		
		protected var _createParam:Object = null;
		public function showModule(parent:Object, param:Object = null) :void
		{
			_createParam = param;
			PopUpManager.addPopUp(this, parent as DisplayObject, true);
		}
		
		public function hideModule() :void
		{
			PopUpManager.removePopUp(this);
		}
	}
}

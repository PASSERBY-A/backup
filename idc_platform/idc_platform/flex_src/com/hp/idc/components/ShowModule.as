package com.hp.idc.components
{
	import com.hp.idc.ui.IShowModule;
	
	import mx.containers.ViewStack;
	import mx.core.IVisualElementContainer;
	
	import spark.components.NavigatorContent;
	import spark.modules.Module;
	
	public class ShowModule extends Module implements IShowModule
	{
		public function ShowModule()
		{
			super();
		}
		
		private var _navigator:NavigatorContent = null;
		protected var _createParam:Object = null;
		public function showModule(parent:Object, param:Object = null) :void
		{
			_createParam = param;

			if (parent is ViewStack)
			{
				var vs:ViewStack = parent as ViewStack;
				_navigator = new NavigatorContent();
				_navigator.percentHeight = 100;
				_navigator.percentWidth = 100;
				_navigator.addElement(this);
				vs.addChild(_navigator);
				vs.selectedChild = _navigator;
			}
			else
			{
				var obj:IVisualElementContainer = parent as IVisualElementContainer;
				obj.addElement(this);
			}
		}
		
		public function hideModule() :void
		{
			if (_navigator != null)
				_navigator.parent.removeChild(_navigator);
			else
				this.parent.removeChild(this);
		}
	}
}
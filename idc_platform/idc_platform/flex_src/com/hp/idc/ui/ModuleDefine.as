package com.hp.idc.ui
{
	import mx.controls.Alert;
	import mx.core.IFlexModule;
	import mx.events.ModuleEvent;
	import mx.modules.IModuleInfo;
	import mx.modules.ModuleManager;

	public class ModuleDefine
	{
		public var name:String;
		
		public var url:String;
		
		public var module:IModuleInfo;
		
		private var status:int;
		private var _func:Function;
		
		public function ModuleDefine(name:String, url:String)
		{
			this.name = name;
			this.url = url;
			this.module = null;
			this.status = 0;
		}
		
		private function onError(event:ModuleEvent) : void
		{
			this.status = 2;
			Alert.show("加载模块" + name + "失败");
		}
		
		private function onReady(event:ModuleEvent) : void
		{
			this.status = 3;
			createInstance(_func);
		}
		
		private function createInstance(func:Function) : void
		{
			var obj:Object = module.factory.create();
			if (func != null)
				func.call(null, obj);
		}
		
		public function load(func:Function) :void
		{
			_func = func;
			if (module != null)
			{
				if (status == 1) // 加载中
					return;
				if (status == 2) // 失败
				{
					module = null; // 需要重新加载
				}
				else if (status == 3) // 成功
				{
					createInstance(func);
					return;
				}
			}
			module = ModuleManager.getModule(url);
			module.addEventListener(ModuleEvent.ERROR, onError);
			module.addEventListener(ModuleEvent.READY, onReady);
			module.load();
		}
		
	}
}

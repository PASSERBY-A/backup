package com.hp.idc.ui
{
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.ui.ModuleDefine;
	
	import spark.components.Application;
	

	public class ModuleUtil
	{
		private static var moduleList:Array = null;
		
		public static var application:Application;
		
		public static function setModuleList(list:Array) : void
		{
			moduleList = list;
		}
		
		public static function load(s :String, func:Function) :void
		{
			for each (var mm:ModuleDefine in moduleList)
			{
				if (mm.name == s)
				{
					mm.load(func);
					return;
				}
			}
			PetAlert.show("找不到要加载的模块:" + s, "错误", 4, null, null, PetAlert.ICON_ERROR);
		}
	}
}
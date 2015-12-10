package com.hp.idc.resm.ui
{
	import mx.core.IFactory;
	
	import spark.components.CheckBox;
	
	public class Test implements IFactory
	{
		public static var xx:String = "1";
		
		public function Test()
		{
		}
		
		public function newInstance():*
		{
			return new CheckBox();
		}
	}
}
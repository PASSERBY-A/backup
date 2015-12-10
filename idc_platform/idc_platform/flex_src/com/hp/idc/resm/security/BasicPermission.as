package com.hp.idc.resm.security
{
	import flash.display.DisplayObject;
	
	import mx.core.IVisualElement;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.security.BasicPermission")]
	public class BasicPermission
	{
		public var id :String; 
		public var name :String; 
		public var remark :String;
		
		public function createControl() :IVisualElement {
			return null;
		}
		
		public function get param() : String
		{
			return "";
		}
		
		public function set param(s :String) : void 
		{
			
		}
	
	}
}
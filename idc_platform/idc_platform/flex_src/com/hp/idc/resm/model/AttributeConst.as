package com.hp.idc.resm.model
{
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.AttributeConst")]
	public class AttributeConst
	{
		public var id:String;
		public var name:String;
		
		private function DO_NOT_CALL() : void
		{
			// 把类编辑进去
			var a0 :AttributeStringConst;
		}
	}
}
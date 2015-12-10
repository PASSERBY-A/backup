package com.hp.idc.resm.model
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.Code")]
	public class Code
	{
		//public var currentIndex:int;
		public var oid:int;
		public var id:String;
		public var name:String;
		public var order:int;
		public var childs:ArrayCollection;
	}
}
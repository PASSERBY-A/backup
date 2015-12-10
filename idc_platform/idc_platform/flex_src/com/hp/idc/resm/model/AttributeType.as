package com.hp.idc.resm.model
{
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.AttributeType")]
	public class AttributeType
	{
		public var id:String;
		public var name:String;
//		public var dataType:String;
//		public var dataLength:int;
//		public var javaClass:Object;
		public var argumentNames:Array;
		public var defineClass:Object;
	}
}
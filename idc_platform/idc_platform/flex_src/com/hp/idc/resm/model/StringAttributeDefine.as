package com.hp.idc.resm.model
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.StringAttributeDefine")]
	public class StringAttributeDefine extends AttributeDefine
	{
		public var codeType:int;
		public var codeId:String;
		public var codes:ArrayCollection;
		
		public static var TEXTINPUT_TYPE:int = 0;
		
		public static var SELECT_TYPE:int = 1;
		
		public static var TREE_TYPE:int = 2;
		
		public static var TEXTAREA_TYPE:int = 3;
		
	}
}
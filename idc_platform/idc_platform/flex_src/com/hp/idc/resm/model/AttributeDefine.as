// ActionScript file
package com.hp.idc.resm.model
{
	import flash.text.StaticText;
	import flash.utils.getQualifiedClassName;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.AttributeDefine")]
	public class AttributeDefine
	{
		public var id :String;
		public var name :String;
		public var remark :String;
		public var type :String;
		public var length :int;
		public var arguments :String;
		public var unitName :String;
		public var checker :String;
		public var converterClass :String;
		public var enabled :Boolean;
		public var important:Boolean;
		public var dimension:Boolean;
		
		private function DO_NOT_CALL() : void
		{
			// 把类编辑进去
			var a0 :BooleanAttributeDefine;
			var a1 :DateAttributeDefine;
			var a2 :DateTimeAttributeDefine;
			var a3 :ExpressionAttributeDefine;
			var a4 :IntegerAttributeDefine;
			var a5 :NumberAttributeDefine;
			var a6 :ReferenceAttributeDefine;
			var a7 :StringAttributeDefine;
			var a8 :TableAttributeDefine;
			var a9 :TimeAttributeDefine;
		}
	}
}

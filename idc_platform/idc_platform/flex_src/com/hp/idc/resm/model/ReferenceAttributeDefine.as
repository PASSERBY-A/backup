package com.hp.idc.resm.model
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.ReferenceAttributeDefine")]
	public class ReferenceAttributeDefine extends AttributeDefine
	{
		public var refModelId:String;
		public var res:ArrayCollection;
	}
}
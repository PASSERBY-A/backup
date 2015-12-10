package com.hp.idc.resm.model
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.TableAttributeDefine")]
	public class TableAttributeDefine extends AttributeDefine
	{
		public var primaryField:String;
		public var columns:ArrayCollection;
	}
}
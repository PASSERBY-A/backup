package com.hp.idc.resm.model
{
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.ModelAttribute")]
	public class ModelAttribute
	{
		public var modelId:String;
		public var attrId:String;
		public var inheritable:Boolean;
		public var defaultValue:String;
		public var remark:String;
		public var name:String;
		public var nullable:Boolean;
		public var define:Object;
		public var condition:String;
	}
}
package com.hp.idc.resm.model
{
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.ModelRelation")]
	public class ModelRelation
	{
		public var id:int;
		public var modelId:String;
		public var relationId:String;
		public var modelId2:String;
		public var modelName:String;
		public var modelName2:String;
		public var relationName:String;
	}
}
package com.hp.idc.resm.resource
{
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.ResourceRelation")]
	public class ResourceRelation
	{
		public var id:int;
		public var itemId:int;
		public var itemId2:int;
		public var relationId:String;
		public var remark:String;
		
		public function ResourceRelation()
		{
			
		}
	}
}
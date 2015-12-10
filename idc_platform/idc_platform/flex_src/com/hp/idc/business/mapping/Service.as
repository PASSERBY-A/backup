package com.hp.idc.business.mapping
{
	[RemoteClass(alias="com.hp.idc.business.entity.Service")]
	[Bindable]
	public class Service
	{
		public var id:int;
		
		public var name:String;
		
		public var type:int;
		
		public var description:String;
		
		public var level:int;
		
		public var parentId:int;
		
		public var effectDate:Date;
		
		public var expireDate:Date;
		
		public function Service()
		{
		}
	}
}